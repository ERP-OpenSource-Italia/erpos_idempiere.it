package it.idempiere.base.model;

import java.math.BigDecimal;

import org.compiere.model.MInvoiceLine;
import org.compiere.util.Env;

public class LITMInvoiceLine 
{
	
	public static final String COLUMNNAME_Discount = "Discount";
	
	/** Set Discount %.
	@param Discount 
	Discount in percent
  */
	public static void setDiscount(MInvoiceLine mInvoiceLine, BigDecimal discount) 
	{
		mInvoiceLine.set_ValueOfColumn(COLUMNNAME_Discount, discount);
	}
	
	/** Get Discount %.
	@return Discount in percent
  */
	public static BigDecimal getDiscount (MInvoiceLine mInvoiceLine) 
	{
		BigDecimal bd = (BigDecimal)mInvoiceLine.get_Value(COLUMNNAME_Discount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
}
	
}

