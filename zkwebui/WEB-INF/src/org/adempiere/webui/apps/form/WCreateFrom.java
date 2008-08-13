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

/**
 * 2007, Modified by Posterita Ltd.
 */

package org.adempiere.webui.apps.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.compiere.model.GridTab;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

/**
 * Create From (Base Class) : Based on VCreateFrom
 * 
 * @author  Niraj Sohun
 * @date    Jul 16, 2007
 */

public abstract class WCreateFrom extends Window implements EventListener, WTableModelListener, ValueChangeListener
{
	private static CLogger s_log = CLogger.getCLogger (WCreateFrom.class);
	protected CLogger log = CLogger.getCLogger(getClass());
	
	protected Hbox hboxCommon = new Hbox();
	protected Vbox parameterShipmentPanel = new Vbox(); 
	protected Hbox parameterBankPanel = new Hbox();
	protected Vbox parameterInvoicePanel = new Vbox();
	private Vbox bottomPanel = new Vbox();
	
	protected Listbox shipmentField = new Listbox();
	protected Listbox orderField = new Listbox();
	protected Listbox invoiceField = new Listbox();
	
	protected WEditor bankAccountField;
	protected WEditor bPartnerField; 
	protected WEditor locatorField;
	
	protected Button btnCancel = new Button();
	protected Button btnOk = new Button();
	protected Button btnSelectAll = new Button();

	protected Label bankAccountLabel = new Label();
	protected Label bPartnerLabel = new Label();
	protected Label shipmentLabel = new Label();
	protected Label orderLabel = new Label();
	protected Label invoiceLabel = new Label();
	protected Label locatorLabel = new Label();
	protected Label lblStatus = new Label();
	
	protected WListbox dataTable = new WListbox();
	
	protected int p_WindowNo;
	protected GridTab p_mTab;
	private boolean p_initOK;
	
	protected MOrder p_order = null;
	
	private void init()
	{
		// Common - BP and Purchase Order
		
		bPartnerLabel.setValue(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
		
		orderLabel.setValue(Msg.getElement(Env.getCtx(), "C_Order_ID", false));
		
		orderField.setRows(0);
        orderField.setMold("select");
        orderField.addEventListener(Events.ON_SELECT, this);
		
        hboxCommon.setWidth("700px");
        hboxCommon.setWidths("13%, 30%, 12%, 25%");
        
        hboxCommon.appendChild(bPartnerLabel);
        
        if (bPartnerField != null)
        	hboxCommon.appendChild(bPartnerField.getComponent());
        
        hboxCommon.appendChild(orderLabel);
        hboxCommon.appendChild(orderField);
        
        //End Common
        
        // WCreateFromShipment

		invoiceLabel.setValue(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false));
		
		locatorLabel.setValue(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		
		invoiceField.setRows(0);
        invoiceField.setMold("select");
        invoiceField.addEventListener(Events.ON_SELECT, this);
        
        Hbox boxShipment = new Hbox();
        boxShipment.setWidth("100%");
        boxShipment.setWidths("13%, 30%, 12%, 25%");
        
        boxShipment.appendChild(locatorLabel);
        
        if (locatorField != null)
        	boxShipment.appendChild(locatorField.getComponent());
        
        boxShipment.appendChild(invoiceLabel);
        boxShipment.appendChild(invoiceField);
        
        parameterShipmentPanel.setWidth("700px");
        parameterShipmentPanel.appendChild(boxShipment);
        
		// WCreateFromInvoice
		
        shipmentLabel.setValue(Msg.getElement(Env.getCtx(), "M_InOut_ID", false));
		
		shipmentField.setRows(0);
        shipmentField.setMold("select");
        shipmentField.addEventListener(Events.ON_SELECT, this);
        
        Hbox boxInvoice = new Hbox();
        boxInvoice.setWidth("100%");
        boxInvoice.setWidths("13%, 30%, 12%, 25%");
        
        boxInvoice.appendChild(new Label());
        boxInvoice.appendChild(new Label());
        boxInvoice.appendChild(shipmentLabel);
        boxInvoice.appendChild(shipmentField);
        //boxInvoice.setStyle("text-align:right");
        
        parameterInvoicePanel.setWidth("700px");
        parameterInvoicePanel.appendChild(boxInvoice);
        
		// End WCreateFromInvoice
		
        // WCreateFromStatement

		bankAccountLabel.setValue(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
        
		Hbox boxStatement = new Hbox();
		boxStatement.appendChild(bankAccountLabel);
		
		if (bankAccountField != null)
			boxStatement.appendChild(bankAccountField.getComponent());
		
		boxStatement.setStyle("text-align:center");
		
		parameterBankPanel.setWidth("700px");
		parameterBankPanel.appendChild(boxStatement);
		
		// End WCreateFromStatement
		
		// Listbox
		
		dataTable.setCheckmark(true);
		dataTable.setMultiSelection(true);
		
		
		// Buttons & Status
		
		Hbox boxButtons = new Hbox();
		boxButtons.setWidth("100%");
		boxButtons.setWidths("90%, 5%, 5%" );
		boxButtons.setStyle("text-align:left");
		
		btnCancel.addEventListener(Events.ON_CLICK, this);
		btnCancel.setImage("/images/Cancel24.gif");
		
		btnOk.addEventListener(Events.ON_CLICK, this);
		btnOk.setImage("/images/Ok24.gif");
		
		btnSelectAll.addEventListener(Events.ON_CLICK, this);
		btnSelectAll.setLabel("Select All");
		
		boxButtons.appendChild(btnSelectAll);
		boxButtons.appendChild(btnCancel);
		boxButtons.appendChild(btnOk);
		
		bottomPanel.setWidth("700px");
		bottomPanel.appendChild(boxButtons);
		bottomPanel.appendChild(lblStatus);
		
		// End Buttons & Status
		
		// Window
		
//		this.setWidth("700px");
		this.setClosable(true);
		this.setBorder("normal");
		
		this.appendChild(hboxCommon);
		this.appendChild(new Separator());
		this.appendChild(parameterInvoicePanel);
		this.appendChild(parameterBankPanel);
		this.appendChild(parameterShipmentPanel);
		this.appendChild(new Separator());
		this.appendChild(dataTable);
		this.appendChild(new Separator());
		this.appendChild(bottomPanel);
	}
	
	public static WCreateFrom create(GridTab mTab)
	{
		// Dynamic init preparation
				
		int AD_Table_ID = Env.getContextAsInt(Env.getCtx(), mTab.getWindowNo(), "BaseTable_ID");

		WCreateFrom retValue = null;

		if (AD_Table_ID == 392)             				// C_BankStatement
			retValue = new WCreateFromStatement(mTab);		
		else if (AD_Table_ID == 318)        				// C_Invoice
			retValue = new WCreateFromInvoice(mTab);
		else if (AD_Table_ID == 319)        				// M_InOut
			retValue = new WCreateFromShipment(mTab);
		else if (AD_Table_ID == 426)						// C_PaySelection
			return null;									// Ignore - will call process C_PaySelection_CreateFrom			
		else    											//  Not supported CreateFrom
		{
			s_log.info("Unsupported AD_Table_ID=" + AD_Table_ID);
			return null;
		}
		return retValue;
	}
	
	public WCreateFrom (GridTab mTab)
	{
		super();

		log.info(mTab.toString());
		p_WindowNo = mTab.getWindowNo();
		p_mTab = mTab;

		try
		{
			if (!dynInit())
				return;
			
			init();
			
			//confirmPanel.addActionListener(this);
			
			//  Set status
			
			//statusBar.setStatusDB("");
			tableChanged(null);
			p_initOK = true;
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			p_initOK = false;
		}
		AEnv.showWindow(this);
	}

	/**
	 *	Init OK to be able to make changes?
	 *  @return on if initialized
	 */
	
	public boolean isInitOK()
	{
		return p_initOK;
	}

	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	
	abstract boolean dynInit() throws Exception;

	/**
	 *  Init Business Partner Details
	 *  @param C_BPartner_ID BPartner
	 */
	
	abstract void initBPDetails(int C_BPartner_ID);

	/**
	 *  Add Info
	 */
	
	abstract void info();

	/**
	 *  Save & Insert Data
	 *  @return true if saved
	 */
	
	abstract boolean save();
	
	public void onEvent(Event e) throws Exception
	{
		//log.config("Action=" + e.getActionCommand());

		//  OK - Save

		if (e.getTarget() == btnOk)
		{
			if (save())
				this.detach();
		}
		//  Cancel
		else if (e.getTarget() == btnCancel)
		{
			this.detach();
		}
		// Select All
		// Trifon
		else if (e.getTarget() == btnSelectAll)
		{
			ListModelTable model = dataTable.getModel();
			int rows = model.size();
			
			for (int i = 0; i < rows; i++)
			{
				//model.setDataAt(new Boolean(true), i, 0);
				dataTable.addItemToSelection(dataTable.getItemAtIndex(i));
				//dataTable.setSelectedIndex(i);
			}
			info();			
		}
	//	m_action = false;
	}  
		
	public void tableChanged (WTableModelEvent e)
	{
		int type = -1;
	
		info();
		
		if (e != null)
		{
			type = e.getType();
		
			if (type != WTableModelEvent.CONTENTS_CHANGED)
				return;
		}
		log.config("Type=" + type);
		info();
	} 
	
	protected void initBPartner (boolean forInvoice) throws Exception
	{
		// Load BPartner
		
		int AD_Column_ID = 3499;        //  C_Invoice.C_BPartner_ID
		MLookup lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		
		bPartnerField = new WSearchEditor(lookup, Msg.translate(Env.getCtx(), "C_BPartner_ID"), "", true, false, true);
		bPartnerField.addValueChangeListner(this);
		
		int C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		bPartnerField.setValue(new Integer(C_BPartner_ID));

		// Initial loading
		
		initBPartnerOIS(C_BPartner_ID, forInvoice);
	}

	/**
	 *  Load PBartner dependent Order/Invoice/Shipment Field.
	 *  @param C_BPartner_ID BPartner
	 *  @param forInvoice for invoice
	 */
	
	protected void initBPartnerOIS (int C_BPartner_ID, boolean forInvoice)
	{
		log.config("C_BPartner_ID=" + C_BPartner_ID);
		KeyNamePair pp = new KeyNamePair(0,"");

		// Load PO Orders - Closed, Completed
		
		orderField.removeEventListener(Events.ON_SELECT, this);
		orderField.getChildren().clear();
		orderField.appendItem(pp.getName(), pp);
			
		//	Display
		
		StringBuffer display = new StringBuffer("o.DocumentNo||' - ' ||")
				.append(DB.TO_CHAR("o.DateOrdered", DisplayType.Date, Env.getAD_Language(Env.getCtx())))
				.append("||' - '||")
				.append(DB.TO_CHAR("o.GrandTotal", DisplayType.Amount, Env.getAD_Language(Env.getCtx())));

		String column = "m.M_InOutLine_ID";
		
		if (forInvoice)
				column = "m.C_InvoiceLine_ID";
		
		StringBuffer sql = new StringBuffer("SELECT o.C_Order_ID,").append(display)
				.append(" FROM C_Order o "
				+ "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='N' AND o.DocStatus IN ('CL','CO')"
				+ " AND o.C_Order_ID IN "
				+ "(SELECT ol.C_Order_ID FROM C_OrderLine ol"
				+ " LEFT OUTER JOIN M_MatchPO m ON (ol.C_OrderLine_ID=m.C_OrderLine_ID) "
				+ "GROUP BY ol.C_Order_ID,ol.C_OrderLine_ID, ol.QtyOrdered,").append(column)
				.append(" HAVING (ol.QtyOrdered <> SUM(m.Qty) AND ").append(column)
				.append(" IS NOT NULL) OR ").append(column).append(" IS NULL) "
				+ "ORDER BY o.DateOrdered");
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				orderField.appendItem(pp.getName(), pp);
			}
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		orderField.setSelectedIndex(0);
		orderField.addEventListener(Events.ON_SELECT, this);

		initBPDetails(C_BPartner_ID);
	}
	
	protected void loadOrder (int C_Order_ID, boolean forInvoice)
	{
		/**
		 *  Selected        - -
		 *  Qty             - 0
		 *  C_UOM_ID        - 1
		 *  M_Product_ID    - 2
		 *  VendorProductNo - 3
		 *  OrderLine       - 4
		 *  ShipmentLine    - 5
		 *  InvoiceLine     - 6
		 */
	
		log.config("C_Order_ID=" + C_Order_ID);
		p_order = new MOrder (Env.getCtx(), C_Order_ID, null);      //  save

		Vector<Vector> data = new Vector<Vector>();
		
		StringBuffer sql = new StringBuffer("SELECT "
			+ "l.QtyOrdered-SUM(COALESCE(m.Qty,0)),"									//	1
			+ "CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END,"		//	2
			+ " l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),"							//	3..4
			+ " COALESCE(l.M_Product_ID,0),COALESCE(p.Name,c.Name),po.VendorProductNo,"	//	5..7
			+ " l.C_OrderLine_ID,l.Line "												//	8..9
			+ "FROM C_OrderLine l"
			+ " LEFT OUTER JOIN M_Product_PO po ON (l.M_Product_ID = po.M_Product_ID AND l.C_BPartner_ID = po.C_BPartner_ID) "
			+ " LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND ");
		
		sql.append(forInvoice ? "m.C_InvoiceLine_ID" : "m.M_InOutLine_ID");
		sql.append(" IS NOT NULL)")
			.append(" LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID)"
			+ " LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID)");
		
		if (Env.isBaseLanguage(Env.getCtx(), "C_UOM"))
			sql.append(" LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID)");
		else
			sql.append(" LEFT OUTER JOIN C_UOM_Trl uom ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='")
				.append(Env.getAD_Language(Env.getCtx())).append("')");

		sql.append(" WHERE l.C_Order_ID=? "			//	#1
			+ "GROUP BY l.QtyOrdered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, "
			+ "l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),po.VendorProductNo, "
				+ "l.M_Product_ID,COALESCE(p.Name,c.Name), l.Line,l.C_OrderLine_ID "
			+ "ORDER BY l.Line");

		log.finer(sql.toString());
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, C_Order_ID);
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				//line.add(new Boolean(false));           //  0-Selection
			
				BigDecimal qtyOrdered = rs.getBigDecimal(1);
				BigDecimal multiplier = rs.getBigDecimal(2);
				BigDecimal qtyEntered = qtyOrdered.multiply(multiplier);
				line.add(new Double(qtyEntered.doubleValue()));  //  1-Qty
				
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(4).trim());
				line.add(pp);                           //  2-UOM
				
				pp = new KeyNamePair(rs.getInt(5), rs.getString(6));
				line.add(pp);                           //  3-Product
				line.add(rs.getString(7));				// 4-VendorProductNo
				
				pp = new KeyNamePair(rs.getInt(8), rs.getString(9));
				line.add(pp);                           //  5-OrderLine
				line.add(null);                         //  6-Ship
				line.add(null);                         //  7-Invoice
				
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		loadTableOIS (data);
	}

	/**
	 *  Load Order/Invoice/Shipment data into Table
	 *  @param data data
	 */
	
	protected void loadTableOIS (Vector data)
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(6);
		//columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_UOM_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
		columnNames.add(Msg.getElement(Env.getCtx(), "VendorProductNo", false));
		columnNames.add(Msg.getElement(Env.getCtx(), "C_Order_ID", false));
		columnNames.add(Msg.getElement(Env.getCtx(), "M_InOut_ID", false));
		columnNames.add(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false));

		//  Remove previous listeners
		//dataTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable model = new ListModelTable(data);
		//DefaultTableModel model = new DefaultTableModel(data, columnNames);
		
		model.addTableModelListener(this);
		dataTable.setData(model, columnNames);
		
		//dataTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		dataTable.setColumnClass(0, Double.class, true);        //  1-Qty
		dataTable.setColumnClass(1, String.class, true);        //  2-UOM
		dataTable.setColumnClass(2, String.class, true);        //  3-Product
		dataTable.setColumnClass(3, String.class, true);        //  4-VendorProductNo
		dataTable.setColumnClass(4, String.class, true);        //  5-Order
		dataTable.setColumnClass(5, String.class, true);        //  6-Ship
		dataTable.setColumnClass(6, String.class, true);        //  7-Invoice
		
		//  Table UI
		//dataTable.autoSize();
	}
}
