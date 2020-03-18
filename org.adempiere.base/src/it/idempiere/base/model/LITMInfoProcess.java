package it.idempiere.base.model;

import org.adempiere.model.MInfoProcess;

public class LITMInfoProcess {
	
    /** Column name RefreshAfterProcess */
    public static final String COLUMNNAME_RefreshAfterProcess = "RefreshAfterProcess";
	
	/** Do not refresh = N */
	public static final String REFRESHAFTERPROCESS_DoNotRefresh = "N";
	/** Refresh and keep selection = U */
	public static final String REFRESHAFTERPROCESS_RefreshAndKeepSelection = "K";
	/** Full refresh = F */
	public static final String REFRESHAFTERPROCESS_FullRefresh = "F";
	
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

}
