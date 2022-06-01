package org.adempiere.webui.factory;

import java.util.Properties;

import org.adempiere.webui.editor.WEditor;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

/**
 * @author strinchero, www.freepath: manage editor for info window
 *
 */
public interface IEditorFactory2 extends IEditorFactory
{
	/**
	 * @param gridTab
	 * @param gridField
	 * @param tableEditor
	 * @param infoEditor	editor is used by info window
	 * @param WindowNo	WindiowNo
	 * @param ctx	context (mandatory for editor for info window)
	 * @return WEditor
	 */
	public WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor, boolean infoEditor, int WindowNo, Properties ctx,boolean searchWindow);

}
