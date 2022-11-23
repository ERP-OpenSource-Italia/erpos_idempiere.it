package org.adempiere.util;

import java.io.File;

import org.compiere.model.MQuery;
import org.compiere.model.PO;
import org.compiere.print.ReportEngine;

public interface IProcessUI2 extends IProcessUI
{
	public void zoom(PO po, int AD_Window_ID);	
	public void zoom (int AD_Table_ID, int Record_ID);	
	public void zoom(MQuery query);
	public void previewReport(ReportEngine re);
	public void showURL(String html);
	public void sendRedirect(String html);
	public void showPDF(File pdf, String title);
	
}
