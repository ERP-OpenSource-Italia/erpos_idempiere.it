package it.idempiere.base.model;

import java.sql.Timestamp;

import org.compiere.model.X_C_Invoice;

/** Funzioni necessarie al core
 * 
 * @author strinchero
 *
 */
public class LITMInvoice {
	
	/** Column name VATLedgerDate */
	public static final String COLUMNNAME_VATLedgerDate = "VATLedgerDate";    

	/** Column name VATLedgerNo */
	public static final String COLUMNNAME_VATLedgerNo = "VATLedgerNo";
	
	/** Column name IsUpdateDocNo */
	public static final String COLUMNNAME_IsUpdateDocNo = "IsUpdateDocNo";	
	
	/** Set VAT Ledger Date.
	 * @param invoice
		@param VATLedgerDate VAT Ledger Date	  */
	public static void setVATLedgerDate (X_C_Invoice invoice, Timestamp VATLedgerDate)
	{
		invoice.set_ValueOfColumn(COLUMNNAME_VATLedgerDate, VATLedgerDate);
	}

	/** Get VAT Ledger Date.
	 * @param invoice
		@return VAT Ledger Date	  */
	public static Timestamp getVATLedgerDate (X_C_Invoice invoice) 
	{
		return (Timestamp)invoice.get_Value(COLUMNNAME_VATLedgerDate);
	}	


	/** Set VAT Ledger No.
		@param invoice
		@param VATLedgerNo VAT Ledger No	  */
	public static void setVATLedgerNo (X_C_Invoice invoice, String VATLedgerNo)
	{
		invoice.set_ValueOfColumn(COLUMNNAME_VATLedgerNo, VATLedgerNo);
	}

	/** Get VAT Ledger No.
		@param invoice
		@return VAT Ledger No	  */
	public static String getVATLedgerNo (X_C_Invoice invoice) 
	{
		return (String)invoice.get_Value(COLUMNNAME_VATLedgerNo);
	}
	
	/**
	 * Set IsUpdateDocNo.
	 * 
	 * @param invoice
	 * @param IsUpdateDocNo
	 *          IsUpdateDocNo
	 */
	public static void setIsUpdateDocNo(X_C_Invoice invoice,
			boolean isUpdateDocNo)
	{
		invoice.set_ValueOfColumn(COLUMNNAME_IsUpdateDocNo, Boolean.valueOf(isUpdateDocNo));
	}

	/**
	 * Get IsUpdateDocNo.
	 * 
	 * @param invoice
	 * @return IsUpdateDocNo
	 */
	public static boolean isUpdateDocNo(X_C_Invoice invoice)
	{
		Object oo = invoice.get_Value(COLUMNNAME_IsUpdateDocNo);

		if (oo != null)
		{
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}	

}
