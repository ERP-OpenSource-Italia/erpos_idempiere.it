/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
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
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.editor;

import static org.compiere.model.SystemIDs.COLUMN_C_INVOICELINE_M_PRODUCT_ID;
import static org.compiere.model.SystemIDs.COLUMN_C_INVOICE_C_BPARTNER_ID;

import java.beans.PropertyChangeEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.ValuePreference;
import org.adempiere.webui.adwindow.ADWindow;
import org.adempiere.webui.adwindow.ADWindowContent;
import org.adempiere.webui.adwindow.IFieldEditorContainer;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Searchbox;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.factory.InfoManager;
import org.adempiere.webui.grid.WQuickEntry;
import org.adempiere.webui.panel.IHelpContext;
import org.adempiere.webui.panel.InfoPanel;
import org.adempiere.webui.part.WindowContainer;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.window.WFieldRecordInfo;
import org.compiere.model.GridField;
import org.compiere.model.Lookup;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.model.X_AD_CtxHelp;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

/**
 * Search Editor for web UI.
 * Web UI port of search type VLookup
 *
 * @author Ashley G Ramdass
 *
 */
public class WSearchEditor extends WEditor implements ContextMenuListener, ValueChangeListener, IZoomableEditor
{
	private static final String[] LISTENER_EVENTS = {Events.ON_CLICK, Events.ON_CHANGE, Events.ON_OK};
	private Lookup 				lookup;
	private String				m_tableName = null;
	private String				m_keyColumnName = null;
	private String 				columnName;
    private Object              value;
    private InfoPanel			infoPanel = null;
	private String imageUrl;

	private static CLogger log = CLogger.getCLogger(WSearchEditor.class);

	private static final String IN_PROGRESS_IMAGE = "~./zk/img/progress3.gif";
	
	private ADWindow adwindow;

	public WSearchEditor (GridField gridField)
	{
		super(new CustomSearchBox(), gridField);

		lookup = gridField.getLookup();

		if (lookup != null)
			columnName = lookup.getColumnName();

		init();
	}


    @Override
	public Searchbox getComponent() {
		return (Searchbox) super.getComponent();
	}

	@Override
	public boolean isReadWrite() {
		return getComponent().isEnabled();
	}


	@Override
	public void setReadWrite(boolean readWrite) {
		getComponent().setEnabled(readWrite);
	}


	/**
	 * Constructor for use if a grid field is unavailable
	 *
	 * @param lookup		Store of selectable data
	 * @param label			column name (not displayed)
	 * @param description	description of component
	 * @param mandatory		whether a selection must be made
	 * @param readonly		whether or not the editor is read only
	 * @param updateable	whether the editor contents can be changed
	 */
	public WSearchEditor (Lookup lookup, String label, String description, boolean mandatory, boolean readonly, boolean updateable)
	{
		super(new Searchbox(), label, description, mandatory, readonly, updateable);

		if (lookup == null)
		{
			throw new IllegalArgumentException("Lookup cannot be null");
		}

		this.lookup = lookup;
        columnName = lookup.getColumnName();
		super.setColumnName(columnName);
		init();
	}

	public WSearchEditor(String columnName, boolean mandatory, boolean readonly, boolean updateable,
    		Lookup lookup)
	{
		super(new Searchbox(), null, null, mandatory, readonly, updateable);

		if (lookup == null)
		{
			throw new IllegalArgumentException("Lookup cannot be null");
		}

		this.lookup = lookup;
        this.columnName = columnName;
		super.setColumnName(columnName);
		init();
	}


	/**
     * initialise editor
     * @param columnName columnName
	 */
	private void init()
	{

		columnName = this.getColumnName();
		if (columnName.equals("C_BPartner_ID"))
		{
			popupMenu = new WEditorPopupMenu(true, true, isShowPreference(), true, true, false, lookup);
			imageUrl = ThemeManager.getThemeResource("images/BPartner16.png");
		}
		else if (columnName.equals("M_Product_ID"))
		{
			popupMenu = new WEditorPopupMenu(true, true, isShowPreference(), false, false, false, lookup);
			imageUrl = ThemeManager.getThemeResource("images/Product16.png");
		}
		else
		{
			popupMenu = new WEditorPopupMenu(true, true, isShowPreference(), false, false, false, lookup);
			imageUrl = ThemeManager.getThemeResource("images/PickOpen16.png");
		}
		getComponent().getButton().setImage(imageUrl);

		addChangeLogMenu(popupMenu);

		return;
	}

	@Override
	public void setValue(Object value)
	{
        this.value = value;
		if (value != null && !"".equals(String.valueOf(value)))
		{
		    String text = lookup.getDisplay(value);

            if (text.startsWith("_"))
            {
                text = text.substring(1);
            }

            getComponent().setText(text);
		}
		else
		{
			getComponent().setText("");
		}
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public String getDisplay()
	{
		return getComponent().getText();
	}

	public void onEvent(Event e)
	{
		if (Events.ON_CHANGE.equals(e.getName()))
		{
			if (infoPanel != null)
		 	{
				infoPanel.detach();
		 	 	infoPanel = null;
		 	}
			// hsv: fix to when switch has text => emtpy text, don't show info panel
			if ("".equals(getComponent().getText().trim())){
				actionCombo(null);
				resetButtonState();
				return;
			}
			actionText(getComponent().getText());
		}
		else if ((Events.ON_OK.equals(e.getName()))) {
			if (getComponent().getText() == null || getComponent().getText().length() == 0) {
				// open Info window similar to swing client
				if (infoPanel != null)
			 	{
					infoPanel.detach();
			 	 	infoPanel = null;
			 	}
				actionText(getComponent().getText());
			} else {
				actionRefresh(getValue());
			}
		}
		else if (Events.ON_CLICK.equals(e.getName()))
		{
			if (infoPanel != null)
			{
				infoPanel.detach();
				infoPanel = null;
			}
			actionButton("");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if ("FieldValue".equals(evt.getPropertyName()))
		{
			actionRefresh(evt.getNewValue());
		}
	}

	private void actionRefresh(Object value)
	{
//		boolean mandatory = isMandatory();
//		AEnv.actionRefresh(lookup, value, mandatory);
    	setValue(value);
	}

	public void actionZoom()
	{
   		AEnv.actionZoom(lookup, getValue());
	}

	private void actionZoom(Object value)
    {
        AEnv.actionZoom(lookup, value);
    }

	public void onMenu(ContextMenuEvent evt)
	{
		if (WEditorPopupMenu.REQUERY_EVENT.equals(evt.getContextEvent()))
		{
			actionRefresh(getValue());
		}
		else if (WEditorPopupMenu.ZOOM_EVENT.equals(evt.getContextEvent()))
		{
			actionZoom();
		}
		else if (WEditorPopupMenu.PREFERENCE_EVENT.equals(evt.getContextEvent()))
		{
			if (isShowPreference())
				ValuePreference.start (getComponent(), this.getGridField(), getValue());
			return;
		}
		else if (WEditorPopupMenu.NEW_EVENT.equals(evt.getContextEvent()))
		{
			if (infoPanel != null)
			{
				infoPanel.detach();
				infoPanel = null;
			}
			actionQuickEntry(true);
		}
		// Elaine 2009/02/16 - update record
		else if (WEditorPopupMenu.UPDATE_EVENT.equals(evt.getContextEvent()))
		{
			if (infoPanel != null)
			{
				infoPanel.detach();
				infoPanel = null;
			}
			actionQuickEntry(false);
		}
		else if (WEditorPopupMenu.CHANGE_LOG_EVENT.equals(evt.getContextEvent()))
		{
			WFieldRecordInfo.start(gridField);
		}
		//
	}

	private void actionText(String text)
	{
		//	Nothing entered
		if (text == null || text.length() == 0 || text.equals("%"))
		{
			actionButton(text);
			resetButtonState();
			return;
		}
		if (log.isLoggable(Level.CONFIG)) log.config(getColumnName() + " - " + text);

		int id = -1;
		
		if (m_tableName == null)	//	sets table name & key column
			getDirectAccessSQL("*");
		
		final InfoPanel ip = InfoManager.create(lookup, gridField, m_tableName, m_keyColumnName, getComponent().getText(), false, getWhereClause());
		if (ip != null && ip.loadedOK() && ip.getRowCount() == 1)
		{
			Integer key = ip.getFirstRowKey();
			if (key != null && key.intValue() > 0)
			{
				id = key.intValue();
			}
		}
		
		//	No (unique) result
		if (id <= 0)
		{
			//m_value = null;	// force re-display
			if (ip != null && ip.loadedOK()) 
			{
				showInfoPanel(ip);
			}
			else
			{
				actionButton(getComponent().getText());
			}
			resetButtonState();
			return;
		}
		if (log.isLoggable(Level.FINE))
			log.fine(getColumnName() + " - Unique ID=" + id);

		actionCombo(new Integer(id));          //  data binding
		
		Searchbox comp = getComponent();
		Component parent = comp.getParent();
		while (parent != null) {
			if (parent instanceof IFieldEditorContainer) {
				((IFieldEditorContainer) parent).focusToNextEditor(this);
				break;
			}
			parent = parent.getParent();
		}
		
		//safety check: if focus is going no where, focus back to self
		String uid = getComponent().getTextbox().getUuid();
		String script = "setTimeout(function(){try{var e = zk.Widget.$('#" + uid +
				"').$n(); if (jq(':focus').size() == 0) e.focus();} catch(error){}}, 100);";
		Clients.response(new AuScript(script));
		
		resetButtonState();
	}	//	actionText


	private void resetButtonState() {
		getComponent().getButton().setEnabled(true);
		getComponent().getButton().setImage(imageUrl);
		getComponent().invalidate();
	}


	private void actionCombo (Object value)
	{
		if (log.isLoggable(Level.FINE))
			log.fine("Value=" + value);
	
		try 
		{
			if (gridField != null)
				gridField.setLookupEditorSettingValue(true);
			
			ValueChangeEvent evt = new ValueChangeEvent(this, this.getColumnName(), getValue(), value);
			// -> ADTabpanel - valuechange
			fireValueChange(evt);
	
			//  is the value updated ?
			boolean updated = false;
			if (value instanceof Object[] && ((Object[])value).length > 0)
			{
				value = ((Object[])value)[0];
			}
	
			if (value == null && getValue() == null) {
				updated = true;
			} else if (value != null && value.equals(getValue())) {
				updated = true;
				getComponent().setText(lookup.getDisplay(value));
			}
			if (!updated)
			{
				setValue(value);
			}				
		} 
		finally 
		{
			if (gridField != null)
				gridField.setLookupEditorSettingValue(false);
		}
		
	}	//	actionCombo

	/**
	 *	Action - Special Quick Entry Screen
	 *  @param newRecord true if new record should be created
	 */
	private void actionQuickEntry (boolean newRecord)
	{
		if(!getComponent().isEnabled())
			return;

		int zoomWindowId = gridField != null ? lookup.getZoom(Env.isSOTrx(Env.getCtx(), gridField.getWindowNo())) : lookup.getZoom(Env.isSOTrx(Env.getCtx()));
		final WQuickEntry vqe = new WQuickEntry (lookup.getWindowNo(), zoomWindowId);
		if (vqe.getQuickFields()<=0)
			return;
		int Record_ID = 0;

		//  if update, get current value
		if (!newRecord)
		{
			if (value instanceof Integer)
				Record_ID = ((Integer)value).intValue();
			else if (value != null && "".compareTo(value.toString())!= 0)
				Record_ID = Integer.parseInt(value.toString());
		}

		vqe.loadRecord (Record_ID);

		final int finalRecord_ID = Record_ID;
		vqe.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (adwindow != null)
				{
					adwindow.getADWindowContent().hideBusyMask();
					adwindow = null;
				}
				// get result
				int result = vqe.getRecord_ID();

				if (result == 0					//	0 = not saved
					&& result == finalRecord_ID)	//	the same
					return;

				//  Maybe new Record - put in cache
				lookup.getDirect(new Integer(result), false, true);
				setValue(new Integer(result));
				actionCombo (new Integer(result));      //  data binding
				lookup.refresh();

				//setValue(getValue());				
			}
		});

		vqe.setSizable(true);
		adwindow = ADWindow.findADWindow(getComponent());
		if (adwindow != null) {
			ADWindowContent content = adwindow.getADWindowContent();				
			content.getComponent().getParent().appendChild(vqe);
			content.showBusyMask(vqe);
			LayoutUtils.openOverlappedWindow(content.getComponent().getParent(), vqe, "middle_center");
		} else {
			AEnv.showWindow(vqe);
		}
	}	//	actionQuickEntry

	private void actionButton(String queryValue)
	{
		if (lookup == null)
			return;		//	leave button disabled

		/**
		 *  Three return options:
		 *  - Value Selected & OK pressed   => store result => result has value
		 *  - Cancel pressed                => store null   => result == null && cancelled
		 *  - Window closed                 -> ignore       => result == null && !cancalled
		 */

		//  Zoom / Validation
		String whereClause = getWhereClause();

		if (log.isLoggable(Level.FINE))
			log.fine(lookup.getColumnName() + ", Zoom=" + lookup.getZoom() + " (" + whereClause + ")");

		// boolean resetValue = false;	// Reset value so that is always treated as new entry

		//  Replace Value with name if no value exists
		if (queryValue.length() == 0 && getComponent().getText().length() > 0)
			queryValue = getComponent().getText();

		if (m_tableName == null)	//	sets table name & key column
			getDirectAccessSQL("*");

		final InfoPanel ip = InfoManager.create(lookup, gridField, m_tableName, m_keyColumnName, queryValue, false, whereClause);
		if (ip != null)
			showInfoPanel(ip);
	}


	protected void showInfoPanel(final InfoPanel ip) {
		ip.setVisible(true);
		ip.setStyle("border: 2px");
		ip.setClosable(true);
		ip.addValueChangeListener(this);
		infoPanel = ip;
		ip.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Component component = SessionManager.getAppDesktop().getActiveWindow();
				if (component instanceof IHelpContext)
					Events.sendEvent(new Event(WindowContainer.ON_WINDOW_CONTAINER_SELECTION_CHANGED_EVENT, component));
				else
					SessionManager.getAppDesktop().updateHelpContext(X_AD_CtxHelp.CTXTYPE_Home, 0);
				
				boolean cancelled = ip.isCancelled();
				Object[] result = ip.getSelectedKeys();

				infoPanel = null;
				//  Result
				if (result != null && result.length > 0)
				{
					//ensure data binding happen
					if (result.length > 1)
						actionCombo (result);
					else
						actionCombo (result[0]);
				}
				else if (cancelled)
				{
					if (log.isLoggable(Level.CONFIG)) log.config(getColumnName() + " - Result = null (cancelled)");
					if (value != null) 
					{
						if (!lookup.getDisplay(value).equals(getComponent().getText())){
							getComponent().setText(lookup.getDisplay(value));
							
						}
					}else{
						getComponent().setText("");
						actionCombo(null);
					}						
				}
				else
				{
					if (log.isLoggable(Level.CONFIG)) log.config(getColumnName() + " - Result = null (not cancelled)");
				}
				getComponent().getTextbox().focus();
			}
		});
		ip.setId(ip.getTitle()+"_"+ip.getWindowNo());
		AEnv.showWindow(ip);
	}

	/**
	 * 	Generate Access SQL for Search.
	 * 	The SQL returns the ID of the value entered
	 * 	Also sets m_tableName and m_keyColumnName
	 *	@param text uppercase text for LIKE comparison
	 *	@return sql or ""
	 *  Example
	 *	SELECT C_Payment_ID FROM C_Payment WHERE UPPER(DocumentNo) LIKE x OR ...
	 */
	private String getDirectAccessSQL (String text)
	{
		String m_columnName = getColumnName();

		StringBuffer sql = new StringBuffer();
		m_tableName = m_columnName.substring(0, m_columnName.length()-3);
		m_keyColumnName = m_columnName;

		if (m_columnName.equals("M_Product_ID"))
		{
			//	Reset
			Env.setContext(Env.getCtx(), lookup.getWindowNo(), Env.TAB_INFO, "M_Product_ID", "0");
			Env.setContext(Env.getCtx(), lookup.getWindowNo(), Env.TAB_INFO, "M_AttributeSetInstance_ID", "0");
			Env.setContext(Env.getCtx(), lookup.getWindowNo(), Env.TAB_INFO, "M_Locator_ID", "0");

			sql.append("SELECT M_Product_ID FROM M_Product WHERE (UPPER(Value) LIKE ")
				.append(DB.TO_STRING(text))
				.append(" OR UPPER(Name) LIKE ").append(DB.TO_STRING(text))
				.append(" OR UPC LIKE ").append(DB.TO_STRING(text)).append(")");
		}
		else if (m_columnName.equals("C_BPartner_ID"))
		{
			sql.append("SELECT C_BPartner_ID FROM C_BPartner WHERE (UPPER(Value) LIKE ")
				.append(DB.TO_STRING(text))
				.append(" OR UPPER(Name) LIKE ").append(DB.TO_STRING(text)).append(")");
		}
		else if (m_columnName.equals("C_Order_ID"))
		{
			sql.append("SELECT C_Order_ID FROM C_Order WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("C_Invoice_ID"))
		{
			sql.append("SELECT C_Invoice_ID FROM C_Invoice WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("M_InOut_ID"))
		{
			sql.append("SELECT M_InOut_ID FROM M_InOut WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("C_Payment_ID"))
		{
			sql.append("SELECT C_Payment_ID FROM C_Payment WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("GL_JournalBatch_ID"))
		{
			sql.append("SELECT GL_JournalBatch_ID FROM GL_JournalBatch WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("SalesRep_ID"))
		{
			sql.append("SELECT AD_User_ID FROM AD_User WHERE UPPER(Name) LIKE ")
				.append(DB.TO_STRING(text));

			m_tableName = "AD_User";
			m_keyColumnName = "AD_User_ID";
		}

		//	Predefined

		if (sql.length() > 0)
		{
			String wc = getWhereClause();

			if (wc != null && wc.length() > 0)
				sql.append(" AND ").append(wc);

			sql.append(" AND IsActive='Y'");
			//	***

			if (log.isLoggable(Level.FINEST)) log.finest(m_columnName + " (predefined) " + sql.toString());

			return MRole.getDefault().addAccessSQL(sql.toString(),
				m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		}

		//	Check if it is a Table Reference

		if (lookup != null && lookup instanceof MLookup)
		{
			int AD_Reference_ID = ((MLookup)lookup).getAD_Reference_Value_ID();

			if (AD_Reference_ID != 0)
			{
				boolean isValueDisplayed = false;
				String query = "SELECT kc.ColumnName, dc.ColumnName, t.TableName, rt.IsValueDisplayed "
					+ "FROM AD_Ref_Table rt"
					+ " INNER JOIN AD_Column kc ON (rt.AD_Key=kc.AD_Column_ID)"
					+ " INNER JOIN AD_Column dc ON (rt.AD_Display=dc.AD_Column_ID)"
					+ " INNER JOIN AD_Table t ON (rt.AD_Table_ID=t.AD_Table_ID) "
					+ "WHERE rt.AD_Reference_ID=?";

				String displayColumnName = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				try
				{
					pstmt = DB.prepareStatement(query, null);
					pstmt.setInt(1, AD_Reference_ID);
					rs = pstmt.executeQuery();

					if (rs.next())
					{
						m_keyColumnName = rs.getString(1);
						displayColumnName = rs.getString(2);
						m_tableName = rs.getString(3);
						String t = rs.getString(4);
						isValueDisplayed = "Y".equalsIgnoreCase(t);
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, query, e);
				}
				finally
				{
					DB.close(rs, pstmt);
				}


				if (displayColumnName != null)
				{
					sql = new StringBuffer();
					sql.append("SELECT ").append(m_keyColumnName)
						.append(" FROM ").append(m_tableName)
						.append(" WHERE (UPPER(").append(displayColumnName)
						.append(") LIKE ").append(DB.TO_STRING(text));
					if (isValueDisplayed)
					{
						sql.append(" OR UPPER(").append("Value")
						   .append(") LIKE ").append(DB.TO_STRING(text));
					}
					sql.append(")");
					sql.append(" AND IsActive='Y'");

					String wc = getWhereClause();

					if (wc != null && wc.length() > 0)
						sql.append(" AND ").append(wc);

					//	***

					if (log.isLoggable(Level.FINEST)) log.finest(m_columnName + " (Table) " + sql.toString());

					return MRole.getDefault().addAccessSQL(sql.toString(),
								m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
				}
			} // Table Reference
		} // MLookup

		/** Check Well Known Columns of Table - assumes TableDir	**/

		String query = "SELECT t.TableName, c.ColumnName "
			+ "FROM AD_Column c "
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID AND t.IsView='N') "
			+ "WHERE (c.ColumnName IN ('DocumentNo', 'Value', 'Name') OR c.IsIdentifier='Y')"
			+ " AND c.AD_Reference_ID IN (10,14)"
			+ " AND EXISTS (SELECT * FROM AD_Column cc WHERE cc.AD_Table_ID=t.AD_Table_ID"
				+ " AND cc.IsKey='Y' AND cc.ColumnName=?)";

		m_keyColumnName = m_columnName;
		sql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(query, null);
			pstmt.setString(1, m_keyColumnName);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				if (sql.length() != 0)
					sql.append(" OR ");

				m_tableName = rs.getString(1);
				sql.append("UPPER(").append(rs.getString(2)).append(") LIKE ").append(DB.TO_STRING(text));
			}
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, query, ex);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (sql.length() == 0)
		{
			log.log(Level.SEVERE, m_columnName + " (TableDir) - no standard/identifier columns");
			return "";
		}
		//
		StringBuffer retValue = new StringBuffer ("SELECT ")
			.append(m_columnName).append(" FROM ").append(m_tableName)
			.append(" WHERE ").append(sql)
			.append(" AND IsActive='Y'");

		String wc = getWhereClause();

		if (wc != null && wc.length() > 0)
			retValue.append(" AND ").append(wc);
		//	***
		if (log.isLoggable(Level.FINEST)) log.finest(m_columnName + " (TableDir) " + sql.toString());
		return MRole.getDefault().addAccessSQL(retValue.toString(),
					m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
	}

	private String getWhereClause()
	{
		String whereClause = "";

		if (lookup == null)
			return "";

		if (lookup.getZoomQuery() != null)
			whereClause = lookup.getZoomQuery().getWhereClause();

		String validation = lookup.getValidation();

		if (validation == null)
			validation = "";

		if (whereClause.length() == 0)
			whereClause = validation;
		else if (validation.length() > 0)
			whereClause += " AND " + validation;

		//	log.finest("ZoomQuery=" + (lookup.getZoomQuery()==null ? "" : lookup.getZoomQuery().getWhereClause())
	//		+ ", Validation=" + lookup.getValidation());

		if (whereClause.indexOf('@') != -1)
		{
			String validated = Env.parseContext(Env.getCtx(), lookup.getWindowNo(), whereClause, false);

			if (validated.length() == 0)
				log.severe(getColumnName() + " - Cannot Parse=" + whereClause);
			else
			{
				if (log.isLoggable(Level.FINE))
					log.fine(getColumnName() + " - Parsed: " + validated);
				return validated;
			}
		}
		return whereClause;
	}	//	getWhereClause


	public String[] getEvents()
    {
        return LISTENER_EVENTS;
    }

	public void valueChange(ValueChangeEvent evt)
	{
        if ("zoom".equals(evt.getPropertyName()))
        {
            actionZoom(evt.getNewValue());
        }
        else
        {
        	if (evt.getNewValue() != null)
			{
				actionCombo(evt.getNewValue());
			}
        }

	}

	@Override
	public void setTableEditor(boolean b) {
		super.setTableEditor(b);
		getComponent().setTableEditorMode(b);
	}


	public boolean isShowingDialog (){
		return infoPanel != null;
	}
	
	/**
	 * @param windowNo
	 * @return WSearchEditor
	 */
	public static WSearchEditor createBPartner(int windowNo) {
		int AD_Column_ID = COLUMN_C_INVOICE_C_BPARTNER_ID;    //  C_Invoice.C_BPartner_ID
		try
		{
			Lookup lookup = MLookupFactory.get (Env.getCtx(), windowNo,
				0, AD_Column_ID, DisplayType.Search);
			return new WSearchEditor ("C_BPartner_ID", false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return null;
	}

	/**
	 * @param windowNo
	 * @return WSearchEditor
	 */
	public static WSearchEditor createProduct(int windowNo) {
		int AD_Column_ID = COLUMN_C_INVOICELINE_M_PRODUCT_ID;    //  C_InvoiceLine.M_Product_ID
		try
		{
			Lookup lookup = MLookupFactory.get (Env.getCtx(), windowNo, 0,
				AD_Column_ID, DisplayType.Search);
			return new WSearchEditor("M_Product_ID", false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return null;
	}
	
	static class CustomSearchBox extends Searchbox {

		/**
		 * generated serial id
		 */
		private static final long serialVersionUID = 7490301044763375829L;

		@Override
		public void onPageAttached(Page newpage, Page oldpage) {
			super.onPageAttached(newpage, oldpage);
			if (newpage != null) {
				String w = "try{var btn=jq('#'+this.parent.uuid+' @button').zk.$();}catch(err){}";
				getTextbox().setWidgetListener("onChange", "try{"+w+"btn.setImage(\""
						+ Executions.getCurrent().encodeURL(IN_PROGRESS_IMAGE)+"\");"
						+ "btn.setDisabled(true, {adbs: false, skip: false});}catch(err){}");
			}
		}
		
	}
}
