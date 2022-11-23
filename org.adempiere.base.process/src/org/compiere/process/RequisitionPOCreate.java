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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.NoVendorForProductException;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.compiere.model.MBPartner;
import org.compiere.model.MCharge;
import org.compiere.model.MConversionType;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPO;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.PO;
import org.compiere.model.POResultSet;
import org.compiere.model.Query;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import it.idempiere.base.util.STDSysConfig;

/**
 * 	Create PO from Requisition 
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: RequisitionPOCreate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  @author Teo Sarca, www.arhipac.ro
 *  		<li>BF [ 2609760 ] RequisitionPOCreate not using DateRequired
 *  		<li>BF [ 2605888 ] CreatePOfromRequisition creates more PO than needed
 *  		<li>BF [ 2811718 ] Create PO from Requsition without any parameter teminate in NPE
 *  			http://sourceforge.net/tracker/?func=detail&atid=879332&aid=2811718&group_id=176962
 *  		<li>FR [ 2844074  ] Requisition PO Create - more selection fields
 *  			https://sourceforge.net/tracker/?func=detail&aid=2844074&group_id=176962&atid=879335
 *  
 *  @author Silvano Trinchero, www.freepath.it
 *  		<li>FR [ 3471930 ] Alllow consolidation of PO from Req. with different dates
 *  			https://sourceforge.net/tracker/?func=detail&aid=3471930&group_id=176962&atid=879335 
 */
public class RequisitionPOCreate extends SvrProcess
{
	/** Org					*/
	protected int			p_AD_Org_ID = 0;
	/** Warehouse			*/
	protected int			p_M_Warehouse_ID = 0;
	/**	Doc Date From		*/
	protected Timestamp	p_DateDoc_From;
	/**	Doc Date To			*/
	protected Timestamp	p_DateDoc_To;
	/**	Doc Date From		*/
	protected Timestamp	p_DateRequired_From;
	/**	Doc Date To			*/
	protected Timestamp	p_DateRequired_To;
	/** Priority			*/
	protected String		p_PriorityRule = null;
	/** User				*/
	protected int			p_AD_User_ID = 0;
	/** Product				*/
	protected int			p_M_Product_ID = 0;
	/** Product	Category	*/
	protected int			p_M_Product_Category_ID = 0;
	/** BPartner Group	*/
	protected int			p_C_BP_Group_ID = 0;
	/** Requisition			*/
	protected int 		p_M_Requisition_ID = 0;
	//F3P: gestione fornitori
	/** BP			*/
	protected int 		p_C_BPartner_ID = 0;
	
	/** BP Filtro			*/
	protected int 		p_Vendor_ID = 0;
	//end
	
	protected int 		p_C_Order_ID = 0;

	/** Consolidate			*/
	protected boolean		p_ConsolidateDocument = false;
	
	/** FR [ 3471930 ] added new parameter **/
	protected boolean		p_ConsolidateByDatePromised = true;
	
	/** Order				*/
	protected MOrder		m_order = null;
	
	protected MOrder		m_orderToAdd = null;
	
	/** Order Line			*/
	protected MOrderLine	m_orderLine = null;
	/** Orders Cache : (C_BPartner_ID, DateRequired, M_PriceList_ID) -> MOrder */
	protected HashMap<MultiKey, MOrder> m_cacheOrders = new HashMap<MultiKey, MOrder>();
	
	protected int m_M_Warehouse_ID = 0; //F3P: Gestione rottura per magazzino
	
	/** DR Aggiunto parametro per il completamento pre-generazione ordine **/
	protected boolean		p_CompleteBeforeGenerateOrder = false;
	
	/**	Manual Selection		*/
	protected boolean 	 	p_Selection = false;
	
	protected List<MOrder> m_orders_generated = new ArrayList<>();
	
	/** LS Aggiunto parametro per consolidare le linee per prodotto con data richiesta minore**/
	protected boolean		p_consolidateByRequisitionDate = true;
	/** LS Aggiunto parametri per spezzare per singola linea di RDA **/
	protected boolean		p_breakForRDALine = false;
	protected boolean		p_useRDALinePrice = false;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null)
				;
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else if (name.equals("DateDoc"))
			{
				p_DateDoc_From = (Timestamp)para[i].getParameter();
				p_DateDoc_To = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("DateRequired"))
			{
				p_DateRequired_From = (Timestamp)para[i].getParameter();
				p_DateRequired_To = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("PriorityRule"))
				p_PriorityRule = (String)para[i].getParameter();
			else if (name.equals("AD_User_ID"))
				p_AD_User_ID = para[i].getParameterAsInt();
			else if (name.equals("Selection"))
				p_Selection = "Y".equals(para[i].getParameter());
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Requisition_ID"))
				p_M_Requisition_ID = para[i].getParameterAsInt();
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = "Y".equals(para[i].getParameter());
			else if (name.equals("ConsolidateByDatePromised")) // FR [ 3471930 ] read value for new parameter
				p_ConsolidateByDatePromised = para[i].getParameterAsBoolean();
			//F3P: gestione fornitori
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("Vendor_ID"))
				p_Vendor_ID = para[i].getParameterAsInt();
			else if (name.equals("CompleteBeforeGenerateOrder")) //DR vedo se completare prima gli ordini.
				p_CompleteBeforeGenerateOrder = para[i].getParameterAsBoolean();	
			else if (name.equalsIgnoreCase("ConsolidateByRequisitionDate"))
				p_consolidateByRequisitionDate = para[i].getParameterAsBoolean();
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = para[i].getParameterAsInt();
			//end
			else if(name.equalsIgnoreCase("BreakForRDALine"))
				p_breakForRDALine = para[i].getParameterAsBoolean();
			else if(name.equalsIgnoreCase("UseRDALinePrice"))
				p_useRDALinePrice = para[i].getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		if (p_C_Order_ID > 0)
		{
			m_orderToAdd = PO.get(getCtx(), MOrder.Table_Name, p_C_Order_ID, get_TrxName());
			m_orders_generated.add(m_orderToAdd);
		}
		
		if(p_Selection) // F3P: add support for T_Selection
		{
			String sql = getTSelectionQuery() + getOrderByClause();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
//			boolean error= false;
			
			Map<Integer,MRequisition> knownRequisition = new HashMap<>();
			
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, getAD_PInstance_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					int Record_ID = rs.getInt(1);
					
					MRequisitionLine mReqLine = PO.get(getCtx(), MRequisitionLine.Table_Name, Record_ID, get_TrxName());
					
					if(p_CompleteBeforeGenerateOrder) // Complete is only relevat for T_Selection processing
					{
						int M_Requisition_ID = mReqLine.getM_Requisition_ID();
						MRequisition mReq = knownRequisition.get(M_Requisition_ID);
						
						if(mReq == null)
						{
							mReq = PO.get(getCtx(), MRequisition.Table_Name, M_Requisition_ID, get_TrxName());
							
							if(!MRequisition.DOCSTATUS_Completed.equals(mReq))
								completeRequisiton(mReq);
							
							knownRequisition.put(M_Requisition_ID, mReq);
						}						
					}
					
					process(mReqLine);
					
				}
				
				closeOrder();
			}
			catch (Exception e)
			{
//				error = true;
				log.log(Level.SEVERE, sql, e);
				addLog(e.getMessage());
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			
			/* F3P: log gia' generato durante la closeOrder			 
			 
			if(m_orders_generated != null && m_orders_generated.isEmpty()==false && error ==false)
			{
				for(MOrder order:m_orders_generated)
				{
					addLog(order.get_ID(), order.getDateOrdered(),null, "", order.get_Table_ID(), order.get_ID());
				}
			}
			*/
		}
		else 
		{
			if (p_M_Requisition_ID != 0)
			{
				if (log.isLoggable(Level.INFO)) log.info("M_Requisition_ID=" + p_M_Requisition_ID);
				MRequisition req = new MRequisition(getCtx(), p_M_Requisition_ID, get_TrxName());
				
				if(p_CompleteBeforeGenerateOrder && !MRequisition.DOCSTATUS_Completed.equals(req.getDocStatus()))
					completeRequisiton(req);
				
				if (!MRequisition.DOCSTATUS_Completed.equals(req.getDocStatus()))
				{
					throw new AdempiereUserError("@DocStatus@ = " + req.getDocStatus());
				}
				MRequisitionLine[] lines = req.getLines();
				for (int i = 0; i < lines.length; i++)
				{
					if (lines[i].getC_OrderLine_ID() == 0)
					{
						process (lines[i]);
					}
				}
				closeOrder();
				return "";
			}	//	single Requisition
			
			//	
			if (log.isLoggable(Level.INFO)) log.info("AD_Org_ID=" + p_AD_Org_ID
				+ ", M_Warehouse_ID=" + p_M_Warehouse_ID
				+ ", DateDoc=" + p_DateDoc_From + "/" + p_DateDoc_To
				+ ", DateRequired=" + p_DateRequired_From + "/" + p_DateRequired_To
				+ ", PriorityRule=" + p_PriorityRule
				+ ", AD_User_ID=" + p_AD_User_ID
				+ ", M_Product_ID=" + p_M_Product_ID
				+ ", ConsolidateDocument=" + p_ConsolidateDocument
				+ ", ConsolidateByDatePromised=" + p_ConsolidateByDatePromised); // FR [ 3471930 ] added param to log, minor typo in ConsolidateDocument line
			
			ArrayList<Object> params = new ArrayList<Object>();
			StringBuilder whereClause = new StringBuilder("C_OrderLine_ID IS NULL");
			if (p_AD_Org_ID > 0)
			{
				whereClause.append(" AND AD_Org_ID=?");
				params.add(p_AD_Org_ID);
			}
			if (p_M_Product_ID > 0)
			{
				whereClause.append(" AND M_Product_ID=?");
				params.add(p_M_Product_ID);
			}
			else if (p_M_Product_Category_ID > 0)
			{
				whereClause.append(" AND EXISTS (SELECT 1 FROM M_Product p WHERE M_RequisitionLine.M_Product_ID=p.M_Product_ID")
					.append(" AND p.M_Product_Category_ID=?)");
				params.add(p_M_Product_Category_ID);
			}
			
			if (p_C_BP_Group_ID > 0)
			{
				whereClause.append(" AND (")
				.append("M_RequisitionLine.C_BPartner_ID IS NULL")
				.append(" OR EXISTS (SELECT 1 FROM C_BPartner bp WHERE M_RequisitionLine.C_BPartner_ID=bp.C_BPartner_ID AND bp.C_BP_Group_ID=?)")
				.append(")");
				params.add(p_C_BP_Group_ID);
			}
			
			//
			//	Requisition Header
			whereClause.append(" AND EXISTS (SELECT 1 FROM M_Requisition r WHERE M_RequisitionLine.M_Requisition_ID=r.M_Requisition_ID")
				.append(" AND r.DocStatus=?");
			params.add(MRequisition.DOCSTATUS_Completed);
			
			if (p_Vendor_ID > 0)
			{
				whereClause.append(" AND coalesce(M_RequisitionLine.C_BPartner_ID,r.C_BPartner_ID) = ? ");
				params.add(p_Vendor_ID);
			}
			if (p_M_Warehouse_ID > 0)
			{
				whereClause.append(" AND r.M_Warehouse_ID=?");
				params.add(p_M_Warehouse_ID);
			}
			if (p_DateDoc_From != null)
			{
				whereClause.append(" AND r.DateDoc >= ?");
				params.add(p_DateDoc_From);
			}
			if (p_DateDoc_To != null)
			{
				whereClause.append(" AND r.DateDoc <= ?");
				params.add(p_DateDoc_To);
			}
			if (p_DateRequired_From != null)
			{
				whereClause.append(" AND r.DateRequired >= ?");
				params.add(p_DateRequired_From);
			}
			if (p_DateRequired_To != null)
			{
				whereClause.append(" AND r.DateRequired <= ?");
				params.add(p_DateRequired_To);
			}
			if (p_PriorityRule != null)
			{
				whereClause.append(" AND r.PriorityRule >= ?");
				params.add(p_PriorityRule);
			}
			if (p_AD_User_ID > 0)
			{
				whereClause.append(" AND r.AD_User_ID=?");
				params.add(p_AD_User_ID);
			}
			whereClause.append(")"); // End Requisition Header
			//
			// ORDER BY clause
			String orderClause = getOrderByClause(); // F3P: externalized order by clause
			
			POResultSet<MRequisitionLine> rs = new Query(getCtx(), MRequisitionLine.Table_Name, whereClause.toString(), get_TrxName())
					.setParameters(params)
					.setOrderBy(orderClause.toString())
					.setClient_ID()
					.scroll();
			 
			try
			{
				while (rs.hasNext())
				{
					process(rs.next());
				}
			}
			finally
			{
				DB.close(rs); rs = null;
			}
				
			closeOrder();
		}
		
		return "";
	}	//	doit
		
	protected int 		m_M_Requisition_ID = 0;
	protected int 		m_M_Product_ID = 0;
	protected int			m_M_AttributeSetInstance_ID = 0;
	/** BPartner				*/
	protected MBPartner	m_bpartner = null;
	
	/**
	 * 	Process Line
	 *	@param rLine request line
	 * 	@throws Exception
	 */
	protected void process (MRequisitionLine rLine) throws Exception
	{
		if (rLine.getM_Product_ID() == 0 && rLine.getC_Charge_ID() == 0)
		{
			log.warning("Ignored Line" + rLine.getLine() 
				+ " " + rLine.getDescription()
				+ " - " + rLine.getLineNetAmt());
			return;
		}
		
		if (!p_ConsolidateDocument && rLine.getM_Requisition_ID() != m_M_Requisition_ID)
		{
			closeOrder();
		}
		if (m_orderLine == null
			|| p_breakForRDALine 
			|| rLine.getM_Product_ID() != m_M_Product_ID
			|| rLine.getM_AttributeSetInstance_ID() != m_M_AttributeSetInstance_ID
			|| rLine.getC_Charge_ID() != 0		//	single line per charge
			|| m_order == null
			|| (p_ConsolidateByDatePromised == true && m_order.getDatePromised().compareTo(rLine.getDateRequired()) != 0) // FR [ 3471930 ] If consolidation by date promised is true, then consider it
			// FR [ 3471930 ] Due to the changes to the previous line, we need to check if two lines differ by date and avoid consolidating them
			|| (p_consolidateByRequisitionDate == true && m_orderLine.getDatePromised().compareTo(rLine.getDateRequired()) != 0) 
			|| (m_M_Warehouse_ID != rLine.getM_Requisition().getM_Warehouse_ID()//F3P: rottura ordine per magazzino
			|| (rLine.get_ValueAsInt("PP_OrderNode_ID") > 0)) 
			)
		{
			newLine(rLine);
			// No Order Line was produced (vendor was not valid/allowed) => SKIP
			if (m_orderLine == null)
				return;
		}

		//	Update Order Line
		m_orderLine.setQty(m_orderLine.getQtyOrdered().add(rLine.getQty()));
		if(p_breakForRDALine && p_useRDALinePrice && rLine.getPriceActual().signum() != 0)
		{
			m_orderLine.setPrice(rLine.getPriceActual());
		}
		else
			m_orderLine.setPrice();

		//	Update Requisition Line
		rLine.setC_OrderLine_ID(m_orderLine.getC_OrderLine_ID());
		rLine.saveEx();
	}	//	process
	
	/**
	 * 	Create new Order
	 *	@param rLine request line
	 *	@param C_BPartner_ID b.partner
	 * 	@throws Exception
	 */
	protected void newOrder(MRequisitionLine rLine, int C_BPartner_ID) throws Exception
	{
		if (m_order != null)
		{
			closeOrder();
		}
		
		//	BPartner
		if (m_bpartner == null || C_BPartner_ID != m_bpartner.get_ID())
		{
			m_bpartner = MBPartner.get(getCtx(), C_BPartner_ID);
		}
		
		int M_PriceList_ID = 0;
		
		//	Order
		Timestamp DateRequired = rLine.getDateRequired();
		
		if(STDSysConfig.isReqPOCreatePreferBPPriceList(rLine.getAD_Client_ID(),rLine.getAD_Org_ID()))
		{
			M_PriceList_ID = m_bpartner.getPO_PriceList_ID();
			
			if(M_PriceList_ID <= 0)
			{
				M_PriceList_ID = rLine.getParent().getM_PriceList_ID();
			}
		}
		else
		{
			M_PriceList_ID = rLine.getParent().getM_PriceList_ID(); 
		}
		
		//
		int M_Warehouse_ID = rLine.getParent().getM_Warehouse_ID();
		MultiKey key = new MultiKey(C_BPartner_ID, DateRequired, M_PriceList_ID,M_Warehouse_ID);
		m_order = m_cacheOrders.get(key);
		if (m_order == null && m_orderToAdd == null)
		{
			m_order = new MOrder(getCtx(), 0, get_TrxName());
			m_order.setAD_Org_ID(rLine.getAD_Org_ID());
			m_order.setM_Warehouse_ID(rLine.getParent().getM_Warehouse_ID());
			m_order.setDatePromised(DateRequired);
			m_order.setIsSOTrx(false);
			m_order.setC_DocTypeTarget_ID();
			m_order.setBPartner(m_bpartner);
			m_order.setM_PriceList_ID(M_PriceList_ID);
			m_order.setM_Warehouse_ID(rLine.getM_Requisition().getM_Warehouse_ID()); //F3P: il magazzino viene settato uguale a quello della RdA
			if (MConversionType.getDefault(getAD_Client_ID()) > 0)
				m_order.setC_ConversionType_ID(MConversionType.getDefault(getAD_Client_ID()));
			//	default po document type
			if (!p_ConsolidateDocument)
			{
				StringBuilder msgsd= new StringBuilder().append(Msg.getElement(getCtx(), "M_Requisition_ID")) 
						.append(": ").append(rLine.getParent().getDocumentNo());
				m_order.setDescription(msgsd.toString());
			}
			
			setOrderCustomFields(rLine, C_BPartner_ID);
			
			//	Prepare Save
			m_order.saveEx();
			m_orders_generated.add(m_order);
			
			// Put to cache
			m_cacheOrders.put(key, m_order);
		}
		m_M_Requisition_ID = rLine.getM_Requisition_ID();
		m_M_Warehouse_ID = rLine.getM_Requisition().getM_Warehouse_ID(); //F3P: Set della variabile globale usata per la rottura
		
	}	//	newOrder

	/**
	 * 	Close Order
	 * 	@throws Exception
	 */
	protected void closeOrder() throws Exception
	{
		if (m_orderLine != null)
		{
			m_orderLine.saveEx();
		}
		if (m_order != null)
		{
			m_order.load(get_TrxName());
			String message = Msg.parseTranslation(getCtx(), "@C_Order_ID@ " + m_order.getDocumentNo());
			addBufferLog(0, null, m_order.getGrandTotal(), message, m_order.get_Table_ID(), m_order.getC_Order_ID());
		}
		m_order = null;
		m_orderLine = null;
	}	//	closeOrder

	
	/**
	 * 	New Order Line (different Product)
	 *	@param rLine request line
	 * 	@throws Exception
	 */
	protected void newLine(MRequisitionLine rLine) throws Exception
	{
		if (m_orderLine != null)
		{
			m_orderLine.saveEx();
		}
		m_orderLine = null;
		MProduct product = MProduct.get(getCtx(), rLine.getM_Product_ID());

		//	Get Business Partner
		int C_BPartner_ID = rLine.getC_BPartner_ID();
		//F3P: gestione fornitori
		if(p_C_BPartner_ID > 0)
			C_BPartner_ID = p_C_BPartner_ID;
		//end
		if (C_BPartner_ID != 0)
		{
			;
		}
		else if (rLine.getC_Charge_ID() != 0)
		{
			MCharge charge = MCharge.get(getCtx(), rLine.getC_Charge_ID());
			C_BPartner_ID = charge.getC_BPartner_ID();
			if (C_BPartner_ID == 0)
			{
				throw new AdempiereUserError("No Vendor for Charge " + charge.getName());
			}
		}
		else
		{
			// Find Strategic Vendor for Product
			// TODO: refactor
			MProductPO[] ppos = MProductPO.getOfProduct(getCtx(), product.getM_Product_ID(), null);
			for (int i = 0; i < ppos.length; i++)
			{
				if (ppos[i].isCurrentVendor() && ppos[i].getC_BPartner_ID() != 0)
				{
					C_BPartner_ID = ppos[i].getC_BPartner_ID();
					break;
				}
			}
			if (C_BPartner_ID == 0 && ppos.length > 0)
			{
				C_BPartner_ID = ppos[0].getC_BPartner_ID();
			}
			if (C_BPartner_ID == 0)
			{
				throw new NoVendorForProductException(product.getName());
			}
		}
		
		if (!isGenerateForVendor(C_BPartner_ID))
		{
			if (log.isLoggable(Level.INFO)) log.info("Skip for partner "+C_BPartner_ID);
			return;
		}

		//	New Order - Different Vendor
		if (m_order == null 
			|| m_order.getC_BPartner_ID() != C_BPartner_ID
			|| (p_ConsolidateByDatePromised == true && m_order.getDatePromised().compareTo(rLine.getDateRequired()) != 0) // FR [ 3471930 ] If consolidation by date promised is true, then consider it
			|| (rLine.getM_Requisition().getM_Warehouse_ID() != m_M_Warehouse_ID)
			)
		{
			newOrder(rLine, C_BPartner_ID);
		}
		
		if(m_orderToAdd != null)
			m_orderLine = MOrderLine.createFromOrder(m_orderToAdd);
		else
			m_orderLine = MOrderLine.createFromOrder(m_order);
		
		m_orderLine.setDatePromised(rLine.getDateRequired());
		if (product != null)
		{
			m_orderLine.setProduct(product);
			m_orderLine.setM_AttributeSetInstance_ID(rLine.getM_AttributeSetInstance_ID());
		}
		else
		{
			m_orderLine.setC_Charge_ID(rLine.getC_Charge_ID());
			m_orderLine.setPriceActual(rLine.getPriceActual());
		}
		m_orderLine.setAD_Org_ID(rLine.getAD_Org_ID());
				
		
		//	Prepare Save
		m_M_Product_ID = rLine.getM_Product_ID();
		m_M_AttributeSetInstance_ID = rLine.getM_AttributeSetInstance_ID();
		
		setOrderLineCustomFields(rLine);
		
		m_orderLine.saveEx();
	}	//	newLine

	/**
	 * Do we need to generate Purchase Orders for given Vendor 
	 * @param C_BPartner_ID
	 * @return true if it's allowed
	 */
	protected boolean isGenerateForVendor(int C_BPartner_ID)
	{
		// No filter group was set => generate for all vendors
		if (p_C_BP_Group_ID <= 0)
			return true;
		
		if (m_excludedVendors.contains(C_BPartner_ID))
			return false;
		//
		boolean match = new Query(getCtx(), MBPartner.Table_Name, "C_BPartner_ID=? AND C_BP_Group_ID=?", get_TrxName())
		.setParameters(new Object[]{C_BPartner_ID, p_C_BP_Group_ID})
		.match();
		if (!match)
		{
			m_excludedVendors.add(C_BPartner_ID);
		}
		return match;
	}
	
	protected boolean completeRequisiton(MRequisition req)
	{		
		String docStatus = req.getDocStatus();
		boolean processed = false;
		
		if(MRequisition.DOCSTATUS_Drafted.equals(docStatus) || 
			MRequisition.DOCSTATUS_InProgress.equals(docStatus))
		{
			req.setDocAction(MRequisition.DOCACTION_Complete);
			
			try
			{
				if(req.processIt(MRequisition.DOCACTION_Complete) == false)
				{
					String processMsg = req.getProcessMsg();
					throw new AdempiereException(processMsg);
				}
			}
			catch(Exception e)
			{
				throw new AdempiereException(e);
			}

			processed = true;
		}
		
		return processed;
	}
	
	protected List<Integer> m_excludedVendors = new ArrayList<Integer>();
	
	//LS per override Query T_Selection
	protected String getTSelectionQuery()
	{
		return "SELECT M_RequisitionLine.M_RequisitionLine_ID "
				+ " FROM M_RequisitionLine M_RequisitionLine "
				+ " JOIN T_Selection s on (M_RequisitionLine.M_RequisitionLine_ID = s.t_selection_ID) "
				+ " WHERE s.AD_PInstance_ID = ?"
				+ " ORDER BY ";
	}
	
	// F3P: Common order by clause for standard and T_Selection processing
	
	protected String getOrderByClause()
	{
		StringBuilder orderClause = new StringBuilder();
		
		if (!p_ConsolidateDocument)
		{
			orderClause.append("M_RequisitionLine.M_Requisition_ID, ");
		}
		
		// B.Partner
		
		if(p_C_BPartner_ID <= 0)
		{
			orderClause.append("COALESCE(M_RequisitionLine.C_BPartner_ID,");
			orderClause.append("(SELECT C_BPartner_ID FROM C_Charge WHERE C_Charge.C_Charge_ID = M_RequisitionLine.C_Charge_ID),");
			orderClause.append("(SELECT C_BPartner_ID FROM M_Product_PO WHERE M_Product_PO.M_Product_ID = M_RequisitionLine.M_Product_ID ORDER BY IsCurrentVendor DESC FETCH FIRST 1 ROWS ONLY)) NULLS FIRST,"); // Nulls firts -> fast fail if no BP
		}
		
		if(p_consolidateByRequisitionDate) 
		{
			orderClause.append("(SELECT DateRequired FROM M_Requisition r WHERE M_RequisitionLine.M_Requisition_ID=r.M_Requisition_ID),");
			orderClause.append("M_RequisitionLine.M_Product_ID, M_RequisitionLine.C_Charge_ID, M_RequisitionLine.M_AttributeSetInstance_ID");
		}
		else
		{
			orderClause.append("M_RequisitionLine.M_Product_ID, ");//LS consolido per prodotto
			orderClause.append("(SELECT DateRequired FROM M_Requisition r WHERE M_RequisitionLine.M_Requisition_ID=r.M_Requisition_ID),");
			orderClause.append(" M_RequisitionLine.C_Charge_ID, M_RequisitionLine.M_AttributeSetInstance_ID");			
		}
		return orderClause.toString();
	}
	
	/*LS set custom fields metods*/
	protected void setOrderCustomFields(MRequisitionLine rLine, int C_BPartner_ID)
	{
		return;
	}
	
	protected void setOrderLineCustomFields(MRequisitionLine rLine)
	{
		return;
	}
	/*LS END*/
	
}	//	RequisitionPOCreate
