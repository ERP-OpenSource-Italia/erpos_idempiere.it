package it.idempiere.base.model;

import org.compiere.model.X_AD_InfoColumn;

public class LITMInfoColumn {
	
	public static final String COLUMNNAME_ColumnWidth = "ColumnWidth";

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
}
