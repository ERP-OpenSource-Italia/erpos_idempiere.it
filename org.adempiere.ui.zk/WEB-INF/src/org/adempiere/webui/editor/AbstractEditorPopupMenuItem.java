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

import org.adempiere.webui.theme.ThemeManager;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zul.Menuitem;

/**
 * Abstract implementation IEditorPopupMenuItem, generalizing common behaviours
 * 
 * @author Silvano Trinchero, www.freepath.ti
 *
 */
public abstract class AbstractEditorPopupMenuItem implements IEditorPopupMenuItem {
 
	private String	label,
					imageUrl,
					event;
	
	/** Creates the item with the given event, label and image
	 * 
	 * @param event  event name
	 * @param adLabel label to used, @..@ will be translated
	 * @param imagePath image path, will be translated to url usin ThemeManager
	 */
	public AbstractEditorPopupMenuItem(String event,String adLabel,String imagePath)
	{
		this.event = event;
		imageUrl = ThemeManager.getThemeResource(imagePath);
		label = Msg.parseTranslation(Env.getCtx(), adLabel);
		label = Util.cleanAmp(label).intern();
	}
	
	@Override
	public String getEvent() {
		return event;
	}

	@Override
	public String getImageURL() {
		return imageUrl;
	}

	@Override
	public String getLabel() 
	{
		return label;
	}

	@Override
	public void onOpenItem(WEditor editor, Menuitem mi) {
		// Nothing to do
	}
}
