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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Menupopup;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.ToolBar;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.component.ZkCssHelper;
import org.adempiere.webui.event.ToolbarListener;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.WRecordInfo;
import org.compiere.model.DataStatusEvent;
import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Menuitem;

/**
 * @author hengsin
 *
 */
public class BreadCrumb extends Div implements EventListener<Event> {

	//LS: Rule value 
	private static final String RULE_IMG_VALUE = "system:imgB64";
	//LS:image to be copied if there is not a rule
	private static final String IMG_BASE64 ="data:image/png;base64,"
			+ "iVBORw0KGgoAAAANSUhEUgAAABYAAAAWCAYAAADEtGw7AAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAA3XAAAN1wFCKJt4AAAA"
			+ "CXZwQWcAAAAWAAAAFgDcxelYAAAFRElEQVQ4y3WVSWwb5xXHf9/MkOIiiaREyqJEmos21lqcyla8pY6RwK6Nqi2K1GiBOECuQYA2"
			+ "gA/JMYfAPcloj00vXVAjqFE4bWwXlbKgsWrFbYzCaqRYEi3LtihZ1EJyZjQcbjM5RBIUI37AB3yX93t//N/DewLg2dNvDQMf8JTo"
			+ "HOybsG27F2gAVCHENPAh8MdLF86lvy1HbH+ehLsbvF/EUh19B/btpSseIhRooM6hsGmWeLymce/hGpOzi1Rr1m8uXTj3xlPBu+HB"
			+ "9tZHJ55/JnrqSIpKzcJZ0+jt6eD66Me42veT14u4ZQtPncy/b8/x5b3lCSHETy5dOLeyzZKfhLYlY4vDp4eiJwbjFDQVQ1exrSp3"
			+ "Zh4QTvRSKZdwyoKuINjlTeKRPdSQokvZ/LHfj19ZeRdmd8C7bTh1+kjj8PdSqJrGkf1drKwXiHbsI9gSRpIUzKKGKOYwjCK93Qla"
			+ "fC487jpyeimaMan/1fyk/i7MSluChwC6B/t44VA3sdYAQ30djH4yTiDYCkLgdCgoDplGf5BUMszQ/hQN9V5cLhfRgIMDfXH+f/jM"
			+ "j9TG4PcBlC3wCYDBvhid7QEkSfD+P//F4OETCAG2DTYgCQlz/QHjmWUklx8qOseH+lAUmYhfoiu+h8dtyYHP1bXhbcXHQ5E9hY5I"
			+ "ENPQef/6RyT6j2EDlr0FtqFm2SiKk5/+4AWeG4gRbApwa+oBQggUyyQc8qPX+5PAy9tgnK66hqDfy+i0jtySYrNUw6zY2DZYQKVm"
			+ "Yah5DvZ3AtASaub4s/3s7wwzPbfA5HQaX6OHh7F9AYAdsKQ4JYdTobGWpWw70Y0S2UKRDb2MbpRRjRKGuoZt29+Y1+aAj8WVDVqb"
			+ "G3EoMve6D3iAn297jMvjqurFiqIZJRrlJXyKg8U18DS1fW1DrUZJtanVLGR5Rw+madIVDfHXsf/w3UMtdMzcNgH3Nvg9dS13Jruu"
			+ "+h5Xmzkca6AzHqE7X+DvtxaQvSGqNYtS2cWv3/uUYsXixYE9DPTEMQwDhyyRXrWJqwZ7H05vAO07pfV8Yfn+4hr1ddAZjwAQ8Pt4"
			+ "5eQALn2BnGqQ14pochOq7eNO+jGapqHrOh/f/B/t0TjL2TzhTDq/2+M/lwzzxvS9ZeLNLj688TmWZX3tvSSolovk1CI5rUhBMyls"
			+ "mixulCkUCmiahmGYHOqPcf9RlvDyfC/wqQyQSY/PhpNHD3rqvVbFIiF5m6lqK3jddVQqFUYnpsiWXGyaFTaLZfRiCZel0tlSR75Q"
			+ "oKCEmV9cJ/7RlavJ+clu4A/Krgb/9/4XU3cUh3LQ7XY2+J0+fKtrjN64zVS+GQuDas2iXKlRKlfJVWB9I8dq2YNq1FD+cWX5xbE/"
			+ "DW+zdpZQJj0+G2zvH3z05Y2/4Gn7cdmWHcLtY2ZpkxXdRtssY5gVTNNEscskQh5CbTGmFzaofnC58NL13wUBCvDSc/C3b6xNoAVo"
			+ "B9rPvHrhYjAS70rubaE15Gdl7jOOHj3GxMRnNCUPsLKaZ2FxlTfeeXkn+Sa8/gv4LVB7EiwDUaAVaI31HD4UjPT8TEgikmgqO06e"
			+ "PMXY2BjzG0rpmczcxmvTN8Pfcjx+eBCuirNnz3YCCeA7QBJIWJbVI4RoE0J4tydnZGTEBshkMly8eFG8efkyT4u5trZfKufPn1/d"
			+ "UuoBnIAXCAFNgPvu3bvSwsICgFhaWmJmZoZAIMDVt98mHo/bqVTKBB4Ct4BPgMkEZJWRkZGddbH1xFYhwa7TNT8/TzqdJpvNksvl"
			+ "yOVyTE1Nce3aNbElqB7wA42A/hVfBj//cSljqwAAAC56VFh0Y3JlYXRlLWRhdGUAAHjaMzIwsNA1MNc1MgsxNLMytbQyNdc2sLAy"
			+ "MAAAQtEFKhjwRR0AAAAuelRYdG1vZGlmeS1kYXRlAAB42jMyMLDQNTDXNTILMTSzMrW0MjXXNrCwMjAAAELRBSp6yLLiAAAAAElF"
			+ "TkSuQmCC";
	
	
	private static final String ON_MOUSE_OVER_ECHO_EVENT = "onMouseOverEcho";
	
	private static final String ON_MOUSE_OUT_ECHO_EVENT = "onMouseOutEcho";

	/**
	 * generated serial id 
	 */
	private static final long serialVersionUID = 929253935475618911L;
	
	private static final String BTNPREFIX = "Btn";
	
	private Hbox layout;

	private ToolBarButton btnFirst, btnPrevious, btnNext, btnLast, btnRecordInfo;
	
	private LinkedHashMap<String, String> links;

	@SuppressWarnings("unused")
	private int windowNo;
	
	private HashMap<String, ToolBarButton> buttons = new HashMap<String, ToolBarButton>();

	private DataStatusEvent m_dse;

	private String m_text;

	private ToolbarListener toolbarListener;

	private Hlayout toolbarContainer;

	protected Menupopup linkPopup;
	//LS	
	private ToolBarButton btnCopyHtml;
	private Textbox teCopyHtml;
	private boolean connected = false;
	private String imgBase64 = IMG_BASE64;
	//LS End	
		
	/**
	 * 
	 */
	public BreadCrumb(int windowNo) {
		this.windowNo = windowNo;
		layout = new Hbox();
		layout.setPack("start");
		layout.setAlign("center");
		this.appendChild(layout);
		ZKUpdateUtil.setHeight(layout, "100%");
		layout.setStyle("float: left");

		this.setVisible(false);
		this.setSclass("adwindow-breadcrumb");
				
		toolbarContainer = new Hlayout();
		toolbarContainer.setStyle("display: inline-block; float: right");
		this.appendChild(toolbarContainer);
		
		ToolBar toolbar = new ToolBar();
		toolbarContainer.appendChild(toolbar);	
		//LS: Div seems not to work: fallback to TextEdit
		teCopyHtml = new Textbox();
		teCopyHtml.setVisible(false); 
		teCopyHtml.setValue("");
		toolbar.appendChild(teCopyHtml);

		btnCopyHtml = createButton("Copy", "Setup", "Copy link Html");
		toolbar.appendChild(btnCopyHtml);
		//LS End
		btnFirst = createButton("First", "First", "First");
		btnFirst.setTooltiptext(btnFirst.getTooltiptext()+"    Alt+Home");
		toolbar.appendChild(btnFirst);
        btnPrevious = createButton("Previous", "Previous", "Previous");
        btnPrevious.setTooltiptext(btnPrevious.getTooltiptext()+"    Alt+Left");
        toolbar.appendChild(btnPrevious);
        btnRecordInfo = new ToolBarButton();
        btnRecordInfo.setLabel("");
        btnRecordInfo.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Who")));
        btnRecordInfo.addEventListener(Events.ON_CLICK, this);
        btnRecordInfo.setSclass("breadcrumb-record-info link");
        btnRecordInfo.setId("recordInfo");
        toolbar.appendChild(btnRecordInfo);
        btnNext = createButton("Next", "Next", "Next");
        btnNext.setTooltiptext(btnNext.getTooltiptext()+"    Alt+Right");
        toolbar.appendChild(btnNext);
        btnLast = createButton("Last", "Last", "Last");
        btnLast.setTooltiptext(btnLast.getTooltiptext()+"    Alt+End");
        toolbar.appendChild(btnLast);

		toolbar.setStyle("background-image: none; background-color: transparent; border: none;");
		setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "breadcrumb");
		
		this.addEventListener(ON_MOUSE_OUT_ECHO_EVENT, this);
		
		String img = DB.getSQLValueString(null, "SELECT script FROM AD_Rule WHERE value = ?", RULE_IMG_VALUE);
		if(Util.isEmpty(img,true) == false)
			imgBase64 = img;
	}

	/**
	 * 
	 * @param listener
	 */
	public void setToolbarListener(ToolbarListener listener) {
		this.toolbarListener = listener;
	}
	
	/**
	 * 
	 * @param label
	 * @param id
	 * @param clickable
	 */
	public void addPath(String label, String id, boolean clickable) {
		if (clickable) {
			BreadCrumbLink a = new BreadCrumbLink();
			a.setLabel(label);
			a.setId("breadcrumb-"+label);
			a.setPathId(id);
			a.addEventListener(Events.ON_CLICK, this);
			if (layout.getChildren().size() > 0) {
				Label symbol = new Label();
				symbol.setValue(" > ");
				layout.appendChild(symbol);
			}
			layout.appendChild(a);
		} else {
			Label pathLabel = new Label();
			pathLabel.setId("breadcrumb-"+label);
			pathLabel.setValue(label);
			if (layout.getChildren().size() > 0) {
				Label symbol = new Label();
				symbol.setValue(" > ");
				layout.appendChild(symbol);
			}
			pathLabel.setStyle("font-weight: bold");
			layout.appendChild(pathLabel);
		}
	}
	
	/**
	 * 
	 * @return list of parent links
	 */
	public List<BreadCrumbLink> getParentLinks() {
		List<BreadCrumbLink> parents = new ArrayList<BreadCrumbLink>();
		for(Component component : layout.getChildren()) {
			if (component instanceof BreadCrumbLink)
				parents.add((BreadCrumbLink) component);
		}
		return parents;
	}
	
	/**
	 * add links to other tabs at the same level
	 * @param links
	 */
	public void addLinks(LinkedHashMap<String, String> links) {
		this.links = links;
		final Label pathLabel = (Label) layout.getChildren().get(layout.getChildren().size()-1);
		pathLabel.setStyle("cursor: pointer; font-weight: bold; padding-right: 10px;");
		EventListener<Event> listener = new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (linkPopup != null && linkPopup.getPage() != null && linkPopup.isVisible()) {
					if (event.getName().equals(Events.ON_MOUSE_OUT)) {
						linkPopup.setAttribute(ON_MOUSE_OUT_ECHO_EVENT, Boolean.TRUE);
						StringBuilder script = new StringBuilder("setTimeout(function(){var w=zk('#")
							.append(BreadCrumb.this.getUuid()).append("').$();")
							.append("var e=new zk.Event(w, '")
							.append(ON_MOUSE_OUT_ECHO_EVENT)
							.append("', null, {toServer:true});")
							.append("zAu.send(e);},500)");
						final AuScript response = new AuScript(script.toString());
						Clients.response(response);
					}
					return;
				}
				
				if (event.getName().equals(Events.ON_CLICK)) {
					if (linkPopup != null && linkPopup.getPage() != null)
						linkPopup.detach();
					linkPopup = new Menupopup();
					showLinksMenu(pathLabel);
				} else if (event.getName().equals(Events.ON_MOUSE_OVER)) {
					if (linkPopup == null || !linkPopup.isVisible()) {
						if (linkPopup != null && linkPopup.getPage() != null)
							linkPopup.detach();
						linkPopup = new Menupopup();
						StringBuilder script = new StringBuilder("setTimeout(function(){var w=zk('#")
							.append(event.getTarget().getUuid()).append("').$();")
							.append("var e=new zk.Event(w, '")
							.append(ON_MOUSE_OVER_ECHO_EVENT)
							.append("', null, {toServer:true});")
							.append("zAu.send(e);},500)");
						AuScript response = new AuScript(script.toString());
						Clients.response(response);
					}
				} else if (event.getName().equals(Events.ON_MOUSE_OUT)) {
					if (linkPopup != null && linkPopup.getPage() == null) {
						linkPopup = null;
					} 
				} else if (event.getName().equals(ON_MOUSE_OVER_ECHO_EVENT)) {
					if (linkPopup != null && linkPopup.getPage() == null) {
						showLinksMenu(pathLabel);
					}
				}
			}

			private void showLinksMenu(final Label pathLabel) {
				for(Map.Entry<String, String>entry : BreadCrumb.this.links.entrySet()) {
					final Menuitem item = new Menuitem();
					item.setLabel(entry.getValue());
					item.setValue(entry.getKey());
					item.addEventListener(Events.ON_CLICK, BreadCrumb.this);
					linkPopup.appendChild(item);
				}
				
				StringBuilder script = new StringBuilder("setTimeout(function(){var w=zk('#")
					.append(BreadCrumb.this.getUuid()).append("').$();")
					.append("var e=new zk.Event(w, '")
					.append(ON_MOUSE_OUT_ECHO_EVENT)
					.append("', null, {toServer:true});")
					.append("zAu.send(e);},500)");
				final AuScript response = new AuScript(script.toString());
				linkPopup.addEventListener(Events.ON_MOUSE_OUT, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (linkPopup != null) {							
							linkPopup.setAttribute(ON_MOUSE_OUT_ECHO_EVENT, Boolean.TRUE);
							Clients.response(response);
						}
					}
				});
				linkPopup.addEventListener(Events.ON_MOUSE_OVER, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (linkPopup != null && linkPopup.isVisible()) {
							linkPopup.removeAttribute(ON_MOUSE_OUT_ECHO_EVENT);
						}						
					}					
				});
				linkPopup.setPage(pathLabel.getPage());
				linkPopup.open(pathLabel);								
			}
		};
		pathLabel.addEventListener(Events.ON_CLICK, listener);
		pathLabel.addEventListener(Events.ON_MOUSE_OVER, listener);
		pathLabel.addEventListener(Events.ON_MOUSE_OUT, listener);
		pathLabel.addEventListener(ON_MOUSE_OVER_ECHO_EVENT, listener);
		ZkCssHelper.appendStyle(pathLabel, "background: transparent url('theme/" + ThemeManager.getTheme() + 
				"/images/downarrow.png') no-repeat right center");
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
		} else if (event.getName().equals(ON_MOUSE_OUT_ECHO_EVENT)) {
			if (linkPopup != null && linkPopup.getPage() != null && linkPopup.isVisible()
				&& linkPopup.getAttribute(ON_MOUSE_OUT_ECHO_EVENT) != null) {
				linkPopup.detach();
				linkPopup = null;
			}
		} else if (event.getName().equals(Events.ON_CTRL_KEY)) {
			if (!LayoutUtils.isReallyVisible(this)) return;
			
			KeyEvent keyEvent = (KeyEvent) event;
			if (keyEvent.isAltKey()) {
				if (keyEvent.getKeyCode() == KeyEvent.LEFT) {
					if (toolbarListener != null) {
						toolbarListener.onPrevious();
					}
				} else if (keyEvent.getKeyCode() == KeyEvent.RIGHT) {
					if (toolbarListener != null) {
						toolbarListener.onNext();
					}
				} else if (keyEvent.getKeyCode() == KeyEvent.HOME) {
					if (toolbarListener != null) {
						toolbarListener.onFirst();
					}
				} else if (keyEvent.getKeyCode() == KeyEvent.END) {
					if (toolbarListener != null) {
						toolbarListener.onLast();
					}
				}
			}
		} else {
			Events.sendEvent(this, event);
		}
	}

	/**
	 * remove all links
	 */
	public void reset() {
		layout.getChildren().clear();
		this.links = null;
	}

	/**
	 * enable/disable first record and previous record toolbar button 
	 * @param enabled
	 */
    public void enableFirstNavigation(boolean enabled)
    {
        this.btnFirst.setDisabled(!enabled);
        this.btnPrevious.setDisabled(!enabled);
    }

    /**
     * enable or disable the next record and last record toolbar button
     * @param enabled
     */
    public void enableLastNavigation(boolean enabled)
    {
        this.btnLast.setDisabled(!enabled);
        this.btnNext.setDisabled(!enabled);
    }

    /**
     * enable or disable the copy record html button
     * @param enabled
     */
    public void enableButtonCopyHtml(boolean enabled)
    {
        this.btnCopyHtml.setDisabled(!enabled);
    }
    
	private ToolBarButton createButton(String name, String image, String tooltip)
    {
    	ToolBarButton btn = new ToolBarButton("");
        btn.setName(BTNPREFIX+name);
        btn.setId(name);
        Executions.createComponents(ThemeManager.getPreference(), this, null);
        String size = Env.getContext(Env.getCtx(), "#ZK_Toolbar_Button_Size");
    	String suffix = "24.png";
    	if (!Util.isEmpty(size)) 
    	{
    		suffix = size + ".png";
    	}
        btn.setImage(ThemeManager.getThemeResource("images/"+image + suffix));
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
        	if (text.indexOf("-1/0") >= 0)
        		text = text.replace("-1/0", "0/0");
        	btnRecordInfo.setLabel(text);
        }

        m_text = text;
        m_dse = dse;
        if (m_dse != null) {
        	enableFirstNavigation(m_dse.getCurrentRow() > 0);
        	enableLastNavigation(m_dse.getTotalRows() > m_dse.getCurrentRow()+1);
        	
        	//LS
        	if(connected == false)
        	{//Any better moment to do this?
        		connectButtonCopyHTML(btnCopyHtml.getUuid(),teCopyHtml.getUuid());
        		connected= true;
        	}
        	setRecordLinkHTML(dse.AD_Table_ID, dse.Record_ID);
        	enableButtonCopyHtml(m_dse.getTotalRows() > 0);
        	//LS End
        }
    }
        
	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		if (linkPopup != null)
			linkPopup.detach();
		
		try {
			SessionManager.getSessionApplication().getKeylistener().removeEventListener(Events.ON_CTRL_KEY, this);
		} catch (Exception e){}
	}

	/**
	 * 
	 * @param visible
	 */
	public void setNavigationToolbarVisibility(boolean visible) {
		toolbarContainer.setVisible(visible);
	}

	/**
	 * @return true if there are one or more parent link
	 */
	public boolean hasParentLink() {
		for(Component c : layout.getChildren()) {
			if (c instanceof BreadCrumbLink) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		if (newpage != null) {
			SessionManager.getSessionApplication().getKeylistener().addEventListener(Events.ON_CTRL_KEY, this);
		}
	}
	
	public boolean isPreviousEnabled() {
		return !btnPrevious.isDisabled();
	}
	
	public boolean isNextEnabled() {
		return !btnNext.isDisabled();
	}
	
	public ToolBarButton getNextButton() {
		return btnNext;
	}
	
	public ToolBarButton getPreviousButton() {
		return btnPrevious;
	}
	
	public boolean isEmpty() {
		return layout == null || layout.getChildren().isEmpty();
	}
	
	//LS
	/** Connect zk.Widget with uuid = @Identifier to listener that copy html from div with uuid = divIdentifier to clipboard
	 * JS needed for client side copy to clipboard 
	 */
	private static void connectButtonCopyHTML(String btnIdentifier, String divIdentifier)
	{
		String js = "{"
				+ "zk.Widget.$('"+btnIdentifier+"').listen("
				+ "		{onClick: function copyToClpbrd(e) "
				+ "			{ "
				+ "				const container = document.createElement('div');"
				+ "				const te = zk.Widget.$('"+divIdentifier+"');"
				//Set div content (te.value need to be HTML)
				+ "				container.innerHTML = te.getValue();"
				//LS: Invisible div is copied as it is: invisible, so is printed as it is: invisible
//				+ "				container.style.position = 'fixed';"
//				+ "  			container.style.pointerEvents = 'none';"
//				+ "  			container.style.opacity = 0;"
				
				+ "				var activeSheets = Array.prototype.slice.call(document.styleSheets)"
				+ "				    .filter(function (sheet) {"
				+ "				      return !sheet.disabled"
				+ "				    });"
				+ "				document.body.appendChild(container);"
				+ "				window.getSelection().removeAllRanges();"
				+ "				var range = document.createRange();"
				+ "				range.selectNode(container);"
				+ "				window.getSelection().addRange(range);"
				//Copy
				+ "				document.execCommand('copy');"
				+ "				for (var i = 0; i < activeSheets.length; i++) activeSheets[i].disabled = true;"
				+ "				document.execCommand('copy');"
				+ "				for (var i = 0; i < activeSheets.length; i++) activeSheets[i].disabled = false;"
				//Remove div
				+ "				document.body.removeChild(container);"
				+ "		}});"
				+ "}";		
		Clients.evalJavaScript(js);
	}
	
	private void setRecordLinkHTML(int AD_Table_ID, Object Record_ID)
	{
		int record_id = -1;
		if(Record_ID!= null)
			record_id = (int)Record_ID;
		setRecordLinkHTML(AD_Table_ID, record_id);
	}

	private void setRecordLinkHTML(int AD_Table_ID, int Record_ID)
	{
		String htmlText = "";	
		if(AD_Table_ID>0 && Record_ID>0)
			htmlText = getRecordLink(AD_Table_ID, imgBase64, Record_ID);
		teCopyHtml.setValue(htmlText);
	}
	
	private static String getRecordLink(int AD_Table_ID, String imgBase64,int Record_ID)
	{

		MessageFormat fmt = new MessageFormat("<a href=\"{0}\" target=\"_blank\"><img src=\"{1}\" alt=\"Link to Document\"></a>");
		String params[] = {AEnv.getZoomUrlTableID(AD_Table_ID, Record_ID), imgBase64};			
		String htmlToCopy = fmt.format(params);
		return htmlToCopy;
	}
	//LS End
}
