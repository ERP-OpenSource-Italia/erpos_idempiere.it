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
package org.adempiere.webui.component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.adempiere.util.GridRowCtx;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.editor.WButtonEditor;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WEditorPopupMenu;
import org.adempiere.webui.editor.WebEditorFactory;
import org.adempiere.webui.event.ActionEvent;
import org.adempiere.webui.event.ActionListener;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.panel.AbstractADWindowPanel;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.util.GridTabDataBinder;
import org.adempiere.webui.window.ADWindow;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.NamePair;
import org.zkoss.xml.XMLs;
import org.zkoss.zk.au.out.AuFocus;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Paging;
import org.zkoss.zul.RendererCtrl;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.RowRendererExt;
import org.zkoss.zul.impl.XulElement;
import org.zkoss.zhtml.Label;
import org.zkoss.zhtml.Text;

/**
 * Row renderer for GridTab grid.
 * @author hengsin
 * 
 * @author Teo Sarca, teo.sarca@gmail.com
 * 		<li>BF [ 2996608 ] GridPanel is not displaying time
 * 			https://sourceforge.net/tracker/?func=detail&aid=2996608&group_id=176962&atid=955896
 */
public class GridTabRowRenderer implements RowRenderer<Object[]>, RowRendererExt, RendererCtrl {

	private static final int MAX_TEXT_LENGTH = 60;
	private GridTab gridTab;
	private int windowNo;
	private GridTabDataBinder dataBinder;
	private Map<GridField, WEditor> editors = new LinkedHashMap<GridField, WEditor>();
	private Paging paging;

	private Map<String, Map<Object, String>> lookupCache = null;
	private RowListener rowListener;

	private Grid grid = null;
	private GridPanel gridPanel = null;
	private Row currentRow;
	private Object[] currentValues;
	private boolean editing = false;
	private int currentRowIndex = -1;
	private AbstractADWindowPanel m_windowPanel;

	/**
	 *
	 * @param gridTab
	 * @param windowNo
	 */
	public GridTabRowRenderer(GridTab gridTab, int windowNo) {
		this.gridTab = gridTab;
		this.windowNo = windowNo;
		this.dataBinder = new GridTabDataBinder(gridTab);
	}

	private WEditor getEditorCell(GridField gridField) {
		WEditor editor = editors.get(gridField);
		if (editor != null)  {
			prepareFieldEditor(gridField, editor);
		}
		editor.addValueChangeListener(dataBinder);
		gridField.removePropertyChangeListener(editor);
		gridField.addPropertyChangeListener(editor);
		editor.setValue(gridField.getValue());
		return editor;
	}

	private void prepareFieldEditor(GridField gridField, WEditor editor) {
			if (editor instanceof WButtonEditor)
            {
				if (m_windowPanel != null)
				{
					((WButtonEditor)editor).addActionListener(m_windowPanel);	
				}
				else
				{
					Object window = SessionManager.getAppDesktop().findWindow(windowNo);
	            	if (window != null && window instanceof ADWindow)
	            	{
	            		AbstractADWindowPanel windowPanel = ((ADWindow)window).getADWindowPanel();
	            		((WButtonEditor)editor).addActionListener(windowPanel);
	            	}
				}
            }

            //streach component to fill grid cell
			if (editor.getComponent() instanceof HtmlBasedComponent) {
            	editor.fillHorizontal();
		}
	}

	public int getColumnIndex(GridField field) {
		GridField[] fields = gridPanel.getFields();
		for(int i = 0; i < fields.length; i++) {
			if (fields[i] == field)
				return i;
		}
		return 0;
	}

	private Component createReadonlyCheckbox(Object value) {
		Checkbox checkBox = new Checkbox();
		if (value != null && "true".equalsIgnoreCase(value.toString()))
			checkBox.setChecked(true);
		else
			checkBox.setChecked(false);
		checkBox.setDisabled(true);
		return checkBox;
	}

	private String getDisplayText(Object value, GridField gridField)
	{
		if (value == null)
			return "";

		if (gridField.isEncryptedField())
		{
			return "********";
		}
		else if (gridField.isLookup())
    	{
			if (lookupCache != null)
			{
				Map<Object, String> cache = lookupCache.get(gridField.getColumnName());
				if (cache != null && cache.size() >0)
				{
					String text = cache.get(value);
					if (text != null)
					{
						return text;
					}
				}
			}
			NamePair namepair = gridField.getLookup().get(value);
			if (namepair != null)
			{
				String text = namepair.getName();
				if (lookupCache != null)
				{
					Map<Object, String> cache = lookupCache.get(gridField.getColumnName());
					if (cache == null)
					{
						cache = new HashMap<Object, String>();
						lookupCache.put(gridField.getColumnName(), cache);
					}
					cache.put(value, text);
				}
				return text;
			}
			else
				return "";
    	}
		else if (DisplayType.getClass(gridField.getDisplayType(), false).equals(Timestamp.class))
    	{
    		SimpleDateFormat dateFormat = DisplayType.getDateFormat(gridField.getDisplayType(), AEnv.getLanguage(Env.getCtx()));
    		return dateFormat.format((Timestamp)value);
    	}
    	else if (DisplayType.isNumeric(gridField.getDisplayType()))
    	{
    		return DisplayType.getNumberFormat(gridField.getDisplayType(), AEnv.getLanguage(Env.getCtx())).format(value);
    	}
    	else if (DisplayType.Button == gridField.getDisplayType())
    	{
    		return "";
    	}
    	else if (DisplayType.Image == gridField.getDisplayType())
    	{
    		if (value == null || (Integer)value <= 0)
    			return "";
    		else
    			return "...";
    	}
    	else
    		return value.toString();
	}

	private Component getDisplayComponent(int rowIndex, Object value, GridField gridField) {
		Component component;
		if (gridField.getDisplayType() == DisplayType.YesNo) {
			component = createReadonlyCheckbox(value);
		} else if (gridField.getDisplayType() == DisplayType.Button) {
			GridRowCtx gridRowCtx = new GridRowCtx(Env.getCtx(), gridTab, rowIndex);
			WButtonEditor editor = new WButtonEditor(gridField, rowIndex);
			editor.setValue(gridTab.getValue(rowIndex, gridField.getColumnName()));
			editor.setReadWrite(gridField.isEditable(gridRowCtx, true));
			editor.getComponent().setAttribute("grid.row.index", rowIndex);
			editor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					WButtonEditor editor = (WButtonEditor) event.getSource();
					int rowIndex = (Integer) editor.getComponent().getAttribute("grid.row.index");
					int newRowIndex = gridTab.navigate(rowIndex);
					if (newRowIndex == rowIndex) {
						m_windowPanel.actionPerformed(event);
					}
				}
			});
			component = editor.getComponent();
		} else {
			String text = getDisplayText(value, gridField);

			Label label = new Label();
			setLabelText(text, label);

			component = label;
		}
		return component;
	}

	/**
	 * @param text
	 * @param label
	 */
	private void setLabelText(String text, Label label) {
		String display = text;
		if (text != null && text.length() > MAX_TEXT_LENGTH)
			display = text.substring(0, MAX_TEXT_LENGTH - 3) + "...";
		if (display != null)
			display = XMLs.encodeText(display);
		label.appendChild(new Text(display));
		if (text != null && text.length() > MAX_TEXT_LENGTH)
			label.setDynamicProperty("title", text);
		else
			label.setDynamicProperty("title", "");
	}

	/**
	 *
	 * @return active editor list
	 */
	public List<WEditor> getEditors() {
		List<WEditor> editorList = new ArrayList<WEditor>();
		if (!editors.isEmpty())
			editorList.addAll(editors.values());

		return editorList;
	}

	/**
	 * @param paging
	 */
	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	/**
	 * Detach all editor and optionally set the current value of the editor as cell label.
	 * @param updateCellLabel
	 */
	public void stopEditing(boolean updateCellLabel) {
		if (!editing) {
			return;
		} else {
			editing = false;
		}
		Row row = null;
		for (Entry<GridField, WEditor> entry : editors.entrySet()) {
			if (entry.getValue().getComponent().getParent() != null) {
				Component child = entry.getValue().getComponent();
				Div div = null;
				while (div == null && child != null) {
					Component parent = child.getParent();
					if (parent instanceof Div && parent.getParent() instanceof Row)
						div = (Div)parent;
					else
						child = parent;
				}
				Component component = (Component) div.getAttribute("display.component");
				if (updateCellLabel) {
					if (component instanceof Label) {
						Label label = (Label)component;
						label.getChildren().clear();
						String text = getDisplayText(entry.getValue().getValue(), entry.getValue().getGridField());
						setLabelText(text, label);
					} else if (component instanceof Checkbox) {
						Checkbox checkBox = (Checkbox)component;
						Object value = entry.getValue().getValue();
						if (value != null && "true".equalsIgnoreCase(value.toString()))
							checkBox.setChecked(true);
						else
							checkBox.setChecked(false);
					}
				}
				if (row == null)
					row = ((Row)div.getParent());

				entry.getValue().getComponent().detach();
				entry.getKey().removePropertyChangeListener(entry.getValue());
				entry.getValue().removeValuechangeListener(dataBinder);
				
				div.appendChild(component);
			}
		}

		GridTableListModel model = (GridTableListModel) grid.getModel();
		model.setEditing(false);
	}

	/**
	 * @param row
	 * @param data
	 * @see RowRenderer#render(Row, Object)
	 */
	@Override
	public void render(Row row, Object[] data, int index) throws Exception {
		//don't render if not visible
		if (gridPanel != null && !gridPanel.isVisible()) {
			return;
		}

		if (grid == null)
			grid = (Grid) row.getParent().getParent();

		if (rowListener == null)
			rowListener = new RowListener((Grid)row.getParent().getParent());

		GridField[] gridPanelFields = gridPanel.getFields();
		int columnCount = gridPanelFields.length;
		
		GridField[] gridTabFields = gridTab.getFields();
		boolean isGridViewCustomized = gridTabFields.length != gridPanelFields.length;
		if (!isGridViewCustomized) {
			for(int i = 0; i < gridTabFields.length; i++) {
				if (gridPanelFields[i].getAD_Field_ID() != gridTabFields[i].getAD_Field_ID()) {
					isGridViewCustomized = true;
					break;
				}
			}
		}
		if (!isGridViewCustomized) {
			currentValues = data;
		} else {
			List<Object> dataList = new ArrayList<Object>();
			for(GridField gridField : gridPanelFields) {
				for(int i = 0; i < gridTabFields.length; i++) {
					if (gridField.getAD_Field_ID() == gridTabFields[i].getAD_Field_ID()) {
						dataList.add(data[i]);
						break;
					}
				}
			}
			currentValues = dataList.toArray(new Object[0]);
		}
		
		
		Grid grid = (Grid) row.getParent().getParent();
		org.zkoss.zul.Columns columns = grid.getColumns();

		int rowIndex = row.getParent().getChildren().indexOf(row);
		if (paging != null && paging.getPageSize() > 0) {
			rowIndex = (paging.getActivePage() * paging.getPageSize()) + rowIndex;
		}

		Cell cell = new Cell();
		cell.setWidth("10px");
		//TODO: checkbox for selection and batch action ( delete, export, complete, etc )
//		cell.appendChild(new Checkbox());
		row.appendChild(cell);
		
		int colIndex = -1;
		for (int i = 0; i < columnCount; i++) {
			if (editors.get(gridPanelFields[i]) == null)
				editors.put(gridPanelFields[i], WebEditorFactory.getEditor(gridPanelFields[i], true));
			
			if (!gridPanelFields[i].isDisplayed()) {
				continue;
			}
			colIndex ++;

			Div div = new Div();
			String divStyle = "border: none; width: 100%; height: 100%; cursor: pointer;";
			org.zkoss.zul.Column column = (org.zkoss.zul.Column) columns.getChildren().get(colIndex);
			if (column.isVisible()) {
				Component component = getDisplayComponent(rowIndex, currentValues[i], gridPanelFields[i]);
				div.appendChild(component);
				div.setAttribute("display.component", component);

				if (DisplayType.YesNo == gridPanelFields[i].getDisplayType() || DisplayType.Image == gridPanelFields[i].getDisplayType()) {
					divStyle += "text-align:center; ";
				}
				else if (DisplayType.isNumeric(gridPanelFields[i].getDisplayType())) {
					divStyle += "text-align:right; ";
				}
			}
			div.setStyle(divStyle);
			div.setAttribute("columnName", gridPanelFields[i].getColumnName());
			div.addEventListener(Events.ON_CLICK, rowListener);
			div.addEventListener(Events.ON_DOUBLE_CLICK, rowListener);
			row.addEventListener(Events.ON_CLICK, rowListener);
			row.setStyle("cursor:pointer");
			row.appendChild(div);
		}

		if (rowIndex == gridTab.getCurrentRow()) {
			setCurrentRow(row);
		}
		row.addEventListener(Events.ON_OK, rowListener);
	}

	/**
	 * @param row
	 */
	public void setCurrentRow(Row row) {
		if (currentRow != null && currentRow.getParent() != null && currentRow != row) {
			Cell cell = (Cell) currentRow.getFirstChild();
			if (cell != null) {
				cell.setStyle("background-color: transparent");
				cell.setSclass(null);
			}
		}
		currentRow = row;
		Cell cell = (Cell) currentRow.getFirstChild();
		if (cell != null) {
			cell.setSclass("current-row-indicator");
		}
		currentRowIndex = gridTab.getCurrentRow();
		
		if (currentRowIndex == gridTab.getCurrentRow()) {
			if (editing) {
				stopEditing(false);
				editCurrentRow();
			}
		} else {
			currentRowIndex = gridTab.getCurrentRow();
			if (editing) {
				stopEditing(false);
			}
		}
	}

	/**
	 * @return Row
	 */
	public Row getCurrentRow() {
		return currentRow;
	}

	/**
	 * @return current row index ( absolute )
	 */
	public int getCurrentRowIndex() {
		return currentRowIndex;
	}

	/**
	 * Enter edit mode
	 */
	public void editCurrentRow() {
		if (currentRow != null && currentRow.getParent() != null && currentRow.isVisible()
			&& grid != null && grid.isVisible() && grid.getParent() != null && grid.getParent().isVisible()) {
			GridField[] gridPanelFields = gridPanel.getFields();
			int columnCount = gridPanelFields.length;
			org.zkoss.zul.Columns columns = grid.getColumns();
			//skip indicator column
			int colIndex = 0;
			for (int i = 0; i < columnCount; i++) {
				if (!gridPanelFields[i].isDisplayed()) {
					continue;
				}
				colIndex ++;
				
				if (editors.get(gridPanelFields[i]) == null)
					editors.put(gridPanelFields[i], WebEditorFactory.getEditor(gridPanelFields[i], true));

				org.zkoss.zul.Column column = (org.zkoss.zul.Column) columns.getChildren().get(colIndex);
				if (column.isVisible()) {
					Div div = (Div) currentRow.getChildren().get(colIndex);
					div.getChildren().clear();
					WEditor editor = getEditorCell(gridPanelFields[i]);
					div.appendChild(editor.getComponent());
					WEditorPopupMenu popupMenu = editor.getPopupMenu();

		            if (popupMenu != null)
		            {
		            	popupMenu.addMenuListener((ContextMenuListener)editor);
		            	div.appendChild(popupMenu);
		            	popupMenu.addContextElement((XulElement) editor.getComponent());
		            }		            
		            
		            //check context
					if (!gridPanelFields[i].isDisplayed(true)) 
					{
						editor.setVisible(false);
					}
					editor.setReadWrite(gridPanelFields[i].isEditable(true));
				}
			}
			editing = true;

			GridTableListModel model = (GridTableListModel) grid.getModel();
			model.setEditing(true);

		}
	}

	/**
	 * @see RowRendererExt#getControls()
	 */
	public int getControls() {
		return DETACH_ON_RENDER;
	}

	/**
	 * @see RowRendererExt#newCell(Row)
	 */
	public Component newCell(Row row) {
		return null;
	}

	/**
	 * @see RowRendererExt#newRow(Grid)
	 */
	public Row newRow(Grid grid) {
		return null;
	}

	/**
	 * @see RendererCtrl#doCatch(Throwable)
	 */
	public void doCatch(Throwable ex) throws Throwable {
		lookupCache = null;
	}

	/**
	 * @see RendererCtrl#doFinally()
	 */
	public void doFinally() {
		lookupCache = null;
	}

	/**
	 * @see RendererCtrl#doTry()
	 */
	public void doTry() {
		lookupCache = new HashMap<String, Map<Object,String>>();
	}

	/**
	 * set focus to first active editor
	 */
	public void setFocusToEditor() {
		if (currentRow != null && currentRow.getParent() != null) {
			WEditor toFocus = null;
			WEditor firstEditor = null;
			for (WEditor editor : getEditors()) {
				if (editor.isHasFocus() && editor.isVisible() && editor.getComponent().getParent() != null) {
					toFocus = editor;
					break;
				}

				if (editor.isVisible() && editor.getComponent().getParent() != null) {
					if (toFocus == null && editor.isReadWrite()) {
						toFocus = editor;
					}
					if (firstEditor == null)
						firstEditor = editor;
				}
			}
			if (toFocus != null) {
				Component c = toFocus.getComponent();
				if (c instanceof EditorBox) {
					c = ((EditorBox)c).getTextbox();
				} else if (c instanceof NumberBox) {
					c = ((NumberBox)c).getDecimalbox();
				}
				Clients.response(new AuFocus(c));
			} else if (firstEditor != null) {
				Component c = firstEditor.getComponent();
				if (c instanceof EditorBox) {
					c = ((EditorBox)c).getTextbox();
				} else if (c instanceof NumberBox) {
					c = ((NumberBox)c).getDecimalbox();
				}
				Clients.response(new AuFocus(c));
			}
		}
	}

	/**
	 *
	 * @param gridPanel
	 */
	public void setGridPanel(GridPanel gridPanel) {
		this.gridPanel = gridPanel;
	}

	class RowListener implements EventListener<Event> {

		private Grid _grid;

		public RowListener(Grid grid) {
			_grid = grid;
		}

		public void onEvent(Event event) throws Exception {
			if (Events.ON_CLICK.equals(event.getName())) {
				Event evt = new Event(Events.ON_CLICK, _grid, event.getTarget());
				Events.sendEvent(_grid, evt);
				evt.stopPropagation();
			}
			else if (Events.ON_DOUBLE_CLICK.equals(event.getName())) {
				Event evt = new Event(Events.ON_DOUBLE_CLICK, _grid, _grid);
				Events.sendEvent(_grid, evt);
			}
			else if (Events.ON_OK.equals(event.getName())) {
				Event evt = new Event(Events.ON_OK, _grid, _grid);
				Events.sendEvent(_grid, evt);
			}
		}
	}

	/**
	 * @return boolean
	 */
	public boolean isEditing() {
		return editing;
	}

	/**
	 * @param windowPanel
	 */
	public void setADWindowPanel(AbstractADWindowPanel windowPanel) {
		this.m_windowPanel = windowPanel;
	}
}
