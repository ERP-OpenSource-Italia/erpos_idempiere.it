/******************************************************************************
 * Copyright (C) 2012 Elaine Tan                                              *
 * Copyright (C) 2012 Trek Global
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

package org.adempiere.webui.panel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Style;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;

/**
 * Menu Search Panel
 * @author Elaine
 * @date July 31, 2012
 */
public class MenuSearchPanel extends AbstractMenuPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5308522340852904168L;
	
	
	
	private TreeSearchPanel pnlSearch;
	private MenuTreeFilterPanel filterPanel;
	private Toolbarbutton filterBtn;
    
    public MenuSearchPanel(Component parent)
    {
    	super(parent);
    }

	protected void init() 
	{
		super.init();
        pnlSearch.initialise();
	}
    
    protected void initComponents()
    {
    	super.initComponents();
    	
    	Toolbar toolbar = new Toolbar();
        toolbar.setMold("panel");
        this.appendChild(toolbar);
        
        pnlSearch = new TreeSearchPanel(getMenuTree());
        Style style = new Style();
        style.setContent(".z-comboitem-img{ vertical-align:top; padding-right:2px; padding-bottom:4px; }");
        pnlSearch.insertBefore(style, pnlSearch.getFirstChild());        
        toolbar.appendChild(pnlSearch);
        filterBtn = new Toolbarbutton();
        filterBtn.setImage("/images/Preference16.png");
        filterBtn.addEventListener(Events.ON_CLICK, this);
        toolbar.appendChild(filterBtn);
        
        Panelchildren pc = new Panelchildren();
        this.appendChild(pc);
        filterPanel = new MenuTreeFilterPanel(getMenuTree(), pnlSearch);
        pc.appendChild(filterPanel);
    }
    
    public void onEvent(Event event)
    {
    	super.onEvent(event);
    	
    	if (event.getName().equals(Events.ON_CLICK) && event.getTarget() == filterBtn)
    		filterPanel.open(filterBtn);
    }
}