/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
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


import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.component.ADTreeOnDropListener;
import org.adempiere.webui.component.SimpleTreeModel;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.panel.TreeSearchPanel;
import org.adempiere.webui.util.TreeUtils;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.MTree_Base;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Tree;

/**
 * 
 * @author hengsin
 *
 */
public class ADTreePanel extends Panel implements EventListener<Event>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2718257463734592729L;
	private static final String ON_EXPAND_MENU_EVENT = "onExpandMenu";
	private TreeSearchPanel pnlSearch;
    private Tree tree;
    
    private ToolBarButton expandToggle; // Elaine 2009/02/27 - expand tree
	private int m_windowno = -1;
	private int m_tabno = -1;
	private int AD_Tree_ID = -1;
    	
    public ADTreePanel()
    {
        init();        
    }
    
    public ADTreePanel(int windowno, int tabno)
    {
    	m_windowno = windowno;
    	m_tabno = tabno;
        init();        
    }
    
    /**
     * @param AD_Tree_ID
     * @param windowNo
     */
    public boolean initTree(int AD_Tree_ID, int windowNo) 
    {
    	return initTree(AD_Tree_ID, windowNo, null, 0);
    }

    public boolean initTree(int AD_Tree_ID, int windowNo, String linkColName, int linkID) 
    {
    	if (this.AD_Tree_ID != AD_Tree_ID)
    	{
	    	this.AD_Tree_ID = AD_Tree_ID;
	    	SimpleTreeModel.initADTree(tree, AD_Tree_ID, windowNo, linkColName, linkID);
	    	MTree_Base tb = MTree_Base.get(Env.getCtx(), AD_Tree_ID, null);
	    	if (tb.isLoadAllNodesImmediately())
	    		TreeUtils.collapseTree(tree, true);
	    	pnlSearch.initialise();
	    	return true;
    	}
    	return false;
    }
    
    private void init()
    {
    	setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "treepanel");
    	ZKUpdateUtil.setWidth(this, "100%");
    	ZKUpdateUtil.setHeight(this, "100%");
    	
        tree = new Tree();
        tree.setMultiple(false);
        ZKUpdateUtil.setWidth(tree, "100%");
        ZKUpdateUtil.setVflex(tree, true);
        tree.setPageSize(-1); // Due to bug in the new paging functionality
        
        tree.setStyle("border: none");
        
        pnlSearch = new TreeSearchPanel(tree, Events.ON_SELECT, m_windowno, m_tabno);
        
        Toolbar toolbar = new Toolbar();
        toolbar.setMold("panel");
        toolbar.appendChild(pnlSearch);
        this.appendChild(toolbar);
        
        Panelchildren pc = new Panelchildren();
        this.appendChild(pc);
        pc.appendChild(tree);  
        
        // Elaine 2009/02/27 - expand tree
        toolbar = new Toolbar();
        toolbar.setMold("panel");
        expandToggle = new ToolBarButton();
        expandToggle.setMode("toggle");
        expandToggle.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "ExpandTree")));
        expandToggle.addEventListener(Events.ON_CHECK, this);
        toolbar.appendChild(expandToggle);
        this.appendChild(toolbar);
        
        this.addEventListener(ON_EXPAND_MENU_EVENT, this);
    }
    
    //F3P
    public void createTree(int AD_Tree_ID, int windowNo) throws Exception
    {
    	MTree vTree = new MTree (Env.getCtx(), AD_Tree_ID, true, true, null);
  		MTreeNode root = vTree.getRoot();
  		
  		SimpleTreeModel treeModel = SimpleTreeModel.createFrom(root);
  		treeModel.setItemDraggable(true);
  		treeModel.addOnDropEventListener(new ADTreeOnDropListener(tree, treeModel, vTree, windowNo));
  		
  		tree.setPageSize(-1);
 			tree.setTreeitemRenderer(treeModel);
 			tree.setModel(treeModel);
 			
    	pnlSearch.initialise();
    	expandOnCheck();
    }
    //F3P end
    
    /**
     * @param event
     * @see EventListener#onEvent(Event)
     */
    public void onEvent(Event event)
    {
        String eventName = event.getName();
        
        // Elaine 2009/02/27 - expand tree
        if (eventName.equals(Events.ON_CHECK) && event.getTarget() == expandToggle)
        {
        	Clients.showBusy(null);
        	Events.echoEvent(ON_EXPAND_MENU_EVENT, this, null);
        }
        else if (eventName.equals(ON_EXPAND_MENU_EVENT)) 
        {
        	expandOnCheck();
        	Clients.clearBusy();
        }
        //
    }
    
    /**
     * @return tree
     */
	public Tree getTree() 
	{
		return tree;
	}
	
	/**
	* expand all node
	*/
	public void expandAll()
	{
		if (!expandToggle.isChecked())
			expandToggle.setChecked(true);
	
		TreeUtils.expandAll(tree);
	}
	
	/**
	 * collapse all node
	 */
	public void collapseAll()
	{
		if (expandToggle.isChecked())
			expandToggle.setChecked(false);
	
		TreeUtils.collapseAll(tree);
	}
	
	/**
	 *  On check event for the expand checkbox
	 */
	private void expandOnCheck()
	{
		if (expandToggle.isChecked())
			expandAll();
		else
			collapseAll();
	}
	//	

	public void prepareForRefresh() {
		this.AD_Tree_ID = -1;
	}

}
