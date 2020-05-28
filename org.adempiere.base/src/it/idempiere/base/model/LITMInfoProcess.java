package it.idempiere.base.model;

import org.adempiere.model.MInfoProcess;
import org.compiere.model.X_AD_InfoProcess;

public class LITMInfoProcess {
	
    /** Column name RefreshAfterProcess */
    public static final String COLUMNNAME_RefreshAfterProcess = "RefreshAfterProcess";
	
	/** Do not refresh = N */
	public static final String REFRESHAFTERPROCESS_DoNotRefresh = "N";
	/** Refresh and keep selection = U */
	public static final String REFRESHAFTERPROCESS_RefreshAndKeepSelection = "K";
	/** Full refresh = F */
	public static final String REFRESHAFTERPROCESS_FullRefresh = "F";
	
	/** Column name IsRefreshCallerRecord */
	public static final String COLUMNNAME_IsRefreshCallerRecord = "IsRefreshCallerRecord";
	
	/** Set Refresh after process execution.
	@param RefreshAfterProcess 
	Defines how to refresh list data after successful process execution
  */
	
	public static void setRefreshAfterProcess (MInfoProcess po, String RefreshAfterProcess)
	{
		po.set_ValueOfColumn(COLUMNNAME_RefreshAfterProcess, RefreshAfterProcess);
	}

	/** Get Refresh after process execution.
		@return Defines how to refresh list data after successful process execution
	  */
	public static String getRefreshAfterProcess (MInfoProcess po) 
	{
		String rap = (String)po.get_Value(COLUMNNAME_RefreshAfterProcess);
		
		if(rap == null)
			rap = REFRESHAFTERPROCESS_RefreshAndKeepSelection;
		
		return rap; 
	}

	/** Set Is Refresh Caller Record
	@param info process
	@param IsRefreshCallerRecord  */
	public static void setIsRefreshCallerRecord (X_AD_InfoProcess infoWindow,boolean IsRefreshCallerRecord)
	{
		infoWindow.set_ValueOfColumn(COLUMNNAME_IsRefreshCallerRecord, Boolean.valueOf(IsRefreshCallerRecord));
	}

	/** Is Refresh Caller Record
	@param info window
	@return IsAutoSpan
	 */
	public static boolean isRefreshCallerRecord (X_AD_InfoProcess infoWindow) 
	{
		Object oo = infoWindow.get_Value(COLUMNNAME_IsRefreshCallerRecord);

		if (oo != null) 
		{
			if (oo instanceof Boolean) 
				return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}
