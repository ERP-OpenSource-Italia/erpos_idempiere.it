package it.idempiere.base.model;

import java.math.BigDecimal;

import org.compiere.model.X_C_CashLine;
import org.compiere.util.Env;

public class LITMCashLine 
{
	/** Column name IsPrepayment */
	public static final String COLUMNNAME_IsPrepayment = "IsPrepayment";
	
    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";
    
    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";
    
    /** Column name OverUnderAmt */
    public static final String COLUMNNAME_OverUnderAmt = "OverUnderAmt";

	/** Set Prepayment.
	@param cashLine
	@param IsPrepayment 
	The Payment/Receipt is a Prepayment
	 */
	public static void setIsPrepayment (X_C_CashLine cashLine, boolean IsPrepayment)
	{
		cashLine.set_ValueOfColumn(COLUMNNAME_IsPrepayment, Boolean.valueOf(IsPrepayment));
	}

	/** Get Prepayment.
		@param cashLine
		@return The Payment/Receipt is a Prepayment
	 */
	public static boolean isPrepayment (X_C_CashLine cashLine) 
	{
		Object oo = cashLine.get_Value(COLUMNNAME_IsPrepayment);
		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}    
	
	/** Set BPartner.
		@param cashLine
		@param C_BPartner_ID 
		BPartner
	 */
	public static void setC_BPartner_ID (X_C_CashLine cashLine, int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			cashLine.set_ValueOfColumn(COLUMNNAME_C_BPartner_ID, null);
		else 
			cashLine.set_ValueOfColumn(COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}
	
	/** Get BPartner.
		@param cashLine
		@return BPartner
	  */
	public static int getC_BPartner_ID (X_C_CashLine cashLine) 
	{
		Integer ii = (Integer)cashLine.get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
	
	/** Set Order.
		@param cashLine
		@param C_Order_ID 
		Order
	  */
	public static void setC_Order_ID (X_C_CashLine cashLine, int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			cashLine.set_ValueOfColumn(COLUMNNAME_C_Order_ID, null);
		else 
			cashLine.set_ValueOfColumn(COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@param cashLine
		@return Order
	  */
	public static int getC_Order_ID (X_C_CashLine cashLine) 
	{
		Integer ii = (Integer)cashLine.get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Over/Under Payment.
		@param cashLine
		@param OverUnderAmt 
		Over-Payment (unallocated) or Under-Payment (partial payment) Amount
	  */
	public static void setOverUnderAmt (X_C_CashLine cashLine, BigDecimal OverUnderAmt)
	{
		cashLine.set_ValueOfColumn(COLUMNNAME_OverUnderAmt, OverUnderAmt);
	}

	/** Get Over/Under Payment.
		@param cashLine
		@return Over-Payment (unallocated) or Under-Payment (partial payment) Amount
	  */
	public static BigDecimal getOverUnderAmt (X_C_CashLine cashLine) 
	{
		BigDecimal bd = (BigDecimal)cashLine.get_Value(COLUMNNAME_OverUnderAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
	
}
