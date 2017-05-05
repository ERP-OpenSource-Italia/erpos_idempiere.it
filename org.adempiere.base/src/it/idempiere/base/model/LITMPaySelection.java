package it.idempiere.base.model;

import java.sql.Timestamp;

import org.compiere.model.X_C_PaySelection;

public class LITMPaySelection
{
	/** Column name C_PaymentProcessor_ID */
	public static final String COLUMNNAME_C_PaymentProcessor_ID = "C_PaymentProcessor_ID";

	/** Column name LIT_PayEffectiveDate */
	public static final String COLUMNNAME_LIT_PayEffectiveDate = "LIT_PayEffectiveDate";

	/** Column name PNG_Journal_ID */
	public static final String COLUMNNAME_PNG_Journal_ID = "PNG_Journal_ID";

	/** Set Pay Effective Date.
	@param paySelection
	@param LIT_PayEffectiveDate 
	Pay Effective Date
	 */
	public static void setLIT_PayEffectiveDate(X_C_PaySelection paySelection, Timestamp date) {
		paySelection.set_ValueOfColumn(COLUMNNAME_LIT_PayEffectiveDate, date);
	}

	/** Get Pay Effective Date.
	@param paySelection
	@return Pay Effective Date
	 */
	public static Timestamp getLIT_PayEffectiveDate(X_C_PaySelection paySelection)
			throws RuntimeException {
		return (Timestamp) paySelection.get_Value(COLUMNNAME_LIT_PayEffectiveDate);
	}

	public static Timestamp getPaymentTrxDate(X_C_PaySelection paySelection) {
		Timestamp dateTrx = getLIT_PayEffectiveDate(paySelection);
		if (dateTrx == null) {
			dateTrx = paySelection.getPayDate();
		}

		return dateTrx;
	}

	/** Set Genied Journal.
	@param paySelection
	@param PNG_Journal_ID Genied Journal	  */
	public static void setPNG_Journal_ID(X_C_PaySelection paySelection, int PNG_Journal_ID) {
		if (PNG_Journal_ID < 1)
			paySelection.set_ValueOfColumn(COLUMNNAME_PNG_Journal_ID, null);
		else
			paySelection.set_ValueOfColumn(COLUMNNAME_PNG_Journal_ID,
					Integer.valueOf(PNG_Journal_ID));
	}

	/** Get Genied Journal.
	@param paySelection
	@return Genied Journal	  */
	public static int getPNG_Journal_ID(X_C_PaySelection paySelection) {
		Integer ii = (Integer) paySelection.get_Value(COLUMNNAME_PNG_Journal_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Payment Processor.
	@param paySelection
	@param C_PaymentProcessor_ID 
	Payment processor for electronic payments
	 */
	public static void setC_PaymentProcessor_ID(X_C_PaySelection paySelection,
			int C_PaymentProcessor_ID) {
		if (C_PaymentProcessor_ID < 1)
			paySelection.set_ValueOfColumn(COLUMNNAME_C_PaymentProcessor_ID, null);
		else
			paySelection.set_ValueOfColumn(COLUMNNAME_C_PaymentProcessor_ID,
					Integer.valueOf(C_PaymentProcessor_ID));
	}

	/** Get Payment Processor.
	@param paySelection
	@return Payment processor for electronic payments
	 */
	public static int getC_PaymentProcessor_ID(X_C_PaySelection paySelection) {
		Integer ii = (Integer) paySelection.get_Value(COLUMNNAME_C_PaymentProcessor_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
