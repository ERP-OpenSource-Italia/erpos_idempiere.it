/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2016 ERP OpenSoure Italia                							  *
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

package org.adempiere.webui.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.editor.IEditorPopupMenuItem;
import org.adempiere.webui.editor.WEditorPopupMenu;

/** Utility class to manage editor popup items. The implementantion is not thread safe
 * 
 * @author Silvano Trinchero, www.freepath.it
 *
 */
public class WEditorPopupMenuItems 
{
	private static final List<String> STANDARD_ITEMS; 
	private Map<String, IEditorPopupMenuItem> items = new LinkedHashMap<>();
	
	static
	{
		STANDARD_ITEMS = new ArrayList<>();
		
		STANDARD_ITEMS.add(WEditorPopupMenu.ZOOM_EVENT);
		STANDARD_ITEMS.add(WEditorPopupMenu.REQUERY_EVENT);
		STANDARD_ITEMS.add(WEditorPopupMenu.PREFERENCE_EVENT);
		STANDARD_ITEMS.add(WEditorPopupMenu.NEW_EVENT);
		STANDARD_ITEMS.add(WEditorPopupMenu.UPDATE_EVENT);
		STANDARD_ITEMS.add(WEditorPopupMenu.SHOWLOCATION_EVENT);
		STANDARD_ITEMS.add(WEditorPopupMenu.CHANGE_LOG_EVENT);
		STANDARD_ITEMS.add(WEditorPopupMenu.EDITOR_EVENT);
	}
	
	public List<IEditorPopupMenuItem> getItems()
	{
		List<IEditorPopupMenuItem> allItems = new ArrayList<>();
		
		for(IEditorPopupMenuItem item:items.values())
		{
			allItems.add(item);
		}
		
		return allItems;
	}
	
	public List<IEditorPopupMenuItem> getNonStandardItems()
	{
		List<IEditorPopupMenuItem> nonStandardItems = new ArrayList<>();
		
		for(IEditorPopupMenuItem item:items.values())
		{
			if(STANDARD_ITEMS.contains(item.getEvent()) == false)
				nonStandardItems.add(item);
		}
		
		return nonStandardItems;		
	}
	
	public boolean hasEvent(String event)
	{
		return items.containsKey(event);
	}
	
	public void removeByEvent(String event)
	{
		items.remove(event);
	}
	
	public IEditorPopupMenuItem getByEvent(String event)
	{
		return items.get(event);
	}
	
	public void add(IEditorPopupMenuItem item)
	{
		String evt = item.getEvent();
		
		if(items.containsKey(evt))
			items.remove(evt);
		
		items.put(evt,item);		
	}
	
	public void addAll(List<IEditorPopupMenuItem> allItems)
	{
		for(IEditorPopupMenuItem item:allItems)
			add(item);
	}

}
