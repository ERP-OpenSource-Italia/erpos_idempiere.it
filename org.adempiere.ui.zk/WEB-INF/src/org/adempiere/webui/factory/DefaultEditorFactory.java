/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2010 Heng Sin Low                							  *
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

import java.util.Properties;

import org.adempiere.webui.editor.WAccountEditor;
import org.adempiere.webui.editor.WAssignmentEditor;
import org.adempiere.webui.editor.WBinaryEditor;
import org.adempiere.webui.editor.WButtonEditor;
import org.adempiere.webui.editor.WChartEditor;
import org.adempiere.webui.editor.WChosenboxListEditor;
import org.adempiere.webui.editor.WDashboardContentEditor;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WDatetimeEditor;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WFileDirectoryEditor;
import org.adempiere.webui.editor.WFilenameEditor;
import org.adempiere.webui.editor.WHtmlEditor;
import org.adempiere.webui.editor.WImageEditor;
import org.adempiere.webui.editor.WLocationEditor;
import org.adempiere.webui.editor.WLocatorEditor;
import org.adempiere.webui.editor.WNumberEditor;
import org.adempiere.webui.editor.WPAttributeEditor;
import org.adempiere.webui.editor.WPasswordEditor;
import org.adempiere.webui.editor.WPaymentEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WStringEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.editor.WTimeEditor;
import org.adempiere.webui.editor.WUnknownEditor;
import org.adempiere.webui.editor.WUrlEditor;
import org.adempiere.webui.editor.WYesNoEditor;
import org.adempiere.webui.editor.grid.selection.WGridTabMultiSelectionEditor;
import org.adempiere.webui.editor.grid.selection.WGridTabSingleSelectionEditor;
import org.adempiere.webui.info.WInfoPAttributeEditor;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DisplayType;

/**
 *
 * @author hengsin
 * @author strinchero, www.freepath.it: manage editor for info window
 *
 */
public class DefaultEditorFactory implements IEditorFactory2 {
	
	@Override
	public WEditor getEditor(GridTab gridTab, GridField gridField,
			boolean tableEditor) {
		return getEditor(gridTab, gridField, tableEditor, false, 0, null);
	}

	@Override
	public WEditor getEditor(GridTab gridTab, GridField gridField,
			boolean tableEditor, boolean infoEditor, int WindowNo, Properties ctx) {
		if (gridField == null)
        {
            return null;
        }

        WEditor editor = null;
        int displayType = gridField.getDisplayType();

        /** Not a Field */
        if (gridField.isHeading())
        {
            return null;
        }

        /** String (clear/password) */
        if (displayType == DisplayType.String
            || displayType == DisplayType.PrinterName || displayType == DisplayType.Color
            || (tableEditor && (displayType == DisplayType.Text || displayType == DisplayType.TextLong)))
        {
            if (gridField.isEncryptedField())
            {
                editor = new WPasswordEditor(gridField, tableEditor);
            }
            else
            {
            	if (gridField.isHtml())
            		editor = new WHtmlEditor(gridField);
            	else
            		editor = new WStringEditor(gridField, tableEditor);
            }
            //enable html5 color input type
            if (displayType == DisplayType.Color)
            	((WStringEditor)editor).getComponent().setClientAttribute("type", "color");
        }
        /** File */
        else if (displayType == DisplayType.FileName)
        {
        	editor = new WFilenameEditor(gridField);
        }
        /** File Path */
        else if (displayType == DisplayType.FilePath)
        {
        	editor = new WFileDirectoryEditor(gridField);
        }
        /** Number */
        else if (DisplayType.isNumeric(displayType))
        {
            editor = new WNumberEditor(tableEditor, gridField);
        }

        /** YesNo */
        else if (displayType == DisplayType.YesNo)
        {
            editor = new WYesNoEditor(gridField);
            if (tableEditor)
            	((WYesNoEditor)editor).getComponent().setLabel("");
        }

        /** Text */
        else if (displayType == DisplayType.Text || displayType == DisplayType.Memo || displayType == DisplayType.TextLong || displayType == DisplayType.ID)
        {
        	if (gridField.isHtml())
        		editor = new WHtmlEditor(gridField);
        	else
        		editor = new WStringEditor(gridField, tableEditor);
        }

        /** Date */
        else if (DisplayType.isDate(displayType))
        {
        	if (displayType == DisplayType.Time)
        		editor = new WTimeEditor(gridField);
        	else if (displayType == DisplayType.DateTime)
        		editor = new WDatetimeEditor(gridField);
        	else
        		editor = new WDateEditor(gridField);
        }
        
        /** Chart */
        else if(displayType == DisplayType.Chart)
        {
        	editor = new WChartEditor(gridField, (gridTab == null ? 0 : gridTab.getWindowNo()));
        }

        /** Dashboard Content */
        else if(displayType == DisplayType.DashboardContent)
        {
        	editor = new WDashboardContentEditor(gridField, (gridTab == null ? 0 : gridTab.getWindowNo()));
        }
        
        /**  Button */
        else if (displayType == DisplayType.Button)
        {
            editor = new WButtonEditor(gridField);
        }

        /** Table Direct */
        else if (displayType == DisplayType.TableDir ||
                displayType == DisplayType.Table || displayType == DisplayType.List)
        {
            editor = new WTableDirEditor(gridField);
        }
        
        else if (displayType == DisplayType.Payment)
        {
        	editor = new WPaymentEditor(gridField);
        }

        else if (displayType == DisplayType.URL)
        {
        	editor = new WUrlEditor(gridField);
        }

        else if (displayType == DisplayType.Search)
        {
        	editor = new WSearchEditor(gridField);
        }

        else if (displayType == DisplayType.Location)
        {
            editor = new WLocationEditor(gridField);
        }
        else if (displayType == DisplayType.Locator)
        {
        	editor = new WLocatorEditor(gridField);
        }
        else if (displayType == DisplayType.Account)
        {
        	editor = new WAccountEditor(gridField);
        }
        else if (displayType == DisplayType.Image)
        {
        	editor = new WImageEditor(gridField);
        }
        else if (displayType == DisplayType.Binary)
        {
        	editor = new WBinaryEditor(gridField);
        }
        else if (displayType == DisplayType.PAttribute)
        {
        	if(infoEditor && ctx != null)
        		editor = new WInfoPAttributeEditor(ctx, WindowNo, gridField);
        	else
        		editor = new WPAttributeEditor(gridTab, gridField);
        }
        else if (displayType == DisplayType.Assignment)
        {
        	editor = new WAssignmentEditor(gridField);
        }
        else if (displayType == DisplayType.SingleSelectionGrid)
        {
        	editor = new WGridTabSingleSelectionEditor(gridField, tableEditor);
        }
        else if (displayType == DisplayType.MultipleSelectionGrid)
        {
        	editor = new WGridTabMultiSelectionEditor(gridField, tableEditor);
        }
        else if (displayType == DisplayType.ChosenMultipleSelectionList )
        {
        	editor = new WChosenboxListEditor(gridField, tableEditor);
        }
        else
        {
            editor = new WUnknownEditor(gridField);
        }

        editor.setTableEditor(tableEditor);
        
        return editor;
	}

}
