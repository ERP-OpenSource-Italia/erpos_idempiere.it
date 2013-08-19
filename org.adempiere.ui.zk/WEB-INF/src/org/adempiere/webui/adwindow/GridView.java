/******************************************************************************
 * Copyright (C) 2008 Low Heng Sin                                            *
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
package org.adempiere.webui.adwindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.table.AbstractTableModel;

import org.adempiere.base.Core;
import org.adempiere.model.MTabCustomization;
import org.adempiere.util.GridRowCtx;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.EditorBox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.util.SortComparator;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridTable;
import org.compiere.model.MSysConfig;
import org.compiere.model.StateChangeEvent;
import org.compiere.model.StateChangeListener;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.au.out.AuFocus;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Div;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.event.ZulEvents;

/**
 * Grid view implemented using the Grid component.
 * @author Low Heng Sin
 *
 */
public class GridView extends Vbox implements EventListener<Event>, IdSpace, IFieldEditorContainer, StateChangeListener
{
	private static final String HEADER_GRID_STYLE = "border: none; margin:0; padding: 0;";

	private static final int DEFAULT_DETAIL_PAGE_SIZE = 10;

	private static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * generated serial version ID
	 */
	private static final long serialVersionUID = -7151423393713654553L;

	private static final int MIN_COLUMN_WIDTH = 100;

	private static final int MAX_COLUMN_WIDTH = 300;

	private static final int MIN_COMBOBOX_WIDTH = 160;

	private static final int MIN_NUMERIC_COL_WIDTH = 120;

	private static final String ATTR_ON_POST_SELECTED_ROW_CHANGED = "org.adempiere.webui.adwindow.GridView.onPostSelectedRowChanged";

	private Grid listbox = null;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private GridField[] gridField;
	private AbstractTableModel tableModel;

	private int numColumns = 5;

	private int windowNo;

	private GridTab gridTab;

	private boolean init;

	private GridTableListModel listModel;

	private Paging paging;

	private GridTabRowRenderer renderer;

	private Div gridFooter;

	private boolean modeless = true;

	private String columnOnClick;

	private AbstractADWindowContent windowPanel;

	private boolean refreshing;

	private Map<Integer, String> columnWidthMap;

	private boolean detailPaneMode;

	protected Checkbox selectAll;

	public GridView()
	{
		this(0);
	}

	/**
	 * @param windowNo
	 */
	public GridView(int windowNo)
	{
		this.windowNo = windowNo;
		setId("gridView");
		createListbox();

		this.setHflex("1");
		
		gridFooter = new Div();
		gridFooter.setHflex("1");
		gridFooter.setVflex("0");
		
		//default paging size
		if (AEnv.isTablet())
		{
			//anything more than 20 is very slow on a tablet
			pageSize = 10;
		}
		else
		{
			pageSize = MSysConfig.getIntValue(MSysConfig.ZK_PAGING_SIZE, DEFAULT_PAGE_SIZE);
		}
		
		//default true for better UI experience
		modeless = MSysConfig.getBooleanValue(MSysConfig.ZK_GRID_EDIT_MODELESS, true);
		
		appendChild(listbox);
		appendChild(gridFooter);								
		this.setVflex("true");
		
		setStyle(HEADER_GRID_STYLE);
		gridFooter.setStyle(HEADER_GRID_STYLE);
		
		addEventListener("onSelectRow", this);
	}

	protected void createListbox() {
		listbox = new Grid();
		listbox.setEmptyMessage(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "FindZeroRecords")));
		listbox.setSizedByContent(false);				
		listbox.setVflex("1");
		listbox.setHflex("1");
		listbox.setSclass("adtab-grid");
	}
	
	public void setDetailPaneMode(boolean detailPaneMode) {
		if (this.detailPaneMode != detailPaneMode) {
			this.detailPaneMode = detailPaneMode;
			pageSize =  detailPaneMode ? DEFAULT_DETAIL_PAGE_SIZE : MSysConfig.getIntValue(MSysConfig.ZK_PAGING_SIZE, 20);
			updatePaging();
		}
	}

	public boolean isDetailPaneMode() {
		return this.detailPaneMode;
	}
	
	private void updatePaging() {
		if (paging != null && paging.getPageSize() != pageSize) {
			paging.setPageSize(pageSize);
			updateModel();
		}
	}

	/**
	 *
	 * @param gridTab
	 */
	public void init(GridTab gridTab)
	{
		if (init) return;

		if (this.gridTab != null)
			this.gridTab.removeStateChangeListener(this);
		
		setupFields(gridTab);

		setupColumns();
		render();

		updateListIndex();

		this.init = true;
	}

	private void setupFields(GridTab gridTab) {		
		this.gridTab = gridTab;		
		gridTab.addStateChangeListener(this);
		
		tableModel = gridTab.getTableModel();
		columnWidthMap = new HashMap<Integer, String>();
		GridField[] tmpFields = ((GridTable)tableModel).getFields();
		MTabCustomization tabCustomization = MTabCustomization.get(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), gridTab.getAD_Tab_ID(), null);
		if (tabCustomization != null && tabCustomization.getAD_Tab_Customization_ID() > 0 
			&& tabCustomization.getCustom() != null && tabCustomization.getCustom().trim().length() > 0) {
			String custom = tabCustomization.getCustom().trim();
			String[] customComponent = custom.split(";");
			String[] fieldIds = customComponent[0].split("[,]");
			List<GridField> fieldList = new ArrayList<GridField>();
			for(String fieldIdStr : fieldIds) {
				fieldIdStr = fieldIdStr.trim();
				if (fieldIdStr.length() == 0) continue;
				int AD_Field_ID = Integer.parseInt(fieldIdStr);
				for(GridField gridField : tmpFields) {
					if (gridField.getAD_Field_ID() == AD_Field_ID) {
						if(gridField.isDisplayedGrid() && !gridField.isToolbarButton())
							fieldList.add(gridField);
						
						break;
					}
				}
			}
			gridField = fieldList.toArray(new GridField[0]);			
			if (customComponent.length == 2) {
				String[] widths = customComponent[1].split("[,]");
				for(int i = 0; i< gridField.length && i<widths.length; i++) {
					columnWidthMap.put(gridField[i].getAD_Field_ID(), widths[i]);
				}
			}
		} else {
			ArrayList<GridField> gridFieldList = new ArrayList<GridField>();
			
			for(GridField field:tmpFields){
				if(field.isDisplayedGrid() && !field.isToolbarButton()) {
					gridFieldList.add(field);
				}
			}
			
			Collections.sort(gridFieldList, new Comparator<GridField>() {
				@Override
				public int compare(GridField o1, GridField o2) {
					return o1.getSeqNoGrid()-o2.getSeqNoGrid();
				}
			});
			
			gridField = new GridField[gridFieldList.size()];
			gridFieldList.toArray(gridField);
		}
		numColumns = gridField.length;
	}

	/**
	 *
	 * @return boolean
	 */
	public boolean isInit() {
		return init;
	}

	/**
	 * call when tab is activated
	 * @param gridTab
	 */
	public void activate(GridTab gridTab) {
		if (!isInit()) {
			init(gridTab);
		}
	}

	/**
	 * refresh after switching from form view
	 * @param gridTab
	 */
	public void refresh(GridTab gridTab) {
		if (this.gridTab != gridTab || !isInit())
		{
			init = false;
			init(gridTab);
		}
		else
		{
			refreshing = true;
			listbox.setModel(listModel);
			updateListIndex();
			refreshing = false;			
		}
	}

	public boolean isRefreshing() {
		return refreshing;
	}

	/**
	 * Update current row from model
	 */
	public void updateListIndex() {
		if (gridTab == null || !gridTab.isOpen()) return;

		int rowIndex  = gridTab.getCurrentRow();
		if (pageSize > 0) {
			if (paging.getTotalSize() != gridTab.getRowCount())
				paging.setTotalSize(gridTab.getRowCount());
			if (paging.getPageCount() > 1 && !gridFooter.isVisible()) {
				gridFooter.setVisible(true);
			}
			int pgIndex = rowIndex >= 0 ? rowIndex % pageSize : 0;
			int pgNo = rowIndex >= 0 ? (rowIndex - pgIndex) / pageSize : 0;
			if (listModel.getPage() != pgNo) {
				listModel.setPage(pgNo);
				if (renderer.isEditing()) {
					renderer.stopEditing(false);
				}
			} else if (rowIndex == renderer.getCurrentRowIndex()){
				if (modeless && !renderer.isEditing())
					echoOnPostSelectedRowChanged();
				return;
			} else {
				if (renderer.isEditing()) {
					renderer.stopEditing(false);
					int editingRow = renderer.getCurrentRowIndex();
					if (editingRow >= 0) {
						int editingPgIndex = editingRow % pageSize;
						int editingPgNo = (editingRow - editingPgIndex) / pageSize;
						if (editingPgNo == pgNo) {
							listModel.updateComponent(renderer.getCurrentRowIndex() % pageSize);
						}
					}
				}
			}
			if (paging.getActivePage() != pgNo) {
				paging.setActivePage(pgNo);
			}
			if (paging.getPageCount() == 1) {
				gridFooter.setVisible(false);
			} else {
				gridFooter.setVisible(true);
			}
			if (rowIndex >= 0 && pgIndex >= 0) {
				echoOnPostSelectedRowChanged();
			}
		} else {
			if (rowIndex >= 0) {
				echoOnPostSelectedRowChanged();
			}
		}
	}

	/**
	 * 
	 */
	protected void echoOnPostSelectedRowChanged() {
		if (getAttribute(ATTR_ON_POST_SELECTED_ROW_CHANGED) == null) {
			setAttribute(ATTR_ON_POST_SELECTED_ROW_CHANGED, Boolean.TRUE);
			Events.echoEvent("onPostSelectedRowChanged", this, null);
		}
	}

	/**
	 * set paging size
	 * @param pageSize
	 */
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public void clear()
	{
		this.getChildren().clear();
	}

	private void setupColumns()
	{
		if (init) return;

		Columns columns = new Columns();
		
		//frozen not working well on tablet devices yet
		if (!AEnv.isTablet())
		{
			Frozen frozen = new Frozen();
			//freeze selection and indicator column
			frozen.setColumns(2);
			listbox.appendChild(frozen);
		}
		
		org.zkoss.zul.Column selection = new Column();
		selection.setWidth("28px");
		try{
			selection.setSort("none");
		} catch (Exception e) {}
		selection.setStyle("border-right: none");
		selectAll = new Checkbox();
		selection.appendChild(selectAll);
		selectAll.setId("selectAll");
		selectAll.addEventListener(Events.ON_CHECK, this);
		columns.appendChild(selection);
		
		org.zkoss.zul.Column indicator = new Column();				
		indicator.setWidth("18px");
		try {
			indicator.setSort("none");
		} catch (Exception e) {}
		indicator.setStyle("border-left: none");
		columns.appendChild(indicator);
		listbox.appendChild(columns);
		columns.setSizable(true);
		columns.setMenupopup("none");
		columns.setColumnsgroup(false);

		Map<Integer, String> colnames = new HashMap<Integer, String>();
		int index = 0;
		for (int i = 0; i < numColumns; i++)
		{
			if (gridField[i].isDisplayedGrid() && !gridField[i].isToolbarButton())
			{
				colnames.put(index, gridField[i].getHeader());
				index++;
				org.zkoss.zul.Column column = new Column();
				int colindex =tableModel.findColumn(gridField[i].getColumnName()); 
				column.setSortAscending(new SortComparator(colindex, true, Env.getLanguage(Env.getCtx())));
				column.setSortDescending(new SortComparator(colindex, false, Env.getLanguage(Env.getCtx())));
				column.setLabel(gridField[i].getHeader());
				if (columnWidthMap != null && columnWidthMap.get(gridField[i].getAD_Field_ID()) != null) {
					column.setWidth(columnWidthMap.get(gridField[i].getAD_Field_ID()));
				} else {
					if (gridField[i].getDisplayType()==DisplayType.YesNo) {
						//safe to use minimum width for checkbox
						column.setHflex("min");
					} else if (DisplayType.isNumeric(gridField[i].getDisplayType()) && "Line".equals(gridField[i].getColumnName())) {
						//special treatment for line
						column.setHflex("min");
					} else {
						int estimatedWidth = 0;
						if (DisplayType.isNumeric(gridField[i].getDisplayType()))
							estimatedWidth = MIN_NUMERIC_COL_WIDTH;
						else if (DisplayType.isLookup(gridField[i].getDisplayType()))
							estimatedWidth = MIN_COMBOBOX_WIDTH;
						else if (DisplayType.isText(gridField[i].getDisplayType()))
							estimatedWidth = gridField[i].getDisplayLength() * 8;
						else
							estimatedWidth = MIN_COLUMN_WIDTH;
					
						int headerWidth = (gridField[i].getHeader().length()+2) * 8;
						if (headerWidth > estimatedWidth)
							estimatedWidth = headerWidth;
						
						if (DisplayType.isLookup(gridField[i].getDisplayType()))
						{
							if (headerWidth > MIN_COMBOBOX_WIDTH)
								column.setHflex("min");
						}
						else if (DisplayType.isNumeric(gridField[i].getDisplayType()))
						{
							if (headerWidth > MIN_NUMERIC_COL_WIDTH)
								column.setHflex("min");
						}
						else if (!DisplayType.isText(gridField[i].getDisplayType()))
						{
							if (headerWidth > MIN_COLUMN_WIDTH)
								column.setHflex("min");
						}
						
						//set estimated width if not using hflex=min
						if (!"min".equals(column.getHflex())) {
							if (estimatedWidth > MAX_COLUMN_WIDTH)
								estimatedWidth = MAX_COLUMN_WIDTH;
							else if ( estimatedWidth < MIN_COLUMN_WIDTH)
								estimatedWidth = MIN_COLUMN_WIDTH;
							column.setWidth(Integer.toString(estimatedWidth) + "px");
						}
					}
				} 
				columns.appendChild(column);
			}
		}
	}

	private void render()
	{
		listbox.addEventListener(Events.ON_CLICK, this);

		updateModel();

		if (pageSize > 0)
		{
			paging = new Paging();
			paging.setPageSize(pageSize);
			paging.setTotalSize(tableModel.getRowCount());
			paging.setDetailed(true);
			gridFooter.appendChild(paging);
			gridFooter.setSclass("adtab-grid-south");
			paging.addEventListener(ZulEvents.ON_PAGING, this);
			renderer.setPaging(paging);
			if (paging.getPageCount() == 1) {
				gridFooter.setVisible(false);
			} else {
				gridFooter.setVisible(true);
			}
			paging.setId("paging");
		}
		else
		{
			gridFooter.setVisible(false);
		}		
		
	}

	private void updateModel() {
		listModel = new GridTableListModel((GridTable)tableModel, windowNo);
		listModel.setPageSize(pageSize);
		if (renderer != null && renderer.isEditing())
			renderer.stopEditing(false);
		renderer = new GridTabRowRenderer(gridTab, windowNo);
		renderer.setGridPanel(this);
		renderer.setADWindowPanel(windowPanel);
		if (pageSize > 0 && paging != null)
			renderer.setPaging(paging);

		listbox.setRowRenderer(renderer);
		listbox.setModel(listModel);
	}

	/**
	 * deactivate panel
	 */
	public void deactivate() {
		if (renderer != null && renderer.isEditing())
			renderer.stopEditing(true);
	}

	public void onEvent(Event event) throws Exception
	{
		if (event == null)
			return;
		else if (event.getTarget() == listbox && Events.ON_CLICK.equals(event.getName()))
		{
			Object data = event.getData();
			org.zkoss.zul.Row row = null;
			String columnName = null;
			if (data != null && data instanceof Component)
			{
				if (data instanceof org.zkoss.zul.Row)
					row = (org.zkoss.zul.Row) data;
				else
				{
					AbstractComponent cmp = (AbstractComponent) data;
					if (cmp.getParent() instanceof org.zkoss.zul.Row)
					{
						row = (Row) cmp.getParent();
						columnName = (String) cmp.getAttribute("columnName");
					}
				}
			}
			if (row != null)
			{
				//click on selected row to enter edit mode
				if (row == renderer.getCurrentRow())
				{
					if (!renderer.isEditing())
					{
						renderer.editCurrentRow();
						if (columnName != null && columnName.trim().length() > 0)
							setFocusToField(columnName);
						else
							renderer.focusToFirstEditor();
					}
				}
				else
				{
					int index = listbox.getRows().getChildren().indexOf(row);
					if (index >= 0 ) {
						columnOnClick = columnName;
						onSelectedRowChange(index);
					}
				}
			}
			event.stopPropagation();
        }
		else if (event.getTarget() == paging)
		{
			int pgNo = paging.getActivePage();
			if (pgNo != listModel.getPage())
			{
				listModel.setPage(pgNo);
				onSelectedRowChange(0);
				gridTab.clearSelection();
			}
		}
		else if (event.getTarget() == selectAll)
		{
			toggleSelectionForAll(selectAll.isChecked());
		}
		else if (event.getName().equals("onSelectRow"))
		{
			Checkbox checkbox = (Checkbox) event.getData();
			int rowIndex = (Integer) checkbox.getAttribute(GridTabRowRenderer.GRID_ROW_INDEX_ATTR);			
			if (checkbox.isChecked())
			{
				gridTab.addToSelection(rowIndex);
				if (!selectAll.isChecked() && isAllSelected())
				{
					selectAll.setChecked(true);
				}
			}
			else
			{
				gridTab.removeFromSelection(rowIndex);
				if (selectAll.isChecked())
					selectAll.setChecked(false);
			}
		}
	}

	private boolean isAllSelected() {
		org.zkoss.zul.Rows rows = listbox.getRows();
		List<Component> childs = rows.getChildren();
		boolean all = false;
		for(Component comp : childs) {
			org.zkoss.zul.Row row = (org.zkoss.zul.Row) comp;
			Component firstChild = row.getFirstChild();
			if (firstChild instanceof Cell) {
				firstChild = firstChild.getFirstChild();
			}
			if (firstChild instanceof Checkbox) {
				Checkbox checkbox = (Checkbox) firstChild;
				if (!checkbox.isChecked())
					return false;
				else
					all = true;
			}
		}
		return all;
	}

	private void toggleSelectionForAll(boolean b) {
		org.zkoss.zul.Rows rows = listbox.getRows();
		List<Component> childs = rows.getChildren();
		for(Component comp : childs) {
			org.zkoss.zul.Row row = (org.zkoss.zul.Row) comp;
			Component firstChild = row.getFirstChild();
			if (firstChild instanceof Cell) {
				firstChild = firstChild.getFirstChild();
			}
			if (firstChild instanceof Checkbox) {
				Checkbox checkbox = (Checkbox) firstChild;
				checkbox.setChecked(b);
				int rowIndex = (Integer) checkbox.getAttribute(GridTabRowRenderer.GRID_ROW_INDEX_ATTR);
				if (b)
					gridTab.addToSelection(rowIndex);
				else
					gridTab.removeFromSelection(rowIndex);
			}
		}		
	}

	private void onSelectedRowChange(int index) {
		if (updateModelIndex(index)) {
			updateListIndex();
		}
	}

	/**
	 * Event after the current selected row change
	 */
	public void onPostSelectedRowChanged() {
		removeAttribute(ATTR_ON_POST_SELECTED_ROW_CHANGED);
		if (listbox.getRows().getChildren().isEmpty())
			return;

		int rowIndex  = gridTab.isOpen() ? gridTab.getCurrentRow() : -1;
		if (rowIndex >= 0 && pageSize > 0) {
			int pgIndex = rowIndex >= 0 ? rowIndex % pageSize : 0;
			org.zkoss.zul.Row row = (org.zkoss.zul.Row) listbox.getRows().getChildren().get(pgIndex);
			if (!isRowRendered(row, pgIndex)) {
				listbox.renderRow(row);
			} else {
				renderer.setCurrentRow(row);
				//remark: following 3 line cause the previously selected row being render twice
//				if (old != null && old != row && oldIndex >= 0 && oldIndex != gridTab.getCurrentRow())
//				{
//					listModel.updateComponent(oldIndex % pageSize);
//				}
			}
			if (modeless && !renderer.isEditing()) {
				renderer.editCurrentRow();
				if (columnOnClick != null && columnOnClick.trim().length() > 0) {
					setFocusToField(columnOnClick);
					columnOnClick = null;
				} else {
					renderer.focusToFirstEditor();
				}
			} else {
				focusToRow(row);
			}
		} else if (rowIndex >= 0) {
			org.zkoss.zul.Row row = (org.zkoss.zul.Row) listbox.getRows().getChildren().get(rowIndex);
			if (!isRowRendered(row, rowIndex)) {
				listbox.renderRow(row);
			} else {
				renderer.setCurrentRow(row);
				//remark: following 3 line cause the previously selected row being render twice
//				if (old != null && old != row && oldIndex >= 0 && oldIndex != gridTab.getCurrentRow())
//				{
//					listModel.updateComponent(oldIndex);
//				}
			}
			if (modeless && !renderer.isEditing()) {
				renderer.editCurrentRow();
				if (columnOnClick != null && columnOnClick.trim().length() > 0) {
					setFocusToField(columnOnClick);
					columnOnClick = null;
				} else {
					renderer.focusToFirstEditor();
				}
			} else {
				focusToRow(row);
			}
		}
	}

	/**
	 * scroll grid to the current focus row
	 */
	public void scrollToCurrentRow() {
		onPostSelectedRowChanged();
	}
	
	private void focusToRow(org.zkoss.zul.Row row) {
		if (renderer.isEditing()) {
			if (columnOnClick != null && columnOnClick.trim().length() > 0) {
				setFocusToField(columnOnClick);
				columnOnClick = null;
			} else {
				renderer.focusToFirstEditor();
			}
		} else {
			Component cmp = null;
			List<?> childs = row.getChildren();
			for (Object o : childs) {
				Component c = (Component) o;
				if (!c.isVisible())
					continue;
				if (c instanceof Cell) {
					cmp = c;
					break;
				}
			}
			if (cmp != null)
				Clients.response(new AuScript(null, "scrollToRow('" + cmp.getUuid() + "');"));

			if (columnOnClick != null && columnOnClick.trim().length() > 0) {
				List<?> list = row.getChildren();
				for(Object element : list) {
					if (element instanceof Div) {
						Div div = (Div) element;
						if (columnOnClick.equals(div.getAttribute("columnName"))) {
							cmp = div.getFirstChild();
							Clients.response(new AuScript(null, "scrollToRow('" + cmp.getUuid() + "');"));
							break;
						}
					}
				}
				columnOnClick = null;
			}
		}
	}

	private boolean isRowRendered(org.zkoss.zul.Row row, int index) {
		if (row.getChildren().size() == 0) {
			return false;
		} else if (row.getChildren().size() == 1) {
			if (!(row.getChildren().get(0) instanceof Div)) {
				return false;
			}
		}
		return true;
	}

	private boolean updateModelIndex(int rowIndex) {
		if (pageSize > 0) {
			int start = listModel.getPage() * listModel.getPageSize();
			rowIndex = start + rowIndex;
		}

		if (gridTab.getCurrentRow() != rowIndex) {
			gridTab.navigate(rowIndex);
			return true;
		}
		return false;
	}

	/**
	 * @return Grid
	 */
	public Grid getListbox() {
		return listbox;
	}

	/**
	 * Validate display properties of fields of current row
	 * @param col
	 */
	public void dynamicDisplay(int col) {
		if (gridTab == null || !gridTab.isOpen())
        {
            return;
        }
		
		if (renderer.getEditors().isEmpty())
			listbox.onInitRender();

        //  Selective
        if (col > 0)
        {
        	GridField changedField = gridTab.getField(col);
            String columnName = changedField.getColumnName();
            ArrayList<?> dependants = gridTab.getDependantFields(columnName);
			if ( ! (   dependants.size() > 0
					|| changedField.getCallout().length() > 0
					|| Core.findCallout(gridTab.getTableName(), columnName).size() > 0)) {
                return;
            }
        }
        	

        boolean noData = gridTab.getRowCount() == 0;
        List<WEditor> list =  renderer.getEditors();
        dynamicDisplayEditors(noData, list);   //  all components
	}

	private void dynamicDisplayEditors(boolean noData, List<WEditor> list) {
		for (WEditor comp : list)
        {
            GridField mField = comp.getGridField();
            if (mField != null)
            {
                if (noData)
                {
                    comp.setReadWrite(false);
                }
                else
                {
                	comp.dynamicDisplay();
                    boolean rw = mField.isEditableGrid(true);   //  r/w - check Context
                    comp.setReadWrite(rw);
                }
                
                Properties ctx = isDetailPane() ? new GridRowCtx(Env.getCtx(), gridTab, gridTab.getCurrentRow()) 
            		: mField.getVO().ctx;
                
                comp.setVisible(mField.isDisplayedGrid() && mField.isDisplayed(ctx, true));
            }
        }
	}

	private boolean isDetailPane() {
		Component parent = this.getParent();
		while (parent != null) {
			if (parent instanceof DetailPane) {
				return true;
			} 
			parent = parent.getParent();					
		}
		return false;
	}
	
	/**
	 *
	 * @param windowNo
	 */
	public void setWindowNo(int windowNo) {
		this.windowNo = windowNo;
	}

	@Override
	public void focus() {
		if (renderer != null && renderer.isEditing()) {
			renderer.focusToFirstEditor();
		}
	}

	/**
	 * Handle enter key event
	 */
	public boolean onEnterKey() {
		if (!modeless && renderer != null && !renderer.isEditing()) {
			renderer.editCurrentRow();
			renderer.focusToFirstEditor();
			return true;
		}
		return false;
	}

	/**
	 * @param columnName
	 */
	public void setFocusToField(String columnName) {
		for (WEditor editor : renderer.getEditors()) {
			if (columnName.equals(editor.getColumnName())) {
				Component c = editor.getComponent();
				if (c instanceof EditorBox) {
					c = ((EditorBox)c).getTextbox();
				} else if (c instanceof NumberBox) {
					c = ((NumberBox)c).getDecimalbox();
				}
				Clients.response(new AuFocus(c));
				break;
			}
		}
	}

	/**
	 * @param winPanel
	 */
	public void setADWindowPanel(AbstractADWindowContent winPanel) {
		windowPanel = winPanel;
		if (renderer != null)
			renderer.setADWindowPanel(windowPanel);
	}

	public void reInit() {
		this.setupFields(gridTab);
		if(listbox.getFrozen()!=null)
		{
			listbox.removeChild(listbox.getFrozen());
		}
		if (listbox.getColumns() != null) {
			listbox.removeChild(listbox.getColumns());
		}
		init = false;
		setupColumns();
		init = true;
		updateModel();
	}

	public GridField[] getFields() {
		return gridField;
	}
	
	public void onEditCurrentRow() {
		onEditCurrentRow(null);
	}
	
	public void onEditCurrentRow(Event event) {
		if (!renderer.isEditing()) {
			Row currentRow = renderer.getCurrentRow();
			if (currentRow == null || currentRow.getParent() == null || !currentRow.isVisible()) {
				if (event == null) {
					Events.postEvent("onEditCurrentRow", this, null);
				}
			} else {
				renderer.editCurrentRow();
				renderer.focusToFirstEditor();
			}
		} 
	}

	@Override
	public void focusToFirstEditor() {
		if (renderer.isEditing()) {
			renderer.focusToFirstEditor();
		}
	}

	@Override
	public void focusToNextEditor(WEditor ref) {
		if (renderer.isEditing()) {
			renderer.focusToNextEditor(ref);
		}
	}

	@Override
	public void stateChange(StateChangeEvent event) {
		switch(event.getEventType()) {
			case StateChangeEvent.DATA_NEW:
			case StateChangeEvent.DATA_QUERY:
			case StateChangeEvent.DATA_REFRESH_ALL:
				if (selectAll.isChecked())
					selectAll.setChecked(false);
				break;
			case StateChangeEvent.DATA_DELETE:
			case StateChangeEvent.DATA_IGNORE:
				if (!selectAll.isChecked() && isAllSelected())
					selectAll.setChecked(true);
				break;
		}
	}
}
