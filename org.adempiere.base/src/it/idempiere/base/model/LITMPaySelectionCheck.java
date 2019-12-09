package it.idempiere.base.model;

import org.compiere.model.X_C_PaySelectionCheck;

public class LITMPaySelectionCheck
{  
	/** Column name PNG_JournalLine_ID */
	public static final String COLUMNNAME_PNG_JournalLine_ID = "PNG_JournalLine_ID";

	/** Set Genied Journal Line.
	@param paySelectionCheck
	@param PNG_JournalLine_ID Genied Journal Line	  */
	public static void setPNG_JournalLine_ID (X_C_PaySelectionCheck paySelectionCheck, int PNG_JournalLine_ID)
	{
		if (PNG_JournalLine_ID < 1) 
			paySelectionCheck.set_ValueOfColumn(COLUMNNAME_PNG_JournalLine_ID, null);
		else 
			paySelectionCheck.set_ValueOfColumn (COLUMNNAME_PNG_JournalLine_ID, Integer.valueOf(PNG_JournalLine_ID));
	}

	/** Get Genied Journal Line.
	@param paySelectionCheck
	@return Genied Journal Line	  */
	public static int getPNG_JournalLine_ID (X_C_PaySelectionCheck paySelectionCheck) 
	{
		Integer ii = (Integer)paySelectionCheck.get_Value(COLUMNNAME_PNG_JournalLine_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
	
	/** Column name Description */
	public static final String  COLUMNNAME_Description = "Description";
	
	/** Get Description
	 * @param check
	 * @return Description
	 */
	public static String getDescription(X_C_PaySelectionCheck check)
	{
		return check.get_ValueAsString(COLUMNNAME_Description);
	}
	
	/** Set Description
	 * @param check
	 * @param Description
	 */
	public static void setDescription(X_C_PaySelectionCheck check, String Description)
	{
		check.set_ValueOfColumn(COLUMNNAME_Description, Description);
	}
}
