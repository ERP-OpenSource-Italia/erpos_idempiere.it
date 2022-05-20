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

package org.adempiere.webui.factory;

import java.util.List;

import org.adempiere.webui.editor.IEditorPopupMenuItem;
import org.adempiere.webui.editor.WEditor;

/** Factory for editor popup menu items 
 * 
 * @author Silvano Trinchero, www.freepath.it
 *
 */
public interface IEditorPopupMenuItemFactory {
	
	/** Get all the items this plugin provides for the specified editor
	 * 
	 * @param editor
	 * @return list of items, or null
	 */
	List<IEditorPopupMenuItem> getItems(WEditor editor,boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation);
	
}
