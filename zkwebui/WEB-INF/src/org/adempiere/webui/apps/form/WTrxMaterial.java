/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.adempiere.webui.apps.form;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WLocatorEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.ADTabpanel;
import org.adempiere.webui.panel.StatusBarPanel;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.model.GridWindowVO;
import org.compiere.model.MLocatorLookup;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MQuery;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Separator;

/**
 * Material Transaction History
 *
 * @author Jorg Janke
 * @version $Id: VTrxMaterial.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class WTrxMaterial extends ADForm
	implements EventListener, ValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2141669182129214237L;
	/** GridController          */
	private ADTabpanel  m_gridController = null;
	/** MWindow                 */
	private GridWindow         m_mWindow = null;
	/** MTab pointer            */
	private GridTab            m_mTab = null;

	private MQuery          m_staticQuery = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(WTrxMaterial.class);
	//
	private Panel mainPanel = new Panel();
	private Borderlayout mainLayout = new Borderlayout();
	private Panel parameterPanel = new Panel();
	private Label orgLabel = new Label();
	private WTableDirEditor orgField;
	private Label locatorLabel = new Label();
	private WLocatorEditor locatorField;
	private Label productLabel = new Label();
	private WSearchEditor productField;
	private Label dateFLabel = new Label();
	private WDateEditor dateFField;
	private Label dateTLabel = new Label();
	private WDateEditor dateTField;
	private Label mtypeLabel = new Label();
	private WTableDirEditor mtypeField;
	private Grid parameterLayout = GridFactory.newGridLayout();
	private Panel southPanel = new Panel();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true, true, false, false, false, true, false);
	private StatusBarPanel statusBar = new StatusBarPanel();


	/**
	 *	Initialize Panel
	 */
	protected void initForm()
	{
		log.info("");
		try
		{
			dynParameter();
			zkInit();
			dynInit();			
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
	}	//	init
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	void zkInit() throws Exception
	{
		this.appendChild(mainPanel);
		mainPanel.setStyle("width: 99%; height: 100%; border: none; padding: 0; margin: 0");
		mainPanel.appendChild(mainLayout);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		parameterPanel.appendChild(parameterLayout);
		//
		orgLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		locatorLabel.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		productLabel.setText(Msg.translate(Env.getCtx(), "Product"));
		dateFLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		dateTLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		mtypeLabel.setText(Msg.translate(Env.getCtx(), "MovementType"));
		//
		North north = new North();
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);
		
		Rows rows = parameterLayout.newRows();
		Row row = rows.newRow();
		row.appendChild(orgLabel.rightAlign());
		row.appendChild(orgField.getComponent());
		row.appendChild(mtypeLabel.rightAlign());
		row.appendChild(mtypeField.getComponent());
		row.appendChild(dateFLabel.rightAlign());
		row.appendChild(dateFField.getComponent());

		row = rows.newRow();
		row.appendChild(locatorLabel.rightAlign());
		row.appendChild(locatorField.getComponent());
		row.appendChild(productLabel.rightAlign());
		row.appendChild(productField.getComponent());
		row.appendChild(dateTLabel.rightAlign());
		row.appendChild(dateTField.getComponent());
		//
		southPanel.appendChild(confirmPanel);
		southPanel.appendChild(new Separator());
		southPanel.appendChild(statusBar);
		South south = new South();
		south.setStyle("border: none");
		mainLayout.appendChild(south);
		south.appendChild(southPanel);
		
		LayoutUtils.addSclass("status-border", statusBar);
	}   //  jbInit

	/**
	 *  Initialize Parameter fields
	 *  @throws Exception if Lookups cannot be initialized
	 */
	private void dynParameter() throws Exception
	{
		Properties ctx = Env.getCtx();
		//  Organization
		MLookup orgLookup = MLookupFactory.get (ctx, m_WindowNo, 0, 3660, DisplayType.TableDir);
		orgField = new WTableDirEditor("AD_Org_ID", false, false, true, orgLookup);
	//	orgField.addVetoableChangeListener(this);
		//  Locator
		MLocatorLookup locatorLookup = new MLocatorLookup(ctx, m_WindowNo);
		locatorField = new WLocatorEditor ("M_Locator_ID", false, false, true, locatorLookup, m_WindowNo);
	//	locatorField.addVetoableChangeListener(this);
		//  Product
		MLookup productLookup = MLookupFactory.get (ctx, m_WindowNo, 0, 3668, DisplayType.Search);
		productField = new WSearchEditor("M_Product_ID", false, false, true, productLookup);
		productField.addValueChangeListener(this);
		//  Movement Type
		MLookup mtypeLookup = MLookupFactory.get (ctx, m_WindowNo, 0, 3666, DisplayType.List);
		mtypeField = new WTableDirEditor("MovementType", false, false, true, mtypeLookup);
		//  Dates
		dateFField = new WDateEditor("DateFrom", false, false, true, Msg.getMsg(Env.getCtx(), "DateFrom"));
		dateTField = new WDateEditor("DateTo", false, false, true, Msg.getMsg(Env.getCtx(), "DateTo"));
		//
		confirmPanel.addActionListener(this);
		statusBar.setStatusLine("");
	}   //  dynParameter

	/**
	 *  Dynamic Layout (Grid).
	 * 	Based on AD_Window: Material Transactions
	 */
	private void dynInit()
	{
		m_staticQuery = new MQuery();
		m_staticQuery.addRestriction("AD_Client_ID", MQuery.EQUAL, Env.getAD_Client_ID(Env.getCtx()));
		int AD_Window_ID = 223;		//	Hardcoded
		GridWindowVO wVO = AEnv.getMWindowVO (m_WindowNo, AD_Window_ID, 0);
		if (wVO == null)
			return;
		m_mWindow = new GridWindow (wVO);
		m_mTab = m_mWindow.getTab(0);
		m_mWindow.initTab(0);
		//
		m_gridController = new ADTabpanel();
		m_gridController.init(null, m_WindowNo, m_mTab, m_mWindow);
		if (!m_gridController.isGridView())
			m_gridController.switchRowPresentation();
		Center center = new Center();
		mainLayout.appendChild(center);
		center.setFlex(true);
		center.appendChild(m_gridController);
		//
		m_mTab.setQuery(MQuery.getEqualQuery("1", "2"));
		m_mTab.query(false);
		statusBar.setStatusLine(" ", false);
		statusBar.setStatusDB(" ");
	}   //  dynInit


	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		SessionManager.getAppDesktop().closeActiveWindow();
	}	//	dispose

	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void onEvent (Event e)
	{
		if (e.getTarget().getId().equals(ConfirmPanel.A_CANCEL))
			dispose();
		else if (e.getTarget().getId().equals(ConfirmPanel.A_REFRESH)
				|| e.getTarget().getId().equals(ConfirmPanel.A_OK))
			refresh();
		else if (e.getTarget().getId().equals(ConfirmPanel.A_ZOOM))
			zoom();
	}   //  actionPerformed

	
	/**************************************************************************
	 *  Property Listener
	 *  @param e event
	 */
	public void valueChange (ValueChangeEvent e)
	{
		if (e.getPropertyName().equals("M_Product_ID"))
			productField.setValue(e.getNewValue());
	}   //  vetoableChange


	
	/**************************************************************************
	 *  Refresh - Create Query and refresh grid
	 */
	private void refresh()
	{
		/**
		 *  Create Where Clause
		 */
		MQuery query = m_staticQuery.deepCopy();
		//  Organization
		Object value = orgField.getValue();
		if (value != null && value.toString().length() > 0)
			query.addRestriction("AD_Org_ID", MQuery.EQUAL, value);
		//  Locator
		value = locatorField.getValue();
		if (value != null && value.toString().length() > 0)
			query.addRestriction("M_Locator_ID", MQuery.EQUAL, value);
		//  Product
		value = productField.getValue();
		if (value != null && value.toString().length() > 0)
			query.addRestriction("M_Product_ID", MQuery.EQUAL, value);
		//  MovementType
		value = mtypeField.getValue();
		if (value != null && value.toString().length() > 0)
			query.addRestriction("MovementType", MQuery.EQUAL, value);
		//  DateFrom
		Timestamp ts = (Timestamp)dateFField.getValue();
		if (ts != null)
			query.addRestriction("TRUNC(MovementDate)", MQuery.GREATER_EQUAL, ts);
		//  DateTO
		ts = (Timestamp)dateTField.getValue();
		if (ts != null)
			query.addRestriction("TRUNC(MovementDate)", MQuery.LESS_EQUAL, ts);
		log.info( "VTrxMaterial.refresh query=" + query.toString());

		/**
		 *  Refresh/Requery
		 */
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "StartSearch"), false);
		//
		m_mTab.setQuery(query);
		m_mTab.query(false);
		//
		int no = m_mTab.getRowCount();
		statusBar.setStatusLine(" ", false);
		statusBar.setStatusDB(Integer.toString(no));
	}   //  refresh

	/**
	 *  Zoom
	 */
	private void zoom()
	{
		log.info("");
		//
		int AD_Window_ID = 0;
		String ColumnName = null;
		String SQL = null;
		//
		int lineID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "M_InOutLine_ID");
		if (lineID != 0)
		{
			log.fine("M_InOutLine_ID=" + lineID);
			if (Env.getContext(Env.getCtx(), m_WindowNo, "MovementType").startsWith("C"))
				AD_Window_ID = 169;     //  Customer
			else
				AD_Window_ID = 184;     //  Vendor
			ColumnName = "M_InOut_ID";
			SQL = "SELECT M_InOut_ID FROM M_InOutLine WHERE M_InOutLine_ID=?";
		}
		else
		{
			lineID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "M_InventoryLine_ID");
			if (lineID != 0)
			{
				log.fine("M_InventoryLine_ID=" + lineID);
				AD_Window_ID = 168;
				ColumnName = "M_Inventory_ID";
				SQL = "SELECT M_Inventory_ID FROM M_InventoryLine WHERE M_InventoryLine_ID=?";
			}
			else
			{
				lineID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "M_MovementLine_ID");
				if (lineID != 0)
				{
					log.fine("M_MovementLine_ID=" + lineID);
					AD_Window_ID = 170;
					ColumnName = "M_Movement_ID";
					SQL = "SELECT M_Movement_ID FROM M_MovementLine WHERE M_MovementLine_ID=?";
				}
				else
				{
					lineID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "M_ProductionLine_ID");
					if (lineID != 0)
					{
						log.fine("M_ProductionLine_ID=" + lineID);
						AD_Window_ID = 191;
						ColumnName = "M_Production_ID";
						SQL = "SELECT M_Production_ID FROM M_ProductionLine WHERE M_ProductionLine_ID=?";
					}
					else
						log.fine("Not found WindowNo=" + m_WindowNo);
				}
			}
		}
		if (AD_Window_ID == 0)
			return;

		//  Get Parent ID
		int parentID = 0;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setInt(1, lineID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				parentID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		MQuery query = MQuery.getEqualQuery(ColumnName, parentID);
		log.config("AD_Window_ID=" + AD_Window_ID + " - " + query);
		if (parentID == 0)
			log.log(Level.SEVERE, "No ParentValue - " + SQL + " - " + lineID);

		//  Zoom
		AEnv.zoom(AD_Window_ID, query);
	}   //  zoom

}   //  VTrxMaterial
