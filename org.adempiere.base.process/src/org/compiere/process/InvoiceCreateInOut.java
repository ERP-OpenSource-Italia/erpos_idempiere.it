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
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.FillMandatoryException;
import org.adempiere.exceptions.InvoiceFullyMatchedException;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.X_M_InOut;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * Create (Generate) Shipment from Invoice
 *	
 * @author Jorg Janke
 * @version $Id: InvoiceCreateInOut.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 * 
 * @author Teo Sarca, www.arhipac.ro
 * 			<li>FR [ 1895317 ] InvoiceCreateInOut: you can create many receipts
 */
public class InvoiceCreateInOut extends SvrProcess
{
	public static final String PARAM_M_Warehouse_ID = MInOut.COLUMNNAME_M_Warehouse_ID;
	
	//F3P:
	
	protected static final String	PARAM_Complete = "Complete",
																PARAM_Document_No = MInOut.COLUMNNAME_DocumentNo;
	
	//F3P end
	
	/**	Warehouse			*/
	private int p_M_Warehouse_ID = 0;
	/** Invoice				*/
	private int p_C_Invoice_ID = 0;
	
	//F3P: 
	/** Complete */
	private boolean p_complete = true;
	
	/** Document No */
	private String p_documentNo = null;
	//F3P end
	
	/** Receipt				*/
	private MInOut m_inout = null;

	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		for (ProcessInfoParameter para : getParameter())
		{
			String name = para.getParameterName();
			if (para.getParameter() == null)
				;
			else if (name.equals(PARAM_M_Warehouse_ID))
				p_M_Warehouse_ID = para.getParameterAsInt();
			//F3P: 
			else if (name.equals(PARAM_Document_No))
				p_documentNo = (String)para.getParameter();
			else if (name.equals(PARAM_Complete))
				p_complete = para.getParameterAsBoolean();
			//F3P end
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_Invoice_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("C_Invoice_ID=" + p_C_Invoice_ID + ", M_Warehouse_ID=" + p_M_Warehouse_ID);
		if (p_C_Invoice_ID <= 0)
			throw new FillMandatoryException("C_Invoice_ID");
		if (p_M_Warehouse_ID == 0)
			throw new FillMandatoryException(PARAM_M_Warehouse_ID);
		//
		MInvoice invoice = new MInvoice (getCtx(), p_C_Invoice_ID, null);
		if (invoice.get_ID() <= 0)
			throw new AdempiereException("@NotFound@ @C_Invoice_ID@");
		if (!MInvoice.DOCSTATUS_Completed.equals(invoice.getDocStatus()))
			throw new AdempiereException("@InvoiceCreateDocNotCompleted@");
		//
		for (MInvoiceLine invoiceLine : invoice.getLines(false))
		{
			createLine(invoice, invoiceLine);
		}
		if (m_inout == null)
			throw new InvoiceFullyMatchedException();
		
		//F3P:
		
		if(p_complete)
		{
			m_inout.processIt(DocAction.ACTION_Complete);
		}
		
		//F3P end
		
		//
		return m_inout.getDocumentNo();
	}	//	doIt

	/**
	 * Create Shipment/Receipt header
	 * @param invoice
	 * @return Shipment/Receipt header
	 */
	private MInOut getCreateHeader(MInvoice invoice)
	{
		if (m_inout != null)
			return m_inout;
		m_inout = new MInOut (invoice, 0, null, p_M_Warehouse_ID);
		
		//F3P:
		if(Util.isEmpty(p_documentNo) == false)
		{
			m_inout.setDocumentNo(p_documentNo);
		}
		
		setF3P_DocDate(m_inout, invoice.getDateInvoiced());
		
		//F3P end
		m_inout.saveEx();
		return m_inout;
	}
	
	/**
	 * Create shipment/receipt line
	 * @param invoice
	 * @param invoiceLine
	 * @return shipment/receipt line
	 */
	private MInOutLine createLine(MInvoice invoice, MInvoiceLine invoiceLine)
	{
		BigDecimal qtyMatched = invoiceLine.getMatchedQty();
		BigDecimal qtyInvoiced = invoiceLine.getQtyInvoiced();
		BigDecimal qtyNotMatched = qtyInvoiced.subtract(qtyMatched);
		// If is fully matched don't create anything
		if (qtyNotMatched.signum() == 0)
		{
			return null;
		}
		MInOut inout = getCreateHeader(invoice);
		MInOutLine sLine = new MInOutLine(inout);
		sLine.setInvoiceLine(invoiceLine, 0,	//	Locator 
			invoice.isSOTrx() ? qtyNotMatched : Env.ZERO);
		sLine.setQtyEntered(qtyNotMatched);
		sLine.setMovementQty(qtyNotMatched);
		if (invoice.isCreditMemo())
		{
			sLine.setQtyEntered(sLine.getQtyEntered().negate());
			sLine.setMovementQty(sLine.getMovementQty().negate());
		}
		sLine.saveEx();
		//
		invoiceLine.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
		invoiceLine.saveEx();
		//
		return sLine;
	}
	
	/*Attenzione è un duplicato che è presente anche in it.adempiere.localization.model.LITMInOut*/
	private void setF3P_DocDate (X_M_InOut mInOut, Timestamp F3P_DocDate)
	{
		String COLUMNNAME_F3P_DocDate = "F3P_DocDate";
		mInOut.set_ValueOfColumn (COLUMNNAME_F3P_DocDate, F3P_DocDate);
	}
}	//	InvoiceCreateInOut
