/******************************************************************************
 * Copyright (C) 2012 Heng Sin Low                                            *
 * Copyright (C) 2012 Trek Global                 							  *
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
package org.adempiere.webui.adwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.AdempiereIdGenerator;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Menupopup;
import org.adempiere.webui.component.ToolBar;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.ToolbarListener;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.WRecordInfo;
import org.compiere.model.DataStatusEvent;
import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zhtml.Text;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Space;

/**
 * @author hengsin
 *
 */
public class BreadCrumb extends Div implements EventListener<Event>{

	private static final String INFO_INDICATOR_IMAGE = "/images/InfoIndicator16.png";

	private static final String ERROR_INDICATOR_IMAGE = "/images/ErrorIndicator16.png";

	/**
	 * generated serial id 
	 */
	private static final long serialVersionUID = 929253935475618911L;
	
	private static final String BTNPREFIX = "Btn";
	
	private Hbox layout;

	private ToolBarButton btnFirst, btnPrevious, btnNext, btnLast, btnRecordInfo;
	
	private LinkedHashMap<String, String> links;

	private int windowNo;
	
	private HashMap<String, ToolBarButton> buttons = new HashMap<String, ToolBarButton>();
	private Map<Integer, ToolBarButton> altKeyMap = new HashMap<Integer, ToolBarButton>();

	private DataStatusEvent m_dse;

	private String m_text;

	private boolean m_statusError;

	private String m_statusText;

	private Window msgPopup;

	private Div msgPopupCnt;

	private ToolbarListener toolbarListener;

	private Hlayout toolbarContainer;

	private Hbox messageContainer;

	private Caption msgPopupCaption;

	/**
	 * 
	 */
	public BreadCrumb(int windowNo) {
		this.windowNo = windowNo;
		layout = new Hbox();
		layout.setPack("start");
		layout.setAlign("center");
		this.appendChild(layout);
		layout.setHeight("100%");
		layout.setStyle("float: left");

		this.setVisible(false);
		this.setSclass("adwindow-breadcrumb");
				
		toolbarContainer = new Hlayout();
		layout.appendChild(toolbarContainer);
		
		ToolBar toolbar = new ToolBar();
		toolbarContainer.appendChild(toolbar);		
		btnFirst = createButton("First", "First", "First");
		toolbar.appendChild(btnFirst);
        btnPrevious = createButton("Previous", "Previous", "Previous");
        toolbar.appendChild(btnPrevious);
        btnRecordInfo = new ToolBarButton();
        btnRecordInfo.setLabel("");
        btnRecordInfo.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Who")));
        btnRecordInfo.addEventListener(Events.ON_CLICK, this);
        btnRecordInfo.setSclass("breadcrumb-record-info link");
        toolbar.appendChild(btnRecordInfo);
        btnNext = createButton("Next", "Next", "Next");
        toolbar.appendChild(btnNext);
        btnLast = createButton("Last", "Last", "Last");
        toolbar.appendChild(btnLast);
        
        messageContainer = new Hbox();
        messageContainer.setStyle("float: right; height: 30px;");
        messageContainer.setAlign("center");
        appendChild(messageContainer);
        
        altKeyMap.put(KeyEvent.UP, btnPrevious);
		altKeyMap.put(KeyEvent.DOWN, btnNext);
		altKeyMap.put(KeyEvent.PAGE_UP, btnFirst);
		altKeyMap.put(KeyEvent.PAGE_DOWN, btnLast);		
		
		createPopup();
		
		toolbar.setStyle("background-image: none; background-color: transparent; border: none;");
	}

	public void setToolbarListener(ToolbarListener listener) {
		this.toolbarListener = listener;
	}
	
	public void addPath(String label, String id, boolean clickable) {
		if (clickable) {
			BreadCrumbLink a = new BreadCrumbLink();
			a.setLabel(label);
			a.setPathId(id);
			a.addEventListener(Events.ON_CLICK, this);
			if (layout.getChildren().size() > 1) {
				Label symbol = new Label();
				symbol.setValue(" > ");
				layout.insertBefore(symbol, toolbarContainer);
			}
			layout.insertBefore(a, toolbarContainer);
		} else {
			Label pathLabel = new Label();
			pathLabel.setValue(label);
			if (layout.getChildren().size() > 1) {
				Label symbol = new Label();
				symbol.setValue(" > ");
				layout.insertBefore(symbol, toolbarContainer);
			}
			pathLabel.setStyle("font-weight: bold");
			layout.insertBefore(pathLabel, toolbarContainer);
		}
	}
	
	public List<BreadCrumbLink> getParentLinks() {
		List<BreadCrumbLink> parents = new ArrayList<BreadCrumbLink>();
		for(Component component : layout.getChildren()) {
			if (component instanceof BreadCrumbLink)
				parents.add((BreadCrumbLink) component);
		}
		return parents;
	}
	
	public void addLinks(LinkedHashMap<String, String> links) {
		this.links = links;
		final Label pathLabel = (Label) layout.getChildren().get(layout.getChildren().size()-2);
		pathLabel.setStyle("cursor: pointer; font-weight: bold");
		pathLabel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				Menupopup popup = new Menupopup();
				for(Map.Entry<String, String>entry : BreadCrumb.this.links.entrySet()) {
					final Menuitem item = new Menuitem();
					item.setLabel(entry.getValue());
					item.setValue(entry.getKey());
					item.addEventListener(Events.ON_CLICK, BreadCrumb.this);
					popup.appendChild(item);
				}
				popup.setPage(pathLabel.getPage());
				popup.open(pathLabel);
			}
		});
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == btnRecordInfo) {
			if (m_dse == null
				|| m_dse.CreatedBy == null
				|| !MRole.getDefault().isShowPreference())
				return;

			String title = Msg.getMsg(Env.getCtx(), "Who") + m_text;
			new WRecordInfo (title, m_dse);
		} else if (event.getTarget().getParent() == messageContainer) {
			showPopup();
		} else if (event.getTarget() == btnFirst) {
			if (toolbarListener != null)
				toolbarListener.onFirst();
		} else if (event.getTarget() == btnPrevious) {
			if (toolbarListener != null)
				toolbarListener.onPrevious();
		} else if (event.getTarget() == btnNext) {
			if (toolbarListener != null)
				toolbarListener.onNext();
		} else if (event.getTarget() == btnLast) {
			if (toolbarListener != null)
				toolbarListener.onLast();
		} else {
			Events.sendEvent(this, event);
		}
	}

	public void reset() {
		layout.getChildren().clear();
		layout.appendChild(toolbarContainer);
		this.links = null;
	}

    public void enableFirstNavigation(boolean enabled)
    {
        this.btnFirst.setDisabled(!enabled);
        this.btnPrevious.setDisabled(!enabled);
    }

    public void enableLastNavigation(boolean enabled)
    {
        this.btnLast.setDisabled(!enabled);
        this.btnNext.setDisabled(!enabled);
    }

	private ToolBarButton createButton(String name, String image, String tooltip)
    {
    	ToolBarButton btn = new ToolBarButton("");
        btn.setName(BTNPREFIX+name);
        if (windowNo > 0)
        	btn.setAttribute(AdempiereIdGenerator.ZK_COMPONENT_PREFIX_ATTRIBUTE, "unq" + btn.getName() + "_" + windowNo);
        else
        	btn.setAttribute(AdempiereIdGenerator.ZK_COMPONENT_PREFIX_ATTRIBUTE, btn.getName());
        btn.setImage("/images/"+image + "24.png");
        btn.setTooltiptext(Msg.getMsg(Env.getCtx(),tooltip));
        btn.setSclass("breadcrumb-toolbar-button");
        
        buttons.put(name, btn);
        this.appendChild(btn);
        //make toolbar button last to receive focus
        btn.setTabindex(0);
        btn.addEventListener(Events.ON_CLICK, this);
        btn.setDisabled(true);

        return btn;
    }
	
	/**
     * @param text
     */
    public void setStatusDB (String text)
    {
        setStatusDB(text, null);
    }

    /**
     * @param text
     * @param dse
     */
    public void setStatusDB (String text, DataStatusEvent dse)
    {
        if (text == null || text.length() == 0)
        {
            btnRecordInfo.setLabel("");
        }
        else
        {
        	btnRecordInfo.setLabel(text);
        }

        m_text = text;
        m_dse = dse;
        if (m_dse != null) {
        	enableFirstNavigation(m_dse.getCurrentRow() > 0);
        	enableLastNavigation(m_dse.getTotalRows() > m_dse.getCurrentRow()+1);
        }
    }
    
    /**
     * @param text
     */
    public void setStatusLine (String text)
    {
        setStatusLine(text, false);
    }

    /**
     * @param text
     * @param error
     */
    public void setStatusLine (String text, boolean error)
    {    	    			
    	Execution execution = Executions.getCurrent();
    	if (execution != null) {
    		String key = this.getClass().getName()+"."+getUuid();
    		Object o = execution.getAttribute(key);
    		if (o != null) {
    			if (text == null || text.trim().length() == 0)
    				return;
    		} else {
    			execution.setAttribute(key, Boolean.TRUE);
    		}
    	}
    	
    	m_statusText = text;
    	m_statusError = error;
    	
		messageContainer.getChildren().clear();
		
		if (text == null || text.trim().length() == 0 )
			return;
		
		Image image = null;
    	if (error)
    		image = new Image(ERROR_INDICATOR_IMAGE);
    	else
			image = new Image(INFO_INDICATOR_IMAGE);    	
    	
		image.setAttribute("org.zkoss.zul.image.preload", Boolean.TRUE);
    	messageContainer.appendChild(image);
    	String labelText = buildLabelText(m_statusText);
    	if (error) {
    		Clients.showNotification(buildNotificationText(m_statusText), "error", image, "overlap_start", 3500, true);
    	}
    	Label label = new Label(labelText);
    	messageContainer.appendChild(label);
    	if (labelText.length() != m_statusText.length()) {
    		image.addEventListener(Events.ON_CLICK, this);
    		image.setStyle("cursor: pointer");
    		label.addEventListener(Events.ON_CLICK, this);
    		label.setStyle("cursor: pointer");
    		
    		label = new Label(" ...");
    		label.setStyle("cursor: pointer");
    		messageContainer.appendChild(label);
    		label.addEventListener(Events.ON_CLICK, this);
    	}
    	messageContainer.appendChild(new Space());

    	createPopupContent();    	    	    	
    }

	private String buildLabelText(String statusText) {
		if (statusText == null)
			return "";
		if (statusText.length() <= 80)
			return statusText;
		
		int index = statusText.indexOf(" - java.lang.Exception");
		if (index > 0)
			return statusText.substring(0, index);
		return statusText.substring(0, 80);
	}
	
	private String buildNotificationText(String statusText) {
		if (statusText == null)
			return "";
		if (statusText.length() <= 140)
			return statusText;
		
		int index = statusText.indexOf(" - java.lang.Exception");
		if (index > 0)
			return statusText.substring(0, index);
		return statusText.substring(0, 136) + " ...";
	}

	protected void createPopupContent() {
		Text t = new Text(m_statusText);
		msgPopupCnt.getChildren().clear();
		msgPopupCnt.appendChild(t);
	}

    /**
     *
     * @return current status line text
     */
    public String getStatusLine() {
   		return m_statusText;
   	}
    
    public boolean getStatusError() {
    	return m_statusError;
    }

	private void createPopup() {
		msgPopupCnt = new Div();
		msgPopupCnt.setVflex("1");

		
		msgPopup = new Window();
		msgPopup.setBorder(true);
		msgPopup.setClosable(true);
		msgPopup.setSizable(true);
		msgPopup.setContentStyle("overflow: auto");
        msgPopup.setWidth("500px");
        msgPopup.appendChild(msgPopupCnt);
        msgPopup.setPage(SessionManager.getAppDesktop().getComponent().getPage());
        msgPopup.setShadow(true);
        msgPopupCaption = new Caption();
        msgPopup.appendChild(msgPopupCaption);        
	}

	private void showPopup() {
		msgPopupCaption.setImage(m_statusError ? ERROR_INDICATOR_IMAGE : INFO_INDICATOR_IMAGE);
		LayoutUtils.openOverlappedWindow(messageContainer, msgPopup, "overlap_end");
	}

	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		if (msgPopup != null)
			msgPopup.detach();
	}

	public void setNavigationToolbarVisibility(boolean visible) {
		toolbarContainer.setVisible(visible);
	}

	public boolean hasParentLink() {
		for(Component c : layout.getChildren()) {
			if (c instanceof BreadCrumbLink) {
				return true;
			}
		}
		return false;
	}

}
