package it.idempiere.base.model;

import org.compiere.model.X_AD_InfoWindow;

public class LITMInfoWindow {
	
	public static final String COLUMNNAME_SelectionColumn = "SelectionColumn";
	
	/** Column name IsAutoSpan */
	public static final String COLUMNNAME_IsAutoSpan = "IsAutoSpan";

	/** Set SelectionColumn.
	@param Selection column
	Selection column for this info window
  */
	public static void setSelectionColumn (X_AD_InfoWindow iw, String selectionColumn)
	{
		iw.set_ValueOfColumn (COLUMNNAME_SelectionColumn, selectionColumn);
	}
	
	/** Get SelectionColumn.
		@return SelectionColumn for this info window
	  */
	public static String getSelectionColumn (X_AD_InfoWindow iw) 
	{
		return (String)iw.get_Value(COLUMNNAME_SelectionColumn);
	}
	
	/** Set Is Auto Span
	@param inOut 
	@param isUpdateDocNo  */
	public static void setIsAutoSpan (X_AD_InfoWindow infoWindow,boolean isAutoSpan)
	{
		infoWindow.set_ValueOfColumn(COLUMNNAME_IsAutoSpan, Boolean.valueOf(isAutoSpan));
	}

	/** Is Auto Span
	@param inOut 
	@return IsUpdateDocNo
	 */
	public static boolean isAutoSpan (X_AD_InfoWindow infoWindow) 
	{
		Object oo = infoWindow.get_Value(COLUMNNAME_IsAutoSpan);

		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	
}
