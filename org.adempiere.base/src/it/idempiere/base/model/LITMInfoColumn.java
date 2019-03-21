package it.idempiere.base.model;

import org.compiere.model.X_AD_InfoColumn;

public class LITMInfoColumn {
	
	public static final String COLUMNNAME_ColumnWidth = "ColumnWidth";
	public static final String COLUMNNAME_IsRunningValue = "IsRunningValue";
	public static final String COLUMNNAME_RunningValueSQL = "RunningValueSQL";

	/** Set ColumnWidth.
	@param ColumnWidth
	ColumnWidth for this column
  */
	public static void setColumnWidth (X_AD_InfoColumn ic, int width)
	{
		ic.set_ValueOfColumn (COLUMNNAME_ColumnWidth, Integer.valueOf(width));
	}
	
	/** Get ColumnWidth.
		@return ColumnWidth for this column
	  */
	public static int getColumnWidth (X_AD_InfoColumn ic) 
	{
		Integer ii = (Integer)ic.get_Value(COLUMNNAME_ColumnWidth);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
	
	/** Set Is Running Value
	@param info column 
	@param isRunningValue  */
	public static void setIsRunningValue (X_AD_InfoColumn infoColumn,boolean isRunning)
	{
		infoColumn.set_ValueOfColumn(COLUMNNAME_IsRunningValue, Boolean.valueOf(isRunning));
	}

	/** Is Running Value
	@param info column
	@return IsRunningValue
	 */
	public static boolean isRunningValue (X_AD_InfoColumn infoColumn) 
	{
		Object oo = infoColumn.get_Value(COLUMNNAME_IsRunningValue);

		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
	
	/** Set running value sql.
	@param Running value sql
    */
	public static void setRunningValueSQL (X_AD_InfoColumn ic, String runningValueSQL)
	{
		ic.set_ValueOfColumn (COLUMNNAME_RunningValueSQL, runningValueSQL);
	}

	/** Get running value sql.
	@return Optional short description of the record
    */
	public static String getRrunningValueSQL (X_AD_InfoColumn ic) 
	{
		return (String)ic.get_Value(COLUMNNAME_RunningValueSQL);
	}
}
