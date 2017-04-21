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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;


/**
 *	User overrides for field model
 *  @author Dirk Niemeyer, action42 GmbH
 *  @version $Id$
 */
public class MUserDefField extends X_AD_UserDef_Field
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522038599257589829L;
	
	private static final String COLUMN_NAME_T = "name_t",
								COLUMN_DESCRIPTION_T = "description_t",
								COLUMN_HELP_T = "help_t",
								CALLOUT_REPLACE_PREFIX = "#rep:",
								CALLOUT_SEP = ";";

	private static final String Q_USERDEFFIELD = 
		" select u.*, COALESCE(t.name,u.name) name_t, COALESCE(t.description,u.description) description_t, COALESCE(t.help,u.help) help_t" +
		" from AD_UserDef_Win w inner join AD_UserDef_Tab tb on (w.AD_UserDef_Win_ID = tb.AD_UserDef_Win_ID)" +
		"   inner join AD_UserDef_Field u on (u.AD_UserDef_Tab_ID = tb.AD_UserDef_Tab_ID) " +
		"	left outer join AD_UserDef_Field_Trl t on (u.AD_UserDef_Field_ID = t.AD_UserDef_Field_ID)" +
		" where u.AD_Field_ID = ? and u.isActive = 'Y' and tb.isActive = 'Y' and w.isActive = 'Y'" + // AD_Field_ID			
		"  and (t.AD_Language = ? or t.AD_Language IS NULL)" + // Language
		"  and (w.ad_client_id = 0 or w.ad_client_id = ?) " + // AD_Client_ID
		"  and (w.ad_org_id = 0 or w.ad_org_id = ?) " + // AD_Org_ID
		"  and (w.ad_role_id is null or w.ad_role_id = ?) " + // AD_Role_ID
		"  and (w.ad_user_id is null or w.ad_user_id = ?) " + // AD_User_ID
		" order by w.ad_user_id nulls first, w.ad_role_id nulls first, w.ad_org_id, w.ad_client_id";
	
	private static final String Q_COLUMNCALLOUT = "SELECT c.callout FROM AD_Column c inner join AD_Field f on (c.AD_Column_ID = f.AD_Column_ID) WHERE f.AD_Field_ID = ?";

	/**
	 * 	Standard constructor.
	 * 	You must implement this constructor for Adempiere Persistency
	 *	@param ctx context
	 *	@param ID the primary key ID
	 *	@param trxName transaction
	 */
	public MUserDefField (Properties ctx, int ID, String trxName)
	{
		super (ctx, ID, trxName);
	}	//	MyModelExample

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
	public MUserDefField (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MyModelExample

	/**
	 * Get matching MUserDefField related to current field and user definition for window and tab
	 * @param ctx
	 * @param AD_Field_ID
	 * @param AD_Tab_ID
	 * @param AD_Window_ID
	 * @return
	 */
	public static MUserDefField get (Properties ctx, int AD_Field_ID, int AD_Tab_ID, int AD_Window_ID )
	{
		// F3P: managed aggregation
		if(MUserDefWin.isAggregationEnabled())		
			return getAggregatedMatch(ctx, AD_Field_ID);

		MUserDefWin userdefWin = MUserDefWin.getBestMatch(ctx, AD_Window_ID);
		if (userdefWin == null)
			return null;
		MUserDefTab userdefTab = MUserDefTab.getMatch(ctx, AD_Tab_ID, userdefWin.getAD_UserDef_Win_ID());
		if (userdefTab == null)
			return null;
		
		MUserDefField retValue = null;

		StringBuilder sql = new StringBuilder("SELECT * "
				+ " FROM AD_UserDef_Field f " 
				+ " WHERE f.AD_Field_ID=? AND f.IsActive='Y' "
				+ " AND f.AD_UserDef_Tab_ID=? ");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			//	create statement
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Field_ID);
			pstmt.setInt(2, userdefTab.getAD_UserDef_Tab_ID());
			// 	get data
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MUserDefField(ctx,rs,null);
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
	 * F3P: Get a fake MUserDefField (NOT SUITABLE TO BE SAVED) for input tab and window, as an aggregate of all applicable records
	 * the best match is cached
	 * @param ctx
	 * @param window_ID
	 * @return best matching MUserDefWin
	 */
	public static MUserDefField getAggregatedMatch(Properties ctx,int AD_Field_ID)
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
				.append(AD_Field_ID).append("_")
				.append(Env.getAD_Client_ID(ctx)).append("_")
				.append(Env.getAD_Language(ctx)).append("_")
				.append(AD_Org_ID).append("_")
				.append(AD_Role_ID).append("_")
				.append(AD_User_ID)
				.toString();
		
		if (s_cacheAggregated.containsKey(key))	{
			return s_cacheAggregated.get(key);
		}
		
		PreparedStatement pstmt = DB.prepareStatement(Q_USERDEFFIELD, null);
		ResultSet		  rs = null;
		MUserDefField 	  fakeField = null;
		
		try
		{
			pstmt.setInt(1, AD_Field_ID);
			pstmt.setString(2, Env.getAD_Language(ctx));
			pstmt.setInt(3, AD_Client_ID);
			pstmt.setInt(4, AD_Org_ID);
			pstmt.setInt(5, AD_Role_ID);
			pstmt.setInt(6, AD_User_ID);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				if(fakeField == null)
				{
					fakeField = new MUserDefField(ctx, -1, null);
					
					// Initialize callout from column callout, to properly manage #rep prefixed callouts
					
					String columnCallout = DB.getSQLValueString(null, Q_COLUMNCALLOUT, AD_Field_ID);
					fakeField.setCallout(columnCallout);
				}
				
				String isTranslatable = rs.getString(COLUMNNAME_IsTranslationEnable),
					   isViewEnable = rs.getString(COLUMNNAME_IsViewEnable),
					   isElaborationEnable = rs.getString(COLUMNNAME_IsElaborationEnable);
				
				if(Util.asBoolean(isTranslatable) == true)
				{
					String	name = rs.getString(COLUMN_NAME_T),
							description = rs.getString(COLUMN_DESCRIPTION_T),
							help = rs.getString(COLUMN_HELP_T);
					
					if(name != null)
						fakeField.setName(name);
					
					if(description != null)
						fakeField.setDescription(description);
					
					if(help != null)
						fakeField.setHelp(help);
				}
				
				if(Util.asBoolean(isViewEnable))
				{
					int AD_FieldGroup_ID = rs.getInt(COLUMNNAME_AD_FieldGroup_ID),
						AD_LabelStyle_ID = rs.getInt(COLUMNNAME_AD_LabelStyle_ID),
						AD_FieldStyle_ID = rs.getInt(COLUMNNAME_AD_FieldStyle_ID);

					int seqNo = rs.getInt(COLUMNNAME_SeqNo);					
					if(rs.wasNull())
						seqNo = -1;
					
					int seqNoGrid = rs.getInt(COLUMNNAME_SeqNoGrid);					
					if(rs.wasNull())
						seqNoGrid = -1;
					
					int sortNo = rs.getInt(COLUMNNAME_SortNo);					
					if(rs.wasNull())
						sortNo = -1;
					
					int xPosition = rs.getInt(COLUMNNAME_XPosition);
					if(rs.wasNull())
						xPosition = -1;

					int colSpan = rs.getInt(COLUMNNAME_ColumnSpan);
					if(rs.wasNull())
						colSpan = -1;

					int numLines = rs.getInt(COLUMNNAME_NumLines);
					if(rs.wasNull())
						numLines = -1;
				
					int displayLength = rs.getInt(COLUMNNAME_DisplayLength);
					if(rs.wasNull())
						displayLength = -1;
												
					String isDisplayed = rs.getString(COLUMNNAME_IsDisplayed),
						   isDisplayedGrid = rs.getString(COLUMNNAME_IsDisplayedGrid),
						   isReadOnly = rs.getString(COLUMNNAME_IsReadOnly),
						   displayLogic = rs.getString(COLUMNNAME_DisplayLogic),
						   isDefaultFocus = rs.getString(COLUMNNAME_IsDefaultFocus),
						   isSameLine = rs.getString(COLUMNNAME_IsSameLine),
						   isToolbarButton = rs.getString(COLUMNNAME_IsToolbarButton);
						   
					
					if(AD_FieldGroup_ID > 0)
					{
						fakeField.setAD_FieldGroup_ID(AD_FieldGroup_ID);
					}
					
					if(AD_LabelStyle_ID > 0)
					{
						fakeField.setAD_LabelStyle_ID(AD_LabelStyle_ID);
					}
					
					if(AD_FieldStyle_ID > 0)
					{
						fakeField.setAD_FieldStyle_ID(AD_FieldStyle_ID);
					}
					
					fakeField.setSeqNo(seqNo);
					fakeField.setSeqNoGrid(seqNoGrid);
					fakeField.setSortNo(sortNo);
					fakeField.setXPosition(xPosition);
					fakeField.setColumnSpan(colSpan);
					fakeField.setNumLines(numLines);
					fakeField.setDisplayLength(displayLength);
					
					if(isDisplayed != null)
						fakeField.setIsDisplayed(isDisplayed);
					
					if(isDisplayedGrid != null)
						fakeField.setIsDisplayedGrid(isDisplayedGrid);
					
					if(isReadOnly != null)
						fakeField.setIsReadOnly(isReadOnly);
					
					if(displayLogic != null)
						fakeField.setDisplayLogic(displayLogic);
					
					if(isDefaultFocus != null)
						fakeField.setIsDefaultFocus(isDefaultFocus);

					if(isSameLine != null)
						fakeField.setIsSameLine(isSameLine);
					
					if(isToolbarButton != null)
						fakeField.setIsToolbarButton(isToolbarButton);
				}
				
				if(Util.asBoolean(isElaborationEnable))
				{
					int AD_Reference_ID = rs.getInt(COLUMNNAME_AD_Reference_ID),
						AD_ReferenceValue_ID = rs.getInt(COLUMNNAME_AD_Reference_Value_ID),
						AD_ValRule_ID = rs.getInt(COLUMNNAME_AD_Val_Rule_ID),
						seqNoSelection = rs.getInt(COLUMNNAME_SeqNoSelection);
										
					String isUpdateable = rs.getString(COLUMNNAME_IsUpdateable),
						   isAlwaysUpdateable = rs.getString(COLUMNNAME_IsAlwaysUpdateable),
						   isMandatory = rs.getString(COLUMNNAME_IsMandatory),
						   isAllowCopy = rs.getString(COLUMNNAME_IsAllowCopy),
						   defaultValue = rs.getString(COLUMNNAME_DefaultValue),
						   readOnlyLogic = rs.getString(COLUMNNAME_ReadOnlyLogic),
						   vFormat = rs.getString(COLUMNNAME_VFormat),
						   mandatoryLogc = rs.getString(COLUMNNAME_MandatoryLogic),
						   isAutocomplete = rs.getString(COLUMNNAME_IsAutocomplete),
						   isSelectionColumn = rs.getString(COLUMNNAME_IsSelectionColumn),
						   callout = rs.getString(COLUMNNAME_Callout);
					
					if(AD_Reference_ID > 0)
						fakeField.setAD_Reference_ID(AD_Reference_ID);
					
					if(AD_ReferenceValue_ID > 0)
						fakeField.setAD_Reference_ID(AD_ReferenceValue_ID);
					
					if(AD_ValRule_ID > 0)
						fakeField.setAD_Val_Rule_ID(AD_ValRule_ID);
					
					fakeField.setSeqNoSelection(seqNoSelection);
					
					if(isUpdateable != null)
						fakeField.setIsUpdateable(isUpdateable);
					
					if(isAlwaysUpdateable != null)
						fakeField.setIsAlwaysUpdateable(isAlwaysUpdateable);
					
					if(isMandatory != null)
						fakeField.setIsMandatory(isMandatory);
					
					if(isAllowCopy != null)
						fakeField.setIsAllowCopy(isAllowCopy);
					
					if(defaultValue != null)
						fakeField.setDefaultValue(defaultValue);
					
					if(readOnlyLogic != null)
						fakeField.setReadOnlyLogic(readOnlyLogic);
					
					if(vFormat != null)
						fakeField.setVFormat(vFormat);
					
					if(mandatoryLogc != null)
						fakeField.setMandatoryLogic(mandatoryLogc);
					
					if(isAutocomplete != null)
						fakeField.setIsAutocomplete(isAutocomplete);
					
					if(isSelectionColumn != null)
						fakeField.setIsSelectionColumn(isSelectionColumn);
					
					// Callouts are in append
					
					if(callout != null)
					{
						if(callout.startsWith(CALLOUT_REPLACE_PREFIX))
						{
							fakeField.setCallout(callout.substring(CALLOUT_REPLACE_PREFIX.length()));
						}
						else
						{
							StringBuilder cbCallout = new StringBuilder(fakeField.getCallout());
							
							if(cbCallout.length() > 0 && callout.startsWith(CALLOUT_SEP) == false)
							{
								cbCallout.append(';');
							}
							
							cbCallout.append(callout);
							fakeField.setCallout(cbCallout.toString());
						}						
					}					
				}
				
			}
			
			s_cacheAggregated.put(key, fakeField);
		}
		catch(Exception e)
		{
			throw new AdempiereException(e); // Should never happen
		}
		finally
		{
			DB.close(rs,pstmt);
		}
		
		return fakeField;			
	}
		
	/**	F3P: Cache of aggregated selected MUserDefField entries 					**/
	private static CCache<String,MUserDefField> s_cacheAggregated = new CCache<String,MUserDefField>(Table_Name + "_Aggregated", 3);	//  3 weights

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (is_ValueChanged("AD_Reference_ID")) {
			MField field = new MField(getCtx(), getAD_Field_ID(), get_TrxName());
			MColumn column = (MColumn) field.getAD_Column();
			if (column.isEncrypted() || field.isEncrypted() || field.getObscureType() != null) {
				log.saveError("Error", Msg.getMsg(getCtx(), "NotChangeReference"));
				return false;
			}
		}
		if (getAD_Reference_ID() <= 0) {
			setAD_Reference_Value_ID(0);
			setAD_Val_Rule_ID(0);
			setIsToolbarButton(null);
		}
		
		s_cacheAggregated.clear(); 	// F3P: Cleared cache at save
		
		return true;
	}
	
	@Override
	protected boolean beforeDelete() {
		s_cacheAggregated.clear(); 	// F3P: Cleared cache at save
		return true;
	}
		
}	//	MUserDefField
