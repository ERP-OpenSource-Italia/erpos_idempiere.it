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
package org.compiere.apps.search;

import java.awt.Frame;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.apps.AEnv;
import org.compiere.apps.ALayout;
import org.compiere.apps.ALayoutConstraint;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MQuery;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *	Search Business Partner and return selection
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: InfoBPartner.java,v 1.3 2006/07/30 00:51:27 jjanke Exp $
 */
public class InfoBPartner extends Info
{
	/**
	 *
	 */
	private static final long serialVersionUID = 5550733934685665946L;

	/**
	 *	Standard Constructor
	 *  @param frame frame
	 *  @param modal modal
	 *  @param WindowNo WindowNo
	 *  @param  value   Query value Name or Value if contains numbers
	 *  @param isSOTrx  if false, query vendors only
	 *  @param multiSelection multiple selection
	 *  @param whereClause where clause
	 */
	public InfoBPartner(Frame frame, boolean modal, int WindowNo,
		String value, boolean isSOTrx,
		boolean multiSelection, String whereClause)
	{
		this(frame, modal, WindowNo, value, isSOTrx, multiSelection, whereClause, true);
	}

	/**
	 *	Standard Constructor
	 *  @param frame frame
	 *  @param modal modal
	 *  @param WindowNo WindowNo
	 *  @param  value   Query value Name or Value if contains numbers
	 *  @param isSOTrx  if false, query vendors only
	 *  @param multiSelection multiple selection
	 *  @param whereClause where clause
	 *  @param lookup
	 */
	public InfoBPartner(Frame frame, boolean modal, int WindowNo,
		String value, boolean isSOTrx,
		boolean multiSelection, String whereClause, boolean lookup)
	{
		super (frame, modal, WindowNo, "C_BPartner", "C_BPartner_ID", multiSelection, whereClause, lookup);
		log.info(value);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoBPartner"));
		m_isSOTrx = isSOTrx;
		//
		statInit();
		initInfo (value, whereClause);
		//
		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		//	AutoQuery
		if (value != null && value.length() > 0)
			executeQuery();
		p_loadedOK = true;
		//	Focus
		fieldValue.requestFocus();

		AEnv.positionCenterWindow(frame, this);
	}	//	InfoBPartner

	/** SalesOrder Trx          */
	private boolean 		m_isSOTrx = false;

	private int m_AD_User_ID_index = -1;
	private int m_C_BPartner_Location_ID_index = -1;

	/** From Clause             */
	private static String s_partnerFROM = "C_BPartner"
		+ " LEFT OUTER JOIN C_BPartner_Location l ON (C_BPartner.C_BPartner_ID=l.C_BPartner_ID AND l.IsActive='Y')"
		+ " LEFT OUTER JOIN AD_User c ON (C_BPartner.C_BPartner_ID=c.C_BPartner_ID AND (c.C_BPartner_Location_ID IS NULL OR c.C_BPartner_Location_ID=l.C_BPartner_Location_ID) AND c.IsActive='Y')"
		+ " LEFT OUTER JOIN C_Location a ON (l.C_Location_ID=a.C_Location_ID)";

	/**  Array of Column Info    */
	private static Info_Column[] s_partnerLayout = {
		new Info_Column(" ", "C_BPartner.C_BPartner_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "C_BPartner.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "C_BPartner.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Contact"), "c.Name AS Contact", KeyNamePair.class, "c.AD_User_ID"),
		new Info_Column(Msg.translate(Env.getCtx(), "SO_CreditAvailable"), "C_BPartner.SO_CreditLimit-C_BPartner.SO_CreditUsed AS SO_CreditAvailable", BigDecimal.class, true, true, null),
		new Info_Column(Msg.translate(Env.getCtx(), "SO_CreditUsed"), "C_BPartner.SO_CreditUsed", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Phone"), "c.Phone", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Postal"), "a.Postal", KeyNamePair.class, "l.C_BPartner_Location_ID"),
		new Info_Column(Msg.translate(Env.getCtx(), "City"), "a.City", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "TotalOpenBalance"), "C_BPartner.TotalOpenBalance", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Revenue"), "C_BPartner.ActualLifetimeValue", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Address1"), "a.Address1", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "IsShipTo"), "l.IsShipTo", Boolean.class),
		new Info_Column(Msg.translate(Env.getCtx(), "IsBillTo"), "l.IsBillTo", Boolean.class)
	};

	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(10);
	private CLabel labelContact = new CLabel();
	private CTextField fieldContact = new CTextField(10);
	private CLabel labelEMail = new CLabel();
	private CTextField fieldEMail = new CTextField(10);
	private CLabel labelPostal = new CLabel();
	private CTextField fieldPostal = new CTextField(10);
	private CLabel labelPhone = new CLabel();
	private CTextField fieldPhone = new CTextField(10);
	private VCheckBox checkAND = new VCheckBox();
	private VCheckBox checkCustomer = new VCheckBox();

	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelValue.setText(Msg.getMsg(Env.getCtx(), "Value"));
		fieldValue.setBackground(AdempierePLAF.getInfoBackground());
		fieldValue.addActionListener(this);

		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
		fieldName.setBackground(AdempierePLAF.getInfoBackground());
		fieldName.addActionListener(this);

		labelContact.setText(Msg.getMsg(Env.getCtx(), "Contact"));
		fieldContact.setBackground(AdempierePLAF.getInfoBackground());
		fieldContact.addActionListener(this);

		labelEMail.setText(Msg.getMsg(Env.getCtx(), "EMail"));
		fieldEMail.setBackground(AdempierePLAF.getInfoBackground());
		fieldEMail.addActionListener(this);

		labelPostal.setText(Msg.getMsg(Env.getCtx(), "Postal"));
		fieldPostal.setBackground(AdempierePLAF.getInfoBackground());
		fieldPostal.addActionListener(this);

		labelPhone.setText(Msg.translate(Env.getCtx(), "Phone"));
		fieldPhone.setBackground(AdempierePLAF.getInfoBackground());
		fieldPhone.addActionListener(this);

		checkAND.setText(Msg.getMsg(Env.getCtx(), "SearchAND", true));
		checkAND.setToolTipText(Msg.getMsg(Env.getCtx(), "SearchANDInfo"));
		checkAND.setSelected(true);
		checkAND.addActionListener(this);
		if (m_isSOTrx)
			checkCustomer.setText(Msg.getMsg(Env.getCtx(), "OnlyCustomers"));
		else
			checkCustomer.setText(Msg.getMsg(Env.getCtx(), "OnlyVendors"));
		checkCustomer.setSelected(true);
		checkCustomer.setFocusable(false);
		checkCustomer.setRequestFocusEnabled(false);
		checkCustomer.addActionListener(this);
		//
		parameterPanel.setLayout(new ALayout());
		//
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);
		parameterPanel.add(labelContact, null);
		parameterPanel.add(fieldContact, null);
		parameterPanel.add(labelPhone, null);
		parameterPanel.add(fieldPhone, null);
		parameterPanel.add(checkCustomer, null);
		//
		parameterPanel.add(labelName, new ALayoutConstraint(1,0));
		parameterPanel.add(fieldName, null);
		parameterPanel.add(labelEMail, null);
		parameterPanel.add(fieldEMail, null);
		parameterPanel.add(labelPostal, null);
		parameterPanel.add(fieldPostal, null);
		parameterPanel.add(checkAND, null);
	}	//	statInit

	/**
	 *	Dynamic Init
	 *  @param value value
	 *  @param whereClause where clause
	 */
	private void initInfo(String value, String whereClause)
	{
		/**	From
			C_BPartner
			 LEFT OUTER JOIN C_BPartner_Location l ON (C_BPartner.C_BPartner_ID=l.C_BPartner_ID AND l.IsActive='Y')
			 LEFT OUTER JOIN AD_User c ON (C_BPartner.C_BPartner_ID=c.C_BPartner_ID AND (c.C_BPartner_Location_ID IS NULL OR c.C_BPartner_Location_ID=l.C_BPartner_Location_ID) AND c.IsActive='Y')
			 LEFT OUTER JOIN C_Location a ON (l.C_Location_ID=a.C_Location_ID)
		**/

		//	Create Grid
		StringBuilder where = new StringBuilder();
		where.append("C_BPartner.IsSummary='N' AND C_BPartner.IsActive='Y'");
		if (whereClause != null && whereClause.length() > 0)
			where.append(" AND ").append(whereClause);
		//
		prepareTable(s_partnerLayout, s_partnerFROM,
			where.toString(),
			"C_BPartner.Value");

		//  Get indexes
		for (int i = 0; i < p_layout.length; i++)
		{
			if (p_layout[i].getIDcolSQL().indexOf("AD_User_ID") != -1)
				m_AD_User_ID_index = i;
			if (p_layout[i].getIDcolSQL().indexOf("C_BPartner_Location_ID") != -1)
				m_C_BPartner_Location_ID_index = i;
		}

		//  Set Value
		if (value == null)
			value = "%";
		if (!value.endsWith("%"))
			value += "%";

		//	Put query string in Name if not numeric
		if (value.equals("%"))
			fieldName.setText(value);
		//	No Numbers entered
		else if ((value.indexOf('0')+value.indexOf('1')+value.indexOf('2')+value.indexOf('3')+value.indexOf('4') +value.indexOf('5')
			+value.indexOf('6')+value.indexOf('7')+value.indexOf('8')+value.indexOf('9')) == -10)
		{
			if (value.startsWith("%"))
				fieldName.setText(value);
			else
				fieldName.setText("%" + value);
		}
		//	Number entered
		else
			fieldValue.setText(value);
	}	//	initInfo

	/*************************************************************************/

	/**
	 *	Construct SQL Where Clause and define parameters.
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return WHERE clause
	 */
	protected String getSQLWhere()
	{
		ArrayList<String> list = new ArrayList<String>();
		//	=> Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			list.add ("UPPER(C_BPartner.Value) LIKE ?");
		//	=> Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			list.add ("UPPER(C_BPartner.Name) LIKE ?");
		//	=> Contact
		String contact = fieldContact.getText().toUpperCase();
		if (!(contact.equals("") || contact.equals("%")))
			list.add ("UPPER(c.Name) LIKE ?");
		//	=> EMail
		String email = fieldEMail.getText().toUpperCase();
		if (!(email.equals("") || email.equals("%")))
			list.add ("UPPER(c.EMail) LIKE ?");
		//	=> Phone
		String phone = fieldPhone.getText().toUpperCase();
		if (!(phone.equals("") || phone.equals("%")))
			list.add ("UPPER(c.Phone) LIKE ?");
		//	=> Postal
		String postal = fieldPostal.getText().toUpperCase();
		if (!(postal.equals("") || postal.equals("%")))
			list.add ("UPPER(a.Postal) LIKE ?");

		StringBuilder sql = new StringBuilder();
		int size = list.size();
		//	Just one
		if (size == 1)
			sql.append(" AND ").append(list.get(0));
		else if (size > 1)
		{
			boolean AND = checkAND.isSelected();
			sql.append(" AND ");
			if (!AND)
				sql.append("(");
			for (int i = 0; i < size; i++)
			{
				if (i > 0)
					sql.append(AND ? " AND " : " OR ");
				sql.append(list.get(i));
			}
			if (!AND)
				sql.append(")");
		}

		//	Static SQL
		if (checkCustomer.isSelected())
		{
			sql.append(" AND ");
			if (m_isSOTrx)
				sql.append ("C_BPartner.IsCustomer='Y'");
			else
				sql.append ("C_BPartner.IsVendor='Y'");
		}
		return sql.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt pstmt
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	protected void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;
		//	=> Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
			if (log.isLoggable(Level.FINE)) log.fine("Value: " + value);
		}
		//	=> Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
			if (log.isLoggable(Level.FINE)) log.fine("Name: " + name);
		}
		//	=> Contact
		String contact = fieldContact.getText().toUpperCase();
		if (!(contact.equals("") || contact.equals("%")))
		{
			if (!contact.endsWith("%"))
				contact += "%";
			pstmt.setString(index++, contact);
			if (log.isLoggable(Level.FINE)) log.fine("Contact: " + contact);
		}
		//	=> EMail
		String email = fieldEMail.getText().toUpperCase();
		if (!(email.equals("") || email.equals("%")))
		{
			if (!email.endsWith("%"))
				email += "%";
			pstmt.setString(index++, email);
			if (log.isLoggable(Level.FINE)) log.fine("EMail: " + email);
		}
		//	=> Phone
		String phone = fieldPhone.getText().toUpperCase();
		if (!(phone.equals("") || phone.equals("%")))
		{
			if (!phone.endsWith("%"))
				phone += "%";
			pstmt.setString(index++, phone);
			if (log.isLoggable(Level.FINE)) log.fine("Phone: " + phone);
		}
		//	=> Postal
		String postal = fieldPostal.getText().toUpperCase();
		if (!(postal.equals("") || postal.equals("%")))
		{
			if (!postal.endsWith("%"))
				postal += "%";
			pstmt.setString(index++, postal);
			if (log.isLoggable(Level.FINE)) log.fine("Postal: " + postal);
		}
	}   //  setParameters

	/*************************************************************************/

	/**
	 *  Save Selection Details
	 *  Get Location/Partner Info
	 */
	public void saveSelectionDetail()
	{
		int row = p_table.getSelectedRow();
		if (row == -1)
			return;

		int AD_User_ID = 0;
		int C_BPartner_Location_ID = 0;

		if (m_AD_User_ID_index != -1)
		{
			Object data = p_table.getModel().getValueAt(row, m_AD_User_ID_index);
			if (data instanceof KeyNamePair)
				AD_User_ID = ((KeyNamePair)data).getKey();
		}
		if (m_C_BPartner_Location_ID_index != -1)
		{
			Object data = p_table.getModel().getValueAt(row, m_C_BPartner_Location_ID_index);
			if (data instanceof KeyNamePair)
				C_BPartner_Location_ID = ((KeyNamePair)data).getKey();
		}
		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "C_BPartner_ID", ID == null ? "0" : ID.toString());
		Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "AD_User_ID", String.valueOf(AD_User_ID));
		Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "C_BPartner_Location_ID", String.valueOf(C_BPartner_Location_ID));
	}   //  saveSelectionDetail


	/**************************************************************************
	 *	Show History
	 */
	protected void showHistory()
	{
		log.info("");
		Integer C_BPartner_ID = getSelectedRowKey();
		if (C_BPartner_ID == null)
			return;
		InvoiceHistory ih = new InvoiceHistory (this, C_BPartner_ID.intValue(),
			0, 0, 0);
		ih.setVisible(true);
		ih = null;
	}	//	showHistory

	/**
	 *	Has History
	 *  @return true
	 */
	protected boolean hasHistory()
	{
		return true;
	}	//	hasHistory

	/**
	 *	Zoom
	 */
	protected void zoom()
	{
		log.info( "InfoBPartner.zoom");
		Integer C_BPartner_ID = getSelectedRowKey();
		if (C_BPartner_ID == null)
			return;
	//	AEnv.zoom(MBPartner.Table_ID, C_BPartner_ID.intValue(), true);	//	SO

		MQuery query = new MQuery("C_BPartner");
		query.addRestriction("C_BPartner_ID", MQuery.EQUAL, C_BPartner_ID);
		query.setRecordCount(1);
		int AD_WindowNo = getAD_Window_ID("C_BPartner", true);	//	SO
		zoom (AD_WindowNo, query);
	}	//	zoom

	/**
	 *	Has Zoom
	 *  @return true
	 */
	protected boolean hasZoom()
	{
		return true;
	}	//	hasZoom

	/**
	 *	Customize
	 */
	protected void customize()
	{
		log.info( "InfoBPartner.customize");
	}	//	customize

	/**
	 *	Has Customize
	 *  @return false
	 */
	protected boolean hasCustomize()
	{
		return false;	//	for now
	}	//	hasCustomize

}	//	InfoBPartner
