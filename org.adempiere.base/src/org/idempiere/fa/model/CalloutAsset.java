/**
 * 
 */
package org.idempiere.fa.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.I_A_Asset;
import org.compiere.model.MAsset;
import org.compiere.model.SetGetUtil;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @category STUB for upgrading to 361
 *
 */
public class CalloutAsset extends CalloutEngine {
	
	public String location (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		Integer location = (Integer)value;
		if (location == null || location <= 0)
			return "";
		if (isCalloutActive())
			return "";
		
		//F3P from adempiere
		mTab.setValue(I_A_Asset.COLUMNNAME_M_Locator_ID, null);
		
		return "";
	}
	public String locator (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	
	{
		Integer locator = (Integer)value;
		if (locator == null || locator <= 0)
			return "";
		if (isCalloutActive())
			return "";
		
		//F3P from adempiere		
		mTab.setValue(I_A_Asset.COLUMNNAME_C_Location_ID, null);
		
		return "";
	}
	

	public String asset(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value != null && MAsset.COLUMNNAME_A_Asset_ID.equals(mField.getColumnName()))
		{
			MAsset asset = new MAsset(ctx, (Integer) value, null);
			if (asset != null)
				
			if(asset.getM_Product_ID() > 0)
				mTab.setValue(MAsset.COLUMNNAME_M_Product_ID, asset.getM_Product_ID());
			else
				mTab.setValue(MAsset.COLUMNNAME_M_Product_ID, null);
		}
		
		return "";
		
	}
	
	
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
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
			  if (A_Depreciation_Table_Header_ID != null){
				String SQL = "SELECT A_Term "
					+ "FROM A_Depreciation_Table_Header "
					+ "WHERE A_Depreciation_Table_Header_ID='"
					+A_Depreciation_Table_Header_ID
					+"'";
				
				pstmt = DB.prepareStatement(SQL, null);				// arhipac: compatibility
				rs = pstmt.executeQuery();
				if (rs.next())
				{
//					Charges - Set Context
						Env.setContext(ctx, WindowNo, "A_DEPRECIATION_MANUAL_PERIOD", rs.getString("A_Term"));					
						mTab.setValue ("A_DEPRECIATION_MANUAL_PERIOD", rs.getString("A_Term"));
		
				}
			  }
			}
			catch (SQLException e)
			{
				if (log.isLoggable(Level.INFO)) log.info("PeriodType "+ e);
				return e.getLocalizedMessage();
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
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
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				String SQL = "SELECT DepreciationType "
					+ "FROM A_Depreciation "
					+ "WHERE A_Depreciation_ID="
					+ A_Depreciation_ID;
				
				pstmt = DB.prepareStatement(SQL, null);				// arhipac: compatibility
				rs = pstmt.executeQuery();
				if (rs.next())
				{
//					Charges - Set Context
					String depType = rs.getString("DepreciationType");
					if ("TAB".equals(depType) || "MAN".equals(depType))
					{
						Env.setContext(ctx, WindowNo, "A_DEPRECIATION_MANUAL_PERIOD", "");					
						mTab.setValue ("A_Depreciation_Manual_Period", null);
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
			}
			catch (SQLException e)
			{
				if (log.isLoggable(Level.INFO)) log.info("PeriodType "+ e);
				return e.getLocalizedMessage();
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			return "";
		}	//	Period Type	

	/** ARHIPAC: TEO: BEGIN ------------------------------------------------------------------------------------------------------------------------------ */
	//F3P: from adempiere ripristinato
	public String invoiceLineProduct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (isCalloutActive()) {
			return "";
		}
		ModelValidator.modelChange_InvoiceLine(SetGetUtil.wrap(mTab),-1);
		return "";
	}//F3P:end
	/* commented by @win - no necessary code
	public String inventoryLineProduct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (isCalloutActive()) {
			return "";
		}
		ro.arhipac.adempiere.fa.ModelValidator.modelChange_InventoryLine(
				SetGetUtil.wrap(mTab),
				-1);
		return "";
	}
	*/
}
