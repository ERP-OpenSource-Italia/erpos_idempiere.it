/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.apps.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.apps.ADialog;
import org.compiere.grid.tree.VTreePanel;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *	Tree Maintenance
 *	
 *  @author Jorg Janke
 *  @version $Id: VTreeMaintenance.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class VTreeMaintenance extends TreeMaintenance
	implements FormPanel, ActionListener, ListSelectionListener, PropertyChangeListener
{
	private CPanel panel = new CPanel();
	
	/**	FormFrame				*/
	private FormFrame 		m_frame;	
	
	private BorderLayout	mainLayout	= new BorderLayout ();
	private CPanel 			northPanel	= new CPanel ();
	private FlowLayout		northLayout	= new FlowLayout ();
	private CLabel			treeLabel	= new CLabel ();
	private CComboBox<Object>		treeField;
	private CButton			bAddAll		= new CButton (Env.getImageIcon("FastBack24.gif"));
	private CButton			bAdd		= new CButton (Env.getImageIcon("StepBack24.gif"));
	private CButton			bDelete		= new CButton (Env.getImageIcon("StepForward24.gif"));
	private CButton			bDeleteAll	= new CButton (Env.getImageIcon("FastForward24.gif"));
	private CCheckBox		cbAllNodes	= new CCheckBox ();
	private CLabel			treeInfo	= new CLabel ();
	//
	private JSplitPane		splitPane	= new JSplitPane ();
	private VTreePanel		centerTree;
	private JList<Object>			centerList	= new JList<Object> ();

	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		if (log.isLoggable(Level.INFO)) log.info( "VMerge.init - WinNo=" + m_WindowNo);
		try
		{
			preInit();
			jbInit ();
			frame.getContentPane().add(panel, BorderLayout.CENTER);
		//	frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			action_loadTree();
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "VTreeMaintenance.init", ex);
		}
	}	//	init
	
	/**
	 * 	Fill Tree Combo
	 */
	private void preInit()
	{
		treeField = new CComboBox<Object>(getTreeData());
		treeField.addActionListener(this);
		//
		centerTree = new VTreePanel (m_WindowNo, false, true);
		centerTree.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
	}	//	preInit
	
	/**
	 * 	Static init
	 *	@throws Exception
	 */
	private void jbInit () throws Exception
	{
		panel.setLayout (mainLayout);
		treeLabel.setText (Msg.translate(Env.getCtx(), "AD_Tree_ID"));
		cbAllNodes.setEnabled (false);
		cbAllNodes.setText (Msg.translate(Env.getCtx(), "IsAllNodes"));
		treeInfo.setText (" ");
		bAdd.setToolTipText(Msg.getMsg(Env.getCtx(), "AddToTree"));
		bAddAll.setToolTipText(Msg.getMsg(Env.getCtx(), "AddAllToTree"));
		bDelete.setToolTipText(Msg.getMsg(Env.getCtx(), "DeleteFromTree"));
		bDeleteAll.setToolTipText(Msg.getMsg(Env.getCtx(), "DeleteAllFromTree"));
		bAdd.addActionListener(this);
		bAddAll.addActionListener(this);
		bDelete.addActionListener(this);
		bDeleteAll.addActionListener(this);
		northPanel.setLayout (northLayout);
		northLayout.setAlignment (FlowLayout.LEFT);
		//
		panel.add (northPanel, BorderLayout.NORTH);
		northPanel.add (treeLabel, null);
		northPanel.add (treeField, null);
		northPanel.add (cbAllNodes, null);
		northPanel.add (treeInfo, null);
		northPanel.add (bAddAll, null);
		northPanel.add (bAdd, null);
		northPanel.add (bDelete, null);
		northPanel.add (bDeleteAll, null);
		//
		panel.add (splitPane, BorderLayout.CENTER);
		splitPane.add (centerTree, JSplitPane.LEFT);
		splitPane.add (new JScrollPane(centerList), JSplitPane.RIGHT);
		centerList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		centerList.addListSelectionListener(this);
	}	//	jbInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == treeField)
			action_loadTree();
		else if (e.getSource() == bAddAll)
			action_treeAddAll();
		else if (e.getSource() == bAdd)
			action_treeAdd((ListItem)centerList.getSelectedValue());
		else if (e.getSource() == bDelete)
			action_treeDelete((ListItem)centerList.getSelectedValue());
		else if (e.getSource() == bDeleteAll)
			action_treeDeleteAll();
	}	//	actionPerformed

	
	/**
	 * 	Action: Fill Tree with all nodes
	 */
	private void action_loadTree()
	{
		KeyNamePair tree = (KeyNamePair)treeField.getSelectedItem();
		if (log.isLoggable(Level.INFO)) log.info("Tree=" + tree);
		if (tree.getKey() <= 0)
		{
			centerList.setModel(new DefaultListModel<Object>());
			return;
		}
		//	Tree
		m_tree = new MTree (Env.getCtx(), tree.getKey(), null);
		cbAllNodes.setSelected(m_tree.isAllNodes());
		bAddAll.setEnabled(!m_tree.isAllNodes());
		bAdd.setEnabled(!m_tree.isAllNodes());
		bDelete.setEnabled(!m_tree.isAllNodes());
		bDeleteAll.setEnabled(!m_tree.isAllNodes());
		//
		
		//	List
		DefaultListModel<Object> model = new DefaultListModel<Object>();
		ArrayList<ListItem> items = getTreeItemData();
		for(ListItem item : items)
			model.addElement(item);
		
		//	List
		if (log.isLoggable(Level.CONFIG)) log.config("#" + model.getSize());
		centerList.setModel(model);
		//	Tree
		centerTree.initTree(m_tree.getAD_Tree_ID());
	}	//	action_fillTree
	
	/**
	 * 	List Selection Listener
	 *	@param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		ListItem selected = null;
		try
		{	//	throws a ArrayIndexOutOfBoundsException if root is selected
			selected = (ListItem)centerList.getSelectedValue();
		}
		catch (Exception ex)
		{
		}
		if (log.isLoggable(Level.INFO)) log.info("Selected=" + selected);
		if (selected != null)	//	allow add if not in tree
			bAdd.setEnabled(!centerTree.setSelectedNode(selected.id));
	}	//	valueChanged
	
	/**
	 * 	VTreePanel Changed
	 *	@param e event
	 */
	public void propertyChange (PropertyChangeEvent e)
	{
		MTreeNode tn = (MTreeNode)e.getNewValue();
		if (tn == null)
			return;
		if (log.isLoggable(Level.INFO)) log.info(tn.toString());
		ListModel<Object> model = centerList.getModel();
		int size = model.getSize();
		int index = -1;
		for (index = 0; index < size; index++)
		{
			ListItem item = (ListItem)model.getElementAt(index);
			if (item.id == tn.getNode_ID())
				break;
		}
		centerList.setSelectedIndex(index);
	}	//	propertyChange

	/**
	 * 	Action: Add Node to Tree
	 * 	@param item item
	 */
	private void action_treeAdd(ListItem item)
	{
		if (log.isLoggable(Level.INFO)) log.info("Item=" + item);
		if (item != null)
		{
			centerTree.nodeChanged(true, item.id, item.name, 
				item.description, item.isSummary, item.imageIndicator);
			
			//	May cause Error if in tree
			addNode(item);
		}
	}	//	action_treeAdd
	
	/**
	 * 	Action: Delete Node from Tree
	 * 	@param item item
	 */
	private void action_treeDelete(ListItem item)
	{
		if (log.isLoggable(Level.INFO)) log.info("Item=" + item);
		if (item != null)
		{
			centerTree.nodeChanged(false, item.id, item.name, 
				item.description, item.isSummary, item.imageIndicator);
			
			deleteNode(item);
		}
	}	//	action_treeDelete

	
	/**
	 * 	Action: Add All Nodes to Tree
	 */
	private void action_treeAddAll()
	{
		if (ADialog.ask(m_WindowNo, null, "TreeAddAllItems")) {	// idempiere-85
			log.info("");
			ListModel<Object> model = centerList.getModel();
			int size = model.getSize();
			int index = -1;
			for (index = 0; index < size; index++)
			{
				ListItem item = (ListItem)model.getElementAt(index);
				action_treeAdd(item);
			}
		}
	}	//	action_treeAddAll
	
	/**
	 * 	Action: Delete All Nodes from Tree
	 */
	private void action_treeDeleteAll()
	{
		if (ADialog.ask(m_WindowNo, null, "TreeRemoveAllItems")) {	// idempiere-85
			log.info("");
			ListModel<Object> model = centerList.getModel();
			int size = model.getSize();
			int index = -1;
			for (index = 0; index < size; index++)
			{
				ListItem item = (ListItem)model.getElementAt(index);
				action_treeDelete(item);
			}
		}
	}	//	action_treeDeleteAll	

}	//	VTreeMaintenance
