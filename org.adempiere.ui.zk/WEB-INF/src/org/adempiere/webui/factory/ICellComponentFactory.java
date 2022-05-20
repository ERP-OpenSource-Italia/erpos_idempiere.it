package org.adempiere.webui.factory;

import org.adempiere.webui.component.WListItemRenderer;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.component.WTableColumn;
import org.zkoss.zul.Listcell;

/**
 * Factory class to customize cell component
 * @author Monica Bean, www.freepath.it
 * @see  IDEMPIERE-3318 Factory for generating cell renderers https://idempiere.atlassian.net/browse/IDEMPIERE-3318
 *
 */
public interface ICellComponentFactory
{
	public boolean createCellComponent(WListItemRenderer wliRenderer,Listcell listCell,WListbox table, Object field,
								  										int rowIndex, int columnIndex,boolean isCellEditable, String identifier);
	
	public Object getValueForCell(WListItemRenderer wliRenderer,WTableColumn column, int iRow,int iCol,Object valueSource, String identifier);
}
