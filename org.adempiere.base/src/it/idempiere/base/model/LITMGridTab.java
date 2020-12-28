package it.idempiere.base.model;

import org.compiere.model.GridTab;
import org.compiere.util.DB;

import it.idempiere.base.util.STDSysConfig;

public class LITMGridTab 
{

	public static boolean isLS_OverwriteCallout(GridTab gridTab) 
	{
		String LS_OverwriteCallout = null;
		if(gridTab != null)//I campi nascosti hanno non hanno il gridTab
			LS_OverwriteCallout = DB.getSQLValueString(null, "SELECT LS_OverwriteCallout FROM AD_Tab WHERE AD_Tab_ID = ?",gridTab.getAD_Tab_ID());
		
		if(LS_OverwriteCallout != null 
				&& LS_OverwriteCallout.equals("Y"))
		{
			return true;
		}
		else if(LS_OverwriteCallout == null) 
		{
			return STDSysConfig.isLSCalloutWhenCopyRecord(); 
		}
				
		
		return false;
	}
	
}

