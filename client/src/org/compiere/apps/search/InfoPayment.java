/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
import java.sql.Timestamp;
import java.util.ArrayList;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.apps.ALayout;
import org.compiere.apps.ALayoutConstraint;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MQuery;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;

/**
 *  Info Payment
 *
 *  @author Jorg Janke
 *  @version  $Id: InfoPayment.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class InfoPayment extends Info
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2917241055484901704L;

	/**
	 *  Detail Protected Contructor
	 *  @param frame parent frame
	 *  @param modal modal
	 *  @param WindowNo window no
	 *  @param value query value
	 *  @param multiSelection multiple selections
	 *  @param whereClause whwre clause
	 */
	protected InfoPayment(Frame frame, boolean modal, int WindowNo, String value,
		boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "p", "C_Payment_ID", multiSelection, whereClause);
		log.info( "InfoPayment");
		setTitle(Msg.getMsg(Env.getCtx(), "InfoPayment"));
		//
		try
		{
			statInit();
			p_loadedOK = initInfo ();
		}
		catch (Exception e)
		{
			return;
		}
		//
		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		if (value != null && value.length() > 0)
		{
			fDocumentNo.setValue(value);
			executeQuery();
		}
		//
		pack();
		//	Focus
		fDocumentNo.requestFocus();
	}   //  InfoPayment

	/**  String Array of Column Info    */
	private Info_Column[] m_generalLayout;
	/** list of query columns           */
	private ArrayList 	m_queryColumns = new ArrayList();
	/** Table Name              */
	private String      m_tableName;
	/** Key Column Name         */
	private String      m_keyColumn;

	//  Static Info
	private CLabel lDocumentNo = new CLabel(Msg.translate(Env.getCtx(), "DocumentNo"));
	private CTextField fDocumentNo = new CTextField(10);
	//
//	private CLabel lOrg_ID = new CLabel(Msg.translate(Env.getCtx(), "AD_Org_ID"));
//	private VLookup fOrg_ID;
	private CLabel lBPartner_ID = new CLabel(Msg.translate(Env.getCtx(), "BPartner"));
	private VLookup fBPartner_ID;
	//
	private CLabel lDateFrom = new CLabel(Msg.translate(Env.getCtx(), "DateTrx"));
	private VDate fDateFrom = new VDate("DateFrom", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private CLabel lDateTo = new CLabel("-");
	private VDate fDateTo = new VDate("DateTo", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateTo"));
	private CLabel lAmtFrom = new CLabel(Msg.translate(Env.getCtx(), "PayAmt"));
	private VNumber fAmtFrom = new VNumber("AmtFrom", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtFrom"));
	private CLabel lAmtTo = new CLabel("-");
	private VNumber fAmtTo = new VNumber("AmtTo", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtTo"));
	private VCheckBox fIsReceipt = new VCheckBox ("IsReceipt", false, false, true, Msg.translate(Env.getCtx(), "IsReceipt"), "", false);

	/**  Array of Column Info    */
	private static final Info_Column[] s_paymentLayout = {
		new Info_Column(" ", "p.C_Payment_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_BankAccount_ID"),
			"(SELECT b.Name || ' ' || ba.AccountNo FROM C_Bank b, C_BankAccount ba WHERE b.C_Bank_ID=ba.C_Bank_ID AND ba.C_BankAccount_ID=p.C_BankAccount_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_BPartner_ID"),
			"(SELECT Name FROM C_BPartner bp WHERE bp.C_BPartner_ID=p.C_BPartner_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "DateTrx"),
			"p.DateTrx", Timestamp.class),
		new Info_Column(Msg.translate(Env.getCtx(), "DocumentNo"),
			"p.DocumentNo", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "IsReceipt"),
			"p.IsReceipt", Boolean.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_Currency_ID"),
			"(SELECT ISO_Code FROM C_Currency c WHERE c.C_Currency_ID=p.C_Currency_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "PayAmt"),
			"p.PayAmt",  BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "ConvertedAmount"),
			"currencyBase(p.PayAmt,p.C_Currency_ID,p.DateTrx, p.AD_Client_ID,p.AD_Org_ID)", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "DiscountAmt"),
			"p.DiscountAmt",  BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "WriteOffAmt"),
			"p.WriteOffAmt",  BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "IsAllocated"),
			"p.IsAllocated",  Boolean.class)
	};

	/**
	 *	Static Setup - add fields to parameterPanel
	 *  @throws Exception if Lookups cannot be created
	 */
	private void statInit() throws Exception
	{
		lDocumentNo.setLabelFor(fDocumentNo);
		fDocumentNo.setBackground(AdempierePLAF.getInfoBackground());
		fDocumentNo.addActionListener(this);
		fIsReceipt.setSelected(!"N".equals(Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx")));
		fIsReceipt.addActionListener(this);
		//
	//	fOrg_ID = new VLookup("AD_Org_ID", false, false, true,
	//		MLookupFactory.create(Env.getCtx(), 3486, m_WindowNo, DisplayType.TableDir, false),
	//		DisplayType.TableDir, m_WindowNo);
	//	lOrg_ID.setLabelFor(fOrg_ID);
	//	fOrg_ID.setBackground(AdempierePLAF.getInfoBackground());
		fBPartner_ID = new VLookup("C_BPartner_ID", false, false, true,
			MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 3499, DisplayType.Search));
		lBPartner_ID.setLabelFor(fBPartner_ID);
		fBPartner_ID.setBackground(AdempierePLAF.getInfoBackground());
		//
		lDateFrom.setLabelFor(fDateFrom);
		fDateFrom.setBackground(AdempierePLAF.getInfoBackground());
		fDateFrom.setToolTipText(Msg.translate(Env.getCtx(), "DateFrom"));
		lDateTo.setLabelFor(fDateTo);
		fDateTo.setBackground(AdempierePLAF.getInfoBackground());
		fDateTo.setToolTipText(Msg.translate(Env.getCtx(), "DateTo"));
		lAmtFrom.setLabelFor(fAmtFrom);
		fAmtFrom.setBackground(AdempierePLAF.getInfoBackground());
		fAmtFrom.setToolTipText(Msg.translate(Env.getCtx(), "AmtFrom"));
		lAmtTo.setLabelFor(fAmtTo);
		fAmtTo.setBackground(AdempierePLAF.getInfoBackground());
		fAmtTo.setToolTipText(Msg.translate(Env.getCtx(), "AmtTo"));
		//
		parameterPanel.setLayout(new ALayout());
		//  First Row
		parameterPanel.add(lDocumentNo, new ALayoutConstraint(0,0));
		parameterPanel.add(fDocumentNo, null);
		parameterPanel.add(lBPartner_ID, null);
		parameterPanel.add(fBPartner_ID, null);
		parameterPanel.add(fIsReceipt, new ALayoutConstraint(0,5));
		//  2nd Row
		parameterPanel.add(lDateFrom, new ALayoutConstraint(1,2));
		parameterPanel.add(fDateFrom, null);
		parameterPanel.add(lDateTo, null);
		parameterPanel.add(fDateTo, null);
		//  3rd Row
		parameterPanel.add(lAmtFrom, new ALayoutConstraint(2,2));
		parameterPanel.add(fAmtFrom, null);
		parameterPanel.add(lAmtTo, null);
		parameterPanel.add(fAmtTo, null);
	//	parameterPanel.add(lOrg_ID, null);
	//	parameterPanel.add(fOrg_ID, null);
	}	//	statInit

	/**
	 *	General Init
	 *	@return true, if success
	 */
	private boolean initInfo ()
	{
		//  Set Defaults
		String bp = Env.getContext(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		if (bp != null && bp.length() != 0)
			fBPartner_ID.setValue(new Integer(bp));

		//  prepare table
		StringBuffer where = new StringBuffer("p.IsActive='Y'");
		if (p_whereClause.length() > 0)
			where.append(" AND ").append(Util.replace(p_whereClause, "C_Payment.", "p."));
		prepareTable(s_paymentLayout,
			" C_Payment_v p",
			where.toString(),
			"2,3,4");
		//
	//	MPayment.setIsAllocated(Env.getCtx(), 0, null);
		return true;
	}	//	initInfo

	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return sql where clause
	 */
	protected String getSQLWhere()
	{
		StringBuffer sql = new StringBuffer();
		if (fDocumentNo.getText().length() > 0)
			sql.append(" AND UPPER(p.DocumentNo) LIKE ?");
		//
		if (fBPartner_ID.getValue() != null)
			sql.append(" AND p.C_BPartner_ID=?");
		//
		if (fDateFrom.getValue() != null || fDateTo.getValue() != null)
		{
			Timestamp from = (Timestamp)fDateFrom.getValue();
			Timestamp to = (Timestamp)fDateTo.getValue();
			if (from == null && to != null)
				sql.append(" AND TRUNC(p.DateTrx) <= ?");
			else if (from != null && to == null)
				sql.append(" AND TRUNC(p.DateTrx) >= ?");
			else if (from != null && to != null)
				sql.append(" AND TRUNC(p.DateTrx) BETWEEN ? AND ?");
		}
		//
		if (fAmtFrom.getValue() != null || fAmtTo.getValue() != null)
		{
			BigDecimal from = (BigDecimal)fAmtFrom.getValue();
			BigDecimal to = (BigDecimal)fAmtTo.getValue();
			if (from == null && to != null)
				sql.append(" AND p.PayAmt <= ?");
			else if (from != null && to == null)
				sql.append(" AND p.PayAmt >= ?");
			else if (from != null && to != null)
				sql.append(" AND p.PayAmt BETWEEN ? AND ?");
		}
		sql.append(" AND p.IsReceipt=?");

		log.fine(sql.toString());
		return sql.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt statement
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	protected void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;
		if (fDocumentNo.getText().length() > 0)
			pstmt.setString(index++, getSQLText(fDocumentNo));
		//
		if (fBPartner_ID.getValue() != null)
		{
			Integer bp = (Integer)fBPartner_ID.getValue();
			pstmt.setInt(index++, bp.intValue());
			log.fine("BPartner=" + bp);
		}
		//
		if (fDateFrom.getValue() != null || fDateTo.getValue() != null)
		{
			Timestamp from = (Timestamp)fDateFrom.getValue();
			Timestamp to = (Timestamp)fDateTo.getValue();
			log.fine("Date From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setTimestamp(index++, to);
			else if (from != null && to == null)
				pstmt.setTimestamp(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setTimestamp(index++, from);
				pstmt.setTimestamp(index++, to);
			}
		}
		//
		if (fAmtFrom.getValue() != null || fAmtTo.getValue() != null)
		{
			BigDecimal from = (BigDecimal)fAmtFrom.getValue();
			BigDecimal to = (BigDecimal)fAmtTo.getValue();
			log.fine("Amt From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setBigDecimal(index++, to);
			else if (from != null && to == null)
				pstmt.setBigDecimal(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setBigDecimal(index++, from);
				pstmt.setBigDecimal(index++, to);
			}
		}
		pstmt.setString(index++, fIsReceipt.isSelected() ? "Y" : "N");
	}   //  setParameters

	/**
	 *  Get SQL WHERE parameter
	 *  @param f field
	 *  @return Upper case text with % at the end
	 */
	private String getSQLText (CTextField f)
	{
		String s = f.getText().toUpperCase();
		if (!s.endsWith("%"))
			s += "%";
		log.fine( "String=" + s);
		return s;
	}   //  getSQLText
	
	/**
	 *	Zoom
	 */
	protected void zoom()
	{
		log.info( "InfoPayment.zoom");
		Integer C_Payment_ID = getSelectedRowKey();
		if (C_Payment_ID == null)
			return;
		MQuery query = new MQuery("C_Payment");
		query.addRestriction("C_Payment_ID", MQuery.EQUAL, C_Payment_ID);
		query.setRecordCount(1);
		int AD_WindowNo = getAD_Window_ID("C_Payment", fIsReceipt.isSelected());
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
	
}   //  InfoPayment
