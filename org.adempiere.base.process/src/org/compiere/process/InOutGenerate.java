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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.ProcessUtil;
import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MLocator;
import org.compiere.model.MLocatorType;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProcess;
import org.compiere.model.MProduct;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.PO;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import it.idempiere.base.util.STDSysConfig;

/**
 *	Generate Shipments.
 *	Manual or Automatic
 *	
 *  @author Jorg Janke
 *  @version $Id: InOutGenerate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class InOutGenerate extends SvrProcess
{
	// F3P: SelectionLine query
	
	public static final String SQL_SELECTIONLINE = "SELECT ol.C_OrderLine_ID,tl.* "
													+ " FROM T_SelectionLine tl INNER JOIN C_OrderLine ol on (tl.T_SelectionLine_ID = ol.C_OrderLine_ID AND ol.C_Order_ID = ?)"
													+ " WHERE tl.AD_PInstance_ID = ?"
													+ " ORDER BY ol.C_BPartner_Location_ID,ol.M_Product_ID,ol.line";

	// F3P: process associated to DocAction, prerequisite to obtain wf
	public static final String SQL_DOCACTION_ADPROCESSID = "SELECT c.AD_Process_ID FROM AD_Table t inner join AD_Column c on (t.AD_Table_ID = c.AD_Table_ID) WHERE t.tablename = 'M_InOut' AND c.ColumnName = 'DocAction'";
	
	// F3P: new parameters. Compatible with patches ruling (no dictionary changes needed)
	public static final String P_SELECTIONLINE = "SelectionLine";
	
	/**	Manual Selection		*/
	private boolean 	p_Selection = false;
	/** Warehouse				*/
	private int			p_M_Warehouse_ID = 0;
	/** BPartner				*/
	private int			p_C_BPartner_ID = 0;
	/** Promise Date			*/
	private Timestamp	p_DatePromised = null;
	/** Include Orders w. unconfirmed Shipments	*/
	private boolean		p_IsUnconfirmedInOut = false;
	/** DocAction				*/
	private String		p_docAction = DocAction.ACTION_None;
	/** Consolidate				*/
	private boolean		p_ConsolidateDocument = true;
    /** Shipment Date                       */
	private Timestamp       p_DateShipped = null;
	
	// F3P: selection line
	protected boolean p_SelectionLine = false;
	private int	DocProcess_AD_Workflow_ID = -1;
	
	//F3P: renumber line
	private boolean	p_overrideLineNo = false;
	protected int lineNoIncr = 0;
	/**	The current Shipment	*/
	protected MInOut 		m_shipment = null;
	/** Number of Shipments	being created	*/
	protected int			m_created = 0;
	/**	Line Number				*/
	private int			m_line = 0;
	/** Movement Date			*/
	private Timestamp	m_movementDate = null;
	/**	Last BP Location		*/
	private int			m_lastC_BPartner_Location_ID = -1;

	/** The Query sql			*/
	private StringBuffer 		m_sql = null;

	
	/** Storages temp space				*/
	private HashMap<SParameter,MStorageOnHand[]> m_map = new HashMap<SParameter,MStorageOnHand[]>();
	/** Last Parameter					*/
	private SParameter		m_lastPP = null;
	/** Last Storage					*/
	private MStorageOnHand[]		m_lastStorages = null;

	
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
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("DatePromised"))
				p_DatePromised = (Timestamp)para[i].getParameter();
			else if (name.equals("Selection"))
				p_Selection = "Y".equals(para[i].getParameter());
			else if (name.equals("IsUnconfirmedInOut"))
				p_IsUnconfirmedInOut = "Y".equals(para[i].getParameter());
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = "Y".equals(para[i].getParameter());
			else if (name.equals("DocAction"))
				p_docAction = (String)para[i].getParameter();
			else if (name.equals("MovementDate"))
                p_DateShipped = (Timestamp)para[i].getParameter();
			else if (name.equals(P_SELECTIONLINE)) // F3P: selection line
				p_SelectionLine = para[i].getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
			//  juddm - added ability to specify a shipment date from Generate Shipments
			if (p_DateShipped == null) {
				m_movementDate = Env.getContextAsDate(getCtx(), "#Date");
				if (m_movementDate == null)
					m_movementDate = new Timestamp(System.currentTimeMillis());
			} else
				m_movementDate = p_DateShipped;
		}
		
		// F3P: use workflow instead of plain 
		if(STDSysConfig.isInOutGenerateUseWorkflow(getAD_Client_ID()))
		{
			DocProcess_AD_Workflow_ID = getDocProcess_AD_Workflow_ID();
		}
		
		//F3P: renumber inoutline
		p_overrideLineNo = STDSysConfig.isOverrideGeneratedInOutLineNo(getAD_Client_ID()); // Spostato qui per evitare l'uso di Env.getCtx, che puo creare problemi in caso di processi eseguiti dal server
		
		if(p_overrideLineNo)
			lineNoIncr = STDSysConfig.getAddLineNoOverride(Env.getAD_Client_ID(getCtx()),Env.getAD_Org_ID(getCtx()));
		else
			lineNoIncr = 10;
	}	//	prepare

	/**
	 * 	Generate Shipments
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info("Selection=" + p_Selection
			+ ", M_Warehouse_ID=" + p_M_Warehouse_ID 
			+ ", C_BPartner_ID=" + p_C_BPartner_ID 
			+ ", Consolidate=" + p_ConsolidateDocument
			+ ", IsUnconfirmed=" + p_IsUnconfirmedInOut
			+ ", Movement=" + m_movementDate);
		
		if (p_M_Warehouse_ID == 0)
			throw new AdempiereUserError("@NotFound@ @M_Warehouse_ID@");
		
		if (p_Selection)	//	VInOutGen
		{
			m_sql = new StringBuffer("SELECT C_Order.* FROM C_Order, T_Selection ")
				.append("WHERE C_Order.DocStatus='CO' AND C_Order.IsSOTrx='Y' AND C_Order.AD_Client_ID=? ")
				.append("AND C_Order.C_Order_ID = T_Selection.T_Selection_ID ") 
				.append("AND T_Selection.AD_PInstance_ID=? ");
		}
		else
		{
			m_sql = new StringBuffer("SELECT * FROM C_Order o ")
				.append("WHERE DocStatus='CO' AND IsSOTrx='Y'")
				//	No Offer,POS
				.append(" AND o.C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType ")
					.append("WHERE DocBaseType='SOO' AND DocSubTypeSO NOT IN ('ON','OB','WR'))")
				.append("	AND o.IsDropShip='N'")
				//	No Manual
				.append(" AND o.DeliveryRule<>'M'")
				//	Open Order Lines with Warehouse
				.append(" AND EXISTS (SELECT * FROM C_OrderLine ol ")
					.append("WHERE ol.M_Warehouse_ID=?");					//	#1
			if (p_DatePromised != null)
				m_sql.append(" AND TRUNC(ol.DatePromised, 'DD')<=?");		//	#2 //F3P: porting adempiere add 'DD'
			m_sql.append(" AND o.C_Order_ID=ol.C_Order_ID AND ol.QtyOrdered<>ol.QtyDelivered)");
			//
			if (p_C_BPartner_ID != 0)
				m_sql.append(" AND o.C_BPartner_ID=?");					//	#3
		}
		//F3P: add order by dateOrdered, DocumentNo
		m_sql.append(" ORDER BY M_Warehouse_ID, PriorityRule, M_Shipper_ID, C_BPartner_ID, C_BPartner_Location_ID, DateOrdered, DocumentNo, C_Order_ID");
		//	m_sql += " FOR UPDATE";

		PreparedStatement pstmt = null;
		//F3P: log error
		boolean error = false;
		try
		{
			pstmt = DB.prepareStatement (m_sql.toString(), get_TrxName());
			int index = 1;
			if (p_Selection)
			{
				pstmt.setInt(index++, Env.getAD_Client_ID(getCtx()));
				pstmt.setInt(index++, getAD_PInstance_ID());
			}
			else	
			{
				pstmt.setInt(index++, p_M_Warehouse_ID);
				if (p_DatePromised != null)
					pstmt.setTimestamp(index++, p_DatePromised);
				if (p_C_BPartner_ID != 0)
					pstmt.setInt(index++, p_C_BPartner_ID);
			}
		}
		catch (Exception e)
		{
			//F3P: log error
			//throw new AdempiereException(e);
			log.log(Level.SEVERE, m_sql.toString(), e);
			addLog(e.getMessage());
			error = true;

		}
		//F3P: log error
		if(error)
			return "@Error@";
		
		return generate(pstmt);
	}	//	doIt
	
	/**
	 * 	Generate Shipments
	 * 	@param pstmt Order Query
	 *	@return info
	 */
	private String generate (PreparedStatement pstmt)
	{
		String sError = null; // F3P: errors as of now are ignored, manage them
		
		ResultSet rs = null;
		try
		{
			rs = pstmt.executeQuery ();
			while (rs.next ())		//	Order
			{
				MOrder order = new MOrder (getCtx(), rs, get_TrxName());
				statusUpdate(Msg.getMsg(getCtx(), "Processing") + " " + order.getDocumentInfo());
				
				// F3P: ignore shipper on shipment consolidate ?
				
				boolean	bIgnoreShipperOnConsolidate = STDSysConfig.isGenInOutIgnoreShipperOnConsolidate(order.getAD_Client_ID(), order.getAD_Org_ID());
				
				//	New Header different Shipper, Shipment Location
				if (!p_ConsolidateDocument 
					|| (m_shipment != null 
					&& (m_shipment.getC_BPartner_Location_ID() != order.getC_BPartner_Location_ID()
						|| (bIgnoreShipperOnConsolidate == false && m_shipment.getM_Shipper_ID() != order.getM_Shipper_ID() ))))
					completeShipment();
				if (log.isLoggable(Level.FINE)) log.fine("check: " + order + " - DeliveryRule=" + order.getDeliveryRule());
				//
				Timestamp minGuaranteeDate = m_movementDate;
				boolean completeOrder = MOrder.DELIVERYRULE_CompleteOrder.equals(order.getDeliveryRule());
				//	OrderLine WHERE
				StringBuilder where = new StringBuilder(" AND M_Warehouse_ID=").append(p_M_Warehouse_ID);
				if (p_DatePromised != null)
					where.append(" AND (TRUNC(DatePromised)<=").append(DB.TO_DATE(p_DatePromised, true))
						.append(" OR DatePromised IS NULL)");		
				//	Exclude Auto Delivery if not Force
				if (!MOrder.DELIVERYRULE_Force.equals(order.getDeliveryRule()))
					where.append(" AND (C_OrderLine.M_Product_ID IS NULL")
						.append(" OR EXISTS (SELECT * FROM M_Product p ")
						.append("WHERE C_OrderLine.M_Product_ID=p.M_Product_ID")
						.append(" AND IsExcludeAutoDelivery='N'))");
				//	Exclude Unconfirmed
				if (!p_IsUnconfirmedInOut)
					where.append(" AND NOT EXISTS (SELECT * FROM M_InOutLine iol")
							.append(" INNER JOIN M_InOut io ON (iol.M_InOut_ID=io.M_InOut_ID) ")
								.append("WHERE iol.C_OrderLine_ID=C_OrderLine.C_OrderLine_ID AND io.DocStatus IN ('DR','IN','IP','WC'))");
				//	Deadlock Prevention - Order by M_Product_ID
				// F3P: support for T_SelectionLine
				MOrderLine[] lines = null;
				
				if(p_SelectionLine)
				{
					PreparedStatement ptmtSL = null;
					ResultSet rsLS = null;
					
					try
					{
						List<MOrderLine> lstLines = new ArrayList<MOrderLine>();
						
						ptmtSL = DB.prepareStatement(SQL_SELECTIONLINE, get_TrxName());
						ptmtSL.setInt(1,order.getC_Order_ID());
						ptmtSL.setInt(2, getProcessInfo().getAD_PInstance_ID());
						rsLS = ptmtSL.executeQuery();
						
						while(rsLS.next())
						{
							int C_OrderLine_ID = rsLS.getInt(MOrderLine.COLUMNNAME_C_OrderLine_ID);
							BigDecimal bdQty = rsLS.getBigDecimal("Qty");
														
							MOrderLine mOLine = PO.get(getCtx(), MOrderLine.Table_Name, C_OrderLine_ID, get_TrxName());
							SelectionLineOrderLineWrapper wrapper = wrapLine(mOLine);
							wrapper.qty = bdQty;
							
							processSelectionLine(mOLine, wrapper.additionalData, rsLS);							
							
							lstLines.add(wrapper);						
						}
						
						lines = lstLines.toArray(new SelectionLineOrderLineWrapper[lstLines.size()]);
					}
					catch(Exception e)
					{
						throw new AdempiereException("Selection Line not supported",e);
					}
					finally
					{
						DB.close(rsLS, ptmtSL);
					}
				}
				else
				{
					lines = order.getLines (where.toString(), "C_BPartner_Location_ID, M_Product_ID");
				}
				
				for (int i = 0; i < lines.length; i++)
				{
					MOrderLine line = lines[i];
					MOrderLine wrapperOrLine = null;
					SelectionLineOrderLineWrapper wrapper = null;
					int deliverFrom_M_Locator_ID = -1;
					int deliverM_AttributeSetInstance_ID = line.getM_AttributeSetInstance_ID();
					
					// Managed wrapped lines, and raw lines
					
					if(line instanceof SelectionLineOrderLineWrapper)
					{
						wrapper = (SelectionLineOrderLineWrapper)line;
						line = wrapper.orderLine;
						wrapperOrLine = wrapper;
						deliverFrom_M_Locator_ID = wrapper.getDeliveryM_Locator_ID();
						int wrapperM_ASI_ID = wrapper.getDeliveryM_AttributeSetInstance_ID();
						
						if(wrapperM_ASI_ID >= 0) // 0 is a valid asi
							deliverM_AttributeSetInstance_ID = wrapperM_ASI_ID; 
					}
					else
						wrapperOrLine = line;
					
					if (line.getM_Warehouse_ID() != p_M_Warehouse_ID)
						continue;
					if (log.isLoggable(Level.FINE)) log.fine("check: " + line);
					BigDecimal onHand = Env.ZERO;
					BigDecimal toDeliver = line.getQtyOrdered()
						.subtract(line.getQtyDelivered());
					
					if(wrapper != null)
					{
						if(wrapper.qty != null)
							toDeliver = wrapper.qty;
					}
					
					MProduct product = line.getProduct();
					//	Nothing to Deliver
					if (product != null && toDeliver.signum() == 0)
					{
						logOrderLineInfo(line, " skipped: product to deliver = 0"); // F3P: Helper for order line-related logs (level info)
						continue;
					}
					
					// or it's a charge - Bug#: 1603966 
					if (line.getC_Charge_ID()!=0 && toDeliver.signum() == 0)
					{
						logOrderLineInfo(line, " skipped: charge to deliver = 0"); // F3P: Helper for order line-related logs (level info)
						continue;
					}
											
					// F3P: or it is not a comment
					
					if (line.getQtyOrdered().signum() != 0 && toDeliver.signum() == 0)
					{
						logOrderLineInfo(line, " skipped: to deliver = 0 and ordered != 0"); // F3P: Helper for order line-related logs (level info)
						continue;
					}
					
					//	Check / adjust for confirmations
					BigDecimal unconfirmedShippedQty = Env.ZERO;
					if (p_IsUnconfirmedInOut && product != null && toDeliver.signum() != 0)
					{
						String where2 = "EXISTS (SELECT * FROM M_InOut io WHERE io.M_InOut_ID=M_InOutLine.M_InOut_ID AND io.DocStatus IN ('DR','IN','IP','WC'))";
						MInOutLine[] iols = MInOutLine.getOfOrderLine(getCtx(), 
							line.getC_OrderLine_ID(), where2, null);
						for (int j = 0; j < iols.length; j++) 
							unconfirmedShippedQty = unconfirmedShippedQty.add(iols[j].getMovementQty());
						StringBuilder logInfo = new StringBuilder("Unconfirmed Qty=").append(unconfirmedShippedQty) 
							.append(" - ToDeliver=").append(toDeliver).append("->");					
						toDeliver = toDeliver.subtract(unconfirmedShippedQty);
						logInfo.append(toDeliver);
						if (toDeliver.signum() < 0)
						{
							toDeliver = Env.ZERO;
							logInfo.append(" (set to 0)");
						}
						//	Adjust On Hand
						onHand = onHand.subtract(unconfirmedShippedQty);
						if (log.isLoggable(Level.FINE)) log.fine(logInfo.toString());
					}
					
					//	Comments & lines w/o product & services
					if ((product == null || !product.isStocked())
						&& (line.getQtyOrdered().signum() == 0 	//	comments
							|| toDeliver.signum() != 0))		//	lines w/o product
					{
						if (!MOrder.DELIVERYRULE_CompleteOrder.equals(order.getDeliveryRule()))	//	printed later
							createLine (order, wrapperOrLine, toDeliver, null, false);
						else
							logOrderLineInfo(line, " skipped: delivery rule == complete order");  // F3P: Helper for order line-related logs (level info)
						continue;
					}

					//	Stored Product
					String MMPolicy = product.getMMPolicy();

					MStorageOnHand[] storages = getStorages(line.getM_Warehouse_ID(),
							 line.getM_Product_ID(), deliverM_AttributeSetInstance_ID, deliverFrom_M_Locator_ID, 
							 minGuaranteeDate, MClient.MMPOLICY_FiFo.equals(MMPolicy));
					
					for (int j = 0; j < storages.length; j++)
					{
						MStorageOnHand storage = storages[j];
						onHand = onHand.add(storage.getQtyOnHand());
					}
					boolean fullLine = onHand.compareTo(toDeliver) >= 0
						|| toDeliver.signum() < 0;
					
					//	Complete Order
					if (completeOrder && !fullLine)
					{
						if (log.isLoggable(Level.FINE)) log.fine("Failed CompleteOrder - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ "), ToDeliver=" + toDeliver + " - " + line);
						completeOrder = false;
						break;
					}
					//	Complete Line
					else if (fullLine && MOrder.DELIVERYRULE_CompleteLine.equals(order.getDeliveryRule()))
					{
						if (log.isLoggable(Level.FINE)) log.fine("CompleteLine - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ ", ToDeliver=" + toDeliver + " - " + line);
						//	
						createLine (order, wrapperOrLine, toDeliver, storages, false);
					}
					//	Availability
					else if (MOrder.DELIVERYRULE_Availability.equals(order.getDeliveryRule())
						&& (onHand.signum() > 0
							|| toDeliver.signum() < 0))
					{
						BigDecimal deliver = toDeliver;
						if (deliver.compareTo(onHand) > 0)
							deliver = onHand;
						if (log.isLoggable(Level.FINE)) log.fine("Available - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ "), ToDeliver=" + toDeliver 
							+ ", Delivering=" + deliver + " - " + line);
						//	
						createLine (order, wrapperOrLine, deliver, storages, false);
					}
					//	Force
					else if (MOrder.DELIVERYRULE_Force.equals(order.getDeliveryRule()))
					{
						BigDecimal deliver = toDeliver;
						if (log.isLoggable(Level.FINE)) log.fine("Force - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ "), ToDeliver=" + toDeliver 
							+ ", Delivering=" + deliver + " - " + line);
						//	
						createLine (order, wrapperOrLine, deliver, storages, true);
					}
					//	Manual
					else if (MOrder.DELIVERYRULE_Manual.equals(order.getDeliveryRule())) {
						if (log.isLoggable(Level.FINE)) log.fine("Manual - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ ") - " + line);
					} else {
						if (log.isLoggable(Level.FINE)) log.fine("Failed: " + order.getDeliveryRule() + " - OnHand=" + onHand 
								+ " (Unconfirmed=" + unconfirmedShippedQty
								+ "), ToDeliver=" + toDeliver + " - " + line);
					}
				}	//	for all order lines
				
				//	Complete Order successful
				if (completeOrder && MOrder.DELIVERYRULE_CompleteOrder.equals(order.getDeliveryRule()))
				{
					for (int i = 0; i < lines.length; i++)
					{
						MOrderLine line = lines[i];
						MOrderLine wrapperOrLine = null;
						SelectionLineOrderLineWrapper orderLineWrapper = null;
						int deliveryFrom_M_Locator_ID = -1;
						int deliveryM_AttributeSetInstance_ID = line.getM_AttributeSetInstance_ID();
						
						if(line instanceof SelectionLineOrderLineWrapper)
						{
							orderLineWrapper = (SelectionLineOrderLineWrapper)line;						
							line = orderLineWrapper.orderLine;
							wrapperOrLine = orderLineWrapper;
							deliveryFrom_M_Locator_ID = orderLineWrapper.getDeliveryM_Locator_ID();
							
							int wrapperM_ASI_ID = orderLineWrapper.getDeliveryM_AttributeSetInstance_ID();
							
							if(wrapperM_ASI_ID >= 0)
								deliveryM_AttributeSetInstance_ID = wrapperM_ASI_ID; 
						}
						else
							wrapperOrLine = line;
						
						if (line.getM_Warehouse_ID() != p_M_Warehouse_ID)
							continue;
						MProduct product = line.getProduct();
						BigDecimal toDeliver = line.getQtyOrdered().subtract(line.getQtyDelivered());
						
						if(orderLineWrapper != null)
						{
							if(orderLineWrapper.qty != null)
								toDeliver = orderLineWrapper.qty;
						}
						
						//
						MStorageOnHand[] storages = null;
						if (product != null && product.isStocked())
						{
							String MMPolicy = product.getMMPolicy();
							storages = getStorages(line.getM_Warehouse_ID(), 
								line.getM_Product_ID(), deliveryM_AttributeSetInstance_ID, deliveryFrom_M_Locator_ID, 
								minGuaranteeDate, MClient.MMPOLICY_FiFo.equals(MMPolicy));
						}
						//	
						createLine (order, wrapperOrLine, toDeliver, storages, false);
					}
				}
				// F3P: avoid mix order when there are more than 100 lines
				//m_line += 1000;
				m_line += 100000;
			}	//	while order
		}
		catch (Exception e)
		{
			//throw new AdempiereException(e);
			// F3P: improved log of error
			
			sError = e.getLocalizedMessage();
			
			if(sError == null)
				sError = e.getMessage();
			
			if(sError == null)
				sError = e.toString();
			
			if(m_shipment != null)
			{
				addLog(m_shipment.getM_InOut_ID(), m_shipment.getMovementDate(), null, m_shipment.getDocumentNo() + " - @Error@ " + sError);
			}
			
			log.log(Level.SEVERE, m_sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		// F3P: management of error, avoid failing silently
		if(sError == null)
		{
			completeShipment();
			return "@Created@ = " + m_created;
		}
		else
		{
			return "@Error@ " + sError;
		}
	}	//	generate
	
	
	
	/**************************************************************************
	 * 	Create Line
	 *	@param order order
	 *	@param orderLine line
	 *	@param qty qty
	 *	@param storages storage info
	 *	@param force force delivery
	 */
	private void createLine (MOrder order, MOrderLine orderLine, BigDecimal qty, 
		MStorageOnHand[] storages, boolean force)
	{
		SelectionLineOrderLineWrapper orderLineWrapper = null;
		Map<String, Object> wrapperAdditionalData = null;
		int deliverM_AttributeSetInstance_ID = orderLine.getM_AttributeSetInstance_ID();
		int deliverFrom_M_Locator_ID = 0;
		
		if(orderLine instanceof SelectionLineOrderLineWrapper)
		{
			orderLineWrapper = (SelectionLineOrderLineWrapper)orderLine;
			wrapperAdditionalData = orderLineWrapper.additionalData;
			orderLine = orderLineWrapper.orderLine;
			
			int wrapperM_ASI_ID = orderLineWrapper.getDeliveryM_AttributeSetInstance_ID();
			
			if(wrapperM_ASI_ID >= 0)
				deliverM_AttributeSetInstance_ID = wrapperM_ASI_ID;
			
			if(orderLineWrapper.getDeliveryM_Locator_ID() > 0)
				deliverFrom_M_Locator_ID = orderLineWrapper.getDeliveryM_Locator_ID();
		}
				
		//	Complete last Shipment - can have multiple shipments
		if (m_lastC_BPartner_Location_ID != orderLine.getC_BPartner_Location_ID() )
			completeShipment();
		m_lastC_BPartner_Location_ID = orderLine.getC_BPartner_Location_ID();
		//	Create New Shipment
		if (m_shipment == null)
		{
			// F3P: moved to separated function to improve overridability
			
			// m_shipment = new MInOut (order, 0, m_movementDate);
			// m_shipment.setM_Warehouse_ID(orderLine.getM_Warehouse_ID());	//	sets Org too
			// if (order.getC_BPartner_ID() != orderLine.getC_BPartner_ID())
			//  m_shipment.setC_BPartner_ID(orderLine.getC_BPartner_ID());
			// if (order.getC_BPartner_Location_ID() != orderLine.getC_BPartner_Location_ID())
			//  m_shipment.setC_BPartner_Location_ID(orderLine.getC_BPartner_Location_ID());

			m_shipment = createShipment(order, orderLine, m_movementDate);
			
			if (!m_shipment.save())
				throw new IllegalStateException("Could not create Shipment");
		}
		//	Non Inventory Lines
		if (storages == null)
		{
			MInOutLine line = new MInOutLine (m_shipment);
			line.setOrderLine(orderLine, deliverFrom_M_Locator_ID, Env.ZERO); // F3P: specify locator for non-delivery, to be consistent with the specified one
			line.setQty(qty);	//	Correct UOM
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(qty
					.multiply(orderLine.getQtyEntered())
					.divide(orderLine.getQtyOrdered(), 12, RoundingMode.HALF_UP));
			line.setLine(m_line + orderLine.getLine());
			
			//F3P add extension point
			beforeSaveInOutLine(line, wrapperAdditionalData);
			
			/* F3P: changed to saveEx to avoid silently ignore the real error 
			if (!line.save())
		    {
					throw new IllegalStateException("Could not create Shipment Line");
		    }
			 */
			
			line.saveEx(get_TrxName());
			
			//F3P add extension point
			afterSaveInOutLine(line, wrapperAdditionalData);
		
			if (log.isLoggable(Level.FINE)) log.fine(line.toString());
			return;
		}
		
	
		//	Inventory Lines
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		BigDecimal toDeliver = qty;
		for (int i = 0; i < storages.length; i++)
		{
			MStorageOnHand storage = storages[i];
			BigDecimal deliver = toDeliver;
			//skip negative storage record
			if (storage.getQtyOnHand().signum() < 0) 
				continue;
			
			//	Not enough On Hand
			if (deliver.compareTo(storage.getQtyOnHand()) > 0 
				&& storage.getQtyOnHand().signum() >= 0)		//	positive storage
			{
				if (!force	//	Adjust to OnHand Qty  
					|| (force && i+1 != storages.length))	//	if force not on last location
					deliver = storage.getQtyOnHand();
			}
			if (deliver.signum() == 0)	//	zero deliver
				continue;
			int M_Locator_ID = storage.getM_Locator_ID();
			//
			MInOutLine line = null;
			if (deliverM_AttributeSetInstance_ID == 0)      //      find line with Locator
			{
				for (int ll = 0; ll < list.size(); ll++)
				{
					MInOutLine test = (MInOutLine)list.get(ll);
					if (test.getM_Locator_ID() == M_Locator_ID && test.getM_AttributeSetInstance_ID() == 0)
					{
						line = test;
						break;
					}
				}
			}
			if (line == null)	//	new line
			{
				line = new MInOutLine (m_shipment);
				line.setOrderLine(orderLine, M_Locator_ID, order.isSOTrx() ? deliver : Env.ZERO);
				line.setM_AttributeSetInstance_ID(deliverM_AttributeSetInstance_ID);
				
				line.setQty(deliver);
				list.add(line);
			}
			else				//	existing line
				line.setQty(line.getMovementQty().add(deliver));
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(line.getMovementQty().multiply(orderLine.getQtyEntered())
					.divide(orderLine.getQtyOrdered(), 12, RoundingMode.HALF_UP));
			line.setLine(m_line + orderLine.getLine());
			
			//F3P add extension point
			beforeSaveInOutLine(line, wrapperAdditionalData);
			
			/* F3P: changed to saveEx to avoid silently ignore the real error 
			if (!line.save())
		    {
					throw new IllegalStateException("Could not create Shipment Line");
		    }
			 */
			
			line.saveEx(get_TrxName());
			
			//F3P add extension point
			afterSaveInOutLine(line, wrapperAdditionalData);
			
			if (log.isLoggable(Level.FINE)) log.fine("ToDeliver=" + qty + "/" + deliver + " - " + line);
			toDeliver = toDeliver.subtract(deliver);
			//      Temp adjustment, actual update happen in MInOut.completeIt - just in memory - not saved
			storage.setQtyOnHand(storage.getQtyOnHand().subtract(deliver));
			//
			if (toDeliver.signum() == 0)
				break;
		}		
		if (toDeliver.signum() != 0)
		{	 
			if (!force)
			{
				throw new IllegalStateException("Not All Delivered - Remainder=" + toDeliver);
			}
			else 
			{
				// F3P: with 'force', try to accumulate lines instead of splitting them
				MInOutLine line = null;
				
				if(list.size() > 0)
				{
					line = list.get(list.size() - 1); // Fetch last line
					BigDecimal bdQty = line.getQtyEntered();
					bdQty = bdQty.add(toDeliver);
					line.setQty(bdQty);
				}
				else
				{
					// F3P: generate a new line only if we cannot accumulate it
					line = new MInOutLine (m_shipment);
					//LS: set deliverFrom_M_Locator_ID and deliverM_AttributeSetInstance_ID
					line.setOrderLine(orderLine, deliverFrom_M_Locator_ID, order.isSOTrx() ? toDeliver : Env.ZERO);
					line.setM_AttributeSetInstance_ID(deliverM_AttributeSetInstance_ID);
					//LS end
					line.setQty(toDeliver);
					if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
						 line.setQtyEntered(line.getMovementQty().multiply(orderLine.getQtyEntered())
						 .divide(orderLine.getQtyOrdered(), 12, RoundingMode.HALF_UP));
					 line.setLine(m_line + orderLine.getLine());
				}	
				
				 /* F3P: changed to saveEx to avoid silently ignore the real error 
					if (!line.save())
				    {
							throw new IllegalStateException("Could not create Shipment Line");
				    }
				  */
				//F3P add extension point
				beforeSaveInOutLine(line, wrapperAdditionalData);
				line.saveEx(get_TrxName());
				//F3P add extension point
				afterSaveInOutLine(line, wrapperAdditionalData);
			}
		}	
	}	//	createLine

	
	/**
	 * 	Get Storages
	 *	@param M_Warehouse_ID
	 *	@param M_Product_ID
	 *	@param M_AttributeSetInstance_ID
	 *	@param minGuaranteeDate
	 *	@param FiFo
	 *	@return storages
	 */
	private MStorageOnHand[] getStorages(int M_Warehouse_ID, 
			 int M_Product_ID, int M_AttributeSetInstance_ID, int M_Locator_ID, 
			  Timestamp minGuaranteeDate, boolean FiFo)
	{
		m_lastPP = new SParameter(M_Warehouse_ID, 
		M_Product_ID, M_AttributeSetInstance_ID, M_Locator_ID, 
			minGuaranteeDate, FiFo);
		//
		m_lastStorages = m_map.get(m_lastPP); 
		
		if (m_lastStorages == null)
		{
			MStorageOnHand[] tmpStorages = MStorageOnHand.getWarehouse(getCtx(), 
				M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,
				minGuaranteeDate, FiFo,false, 0, get_TrxName());

			/* IDEMPIERE-2668 - filter just locators enabled for shipping */
			List<MStorageOnHand> m_storagesForShipping = new ArrayList<MStorageOnHand>();
			for (MStorageOnHand soh : tmpStorages) {
				
				if(M_Locator_ID > 0 && soh.getM_Locator_ID() != M_Locator_ID) // F3P: if a locator is provided, accept only storage with this locator
					continue;
				
				MLocator loc = MLocator.get(getCtx(), soh.getM_Locator_ID());
				MLocatorType lt = null;
				if (loc.getM_LocatorType_ID() > 0)
					lt = MLocatorType.get(getCtx(), loc.getM_LocatorType_ID());
				if (lt == null || lt.isAvailableForShipping())
					m_storagesForShipping.add(soh);
			}
			m_lastStorages = new MStorageOnHand[m_storagesForShipping.size()];
			m_storagesForShipping.toArray(m_lastStorages);

			m_map.put(m_lastPP, m_lastStorages);
		}
		return m_lastStorages;
	}	//	getStorages
	
	/**
	 * 	Complete Shipment (default process action)
	 */
	protected void completeShipment()
	{
		completeShipment(p_docAction);
	}
	
	/**
	 * 	Complete Shipment
	 *  
	 */
	protected void completeShipment(String docAction)
	{
		if (m_shipment != null)
		{
			//F3P renumber
			if(p_overrideLineNo)
			{
				MInOutLine[] lines = m_shipment.getLines();
				
				int lineNo = 0;
				
				for(MInOutLine line : lines)
				{
					lineNo += lineNoIncr;
					line.setLine(lineNo); 
					line.saveEx(get_TrxName());
				}
			}
			
			// F3P: rollback complete if there is an error
			
			Trx trx = Trx.get(get_TrxName(), false);
			Savepoint savepoint = null;
			
			try
			{
				savepoint = trx.setSavepoint("beforeComplete");
				
				if(DocAction.ACTION_Complete.equals(docAction) && DocProcess_AD_Workflow_ID > 0) // F3P use workflow instead of plain process
				{
					ProcessInfo pi = new ProcessInfo("CompleteWF", getProcessInfo().getAD_Process_ID(), 0, m_shipment.getM_InOut_ID());
					pi.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
					pi.setAD_Client_ID(m_shipment.getAD_Client_ID());
					pi.setTransactionName(trx.getTrxName());
					
					ProcessUtil.startWorkFlow(getCtx(), pi, DocProcess_AD_Workflow_ID);
				}
				else
				{
					if (!DocAction.ACTION_None.equals(docAction))
					{
						//	Fails if there is a confirmation
						if (!m_shipment.processIt(docAction))
						{
							log.warning("Failed: " + m_shipment);
							//
							addLog(m_shipment.getM_InOut_ID(), m_shipment.getMovementDate(), null, m_shipment.getDocumentNo() + " - @Error@ " + m_shipment.getProcessMsg());
	
							//
							rollback(trx,savepoint);
							m_shipment.setDocStatus(MInOut.DOCSTATUS_Invalid);
						}
						else
						{
							addLog(m_shipment.getM_InOut_ID(), m_shipment.getMovementDate(), null, m_shipment.getDocumentNo(),MInOut.Table_ID,m_shipment.getM_InOut_ID());
						}
					}
					m_shipment.saveEx();
				}
			}
			catch(Exception e)
			{
				addLog(m_shipment.getM_InOut_ID(), m_shipment.getMovementDate(), null, m_shipment.getDocumentNo() + " - @Error@ " + e.getLocalizedMessage());
				rollback(trx,savepoint);
			}
			
			m_created++;
			
			//reset storage cache as MInOut.completeIt will update m_storage
			m_map = new HashMap<SParameter,MStorageOnHand[]>();
			m_lastPP = null;
			m_lastStorages = null;
		}
		m_shipment = null;
		m_line = 0;
	}	//	completeOrder
	
	// F3P: added to simplify rollback management
	private void rollback(Trx trx,Savepoint savepoint)
	{
		try
		{
			if(savepoint != null)
				trx.rollback(savepoint);
		}
		catch(SQLException ex)
		{
			log.warning("Failed rollback: " + ex.getMessage());
		}
	}
	
	// F3P: Helper for order line-related logs (level info)
	
	private void logOrderLineInfo(MOrderLine orderLine,String info)
	{
		if(log.isLoggable(Level.INFO))
		{
			StringBuilder sbLog = new StringBuilder("Order line");
			sbLog.append(orderLine.toString())
				.append(" [")
				.append(orderLine.getLine())
				.append(" ] ")
				.append(info);
			
			log.info(sbLog.toString());
		}		
	}
	
	/** Get the workflow associated to doc. processing of InOut
	 *  
	 * @return
	 */
	protected int getDocProcess_AD_Workflow_ID()
	{
		int AD_Process_ID = DB.getSQLValue(get_TrxName(), SQL_DOCACTION_ADPROCESSID);
		MProcess mProcess = PO.get(getCtx(), MProcess.Table_Name, AD_Process_ID, get_TrxName());
		
		return mProcess.getAD_Workflow_ID();
	}
	/**
	 * 	InOutGenerate Parameter
	 */
	static class SParameter
	{
		/**
		 * 	Parameter
		 *	@param p_Warehouse_ID warehouse
		 *	@param p_Product_ID 
		 *	@param p_AttributeSetInstance_ID 
		 *	@param p_minGuaranteeDate
		 *	@param p_FiFo
		 */
		
		// F3P: Manage locator
		
		protected SParameter (int p_Warehouse_ID, 
			int p_Product_ID, int p_AttributeSetInstance_ID, int p_M_Locator_ID, 
			Timestamp p_minGuaranteeDate,boolean p_FiFo)
		{
			this.M_Warehouse_ID = p_Warehouse_ID;
			this.M_Product_ID = p_Product_ID;
			this.M_AttributeSetInstance_ID = p_AttributeSetInstance_ID; 
			this.minGuaranteeDate = p_minGuaranteeDate;
			this.FiFo = p_FiFo;
			this.M_Locator_ID = p_M_Locator_ID;
		}
		/** Warehouse		*/
		public int M_Warehouse_ID;
		/** Product			*/
		public int M_Product_ID;
		/** ASI				*/
		public int M_AttributeSetInstance_ID;
		/** AS				*/
		public int M_AttributeSet_ID;
		/** All instances	*/
		public boolean allAttributeInstances;
		/** Mon Guarantee Date	*/
		public Timestamp minGuaranteeDate;
		/** FiFo			*/
		public boolean FiFo;		
		/** F3P: Locator	*/
		public int M_Locator_ID;

		/**
		 * 	Equals
		 *	@param obj
		 *	@return true if equal
		 */
		public boolean equals (Object obj)
		{
			if (obj != null && obj instanceof SParameter)
			{
				SParameter cmp = (SParameter)obj;
				boolean eq = cmp.M_Warehouse_ID == M_Warehouse_ID
					&& cmp.M_Product_ID == M_Product_ID
					&& cmp.M_AttributeSetInstance_ID == M_AttributeSetInstance_ID
					&& cmp.M_AttributeSet_ID == M_AttributeSet_ID
					&& cmp.allAttributeInstances == allAttributeInstances
					&& cmp.FiFo == FiFo
					&& cmp.M_Locator_ID == M_Locator_ID;  // F3P: introduce locator
				if (eq)
				{
					if (cmp.minGuaranteeDate == null && minGuaranteeDate == null)
						;
					else if (cmp.minGuaranteeDate != null && minGuaranteeDate != null
						&& cmp.minGuaranteeDate.equals(minGuaranteeDate))
						;
					else
						eq = false;
				}
				return eq;
			}
			return false;
		}	//	equals
		
		/**
		 * 	hashCode
		 *	@return hash code
		 */
		public int hashCode ()
		{
			long hash = M_Warehouse_ID
				+ (M_Product_ID * 2)
				+ (M_AttributeSetInstance_ID * 3)
				+ (M_AttributeSet_ID * 4)
				+ (M_Locator_ID * 5); // F3P: introduce locator

			if (allAttributeInstances)
				hash *= -1;
			if (FiFo)	
				hash *= -2;
			if (hash < 0)
				hash = -hash + 7;
			while (hash > Integer.MAX_VALUE)
				hash -= Integer.MAX_VALUE;
			//
			if (minGuaranteeDate != null)
			{
				hash += minGuaranteeDate.hashCode();
				while (hash > Integer.MAX_VALUE)
					hash -= Integer.MAX_VALUE;
			}
				
			return (int)hash;
		}	//	hashCode
		
	}	//	Parameter
	
	// F3P: split creation in overridabile function
	
	protected MInOut createShipment(MOrder order, MOrderLine orderLine, Timestamp movementDate)
	{
		MInOut shipment = new MInOut (order, 0, movementDate);
				
		shipment.setM_Warehouse_ID(orderLine.getM_Warehouse_ID());	//	sets Org too
		if (order.getC_BPartner_ID() != orderLine.getC_BPartner_ID())
			shipment.setC_BPartner_ID(orderLine.getC_BPartner_ID());
		if (order.getC_BPartner_Location_ID() != orderLine.getC_BPartner_Location_ID())
			shipment.setC_BPartner_Location_ID(orderLine.getC_BPartner_Location_ID());
				
		return shipment;
	}	
	//F3P
	protected void processSelectionLine(MOrderLine line, Map<String,Object> lineAdditionalData, ResultSet rs) throws Exception
	{
		//Nothing to do
	}
	
	protected void beforeSaveInOutLine(MInOutLine line, Map<String,Object> selectionLineAdditionalData) throws AdempiereException
	{
		//Nothing to do
	}
	
	protected void afterSaveInOutLine(MInOutLine line, Map<String,Object> selectionLineAdditionalData) throws AdempiereException
	{
		//Nothing to do
	}
	
	public SelectionLineOrderLineWrapper wrapLine(MOrderLine line)
	{
		return new SelectionLineOrderLineWrapper(line);
	}
	
	public class SelectionLineOrderLineWrapper extends MOrderLine
	{
		/**
		 * 
		 */
		
		private static final long serialVersionUID = -7385140481126599352L;
		private MOrderLine	orderLine = null;
		private BigDecimal	qty = null;
		private Map<String, Object> additionalData = new HashMap<>(); 
		
		public SelectionLineOrderLineWrapper(MOrderLine line)
		{
			super(line.getParent());			
			this.orderLine = line;
		}
		
		public int getDeliveryM_Locator_ID() // Common use case: set delivery locator
		{
			int M_Locator_ID = -1;
			
			if(additionalData.containsKey(MInOutLine.COLUMNNAME_M_Locator_ID))
			{
				Integer loc = (Integer)additionalData.get(MInOutLine.COLUMNNAME_M_Locator_ID);
				
				if(loc != null)
					M_Locator_ID = loc.intValue();
			}
			
			return M_Locator_ID;
		}
		
		public int getDeliveryM_AttributeSetInstance_ID() // Common use case: set delivery locator
		{
			int M_AttributeSetInstance_ID = -1;
			
			if(additionalData.containsKey(MInOutLine.COLUMNNAME_M_AttributeSetInstance_ID))
			{
				Integer asi = (Integer)additionalData.get(MInOutLine.COLUMNNAME_M_AttributeSetInstance_ID);
				
				if(asi != null)
					M_AttributeSetInstance_ID = asi.intValue();
			}
			
			return M_AttributeSetInstance_ID;
		}
	}
	
}	//	InOutGenerate
