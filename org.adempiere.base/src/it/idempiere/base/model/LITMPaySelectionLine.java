package it.idempiere.base.model;

import java.math.BigDecimal;

import org.compiere.model.MPaySelectionLine;
import org.compiere.util.Env;

public class LITMPaySelectionLine {
	/** Column name OverUnderAmt */
	public static final String COLUMNNAME_OverUnderAmt = "OverUnderAmt";

	/**
	 * Set Over/Under Payment.
	 * 
	 * @param OverUnderAmt
	 *            Over-Payment (unallocated) or Under-Payment (partial payment)
	 *            Amount
	 */
	public static void setOverUnderAmt(MPaySelectionLine psl, BigDecimal OverUnderAmt) {
		psl.set_ValueOfColumn(COLUMNNAME_OverUnderAmt, OverUnderAmt);
	}

	/**
	 * Get Over/Under Payment.
	 * 
	 * @return Over-Payment (unallocated) or Under-Payment (partial payment)
	 *         Amount
	 */
	public static BigDecimal getOverUnderAmt(MPaySelectionLine psl) {
		BigDecimal bd = (BigDecimal) psl.get_Value(COLUMNNAME_OverUnderAmt);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

}