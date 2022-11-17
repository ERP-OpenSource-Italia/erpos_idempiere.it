package it.idempiere.base.model;

import org.compiere.model.MSequence;

public class LITMSequence {
	
	public static final String COLUMNNAME_IsUseNewTrx = "IsUseNewTrx";

	public static void setIsUseTrxNew (MSequence seq,boolean IsUseNewTrx)
	{
		seq.set_ValueOfColumn (COLUMNNAME_IsUseNewTrx, Boolean.valueOf(IsUseNewTrx));
	}

	public static boolean isUseTrxNew (MSequence seq) 
	{
		Object oo = seq.get_Value(COLUMNNAME_IsUseNewTrx);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	
}

