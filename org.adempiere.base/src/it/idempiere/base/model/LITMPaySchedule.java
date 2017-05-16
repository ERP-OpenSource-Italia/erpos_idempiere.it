package it.idempiere.base.model;

import org.compiere.model.X_C_PaySchedule;

public class LITMPaySchedule 
{

	/** Column name IsDueFixed */
	public static final String COLUMNNAME_IsDueFixed = "IsDueFixed";

	/** Column name FixMonthOffset */
    public static final String COLUMNNAME_FixMonthOffset = "FixMonthOffset";


	/** Set Fixed due date.
		@param paySchedule
		@param IsDueFixed 
		Payment is due on a fixed date
	 */
	public static void setIsDueFixed (X_C_PaySchedule paySchedule, boolean IsDueFixed)
	{
		paySchedule.set_ValueOfColumn(COLUMNNAME_IsDueFixed, Boolean.valueOf(IsDueFixed));
	}

	/** Get Fixed due date.
		@param paySchedule
		@return Payment is due on a fixed date
	 */
	public static boolean isDueFixed (X_C_PaySchedule paySchedule) 
	{
		Object oo = paySchedule.get_Value(COLUMNNAME_IsDueFixed);
		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}


	/** Set Fix month offset.
		@param paySchedule
		@param FixMonthOffset 
		Number of months (0=same, 1=following)
	  */
	public static void setFixMonthOffset (X_C_PaySchedule paySchedule, int FixMonthOffset)
	{
		paySchedule.set_ValueOfColumn(COLUMNNAME_FixMonthOffset, Integer.valueOf(FixMonthOffset));
	}

	/** Get Fix month offset.
		@param paySchedule
		@return Number of months (0=same, 1=following)
	  */
	public static int getFixMonthOffset (X_C_PaySchedule paySchedule) 
	{
		Integer ii = (Integer)paySchedule.get_Value(COLUMNNAME_FixMonthOffset);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

}
