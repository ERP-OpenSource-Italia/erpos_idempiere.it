package org.adempiere.util;

import org.compiere.model.MQuery;
import org.compiere.model.PO;
import org.compiere.print.ReportEngine;

public interface IProcessUI2 extends IProcessUI
{
	public void zoom(PO po, int AD_Window_ID);	
	public void zoom (int AD_Table_ID, int Record_ID);	
	public void zoom(MQuery query);
	public void previewReport(ReportEngine re);
}
