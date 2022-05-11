package it.idempiere.base.model;

import java.math.BigDecimal;

import org.compiere.model.X_C_PaySelectionLine;
import org.compiere.util.Env;

public class LITMPaySelectionLine {
	/** Column name OverUnderAmt */
	public static final String COLUMNNAME_OverUnderAmt = "OverUnderAmt";

	/**
	 * Set Over/Under Payment.
	 * 
	 * @param paySelectionLine
	 * @param OverUnderAmt
	 *            Over-Payment (unallocated) or Under-Payment (partial payment)
	 *            Amount
	 */
	public static void setOverUnderAmt(X_C_PaySelectionLine paySelectionLine, BigDecimal OverUnderAmt) {
		paySelectionLine.set_ValueOfColumn(COLUMNNAME_OverUnderAmt, OverUnderAmt);
	}

	/**
	 * Get Over/Under Payment.
	 * 
	 * @param paySelectionLine
	 * @return Over-Payment (unallocated) or Under-Payment (partial payment)
	 *         Amount
	 */
	public static BigDecimal getOverUnderAmt(X_C_PaySelectionLine paySelectionLine) {
		BigDecimal bd = (BigDecimal) paySelectionLine.get_Value(COLUMNNAME_OverUnderAmt);
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

}