/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2012 Dirk Niemeyer                                           *
 * Copyright (C) 2012 action 42 GmbH                                          *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;


/**
 *	User overrides for tab model
 *  @author Dirk Niemeyer, action 42 GmbH
 *  @version $Id$
 *  
 */
public class MUserDefTab extends X_AD_UserDef_Tab
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20120403111900L;
	
	private static final String COLUMN_NAME_T = "name_t",
								COLUMN_DESCRIPTION_T = "description_t",
								COLUMN_HELP_T = "help_t";

	private static final String Q_USERDEFTAB = 
			" select u.*, COALESCE(t.name,u.name) name_t, COALESCE(t.description,u.description) description_t, COALESCE(t.help,u.help) help_t" +
			" from AD_UserDef_Win w inner join AD_UserDef_Tab u on (w.AD_UserDef_Win_ID = u.AD_UserDef_Win_ID)" +
			"	left outer join AD_UserDef_Tab_Trl t on (u.AD_UserDef_Tab_ID = t.AD_UserDef_Tab_ID and t.AD_Language = ?)" +
			" where u.AD_Tab_ID = ? and u.isActive = 'Y' and w.isActive = 'Y'" + // AD_Tab_ID			
//			"  and (t.AD_Language = ? or t.AD_Language IS NULL)" + // Language
			"  and (w.ad_client_id = 0 or w.ad_client_id = ?) " + // AD_Client_ID
			"  and (w.ad_org_id = 0 or w.ad_org_id = ?) " + // AD_Org_ID
			"  and (w.ad_role_id is null or w.ad_role_id = ?) " + // AD_Role_ID
			"  and (w.ad_user_id is null or w.ad_user_id = ?) " + // AD_User_ID
			" order by w.ad_user_id nulls first, w.ad_role_id nulls first, w.ad_org_id, w.ad_client_id";

	/**
	 * 	Standard constructor.
	 * 	You must implement this constructor for Adempiere Persistency
	 *	@param ctx context
	 *	@param ID the primary key ID
	 *	@param trxName transaction
	 */
	public MUserDefTab (Properties ctx, int ID, String trxName)
	{
		super (ctx, ID, trxName);
	}	//	MUserDefTab

	/**
	 * 	Optional Load Constructor.
	 * 	You would use this constructor to load several business objects.
	 *  <code>
	 * 	SELECT * FROM MyModelExample WHERE ...
	 *  </code> 
	 *  @param ctx context
	 *  @param rs result set
	 *	@param trxName transaction
	 */
	public MUserDefTab (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MUserDefTab

	/**
	 * Get matching MUserDefTab related to current tab and user definition for window
	 * @param ctx
	 * @param AD_Tab_ID
	 * @param AD_UserDefWin_ID
	 * @return
	 */
	public static MUserDefTab getMatch (Properties ctx, int AD_Tab_ID, int AD_UserDefWin_ID )
	{

		MUserDefTab retValue = null;

		StringBuilder sql = new StringBuilder("SELECT * "
				+ " FROM AD_UserDef_Tab " 
				+ " WHERE AD_UserDef_Win_ID=? AND IsActive='Y' "
				+ " AND AD_Tab_ID=? ");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			//	create statement
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_UserDefWin_ID);
			pstmt.setInt(2, AD_Tab_ID);
			// 	get data
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MUserDefTab(ctx,rs,null);
			}
		}
		catch (SQLException ex)
		{
			CLogger.get().log(Level.SEVERE, sql.toString(), ex);
			return null;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; 
			pstmt = null;
		}

		return retValue;
	}
	
	/**
	 * F3P: Get a fake MUserDefTab (NOT SUITABLE TO BE SAVED) for input tab and window, as an aggregate of all applicable records
	 * the best match is cached
	 * @param ctx
	 * @param window_ID
	 * @return best matching MUserDefWin
	 */
	public static MUserDefTab getAggregatedMatch(Properties ctx,int AD_Tab_ID)
	{
		// parameters
		final int AD_Client_ID = Env.getAD_Client_ID(ctx);
		final int AD_Org_ID = Env.getAD_Org_ID(ctx);
		//final int anyOrg = 0;
		final int AD_Role_ID = Env.getAD_Role_ID(ctx);
		//final String anyRole = "NULL";
		final int AD_User_ID = Env.getAD_User_ID(ctx);
		//final String anyUser = "NULL";
		
		//  Check Cache
		String key = new StringBuilder()
				.append(AD_Tab_ID).append("_")
				.append(Env.getAD_Client_ID(ctx)).append("_")
				.append(Env.getAD_Language(ctx)).append("_")
				.append(AD_Org_ID).append("_")
				.append(AD_Role_ID).append("_")
				.append(AD_User_ID)
				.toString();
		
		if (s_cacheAggregated.containsKey(key))	{
			return s_cacheAggregated.get(key);
		}
		
		PreparedStatement pstmt = DB.prepareStatement(Q_USERDEFTAB, null);
		ResultSet		  rs = null;
		MUserDefTab 	  fakeTab = null;
		
		try
		{
			pstmt.setString(1, Env.getAD_Language(ctx));
			pstmt.setInt(2, AD_Tab_ID);
			pstmt.setInt(3, AD_Client_ID);
			pstmt.setInt(4, AD_Org_ID);
			pstmt.setInt(5, AD_Role_ID);
			pstmt.setInt(6, AD_User_ID);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				if(fakeTab == null)
					fakeTab = new MUserDefTab(ctx, -1, null);
				
				String isTranslatable = rs.getString(COLUMNNAME_IsTranslationEnable),
					   isViewEnable = rs.getString(COLUMNNAME_IsViewEnable),
					   isElaborationEnable = rs.getString(COLUMNNAME_IsElaborationEnable);
				
				if(Util.asBoolean(isTranslatable) == true)
				{
					String	name = rs.getString(COLUMN_NAME_T),
							description = rs.getString(COLUMN_DESCRIPTION_T),
							help = rs.getString(COLUMN_HELP_T);
					
					if(name != null)
						fakeTab.setName(name);
					
					if(description != null)
						fakeTab.setDescription(description);
					
					if(help != null)
						fakeTab.setHelp(help);
				}
				
				if(Util.asBoolean(isViewEnable))
				{
					int AD_CtxHelp_ID = rs.getInt(COLUMNNAME_AD_CtxHelp_ID);
					
					String isSingleRow = rs.getString(COLUMNNAME_IsSingleRow),
						   isMultiRowOnly = rs.getString(COLUMNNAME_IsMultiRowOnly),
						   displayLogic = rs.getString(COLUMNNAME_DisplayLogic),
						   whereClause = rs.getString(COLUMNNAME_WhereClause),
						   orderByClause = rs.getString(COLUMNNAME_OrderByClause),
						   readOnlyLogic = rs.getString(COLUMNNAME_ReadOnlyLogic),
						   isReadOnly = rs.getString(COLUMNNAME_IsReadOnly),
						   commitWarning = rs.getString(COLUMNNAME_CommitWarning);
					
					if(AD_CtxHelp_ID > 0)
						fakeTab.setAD_CtxHelp_ID(AD_CtxHelp_ID);
					
					if(isSingleRow != null)
						fakeTab.setIsSingleRow(isSingleRow);
					
					if(isMultiRowOnly != null)
						fakeTab.setIsMultiRowOnly(isMultiRowOnly);
					
					if(readOnlyLogic != null)
						fakeTab.setReadOnlyLogic(readOnlyLogic);
					
					if(isReadOnly != null)
						fakeTab.setIsReadOnly(isReadOnly);
					
					if(displayLogic != null)
						fakeTab.setDisplayLogic(displayLogic);
					
					if(whereClause != null)
						fakeTab.setWhereClause(whereClause);
					
					if(orderByClause != null)
						fakeTab.setOrderByClause(orderByClause);
					
					if(commitWarning != null)
						fakeTab.setCommitWarning(commitWarning);
				}
				
				if(Util.asBoolean(isElaborationEnable))
				{
					String isInsertRecord = rs.getString(COLUMNNAME_IsInsertRecord);						   
					int AD_Process_ID = rs.getInt(COLUMNNAME_AD_Process_ID);
					
					if(isInsertRecord != null)
						fakeTab.setIsInsertRecord(isInsertRecord);
					
					if(AD_Process_ID > 0)
						fakeTab.setAD_Process_ID(AD_Process_ID);					
				}
			}
			
			s_cacheAggregated.put(key, fakeTab);
		}
		catch(Exception e)
		{
			throw new AdempiereException(e); // Should never happen
		}
		finally
		{
			DB.close(rs,pstmt);
		}
		
		return fakeTab;			
	}

	/**
	 * Get matching MUserDefTab related to current tab and window 
	 * @param ctx
	 * @param AD_Tab_ID
	 * @param AD_Window_ID
	 * @return
	 */
	public static MUserDefTab get (Properties ctx, int AD_Tab_ID, int AD_Window_ID) {
		
		// F3P: managed aggregation
		
		if(MUserDefWin.isAggregationEnabled())
		{
			return getAggregatedMatch(ctx, AD_Tab_ID);
		}
		else
		{
			MUserDefWin userdefWin = MUserDefWin.getBestMatch(ctx, AD_Window_ID);
			if (userdefWin == null)
				return null;
			
			return getMatch(ctx, AD_Tab_ID, userdefWin.getAD_UserDef_Win_ID());
		}				
	}
	
	/**	F3P: Cache of aggregated selected MUserDefTab entries 					**/
	private static CCache<String,MUserDefTab> s_cacheAggregated = new CCache<String,MUserDefTab>(Table_Name + "_Aggregated", 3);	//  3 weights
	
	// F3P: backward compatibility functions
	
	/** Returns boolean equivalent of getIsSingleRow
	 * 
	 * @return true if getIsSingleRow is Y
	 */
	public boolean isSingleRow()
	{
		return Util.asBoolean(getIsSingleRow());
	}
	
	/** Returns boolean equivalent of getIsReadOnly
	 * 
	 * @return true if getIsReadOnly is Y
	 */
	public boolean isReadOnly()
	{
		return Util.asBoolean(getIsReadOnly());
	}
	
	public MUserDefField	getUserDefField(int AD_Field_ID)
	{
		Query qField = new Query(getCtx(),MUserDefField.Table_Name,"AD_UserDef_Tab_ID = ? AND AD_Field_ID = ?",get_TrxName());
		qField.setOnlyActiveRecords(true).setParameters(getAD_UserDef_Tab_ID(),AD_Field_ID);
		
		MUserDefField userDef = qField.first();
		
		return userDef;
	}
	
	// F3P: Cleared cache at save

	@Override
	protected boolean beforeSave(boolean newRecord) {
		s_cacheAggregated.clear();		
		return true;
	}

	@Override
	protected boolean beforeDelete() {
		s_cacheAggregated.clear();		
		return true;
	}
			
}	//	MUserDefTab
