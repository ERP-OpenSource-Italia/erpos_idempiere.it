/*******************************************************************************
 * Copyright (C) 2016 Trek Global Inc.										   *
 * Copyright (C) 2016 Low Heng Sin                                             *
 * This program is free software; you can redistribute it and/or modify it     *
 * under the terms version 2 of the GNU General Public License as published    *
 * by the Free Software Foundation. This program is distributed in the hope    *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied  *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            *
 * See the GNU General Public License for more details.                        *
 * You should have received a copy of the GNU General Public License along     *
 * with this program; if not, write to the Free Software Foundation, Inc.,     *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                      *
 *******************************************************************************/

package org.adempiere.webui.editor.grid.selection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.adempiere.webui.ValuePreference;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WEditorPopupMenu;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.window.WFieldRecordInfo;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridTabVO;
import org.compiere.model.GridWindow;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.ext.Selectable;

/**
 * 
 * @author hengsin
 *
 */
public class WGridTabMultiSelectionEditor extends WEditor implements ContextMenuListener
{
	private static final String[] LISTENER_EVENTS = {Events.ON_SELECT};
	
	//LS query
	
	private static final String SQL_COLUMN_NAME = "SELECT tab.AD_Tab_ID, tab.ad_window_id "
			+ "FROM AD_Column c "
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
			+ " INNER JOIN AD_Tab tab ON (t.AD_Window_ID=tab.AD_Window_ID AND tab.AD_Table_ID=t.AD_Table_ID) "
			+ "WHERE c.columnname = ? AND c.IsKey='Y' "
			+ "ORDER BY tab.tablevel ";
	private static final String SQL_REFERENCE_VALUE = "SELECT tab.AD_Tab_ID, tab.ad_window_id "
			+ "FROM AD_Column c "
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
			+ " INNER JOIN AD_Ref_Table rt ON (rt.AD_Table_ID=t.AD_Table_ID AND c.AD_Column_ID = rt.AD_Key) "
			+ " INNER JOIN AD_Tab tab ON (coalesce(rt.AD_Window_ID, t.AD_Window_ID)=tab.AD_Window_ID AND tab.AD_Table_ID=t.AD_Table_ID) "
			+ "WHERE rt.AD_Reference_ID = ? AND c.IsKey='Y' "
			+ "ORDER BY tab.tablevel ";
	
	//LS

    private Object oldValue;

	private boolean tableEditor = false;
	
	private GridTab listViewGridTab = null;

	private String currentLinkValue = null;

	private boolean readWrite;
	
    public WGridTabMultiSelectionEditor(GridField gridField) {
    	this(gridField, false);
    }

    public WGridTabMultiSelectionEditor(GridField gridField, boolean tableEditor)
    {
        super(tableEditor ? new Textbox() : new GridTabSelectionListView(true, gridField.getWindowNo()), gridField);
        this.tableEditor = tableEditor;
        init();
    }

    @Override
    public HtmlBasedComponent getComponent() {
    	return (HtmlBasedComponent) component;
    }

    @Override
	public boolean isReadWrite() {
    	return readWrite;		
	}

	@Override
	public void setReadWrite(boolean readWrite) {
		if (getComponent() instanceof GridTabSelectionListView) {
    		GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
    		listView.getListbox().setEnabled(readWrite);
    	}
		this.readWrite = readWrite; 
	}

	private void init()
    {
		if (tableEditor)
		{
			((Textbox)getComponent()).setReadonly(true);
		}
		else if (gridField != null)
		{
			int AD_Tab_ID = 0, AD_Window_ID = 0;
			GridWindow gridWindow;
			
			if (gridField.getGridTab() != null)
			{
				AD_Tab_ID = gridField.getIncluded_Tab_ID();
				gridWindow = gridField.getGridTab().getGridWindow();
			}
			//LS enable multiselction 'style' for process params
			else
			{
					
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				try
				{
					
					if (gridField.getVO().AD_Reference_Value_ID > 0){
						pstmt = DB.prepareStatement(SQL_REFERENCE_VALUE, null);
						pstmt.setInt(1, gridField.getVO().AD_Reference_Value_ID);
					}else{
						pstmt = DB.prepareStatement(SQL_COLUMN_NAME, null);
						pstmt.setString(1, gridField.getVO().ColumnName);
					}
					
					rs = pstmt.executeQuery();

					if (rs.next())
					{
						AD_Tab_ID = rs.getInt(1);
						AD_Window_ID = rs.getInt(2);
					}
					
				}
				catch (SQLException e)
				{
//					log.log(Level.SEVERE, sql, e);
				}
				finally
				{
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
				
				if (AD_Tab_ID == 0 || AD_Window_ID == 0){
					return;
				}
				
				gridWindow = GridWindow.get(Env.getCtx(), 0, AD_Window_ID);
			}
			
			//LS end
			
			int count = gridWindow.getTabCount();
			GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
			for(int i = 0; i < count; i++)
			{
				GridTab t = gridWindow.getTab(i);
				if (t.getAD_Tab_ID() == AD_Tab_ID)
				{
					GridTabVO vo = t.getVO();
					listViewGridTab = new GridTab(vo, gridWindow);
					String lcn = t.getLinkColumnName();
					if (Util.isEmpty(lcn)) {
						t.setLinkColumnName(null);
						lcn = t.getLinkColumnName();
					}
					listViewGridTab.setLinkColumnName(lcn);
					listView.init(listViewGridTab);
					break;
				}
			}
			
			popupMenu = new WEditorPopupMenu(false, false, isShowPreference());
			Menuitem clear = new Menuitem(Msg.getMsg(Env.getCtx(), "ClearSelection"), null);
			clear.setAttribute("EVENT", "onClearSelection");
			clear.addEventListener(Events.ON_CLICK, popupMenu);
			popupMenu.appendChild(clear);
			
			listView.getListbox().setContext(popupMenu);
		}
    }

	public void onEvent(Event event)
    {
    	if (Events.ON_SELECT.equals(event.getName()))
    	{
    		Object newValue = null;
    		GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
    		Listbox listbox = listView.getListbox();
    		ListModel<GridTableRow> model = listbox.getModel();
    		if (model != null && model instanceof Selectable<?>)
    		{
    			StringBuilder buffer = new StringBuilder();
    			@SuppressWarnings("unchecked")
				Selectable<GridTableRow> selectable = (Selectable<GridTableRow>) model;
    			Set<GridTableRow> selection = selectable.getSelection();
    			for(GridTableRow row : selection) 
    			{
    				if (buffer.length() > 0)
		        		buffer.append(",");
    				buffer.append(row.hashCode());
    			}
    			newValue = buffer.toString();
    		}
    		else
    		{
	    		int[] selection = listView.getListbox().getSelectedIndices();
		        StringBuilder buffer = new StringBuilder();
		        for(int rowIndex : selection) 
		        {
		        	int id = listViewGridTab.getKeyID(rowIndex);
		        	if (buffer.length() > 0)
		        		buffer.append(",");
		        	buffer.append(id);
		        }
		        newValue = buffer.toString();
    		}
	        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
	    	    return;
	    	}
	        if (oldValue == null && newValue == null) {
	        	return;
	        }
	        Object prevValue = oldValue;
	        oldValue = newValue;
	        ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), prevValue, newValue);
	        super.fireValueChange(changeEvent);	        
    	}
    }

    @Override
    public String getDisplay()
    {
        return oldValue != null ? oldValue.toString() : "";
    }

    @Override
    public Object getValue()
    {
        return oldValue;
    }

    @Override
    public void setValue(Object value)
    {
    	if (value == null && oldValue == null)
    	{
    		return;
    	}
    	else if (value != null && oldValue != null && value.equals(oldValue))
    	{
    		return;
    	}
    	oldValue = value;
    	if (!tableEditor)
    		updateSlectedIndices();
    	else
    		((Textbox)getComponent()).setValue(oldValue != null ? oldValue.toString() : "");
    }

	private void updateSlectedIndices() {
		GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
		if (!Util.isEmpty((String)oldValue))
        {
        	List<Integer> list = new ArrayList<Integer>();
        	String[] selected = ((String)oldValue).split("[,]");
        	for(String s : selected) {
        		int id = Integer.parseInt(s);
        		for(int i = 0; i < listViewGridTab.getRowCount(); i++) {
        			if (listViewGridTab.getKeyID(i) == id) {
        				list.add(i);
        				break;
        			}
        		}
        	}
        	int[] selectedIndices = new int[list.size()];
        	for(int i = 0; i < selectedIndices.length; i++)
        	{
        		selectedIndices[i] = list.get(i);
        	}
        	listView.setSelectedIndices(selectedIndices);
        }
		else
		{
			listView.clearSelection();
		}
	}

    @Override
    public String[] getEvents()
    {
        return LISTENER_EVENTS;
    }

    public void onMenu(ContextMenuEvent evt)
	{
		if (WEditorPopupMenu.PREFERENCE_EVENT.equals(evt.getContextEvent()))
		{
			if (isShowPreference())
				ValuePreference.start (getComponent(), this.getGridField(), getValue());
			return;
		}
		else if (WEditorPopupMenu.CHANGE_LOG_EVENT.equals(evt.getContextEvent()))
		{
			WFieldRecordInfo.start(gridField);
		}
		else if ("onClearSelection".equals(evt.getContextEvent()))
		{
			ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), oldValue, null);
	        super.fireValueChange(changeEvent);
	        oldValue = null;
		}
	}

	@Override
	public void dynamicDisplay() {
		if (!tableEditor && listViewGridTab != null) {
			String name = listViewGridTab.getLinkColumnName();
			String linkValue = Env.getContext(Env.getCtx(), gridField.getWindowNo(), name);
			if ((currentLinkValue == null && linkValue != null)
				|| (currentLinkValue != null && linkValue == null)
				|| (currentLinkValue != null && linkValue != null && !currentLinkValue.equals(linkValue)))
			{
				GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
				listView.refresh(listViewGridTab);
				updateSlectedIndices();
			}
		}
	}
}
