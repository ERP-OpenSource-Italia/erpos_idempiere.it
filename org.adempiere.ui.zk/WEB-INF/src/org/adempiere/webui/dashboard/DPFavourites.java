/******************************************************************************
 * Copyright (C) 2008 Elaine Tan                                              *
 * Copyright (C) 2008 Idalica Corporation
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
package org.adempiere.webui.dashboard;

import java.util.Enumeration;

import org.adempiere.exceptions.DBException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.event.TouchEventHelper;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Box;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vbox;

/**
 * Dashboard item: User favourites
 * @author Elaine
 * @date November 20, 2008
 */
public class DPFavourites extends DashboardPanel implements EventListener<Event> {

	private static final String ON_ADD_TAP_EVENT_LISTENER = "onAddTapEventListener";

	private static final String NODE_ID_ATTR = "Node_ID";

	/**
	 * 
	 */
	private static final long serialVersionUID = -481859785800845108L;

	public static final String FAVOURITE_DROPPABLE = "favourite";

	public static final String DELETE_FAV_DROPPABLE = "deleteFav";

	private Box bxFav;
	
	private Label lblMsg;
	
	private int m_AD_Tree_ID;
		
	public DPFavourites()
	{
		super();
		
		Panel panel = new Panel();
		this.appendChild(panel);
		
		Panelchildren favContent = new Panelchildren();
		panel.appendChild(favContent);
		bxFav = new Vbox();
		favContent.appendChild(bxFav);
		createFavouritesPanel();
		
		Toolbar favToolbar = new Toolbar();
		this.appendChild(favToolbar);
		
		// Elaine 2008/07/24
		Image img = new Image("/images/Delete24.png");
		favToolbar.appendChild(img);
		img.setAlign("right");
		img.setDroppable(DELETE_FAV_DROPPABLE);
		img.addEventListener(Events.ON_DROP, this);
		//
        
        favContent.setDroppable(FAVOURITE_DROPPABLE); 
        favContent.addEventListener(Events.ON_DROP, this);
	}
	
	private void createFavouritesPanel()
	{
		int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
		int AD_Tree_ID = DB.getSQLValue(null,
			"SELECT COALESCE(r.AD_Tree_Menu_ID, ci.AD_Tree_Menu_ID)" 
			+ "FROM AD_ClientInfo ci" 
			+ " INNER JOIN AD_Role r ON (ci.AD_Client_ID=r.AD_Client_ID) "
			+ "WHERE AD_Role_ID=?", AD_Role_ID);
		if (AD_Tree_ID <= 0)
			AD_Tree_ID = 10;	//	Menu
		
		m_AD_Tree_ID = AD_Tree_ID;
		
		MTree vTree = new MTree(Env.getCtx(), AD_Tree_ID, false, true, null);
		MTreeNode m_root = vTree.getRoot();
		Enumeration<?> enTop = m_root.children();
		while(enTop.hasMoreElements())
		{
			MTreeNode ndTop = (MTreeNode)enTop.nextElement();
			Enumeration<?> en = ndTop.preorderEnumeration();
			while (en.hasMoreElements())
			{
				MTreeNode nd = (MTreeNode)en.nextElement();
				if (nd.isOnBar()) {
					String label = nd.toString().trim();
					A btnFavItem = new A();
					btnFavItem.setAttribute(NODE_ID_ATTR, String.valueOf(nd.getNode_ID()));
					bxFav.appendChild(btnFavItem);
					btnFavItem.setLabel(label);
					btnFavItem.setImage(getIconFile(nd));
					btnFavItem.setDraggable(DELETE_FAV_DROPPABLE);
					btnFavItem.addEventListener(Events.ON_CLICK, this);
					btnFavItem.addEventListener(Events.ON_DROP, this);
					btnFavItem.setSclass("menu-href");
										
					if (getPage() != null)
					{
						TouchEventHelper.addOnTapEventListener(btnFavItem, this);
					}
					else
					{
						Executions.schedule(AEnv.getDesktop(), this, new Event(ON_ADD_TAP_EVENT_LISTENER, btnFavItem, null));
					}
				}
			}
		}
		
		lblMsg = new Label("(Drag and drop menu item here)"); 
		if(bxFav.getChildren().isEmpty()) bxFav.appendChild(lblMsg);
	}
	
    /**
	 *	Make Bar add/remove persistent
	 *  @param add true if add - otherwise remove
	 *  @param Node_ID Node ID
	 *  @return true if updated
	 */
    private boolean barDBupdate(boolean add, int Node_ID)
	{
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Org_ID");
		int AD_User_ID = Env.getContextAsInt(Env.getCtx(), "#AD_User_ID");
		StringBuffer sql = new StringBuffer();
		if (add)
			sql.append("INSERT INTO AD_TreeBar "
				+ "(AD_Tree_ID,AD_User_ID,Node_ID, "
				+ "AD_Client_ID,AD_Org_ID, "
				+ "IsActive,Created,CreatedBy,Updated,UpdatedBy)VALUES (")
				.append(m_AD_Tree_ID).append(",").append(AD_User_ID).append(",").append(Node_ID).append(",")
				.append(AD_Client_ID).append(",").append(AD_Org_ID).append(",")
				.append("'Y',SysDate,").append(AD_User_ID).append(",SysDate,").append(AD_User_ID).append(")");
			//	if already exist, will result in ORA-00001: unique constraint (ADEMPIERE.AD_TREEBAR_KEY)
		else
			sql.append("DELETE AD_TreeBar WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
				.append(" AND AD_User_ID=").append(AD_User_ID)
				.append(" AND Node_ID=").append(Node_ID);
		int no = DB.executeUpdate(sql.toString(), false, null);
		return no == 1;
	}
        
    public void onEvent(Event event)
    {
        Component comp = event.getTarget();
        String eventName = event.getName();
        
        if (eventName.equals(TouchEventHelper.ON_TAP))
        {
        	doOnClick(comp);
        }
        else if(eventName.equals(Events.ON_CLICK) && !TouchEventHelper.isIgnoreClick(comp))
        {
            doOnClick(comp);
        }
        // Elaine 2008/07/24
        else if(eventName.equals(Events.ON_DROP))
        {
        	DropEvent de = (DropEvent) event;
    		Component dragged = de.getDragged();
        	
        	if(comp instanceof Panelchildren)
        	{
        		if(dragged instanceof Treerow)
        		{
        			Treerow treerow = (Treerow) dragged;
        			Treeitem treeitem = (Treeitem) treerow.getParent();
        			
        			addItem(treeitem);
        		}
        	}
        	else if(comp instanceof Image)
        	{
        		if(dragged instanceof A)
        		{
        			A btn = (A) dragged;
        			removeLink(btn);
        		}
        	}
        }
        else if (eventName.equals(ON_ADD_TAP_EVENT_LISTENER))
        {
        	TouchEventHelper.addOnTapEventListener(event.getTarget(), this);
        }
        //
	}

	private void doOnClick(Component comp) {
		if(comp instanceof A)
		{
			A btn = (A) comp;
			
			int menuId = 0;
			try
			{
				menuId = Integer.valueOf((String)btn.getAttribute(NODE_ID_ATTR));            		
			}
			catch (Exception e) {
				
			}
			
			if(menuId > 0) SessionManager.getAppDesktop().onMenuSelected(menuId);
		}
	}

	private void removeLink(A btn) {
		String value = (String) btn.getAttribute(NODE_ID_ATTR);
		
		if(value != null)
		{
			int Node_ID = Integer.valueOf(value.toString());
			if(barDBupdate(false, Node_ID))
			{
				bxFav.removeChild(btn);
				
				if(bxFav.getChildren().isEmpty())
					bxFav.appendChild(lblMsg);
				
				bxFav.invalidate();
			}
		}
	}

    /**
     * Add menu treeitem into the user favorite panel
     * @param treeitem
     */
	public void addItem(Treeitem treeitem) {
		Object value = treeitem.getValue();
		if(value != null)
		{
			int Node_ID = Integer.valueOf(value.toString());
			if(barDBupdate(true, Node_ID))
			{
				String label = null;
				String image = null;
				if (treeitem.getLabel() != null && treeitem.getLabel().trim().length() > 0)
				{
					label = treeitem.getLabel().trim();
					image = treeitem.getImage();
				}
				else
				{
					A link = (A) treeitem.getTreerow().getFirstChild().getFirstChild();
					label = link.getLabel();
					image = link.getImage();
				}
				A btnFavItem = new A();
				bxFav.appendChild(btnFavItem);
				btnFavItem.setAttribute(NODE_ID_ATTR, String.valueOf(Node_ID));
				btnFavItem.setLabel(label);
				btnFavItem.setImage(image);
				btnFavItem.setDraggable(DELETE_FAV_DROPPABLE);
				btnFavItem.addEventListener(Events.ON_CLICK, this);
				btnFavItem.addEventListener(Events.ON_DROP, this);				
				btnFavItem.setSclass("menu-href");
				if (getPage() != null)
				{
					TouchEventHelper.addOnTapEventListener(btnFavItem, this);
				}
				else
				{
					Executions.schedule(AEnv.getDesktop(), this, new Event(ON_ADD_TAP_EVENT_LISTENER, btnFavItem, null));
				}
				bxFav.removeChild(lblMsg);        					
				bxFav.invalidate();
			} else {
				if (DBException.isUniqueContraintError(CLogger.retrieveException()))
					FDialog.error(0, this, "BookmarkExist", null);
			}
		}
	}
	
	private String getIconFile(MTreeNode mt) {
		if (mt.isWindow())
			return "images/mWindow.png";
		if (mt.isReport())
			return "images/mReport.png";
		if (mt.isProcess())
			return "images/mProcess.png";
		if (mt.isWorkFlow())
			return "images/mWorkFlow.png";
		return "images/mWindow.png";
	}
}
