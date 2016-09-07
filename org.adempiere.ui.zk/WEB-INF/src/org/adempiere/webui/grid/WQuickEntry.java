/******************************************************************************
 * Copyright (C) 2012 Trek Global                                             *
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package org.adempiere.webui.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.base.Core;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WLocationEditor;
import org.adempiere.webui.editor.WebEditorFactory;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.model.MField;
import org.compiere.model.MLookup;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Span;
import org.zkoss.zul.Vlayout;

/**
 * Quick Entry Window
 * Author: Carlos Ruiz
 */

public class WQuickEntry extends Window implements EventListener<Event>, ValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8530102231615195037L;

	public static final String QUICK_ENTRY_MODE = "_QUICK_ENTRY_MODE_";

	private static CLogger log = CLogger.getCLogger(WQuickEntry.class);

	protected int m_WindowNo;
	private int parent_WindowNo;

	List<GridField> quickFields = new ArrayList<GridField>();
	protected List<WEditor> quickEditors = new ArrayList<WEditor>();
	List<Object> initialValues = new ArrayList<Object>();
	List<GridTab> quickTabs = new ArrayList<GridTab>();
	List<PO> quickPOs = new ArrayList<PO>();

	/** Read Only				*/
	private boolean m_readOnly = false;

	protected Vlayout centerPanel = new Vlayout();

	private ConfirmPanel confirmPanel = new ConfirmPanel(true, false, false, false, false, false);

	protected int m_AD_Window_ID;
	
	private boolean isHasField = false;
	/**
	 *	Constructor.
	 *	Requires call loadRecord
	 * 	@param WindowNo	Window No
	 * 	@param AD_Window_ID
	 */

	public WQuickEntry(int WindowNo, int AD_Window_ID)
	{
		super();

		m_AD_Window_ID = AD_Window_ID;
		parent_WindowNo = WindowNo;
		m_WindowNo = SessionManager.getAppDesktop().registerWindow(this);
		log.info("R/O=" + m_readOnly);

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, ex.getMessage());
		}

		Env.setContext(Env.getCtx(), m_WindowNo, QUICK_ENTRY_MODE, "Y");
		initPOs();

	}	//	WQuickEntry
	
	public WQuickEntry(int AD_Window_ID)
	{
		super();
		
		m_AD_Window_ID = AD_Window_ID;
		parent_WindowNo = 0;
		m_WindowNo = 0;
		log.info("R/O=" + m_readOnly);
	}	//	WQuickEntry

	/**
	 *	Static Init
	 * 	@throws Exception
	 */

	private void jbInit() throws Exception
	{
		ZKUpdateUtil.setWidth(this, "350px");
		this.setBorder("normal");
		this.setClosable(true);
		this.setSizable(true);
		this.appendChild(centerPanel);
		this.appendChild(confirmPanel);
		ZKUpdateUtil.setWidth(centerPanel, "100%");

		confirmPanel.addActionListener(Events.ON_CLICK, this);
	}

	/**
	 *	Dynamic Init
	 */
	protected void initPOs()
	{
		GridWindow gridwindow = GridWindow.get(Env.getCtx(), m_WindowNo, m_AD_Window_ID);
		this.setTitle(gridwindow.getName());
		boolean newTab = false;
		for (int i=0; i < gridwindow.getTabCount(); i++) {
			GridTab gridtab = gridwindow.getTab(i);
			if (i == 0) {
				m_readOnly = !MRole.getDefault().canUpdate(
						Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Org_ID(Env.getCtx()),
						gridtab.getAD_Table_ID(), 0, false);
			}
			if (!gridtab.isLoadComplete())
				gridwindow.initTab(i);
			for (GridField gridfield : gridtab.getFields()) {
				MField field = new MField(Env.getCtx(), gridfield.getAD_Field_ID(), null);
				if (field.isQuickEntry()) {
					if (! isValidQuickEntryType(field.getAD_Reference_ID()))
						continue;
			        WEditor editor = WebEditorFactory.getEditor(gridfield, false);
			        if (m_readOnly)
			        	editor.setReadWrite(false);
			        if (gridfield.isMandatory(false))
			        	editor.setMandatory(true);
					createLine(editor, newTab, gridtab);
					quickFields.add(gridfield);
					quickEditors.add(editor);
					editor.addValueChangeListener(this);
					
					if (! quickTabs.contains(gridtab)) {
						quickTabs.add(gridtab);
					}
					isHasField = true;
					newTab = false;
				}
			}
			newTab = true;
		}
	}	//	initPOs

	private boolean isValidQuickEntryType(int refID) {
		boolean valid =
			! (
				 refID == DisplayType.Button
			  || refID == DisplayType.Binary
			  || refID == DisplayType.ID
			  );
		return valid;
	}

	private void createLine(WEditor editor, boolean newTab, GridTab gt) {
		if (newTab) {
			Separator sep = new Separator();
			centerPanel.appendChild(sep);

			Label tabLabel = new Label(gt.getName());
			centerPanel.appendChild(tabLabel);

			Separator sepl = new Separator();
			sepl.setBar(true);
			centerPanel.appendChild(sepl);
		}
		Component field = editor.getComponent();
		Hlayout layout = new Hlayout();

		ZKUpdateUtil.setHflex(layout, "10");

		Span span = new Span();
		if(parent_WindowNo!= 0)
			ZKUpdateUtil.setHflex(span, "3");
		layout.appendChild(span);
		Label label = editor.getLabel();
		span.appendChild(label);
		label.setSclass("field-label");

		layout.appendChild(field);
		ZKUpdateUtil.setHflex((HtmlBasedComponent)field, "7");
		
		//editor.setValue("Y");
		centerPanel.appendChild(layout);
	}

	/**
	 * check table is editable in quick entry
	 * user must has write right and has at least a input field
	 * @return
	 */
	public boolean isAvailableQuickEdit (){
		return isHasField && !m_readOnly;
	}
	
	/**
	 *	Load Record_ID
	 *  @param Record_ID - existing Record or 0 for new
	 * 	@return true if loaded
	 */

	public boolean loadRecord (int Record_ID)
	{
		String parentColumn = null;
		for (int idxt = 0; idxt < quickTabs.size(); idxt++) {
			GridTab gridtab = quickTabs.get(idxt);
			int id = 0;
			if (idxt == 0) {
				id = Record_ID;
				parentColumn = gridtab.getTableName() + "_ID";
			} else {
				if (Record_ID > 0) {
					String columnname = gridtab.getTableName() + "_ID";
					id = Env.getContextAsInt(Env.getCtx(), parent_WindowNo, columnname);
				}
			}
			MQuery query = new MQuery(gridtab.getAD_Table_ID());
			if (id == 0) {	//new record			
				query.addRestriction("1=2");
				gridtab.setQuery(query);
				gridtab.query(false);
				if (gridtab.isInsertRecord())
					gridtab.dataNew(false);
			}
			else{ //update record
				if (gridtab.getTabNo() == 0)
					query.addRestriction(gridtab.getKeyColumnName(),"=",id);
				gridtab.setQuery(query);
				gridtab.query(false);
			}
			quickTabs.set(idxt, gridtab);
			
			MTable table = MTable.get(Env.getCtx(), gridtab.getTableName());
			PO po = table.getPO(id, null);
			if (idxt > 0) {
				// check the detail record is a child of parent
				int parentid = po.get_ValueAsInt(parentColumn);
				if (parentid != Record_ID) {
					po = table.getPO(0, null);
				}
			}
			quickPOs.add(po);
		}

		if (log.isLoggable(Level.CONFIG)) log.config("Record_ID=" + Record_ID);

		//  New record
		if (Record_ID == 0)
		{
			// set defaults on editors
			for (int idxf = 0; idxf < quickFields.size(); idxf++) {
				GridField field = quickFields.get(idxf);
				WEditor editor = quickEditors.get(idxf);

				Object value = field.getDefault();
				if (value != null) {
					editor.setValue(value);
					field.setValue(value, true);
					field.getGridTab().processCallout(field);
				}
				initialValues.add(editor.getValue());
			}
			dynamicDisplay();
			return true;
		}

		if (quickPOs.get(0).get_ID() == 0)
		{
			FDialog.error(m_WindowNo, this, "RecordNotFound");
			return false;
		}
		
		for (int idxf = 0; idxf < quickFields.size(); idxf++) {
			GridField field = quickFields.get(idxf);
			WEditor editor = quickEditors.get(idxf);
			
			int idxt = quickTabs.indexOf(field.getGridTab());
			PO po = quickPOs.get(idxt);
			Object value = po.get_Value(field.getColumnName());
			if (value != null) {
				editor.setValue(value);
				field.setValue(value, false);
			} else {
				editor.dynamicDisplay();
			}
			initialValues.add(editor.getValue());
		}

		dynamicDisplay();
		return true;
	}	//	loadRecord

	/**
	 *	Save.
	 * 	@return true if saved
	 */
	protected boolean actionSave()
	{
		log.config("");
		boolean anyChange = false;
		for (int idxf = 0; idxf < quickEditors.size(); idxf++) {
			WEditor editor = quickEditors.get(idxf);
			Object value = editor.getValue();
			Object initialValue = initialValues.get(idxf);

			boolean changed = (value != null && initialValue == null)
					|| (value == null && initialValue != null)
					|| (value != null && initialValue != null && ! value.equals(initialValue));

			if (changed) {
				anyChange = true;
			}
		}
		if (anyChange) {
			// Call all callouts to allow quick entry special behavior
			for (GridField field : quickFields) {
				String msg = field.getGridTab().processCallout(field);
				if (! Util.isEmpty(msg)) {
					FDialog.error(m_WindowNo, this, "", msg);
					return false;
				}
			}
		}

		int parentID = 0;
		String parentColumn = null;
		String tabZeroName=null;
		boolean isParentSave = (getRecord_ID() > 0);
		for (int idxt = 0; idxt < quickTabs.size(); idxt++) {
			GridTab gridtab = quickTabs.get(idxt);
			PO po = quickPOs.get(idxt);
			if (idxt == 0) {
				parentColumn = gridtab.getTableName() + "_ID";
				tabZeroName=gridtab.getName();
			}

			boolean savePO = false;
			boolean fillMandatoryError = false;
			StringBuilder mandatoryFields = new StringBuilder();
			for (int idxf = 0; idxf < quickFields.size(); idxf++) {
				GridField field = quickFields.get(idxf);
				if (field.getGridTab() != gridtab)
					continue;

				WEditor editor = quickEditors.get(idxf);
				Object value = editor.getValue();
				Object initialValue = initialValues.get(idxf);

				boolean changed = (value != null && initialValue == null)
						|| (value == null && initialValue != null)
						|| (value != null && initialValue != null && !value.equals(initialValue));
				boolean thisMandatoryError = false;
				if (field.isMandatory(true)) {
					if (value == null || value.toString().length() == 0) {
						fillMandatoryError = true;
						thisMandatoryError = true;
						if (mandatoryFields.length() > 0)
							mandatoryFields.append(", ");
						mandatoryFields.append(Msg.getElement(Env.getCtx(), field.getColumnName()));
					}
				}

				po.set_ValueOfColumn(field.getColumnName(), value);
				if (changed && ! thisMandatoryError) {
					savePO = true;
				}
			}
			if (savePO && fillMandatoryError) {
				FDialog.error(m_WindowNo, this, "FillMandatory", mandatoryFields.toString());
				return false;
			}
			if (savePO) {
				if (idxt > 0) {
					int actualID = po.get_ValueAsInt(parentColumn);
					if (actualID != parentID) {
						po.set_ValueOfColumn(parentColumn, parentID);
					}
				}
				if(gridtab.getTabLevel()>0 && !isParentSave){
					FDialog.error(m_WindowNo, this, "FillMinimumInfo",tabZeroName);
					return false;
				}
				po.saveEx();
				if(gridtab.getTabLevel()==0){
					isParentSave=true;
				}
				for (int idxf = 0; idxf < quickFields.size(); idxf++) {
					GridField field = quickFields.get(idxf);
					if (field.getGridTab() != gridtab)
						continue;

					WEditor we = quickEditors.get(idxf);
					if (po.get_Value(we.getColumnName()) != null) {
						we.setValue(po.get_Value(we.getColumnName()));
					}
				}
			}
			if (idxt == 0) {
				parentID = po.get_ID();
			}
		}
		return true;
	}	//	actionSave

	/**
	 *	Returns Record_ID
	 *	@return Record_ID (0 = not saved)
	 */

	public int getRecord_ID()
	{
		if (quickPOs.isEmpty() || quickPOs.get(0) == null)
			return 0;

		return quickPOs.get(0).get_ID();
	}	//	getRecord_ID

	public void onEvent(Event e) throws Exception
	{
		if (m_readOnly)
			this.detach();

		//	OK pressed
		else if ((e.getTarget() == confirmPanel.getButton("Ok")) && actionSave())
			this.detach();

		//	Cancel pressed
		else if (e.getTarget() == confirmPanel.getButton("Cancel"))
			this.detach();

	}

	@Override
	public void detach() {
		super.detach();
		if(m_WindowNo!=0)
			SessionManager.getAppDesktop().unregisterWindow(m_WindowNo);
	}
	
	public void valueChange(ValueChangeEvent evt)
	{
		if (evt.getSource() instanceof WEditor) {
			int idx = quickEditors.indexOf(evt.getSource());
			if (idx >= 0) {
				GridField field = quickFields.get(idx);
				WEditor editor = quickEditors.get(idx);
				GridTab gridTab = field.getGridTab();
				String columnName = field.getColumnName();
				// process dependencies and callouts for the changed field
				if (evt.getSource() instanceof WLocationEditor && evt.getNewValue() == null && editor.getValue() != null) {
					// ignore first call of WLocationEditor valuechange set to null
					// it will be called later with correct value
					// see WLocationEditor firing twice ValueChangeEvent (first with null and then with value)
				} else {
					field.setValue(evt.getNewValue(), field.getGridTab().getTableModel().isInserting());
					gridTab.processFieldChange(field);
				}
	            // Refresh the list on dependant fields
	            ArrayList<GridField> dependants = gridTab.getDependantFields(columnName);
	    		for (GridField dependentField : dependants)
	    		{
	    			//  if the field has a lookup
	    			if (dependentField != null && dependentField.getLookup() instanceof MLookup)
	    			{
	    				MLookup mLookup = (MLookup)dependentField.getLookup();
	    				//  if the lookup is dynamic (i.e. contains this columnName as variable)
	    				if (mLookup.getValidation().indexOf("@"+columnName+"@") != -1)
	    				{
	    					mLookup.refresh();
	    				}
	    			}
	    		}   //  for all dependent fields
				if (   dependants.size() > 0
					|| field.getCallout().length() > 0
					|| Core.findCallout(gridTab.getTableName(), columnName).size() > 0) {
					dynamicDisplay();
	            }
			}
		}
	}

	/**
	 *	refresh all fields
	 */
	public void dynamicDisplay()
	{
		for (int idxf = 0; idxf < quickFields.size(); idxf++) {
			GridField field = quickFields.get(idxf);
			WEditor editor = quickEditors.get(idxf);
			editor.setValue(field.getValue());
			editor.setReadWrite(field.isEditable(true));
			editor.setVisible(field.isDisplayed(true));
		}
	} // dynamicDisplay
	
	/**
	 *	get size quickfields
	 */
	public int getQuickFields(){
		return quickFields.size();
	}// size of quickfields

} // WQuickEntry
