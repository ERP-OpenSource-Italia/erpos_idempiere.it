package it.idempiere.base.model;

import org.compiere.util.DB;

public class LITMProcess {
	
	/** Column name AD_InfoWindow_ID */
	public static final String COLUMNNAME_AD_InfoWindow_ID = "AD_InfoWindow_ID";
	
	public static int getAD_InfoWindow_ID_DB(String trxName, int AD_Process_ID)
	{
		int AD_InfoWindow_ID = DB.getSQLValue(trxName, "SELECT AD_InfoWindow_ID FROM AD_Process WHERE AD_Process_ID = ?", AD_Process_ID);
		return AD_InfoWindow_ID;
	}
}
