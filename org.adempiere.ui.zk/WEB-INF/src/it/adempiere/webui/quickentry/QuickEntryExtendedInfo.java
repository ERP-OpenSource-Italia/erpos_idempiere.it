package it.adempiere.webui.quickentry;

import java.io.Serializable;
import java.util.List;

import org.adempiere.webui.editor.WEditor;
import org.compiere.model.Lookup;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupInfo;
import org.compiere.util.DB;

import it.idempiere.base.util.STDUtils;

public class QuickEntryExtendedInfo implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2946758809358870879L;

	private static final String SQL_QUICKENTRUINFO = "SELECT AD_Window_ID, LIT_QE_AD_Window_ID, LIT_QE_ZoomAfterCreate, LIT_QE_SaveRecord "
			+ " FROM AD_Ref_Table WHERE AD_Reference_ID = ?";
	
	private int AD_Window_ID;
	private int zoomAD_Window_ID;
	private boolean zoomAfterCreate;
	private boolean saveRecord;
	private boolean hasExtendedInfo;	
	private int AD_Reference_Value_ID;
	
	public static QuickEntryExtendedInfo get(WEditor editor, Lookup lookup)
	{
		int AD_Reference_Value_ID = -1;
		
		if(editor != null && editor.getGridField() != null)
		{
			AD_Reference_Value_ID = editor.getGridField().getAD_Reference_Value_ID();
		}
		else if(lookup instanceof MLookup)
		{
			MLookupInfo info = ((MLookup) lookup).getLookupInfo();
			AD_Reference_Value_ID = info.AD_Reference_Value_ID;
		}
		
		QuickEntryExtendedInfo quExt = new QuickEntryExtendedInfo(AD_Reference_Value_ID);
		return quExt;
	}

	public QuickEntryExtendedInfo(int AD_Reference_Value_ID)
	{
		this.AD_Reference_Value_ID = AD_Reference_Value_ID;
		loadInfo();
	}
	
	protected void loadInfo()
	{
		hasExtendedInfo = false;
		
		if(AD_Reference_Value_ID > 0)
		{		
			List<Object> data = DB.getSQLValueObjectsEx(null, SQL_QUICKENTRUINFO, AD_Reference_Value_ID);
			
			if(data != null && data.size() > 0)
			{
				hasExtendedInfo = true;
				
				// AD_Window_ID -> Zoom
				
				Number zoomWinID = (Number)data.get(0);
				
				if(zoomWinID != null)
					zoomAD_Window_ID = zoomWinID.intValue();
				else
					zoomAD_Window_ID = -1;
				
				// QE_AD_Window_ID -> Quick entry
								
				Number winID = (Number)data.get(1);
				
				if(winID != null)
					AD_Window_ID = winID.intValue();
				else
					AD_Window_ID = -1;
				
				zoomAfterCreate = STDUtils.asBoolean(data.get(2));
				saveRecord = STDUtils.asBoolean(data.get(3));
				hasExtendedInfo = true;
			}
		}		
	}

	public int getAD_Window_ID() {
		return AD_Window_ID;
	}

	public boolean isZoomAfterCreate() {
		return zoomAfterCreate;
	}

	public boolean isSaveRecord() {
		return saveRecord;
	}

	public boolean hasExtendedInfo() {
		return hasExtendedInfo;
	}

	public int getZoomAD_Window_ID() {
		return zoomAD_Window_ID;
	}

}
