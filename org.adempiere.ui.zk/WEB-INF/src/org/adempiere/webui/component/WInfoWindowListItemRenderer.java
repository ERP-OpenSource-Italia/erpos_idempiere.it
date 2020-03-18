/**********************************************************************
* This file is part of iDempiere ERP Open Source                      *
* http://www.idempiere.org                                            *
*                                                                     *
* Copyright (C) Contributors                                          *
*                                                                     *
* This program is free software; you can redistribute it and/or       *
* modify it under the terms of the GNU General Public License         *
* as published by the Free Software Foundation; either version 2      *
* of the License, or (at your option) any later version.              *
*                                                                     *
* This program is distributed in the hope that it will be useful,     *
* but WITHOUT ANY WARRANTY; without even the implied warranty of      *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
* GNU General Public License for more details.                        *
*                                                                     *
* You should have received a copy of the GNU General Public License   *
* along with this program; if not, write to the Free Software         *
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
* MA 02110-1301, USA.                                                 *
**********************************************************************/
package org.adempiere.webui.component;

import java.util.List;
import java.util.Properties;

import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WebEditorFactory;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.info.InfoWindow;
import org.adempiere.webui.theme.ThemeManager;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.GridField;
import org.compiere.model.MInfoColumn;
import org.compiere.model.MStyle;
import org.compiere.model.X_AD_StyleLine;
import org.compiere.util.Env;
import org.compiere.util.Evaluatee;
import org.compiere.util.Evaluator;
import org.compiere.util.NamePair;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import it.idempiere.base.model.LITMInfoColumn;

public class WInfoWindowListItemRenderer extends WListItemRenderer
{
	private MInfoColumn[]	gridDisplayedInfoColumns = null;
	private ColumnInfo[]	gridDisplayedColumnInfos = null;
	private InfoWindow infoWindow = null;	
	private RowEvaluetee rowEvaluatee = new RowEvaluetee();

	public WInfoWindowListItemRenderer(InfoWindow infoWindow)
	{
		this.infoWindow = infoWindow;
	}

	public WInfoWindowListItemRenderer(InfoWindow infoWindow, List<? extends String> columnNames)
	{
		this(infoWindow, columnNames, null);
	}

	public WInfoWindowListItemRenderer(InfoWindow infoWindow, List<? extends String> columnNames,
			String identifier)
	{
		super(columnNames, identifier);
		this.infoWindow = infoWindow;
	}
	
	public void setGridDisplayedInfoColumns(MInfoColumn[] infoColumns, ColumnInfo[] columnInfos)
	{
		this.gridDisplayedInfoColumns = infoColumns;
		this.gridDisplayedColumnInfos = columnInfos;
	}
		
	@Override
	public void render(Listitem item, Object data, int index) throws Exception {
		rowEvaluatee.resetRow();
		super.render(item, data, index);
	}

	@Override
	protected Listcell getCellComponent(WListbox table, Object field,
			final int rowIndex, final int columnIndex)
	{		
		if(gridDisplayedInfoColumns == null || gridDisplayedColumnInfos == null)
		{
			return super.getCellComponent(table, field, rowIndex, columnIndex);
		}
		
		Listcell listcell = null;
		ListModelTable model = table.getModel();
		Object obj = model.get(rowIndex);
		
		MInfoColumn infoColumn = gridDisplayedInfoColumns[columnIndex];
		
		if(model.isSelected(obj) && infoColumn != null) // First index may be null
		{
			if(infoColumn.isReadOnly() == false 
					&& columnIndex > 0)
			{
				ListCell listCell = new ListCell();

				// Propagate style class

				WTableColumn col = getColumn(columnIndex);

				String cssClass = col.getCssClass();

				if(cssClass != null)
					listCell.setClass(cssClass);

				final GridField gridField = gridDisplayedColumnInfos[columnIndex].getGridField();
				final WEditor editor = WebEditorFactory.getEditor(gridField, false);
				
				// Set editor value
				
				Object value = table.getValueAt(rowIndex, columnIndex);
				
				if(value instanceof IDColumn)
				{
					IDColumn idc = (IDColumn)value;
					value = idc.getRecord_ID();
				}
				else if(value instanceof NamePair)
				{
					NamePair knp = (NamePair)value;
					value = knp.getID();
				}
				
				editor.setValue(value);
				
				if(infoWindow != null)
				{
					editor.addValueChangeListener(new ValueChangeListener()
					{					
						@Override
						public void valueChange(ValueChangeEvent evt)
						{
							infoWindow.onCellEditCallback(evt, rowIndex, columnIndex, editor, gridField);
						}
					});
				}
				
				listCell.appendChild(editor.getComponent());
				listcell = listCell;
			}
		}
		
		if(listcell == null)
			listcell = super.getCellComponent(table, field, rowIndex, columnIndex);
		
		if(listcell != null && infoColumn != null && LITMInfoColumn.getAD_FieldStyle_ID(infoColumn) > 0)
		{
			Component component = listcell.getFirstChild();
			if(component == null)
				component = listcell;			
			
			if(component instanceof HtmlBasedComponent)
			{
				int AD_Style_ID = LITMInfoColumn.getAD_FieldStyle_ID(infoColumn);
				
				rowEvaluatee.setRow(model, rowIndex);
				
				applyFieldStyle(rowIndex, (HtmlBasedComponent)component, AD_Style_ID, rowEvaluatee);
			}
		}
		
		return listcell;
	}
	
	protected void applyFieldStyle(int rowIndex,
			HtmlBasedComponent component, int AD_Style_ID, Evaluatee rowEval) 
	{		
		MStyle style = MStyle.get(Env.getCtx(), AD_Style_ID);
		X_AD_StyleLine[] lines = style.getStyleLines();
		StringBuilder styleBuilder = new StringBuilder();
		
		for (X_AD_StyleLine line : lines) 
		{
			String inlineStyle = line.getInlineStyle().trim();
			String displayLogic = line.getDisplayLogic();
			String theme = line.getTheme();
			
			if (!Util.isEmpty(theme)) {
				if (!ThemeManager.getTheme().equals(theme))
					continue;
			}
			
			if (!Util.isEmpty(displayLogic))
			{
				if (!Evaluator.evaluateLogic(rowEval, displayLogic)) 
					continue;
			}
			
			if (styleBuilder.length() > 0 && !(styleBuilder.charAt(styleBuilder.length()-1)==';'))
				styleBuilder.append("; ");
			styleBuilder.append(inlineStyle);
			
		}
		
		component.setStyle(styleBuilder.toString());
	}
	
	/** Evaluator based on model content
	 *  NOT thread safe.
	 * 
	 * @author strinchero
	 *
	 */
	protected class RowEvaluetee implements Evaluatee
	{
		private int row = -1;
		private Properties ctx = new Properties(Env.getCtx());
		
		public void resetRow()
		{
			row = -1;
		}
		
		public void setRow(ListModelTable table, int newRow)
		{
			if(newRow != row)
			{
				row = newRow;
				
				for(int i=0; i<gridDisplayedInfoColumns.length; i++)
				{
					if(gridDisplayedInfoColumns[i] == null)
						continue;
					
					Object value = table.getValueAt(row, i);
					
					if(value instanceof IDColumn)
					{
						IDColumn idc = (IDColumn)value;
						value = idc.getRecord_ID();
					}
					else if(value instanceof NamePair)
					{
						NamePair knp = (NamePair)value;
						value = knp.getID();
					}
					
					if(value == null)
						value = "";
					
					Env.setContext(ctx, gridDisplayedInfoColumns[i].getColumnName(), value.toString());
				}
			}
		}
		
		@Override
		public String get_ValueAsString(String variableName) 
		{
			return Env.getContext(ctx, variableName);
		}
	}

}
