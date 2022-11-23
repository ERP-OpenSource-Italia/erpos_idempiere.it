package it.idempiere.base.model;

import org.compiere.model.X_AD_Field;

public class LITMField
{
	public static final String COLUMNNAME_AD_SearchReference_ID = "AD_SearchReference_ID";
	public static final String COLUMNNAME_AD_SearchVal_Rule_ID  = "AD_SearchVal_Rule_ID";
	
	public static void setAD_SearchReference_ID(X_AD_Field field,int AD_SearchReference_ID)
	{
		if (AD_SearchReference_ID < 1) 
			field.set_ValueOfColumn(COLUMNNAME_AD_SearchReference_ID, null);
		else 
			field.set_ValueOfColumn(COLUMNNAME_AD_SearchReference_ID, Integer.valueOf(AD_SearchReference_ID));
	}
	
	public static void setAD_SearchVal_Rule_ID(X_AD_Field field,int AD_SearchVal_Rule_ID)
	{
		if (AD_SearchVal_Rule_ID < 1) 
			field.set_ValueOfColumn(COLUMNNAME_AD_SearchVal_Rule_ID, null);
		else 
			field.set_ValueOfColumn(COLUMNNAME_AD_SearchVal_Rule_ID, Integer.valueOf(AD_SearchVal_Rule_ID));
	}
	
	public static int getAD_SearchReference_ID(X_AD_Field field)
	{
		Integer ii = (Integer)field.get_Value(COLUMNNAME_AD_SearchReference_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
	
	public static int getAD_SearchVal_Rule_ID(X_AD_Field field)
	{
		Integer ii = (Integer)field.get_Value(COLUMNNAME_AD_SearchVal_Rule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
