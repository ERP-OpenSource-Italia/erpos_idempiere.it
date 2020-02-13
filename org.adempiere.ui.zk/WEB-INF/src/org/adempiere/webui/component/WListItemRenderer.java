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

package org.adempiere.webui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.adempiere.base.Service;
import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.event.TableValueChangeEvent;
import org.adempiere.webui.event.TableValueChangeListener;
import org.adempiere.webui.factory.ICellComponentFactory;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.minigrid.IDColumn;
import org.compiere.util.MSort;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.ListitemRendererExt;

/**
 * Renderer for {@link org.adempiere.webui.component.ListItems}
 * for the {@link org.adempiere.webui.component.Listbox}.
 *
 * @author Andrew Kimball
 * @author Monica Bean, www.freepath.it
 * @see  IDEMPIERE-3318 Factory for generating cell renderers https://idempiere.atlassian.net/browse/IDEMPIERE-3318
 */
public class WListItemRenderer implements ListitemRenderer<Object>, EventListener<Event>, ListitemRendererExt
{
	/** Array of listeners for changes in the table components. */
	protected ArrayList<TableValueChangeListener> m_listeners =
            new ArrayList<TableValueChangeListener>();

	/** A list containing the indices of the currently selected ListItems. */
	private Set<ListItem> m_selectedItems = new HashSet<ListItem>();
	/**	Array of table details. */
	private ArrayList<WTableColumn> m_tableColumns = new ArrayList<WTableColumn>();
	/** Array of {@link ListHeader}s for the list head. */
    private ArrayList<ListHeader> m_headers = new ArrayList<ListHeader>();

    private Listbox listBox;

	private EventListener<Event> cellListener;

	//F3P: identifier
	private String	cellFactoryIdentifier;
	
	private List<ListitemEventListener> listitemListeners = null;
	
	/**
	 * Default constructor.
	 *
	 */
	public WListItemRenderer()
	{
		super();
	}
	
	/**
	 * Constructor specifying the column headers.
	 *
	 * @param columnNames	vector of column titles.
	 */
	public WListItemRenderer(List< ? extends String> columnNames) 
	{
		this(columnNames, null); //F3P: identifier
	}

	/**
	 * Constructor specifying the column headers.
	 *
	 * @param columnNames	vector of column titles.
	 * @param cellFactoryIdentifier	identifier
	 */
	public WListItemRenderer(List< ? extends String> columnNames, String cellFactoryIdentifier) //F3P: identifier
	{
		super();
		
		this.cellFactoryIdentifier = cellFactoryIdentifier;
		
		WTableColumn tableColumn;

		for (String columnName : columnNames)
		{
			tableColumn = new WTableColumn();
			tableColumn.setHeaderValue(Util.cleanAmp(columnName));
			m_tableColumns.add(tableColumn);
		}
	}

	/**
	 * Get the column details of the specified <code>column</code>.
	 *
	 * @param columnIndex	The index of the column for which details are to be retrieved.
	 * @return	The details of the column at the specified index.
	 */
	//F3P: changed into public
	public WTableColumn getColumn(int columnIndex)
	{
		try
		{
			return m_tableColumns.get(columnIndex);
		}
		catch (IndexOutOfBoundsException exception)
		{
			throw new IllegalArgumentException("There is no WTableColumn at column "
                    + columnIndex);
		}
	}


	/* (non-Javadoc)
	 * @see org.zkoss.zul.ListitemRenderer#render(org.zkoss.zul.Listitem, java.lang.Object)
	 */
	@Override
	public void render(Listitem item, Object data, int index) throws Exception
	{
		render((ListItem)item, data, index);
		
		// F3P: after render add listeners
		
		if(listitemListeners != null && listitemListeners.size() > 0)
		{
			for(ListitemEventListener lile:listitemListeners)
			{
				item.addEventListener(lile.event, lile.listener);
			}
		}
			
	}

	/**
	 * Renders the <code>data</code> to the specified <code>Listitem</code>.
	 *
	 * @param item 	the listitem to render the result.
	 * 				Note: when this method is called, the listitem has no child
	 * 				at all.
	 * @param data 	that is returned from {@link ListModel#getElementAt}
	 * @throws Exception
	 * @see {@link #render(Listitem, Object)}
	 */
	private void render(ListItem item, Object data, int index)
	{
		Listcell listcell = null;
		int colIndex = 0;
		int rowIndex = item.getIndex();
		WListbox table = null;

		if (item.getListbox() instanceof WListbox)
		{
			table = (WListbox)item.getListbox();
		}

		if (!(data instanceof List))
		{
			throw new IllegalArgumentException("A model element was not a list");
		}

		if (listBox == null || listBox != item.getListbox())
		{
			listBox = item.getListbox();
		}
		if (cellListener == null)
		{
			cellListener = new CellListener();
		}

		for (Object field : (List<?>)data)
		{
			listcell = getCellComponent(table, field, rowIndex, colIndex);
			listcell.setParent(item);
			listcell.addEventListener(Events.ON_DOUBLE_CLICK, cellListener);
			colIndex++;
			// just render column in list m_tableColumns
			if (m_tableColumns != null && m_tableColumns.size() == colIndex)
				break;
		}
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClicked");

		return;
	}

	/**
	 * Generate the cell for the given <code>field</code>.
	 *
	 * @param table 	The table into which the cell will be placed.
	 * @param field		The data field for which the cell is to be created.
	 * @param rowIndex	The row in which the cell is to be placed.
	 * @param columnIndex	The column in which the cell is to be placed.
	 * @return	The list cell component.
	 */
	// F3P: from private to protected to allow overriding
	protected Listcell getCellComponent(WListbox table, Object field, 
									  int rowIndex, int columnIndex)
	{
		ListCell listcell = new ListCell();
		if (m_tableColumns.size() > columnIndex) {
			WTableColumn column = getColumn(columnIndex);
			if (column != null && column.getHeaderValue() != null) {
				listcell.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, column.getHeaderValue().toString());
			}
		}
		boolean isCellEditable = table != null ? table.isCellEditable(rowIndex, columnIndex) : false;

		if (field != null)
		{
			// F3P: IDEMPIERE-3318 - Factory for generating cell renderers
			List<ICellComponentFactory> factories = Service.locator().list(ICellComponentFactory.class).getServices();
			
			if (factories != null) {
				for(ICellComponentFactory factory : factories) 
				{
					boolean managed = factory.createCellComponent(this, listcell, table, field, rowIndex, columnIndex, 
							isCellEditable, cellFactoryIdentifier);
					
					if(managed)
						break;	
				}
			}
		}
		else
		{
			listcell.setLabel("");
			listcell.setValue("");
		}

		return listcell;
	}


	/**
	 *  Update Table Column.
	 *
	 *  @param index	The index of the column to update
	 *  @param header 	The header text for the column
	 */
	public void updateColumn(int index, String header)
	{
		WTableColumn tableColumn;

		tableColumn = getColumn(index);
		tableColumn.setHeaderValue(Util.cleanAmp(header));

		return;
	}   //  updateColumn

	/**
	 * @param header
	 */
	public void addColumn(String header)
	{
		addColumn(header, null);
	}
	
	/**
	 *  Add Table Column.
	 *  after adding a column, you need to set the column classes again
	 *  (DefaultTableModel fires TableStructureChanged, which calls
	 *  JTable.tableChanged .. createDefaultColumnsFromModel
	 *  @param header The header text for the column
	 *  @param description
	 */
	public void addColumn(String header, String description)
	{
		WTableColumn tableColumn;

		tableColumn = new WTableColumn();
		tableColumn.setHeaderValue(Util.cleanAmp(header));
		tableColumn.setTooltipText(description);
		m_tableColumns.add(tableColumn);

		return;
	}   //  addColumn

	/**
	 * Get the number of columns.
	 * @return the number of columns
	 */
	public int getNoColumns()
	{
		return m_tableColumns.size();
	}

	/**
	 * This is unused.
	 * The readonly proprty of a column should be set in
	 * the parent table.
	 *
	 * @param colIndex
	 * @param readOnly
	 * @deprecated
	 */
	public void setRO(int colIndex, Boolean readOnly)
	{
		return;
	}

	/**
	 * Create a ListHeader using the given <code>headerValue</code> to
	 * generate the header text.
	 * The <code>toString</code> method of the <code>headerValue</code>
	 * is used to set the header text.
	 *
	 * @param headerValue	The object to use for generating the header text.
	 * @param tooltipText
     * @param headerIndex   The column index of the header
	 * @param classType
	 * @param columnWidth			The width of column, if null default value
	 * @return The generated ListHeader
	 * @see #renderListHead(ListHead)
	 */
	private Component getListHeaderComponent(Object headerValue, String tooltipText, int headerIndex, Class<?> classType, Integer columnWidth)
	{
        ListHeader header = null;

        String headerText = headerValue.toString();
        if (m_headers.size() <= headerIndex || m_headers.get(headerIndex) == null)
        {
        	if (classType != null && classType.isAssignableFrom(IDColumn.class))
        	{
        		header = new ListHeader("");
        		ZKUpdateUtil.setWidth(header, "30px");
        	}
        	else
        	{
	            Comparator<Object> ascComparator =  getColumnComparator(true, headerIndex);
	            Comparator<Object> dscComparator =  getColumnComparator(false, headerIndex);

	            header = new ListHeader(headerText);
	            if (!Util.isEmpty(tooltipText))
	            {
	            	header.setTooltiptext(tooltipText);
	            }

	            header.setSort("auto");
	            header.setSortAscending(ascComparator);
	            header.setSortDescending(dscComparator);

	            int width = headerText.trim().length() * 9;
	            if (width > 300)
	            	width = 300;
	            else if (classType != null)
	            {
	            	if (classType.equals(String.class))
	            	{
	            		if (width > 0 && width < 180)
	            			width = 180;
	            	}
	            	else if (classType.equals(IDColumn.class))
	            	{
	            		header.setSort("none");
	            		if (width < 30)
	            			width = 30;
	            	}
	            	else if (classType.isAssignableFrom(Boolean.class))
	            	{
	            		if (width > 0 && width < 30)
	            			width = 30;
	            	}
		            else if (width > 0 && width < 100)
	            		width = 100;
	            }
	            else if (width > 0 && width < 100)
	            	width = 100;

	            header.setStyle("min-width: " + width + "px");
        	}
            ZKUpdateUtil.setHflex(header, "min");
            m_headers.add(header);
        }
        else
        {
            header = m_headers.get(headerIndex);

            if (!header.getLabel().equals(headerText))
            {
                header.setLabel(headerText);
            }
        }

        //FINMATICA set width
        if(columnWidth != null)
        	ZKUpdateUtil.setWidth(header, columnWidth + "px");
        
		return header;
	}

	/**
	 * set custom list header
	 * @param index
	 * @param header
	 */
	public void setListHeader(int index, ListHeader header) {
		int size = m_headers.size();
		if (size <= index) {
			while (size <= index) {
				if (size == index)
					m_headers.add(header);
				else
					m_headers.add(null);
				size++;
			}

		} else
			m_headers.set(index, header);
	}

    /**
     * Obtain the comparator for a given column.
     *
     * @param ascending     whether the comparator will sort ascending
     * @param columnIndex   the index of the column for which the comparator is required
     * @return  comparator for the given column for the given direction
     */
    protected Comparator<Object> getColumnComparator(boolean ascending, final int columnIndex)
    {
    	return new ColumnComparator(ascending, columnIndex);
    }

    public static class ColumnComparator implements Comparator<Object>
    {

    	private int columnIndex;
		private MSort sort;

		public ColumnComparator(boolean ascending, int columnIndex)
    	{
    		this.columnIndex = columnIndex;
    		sort = new MSort(0, null);
        	sort.setSortAsc(ascending);
    	}

        public int compare(Object o1, Object o2)
        {
                Object item1 = ((List<?>)o1).get(columnIndex);
                Object item2 = ((List<?>)o2).get(columnIndex);
                return sort.compare(item1, item2);
        }

		public int getColumnIndex()
		{
			return columnIndex;
		}
    }

	/**
	 * Render the ListHead for the table with headers for the table columns.
	 *
	 * @param head	The ListHead component to render.
	 * @see #addColumn(String)
	 * @see #WListItemRenderer(List)
	 */
	public void renderListHead(ListHead head)
	{
		renderListHead(head, null);
	}
	
	
	/**
	 * FIN possibilità di indicare il width
	 * 
	 * Render the ListHead for the table with headers for the table columns.
	 *
	 * @param head	The ListHead component to render.
	 * @param columnWidths
	 * @see #addColumn(String)
	 * @see #WListItemRenderer(List)
	 */
	public void renderListHead(ListHead head, List<? extends Integer> columnWidths)
	{
		Component header;
        WTableColumn column;
        
        if(columnWidths != null && columnWidths.size() == m_tableColumns.size())
        {
        	for (int columnIndex = 0; columnIndex < m_tableColumns.size(); columnIndex++)
            {
        		Integer width = columnWidths.get(columnIndex);
        		
        		if(width != null) // F3P: avoid setting zero-width, and use zero as a flag o
        			m_tableColumns.get(columnIndex).setFixedWidth(width.intValue());
            }
        }

		for (int columnIndex = 0; columnIndex < m_tableColumns.size(); columnIndex++)
        {
            column = m_tableColumns.get(columnIndex);
			header = getListHeaderComponent(column.getHeaderValue(), column.getTooltipText(), columnIndex, column.getColumnClass(), column.getFixedWidth());
			head.appendChild(header);
		}
		head.setSizable(true);

		return;
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.event.EventListener#onEvent(org.zkoss.zk.ui.event.Event)
	 */
	public void onEvent(Event event) throws Exception
	{
		int col = -1;
		int row = -1;
		Object value = null;
		TableValueChangeEvent vcEvent = null;
		WTableColumn tableColumn;

		Component source = event.getTarget();

		if (isWithinListCell(source))
		{
			row = getRowPosition(source);
			col = getColumnPosition(source);

			tableColumn = m_tableColumns.get(col);
			
			// F3P: IDEMPIERE-3318 - Factory for generating cell renderers
			List<ICellComponentFactory> factories = Service.locator().list(ICellComponentFactory.class).getServices();
			
			if (factories != null) {
				for(ICellComponentFactory factory : factories) {
					value = factory.getValueForCell(this, tableColumn, row, col, source, cellFactoryIdentifier);
					if(value != null)
						break;	
				}
			}
			
			if(value != null)
			{
				vcEvent = new TableValueChangeEvent(source,
						tableColumn.getHeaderValue().toString(),
						row, col,
						value, value);

				fireTableValueChange(vcEvent);
			}
		}
		else if (event.getTarget() instanceof WListbox && Events.ON_SELECT.equals(event.getName()))
		{
			WListbox table = (WListbox) event.getTarget();
			if (table.isCheckmark()) {
				int cnt = table.getRowCount();
				if (cnt == 0 || !(table.getValueAt(0, 0) instanceof IDColumn))
					return;

				//update IDColumn
				tableColumn = m_tableColumns.get(0);
				for (int i = 0; i < cnt; i++) {
					IDColumn idcolumn = (IDColumn) table.getValueAt(i, 0);
					Listitem item = table.getItemAtIndex(i);

					value = item.isSelected();
					Boolean old = idcolumn.isSelected();

					if (!old.equals(value)) {
						vcEvent = new TableValueChangeEvent(source,
								tableColumn.getHeaderValue().toString(),
								i, 0,
								old, value);

						fireTableValueChange(vcEvent);
					}
				}
			}
		}

		return;
	}

	private boolean isWithinListCell(Component source) {
		if (source instanceof Listcell)
			return true;
		Component c = source.getParent();
		while(c != null) {
			if (c instanceof Listcell)
				return true;
			c = c.getParent();
		}
		return false;
	}

	/**
	 * Get the row index of the given <code>source</code> component.
	 *
	 * @param source	The component for which the row index is to be found.
	 * @return The row index of the given component.
	 */
	protected int getRowPosition(Component source)
	{
		Listcell cell;
		ListItem item;
		int row = -1;

		cell = findListcell(source);
		item = (ListItem)cell.getParent();

		row = item.getIndex();

		return row;
	}

	private Listcell findListcell(Component source) {
		if (source instanceof Listcell)
			return (Listcell) source;
		Component c = source.getParent();
		while(c != null) {
			if (c instanceof Listcell)
				return (Listcell) c;
			c = c.getParent();
		}
		return null;
	}

	/**
	 * Get the column index of the given <code>source</code> component.
	 *
	 * @param source	The component for which the column index is to be found.
	 * @return The column index of the given component.
	 */
	protected int getColumnPosition(Component source)
	{
		Listcell cell;
		int col = -1;

		cell = findListcell(source);
		col = cell.getColumnIndex();

		return col;
	}


	/**
	 * Reset the renderer.
	 * This should be called if the table using this renderer is cleared.
	 */
	public void clearColumns()
	{
		m_tableColumns.clear();
	}

	/**
	 * Clear the renderer.
	 * This should be called if the table using this renderer is cleared.
	 */
	public void clearSelection()
	{
		m_selectedItems.clear();
	}

	/**
	 * Add a listener for changes in the table's component values.
	 *
	 * @param listener	The listener to add.
	 */
	public void addTableValueChangeListener(TableValueChangeListener listener)
	{
	    if (listener == null)
	    {
	    	return;
	    }

	    m_listeners.add(listener);
	}

	public void removeTableValueChangeListener(TableValueChangeListener listener)
	{
		if (listener == null)
	    {
	    	return;
		}

	    m_listeners.remove(listener);
	}

	/**
	 * Fire the given table value change <code>event</code>.
	 *
	 * @param event	The event to pass to the listeners
	 */
	private void fireTableValueChange(TableValueChangeEvent event)
	{
	    for (TableValueChangeListener listener : m_listeners)
	    {
	       listener.tableValueChange(event);
	    }
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zul.ListitemRendererExt#getControls()
	 */
	public int getControls()
	{
		return DETACH_ON_RENDER;
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zul.ListitemRendererExt#newListcell(org.zkoss.zul.Listitem)
	 */
	public Listcell newListcell(Listitem item)
	{
		ListCell cell = new ListCell();
		cell.applyProperties();
		return cell;
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zul.ListitemRendererExt#newListitem(org.zkoss.zul.Listbox)
	 */
	public Listitem newListitem(Listbox listbox)
	{
		ListItem item = new ListItem();
		item.applyProperties();

		return item;
	}

	/**
	 * @param index
	 * @param header
	 */
	public void setColumnHeader(int index, String header)
	{
		if (index >= 0 && index < m_tableColumns.size())
		{
			m_tableColumns.get(index).setHeaderValue(Util.cleanAmp(header));
		}

	}

	public void setColumnClass(int index, Class<?> classType) {
		if (index >= 0 && index < m_tableColumns.size())
		{
			m_tableColumns.get(index).setColumnClass(classType);
		}
	}

	//F3P: IDEMPIERE-3318 - Factory for generating cell renderers
	public List<WTableColumn> getTableColumns() {
		return Collections.unmodifiableList(m_tableColumns);
	}

	class CellListener implements EventListener<Event> {

		public CellListener() {
		}

		public void onEvent(Event event) throws Exception {
			if (listBox != null && Events.ON_DOUBLE_CLICK.equals(event.getName())) {
				Event evt = new Event(Events.ON_DOUBLE_CLICK, listBox);
				Events.sendEvent(listBox, evt);
			}
		}

	}

	public String getCellFactoryIdentifier() {
		return cellFactoryIdentifier;
	}

	public void setCellFactoryIdentifier(String cellFactoryIdentifier) {
		this.cellFactoryIdentifier = cellFactoryIdentifier;
	}
	
	// F3P: Listeners to be added to list items
	
	public void addListitemEventListener(String evtnm, EventListener<Event> listener)
	{
		ListitemEventListener lile = new ListitemEventListener(evtnm,listener);
		
		if(listitemListeners == null)
			listitemListeners = new ArrayList<>();
		
		listitemListeners.add(lile);		
	}
	
	private static class ListitemEventListener
	{
		private ListitemEventListener(String evnm, EventListener<Event> listener)
		{
			this.event = evnm;
			this.listener = listener;
		}
				
		private String event;
		private EventListener<Event> listener;
	}
}


