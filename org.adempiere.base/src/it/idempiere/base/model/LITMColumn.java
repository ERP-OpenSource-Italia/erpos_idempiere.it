package it.idempiere.base.model;

import org.compiere.model.X_AD_Column;

public class LITMColumn
{
	public static final String COLUMNNAME_AD_SearchReference_ID = "AD_SearchReference_ID";
	public static final String COLUMNNAME_AD_SearchVal_Rule_ID  = "AD_SearchVal_Rule_ID";
	
	public static void setAD_SearchReference_ID(X_AD_Column column,int AD_SearchReference_ID)
	{
		if (AD_SearchReference_ID < 1) 
			column.set_ValueOfColumn(COLUMNNAME_AD_SearchReference_ID, null);
		else 
			column.set_ValueOfColumn(COLUMNNAME_AD_SearchReference_ID, Integer.valueOf(AD_SearchReference_ID));
	}
	
	public static void setAD_SearchVal_Rule_ID(X_AD_Column column,int AD_SearchVal_Rule_ID)
	{
		if (AD_SearchVal_Rule_ID < 1) 
			column.set_ValueOfColumn(COLUMNNAME_AD_SearchVal_Rule_ID, null);
		else 
			column.set_ValueOfColumn(COLUMNNAME_AD_SearchVal_Rule_ID, Integer.valueOf(AD_SearchVal_Rule_ID));
	}
	
	public static int getAD_SearchReference_ID(X_AD_Column column)
	{
		Integer ii = (Integer)column.get_Value(COLUMNNAME_AD_SearchReference_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
	
	public static int getAD_SearchVal_Rule_ID(X_AD_Column column)
	{
		Integer ii = (Integer)column.get_Value(COLUMNNAME_AD_SearchVal_Rule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
