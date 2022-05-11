/******************************************************************************
 * Product: FreePath Adempiere Open                                           *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2008-2009 Freepath srl. All Rights Reserved.                 *
 * Contributor(s): Silvano Trinchero www.freepath.it                          *
 *****************************************************************************/

package it.idempiere.base.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Util;

// TODO: migliorabile ?

public class BaseEnvHelper
{
	protected static 			CLogger			s_log = CLogger.getCLogger(BaseEnvHelper.class);
	
	public static final DateFormat	SQLCOMPATIBLE_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final String SYSC_ENVFROMSQL_STRICTMODE = "F3P_ENVFROMSQL_STRICTMODE";
	
	public static final String PREFIX_COL_G = "_g_";
	public static final String PREFIX_COL_C = "_c_";
	public static final int PREFIX_LENGTH = 3;
	
	public static String parseStringWithEnv(Properties ctx,String sString,int WindowNo,int TabNo)
	{
		StringBuffer			sParsed = new StringBuffer();
		
		if(sString != null && sString.length() != 0)
		{
			StringTokenizer		sTokenizer = new StringTokenizer(sString,"@");
			
			while(sTokenizer.hasMoreTokens())
			{
				sParsed.append(sTokenizer.nextToken());
				
				if(sTokenizer.hasMoreTokens()) // Se ha ancora tokens, bene, altrimenti la stringa e' finita
				{
					String sVar = sTokenizer.nextToken();
					
					// IDEMPIERE-194 Handling null context variable
					String defaultV = "NULL";
					int idx = sVar.indexOf(":");	//	or clause
					if (idx  >=  0) 
					{
						defaultV = sVar.substring(idx+1, sVar.length());
						sVar = sVar.substring(0, idx);
					}
					
					//format string
					String format = "";
					int f = sVar.indexOf('<');
					if (f > 0 && sVar.endsWith(">")) {
						format = sVar.substring(f+1, sVar.length()-1);
						sVar = sVar.substring(0, f);
					}
					
					String sVal = Env.getContext(ctx,WindowNo,TabNo,sVar);
					
					if (format != null && format.length() > 0) 
					{
						int tblIndex = format.indexOf(".");
						if ((sVar.endsWith("_ID") || tblIndex > 0)) 
						{
							String table = tblIndex > 0 ? format.substring(0, tblIndex) : sVar.substring(0, sVar.length() - 3);
							String column = tblIndex > 0 ? format.substring(tblIndex + 1) : format;
							sVal = (DB.getSQLValueString(null, 
									"select " + column + " from  " + table + " where " + table + "_id = ?", Integer.parseInt(sVal)));
						} 
						else 
						{
							MessageFormat mf = new MessageFormat(format);
							sVal = mf.format(new Object[]{sVal}); // F3P: parameter needs to be an array
						}
					}
					
					if(Util.isEmpty(sVal))
						sParsed.append(defaultV);
					else
						sParsed.append(sVal);
				}
			}
		}
		
		return sParsed.toString();
	}
	
	public static void fillFieldsFromResults(ResultSet rs,GridTab	gTab,Properties ctx) throws SQLException
	{		
		if(rs.next())
		{
			ResultSetMetaData	rsMeta = rs.getMetaData();
			int iCount = rsMeta.getColumnCount();
			
			for(int i=0;i<iCount;i++)
			{
				String sColName = rsMeta.getColumnName(i+1);						
				
				if(sColName.toLowerCase().startsWith(PREFIX_COL_C)) // Context !
				{
					Object oColValue = rs.getObject(i+1);										
					Env.setContext(ctx, gTab.getWindowNo(), sColName.substring(PREFIX_LENGTH), oColValue.toString());
				}
				if(sColName.toLowerCase().startsWith(PREFIX_COL_G)) // Global Context (non-bound to a window) !
				{
					Object oColValue = rs.getObject(i+1);
					Env.setContext(ctx,sColName.substring(PREFIX_LENGTH),oColValue.toString());
				}
				else
				{	
				    Object		oColValue = null,
											oOriginalColValue = gTab.getValue(sColName);
				    GridField	field = gTab.getField(sColName);
				    
				    if(field != null)
				    {
					    int				dtype = field.getVO().displayType;						    
					    
					    if(DisplayType.isNumeric(dtype))
					        oColValue = rs.getBigDecimal(i+1);
					    else if(DisplayType.isDate(dtype))
					        oColValue = rs.getTimestamp(i+1);
					    else if(DisplayType.YesNo == dtype)
					    {
					    	String sVal = rs.getString(i+1);
					    	if(sVal != null && sVal.equals("Y"))
					    		oColValue = new Boolean(true);
					    	else
					    		oColValue = new Boolean(false);				    		
					    }
					    else if(sColName.toLowerCase().endsWith("_id")) // to lowercase, to avoid pg-related problems
					    {
					    	int value = rs.getInt(i+1);
					    	
					    	if(value > 0)
					    	{
					    		oColValue = new Integer(value);	
					    	}
					    }
					    else
					        oColValue = rs.getObject(i+1);
					    
					    boolean bSet = true;
					    
					    if( oOriginalColValue == null)
					    {
					    	if(oColValue == null)
					    		bSet = false;
					    }
					    else if(oOriginalColValue.equals(oColValue))
					    	bSet = false;
					    	
					    if(bSet)
					    	gTab.setValue(sColName, oColValue);
				    }
				    else if(MSysConfig.getBooleanValue(SYSC_ENVFROMSQL_STRICTMODE, false, Env.getAD_Client_ID(ctx), Env.getAD_Org_ID(ctx)))
				    {
				    	throw new AdempiereException("Field " + sColName + " not found on tab");
				    }
				}						
			}										
		}
	}	
	
	public static void executeAndFill(String sQuery,GridTab gTab,Properties ctx) throws Exception
	{
		String	sSQL = parseStringWithEnv(ctx, sQuery, gTab.getWindowNo(),gTab.getTabNo());
		
		if(s_log.isLoggable(Level.INFO))
		{
			s_log.log(Level.INFO, "\n" + sSQL + "\n\n");
		}
		
		PreparedStatement	pstmt = DB.prepareStatement(sSQL, null);
		ResultSet rs = null;
		
		try
		{
			rs = pstmt.executeQuery();
			fillFieldsFromResults(rs, gTab, ctx);
		}
		catch(Exception e)
		{
			throw	new Exception("EnvHelper.executeAndFill",e);
		}
		finally
		{
			DB.close(rs,pstmt);
		}
	}
	
	private static final String SQL = "@SQL=";
	
	public static String parseValue(Properties ctx,String toParseString,int windowNo,String trxName) 
	{
		String parsedValue = toParseString;
		
		if (toParseString != null )
		{	
			if(toParseString.startsWith(SQL))
			{
				String sql = toParseString.substring(5);
				
				if (sql.equals(""))
				{
					return toParseString;
				}
				else
				{
					sql = Env.parseContext(ctx, windowNo, sql, false, false);
					PreparedStatement stmt = null;
					ResultSet rs = null;
					try
					{
						stmt = DB.prepareStatement(sql, trxName);
						rs = stmt.executeQuery();
						
						if (rs.next())
							parsedValue = rs.getString(1);
					}
					catch (SQLException e)
					{
						throw new AdempiereException(e);
					}
					finally
					{
						DB.close(rs, stmt);
						rs = null;
						stmt = null;
					}
				}
			}
			else if(toParseString.contains("@"))
			{
				parsedValue = Env.parseContext(ctx, windowNo, toParseString, false, false);
			}
		}
	
		
		return parsedValue;
	}
	
	public static String convertToEnvString(String sName,Object param, DateFormat convFormat, boolean bConvertIDs)
	{		
		if(param == null)
			return "NULL";
		
		if(param instanceof Boolean)
		{
			Boolean bParam = (Boolean)param;
			return (bParam.booleanValue())?"Y":"N";
		}
		else if(param instanceof Timestamp)
		{
			if(convFormat == null)
				convFormat = SQLCOMPATIBLE_DAY_FORMAT;
			
			return convFormat.format((Timestamp)param);
		}
		else if(param instanceof BigDecimal)
		{
			BigDecimal bdParam = (BigDecimal)param;
			
			if(bConvertIDs && sName.endsWith("_ID"))
			{
				int iVal = bdParam.intValue();
				return Integer.toString(iVal);
			}
			
			return bdParam.toPlainString();
		}
		
		return param.toString();
	}
	
	
	
	/** Copia il contesto di una finestra su una finestra destinazione.
	 *  Copia 'senza tab' sia in sorgente che in destinazione 
	 *  
	 * @param ctx contesto
	 * @param sourceWinNo
	 * @param destWinNo
	 */
	public static void copyWindowEnv(Properties ctx, int sourceWinNo, int destWinNo)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		
		String ctxPrefix = sourceWinNo+"|";
		
		// We need to clone the ctx to avoid concurrent modification exceptions
		Properties clone = new Properties();
		clone.putAll(ctx);
		
		for(Entry<Object,Object> entry:clone.entrySet())
		{
			String tag = entry.getKey().toString();
			
			if (tag.startsWith(ctxPrefix))
			{
				String potentialTag = tag.substring(ctxPrefix.length()); 
				
				if(potentialTag.indexOf('|') <= 0) // Contiene tab, non ci interessa
				{
					String value =  entry.getValue().toString();
					Env.setContext(ctx, destWinNo, potentialTag, value);
				}				
			}
		}		
	}
}
