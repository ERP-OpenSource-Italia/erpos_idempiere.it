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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_M_InOut;
import org.compiere.model.I_M_PriceList;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MInvoiceSchedule;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderPaySchedule;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;

import it.idempiere.base.model.LITMInvoice;
import it.idempiere.base.util.STDSysConfig;
import it.idempiere.base.util.STDUtils;

/**
 *	Generate Invoices
 *	
 *  @author Jorg Janke
 *  @version $Id: InvoiceGenerate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */

//--------------------------------------------------
//--------------------------------------------------
// ----> ATTENZIONE: F3P Usata quella in LIT <------
//--------------------------------------------------
//--------------------------------------------------

public class InvoiceGenerate extends SvrProcess
{
	// F3P: Custom fields and key sep
	private static final String COLUMNNAME_C_BP_BANKACCOUNT_ID = "C_BP_BankAccount_ID",
								COLUMNNAME_C_BankAccount_ID = "C_BankAccount_ID";
	
	private static final String	KEY_SEP = "|";
	
	// F3P: moved order by
	public static final String ORDER_BY = "bp.Name,o.Bill_BPartner_ID,o.Bill_Location_ID,o.C_BankAccount_ID,o.C_BP_BankAccount_ID,o.M_PriceList_ID," +
																				"o.C_ConversionType_ID,o.PaymentRule,o.C_PaymentTerm_ID,o.Bill_User_ID,o.DocumentNo";
	
	/**	Manual Selection		*/
	private boolean 	p_Selection = false;
	/**	Date Invoiced			*/
	private Timestamp	p_DateInvoiced = null;
	/**	Org						*/
	private int			p_AD_Org_ID = 0;
	/** BPartner				*/
	private int			p_C_BPartner_ID = 0;
	/** Order					*/
	private int			p_C_Order_ID = 0;
	/** Consolidate				*/
	private boolean		p_ConsolidateDocument = true;
	/** Invoice Document Action	*/
	private String		p_docAction = DocAction.ACTION_Complete;
	
	/**	The current Invoice	*/
	private MInvoice 	m_invoice = null;
	/**	The current Shipment	*/
	private MInOut	 	m_ship = null;
	/** Numner of Invoices		*/
	private int			m_created = 0;
	/**	Line Number				*/
	private int			m_line = 0;
	/**	Business Partner		*/
	private MBPartner	m_bp = null;
	/**	Minimum Amount to Invoice */
	private BigDecimal p_MinimumAmt = null;
	/**	Minimum Amount to Invoice according to Invoice Schedule */
	private BigDecimal p_MinimumAmtInvSched = null;
	/**	Per Invoice Savepoint */
	private Savepoint m_savepoint = null;
	/** Omit Shipment Comment				*/
	private boolean	p_OmitShipmentComment = false; //F3P: porting adempiere
	
	//F3P
	private boolean	p_overrideLineNo = false;
	
	private SortedMap<String, ArrayList<MInvoiceLine>> mapLine = new TreeMap<String, ArrayList<MInvoiceLine>>();
	private List<Integer> generatedInvoices = new ArrayList<Integer>(); // f3P: teniamo traccia delle invoice create
	private SimpleDateFormat	sdf = new SimpleDateFormat("yyMMdd");
	
	/**	T_Selection su InOUTs		
	 * 
	 * Nota: usiamo la T_Selection su inout come mezzo per ottenere i c_order. Questa soluzione ci consente di mantenere i due casi allineati, a prezzo della perdita di performance
	 * 
	 * */
	private boolean 	p_SelectionInOut = false;
	
	//F3P End
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
			else if (name.equals("Selection"))
				p_Selection = "Y".equals(para[i].getParameter());
			else if (name.equals("DateInvoiced"))
				p_DateInvoiced = (Timestamp)para[i].getParameter();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = para[i].getParameterAsInt();
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = "Y".equals(para[i].getParameter());
			else if (name.equals("DocAction"))
				p_docAction = (String)para[i].getParameter();
			else if (name.equals("MinimumAmt"))
				p_MinimumAmt = para[i].getParameterAsBigDecimal();
			else if (name.equals("OmitShipmentComment")) //F3P: porting adempiere
				p_OmitShipmentComment = "Y".equals(para[i].getParameter());
			else if( name.equals("SelectionInOut")) // F3P: T_Selection su inouts
				p_SelectionInOut = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		//	Login Date
		if (p_DateInvoiced == null)
			p_DateInvoiced = Env.getContextAsDate(getCtx(), "#Date");
		if (p_DateInvoiced == null)
			p_DateInvoiced = new Timestamp(System.currentTimeMillis());

		//	DocAction check
		if (!DocAction.ACTION_Complete.equals(p_docAction))
			p_docAction = DocAction.ACTION_Prepare;
		
		//F3P:renumber
		// Spostato qui per evitare l'uso di Env.getCtx, che puo creare problemi in caso di processi eseguiti dal server
		p_overrideLineNo = STDSysConfig.isOverrideGeneratedInvoiceLineNo(getAD_Client_ID()); 
	}	//	prepare

	/**
	 * 	Generate Invoices
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info("Selection=" + p_Selection + ", DateInvoiced=" + p_DateInvoiced
			+ ", AD_Org_ID=" + p_AD_Org_ID + ", C_BPartner_ID=" + p_C_BPartner_ID
			+ ", C_Order_ID=" + p_C_Order_ID + ", DocAction=" + p_docAction 
			+ ", Consolidate=" + p_ConsolidateDocument);
		//
		StringBuilder sql = null;
		if(p_SelectionInOut) // F3P: p_SelectionInOut
		{
			// T_Selection -> InOut -> InOutLine -> OrderLine -> Order
			
			sql =  new StringBuilder("SELECT DISTINCT o.*,bp.Name as BpName ") 
					.append(" FROM T_Selection t inner join M_InOut io on (t.T_Selection_ID = io.M_InOut_ID) ")
					.append(" inner join M_InOutLine iol on (io.M_InOut_ID = iol.M_InOut_ID) ")
					.append(" inner join C_OrderLine ol on (iol.C_OrderLine_ID = ol.C_OrderLine_ID) ")
					.append(" inner join C_Order o on (ol.C_Order_ID = o.C_Order_ID) ")
					.append(" inner join C_BPartner bp on (bp.C_BPartner_ID = o.Bill_BPartner_ID) ")
					.append(" where io.DocStatus IN ('CO','CL') ")
					.append(" and o.DocStatus IN ('CO','CL') " )
					.append(" and o.IsSOTrx='Y' ")
					.append(" and io.IsSOTrx='Y' ")
					.append(" and t.AD_PInstance_ID=? ")
					.append("ORDER BY ").append(ORDER_BY); // F3P: external order by		
		}
		else if (p_Selection)	//	VInvoiceGen
		{
			sql = new StringBuilder("SELECT o.* FROM C_Order o, T_Selection, C_BPartner bp  ") //F3P: porting adempiere add C_BPartner
				.append("WHERE o.DocStatus IN ('CO','CL') AND o.IsSOTrx='Y' ") // F3P: fix generation, everywhere checks for CO/CL and here just for CL
				.append("AND o.C_Order_ID = T_Selection.T_Selection_ID ")
				.append("AND bp.C_BPartner_ID = o.Bill_BPartner_ID ")//F3P: porting adempiere add C_BPartner
				.append("AND T_Selection.AD_PInstance_ID=? ")
				.append("ORDER BY ").append(ORDER_BY); // F3P: external order by	
		}
		else
		{
			sql = new StringBuilder("SELECT * FROM C_Order o, C_BPartner bp ")//F3P: porting adempiere add C_BPartner
				.append("WHERE DocStatus IN('CO','CL') AND IsSOTrx='Y'")
				.append(" AND bp.C_BPartner_ID = o.Bill_BPartner_ID ");//F3P: porting adempiere add C_BPartner
			if (p_AD_Org_ID != 0)
				sql.append(" AND o.AD_Org_ID=?");
			if (p_C_BPartner_ID != 0)
				sql.append(" AND o.Bill_BPartner_ID=?");
			if (p_C_Order_ID != 0)
				sql.append(" AND o.C_Order_ID=?");
			//
			sql.append(" AND EXISTS (SELECT * FROM C_OrderLine ol ")
					.append("WHERE o.C_Order_ID=ol.C_Order_ID AND ol.QtyOrdered<>ol.QtyInvoiced) ")
				.append("AND o.C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType ")
					.append("WHERE DocBaseType='SOO' AND DocSubTypeSO NOT IN ('ON','OB','WR')) ")
				.append("ORDER BY ").append(ORDER_BY); // F3P: external order by
		}
	//	sql += " FOR UPDATE";
		
		PreparedStatement pstmt = null;
		boolean error = false; //F3P: gestione errore
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			int index = 1;
			if (p_Selection || p_SelectionInOut) // F3P: aggiunto anche p_selectionInout  
			{
				pstmt.setInt(index, getAD_PInstance_ID());
			}
			else
			{
				if (p_AD_Org_ID != 0)
					pstmt.setInt(index++, p_AD_Org_ID);
				if (p_C_BPartner_ID != 0)
					pstmt.setInt(index++, p_C_BPartner_ID);
				if (p_C_Order_ID != 0)
					pstmt.setInt(index++, p_C_Order_ID);
			}
		}
		catch (Exception e)
		{
			//throw new AdempiereException(e);
			//F3P: gestione errore
			addLog(e.getMessage());
			error = true;
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		if(error)
			return "@Errors@"; //F3P: gestione errore
		
		return generate(pstmt);
	}	//	doIt
	
	
	/**
	 * 	Generate Shipments
	 * 	@param pstmt order query 
	 *	@return info
	 */
	private String generate (PreparedStatement pstmt)
	{
		int clientC_Currency_ID = STDUtils.getCurrencyOf(getProcessInfo().getAD_Client_ID(), p_AD_Org_ID);
		
		boolean error = false; //F3P: gestione errore
		ResultSet rs = null;
		try
		{
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				p_MinimumAmtInvSched = null;
				MOrder order = new MOrder (getCtx(), rs, get_TrxName());
				StringBuilder msgsup = new StringBuilder(Msg.getMsg(getCtx(), "Processing")).append(" ").append(order.getDocumentInfo());
				statusUpdate(msgsup.toString());
				
				//	New Invoice Location
				if (!p_ConsolidateDocument 
					|| (m_invoice != null 
						&& isNewInvoiceNeeded(order, m_invoice, clientC_Currency_ID)) ) //F3P: new check
					completeInvoice();
				boolean completeOrder = MOrder.INVOICERULE_AfterOrderDelivered.equals(order.getInvoiceRule());
				
				//	Schedule After Delivery
				boolean doInvoice = false;
				if (MOrder.INVOICERULE_CustomerScheduleAfterDelivery.equals(order.getInvoiceRule()))
				{
					m_bp = new MBPartner (getCtx(), order.getBill_BPartner_ID(), null);
					if (m_bp.getC_InvoiceSchedule_ID() == 0)
					{
						log.warning("BPartner has no Schedule - set to After Delivery");
						order.setInvoiceRule(MOrder.INVOICERULE_AfterDelivery);
						order.saveEx();
					}
					else
					{
						MInvoiceSchedule is = MInvoiceSchedule.get(getCtx(), m_bp.getC_InvoiceSchedule_ID(), get_TrxName());
						if (is.canInvoice(order.getDateOrdered(), p_DateInvoiced)) {
							if (is.isAmount() && is.getAmt() != null)
							p_MinimumAmtInvSched = is.getAmt();
							doInvoice = true;
						} else {
							continue;
						}
					}
				}	//	Schedule
				
				//	After Delivery
				if (doInvoice || MOrder.INVOICERULE_AfterDelivery.equals(order.getInvoiceRule()))
				{
					// F3P: sostituito con versione che filter per t_selection, se necessario
					// MInOut[] shipments = order.getShipments();
					MInOut[] shipments = getFilteredShipments(order);
					for (int i = 0; i < shipments.length; i++)
					{
						MInOut ship = shipments[i];
						if (!ship.isComplete()		//	ignore incomplete or reversals 
							|| ship.getDocStatus().equals(MInOut.DOCSTATUS_Reversed))
							continue;
						MInOutLine[] shipLines = ship.getLines(false);
						for (int j = 0; j < shipLines.length; j++)
						{
							MInOutLine shipLine = shipLines[j];
							if (!order.isOrderLine(shipLine.getC_OrderLine_ID()))
								continue;
							 //F3P: Filter added on shipDate: consider only inOut with date before or equals to invoiceDate
							if (!shipLine.isInvoiced() &&  
									(p_DateInvoiced.after(ship.getMovementDate()) || 
											p_DateInvoiced.compareTo(ship.getMovementDate()) == 0))
								createLine (order, ship, shipLine);
						}
						if(p_overrideLineNo == false) //F3P check p_overrideLineNo
							m_line += 1000;
					}
				}
				//	After Order Delivered, Immediate
				else if(p_SelectionInOut == false) // F3P: il caso generico non e' compatibile con la selezione inout
				{
					MOrderLine[] oLines = order.getLines(true, null);
					for (int i = 0; i < oLines.length; i++)
					{
						MOrderLine oLine = oLines[i];
						BigDecimal toInvoice = oLine.getQtyOrdered().subtract(oLine.getQtyInvoiced());
						if (toInvoice.compareTo(Env.ZERO) == 0 && oLine.getM_Product_ID() != 0)
							continue;
						
						// F3P:add check for qtyOrdered != 0, or it will result in a division by zero (and, a zero qty means the line is not really ordered...)
						if(oLine.getQtyOrdered().compareTo(Env.ZERO) == 0)
							continue;
						// F3P end
						@SuppressWarnings("unused")
						BigDecimal notInvoicedShipment = oLine.getQtyDelivered().subtract(oLine.getQtyInvoiced());
						//
						boolean fullyDelivered = oLine.getQtyOrdered().compareTo(oLine.getQtyDelivered()) == 0;
					
						//	Complete Order
						if (completeOrder && !fullyDelivered)
						{
							if (log.isLoggable(Level.FINE)) log.fine("Failed CompleteOrder - " + oLine);
							addBufferLog(0, null, null,"Failed CompleteOrder - " + oLine,oLine.get_Table_ID(),oLine.getC_OrderLine_ID()); // Elaine 2008/11/25
							completeOrder = false;
							break;
						}
						//	Immediate
						else if (MOrder.INVOICERULE_Immediate.equals(order.getInvoiceRule()))
						{
							if (log.isLoggable(Level.FINE)) log.fine("Immediate - ToInvoice=" + toInvoice + " - " + oLine);
							BigDecimal qtyEntered = toInvoice;
							//	Correct UOM for QtyEntered
							if (oLine.getQtyEntered().compareTo(oLine.getQtyOrdered()) != 0)
								qtyEntered = toInvoice
									.multiply(oLine.getQtyEntered())
									.divide(oLine.getQtyOrdered(), 12, RoundingMode.HALF_UP);
							createLine (order, oLine, toInvoice, qtyEntered);
						}
						else if (!completeOrder)
						{
							if (log.isLoggable(Level.FINE)) log.fine("Failed: " + order.getInvoiceRule() 
								+ " - ToInvoice=" + toInvoice + " - " + oLine);
							addBufferLog(0, null, null,"Failed: " + order.getInvoiceRule() 
								+ " - ToInvoice=" + toInvoice + " - " + oLine,oLine.get_Table_ID(),oLine.getC_OrderLine_ID());
						}
					}	//	for all order lines
					if (MOrder.INVOICERULE_Immediate.equals(order.getInvoiceRule()))
						if(p_overrideLineNo == false) //F3P check p_overrideLineNo
							m_line += 1000;
				}
				
				//	Complete Order successful
				if (completeOrder && MOrder.INVOICERULE_AfterOrderDelivered.equals(order.getInvoiceRule()))
				{
					// F3P: sostituito con versione che filter per t_selection, se necessario
					// MInOut[] shipments = order.getShipments();
					MInOut[] shipments = getFilteredShipments(order);
					
					for (int i = 0; i < shipments.length; i++)
					{
						MInOut ship = shipments[i];
						if (!ship.isComplete()		//	ignore incomplete or reversals 
							|| ship.getDocStatus().equals(MInOut.DOCSTATUS_Reversed))
							continue;
						MInOutLine[] shipLines = ship.getLines(false);
						for (int j = 0; j < shipLines.length; j++)
						{
							MInOutLine shipLine = shipLines[j];
							if (!order.isOrderLine(shipLine.getC_OrderLine_ID()))
								continue;
							if (!shipLine.isInvoiced())
								createLine (order, ship, shipLine);
						}
						if(p_overrideLineNo == false) //F3P check p_overrideLineNo
							m_line += 1000;
					}
				}	//	complete Order
			}	//	for all orders
		}
		catch (Exception e)
		{
			//F3P:
			//throw new AdempiereException(e);
			addLog(e.getMessage());
			error = true;
			log.log(Level.SEVERE, "", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		if(error == false) //F3P: gestione errore
		{
			orderInvoiceLines();
			completeInvoice();
		}
		
		StringBuilder msgreturn = new StringBuilder("@Created@ = ").append(m_created);
		return msgreturn.toString();
	}	//	generate
	
	
	
	/**************************************************************************
	 * 	Create Invoice Line from Order Line
	 *	@param order order
	 *	@param orderLine line
	 *	@param qtyInvoiced qty
	 *	@param qtyEntered qty
	 */
	private void createLine (MOrder order, MOrderLine orderLine, 
		BigDecimal qtyInvoiced, BigDecimal qtyEntered)
	{
		if (m_invoice == null)
		{
			try {
				if (m_savepoint != null)
					Trx.get(get_TrxName(), false).releaseSavepoint(m_savepoint);
				m_savepoint = Trx.get(get_TrxName(), false).setSavepoint(null);
			} catch (SQLException e) {
				throw new AdempiereException(e);
			}
			m_invoice = new MInvoice (order, 0, p_DateInvoiced);
			LITMInvoice.setVATLedgerDate(m_invoice, p_DateInvoiced); // LIT
			if (!m_invoice.save())
				throw new IllegalStateException("Could not create Invoice (o)");
		}
		//	
		MInvoiceLine line = new MInvoiceLine (m_invoice);
		line.setOrderLine(orderLine);
		line.setQtyInvoiced(qtyInvoiced);
		line.setQtyEntered(qtyEntered);
		
		// F3P: propagate accounting dimensione
		copyAccountingDimensions(order,m_invoice,orderLine,line);

		//F3P
		if(p_overrideLineNo)
		{
			m_line += 10;
			line.setLine(m_line);
		}
		else
		{
			line.setLine(m_line + orderLine.getLine());
		}
		//F3P end
		
		if (!line.save())
			
		try
		{
			line.saveEx();
		}
		catch (Exception e) 
		{
			addLog(e.getMessage());
			throw new IllegalStateException("Could not create Invoice Line (o)");
		}
		
		//F3P
		if(p_overrideLineNo)
		{
			ArrayList<MInvoiceLine> lstLines = null;
			
			String sKey = getMapLinesKey(m_invoice, null);
			
			if(mapLine.containsKey(sKey) == true)
			{
				lstLines = mapLine.get(sKey);
			}
			else
			{
				lstLines = new ArrayList<MInvoiceLine>();
				mapLine.put(sKey, lstLines);
			}
				
			lstLines.add(line);
		}
		//F3P end
		
		if (log.isLoggable(Level.FINE)) log.fine(line.toString());
	}	//	createLine

	/**
	 * 	Create Invoice Line from Shipment
	 *	@param order order
	 *	@param ship shipment header
	 *	@param sLine shipment line
	 */
	private void createLine (MOrder order, MInOut ship, MInOutLine sLine)
	{
		if (m_invoice == null)
		{
			try {
				if (m_savepoint != null)
					Trx.get(get_TrxName(), false).releaseSavepoint(m_savepoint);
				m_savepoint = Trx.get(get_TrxName(), false).setSavepoint(null);
			} catch (SQLException e) {
				throw new AdempiereException(e);
			}
			m_invoice = new MInvoice (order, 0, p_DateInvoiced);
			LITMInvoice.setVATLedgerDate(m_invoice, p_DateInvoiced); // LIT
			if (!m_invoice.save())
				throw new IllegalStateException("Could not create Invoice (s)");
		}
		//	Create Shipment Comment Line
		
		//F3P insert Shipment Comment Line once
		boolean bCreateLine = true;
		ArrayList<MInvoiceLine> lstLines = null;
		
		if(p_overrideLineNo)
		{
			String sKey = getMapLinesKey(m_invoice, ship);
			//insert Shipment Comment Line once
			if(mapLine.containsKey(sKey) == true)
			{
				bCreateLine = false;
				lstLines = mapLine.get(sKey);
			}
			else
			{
				bCreateLine = true;
				lstLines = new ArrayList<MInvoiceLine>();
				mapLine.put(sKey, lstLines);
			}
		}
		
		if(bCreateLine)
		{
		//F3P end
		
			if (!p_OmitShipmentComment && (m_ship == null 
					|| m_ship.getM_InOut_ID() != ship.getM_InOut_ID()))
			{
				MDocType dt = MDocType.get(getCtx(), ship.getC_DocType_ID());
				if (m_bp == null || m_bp.getC_BPartner_ID() != ship.getC_BPartner_ID())
					m_bp = new MBPartner (getCtx(), ship.getC_BPartner_ID(), get_TrxName());
				
				//	Reference: Delivery: 12345 - 12.12.12
				MClient client = MClient.get(getCtx(), order.getAD_Client_ID ());
				String AD_Language = client.getAD_Language();
				if (client.isMultiLingualDocument() && m_bp.getAD_Language() != null)
					AD_Language = m_bp.getAD_Language();
				if (AD_Language == null)
					AD_Language = Language.getBaseAD_Language();
				java.text.SimpleDateFormat format = DisplayType.getDateFormat 
					(DisplayType.Date, Language.getLanguage(AD_Language));
				StringBuilder reference = new StringBuilder().append(dt.getPrintName(m_bp.getAD_Language()))
					.append(": ").append(ship.getDocumentNo()) 
					.append(" - ").append(format.format(ship.getMovementDate()));
				m_ship = ship;
				//
				MInvoiceLine line = new MInvoiceLine (m_invoice);
				line.setIsDescription(true);
				line.setDescription(reference.toString());
				
				//F3P
				if(p_overrideLineNo)
				{
					m_line += 10;
					line.setLine(m_line);
				}
				else //F3P end
					line.setLine(m_line + sLine.getLine() - 2);
				
				if (!line.save())
					throw new IllegalStateException("Could not create Invoice Comment Line (sh)");
				
				//F3P
				if(p_overrideLineNo)
					STDUtils.addToArrayListAndScroll(lstLines, 0, line);
				//F3P:end
				//	Optional Ship Address if not Bill Address
				if (order.getBill_Location_ID() != ship.getC_BPartner_Location_ID())
				{
					MLocation addr = MLocation.getBPLocation(getCtx(), ship.getC_BPartner_Location_ID(), null);
					line = new MInvoiceLine (m_invoice);
					line.setIsDescription(true);
					line.setDescription(addr.toString());
					
					//F3P
					if(p_overrideLineNo)
					{
						m_line += 10;
						line.setLine(m_line);
					}
					else//F3P end
						line.setLine(m_line + sLine.getLine() - 1);
					
					if (!line.save())
						throw new IllegalStateException("Could not create Invoice Comment Line 2 (sh)");
					
					//F3P
					if(p_overrideLineNo)
						//insert Shipment Comment Line once
						STDUtils.addToArrayListAndScroll(lstLines, 1, line);
					//F3P end
				}
			}
		}
		//	
		MInvoiceLine line = new MInvoiceLine (m_invoice);
		line.setShipLine(sLine);
		if (sLine.sameOrderLineUOM())
			line.setQtyEntered(sLine.getQtyEntered());
		else
			line.setQtyEntered(sLine.getMovementQty());
		line.setQtyInvoiced(sLine.getMovementQty());
		
		// F3P: propagate accounting dimensions				
		copyAccountingDimensions(ship, m_invoice, sLine, line);
		
		//F3P
		if(p_overrideLineNo)
		{
			m_line += 10;
			line.setLine(m_line);
		}
		else //F3P end
			line.setLine(m_line + sLine.getLine());
		
		if (!line.save())
			throw new IllegalStateException("Could not create Invoice Line (s)");
		
		//F3P
		if(p_overrideLineNo)
		{
			if(lstLines.size() == 0)
				throw new IllegalStateException("Shipment not found.");
				
			STDUtils.addToArrayListAndScroll(lstLines, sLine.getLine(), line);
		}
		//F3P end
		//	Link
		sLine.setIsInvoiced(true);
		if (!sLine.save())
			throw new IllegalStateException("Could not update Shipment Line");
		
		if (log.isLoggable(Level.FINE)) log.fine(line.toString());
	}	//	createLine

	
	/**
	 * 	Complete Invoice
	 */
	private void completeInvoice()
	{
		if (m_invoice != null)
		{
			MOrder order = new MOrder(getCtx(), m_invoice.getC_Order_ID(), get_TrxName());
			if (order != null) {
				m_invoice.setPaymentRule(order.getPaymentRule());
				m_invoice.setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
				m_invoice.saveEx();
				m_invoice.load(m_invoice.get_TrxName()); // refresh from DB
				// copy payment schedule from order if invoice doesn't have a current payment schedule
				MOrderPaySchedule[] opss = MOrderPaySchedule.getOrderPaySchedule(getCtx(), order.getC_Order_ID(), 0, get_TrxName());
				MInvoicePaySchedule[] ipss = MInvoicePaySchedule.getInvoicePaySchedule(getCtx(), m_invoice.getC_Invoice_ID(), 0, get_TrxName());
				if (ipss.length == 0 && opss.length > 0) {
					BigDecimal ogt = order.getGrandTotal();
					BigDecimal igt = m_invoice.getGrandTotal();
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
						ips.setC_Invoice_ID(m_invoice.getC_Invoice_ID());
						ips.setAD_Org_ID(ops.getAD_Org_ID());
						ips.setProcessing(ops.isProcessing());
						ips.setIsActive(ops.isActive());
						ips.saveEx();
					}
					m_invoice.validatePaySchedule();
					m_invoice.saveEx();
				}
			}

			if ((p_MinimumAmt != null && p_MinimumAmt.signum() != 0
				   && m_invoice.getGrandTotal().compareTo(p_MinimumAmt) < 0)
			    ||   (p_MinimumAmtInvSched != null
				   && m_invoice.getGrandTotal().compareTo(p_MinimumAmtInvSched) < 0)) {

				// minimum amount not reached
				DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);
				String amt = format.format(m_invoice.getGrandTotal().doubleValue());
				String message = Msg.parseTranslation(getCtx(), "@NotInvoicedAmt@ " + amt + " - " + m_invoice.getC_BPartner().getName());
				addLog(message);
				if (m_savepoint != null) {
					try {
						Trx.get(get_TrxName(), false).rollback(m_savepoint);
					} catch (SQLException e) {
						throw new AdempiereException(e);
					}
				} else {
					throw new AdempiereException("No savepoint");
				}

			}
			else 
			{
				// F3P: run doc action inside an isolated trx block, to rollback the whole operation
				try
				{
					Trx.run(get_TrxName(),new TrxRunnable() {
						
						@Override
						public void run(String trxName) {
							if (!m_invoice.processIt(p_docAction))
							{
								log.warning("completeInvoice - failed: " + m_invoice);
								addBufferLog(0, null, null,"completeInvoice - failed: " + m_invoice,m_invoice.get_Table_ID(),m_invoice.getC_Invoice_ID()); // Elaine 2008/11/25
								throw new IllegalStateException("Invoice Process Failed: " + m_invoice + " - " + m_invoice.getProcessMsg());
								
							}
						}
					}); 
				}
				catch (Exception e)
				{
					String sWarning = e.getMessage();
					log.warning(sWarning);

					addLog(sWarning); // Elaine 2008/11/25
				}
				
				m_invoice.saveEx();

				String message = Msg.parseTranslation(getCtx(), "@InvoiceProcessed@ " + m_invoice.getDocumentNo());
				addBufferLog(m_invoice.getC_Invoice_ID(), m_invoice.getDateInvoiced(), null, message, m_invoice.get_Table_ID(), m_invoice.getC_Invoice_ID());
				m_created++;
				
				generatedInvoices.add(m_invoice.getC_Invoice_ID()); //F3P
			}
		}
		m_invoice = null;
		m_ship = null;
		m_line = 0;
	}	//	completeInvoice
	
	//F3P
	public List<Integer> getGeneratedInvoices()
	{
		return generatedInvoices;
	}

	private void orderInvoiceLines()
	{
		ArrayList<String> lstInvAndInout = new ArrayList<String>(mapLine.keySet());
		
		Collections.sort(lstInvAndInout);
		
		int lineNo = 0;
		int curC_Invoice_ID = -1;
		
		for(String sKey : lstInvAndInout)
		{
			int C_Invoice_ID = getKeyC_Invoice_ID(sKey);
			
			if(C_Invoice_ID != curC_Invoice_ID)
			{
				lineNo = 0;
				curC_Invoice_ID = C_Invoice_ID;
			}
			
			ArrayList<MInvoiceLine> arInvLines = mapLine.get(sKey);
			
			for(MInvoiceLine line : arInvLines)
			{
				if(line != null)
				{
					lineNo +=10;
					line.setLine(lineNo);
					line.saveEx(get_TrxName());
				}
			}
		}
	}
	
	// F3P: copy accounting dimensions
	
	/**
	 * Copy accounting dimensions. If not present on line, its copied from order, unless order and invoice have the same value
	 * 
	 * @param mOrder				order to read from
	 * @param mInvoice			invoice to read from
	 * @param mOrderLine		source order line 
	 * @param mInvLine			generated invoice line
	 */
	private void copyAccountingDimensions(PO mOrder,MInvoice mInvoice,PO mOrderLine,MInvoiceLine mInvLine)
	{
		copyValue(mOrder, mInvoice, mOrderLine, mInvLine, MOrder.COLUMNNAME_C_Project_ID);
		copyValue(mOrder, mInvoice, mOrderLine, mInvLine, MOrder.COLUMNNAME_C_Activity_ID);
		copyValue(mOrder, mInvoice, mOrderLine, mInvLine, MOrder.COLUMNNAME_C_Campaign_ID);
		copyValue(mOrder, mInvoice, mOrderLine, mInvLine, MOrder.COLUMNNAME_AD_OrgTrx_ID);
		copyValue(mOrder, mInvoice, mOrderLine, mInvLine, MOrder.COLUMNNAME_User1_ID);
		copyValue(mOrder, mInvoice, mOrderLine, mInvLine, MOrder.COLUMNNAME_User2_ID);
	}
	
	/**
	 * Copy accounting dimensions. If not present on line, its copied from order, unless order and invoice have the same value
	 * 
	 * @param mSrcHeader				order to read from
	 * @param mInvoice			invoice to reaad from
	 * @param mSourceLine		source order line 
	 * @param mInvLine			generated invoice line
	 * @param sColumnName		accounting dimension
	 */
	private void copyValue(PO mSrcHeader,MInvoice mInvoice,PO mSourceLine,MInvoiceLine mInvLine,String sColumnName)
	{
		int iInvLineValue = mInvLine.get_ValueAsInt(sColumnName);
		
		if(iInvLineValue > 0)
		{
			return;
		}
		
		int iOrderValue = mSrcHeader.get_ValueAsInt(sColumnName),
				iInvValue = mInvoice.get_ValueAsInt(sColumnName);
		
		if(iOrderValue > 0 && iOrderValue != iInvValue)
		{
			mInvLine.set_ValueOfColumn(sColumnName, iOrderValue);
		}
	}
	
	private int getC_BankAccount_ID(PO mPO)
	{
		if(mPO.get_ColumnIndex(COLUMNNAME_C_BankAccount_ID) >= 0)
		{
			return mPO.get_ValueAsInt(COLUMNNAME_C_BankAccount_ID);
		}
		else
		{
			return 0;
		}
	}
	
	private int getC_BP_BankAccount_ID(PO mPO)
	{
		if(mPO.get_ColumnIndex(COLUMNNAME_C_BP_BANKACCOUNT_ID) >= 0)
		{
			return mPO.get_ValueAsInt(COLUMNNAME_C_BP_BANKACCOUNT_ID);
		}
		else
		{
			return 0;
		}		
	}

	
	/**
	 * Used to tell, when consolidating, if a new invoice is needed
	 * 
	 * @param order
	 * @param mCurrentInvoice
	 * @return
	 */
	private boolean isNewInvoiceNeeded(MOrder order,MInvoice mCurrentInvoice,int iClient_Currency_ID)
	{
		// Standard fields
		
		if(mCurrentInvoice.getC_BPartner_Location_ID() != order.getBill_Location_ID() ||
			 //mCurrentInvoice.getAD_User_ID() != order.getBill_User_ID() || F3P: gestito con varialbile di sistema
			 getC_BankAccount_ID(mCurrentInvoice) != getC_BankAccount_ID(order) ||
			 getC_BP_BankAccount_ID(mCurrentInvoice) != getC_BP_BankAccount_ID(order) ||
			 mCurrentInvoice.getM_PriceList_ID() != order.getM_PriceList_ID() ||
			 mCurrentInvoice.getC_PaymentTerm_ID() != order.getC_PaymentTerm_ID() ||
			 mCurrentInvoice.getPaymentRule().equals(order.getPaymentRule()) == false)
		{
			return true;
		}
		
		//F3P: Check AD_User_ID based on sysConfig
		if(STDSysConfig.isUserBreakInvoice(mCurrentInvoice.getAD_Client_ID(),p_AD_Org_ID) 
				&& mCurrentInvoice.getAD_User_ID() != order.getBill_User_ID())
		{
			return true;
		}
		
		//F3P: Check SalesRep_ID based on sysConfig
		if(STDSysConfig.isSaleRepBreakInvoice(mCurrentInvoice.getAD_Client_ID(),p_AD_Org_ID) 
				&& mCurrentInvoice.getSalesRep_ID() != order.getSalesRep_ID())
		{
			return true;
		}
		
		// Check conversion type if currency is not the client currency
		
		I_M_PriceList mPriceList = order.getM_PriceList();
		
		if(mPriceList.getC_Currency_ID() != iClient_Currency_ID && 
				mCurrentInvoice.getC_ConversionType_ID() != order.getC_ConversionType_ID())
		{
			return true;
		}
		
		return false;
	}
	
	private String getMapLinesKey(MInvoice mInvoice,MInOut mInout)
	{
		StringBuilder	sbKey = new StringBuilder();
		
		sbKey.append(mInvoice.getC_Invoice_ID()).append(KEY_SEP);
		
		if(mInout != null)
		{
			sbKey.append(sdf.format(mInout.getMovementDate()))
			.append(KEY_SEP).append(mInout.getDocumentNo())
			.append(KEY_SEP).append(mInout.getM_InOut_ID());
		}
		
		return sbKey.toString();
	}
	
	private int getKeyC_Invoice_ID(String sKey)
	{
		int iSep = sKey.indexOf(KEY_SEP);
		
		if(iSep >= 0)
		{
			String sID = sKey.substring(0,iSep);
			
			return Integer.parseInt(sID);
		}
		
		return -1;
	}
	
	//F3P: ottiene le spedizioni di un ordine, eventualmente filtrate da T_Selection
	
	/**
	 * 	Get Shipments of Order
	 * 	@return shipments
	 */
	public MInOut[] getFilteredShipments(MOrder order)
	{
		 MInOut[] availablesIO = null;
		 
		if(p_SelectionInOut)
		{
			final String whereClause = "EXISTS (SELECT 1 FROM M_InOutLine iol, C_OrderLine ol, T_Selection t "
					+" WHERE iol.M_InOut_ID=M_InOut.M_InOut_ID"
					+" AND iol.C_OrderLine_ID=ol.C_OrderLine_ID"
					+" AND ol.C_Order_ID=?"
					+ "AND t.T_Selection_ID = iol.M_InOut_ID "
					+ "AND t.AD_Pinstance_ID = ? ) ";

			List<MInOut> list = new Query(getCtx(), I_M_InOut.Table_Name, whereClause, get_TrxName())
				.setParameters(order.getC_Order_ID(), getAD_PInstance_ID())
				.setOrderBy("M_InOut_ID DESC")
				.list();

			availablesIO = list.toArray(new MInOut[list.size()]);
		}
		else
		{
			availablesIO = order.getShipments();
		}
		
		return availablesIO;		
	}	//	getShipments		
	
	// F3P: Abilita selectionInOut
	
	public void enableInOutSelection()
	{
		p_SelectionInOut = true;
		p_Selection = false;
	}
	//F3P end
}	//	InvoiceGenerate
