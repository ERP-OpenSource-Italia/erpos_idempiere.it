package org.adempiere.webui.factory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListItemRenderer;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.component.WTableColumn;
import org.adempiere.webui.component.ZkCssHelper;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MImage;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;

/**
 * 
 * Factory with default behaviors (copied from WListItemRendered)	
 * 
 * @author Monica Bean, www.freepath.it
 * @see  IDEMPIERE-3318 Factory for generating cell renderers https://idempiere.atlassian.net/browse/IDEMPIERE-3318
 *
 */
public class DefaultCellComponentFactory implements ICellComponentFactory
{

	@Override
	public boolean createCellComponent(WListItemRenderer wliRenderer, Listcell listcell, WListbox table,
			Object field, int rowIndex, int columnIndex,boolean isCellEditable, String identifier)
	{
		boolean isEmptyNullField = false;
		
		if(wliRenderer.getNoColumns() > 0)
		{
			WTableColumn col = wliRenderer.getColumn(columnIndex);
			
			String cssClass = col.getCssClass();
			
			if(cssClass != null)
				listcell.setClass(cssClass);
			
			isEmptyNullField = col.isEmptyNullValue();
		}
		
		if(isEmptyNullField && field == null)
		{
			listcell.setLabel("");
			listcell.setValue("");
		}
		else
		{
			if (field instanceof Boolean)
			{
				listcell.setValue(Boolean.valueOf(field.toString()));

				if (table != null && columnIndex == 0)
					table.setCheckmark(false);
				Checkbox checkbox = new Checkbox();
				checkbox.setChecked(Boolean.valueOf(field.toString()));

				if (isCellEditable)
				{
					checkbox.setEnabled(true);
					checkbox.addEventListener(Events.ON_CHECK, wliRenderer);
				}
				else
				{
					checkbox.setEnabled(false);
				}

				listcell.appendChild(checkbox);
				ZkCssHelper.appendStyle(listcell, "text-align:center");
			}
			else if (field instanceof Number)
			{
				List<WTableColumn> tableColumns = wliRenderer.getTableColumns();
				
				if (tableColumns != null && columnIndex < tableColumns.size()
						&& tableColumns.get(columnIndex).getColumnClass() != null
						&& tableColumns.get(columnIndex).getColumnClass().getName().equals(MImage.class.getName()) 
						&& field instanceof Integer)
				{
					MImage mImage = MImage.get(Env.getCtx(), (Integer) field);
					AImage img = null;
					byte[] data = mImage.getData();
					if (data != null && data.length > 0) {
						try {
							img = new AImage(null, data);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
					Image image = new Image();
					image.setContent(img);
					image.setStyle("width: 48px; height: 48px;");
					listcell.appendChild(image);
					listcell.setStyle("text-align: center;");
				}
				else
				{
					Language lang = AEnv.getLanguage(Env.getCtx());
					int displayType = (field instanceof BigDecimal || field instanceof Double || field instanceof Float)
							? DisplayType.Amount
						    : DisplayType.Integer;
					DecimalFormat format = DisplayType.getNumberFormat(displayType, lang);

					// set cell value to allow sorting
					listcell.setValue(field.toString());

					if (isCellEditable)
					{
						NumberBox numberbox = new NumberBox(false);
						numberbox.getDecimalbox().setFormat(format.toPattern());
						numberbox.getDecimalbox().setLocale(lang.getLocale());
						numberbox.setFormat(format);
						numberbox.setValue(field);
//						numberbox.setWidth("100px");
						numberbox.setEnabled(true);
						numberbox.setStyle("text-align:right; width: 96%;"
										+ listcell.getStyle());
						numberbox.addEventListener(Events.ON_CHANGE, wliRenderer);
						listcell.appendChild(numberbox);
					}
					else
					{
						listcell.setLabel(format.format(((Number)field).doubleValue()));
						ZkCssHelper.appendStyle(listcell, "text-align: right");
					}
				}
			}
			else if (field instanceof Timestamp)
			{

				SimpleDateFormat dateFormat = DisplayType.getDateFormat(DisplayType.Date, AEnv.getLanguage(Env.getCtx()));
				listcell.setValue(dateFormat.format((Timestamp)field));
				if (isCellEditable)
				{
					Datebox datebox = new Datebox();
					datebox.setValue(new Date(((Timestamp)field).getTime()));
					datebox.addEventListener(Events.ON_CHANGE, wliRenderer);
					listcell.appendChild(datebox);
				}
				else
				{
					listcell.setLabel(dateFormat.format((Timestamp)field));
					ZkCssHelper.appendStyle(listcell, "margin: auto");
				}
			}
			else if (field instanceof String)
			{
				List<WTableColumn> tableColumns = wliRenderer.getTableColumns();
				
				if (tableColumns != null && columnIndex < tableColumns.size()
						&& tableColumns.get(columnIndex).getColumnClass() != null
						&& tableColumns.get(columnIndex).getColumnClass().getName().equals(MImage.class.getName()))
				{
					try {
						URL url = new URL(field.toString());
						AImage aImage = new AImage(url);
						Image image = new Image();
						image.setContent(aImage);
						image.setStyle("width: 48px; height: 48px;");
						listcell.appendChild(image);
						listcell.setStyle("text-align: center;");
					} catch (MalformedURLException e) {
						throw new RuntimeException(e);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				else
				{
					listcell.setValue(field.toString());
					if (isCellEditable)
					{
						Textbox textbox = new Textbox();
						textbox.setValue(field.toString());
						textbox.addEventListener(Events.ON_CHANGE, wliRenderer);
						ZkCssHelper.appendStyle(textbox, "width: 96%;");
						listcell.appendChild(textbox);
					}
					else
					{
						listcell.setLabel(field.toString());
						listcell.setTooltiptext(field.toString()); // F3P: added hover tooltip
					}
				}
			}
			// if ID column make it invisible
			else if (field instanceof IDColumn)
			{
				listcell.setValue(((IDColumn) field).getRecord_ID());
				if (!table.isCheckmark() && columnIndex == 0) {
					table.setCheckmark(true);
					table.removeEventListener(Events.ON_SELECT, wliRenderer);
					table.addEventListener(Events.ON_SELECT, wliRenderer);
				}
				
				/* F3P: Rimosso, non sembra piu necessario. Lasciato come riferimento
				// F3P: Reflect selection status				
				if(((IDColumn)field).isSelected())
				{
					ListItem rowItem = table.getItemAtIndex(rowIndex);
					table.addItemToSelection(rowItem);
				}
				else
				{
					ListItem rowItem = table.getItemAtIndex(rowIndex);
					table.removeItemFromSelection(rowItem);					
				}
				*/
			}
			else
			{
				listcell.setLabel(field.toString());
				listcell.setValue(field.toString());
				listcell.setTooltiptext(field.toString()); // F3P: added hover tooltip
			}
		}
		
		return true;
	}

	@Override
	public Object getValueForCell(WListItemRenderer wliRenderer,WTableColumn column, int iRow, int iCol, Object source, String identifier)
	{
		Object value = null;
		
		if (source instanceof Checkbox)
		{
			value = Boolean.valueOf(((Checkbox)source).isChecked());
		}
		else if (source instanceof Decimalbox)
		{
			value = ((Decimalbox)source).getValue();
		}
		else if (source instanceof Datebox)
		{
			if (((Datebox)source).getValue() != null)
				value = new Timestamp(((Datebox)source).getValue().getTime());
		}
		else if (source instanceof Textbox)
		{
			value = ((Textbox)source).getValue();
		}
				
		return value;
	}
}
