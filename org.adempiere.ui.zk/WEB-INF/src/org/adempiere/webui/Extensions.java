/******************************************************************************
 * This file is part of Adempiere ERP Bazaar                                  *
 * http://www.adempiere.org                                                   *
 *                                                                            *
 * Copyright (C) Jorg Viola			                                          *
 * Copyright (C) Contributors												  *
 *                                                                            *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * Contributors:                                                              *
 * - Heng Sin Low                                                             *
 *****************************************************************************/
package org.adempiere.webui;

import java.util.List;

import org.adempiere.base.Service;
import org.adempiere.webui.component.WListItemRenderer;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.IEditorPopupMenuItem;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.factory.ICellComponentFactory;
import org.adempiere.webui.factory.IEditorPopupMenuItemFactory;
import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.util.WEditorPopupMenuItems;
import org.zkoss.zul.Listcell;

/**
 *
 * @author viola
 * @author hengsin
 * @author Silvano Trinchero, www.freepath.it
 * 			<li> IDEMPIERE-3276 New extension to add or replace context menu items on editor
 *
 */
public class Extensions {

	/**
	 *
	 * @param formId Java class name or equinox extension Id
	 * @return IFormController instance or null if formId not found
	 */
	public static ADForm getForm(String formId) {
		List<IFormFactory> factories = Service.locator().list(IFormFactory.class).getServices();
		if (factories != null) {
			for(IFormFactory factory : factories) {
				ADForm form = factory.newFormInstance(formId);
				if (form != null)
					return form;
			}
		}
		return null;
	}
	
	/** Get the additional menu items for the editor
	 * 
	 * @param editor
	 * @return the list of items
	 */
	public static WEditorPopupMenuItems getEditorPopupMenuItems(WEditor editor,boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation)
	{
		List<IEditorPopupMenuItemFactory> factories = Service.locator().list(IEditorPopupMenuItemFactory.class).getServices();
		WEditorPopupMenuItems items = new WEditorPopupMenuItems();
		
		if(factories != null)
		{
			for(IEditorPopupMenuItemFactory factory:factories)
			{
				List<IEditorPopupMenuItem> factoryItems = factory.getItems(editor, zoom, requery, preferences, newRecord, updateRecord, showLocation);
				if(factoryItems != null)
					items.addAll(factoryItems);
			}
		}

		return items;
	}
	
	/** Cell components creators
	 * 	IDEMPIERE-3318 - Factory for generating cell renderers
	 * 
	 * @param editor
	 * @return the list of items
	 */
	
	public static boolean createCellComponent(WListItemRenderer renderer, Listcell listcell, WListbox table, Object field,
			int rowIndex, int columnIndex,boolean isCellEditable, String identifier)	
	{
		List<ICellComponentFactory> factories = Service.locator().list(ICellComponentFactory.class).getServices();
		boolean managed = false;

		if (factories != null) {
			for(ICellComponentFactory factory : factories) 
			{
				managed = factory.createCellComponent(renderer, listcell, table, field, rowIndex, columnIndex, 
						isCellEditable, identifier);

				if(managed)
					break;
			}
		}
		
		return managed;
	}

}
