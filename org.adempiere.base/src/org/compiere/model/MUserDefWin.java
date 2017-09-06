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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 *	User overrides for window model
 *  @author Dirk Niemeyer, action42 GmbH
 *  @version $Id$
 *  
 */
public class MUserDefWin extends X_AD_UserDef_Win
{
	public static final String ENABLE_AGGREGATED_USERDEF = "USERCUSTOMIZATION_ENABLE_AGGREGATION";
	
	// F3P: query to obtain window customization in proper order
	
	private static final String COLUMN_NAME_T = "name_t",
								COLUMN_DESCRIPTION_T = "description_t",
								COLUMN_HELP_T = "help_t";
	
	private static final String Q_USERDEFWIN = 
			" select u.*, COALESCE(t.name,u.name) name_t, COALESCE(t.description,u.description) description_t, COALESCE(t.help,u.help) help_t" +
	        " from AD_UserDef_Win u left outer join AD_UserDef_Win_Trl t on (u.AD_UserDef_Win_ID = t.AD_UserDef_Win_ID)" +
			" where u.ad_window_id = ? and u.isActive = 'Y'" + // AD_Window_ID
	        "  and (t.AD_Language = ? or t.AD_Language IS NULL)" + // Language
			"  and (u.ad_client_id = 0 or u.ad_client_id = ?) " + // AD_Client_ID
			"  and (u.ad_org_id = 0 or u.ad_org_id = ?) " + // AD_Org_ID
			"	and (u.ad_role_id is null or u.ad_role_id = ?) " + // AD_Role_ID
			"	and (u.ad_user_id is null or u.ad_user_id = ?) " + // AD_User_ID
			" order by u.ad_user_id nulls first, u.ad_role_id nulls first, u.ad_org_id, u.ad_client_id";
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5775251886672840324L;

	private volatile static List<MUserDefWin> m_fullList = null;

	/**
	 * 	Standard constructor.
	 * 	You must implement this constructor for Adempiere Persistency
	 *	@param ctx context
	 *	@param ID the primary key ID
	 *	@param trxName transaction
	 */
	public MUserDefWin (Properties ctx, int ID, String trxName)
	{
		super (ctx, ID, trxName);
	}	//	MUserDefWin

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
	public MUserDefWin (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MUserDefWin

	/**
	 *  Get all MUserDefWin entries related to window
	 * 	@param ctx context
	 *  @param window_ID window
	 *  @return Array of MUserDefWin for window
	 */
	private static MUserDefWin[] getAll (Properties ctx, int window_ID )
	{
		if (m_fullList == null) {
			m_fullList = new Query(ctx, MUserDefWin.Table_Name, "IsActive='Y'", null).list();
		}
		
		if (m_fullList.size() == 0) {
			return null;
		}

		List<MUserDefWin> list = new ArrayList<MUserDefWin>();
		
		for (MUserDefWin udw : m_fullList) {
			if (udw.getAD_Window_ID() == window_ID
				&& udw.getAD_Client_ID() == Env.getAD_Client_ID(ctx)
				&& (udw.getAD_Language() == null || udw.getAD_Language().equals(Env.getAD_Language(ctx)))
				) {
				list.add(udw);
			}
		}

		if (list.size() == 0)
			return null;

		return list.toArray(new MUserDefWin[list.size()]);
	}
	
	/**
	 * Get best matching MUserDefWin for current window
	 * the best match is cached
	 * @param ctx
	 * @param window_ID
	 * @return best matching MUserDefWin
	 */
	public static MUserDefWin getBestMatch (Properties ctx, int window_ID)
	{
		if(isAggregationEnabled())
			return getAggregatedMatch(ctx, window_ID);
		
		// parameters
		final int AD_Org_ID = Env.getAD_Org_ID(ctx);
		//final int anyOrg = 0;
		final int AD_Role_ID = Env.getAD_Role_ID(ctx);
		//final String anyRole = "NULL";
		final int AD_User_ID = Env.getAD_User_ID(ctx);
		//final String anyUser = "NULL";
		
		//  Check Cache
		String key = new StringBuilder().append(window_ID).append("_")
				.append(Env.getAD_Client_ID(ctx)).append("_")
				.append(Env.getAD_Language(ctx)).append("_")
				.append(AD_Org_ID).append("_")
				.append(AD_Role_ID).append("_")
				.append(AD_User_ID)
				.toString();
		if (s_cache.containsKey(key))
			return s_cache.get(key);

		// candidates
		MUserDefWin[] candidates = getAll(ctx, window_ID);
		if (candidates == null) {
	    	s_cache.put(key, null);
			return null;
		}
		final int size = candidates.length;
		int[] weight = new int[size];
		
		// this user + this role + this org => weight = 7
		// this user + this role + any org  => weight = 6
		// this user + any role  + this org => weight = 5
		// this user + any role  + any org  => weight = 4
		// any user  + this role + this org => weight = 3
		// any user  + this role + any org  => weight = 2
		// any user  + any role  + this org => weight = 1
		// any user  + any role  + any org  => weight = 0
		// other user or other role or other org => weight = -1 and thus ruled out
		for (int i=0; i < size; i++)
		{
			weight[i] = 0;
			if (candidates[i].getAD_User_ID() > 0) {
				if (candidates[i].getAD_User_ID() == AD_User_ID) {
					weight[i] = weight[i] + 4;
				} else {
					weight[i] = -1;
				}
			}
			if (weight[i] > -1 && candidates[i].getAD_Role_ID() > 0) {
				if (candidates[i].getAD_Role_ID() == AD_Role_ID) {
					weight[i] = weight[i] + 2;
				} else {
					weight[i] = -1;
				}
			}
			if (weight[i] > -1 && candidates[i].getAD_Org_ID() > 0) {
				if (candidates[i].getAD_Org_ID() == AD_Org_ID) {
					weight[i] = weight[i] + 1;
				} else {
					weight[i] = -1;
				}
			}
			// prefer if related to current login language
			if (weight[i] > -1 && Env.getAD_Language(ctx).equalsIgnoreCase(candidates[i].getAD_Language())) {
				weight[i] = weight[i] + 8;
			}
			// others are implicit
		}

	    int maximum = weight[0];   // start with the first value
	    int maxindex = 0;
	    for (int j=0; j<weight.length; j++) {
	        if (weight[j] > maximum) {
	            maximum = weight[j];   // new maximum
	            maxindex = j;
	        }
	    }

	    if (weight[maxindex] > -1) {
			MUserDefWin retValue = null;
	    	retValue=candidates[maxindex];
	    	s_cache.put(key, retValue);
	    	return retValue;
	    } else {
	    	s_cache.put(key, null);
	    	return null;
	    }
	}
	
	/**
	 * F3P: Get a fake MUserDefWin (NOT SUITABLE TO BE SAVED) for input window, as an aggregate of all applicable records
	 * the best match is cached
	 * @param ctx
	 * @param window_ID
	 * @return best matching MUserDefWin
	 */
	public static MUserDefWin getAggregatedMatch (Properties ctx, int window_ID)
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
		String key = new StringBuilder().append(window_ID).append("_")
				.append(Env.getAD_Client_ID(ctx)).append("_")
				.append(Env.getAD_Language(ctx)).append("_")
				.append(AD_Org_ID).append("_")
				.append(AD_Role_ID).append("_")
				.append(AD_User_ID)
				.toString();
		
		if (s_cacheAggregated.containsKey(key))	{
			return s_cacheAggregated.get(key);
		}
		
		PreparedStatement pstmt = DB.prepareStatement(Q_USERDEFWIN, null);
		ResultSet		  rs = null;
		MUserDefWin 	  fakeWin = null;
		
		try
		{
			pstmt.setInt(1, window_ID);
			pstmt.setString(2, Env.getAD_Language(ctx));
			pstmt.setInt(3, AD_Client_ID);
			pstmt.setInt(4, AD_Org_ID);
			pstmt.setInt(5, AD_Role_ID);
			pstmt.setInt(6, AD_User_ID);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				if(fakeWin == null)
					fakeWin = new MUserDefWin(ctx, -1, null);
				
				String isTranslatable = rs.getString(COLUMNNAME_IsTranslationEnable);
				
				if(Util.asBoolean(isTranslatable) == true)
				{
					String	name = rs.getString(COLUMN_NAME_T),
							description = rs.getString(COLUMN_DESCRIPTION_T),
							help = rs.getString(COLUMN_HELP_T);
					
					if(name != null)
						fakeWin.setName(name);
					
					if(description != null)
						fakeWin.setDescription(description);
					
					if(help != null)
						fakeWin.setHelp(help);
				}
				
				String isDefault = rs.getString(COLUMNNAME_IsDefault),
					   isReadOnly = rs.getString(COLUMNNAME_IsReadOnly),
					   isUserUpdateable = rs.getString(COLUMNNAME_IsUserUpdateable);
				
				if(isDefault != null)
					fakeWin.setIsDefault(isDefault);
				
				if(isReadOnly != null)
					fakeWin.setIsReadOnly(isReadOnly);
				
				if(isUserUpdateable != null)
					fakeWin.setIsUserUpdateable(isUserUpdateable);
			}
			
			s_cacheAggregated.put(key, fakeWin);
		}
		catch(Exception e)
		{
			throw new AdempiereException(e); // Should never happen
		}
		finally
		{
			DB.close(rs,pstmt);
		}
		
		return fakeWin;		
	}
	
	public static boolean isAggregationEnabled()
	{
		return MSysConfig.getBooleanValue(ENABLE_AGGREGATED_USERDEF, false);
	}
	
	/**	Cache of selected MUserDefWin entries 					**/
	private static CCache<String,MUserDefWin> s_cache = new CCache<String,MUserDefWin>(Table_Name, 3);	//  3 weights
	
	/**	F3P: Cache of aggregated selected MUserDefWin entries 					**/
	private static CCache<String,MUserDefWin> s_cacheAggregated = new CCache<String,MUserDefWin>(Table_Name + "_Aggregated", 3);	//  3 weights

	@Override
	protected boolean beforeSave(boolean newRecord) {
		m_fullList = null;		
		s_cacheAggregated.clear(); 	// F3P: Cleared cache at save
		return true;
	}
	
	@Override
	protected boolean beforeDelete() {
		m_fullList = null;
		s_cacheAggregated.clear(); 	// F3P: Cleared cache at save
		return true;
	}
	
}	//	MUserDefWin
