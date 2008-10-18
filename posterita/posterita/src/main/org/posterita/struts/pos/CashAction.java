/**
 *  Product: Posterita Web-Based POS and Adempiere Plugin
 *  Copyright (C) 2007  Posterita Ltd
 *  This file is part of POSterita
 *  
 *  POSterita is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* Created on Sep 11, 2006 by ashley
* 
*/

/**
	@author ashley
 */

package org.posterita.struts.pos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.compiere.model.MAttachment;
import org.compiere.model.MCash;
import org.posterita.Constants;
import org.posterita.beans.CashBean;
import org.posterita.beans.CashBookBean;
import org.posterita.beans.CashLineBean;
import org.posterita.beans.CashSummaryBean;
import org.posterita.beans.CloseTillBean;
import org.posterita.beans.DateFilterBean;
import org.posterita.beans.POSHistoryBean;
import org.posterita.businesslogic.CashManager;
import org.posterita.businesslogic.POSTerminalManager;
import org.posterita.businesslogic.performanceanalysis.POSReportManager;
import org.posterita.core.TmkJSPEnv;
import org.posterita.exceptions.ApplicationException;
import org.posterita.exceptions.OperationException;
import org.posterita.form.POSHistoryForm;
import org.posterita.struts.core.DefaultForm;

public class CashAction extends POSDispatchAction
{

	public static final String INIT_GET_CASH_DETAILS_HISTORY = "initGetCashDetailsHistory";
    public ActionForward initGetCashDetailsHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        
        POSHistoryForm historyForm = new POSHistoryForm();
        POSHistoryBean bean = (POSHistoryBean)historyForm.getBean();

        int cashBookId = POSTerminalManager.getCashBookId(ctx);
        bean.setCashBookId(cashBookId);
        
        getCashDetailsHistory(mapping, historyForm, request, response);
        ArrayList<CashBookBean> cashBooks = CashManager.getCashBooks(ctx, null);
        
        request.getSession().setAttribute(Constants.ACCESSIBLE_CASHBOOKS, cashBooks);
        
        return mapping.findForward(INIT_GET_CASH_DETAILS_HISTORY);
    }
	
	public static final String GET_CASH_DETAILS_HISTORY = "getCashDetailsHistory";
    public ActionForward getCashDetailsHistory(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        DefaultForm df = (DefaultForm)form;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        
        POSHistoryBean historyBean = (POSHistoryBean)df.getBean();
        
        ArrayList<CashBean> cashList = CashManager.getCashDetails(ctx, historyBean.getCashBookId(), historyBean.getMonth(), historyBean.getYear(), null);
        
        request.getSession().setAttribute(Constants.CASH_DETAILS, cashList);
        
        return mapping.findForward(GET_CASH_DETAILS_HISTORY);
    }
    
    public static final String GET_CASHLINE_DETAILS = "getCashLineDetails";
    public ActionForward getCashLineDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        DefaultForm df = (DefaultForm)form;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        
        CashBean cashBean = (CashBean)df.getBean();
        
        CashSummaryBean cashSummaryBean = CashManager.getCashSummary(ctx, cashBean.getCashId(), null);        
        ArrayList<CashLineBean> cashLineList = CashManager.getCashLineHistory(ctx, cashBean.getCashId(), null);
        
        int cash_id = cashBean.getCashId();
        MAttachment attachment = MAttachment.get(ctx, MCash.Table_ID, cash_id);
        CloseTillBean closeTillBean = null;
        if(attachment != null)
        {
        	try 
        	{
				byte[] data = attachment.getEntryData(0);
				if(data != null)
				{
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
					Object obj = ois.readObject();
					closeTillBean = (CloseTillBean) obj;
				}
			} 
        	catch (IOException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	catch (ClassNotFoundException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        if(closeTillBean != null)
        {
        	String printData = POSReportManager.endOfTheDayHTMLReport(ctx, closeTillBean);
        	request.setAttribute(Constants.CLOSE_TILL_PRINT_DATA, printData);
        }
        
        request.setAttribute(Constants.CASH_LINE_DETAILS, cashLineList);
        request.setAttribute(Constants.CASH_SUMMARY, cashSummaryBean);
        
        return mapping.findForward(GET_CASHLINE_DETAILS);
    }
    
    public static final String GET_CASH_SUMMARY_BY_DATE = "getCashSummaryByDate";
    public ActionForward getCashSummaryByDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        DefaultForm df = (DefaultForm)form;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        
        DateFilterBean dateBean = (DateFilterBean)df.getBean();
                
        CashSummaryBean cashSummaryBean = CashManager.getCashSummary(ctx, dateBean.getFromDate(), dateBean.getToDate(), null);
        
        ArrayList<CashLineBean> cashLineList = CashManager.getCashLineHistory(ctx, dateBean.getFromDate(), dateBean.getToDate(), null);
        
        request.setAttribute(Constants.CASH_LINE_DETAILS, cashLineList);
        request.setAttribute(Constants.CASH_SUMMARY, cashSummaryBean);
        
        return mapping.findForward(GET_CASH_SUMMARY_BY_DATE);
    }
}
