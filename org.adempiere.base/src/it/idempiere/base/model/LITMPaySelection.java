package it.idempiere.base.model;

import java.sql.Timestamp;

import org.compiere.model.MPaySelection;

public class LITMPaySelection {
	/** Column name C_PaymentProcessor_ID */
	public static final String COLUMNNAME_C_PaymentProcessor_ID = "C_PaymentProcessor_ID";

	/** Column name LIT_PayEffectiveDate */
	public static final String COLUMNNAME_LIT_PayEffectiveDate = "LIT_PayEffectiveDate";

	/** Column name PNG_Journal_ID */
	public static final String COLUMNNAME_PNG_Journal_ID = "PNG_Journal_ID";

	public static Timestamp getLIT_PayEffectiveDate(MPaySelection ps)
			throws RuntimeException {
		return (Timestamp) ps.get_Value(COLUMNNAME_LIT_PayEffectiveDate);
	}

	public static void setLIT_PayEffectiveDate(MPaySelection ps, Timestamp date) {
		ps.set_ValueOfColumn(COLUMNNAME_LIT_PayEffectiveDate, date);
	}

	public static Timestamp getPaymentTrxDate(MPaySelection ps) {
		Timestamp dateTrx = LITMPaySelection.getLIT_PayEffectiveDate(ps);
		if (dateTrx == null) {
			dateTrx = ps.getPayDate();
		}

		return dateTrx;
	}

	public static int getPNG_Journal_ID(MPaySelection ps) {
		Integer ii = (Integer) ps.get_Value(COLUMNNAME_PNG_Journal_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public static void setPNG_Journal_ID(MPaySelection ps, int PNG_Journal_ID) {
		if (PNG_Journal_ID < 1)
			ps.set_ValueOfColumn(COLUMNNAME_PNG_Journal_ID, null);
		else
			ps.set_ValueOfColumn(COLUMNNAME_PNG_Journal_ID,
					Integer.valueOf(PNG_Journal_ID));
	}

	public static int getC_PaymentProcessor_ID(MPaySelection ps) {
		Integer ii = (Integer) ps.get_Value(COLUMNNAME_C_PaymentProcessor_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public static void setC_PaymentProcessor_ID(MPaySelection ps,
			int C_PaymentProcessor_ID) {
		if (C_PaymentProcessor_ID < 1)
			ps.set_ValueOfColumn(COLUMNNAME_C_PaymentProcessor_ID, null);
		else
			ps.set_ValueOfColumn(COLUMNNAME_C_PaymentProcessor_ID,
					Integer.valueOf(C_PaymentProcessor_ID));
	}
}
