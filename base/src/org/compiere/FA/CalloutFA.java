/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 *
 * Copyright (C) 2005 Robert KLEIN. robeklein@gmail.com * 
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.FA;

import java.sql.*;
import java.util.*;

import org.compiere.util.*;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

/**
 *	FA Callout.
 *
 *  @author Rob Klein
 *  @version  $Id: CalloutFA.java,v 1.0 $
 */
public class CalloutFA extends CalloutEngine
{
	
	/**
	 *	Table_Period.  Used to set the Manual Period Field.  This allows
	 *	the Spread Field to be displayed when there is a code that
	 *  has been setup as Yearly. 
	 *  The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutEngine.Table_Period</code> 
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *	@return error message or "" if OK
	 */
	public String Table_Period (Properties ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue)
		{
		Integer A_Depreciation_Table_Header_ID = (Integer)value;
			
			try
			{
				if (A_Depreciation_Table_Header_ID != null){
				String SQL = "SELECT A_Term "
					+ "FROM A_Depreciation_Table_Header "
					+ "WHERE A_Depreciation_Table_Header_ID='"
					+A_Depreciation_Table_Header_ID
					+"'";
				
				PreparedStatement pstmt = DB.prepareStatement(SQL,null);				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
//					Charges - Set Context
						Env.setContext(ctx, WindowNo, "A_DEPRECIATION_MANUAL_PERIOD", rs.getString("A_Term"));					
						mTab.setValue ("A_DEPRECIATION_MANUAL_PERIOD", rs.getString("A_Term"));
		
				}
				rs.close();
				pstmt.close();
				}
			}
			catch (SQLException e)
			{
				log.info("PeriodType "+ e);
				return e.getLocalizedMessage();
			}
			return "";
		}	//	Period Type
	
	/**
	 *	Field_Clear.  Used to set the Manual Period Field.  This allows
	 *	the Spread Field to be displayed when there is a code that
	 *  has been setup as Yearly. 
	 *  The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutEngine.Table_Period</code> 
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *	@return error message or "" if OK
	 */
	public String Field_Clear (Properties ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue)
		{
		Object A_Depreciation_ID = value;
			
			try
			{
				String SQL = "SELECT DepreciationType "
					+ "FROM A_Depreciation "
					+ "WHERE A_Depreciation_ID="
					+ A_Depreciation_ID;
				
				PreparedStatement pstmt = DB.prepareStatement(SQL,null);				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
//					Charges - Set Context
					if (rs.getString("DepreciationType")!= "TAB" | rs.getString("DepreciationType")!= "MAN")
					{
						Env.setContext(ctx, WindowNo, "A_DEPRECIATION_MANUAL_PERIOD", "");					
						//mTab.setValue ("A_Depreciation_Manual_Period", null);
						mTab.setValue ("A_Depreciation_Manual_Amount", null);
						mTab.setValue ("A_Depreciation_Table_Header_ID", null);
					}
					if (rs.getString("DepreciationType")== "TAB")
					{						
						mTab.setValue ("A_Depreciation_Manual_Amount", null);
					}
					if (rs.getString("DepreciationType")== "MAN")
					{
						mTab.setValue ("A_Depreciation_Table_Header_ID", null);
					}	
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.info("PeriodType "+ e);
				return e.getLocalizedMessage();
			}
			return "";
		}	//	Period Type	

}	//	CalloutFA
