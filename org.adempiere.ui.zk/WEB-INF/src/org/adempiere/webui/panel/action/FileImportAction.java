/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2012 Trek Global                							  *
 * Copyright (C) 2012 Carlos Ruiz                							  *
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
package org.adempiere.webui.panel.action;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.adempiere.base.IGridTabImporter;
import org.adempiere.base.Service;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.adwindow.AbstractADWindowContent;
import org.adempiere.webui.adwindow.IADTabbox;
import org.adempiere.webui.adwindow.IADTabpanel;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.util.ReaderInputStream;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.GridTab;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Msg;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Vbox;

/**
 *
 * @author Carlos Ruiz
 *
 */
public class FileImportAction implements EventListener<Event>
{
	private AbstractADWindowContent panel;

	private Map<String, IGridTabImporter> importerMap = null;
	private Map<String, String> extensionMap = null;

	private Window winImportFile = null;
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private Listbox cboType = new Listbox();
	private Button bFile = new Button();
	private Listbox fCharset = new Listbox();
	private InputStream m_file_istream = null;
	
	/**
	 * @param panel
	 */
	public FileImportAction(AbstractADWindowContent panel)
	{
		this.panel = panel;
	}

	/**
	 * execute import action
	 */
	public void fileImport()
	{
		// charset
		Charset[] charsets = Ini.getAvailableCharsets();
		for (int i = 0; i < charsets.length; i++)
			fCharset.appendItem(charsets[i].displayName(), charsets[i]);
		Charset charset = Ini.getCharset();
		for (int i = 0; i < fCharset.getItemCount(); i++)
		{
			ListItem listitem = fCharset.getItemAtIndex(i);
			Charset compare = (Charset)listitem.getValue();
			
			if (charset == compare)
			{
				fCharset.setSelectedIndex(i);
				Executions.getCurrent().getDesktop().getWebApp().getConfiguration().setUploadCharset(compare.name());
				break;
			}
		}
		fCharset.addEventListener(Events.ON_SELECT, this);

		importerMap = new HashMap<String, IGridTabImporter>();
		extensionMap = new HashMap<String, String>();
		List<IGridTabImporter> importerList = Service.locator().list(IGridTabImporter.class).getServices();
		for(IGridTabImporter importer : importerList)
		{
			String extension = importer.getFileExtension();
			if (!extensionMap.containsKey(extension))
			{
				extensionMap.put(extension, importer.getFileExtensionLabel());
				importerMap.put(extension, importer);
			}
		}

		if (winImportFile == null)
		{
			winImportFile = new Window();
			winImportFile.setTitle(Msg.getMsg(Env.getCtx(), "FileImport") + ": " + panel.getActiveGridTab().getName());
			winImportFile.setWidth("450px");
			winImportFile.setClosable(true);
			winImportFile.setBorder("normal");
			winImportFile.setStyle("position:absolute");

			cboType.setMold("select");

			cboType.getItems().clear();
			for(Map.Entry<String, String> entry : extensionMap.entrySet())
			{
				cboType.appendItem(entry.getKey() + " - " + entry.getValue(), entry.getKey());
			}

			cboType.setSelectedIndex(0);

			Vbox vb = new Vbox();
			vb.setWidth("100%");
			winImportFile.appendChild(vb);

			Hbox hb = new Hbox();
			Div div = new Div();
			div.setStyle("text-align: right;");
			div.appendChild(new Label(Msg.getMsg(Env.getCtx(), "FilesOfType")));
			hb.appendChild(div);
			hb.appendChild(cboType);
			cboType.setWidth("100%");
			vb.appendChild(hb);

			hb = new Hbox();
			fCharset.setMold("select");
			fCharset.setRows(0);
			fCharset.setTooltiptext(Msg.getMsg(Env.getCtx(), "Charset", false));
			hb.appendChild(fCharset);
			vb.appendChild(hb);
			
			hb = new Hbox();
			bFile.setLabel(Msg.getMsg(Env.getCtx(), "FileImportFile"));
			bFile.setTooltiptext(Msg.getMsg(Env.getCtx(), "FileImportFileInfo"));
			bFile.setUpload("true");
			bFile.addEventListener(Events.ON_UPLOAD, this);
			hb.appendChild(bFile);
			vb.appendChild(hb);

			vb.appendChild(confirmPanel);
			confirmPanel.addActionListener(this);
		}

		Clients.showBusy(panel.getComponent().getParent(), " ");
		panel.getComponent().getParent().appendChild(winImportFile);
		LayoutUtils.openOverlappedWindow(panel.getComponent(), winImportFile, "middle_center");
		winImportFile.addEventListener(DialogEvents.ON_WINDOW_CLOSE, this);
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event instanceof UploadEvent) 
		{
			UploadEvent ue = (UploadEvent) event;
			processUploadMedia(ue.getMedia());
		} else if (event.getTarget().getId().equals(ConfirmPanel.A_CANCEL)) {
			winImportFile.onClose();
		} else if (event.getTarget() == fCharset) {
			if (m_file_istream != null) {
				m_file_istream.close();
				m_file_istream = null;
			}
			ListItem listitem = fCharset.getSelectedItem();
			if (listitem == null)
				return;
			Charset charset = (Charset)listitem.getValue();
			Executions.getCurrent().getDesktop().getWebApp().getConfiguration().setUploadCharset(charset.name());
			bFile.setLabel(Msg.getMsg(Env.getCtx(), "FileImportFile"));
		} else if (event.getTarget().getId().equals(ConfirmPanel.A_OK)) {
			if (m_file_istream == null || fCharset.getSelectedItem() == null)
				return;
			importFile();
		} else if (event.getName().equals(DialogEvents.ON_WINDOW_CLOSE)) {
			Clients.clearBusy(panel.getComponent().getParent());
			panel.getComponent().invalidate();
		}
	}

	private void processUploadMedia(Media media) {
		if (media == null)
			return;

		if (media.isBinary()) {
			m_file_istream = media.getStreamData();
		}
		else {
			ListItem listitem = fCharset.getSelectedItem();
			if (listitem == null) {
				m_file_istream = new ReaderInputStream(media.getReaderData());
			} else {
				Charset charset = (Charset)listitem.getValue();
				m_file_istream = new ReaderInputStream(media.getReaderData(), charset.name());
			}
		}
		
		bFile.setLabel(media.getName());
	}
	
	private void importFile() {
		try {
			ListItem li = cboType.getSelectedItem();
			if(li == null || li.getValue() == null)
			{
				FDialog.error(0, winImportFile, "FileInvalidExtension");
				return;
			}

			String ext = li.getValue().toString();
			IGridTabImporter importer = importerMap.get(ext);
			if (importer == null)
			{
				FDialog.error(0, winImportFile, "FileInvalidExtension");
				return;
			}

			IADTabbox adTab = panel.getADTab();
			int selected = adTab.getSelectedIndex();
			int tabLevel = panel.getActiveGridTab().getTabLevel();
			Set<String> tables = new HashSet<String>();
			List<GridTab> childs = new ArrayList<GridTab>();
			List<GridTab> includedList = panel.getActiveGridTab().getIncludedTabs();
			for(GridTab included : includedList)
			{
				String tableName = included.getTableName();
				if (tables.contains(tableName))
					continue;
				tables.add(tableName);
				childs.add(included);
			}
			for(int i = selected+1; i < adTab.getTabCount(); i++)
			{
				IADTabpanel adTabPanel = adTab.getADTabpanel(i);
				if (adTabPanel.getGridTab().isSortTab())
					continue;
				if (adTabPanel.getGridTab().getTabLevel() <= tabLevel)
					break;
				String tableName = adTabPanel.getGridTab().getTableName();
				if (tables.contains(tableName))
					continue;
				tables.add(tableName);
				childs.add(adTabPanel.getGridTab());
			}

			ListItem listitem = fCharset.getSelectedItem();
			Charset charset = null;
			if (listitem == null)
				return;
			charset = (Charset)listitem.getValue();

			File outFile = importer.fileImport(panel.getActiveGridTab(), childs, m_file_istream, charset);
			winImportFile.onClose();
			winImportFile = null;

			AMedia media = null;
			media = new AMedia(importer.getSuggestedFileName(panel.getActiveGridTab()), null, importer.getContentType(), outFile, true);
			Filedownload.save(media, panel.getActiveGridTab().getName() + "." + ext);

		} catch (Exception e) {
			throw new AdempiereException(e);
		} finally {
			if (winImportFile != null)
				winImportFile.onClose();
		}
	}
}
