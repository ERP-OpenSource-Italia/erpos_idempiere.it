/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2007 Adempiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *
 * Copyright (C) 2007 Low Heng Sin hengsin@avantz.com
 * _____________________________________________
 *****************************************************************************/
package org.adempiere.webui.window;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import javax.activation.FileDataSource;
import javax.servlet.http.HttpServletRequest;

import org.adempiere.exceptions.DBException;
import org.adempiere.pdf.Document;
import org.adempiere.util.ContextRunnable;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.apps.BusyDialog;
import org.adempiere.webui.apps.DesktopRunnable;
import org.adempiere.webui.apps.WReport;
import org.adempiere.webui.apps.form.WReportCustomization;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Mask;
import org.adempiere.webui.component.Tabpanel;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.event.DrillEvent;
import org.adempiere.webui.event.ZoomEvent;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.ITabOnCloseHandler;
import org.adempiere.webui.panel.StatusBarPanel;
import org.adempiere.webui.report.HTMLExtension;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.IServerPushCallback;
import org.adempiere.webui.util.ServerPushTemplate;
import org.compiere.Adempiere;
import org.compiere.model.GridField;
import org.compiere.model.MArchive;
import org.compiere.model.MClient;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
import org.compiere.model.MToolBarButtonRestrict;
import org.compiere.model.MUser;
import org.compiere.model.SystemIDs;
import org.compiere.model.X_AD_ToolBarButton;
import org.compiere.print.ArchiveEngine;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.tools.FileUtil;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.render.DynamicMedia;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.impl.Utils;
import org.zkoss.zul.impl.XulElement;


/**
 *	Print View Frame
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Viewer.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 * globalqss: integrate phib contribution from 
 *   http://sourceforge.net/tracker/index.php?func=detail&aid=1566335&group_id=176962&atid=879334
 * globalqss: integrate Teo Sarca bug fixing
 * Colin Rooney 2007/03/20 RFE#1670185 & BUG#1684142
 *                         Extend security to Info queries
 *
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 				<li>FR [ 1762466 ] Add "Window" menu to report viewer.
 * 				<li>FR [ 1894640 ] Report Engine: Excel Export support
 * 
 * @author Low Heng Sin
 */
public class ZkReportViewer extends Window implements EventListener<Event>, ITabOnCloseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3463776496724974142L;

	/** Window No					*/
	private int                 m_WindowNo = -1;
	/**	Print Context				*/
	private Properties			m_ctx;
	/**	Setting Values				*/
	private boolean				m_setting = false;
	/**	Report Engine				*/
	private ReportEngine 		m_reportEngine;
	/** Table ID					*/
	private int					m_AD_Table_ID = 0;
	private boolean				m_isCanExport;
	
	private MQuery 		m_ddQ = null;
	private MQuery 		m_daQ = null;
	private Menuitem 	m_ddM = null;
	private Menuitem 	m_daM = null;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ZkReportViewer.class);

	//
	private StatusBarPanel statusBar = new StatusBarPanel();
	private Toolbar toolBar = new Toolbar();
	private ToolBarButton bSendMail = new ToolBarButton();
	private ToolBarButton bArchive = new ToolBarButton();
	private ToolBarButton bCustomize = new ToolBarButton();
	private ToolBarButton bFind = new ToolBarButton();
	private ToolBarButton bExport = new ToolBarButton();
	private ToolBarButton bWizard = new ToolBarButton();
	private Listbox comboReport = new Listbox();
	private Label labelDrill = new Label();
	private Listbox comboDrill = new Listbox();
	private Listbox previewType = new Listbox();
	
	private ToolBarButton bRefresh = new ToolBarButton();
	private Iframe iframe;
	
	private Window winExportFile = null;
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private Listbox cboType = new Listbox();
	private Checkbox summary = new Checkbox();

	private AMedia media;
	private int mediaVersion = 0;

	private A reportLink;

	private boolean init;
	
	private BusyDialog progressWindow;
	private Mask mask;

	private Future<?> future;
	
	private final static String ON_RENDER_REPORT_EVENT = "onRenderReport";
	
	//private static final String REPORT = "org.idempiere.ui.report";
	
	/**
	 * 	Static Layout
	 * 	@throws Exception
	 */
	public ZkReportViewer(ReportEngine re, String title) {		
		super();
		
		init = false;
		m_WindowNo = SessionManager.getAppDesktop().registerWindow(this);
		Env.setContext(re.getCtx(), m_WindowNo, "_WinInfo_IsReportViewer", "Y");
		m_reportEngine = re;
		m_AD_Table_ID = re.getPrintFormat().getAD_Table_ID();
		if (!MRole.getDefault().isCanReport(m_AD_Table_ID))
		{
			FDialog.error(m_WindowNo, this, "AccessCannotReport", m_reportEngine.getName());
			this.onClose();
		}
		m_isCanExport = MRole.getDefault().isCanExport(m_AD_Table_ID);
		
		setTitle(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Report") + ": " + m_reportEngine.getName()));
		
		addEventListener(ON_RENDER_REPORT_EVENT, this);
	}
	
	

	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		if (newpage != null && !init) {
			try
			{
				m_ctx = m_reportEngine.getCtx();
				init();
				dynInit();
				
				if (!ArchiveEngine.isValid(m_reportEngine.getLayout()))
					log.warning("Cannot archive Document");
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE, "", e);
				FDialog.error(m_WindowNo, this, "LoadError", e.getLocalizedMessage());
				this.onClose();
			}
		}
	}

	private void init() {
		Borderlayout layout = new Borderlayout();
		layout.setStyle("position: absolute; height: 97%; width: 98%; border:none; padding:none; margin:none;");
		this.appendChild(layout);
		this.setStyle("width: 100%; height: 100%; position: absolute; border:none; padding:none; margin:none;");

		toolBar.setHeight("32px");
		toolBar.setWidth("100%");
		
		previewType.setMold("select");
		previewType.appendItem("HTML", "HTML");
		
		if ( m_isCanExport )
		{
			previewType.appendItem("PDF", "PDF");
			previewType.appendItem("Excel", "XLS");
		}
		
		toolBar.appendChild(previewType);		
		previewType.addEventListener(Events.ON_SELECT, this);
		toolBar.appendChild(new Separator("vertical"));
		
		//set default type
		String type = m_reportEngine.getPrintFormat().isForm()
				// a42niem - provide explicit default and check on client/org specifics
				? MSysConfig.getValue(MSysConfig.ZK_REPORT_FORM_OUTPUT_TYPE,"PDF",Env.getAD_Client_ID(m_ctx),Env.getAD_Org_ID(m_ctx))
				: MSysConfig.getValue(MSysConfig.ZK_REPORT_TABLE_OUTPUT_TYPE,"PDF",Env.getAD_Client_ID(m_ctx),Env.getAD_Org_ID(m_ctx));

		if ("HTML".equals(type)) {
			previewType.setSelectedIndex(0);
		} else if ("PDF".equals(type) && m_isCanExport) {
			previewType.setSelectedIndex(1);
		} else if ("XLS".equals(type) && m_isCanExport) {
			previewType.setSelectedIndex(2);
		} else {
			// XXX - provide hint if unexpected value
			previewType.setSelectedIndex(0); //fall back to HTML
		}
		
		labelDrill.setValue(Msg.getMsg(Env.getCtx(), "Drill") + ": ");
		toolBar.appendChild(labelDrill);
		
		comboDrill.setMold("select");
		comboDrill.setTooltiptext(Msg.getMsg(Env.getCtx(), "Drill"));
		toolBar.appendChild(comboDrill);
		
		toolBar.appendChild(new Separator("vertical"));
		
		comboReport.setMold("select");
		comboReport.setTooltiptext(Msg.translate(Env.getCtx(), "AD_PrintFormat_ID"));
		toolBar.appendChild(comboReport);
		
		toolBar.appendChild(new Separator("vertical"));
		
		summary.setText(Msg.getMsg(Env.getCtx(), "Summary"));
		toolBar.appendChild(summary);
		
		toolBar.appendChild(new Separator("vertical"));
		
		bCustomize.setName("Customize");
		bCustomize.setImage(ThemeManager.getThemeResource("images/Preference24.png"));
		bCustomize.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "PrintCustomize")));
		toolBar.appendChild(bCustomize);
		bCustomize.addEventListener(Events.ON_CLICK, this);
		
		
		
		bFind.setName("Find");
		bFind.setImage(ThemeManager.getThemeResource("images/Find24.png"));
		bFind.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Find")));
		toolBar.appendChild(bFind);
		bFind.addEventListener(Events.ON_CLICK, this);
		if (getAD_Tab_ID(m_reportEngine.getPrintFormat().getAD_Table_ID()) <= 0) {
			bFind.setVisible(false); // IDEMPIERE-1185
		}
		
		toolBar.appendChild(new Separator("vertical"));
		
		bSendMail.setName("SendMail");
		bSendMail.setImage(ThemeManager.getThemeResource("images/SendMail24.png"));
		bSendMail.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "SendMail")));
		toolBar.appendChild(bSendMail);
		bSendMail.addEventListener(Events.ON_CLICK, this);
		
		bArchive.setName("Archive");
		bArchive.setImage(ThemeManager.getThemeResource("images/Archive24.png"));
		bArchive.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Archive")));
		toolBar.appendChild(bArchive);
		bArchive.addEventListener(Events.ON_CLICK, this);
		
		if ( m_isCanExport )
		{
			bExport.setName("Export");
			bExport.setImage(ThemeManager.getThemeResource("images/Export24.png"));
			bExport.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Export")));
			toolBar.appendChild(bExport);
			bExport.addEventListener(Events.ON_CLICK, this);
		}
		
		toolBar.appendChild(new Separator("vertical"));
		
		bRefresh.setName("Refresh");
		bRefresh.setImage(ThemeManager.getThemeResource("images/Refresh24.png"));
		bRefresh.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")));
		toolBar.appendChild(bRefresh);
		bRefresh.addEventListener(Events.ON_CLICK, this);

		bWizard.setImage(ThemeManager.getThemeResource("images/Wizard24.png"));
		bWizard.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "PrintWizard")));
		toolBar.appendChild(bWizard);
		bWizard.addEventListener(Events.ON_CLICK, this);

		North north = new North();
		layout.appendChild(north);
		north.appendChild(toolBar);
		north.setVflex("min");
		
		Center center = new Center();
		layout.appendChild(center);
		iframe = new Iframe();
		//iframe.setHflex("true");
		//iframe.setVflex("true");
		iframe.setWidth("100%");
		iframe.setHeight("100%");
		iframe.setId("reportFrame");
		center.appendChild(iframe);
		
		South south = new South();
		south.setHeight("34px");
		layout.appendChild(south);
		reportLink = new A();
		reportLink.setTarget("_blank");
		Div linkDiv = new Div();
		linkDiv.setStyle("width:100%; height: 30px; padding-top: 2px;");
		linkDiv.appendChild(reportLink);
		south.appendChild(linkDiv);
		//m_WindowNo
		int AD_Window_ID = Env.getContextAsInt(Env.getCtx(), m_reportEngine.getWindowNo(), "_WinInfo_AD_Window_ID", true);
		if (AD_Window_ID == 0)
			AD_Window_ID = Env.getZoomWindowID(m_reportEngine.getQuery());
		int AD_Process_ID = m_reportEngine.getPrintInfo() != null ? m_reportEngine.getPrintInfo().getAD_Process_ID() : 0;
		updateToolbarAccess(AD_Window_ID, AD_Process_ID);
		
		postRenderReportEvent();
				
		iframe.setAutohide(true);

		this.setBorder("normal");
		
		this.addEventListener("onZoom", new EventListener<Event>() {
			
			public void onEvent(Event event) throws Exception {
				if (event instanceof ZoomEvent) {
					Clients.clearBusy();
					ZoomEvent ze = (ZoomEvent) event;
					if (ze.getData() != null && ze.getData() instanceof MQuery) {
						AEnv.zoom((MQuery) ze.getData());
					}
				}
				
			}
		});
		
		this.addEventListener(DrillEvent.ON_DRILL_ACROSS, new EventListener<Event>() {
			
			public void onEvent(Event event) throws Exception {
				if (event instanceof DrillEvent) {
					Clients.clearBusy();
					DrillEvent de = (DrillEvent) event;
					if (de.getData() != null && de.getData() instanceof MQuery) {
						MQuery query = (MQuery) de.getData();
						Listitem item = comboDrill.getSelectedItem();
						if (item != null && item.getValue() != null && item.toString().trim().length() > 0)
						{
							query.setTableName(item.getValue().toString());
							executeDrill(query, event.getTarget());
						}
					}
				}
				
			}
		});
		
		this.addEventListener(DrillEvent.ON_DRILL_DOWN, new EventListener<Event>() {
			
			public void onEvent(Event event) throws Exception {
				if (event instanceof DrillEvent) {
					Clients.clearBusy();
					DrillEvent de = (DrillEvent) event;
					if (de.getData() != null && de.getData() instanceof MQuery) {
						MQuery query = (MQuery) de.getData();
						executeDrill(query, event.getTarget());
					}
				}
				
			}
		});
		
		init = true;
	}

	/**
	 * Get the maintenance tab of the table associated to the report engine
	 * @return AD_Tab_ID or -1 if not found
	 */
	private int getAD_Tab_ID(int AD_Table_ID) {
		// Get Find Tab Info
		final String sql = "SELECT t.AD_Tab_ID "
				+ "FROM AD_Tab t"
				+ " INNER JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)"
				+ " INNER JOIN AD_Table tt ON (t.AD_Table_ID=tt.AD_Table_ID) "
				+ "WHERE tt.AD_Table_ID=? "
				+ "ORDER BY w.IsDefault DESC, t.SeqNo, ABS (tt.AD_Window_ID-t.AD_Window_ID)";
		int AD_Tab_ID = DB.getSQLValueEx(null, sql, AD_Table_ID);
		return AD_Tab_ID;
	}

	private void renderReport() {
		media = null;
		Listitem selected = previewType.getSelectedItem();
		if (selected == null || "PDF".equals(selected.getValue())) {
			future = Adempiere.getThreadPoolExecutor().submit(new DesktopRunnable(new PDFRendererRunnable(this),getDesktop()));
		} else if ("HTML".equals(previewType.getSelectedItem().getValue())) {
			future = Adempiere.getThreadPoolExecutor().submit(new DesktopRunnable(new HTMLRendererRunnable(this),getDesktop()));
		} else if ("XLS".equals(previewType.getSelectedItem().getValue())) {			
			future = Adempiere.getThreadPoolExecutor().submit(new DesktopRunnable(new XLSRendererRunnable(this),getDesktop()));
		}						
	}

	private void onPreviewReport() {
		try {
			if (future != null) {
				try {
					future.get();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new RuntimeException(e.getCause());
				}
			}
			mediaVersion++;
			String url = Utils.getDynamicMediaURI(this, mediaVersion, media.getName(), media.getFormat());
			iframe.setContent(media);
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			if (url.startsWith(request.getContextPath() + "/"))
				url = url.substring((request.getContextPath() + "/").length());
			reportLink.setHref(url);
			reportLink.setLabel(media.getName());
			revalidate();
		} finally {
			hideBusyDialog();
			future = null;
		}
	}

	private String makePrefix(String name) {
		StringBuilder prefix = new StringBuilder();
		char[] nameArray = name.toCharArray();
		for (char ch : nameArray) {
			if (Character.isLetterOrDigit(ch)) {
				prefix.append(ch);
			} else {
				prefix.append("_");
			}
		}
		return prefix.toString();
	}
	
	/**
	 * 	Dynamic Init
	 */
	private void dynInit()
	{
		summary.addActionListener(this);
		summary.setStyle("font-size: 14px");
		
		fillComboReport(m_reportEngine.getPrintFormat().get_ID());

		//	fill Drill Options (Name, TableName)
		comboDrill.appendItem("", null);
		String sql = "SELECT t.AD_Table_ID, t.TableName, e.PrintName, NULLIF(e.PO_PrintName,e.PrintName) "
			+ "FROM AD_Column c "
			+ " INNER JOIN AD_Column used ON (c.ColumnName=used.ColumnName)"
			+ " INNER JOIN AD_Table t ON (used.AD_Table_ID=t.AD_Table_ID AND t.IsView='N' AND t.AD_Table_ID <> c.AD_Table_ID)"
			+ " INNER JOIN AD_Column cKey ON (t.AD_Table_ID=cKey.AD_Table_ID AND cKey.IsKey='Y')"
			+ " INNER JOIN AD_Element e ON (cKey.ColumnName=e.ColumnName) "
			+ "WHERE c.AD_Table_ID=? AND c.IsKey='Y' "
			+ "ORDER BY 3";
		boolean trl = !Env.isBaseLanguage(Env.getCtx(), "AD_Element");
		if (trl)
			sql = "SELECT t.AD_Table_ID, t.TableName, et.PrintName, NULLIF(et.PO_PrintName,et.PrintName) "
				+ "FROM AD_Column c"
				+ " INNER JOIN AD_Column used ON (c.ColumnName=used.ColumnName)"
				+ " INNER JOIN AD_Table t ON (used.AD_Table_ID=t.AD_Table_ID AND t.IsView='N' AND t.AD_Table_ID <> c.AD_Table_ID)"
				+ " INNER JOIN AD_Column cKey ON (t.AD_Table_ID=cKey.AD_Table_ID AND cKey.IsKey='Y')"
				+ " INNER JOIN AD_Element e ON (cKey.ColumnName=e.ColumnName)"
				+ " INNER JOIN AD_Element_Trl et ON (e.AD_Element_ID=et.AD_Element_ID) "
				+ "WHERE c.AD_Table_ID=? AND c.IsKey='Y'"
				+ " AND et.AD_Language=? "
				+ "ORDER BY 3";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_reportEngine.getPrintFormat().getAD_Table_ID());
			if (trl)
				pstmt.setString(2, Env.getAD_Language(Env.getCtx()));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String tableName = rs.getString(2);
				String name = rs.getString(3);
				String poName = rs.getString(4);
				if (poName != null)
					name += "/" + poName;
				comboDrill.appendItem(name, tableName);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		if (comboDrill.getItemCount() == 1)
		{
			labelDrill.setVisible(false);
			comboDrill.setVisible(false);
		}
		else
			comboDrill.addEventListener(Events.ON_SELECT, this);

		revalidate();
	}	//	dynInit
	
	/**
	 * 	Fill ComboBox comboReport (report options)
	 *  @param AD_PrintFormat_ID item to be selected
	 */
	private void fillComboReport(int AD_PrintFormat_ID)
	{
		comboReport.removeEventListener(Events.ON_SELECT, this);
		comboReport.getItems().clear();
		KeyNamePair selectValue = null;
		
		int AD_Window_ID = Env.getContextAsInt(Env.getCtx(), m_reportEngine.getWindowNo(), "_WinInfo_AD_Window_ID", true);
		if (AD_Window_ID == 0)
			AD_Window_ID = Env.getZoomWindowID(m_reportEngine.getQuery());

		int reportViewID = m_reportEngine.getPrintFormat().getAD_ReportView_ID();

		//	fill Report Options
		String sql = MRole.getDefault().addAccessSQL(
			"SELECT * "
				+ "FROM AD_PrintFormat "
				+ "WHERE AD_Table_ID=? "
				//Added Lines by Armen
				+ "AND IsActive='Y' "
				//End of Added Lines
				+ (AD_Window_ID > 0 ? "AND (AD_Window_ID=? OR AD_Window_ID IS NULL) " : "")
				+ (reportViewID > 0 ? "AND AD_ReportView_ID=? " : "")
				+ "ORDER BY Name",
			"AD_PrintFormat", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		int AD_Table_ID = m_reportEngine.getPrintFormat().getAD_Table_ID();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			int idx = 1;
			pstmt.setInt(idx++, AD_Table_ID);
			if (AD_Window_ID > 0)
				pstmt.setInt(idx++, AD_Window_ID);
			if (reportViewID > 0)
				pstmt.setInt(idx++, reportViewID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPrintFormat printFormat = new MPrintFormat (Env.getCtx(), rs, null);
				
				KeyNamePair pp = new KeyNamePair(printFormat.get_ID(), printFormat.get_Translation(MPrintFormat.COLUMNNAME_Name));
				Listitem li = comboReport.appendItem(pp.getName(), pp.getKey());
				if (rs.getInt(1) == AD_PrintFormat_ID)
				{
					selectValue = pp;
					if(selectValue != null)
						comboReport.setSelectedItem(li);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		// IDEMPIERE-297 - Check for Table Access and Window Access for New Report
		int pfAD_Window_ID = MPrintFormat.getZoomWindowID(AD_PrintFormat_ID);
		if (   MRole.getDefault().isTableAccess(MPrintFormat.Table_ID, false) 
			&& Boolean.TRUE.equals(MRole.getDefault().getWindowAccess(pfAD_Window_ID)))
		{
			StringBuffer sb = new StringBuffer("** ").append(Msg.getMsg(Env.getCtx(), "NewReport")).append(" **");
			KeyNamePair pp = new KeyNamePair(-1, sb.toString());
			comboReport.appendItem(pp.getName(), pp.getKey());
		}
		comboReport.addEventListener(Events.ON_SELECT, this);
	}	//	fillComboReport

	/**
	 * 	Revalidate settings after change of environment
	 */
	private void revalidate()
	{
		//	Report Info
		setTitle(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Report") + ": " + m_reportEngine.getName()));
		StringBuilder sb = new StringBuilder ();
		sb.append(Msg.getMsg(Env.getCtx(), "DataCols")).append("=")
			.append(m_reportEngine.getColumnCount())
			.append(", ").append(Msg.getMsg(Env.getCtx(), "DataRows")).append("=")
			.append(m_reportEngine.getRowCount());
		statusBar.setStatusLine(sb.toString());
		//
		bWizard.setDisabled(
				(   m_reportEngine.getPrintFormat() == null
				 || (m_reportEngine.getPrintFormat().getAD_Client_ID() == 0 && Env.getAD_Client_ID(Env.getCtx()) != 0)
				 || m_reportEngine.getPrintFormat().isForm()));
	}	//	revalidate

	/**
	 * 	Dispose
	 */
	public void onClose()
	{
		cleanUp();
		super.onClose();
	}	//	dispose

	@Override
	public void onClose(Tabpanel tabPanel) {
		Tab tab = tabPanel.getLinkedTab();
		tab.close();
		cleanUp();
	}
	
	
	@Override
	public void setParent(Component parent) {
		super.setParent(parent);
		if (parent != null) {
			if (parent instanceof Tabpanel) {
				Tabpanel tabPanel = (Tabpanel) parent;
				tabPanel.setOnCloseHandler(this);
			}
		}
	}

	private void cleanUp() {
		if (m_reportEngine != null || m_WindowNo >= 0)
		{
			SessionManager.getAppDesktop().unregisterWindow(m_WindowNo);
			m_reportEngine = null;
			m_ctx = null;
			m_WindowNo = -1;
		}
		if (future != null && !future.isDone())
		{
			future.cancel(true);
			future = null;
		}
			
	}
	
	public void onEvent(Event event) throws Exception {
		
		if(event.getTarget().getId().equals(ConfirmPanel.A_CANCEL))
			winExportFile.onClose();
		else if(event.getTarget().getId().equals(ConfirmPanel.A_OK))			
			exportFile();
		else if(event.getName().equals(Events.ON_CLICK) || event.getName().equals(Events.ON_SELECT)) 
			actionPerformed(event);
		else if (event.getTarget() == summary) 
		{
			m_reportEngine.setSummary(summary.isSelected());
			cmd_report();
		}		
		else if (event.getName().equals(ON_RENDER_REPORT_EVENT))
		{
			onRenderReportEvent();
		}
	}

	private void onRenderReportEvent() {
    	renderReport();
	}

	/**************************************************************************
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed (Event e)
	{
		if (m_setting)
			return;
		if (e.getTarget() == comboReport)
			cmd_report();
		else if (e.getTarget() == bFind)
			cmd_find();
		else if (e.getTarget() == bExport)
			cmd_export();
		else if (e.getTarget() == previewType)
			cmd_render();
		else if (e.getTarget() == bSendMail)
			cmd_sendMail();
		else if (e.getTarget() == bArchive)
			cmd_archive();
		else if (e.getTarget() == bCustomize)
			cmd_customize();
		else if (e.getTarget() == bWizard)
			cmd_Wizard();
		else if (e.getTarget() == bRefresh)
			cmd_report();
		//
		else if (e.getTarget() == m_ddM)
			cmd_window(m_ddQ);
		else if (e.getTarget() == m_daM)
			cmd_window(m_daQ);
	}	//	actionPerformed
	
	private void cmd_render() {
		postRenderReportEvent();		
	}

	/**
	 * 	Execute Drill to Query
	 * 	@param query query
	 *  @param component
	 */
	private void executeDrill (MQuery query, Component component)
	{
		int AD_Table_ID = MTable.getTable_ID(query.getTableName());
		if (!MRole.getDefault().isCanReport(AD_Table_ID))
		{
			FDialog.error(m_WindowNo, this, "AccessCannotReport", query.getTableName());
			return;
		}
		if (AD_Table_ID != 0)
			new WReport (AD_Table_ID, query, component, 0);
		else
			log.warning("No Table found for " + query.getWhereClause(true));
	}	//	executeDrill
	
	/**
	 * 	Open Window
	 *	@param query query
	 */
	private void cmd_window (MQuery query)
	{
		if (query == null)
			return;
		AEnv.zoom(query);
	}	//	cmd_window
	
	/**
	 * 	Send Mail
	 */
	private void cmd_sendMail()
	{
		String to = "";
		MUser from = MUser.get(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()));
		String subject = m_reportEngine.getName();
		String message = "";
		File attachment = null;
		
		try
		{
			attachment = new File(FileUtil.getTempMailName(subject, ".pdf"));
			m_reportEngine.getPDF(attachment);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}

		WEMailDialog dialog = new WEMailDialog (Msg.getMsg(Env.getCtx(), "SendMail"),
			from, to, subject, message, new FileDataSource(attachment));
		AEnv.showWindow(dialog);
	}	//	cmd_sendMail

	/**
	 * 	Archive Report directly
	 */
	private void cmd_archive ()
	{
		boolean success = false;
		byte[] data = Document.getPDFAsArray(m_reportEngine.getLayout().getPageable(false));	//	No Copy
		if (data != null)
		{
			MArchive archive = new MArchive (Env.getCtx(), m_reportEngine.getPrintInfo(), null);
			archive.setBinaryData(data);
			success = archive.save();
		}
		if (success)
			FDialog.info(m_WindowNo, this, "Archived");
		else
			FDialog.error(m_WindowNo, this, "ArchiveError");
	}	//	cmd_archive

	/**
	 * 	Export
	 */
	private void cmd_export()
	{		
		log.config("");
		if (!m_isCanExport)
		{
			FDialog.error(m_WindowNo, this, "AccessCannotExport", getTitle());
			return;
		}
		
		if(winExportFile == null)
		{
			winExportFile = new Window();
			winExportFile.setTitle(Msg.getMsg(Env.getCtx(), "Export") + ": " + getTitle());
			winExportFile.setWidth("450px");
			winExportFile.setHeight("150px");
			winExportFile.setClosable(true);
			winExportFile.setBorder("normal");
			winExportFile.setSclass("popup-dialog");
			winExportFile.setStyle("position:absolute");

			cboType.setMold("select");
			
			cboType.getItems().clear();			
			cboType.appendItem("ps" + " - " + Msg.getMsg(Env.getCtx(), "FilePS"), "ps");
			cboType.appendItem("xml" + " - " + Msg.getMsg(Env.getCtx(), "FileXML"), "xml");
			ListItem li = cboType.appendItem("pdf" + " - " + Msg.getMsg(Env.getCtx(), "FilePDF"), "pdf");
			cboType.appendItem("html" + " - " + Msg.getMsg(Env.getCtx(), "FileHTML"), "html");
			cboType.appendItem("txt" + " - " + Msg.getMsg(Env.getCtx(), "FileTXT"), "txt");
			cboType.appendItem("ssv" + " - " + Msg.getMsg(Env.getCtx(), "FileSSV"), "ssv");
			cboType.appendItem("csv" + " - " + Msg.getMsg(Env.getCtx(), "FileCSV"), "csv");
			cboType.appendItem("xls" + " - " + Msg.getMsg(Env.getCtx(), "FileXLS"), "xls");
			cboType.setSelectedItem(li);
			
			Hbox hb = new Hbox();
			hb.setSclass("dialog-content");			
			hb.setAlign("center");
			hb.setPack("start");
			Div div = new Div();
			div.setStyle("text-align: right;");
			div.appendChild(new Label(Msg.getMsg(Env.getCtx(), "FilesOfType")));
			hb.appendChild(div);
			hb.appendChild(cboType);
			cboType.setWidth("100%");

			Vbox vb = new Vbox();
			vb.setWidth("100%");
			winExportFile.appendChild(vb);
			vb.appendChild(hb);
			vb.appendChild(confirmPanel);
			LayoutUtils.addSclass("dialog-footer", confirmPanel);
			confirmPanel.addActionListener(this);
		}
		
		winExportFile.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
		AEnv.showWindow(winExportFile);
	}	//	cmd_export
		
	private void exportFile()
	{
		try
		{
			ListItem li = cboType.getSelectedItem();
			if(li == null || li.getValue() == null)
			{
				FDialog.error(m_WindowNo, winExportFile, "FileInvalidExtension");
				return;
			}
			
			String ext = li.getValue().toString();
			
			byte[] data = null;
			File inputFile = null;
									
			if (ext.equals("pdf"))
			{
				data = m_reportEngine.createPDFData();
			}
			else if (ext.equals("ps"))
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				m_reportEngine.createPS(baos);
				data = baos.toByteArray();
			}
			else if (ext.equals("xml"))
			{
				StringWriter sw = new StringWriter();							
				m_reportEngine.createXML(sw);
				data = sw.getBuffer().toString().getBytes();
			}
			else if (ext.equals("csv"))
			{
				StringWriter sw = new StringWriter();							
				m_reportEngine.createCSV(sw, ',', m_reportEngine.getPrintFormat().getLanguage());
				data = sw.getBuffer().toString().getBytes();
			}
			else if (ext.equals("ssv"))
			{
				StringWriter sw = new StringWriter();							
				m_reportEngine.createCSV(sw, ';', m_reportEngine.getPrintFormat().getLanguage());
				data = sw.getBuffer().toString().getBytes();
			}
			else if (ext.equals("txt"))
			{
				StringWriter sw = new StringWriter();							
				m_reportEngine.createCSV(sw, '\t', m_reportEngine.getPrintFormat().getLanguage());
				data = sw.getBuffer().toString().getBytes();							
			}
			else if (ext.equals("html") || ext.equals("htm"))
			{
				StringWriter sw = new StringWriter();							
				m_reportEngine.createHTML(sw, false, m_reportEngine.getPrintFormat().getLanguage());
				data = sw.getBuffer().toString().getBytes();	
			}
			else if (ext.equals("xls"))
			{
				inputFile = File.createTempFile("Export", ".xls");							
				m_reportEngine.createXLS(inputFile, m_reportEngine.getPrintFormat().getLanguage());
			}
			else
			{
				FDialog.error(m_WindowNo, winExportFile, "FileInvalidExtension");
				return;
			}

			winExportFile.onClose();
			AMedia media = null;
			if (data != null)
				media = new AMedia(m_reportEngine.getPrintFormat().getName() + "." + ext, null, "application/octet-stream", data);
			else
				media = new AMedia(m_reportEngine.getPrintFormat().getName() + "." + ext, null, "application/octet-stream", inputFile, true);
			Filedownload.save(media, m_reportEngine.getPrintFormat().getName() + "." + ext);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Failed to export content.", e);
		}
	}
	
	/**
	 * 	Report Combo - Start other Report or create new one
	 */
	private void cmd_report()
	{
		ListItem li = comboReport.getSelectedItem();
		if(li == null || li.getValue() == null) return;
		
		Object pp = li.getValue();
		if (pp == null)
			return;
		//
		MPrintFormat pf = null;
		int AD_PrintFormat_ID = Integer.valueOf(pp.toString());

		//	create new
		if (AD_PrintFormat_ID == -1)
		{
			int AD_ReportView_ID = m_reportEngine.getPrintFormat().getAD_ReportView_ID();
			if (AD_ReportView_ID != 0)
			{
				String name = m_reportEngine.getName();
				int index = name.lastIndexOf('_');
				if (index != -1)
					name = name.substring(0,index);
				pf = MPrintFormat.createFromReportView(m_ctx, AD_ReportView_ID, name);
			}
			else
			{
				int AD_Table_ID = m_reportEngine.getPrintFormat().getAD_Table_ID();
				pf = MPrintFormat.createFromTable(m_ctx, AD_Table_ID);
			}
			if (pf != null)
				fillComboReport(pf.get_ID());
			else
				return;
		}
		else
			pf = MPrintFormat.get (Env.getCtx(), AD_PrintFormat_ID, true);
		
		//	Get Language from previous - thanks Gunther Hoppe 
		if (m_reportEngine.getPrintFormat() != null)
		{
			pf.setLanguage(m_reportEngine.getPrintFormat().getLanguage());		//	needs to be re-set - otherwise viewer will be blank
			pf.setTranslationLanguage(m_reportEngine.getPrintFormat().getLanguage());
		}
		m_reportEngine.setPrintFormat(pf);
		
		postRenderReportEvent();
	}	//	cmd_report

	private void postRenderReportEvent() {
		showBusyDialog();
		Events.echoEvent(ON_RENDER_REPORT_EVENT, this, null);
	}



	/**
	 * 	Query Report
	 */
	private void cmd_find()
	{
		String title = null; 
		String tableName = null;

		int AD_Table_ID = m_reportEngine.getPrintFormat().getAD_Table_ID();
		int AD_Tab_ID = getAD_Tab_ID(AD_Table_ID);
		// ASP
		MClient client = MClient.get(Env.getCtx());
		String ASPFilter = "";
		if (client.isUseASP())
			ASPFilter =
				"     AND (   AD_Tab_ID IN ( "
				// Just ASP subscribed tabs for client "
				+ "              SELECT t.AD_Tab_ID "
				+ "                FROM ASP_Tab t, ASP_Window w, ASP_Level l, ASP_ClientLevel cl "
				+ "               WHERE w.ASP_Level_ID = l.ASP_Level_ID "
				+ "                 AND cl.AD_Client_ID = " + client.getAD_Client_ID()
				+ "                 AND cl.ASP_Level_ID = l.ASP_Level_ID "
				+ "                 AND t.ASP_Window_ID = w.ASP_Window_ID "
				+ "                 AND t.IsActive = 'Y' "
				+ "                 AND w.IsActive = 'Y' "
				+ "                 AND l.IsActive = 'Y' "
				+ "                 AND cl.IsActive = 'Y' "
				+ "                 AND t.ASP_Status = 'S') " // Show
				+ "        OR AD_Tab_ID IN ( "
				// + show ASP exceptions for client
				+ "              SELECT AD_Tab_ID "
				+ "                FROM ASP_ClientException ce "
				+ "               WHERE ce.AD_Client_ID = " + client.getAD_Client_ID()
				+ "                 AND ce.IsActive = 'Y' "
				+ "                 AND ce.AD_Tab_ID IS NOT NULL "
				+ "                 AND ce.AD_Field_ID IS NULL "
				+ "                 AND ce.ASP_Status = 'S') " // Show
				+ "       ) "
				+ "   AND AD_Tab_ID NOT IN ( "
				// minus hide ASP exceptions for client
				+ "          SELECT AD_Tab_ID "
				+ "            FROM ASP_ClientException ce "
				+ "           WHERE ce.AD_Client_ID = " + client.getAD_Client_ID()
				+ "             AND ce.IsActive = 'Y' "
				+ "             AND ce.AD_Tab_ID IS NOT NULL "
				+ "             AND ce.AD_Field_ID IS NULL "
				+ "             AND ce.ASP_Status = 'H')"; // Hide
		//
		String sql = null;
		if (!Env.isBaseLanguage(Env.getCtx(), "AD_Tab")) {
			sql = "SELECT Name, TableName FROM AD_Tab_vt WHERE AD_Tab_ID=?"
					+ " AND AD_Language='" + Env.getAD_Language(Env.getCtx()) + "' " + ASPFilter;
		} else {
			sql = "SELECT Name, TableName FROM AD_Tab_v WHERE AD_Tab_ID=? " + ASPFilter;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Tab_ID);
			rs = pstmt.executeQuery();
			//
			if (rs.next())
			{
				title = rs.getString(1);				
				tableName = rs.getString(2);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		GridField[] findFields = null;
		if (tableName != null)
			findFields = GridField.createFields(m_ctx, m_WindowNo, 0, AD_Tab_ID);
		
		if (findFields == null)		//	No Tab for Table exists
			bFind.setVisible(false);
		else
		{
            final FindWindow find = new FindWindow(m_WindowNo, title, AD_Table_ID, tableName,m_reportEngine.getWhereExtended(), findFields, 1, AD_Tab_ID);
            if (!find.initialize())
            	return;
            
            find.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (!find.isCancel())
		            {
		            	m_reportEngine.setQuery(find.getQuery());
		            	postRenderReportEvent();
		            }
				}
			});
            find.setTitle(null);
            LayoutUtils.openPopupWindow(toolBar, find, "after_start");
		}
	}	//	cmd_find

	/**
	 * 	Call Customize
	 */
	private void cmd_customize()
	{
		int AD_PrintFormat_ID = m_reportEngine.getPrintFormat().get_ID();
		int pfAD_Window_ID = MPrintFormat.getZoomWindowID(AD_PrintFormat_ID);
		AEnv.zoom(pfAD_Window_ID, MQuery.getEqualQuery("AD_PrintFormat_ID", AD_PrintFormat_ID));
	}	//	cmd_customize
	
	/*IDEMPIERE -379*/
	private void cmd_Wizard()
	{
		int AD_PrintFormat_ID = m_reportEngine.getPrintFormat().get_ID();

		Env.setContext(m_ctx, "AD_PrintFormat_ID", AD_PrintFormat_ID);
	    ADForm form = ADForm.openForm(SystemIDs.FORM_REPORT_WIZARD);
		WReportCustomization av = (WReportCustomization) form.getICustomForm();
		av.setReportEngine(m_reportEngine);
		form.setClosable(true);
		form.setWidth("70%");
		form.setHeight("85%");
		form.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (DialogEvents.ON_WINDOW_CLOSE.equals(event.getName())) {
				   if(m_reportEngine.getPrintFormat().get_ID()!=Env.getContextAsInt(m_ctx, "AD_PrintFormat_ID")){
					  fillComboReport (m_reportEngine.getPrintFormat().get_ID());	
				   }
				   cmd_report();		
				}
			}
		});
		form.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
		SessionManager.getAppDesktop().showWindow(form);
	}	//	cmd_Wizard

	//-- ComponentCtrl --//
	public Object getExtraCtrl() {
		return new ExtraCtrl();
	}
	/** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
	protected class ExtraCtrl extends XulElement.ExtraCtrl
	implements DynamicMedia {
		//-- DynamicMedia --//
		public Media getMedia(String pathInfo) {
			return media;
		}
	}
	
	private boolean ToolBarMenuRestictionLoaded = false;
	public void updateToolbarAccess(int AD_Window_ID, int AD_Process_ID) {
		if (ToolBarMenuRestictionLoaded)
			return;
		Properties m_ctx = Env.getCtx();
		int ToolBarButton_ID = 0;

		int[] restrictionList = AD_Window_ID > 0 
				? MToolBarButtonRestrict.getOfWindow(m_ctx, MRole.getDefault().getAD_Role_ID(), AD_Window_ID, true, null)
				: MToolBarButtonRestrict.getOfReport(m_ctx, MRole.getDefault().getAD_Role_ID(), AD_Process_ID, null);
		if (log.isLoggable(Level.INFO))
			log.info("restrictionList="+restrictionList.toString());

		for (int i = 0; i < restrictionList.length; i++)
		{
			ToolBarButton_ID= restrictionList[i];
			X_AD_ToolBarButton tbt = new X_AD_ToolBarButton(m_ctx, ToolBarButton_ID, null);
			if (!"R".equals(tbt.getAction()))
				continue;
			
			String restrictName = tbt.getComponentName();
			if (log.isLoggable(Level.CONFIG)) log.config("tbt="+tbt.getAD_ToolBarButton_ID() + " / " + restrictName);

			for (Component p = this.toolBar.getFirstChild(); p != null; p = p.getNextSibling()) {
				if (p instanceof Toolbarbutton) {
					if ( restrictName.equals(((ToolBarButton)p).getName()) ) {
						this.toolBar.removeChild(p);
						break;
					}
				}
			}
		}	// All restrictions

		ToolBarMenuRestictionLoaded = true;
	}//updateToolBarAndMenuWithRestriction

	private void showBusyDialog() {		
		progressWindow = new BusyDialog();
		progressWindow.setStyle("position: absolute;");
		this.appendChild(progressWindow);
		showBusyMask(progressWindow);
		LayoutUtils.openOverlappedWindow(this, progressWindow, "middle_center");
	}
	
	private Div getMask() {
		if (mask == null) {
			mask = new Mask();
		}
		return mask;
	}
	
	private void showBusyMask(Window window) {
		getParent().appendChild(getMask());
		StringBuilder script = new StringBuilder("var w=zk.Widget.$('#");
		script.append(getParent().getUuid()).append("');");
		if (window != null) {
			script.append("var d=zk.Widget.$('#").append(window.getUuid()).append("');w.busy=d;");
		} else {
			script.append("w.busy=true;");
		}
		Clients.response(new AuScript(script.toString()));
	}
	
	public void hideBusyMask() {
		if (mask != null && mask.getParent() != null) {
			mask.detach();
			StringBuilder script = new StringBuilder("var w=zk.Widget.$('#");
			script.append(getParent().getUuid()).append("');w.busy=false;");
			Clients.response(new AuScript(script.toString()));
		}
	}
	
	private void hideBusyDialog() {
		hideBusyMask();
		if (progressWindow != null) {
			progressWindow.dispose();
			progressWindow = null;
		}
	}
	
	static class PDFRendererRunnable extends ContextRunnable implements IServerPushCallback {

		private ZkReportViewer viewer;

		public PDFRendererRunnable(ZkReportViewer viewer) {
			super();
			this.viewer = viewer;
		}

		@Override
		protected void doRun() {
			try {
				String path = System.getProperty("java.io.tmpdir");
				String prefix = viewer.makePrefix(viewer.m_reportEngine.getName());
				if (log.isLoggable(Level.FINE))
				{
					log.log(Level.FINE, "Path="+path + " Prefix="+prefix);
				}
				File file = File.createTempFile(prefix, ".pdf", new File(path));
				viewer.m_reportEngine.createPDF(file);
				viewer.media = new AMedia(file.getName(), "pdf", "application/pdf", file, true);
			} catch (Exception e) {
				if (e instanceof RuntimeException)
					throw (RuntimeException)e;
				else
					throw new RuntimeException(e);
			} finally {		
				Desktop desktop = AEnv.getDesktop();
				if (desktop != null && desktop.isAlive()) {
					new ServerPushTemplate(desktop).executeAsync(this);
				}
			}
		}

		@Override
		public void updateUI() {
			viewer.labelDrill.setVisible(false);
			viewer.comboDrill.setVisible(false);
			viewer.onPreviewReport();
		}
		
	}
	
	static class HTMLRendererRunnable extends ContextRunnable implements IServerPushCallback {

		private ZkReportViewer viewer;
		private String contextPath;
		
		public HTMLRendererRunnable(ZkReportViewer viewer) {
			super();
			this.viewer = viewer;
			contextPath = Executions.getCurrent().getContextPath();
		}

		@Override
		protected void doRun() {
			try {
				String path = System.getProperty("java.io.tmpdir");
				String prefix = viewer.makePrefix(viewer.m_reportEngine.getName());
				if (log.isLoggable(Level.FINE))
				{
					log.log(Level.FINE, "Path="+path + " Prefix="+prefix);
				}
				File file = File.createTempFile(prefix, ".html", new File(path));
				viewer.m_reportEngine.createHTML(file, false, AEnv.getLanguage(Env.getCtx()), new HTMLExtension(contextPath, "rp", viewer.getUuid()));
				viewer.media = new AMedia(file.getName(), "html", "text/html", file, false);
			} catch (Exception e) {
				if (e instanceof RuntimeException)
					throw (RuntimeException)e;
				else
					throw new RuntimeException(e);
			} finally {
				Desktop desktop = AEnv.getDesktop();
				if (desktop != null && desktop.isAlive()) {
					new ServerPushTemplate(desktop).executeAsync(this);
				}
			}
		}

		@Override
		public void updateUI() {						
			if (viewer.comboDrill.getItemCount() > 1) {
				viewer.labelDrill.setVisible(true);
				viewer.comboDrill.setVisible(true);
			}
			viewer.onPreviewReport();
		}		
	}
	
	static class XLSRendererRunnable extends ContextRunnable  implements IServerPushCallback {

		private ZkReportViewer viewer;

		public XLSRendererRunnable(ZkReportViewer viewer) {
			super();
			this.viewer = viewer;
		}

		@Override
		protected void doRun() {
			try {
				String path = System.getProperty("java.io.tmpdir");
				String prefix = viewer.makePrefix(viewer.m_reportEngine.getName());
				if (log.isLoggable(Level.FINE))
				{
					log.log(Level.FINE, "Path="+path + " Prefix="+prefix);
				}
				File file = File.createTempFile(prefix, ".xls", new File(path));
				viewer.m_reportEngine.createXLS(file, AEnv.getLanguage(Env.getCtx()));
				viewer.media = new AMedia(file.getName(), "xls", "application/vnd.ms-excel", file, true);
			} catch (Exception e) {
				if (e instanceof RuntimeException)
					throw (RuntimeException)e;
				else
					throw new RuntimeException(e);
			} finally {			
				Desktop desktop = AEnv.getDesktop();
				if (desktop != null && desktop.isAlive()) {
					new ServerPushTemplate(desktop).executeAsync(this);
				}
			}
		}

		@Override
		public void updateUI() {
			viewer.labelDrill.setVisible(false);
			viewer.comboDrill.setVisible(false);
			viewer.onPreviewReport();
		}
		
	}
}
