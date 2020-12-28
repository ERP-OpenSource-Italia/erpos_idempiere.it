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
		String freeType = getLIT_FreeOfChargeType(from);
		setLIT_FreeOfChargeType(to, freeType);
		
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
	
	/** Column name LIT_FreeOfChargeType */
	public static final String COLUMNNAME_LIT_FreeOfChargeType = "LIT_FreeOfChargeType";
	/** Omaggio = O */
	public static final String LIT_FreeOfChargeType_Omaggio = "O";
	/** Sconto Merce = M */
	public static final String LIT_FreeOfChargeType_ScontoMerce = "M";
	/** Sconto Linea = L */
	public static final String LIT_FreeOfChargeType_ScontoLinea = "L";
	
	public static final String LIT_FreeOfChargeType_NULL_CHECK = "X";
		
	//avoid AdempiereException because this field is not on every table
	//checks already done on IsFreeOfCharge
	public static void setLIT_FreeOfChargeType(PO po, String LIT_FreeOfChargeType) 
	{
		if(po.get_ColumnIndex(COLUMNNAME_LIT_FreeOfChargeType) < 0)
//			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_FreeOfChargeType + " column");
			return;
			
		po.set_ValueOfColumn(COLUMNNAME_LIT_FreeOfChargeType, LIT_FreeOfChargeType);
	}

	public static String getLIT_FreeOfChargeType(PO po) {
		if(po.get_ColumnIndex(COLUMNNAME_LIT_FreeOfChargeType) < 0)
//			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_FreeOfChargeType + " column");
			return null;
		
		return (String) po.get_Value(COLUMNNAME_LIT_FreeOfChargeType);
	}
		
}
