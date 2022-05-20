/******************************************************************************
 * Copyright (C) 2008 Low Heng Sin                                            *
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
package org.adempiere.webui.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.webui.ClientInfo;
import org.adempiere.webui.panel.TreeSearchPanel;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.event.TreeDataEvent;

/**
 * 
 * @author Low Heng Sin
 *
 */
public class SimpleTreeModel extends org.zkoss.zul.DefaultTreeModel<Object> implements TreeitemRenderer<Object>, EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4945968834244672653L;

	private static final CLogger logger = CLogger.getCLogger(SimpleTreeModel.class);
	
	private boolean itemDraggable;
	private List<EventListener<Event>> onDropListners = new ArrayList<EventListener<Event>>();

	public SimpleTreeModel(DefaultTreeNode<Object> root) {
		super(root);
	}
	
	/**
	 * @param tree
	 * @param AD_Tree_ID
	 * @param windowNo
	 * @return SimpleTreeModel
	 */
	public static SimpleTreeModel initADTree(Tree tree, int AD_Tree_ID, int windowNo) {
		return initADTree(tree, AD_Tree_ID, windowNo, true, null);
	}

	public static SimpleTreeModel initADTree(Tree tree, int AD_Tree_ID, int windowNo, String linkColName, int linkID) {
		return initADTree(tree, AD_Tree_ID, windowNo, true, null, linkColName, linkID);
	}
	
	/**
	 * @param tree
	 * @param AD_Tree_ID
	 * @param windowNo
	 * @param editable
	 * @param trxName
	 * @return SimpleTreeModel
	 */
	public static SimpleTreeModel initADTree(Tree tree, int AD_Tree_ID, int windowNo, boolean editable, String trxName) {
		return initADTree(tree, AD_Tree_ID, windowNo, editable, trxName, null, 0);
	}

	public static SimpleTreeModel initADTree(Tree tree, int AD_Tree_ID, int windowNo, boolean editable, String trxName, String linkColName, int linkID) {
		MTree vTree = new MTree (Env.getCtx(), AD_Tree_ID, editable, true, trxName, linkColName, linkID);
		MTreeNode root = vTree.getRoot();
		SimpleTreeModel treeModel = SimpleTreeModel.createFrom(root);
		treeModel.setItemDraggable(true);
		treeModel.setTreeDrivenByValue(vTree.isTreeDrivenByValue());
		treeModel.setIsValueDisplayed(vTree.isValueDisplayed());
		treeModel.addOnDropEventListener(new ADTreeOnDropListener(tree, treeModel, vTree, windowNo));

		if (tree.getTreecols() == null)
		{
				Treecols treeCols = new Treecols();
				tree.getTreecols();
				tree.appendChild(treeCols);
				Treecol treeCol = new Treecol();
				treeCols.appendChild(treeCol);
		}

		tree.setPageSize(-1);
		try {
			tree.setItemRenderer(treeModel);
			tree.setModel(treeModel);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to setup tree");
		}
		
		return treeModel;
	}

	private boolean isTreeDrivenByValue = false;

	public boolean isTreeDrivenByValue() {
		return isTreeDrivenByValue;
	}

	public void setTreeDrivenByValue(boolean isTreeDrivenByValue) {
		this.isTreeDrivenByValue = isTreeDrivenByValue;
	}

	private boolean isValueDisplayed = false;

	public boolean isValueDisplayed() {
		return isValueDisplayed;
	}

	public void setIsValueDisplayed(boolean isValueDisplayed) {
		this.isValueDisplayed = isValueDisplayed;
	}

	/**
	 * 
	 * @param root
	 * @return SimpleTreeModel
	 */
	public static SimpleTreeModel createFrom(MTreeNode root) {
		SimpleTreeModel model = null;
		Enumeration<?> nodeEnum = root.children();
	    
		DefaultTreeNode<Object> stRoot = new DefaultTreeNode<Object>(root, nodeEnum.hasMoreElements() ? new ArrayList<TreeNode<Object>>() : null);
        while(nodeEnum.hasMoreElements()) {
        	MTreeNode childNode = (MTreeNode)nodeEnum.nextElement();
        	DefaultTreeNode<Object> stNode = childNode.getChildCount() > 0 ? new DefaultTreeNode<Object>(childNode,  new ArrayList<TreeNode<Object>>()) 
        		: new DefaultTreeNode<Object>(childNode); 
        	stRoot.getChildren().add(stNode);
        	if (childNode.getChildCount() > 0) {
        		populate(stNode, childNode);
        	}
        }
        model = new SimpleTreeModel(stRoot);
		return model;
	}

	private static void populate(DefaultTreeNode<Object> stNode, MTreeNode root) {
		Enumeration<?> nodeEnum = root.children();
		while(nodeEnum.hasMoreElements()) {
			MTreeNode childNode = (MTreeNode)nodeEnum.nextElement();
			DefaultTreeNode<Object> stChildNode = childNode.getChildCount() > 0 ? new DefaultTreeNode<Object>(childNode, new ArrayList<TreeNode<Object>>())
				: new DefaultTreeNode<Object>(childNode);
			stNode.getChildren().add(stChildNode);
			if (childNode.getChildCount() > 0) {
				populate(stChildNode, childNode);
			}
		}
	}

	/**
	 * @param ti
	 * @param node
	 */
	@Override
	public void render(Treeitem ti, Object node, int index) {
		Treecell tc = new Treecell(Objects.toString(node));
		Treerow tr = null;
		if(ti.getTreerow()==null){
			tr = new Treerow();			
			tr.setParent(ti);
			if (isItemDraggable()) {
				//use different approach on mobile, dnd not working well
				if (ClientInfo.isMobile()) {
					tr.setAttribute(TreeSearchPanel.TREE_ROW_MOVABLE, Boolean.TRUE);
				} else {
					tr.setDraggable("true");
				}
			}
			if (!onDropListners.isEmpty()) {
				tr.setDroppable("true");
				tr.addEventListener(Events.ON_DROP, this);
			}

			// Color
			Object data = ((DefaultTreeNode<?>) node).getData();
			if (data instanceof MTreeNode) {
				final MTreeNode mNode = (MTreeNode) data;
				Color color = mNode.getColor();
				if (color != null){				
					String hex = ZkCssHelper.createHexColorString(color);
					ZkCssHelper.appendStyle(tc, "color: #" + hex);
				}				
				ti.setTooltiptext(mNode.getDescription());
				if (mNode.isSummary())
		    		ZkCssHelper.appendStyle(tc, "font-weight: bold");
			}
			// End color
		}else{
			tr = ti.getTreerow(); 
			tr.getChildren().clear();
		}				
		tc.setParent(tr);
		
		ti.setValue(node);
	}

	/**
	 * Add to root
	 * @param newNode
	 */
	public void addNode(DefaultTreeNode<Object> newNode) {
		DefaultTreeNode<Object> root =  getRoot();
		root.getChildren().add(newNode);
	}

	@Override
	public DefaultTreeNode<Object> getRoot() {
		return (DefaultTreeNode<Object>) super.getRoot();
	}

	/**
	 * @param treeNode
	 */
	public void removeNode(DefaultTreeNode<Object> treeNode) {
		int path[] = this.getPath(treeNode);
		
		if (path != null && path.length > 0) {
			DefaultTreeNode<Object> parentNode = getRoot();
			int index = path.length - 1;
			for (int i = 0; i < index; i++) {
				parentNode = (DefaultTreeNode<Object>) getChild((TreeNode<Object>)parentNode, path[i]);
			}
			
			
			parentNode.getChildren().remove(path[index]);			
		}
	}
	
	/**
	 * @param b
	 */
	public void setItemDraggable(boolean b) {
		itemDraggable = b;
	}
	
	/**
	 * @return boolean
	 */
	public boolean isItemDraggable() {
		return itemDraggable;
	}
	
	/**
	 * @param listener
	 */
	public void addOnDropEventListener(EventListener<Event> listener) {
		onDropListners.add(listener);
	}

	/**
	 * @param event
	 * @see EventListener#onEvent(Event)
	 */
	public void onEvent(Event event) throws Exception {
		if (Events.ON_DROP.equals(event.getName())) {
			for (EventListener<Event> listener : onDropListners) {
				listener.onEvent(event);
			}
		}
	}

	/**
	 * @param treeNode
	 * @return DefaultTreeNode
	 */
	public DefaultTreeNode<Object> getParent(DefaultTreeNode<Object> treeNode) {
		int path[] = this.getPath(treeNode);
		
		if (path != null && path.length > 0) {
			DefaultTreeNode<Object> parentNode = getRoot();
			int index = path.length - 1;
			for (int i = 0; i < index; i++) {
				parentNode = (DefaultTreeNode<Object>) getChild((TreeNode<Object>)parentNode, path[i]);
			}
						
			return parentNode;
		}
		
		return null;
	}

	/**
	 * @param newParent
	 * @param newNode
	 * @param index
	 * @return parent node. this could be a new instance created to replace the newParent node param
	 */
	public DefaultTreeNode<Object> addNode(DefaultTreeNode<Object> newParent, DefaultTreeNode<Object> newNode,
			int index) {
		DefaultTreeNode<Object> parent = newParent;
		if (newParent.getChildren() == null) {
			parent = new DefaultTreeNode<Object>(newParent.getData(), new ArrayList<TreeNode<Object>>());
			newParent.getParent().insert(parent, newParent.getParent().getIndex(newParent));
			removeNode(newParent);
		}
		
		parent.getChildren().add(index, newNode);
		return parent;
	}
	
	/**
	 * @param fromNode
	 * @param recordId
	 * @return DefaultTreeNode
	 */
	public DefaultTreeNode<Object> find(DefaultTreeNode<Object> fromNode, int recordId) {
		if (fromNode == null)
			fromNode = getRoot();
		MTreeNode data = (MTreeNode) fromNode.getData();
		if (data.getNode_ID() == recordId) 
			return fromNode;
		if (isLeaf(fromNode)) 
			return null;
		int cnt = getChildCount(fromNode);
		for(int i = 0; i < cnt; i++ ) {
			DefaultTreeNode<Object> child = (DefaultTreeNode<Object>) getChild(fromNode, i);
			DefaultTreeNode<Object> treeNode = find(child, recordId);
			if (treeNode != null)
				return treeNode;
		}
		return null;
	}
	
	/**
	 * @param node
	 */
	public void nodeUpdated(DefaultTreeNode<Object> node) {
		DefaultTreeNode<Object> parent = getParent(node);
		if (parent != null) {
			int i = parent.getChildren().indexOf(node);
			fireEvent(TreeDataEvent.CONTENTS_CHANGED, getPath(parent), i, i);
		}
	}
}
