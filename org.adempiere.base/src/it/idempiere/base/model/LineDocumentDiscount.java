package it.idempiere.base.model;

import java.math.BigDecimal;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.PO;
import org.compiere.util.Env;

import it.idempiere.base.util.STDUtils;

public class LineDocumentDiscount
{
	public static final String TYPE_LINES = "L"; // Sconto spalmato sulle righe
	
	/** Column name LIT_LineAmtBeforeDisc */
	public static final String COLUMNNAME_LIT_LineAmtBeforeDisc = "LIT_LineAmtBeforeDisc";

	/** Column name LIT_LineDocDiscVal */
	public static final String COLUMNNAME_LIT_LineDocDiscVal = "LIT_LineDocDiscVal";
	
	public static void setLIT_LineAmtBeforeDisc (PO model, BigDecimal amt)
	{
		if(model.get_ColumnIndex(COLUMNNAME_LIT_LineAmtBeforeDisc) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_LineAmtBeforeDisc + " column");
		
		model.set_ValueOfColumn(COLUMNNAME_LIT_LineAmtBeforeDisc, amt);
	}
	
	public static BigDecimal getLIT_LineDocDiscVal(PO po)
	{
		BigDecimal bd = STDUtils.getPORawValue(po,COLUMNNAME_LIT_LineDocDiscVal);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
	
	public static BigDecimal getLIT_LineAmtBeforeDisc(PO po)
	{
		BigDecimal bd = STDUtils.getPORawValue(po,COLUMNNAME_LIT_LineAmtBeforeDisc);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
		
	public static void setLIT_LineDocDiscVal (PO model, BigDecimal disc)
	{
		if(model.get_ColumnIndex(COLUMNNAME_LIT_LineDocDiscVal) < 0)
			throw new AdempiereException("Invalid object: no " + COLUMNNAME_LIT_LineDocDiscVal + " column");

		model.set_ValueOfColumn(COLUMNNAME_LIT_LineDocDiscVal, disc);
	}

}
