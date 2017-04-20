package it.idempiere.base.model;

import org.compiere.model.MPaySelectionCheck;

public class LITMPaySelectionCheck
{  
  /** Column name PNG_JournalLine_ID */
  public static final String COLUMNNAME_PNG_JournalLine_ID = "PNG_JournalLine_ID";
  
	public static int getPNG_JournalLine_ID (MPaySelectionCheck psc) 
	{
		Integer ii = (Integer)psc.get_Value(COLUMNNAME_PNG_JournalLine_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}
	
	public static void setPNG_JournalLine_ID (MPaySelectionCheck psc, int PNG_JournalLine_ID)
	{
		if (PNG_JournalLine_ID < 1) 
			psc.set_ValueOfColumn(COLUMNNAME_PNG_JournalLine_ID, null);
		else 
			psc.set_ValueOfColumn (COLUMNNAME_PNG_JournalLine_ID, Integer.valueOf(PNG_JournalLine_ID));
	}

	
}
