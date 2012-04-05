/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *  ToolBar & Button Restriction
 *  @author Nicolas Micoud
 */
public class MToolBarButtonRestrict extends X_AD_ToolBarButtonRestrict
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 751989571891306735L;

	private static CLogger s_log = CLogger.getCLogger(MToolBarButtonRestrict.class);

	/**
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param AD_Note_ID id
	 *	@param trxName transaction
	 */
	public MToolBarButtonRestrict (Properties ctx, int AD_ToolBarButtonRestrict_ID, String trxName)
	{
		super (ctx, AD_ToolBarButtonRestrict_ID, trxName);
	}	//	MToolBarButtonRestrict

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MToolBarButtonRestrict(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MToolBarButtonRestrict

	/** Returns a list of restrictions to be applied according to the role, the window of the form ... **/
	public static int[] getOf (Properties ctx, int AD_Role_ID, String Action, int Action_ID, String className, String trxName)
	{
		// Action : R-Report, W-Window, X-form
		String sql = "SELECT AD_ToolBarButton_ID FROM AD_ToolBarButtonRestrict WHERE IsActive = 'Y'"
				+ " AND AD_Client_ID IN (0, ?)"
				+ " AND (AD_Role_ID IS NULL OR AD_Role_ID = ?)"		
				+ " AND (Action IS NULL OR Action=? AND (AD_Window_ID IS NULL OR (Action='W' AND AD_Window_ID=?)))"
				+ " AND AD_ToolBarButton_ID IN (SELECT AD_ToolBarButton_ID FROM AD_ToolBarButton WHERE IsActive='Y' AND Classname=?)";
		s_log.info("sql="+sql);
		
		int[] ids = DB.getIDsEx(trxName, sql, Env.getAD_Client_ID(ctx), AD_Role_ID, Action, Action_ID, className);

		return ids;
	}	//	getOf
	
	/**
	 * 	String Representation
	 *	@return	info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MToolBarButtonRestrict[")
			.append(get_ID()).append(",AD_ToolBarButtonRestrict_ID=").append(getAD_ToolBarButtonRestrict_ID())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MToolBarButtonRestrict
