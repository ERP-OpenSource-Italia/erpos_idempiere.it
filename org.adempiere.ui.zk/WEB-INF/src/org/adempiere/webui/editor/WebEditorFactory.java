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

package org.adempiere.webui.editor;

import java.util.List;
import java.util.Properties;

import org.adempiere.base.Service;
import org.adempiere.webui.factory.IEditorFactory;
import org.adempiere.webui.factory.IEditorFactory2;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Mar 12, 2007
 * @version $Revision: 0.10 $
 *
 * @author Low Heng Sin
 * @author strinchero, www.freepath.it: manage editor for info window
 * @date 	July 14 2008
 */
public class WebEditorFactory
{

    @SuppressWarnings("unused")
	private final static CLogger logger;

    static
    {
        logger = CLogger.getCLogger(WebEditorFactory.class);
    }
    
    public static WEditor getEditor(GridField gridField, boolean tableEditor,boolean searchWindow)
    {
    	return getEditor(gridField.getGridTab(), gridField, tableEditor, false, null, 0,searchWindow);
    }

    public static WEditor getEditor(GridField gridField, boolean tableEditor)
    {
    	return getEditor(gridField.getGridTab(), gridField, tableEditor, false, null, 0,false);
    }
    
    public static WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor)
    {
    	return getEditor(gridTab, gridField, tableEditor, false, null, 0,false);
    }
    
    public static WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor,boolean searchWindow)
    {
    	return getEditor(gridTab, gridField, tableEditor, false, null, 0,searchWindow);
    }
    
    public static WEditor getEditor(GridField gridField, boolean tableEditor, boolean infoEditor, Properties ctx, int WindowNo)
    {
    	return getEditor(gridField.getGridTab(), gridField, tableEditor, infoEditor, ctx, WindowNo,false);
    }
    
    public static WEditor getEditor(GridField gridField, boolean tableEditor, boolean infoEditor, Properties ctx, int WindowNo,boolean searchWindow)
    {
    	return getEditor(gridField.getGridTab(), gridField, tableEditor, infoEditor, ctx, WindowNo,false);
    }

    public static WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor, boolean infoEditor, Properties ctx, int WindowNo,boolean searchWindow)
    {
        WEditor editor = null;
        List<IEditorFactory> factoryList = Service.locator().list(IEditorFactory.class).getServices();
        for(IEditorFactory factory : factoryList)
        {
        	if(factory instanceof IEditorFactory2)
        	{
        		IEditorFactory2 factory2 = (IEditorFactory2)factory;
        		editor = factory2.getEditor(gridTab, gridField, tableEditor, infoEditor, WindowNo, ctx,searchWindow);
        	}
        	else
        		editor = factory.getEditor(gridTab, gridField, tableEditor,searchWindow);
        	
        	if (editor != null)
        		break;
        }
        return editor;
    }
}
