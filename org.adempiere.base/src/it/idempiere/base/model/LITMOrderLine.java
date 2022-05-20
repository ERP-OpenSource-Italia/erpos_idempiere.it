package it.idempiere.base.model;

import org.compiere.model.X_C_OrderLine;

public class LITMOrderLine 
{
	/** Column name BOM_OrderLine_ID */
	public static final String	COLUMNNAME_BOM_OrderLine_ID = "BOM_OrderLine_ID";
	
	/** Column name BOM_Product_ID */
	public static final String	COLUMNNAME_BOM_Product_ID = "BOM_Product_ID";

	/** Set BOM_OrderLine_ID.
	@param orderLine
	@param BOM_OrderLine_ID 
	BOM_OrderLine_ID indicates the orderLine that originates the X_C_OrderLine after explodeBOM()
	 */
	public static void setBOM_OrderLine_ID(X_C_OrderLine orderLine,Integer BOM_OrderLine_ID)
	{
		if (BOM_OrderLine_ID < 1) 
			orderLine.set_ValueOfColumn(COLUMNNAME_BOM_OrderLine_ID, null);
		else 
			orderLine.set_ValueOfColumn(COLUMNNAME_BOM_OrderLine_ID, BOM_OrderLine_ID);
	}
	
	/** Get BOM_OrderLine_ID.
	 	@param orderLine 
		@return BOM_OrderLine_ID indicates the orderLine that originates the X_C_OrderLine after explodeBOM()
	 */
	public static int getBOM_OrderLine_ID(X_C_OrderLine orderLine) 
	{
		Integer ii = (Integer)orderLine.get_Value(COLUMNNAME_BOM_OrderLine_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
	
	/** Set BOM_Product_ID.
	@param orderLine
	@param BOM_Product_ID 
	BOM_Product_ID is the product that originates this line when exloded with explodeBOM()
	 */
	public static void setBOM_Product_ID(X_C_OrderLine orderLine,Integer BOM_Product_ID)
	{
		if (BOM_Product_ID < 1) 
			orderLine.set_ValueOfColumn(COLUMNNAME_BOM_Product_ID, null);
		else 
			orderLine.set_ValueOfColumn(COLUMNNAME_BOM_Product_ID, BOM_Product_ID);
	}

	/** Get BOM_Product_ID.
	 	@param orderLine 
		@return BOM_Product_ID is the product that originates this line when exloded with explodeBOM()
	 */
	public static Integer getBOM_Product_ID(X_C_OrderLine orderLine) 
	{
		Integer ii = (Integer)orderLine.get_Value(COLUMNNAME_BOM_Product_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}

