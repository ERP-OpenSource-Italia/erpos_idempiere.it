package it.idempiere.base.model;

import org.compiere.model.X_C_DocType;
import org.compiere.model.X_C_Order;
import org.compiere.util.Util;

import it.idempiere.base.util.STDSysConfig;

public class LITMOrder 
{
	/** Column name OrderType */
	public static final String COLUMNNAME_OrderType = "OrderType";
	
	/** OrderType.
	@param Order
	@return OrderType	  */
	public static String getOrderType (X_C_Order order) 
	{
		return (String)order.get_Value(COLUMNNAME_OrderType);
	}
	
	/** Set OrderType.
	@param Order
	@param orderType */
	public static void setOrderType (X_C_Order order, String orderType)
	{

		order.set_ValueOfColumn (COLUMNNAME_OrderType, orderType);
	}
	
	public static String getDocSubTypeSO (X_C_Order order, X_C_DocType dt) 
	{
		boolean forceable = STDSysConfig.isAllowDifferentOrderType(order.getAD_Client_ID(), order.getAD_Org_ID());
		String DocSubTypeSO = null;
		if (forceable)
		{
			DocSubTypeSO = getOrderType(order);
		}
		if (forceable == false || Util.isEmpty(DocSubTypeSO))
		{
			if (dt == null)
				dt = (X_C_DocType) order.getC_DocType();
			
			DocSubTypeSO = dt.getDocSubTypeSO();
		}
		
		return DocSubTypeSO;
	}
}

