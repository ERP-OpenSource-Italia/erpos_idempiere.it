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
package org.adempiere.webui.editor;

import org.zkoss.zul.Menuitem;

/** Item to be displayed on popup menu of editors
 *  Items are shared, the MUST BE stateless. 
 * 
 * @author Silvano Trinchero, www.freepath.it
 *
 */
public interface IEditorPopupMenuItem {
	
	
	/** The event id string. Must be unique, avoid using a generic name.
	 *  If more then one event string matches (or matches a standard one), the last one takes precedence 
	 * 
	 * @return event id
	 */
	public String getEvent();
	
	/** URL of image to be displayed
	 * 
	 * @return the image url
	 */
	public String getImageURL();
	
	/** Get the item label
	 * 
	 * @return the item label with translation applied
	 */
	public String getLabel();
	
	/** Callback function invoked if the item is selected
	 *  @param the editor
	 */
	public void onEvent(WEditor editor);
	
	/** Callback function invoked when the menu is getting opened
	 *  @param the editor
	 */
	public void onOpenItem(WEditor editor, Menuitem mi);
}
