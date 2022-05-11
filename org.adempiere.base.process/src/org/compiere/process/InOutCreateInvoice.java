/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

import org.compiere.model.I_C_Order;
import org.compiere.model.MCurrency;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderPaySchedule;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	Create (Generate) Invoice from Shipment
 *	
 *  @author Jorg Janke
 *  @version $Id: InOutCreateInvoice.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class InOutCreateInvoice extends SvrProcess
{
	/**	Shipment					*/
	private int 	p_M_InOut_ID = 0;
	/**	Price List Version			*/
	private int		p_M_PriceList_ID = 0;
	/* Document No					*/
	private String	p_InvoiceDocumentNo = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_PriceList_ID"))
				p_M_PriceList_ID = para[i].getParameterAsInt();
			else if (name.equals("InvoiceDocumentNo"))
				p_InvoiceDocumentNo = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_M_InOut_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Create Invoice.
	 *	@return document no
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info("M_InOut_ID=" + p_M_InOut_ID 
			+ ", M_PriceList_ID=" + p_M_PriceList_ID
			+ ", InvoiceDocumentNo=" + p_InvoiceDocumentNo);
		if (p_M_InOut_ID == 0)
			throw new IllegalArgumentException("No Shipment");
		//
		MInOut ship = new MInOut (getCtx(), p_M_InOut_ID, get_TrxName());
		if (ship.get_ID() == 0)
			throw new IllegalArgumentException("Shipment not found");
		if (!MInOut.DOCSTATUS_Completed.equals(ship.getDocStatus()))
			throw new IllegalArgumentException("Shipment not completed");
		
		MInvoice invoice = new MInvoice (ship, null);
		// Should not override pricelist for RMA
		int M_PriceList_ID = 0;
		
		if (p_M_PriceList_ID != 0)
		{
			M_PriceList_ID = p_M_PriceList_ID;
		}
			
		/*If the parameter is not configured and there is an RMA assigned to the return, take the price list from the C_Order (InOut-->RMA-->RMA_Line-->InOut_Line-->OrderLine-->Order)
		 *else if the RMA is not there it is always taken from the C_Order without going through the RMA (InOut-->Order)*/
			
		/* If M_PriceList_ID parameter is configured use to generate invoice	*/
		
		if (ship.getM_RMA_ID() > 0 && M_PriceList_ID == 0)
		{
			M_PriceList_ID = DB.getSQLValue(get_TrxName(), "SELECT o.M_PRICELIST_ID "
				+ "FROM M_InOut ior "
				+ "JOIN M_RMA rma ON ior.m_rma_id = rma.m_rma_id "
				+ "JOIN M_RMALINE rmal ON rma.M_rma_ID = rmal.m_rma_id "
				+ "JOIN M_InOutline iol ON rmal.M_InoutLine_ID = iol.M_InoutLine_ID "
				+ "JOIN M_Inout io ON iol.M_Inout_ID = io.M_Inout_ID "
				+ "JOIN C_Order o ON io.C_ORder_ID = o.C_Order_ID "
				+" WHERE rma.M_RMA_ID=? "
				+ "fetch first 1 rows only ", ship.getM_RMA_ID());
			
			invoice.setC_Currency_ID(ship.getC_Currency_ID());
		}
		
		if (ship.getC_Order_ID() > 0 && M_PriceList_ID ==0)
		{
			I_C_Order order = ship.getC_Order();
			M_PriceList_ID = order.getM_PriceList_ID();
		}
		
		invoice.setM_PriceList_ID(M_PriceList_ID);
		
		if (p_InvoiceDocumentNo != null && p_InvoiceDocumentNo.length() > 0)
			invoice.setDocumentNo(p_InvoiceDocumentNo);
		invoice.saveEx();

		MInOutLine[] shipLines = ship.getLines(false);
		for (int i = 0; i < shipLines.length; i++)
		{
			MInOutLine sLine = shipLines[i];
			MInvoiceLine line = new MInvoiceLine(invoice);
			line.setShipLine(sLine);
			if (sLine.sameOrderLineUOM())
				line.setQtyEntered(sLine.getQtyEntered());
			else
				line.setQtyEntered(sLine.getMovementQty());
			line.setQtyInvoiced(sLine.getMovementQty());
			if (!line.save())
				throw new IllegalArgumentException("Cannot save Invoice Line");
		}
				
		if (invoice.getC_Order_ID() > 0) {
			MOrder order = new MOrder(getCtx(), invoice.getC_Order_ID(), get_TrxName());
			invoice.setPaymentRule(order.getPaymentRule());
			invoice.setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
			invoice.saveEx();
			invoice.load(invoice.get_TrxName()); // refresh from DB
			// copy payment schedule from order if invoice doesn't have a current payment schedule
			MOrderPaySchedule[] opss = MOrderPaySchedule.getOrderPaySchedule(getCtx(), order.getC_Order_ID(), 0, get_TrxName());
			MInvoicePaySchedule[] ipss = MInvoicePaySchedule.getInvoicePaySchedule(getCtx(), invoice.getC_Invoice_ID(), 0, get_TrxName());
			if (ipss.length == 0 && opss.length > 0) {
				BigDecimal ogt = order.getGrandTotal();
				BigDecimal igt = invoice.getGrandTotal();
				BigDecimal percent = Env.ONE;
				if (ogt.compareTo(igt) != 0)
					percent = igt.divide(ogt, 10, RoundingMode.HALF_UP);
				MCurrency cur = MCurrency.get(order.getCtx(), order.getC_Currency_ID());
				int scale = cur.getStdPrecision();
			
				for (MOrderPaySchedule ops : opss) {
					MInvoicePaySchedule ips = new MInvoicePaySchedule(getCtx(), 0, get_TrxName());
					PO.copyValues(ops, ips);
					if (percent != Env.ONE) {
						BigDecimal propDueAmt = ops.getDueAmt().multiply(percent);
						if (propDueAmt.scale() > scale)
							propDueAmt = propDueAmt.setScale(scale, RoundingMode.HALF_UP);
						ips.setDueAmt(propDueAmt);
					}
					ips.setC_Invoice_ID(invoice.getC_Invoice_ID());
					ips.setAD_Org_ID(ops.getAD_Org_ID());
					ips.setProcessing(ops.isProcessing());
					ips.setIsActive(ops.isActive());
					ips.saveEx();
				}
			}
			invoice.validatePaySchedule();
			invoice.saveEx();
		}
		
		addLog(invoice.getC_Invoice_ID(), invoice.getDateInvoiced(), invoice.getGrandTotal(), invoice.getDocumentNo(), invoice.get_Table_ID(), invoice.getC_Invoice_ID());
		
		return invoice.getDocumentNo();
	}	//	InOutCreateInvoice
	
}	//	InOutCreateInvoice
