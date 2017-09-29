package it.idempiere.base.model;

import java.math.BigDecimal;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.model.SetGetModel;
import org.compiere.util.Env;

public class FreeOfCharge
{
	 /** Column name IsFreeOfCharge */
  public static final String COLUMNNAME_IsFreeOfCharge = "IsFreeOfCharge";	
  
	/** Column name FreeOfChargeAmt */
	public static final String COLUMNNAME_FreeOfChargeAmt = "FreeOfChargeAmt";

  /**
	 * Set Free of Charge.
	 * 
	 * @param line
	 * @param IsFreeOfCharge.
	 *          Free of Charge.
	 */
	public static void setIsFreeOfCharge(PO po, boolean IsFreeOfCharge)
	{
		if(po.get_ColumnIndex(COLUMNNAME_IsFreeOfCharge) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_IsFreeOfCharge + " column");
		
		po.set_ValueOfColumn(COLUMNNAME_IsFreeOfCharge, Boolean.valueOf(IsFreeOfCharge));
	}

	/**
	 * Get Free of Charge.
	 * 
	 * @param line
	 * @return Free of Charge
	 */
	public static boolean isFreeOfCharge(PO po)
	{
		if(po.get_ColumnIndex(COLUMNNAME_IsFreeOfCharge) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_IsFreeOfCharge + " column");
		
		Object oo = po.get_Value(COLUMNNAME_IsFreeOfCharge);
		if (oo != null)
		{
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
	

	/** FreeOfChargeAmt.
	@param FreeOfChargeAmt Amt	  */
	public static void setFreeOfChargeAmt (PO po,BigDecimal LAM_FreeOfChargeAmt)
	{
		if(po.get_ColumnIndex(COLUMNNAME_FreeOfChargeAmt) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_FreeOfChargeAmt + " column");
		
		po.set_ValueOfColumn (COLUMNNAME_FreeOfChargeAmt, LAM_FreeOfChargeAmt);
	}

	/** Get FreeOfChargeAmt.
		@return FreeOfChargeAmt	  */
	public static BigDecimal getFreeOfChargeAmt (PO po) 
	{
		if(po.get_ColumnIndex(COLUMNNAME_FreeOfChargeAmt) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_FreeOfChargeAmt + " column");
		
		BigDecimal bd = (BigDecimal)po.get_Value(COLUMNNAME_FreeOfChargeAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}		
	
	/** Propagate isFreeOfCharge and return value
	 * @param from
	 * @param to
	 * @return free of charge
	 */
	public static boolean propagate(PO from, PO to)
	{
		boolean isFree = isFreeOfCharge(from);
		setIsFreeOfCharge(to, isFree);
		
		return isFree;		
	}
	
	public static void setOrderLineAsFreeOfCharge(SetGetModel mdl)
	{
		BigDecimal bdPriceList = (BigDecimal)mdl.get_AttrValue(MOrderLine.COLUMNNAME_PriceList);
		
		if(bdPriceList == null || bdPriceList.signum() == 0) // zero
		{
			BigDecimal bdPriceEntered = (BigDecimal)mdl.get_AttrValue(MOrderLine.COLUMNNAME_PriceEntered);
			mdl.set_AttrValue(MOrderLine.COLUMNNAME_PriceList, bdPriceEntered);
		}
				
		mdl.set_AttrValue(MOrderLine.COLUMNNAME_PriceEntered, Env.ZERO);
		mdl.set_AttrValue(MOrderLine.COLUMNNAME_PriceActual, Env.ZERO);
		mdl.set_AttrValue(MOrderLine.COLUMNNAME_Discount, Env.ONEHUNDRED);
		mdl.set_AttrValue(CompositeDiscount.COLUMNNAME_LIT_CompositeDisc, "100");
	}	
	
}
