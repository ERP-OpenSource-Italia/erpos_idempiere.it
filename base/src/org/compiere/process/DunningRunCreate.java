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
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MDunningLevel;
import org.compiere.model.MDunningRun;
import org.compiere.model.MDunningRunEntry;
import org.compiere.model.MDunningRunLine;
import org.compiere.model.MInvoice;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Create Dunning Run Entries/Lines
 *	
 *  @author Jorg Janke
 *  @version $Id: DunningRunCreate.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class DunningRunCreate extends SvrProcess
{
	private boolean 	p_IncludeInDispute = false;
	private boolean		p_OnlySOTrx = false;
	private boolean		p_IsAllCurrencies = false;
	private int			p_SalesRep_ID = 0;
	private int			p_C_Currency_ID = 0;
	private int			p_C_BPartner_ID = 0;
	private int			p_C_BP_Group_ID = 0;
	private int			p_C_DunningRun_ID = 0;
	private int			p_AD_Org_ID = 0;
	
	private MDunningRun m_run = null;
	private MDunningLevel m_level = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("IncludeInDispute"))
				p_IncludeInDispute = "Y".equals(para[i].getParameter());
			else if (name.equals("OnlySOTrx"))
				p_OnlySOTrx = "Y".equals(para[i].getParameter());
			else if (name.equals("IsAllCurrencies"))
				p_IsAllCurrencies = "Y".equals(para[i].getParameter());
			else if (name.equals("SalesRep_ID"))
				p_SalesRep_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_DunningRun_ID = getRecord_ID();
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("C_DunningRun_ID=" + p_C_DunningRun_ID
			+ ", Dispute=" + p_IncludeInDispute
			+ ", C_BP_Group_ID=" + p_C_BP_Group_ID
			+ ", C_BPartner_ID=" + p_C_BPartner_ID);
		m_run = new MDunningRun (getCtx(),p_C_DunningRun_ID, get_TrxName());
		if (m_run.get_ID() == 0)
			throw new IllegalArgumentException ("Not found MDunningRun");
		if (!m_run.deleteEntries(true))
			throw new IllegalArgumentException ("Cannot delete existing entries");
		if (p_SalesRep_ID == 0)
			throw new IllegalArgumentException ("No SalesRep");
		if (p_C_Currency_ID == 0)
			throw new IllegalArgumentException ("No Currency");
		//
		m_level = m_run.getLevel ();
		
		int inv = addInvoices();
		int pay = addPayments();
		
		if (m_level.isChargeFee ()) 
			addFees();
		
		return "@C_Invoice_ID@ #" + inv + " - @C_Payment_ID@=" + pay;
	}	//	doIt

	
	/**************************************************************************
	 * 	Add Invoices to Run
	 *	@return no of invoices
	 */
	private int addInvoices()
	{
		int count = 0;
		String sql = "SELECT i.C_Invoice_ID, i.C_Currency_ID,"
			+ " i.GrandTotal*i.MultiplierAP,"
			+ " invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID)*MultiplierAP,"
			+ " COALESCE(daysBetween(?,ips.DueDate),paymentTermDueDays(i.C_PaymentTerm_ID,i.DateInvoiced,?))," // ##1/2
			+ " i.IsInDispute, i.C_BPartner_ID, i.C_InvoicePaySchedule_ID "
			+ "FROM C_Invoice_v i "
			+ " LEFT OUTER JOIN C_InvoicePaySchedule ips ON (i.C_InvoicePaySchedule_ID=ips.C_InvoicePaySchedule_ID) "
			+ "WHERE i.IsPaid='N' AND i.AD_Client_ID=?"				//	##3
			+ " AND i.DocStatus IN ('CO','CL')"
			+ " AND (i.DunningGrace IS NULL OR i.DunningGrace<?) "
		//	Only BP(Group) with Dunning defined
			+ " AND EXISTS (SELECT * FROM C_DunningLevel dl "
				+ "WHERE dl.C_DunningLevel_ID=?"	//	//	##4
				+ " AND dl.C_Dunning_ID IN "
					+ "(SELECT COALESCE(bp.C_Dunning_ID, bpg.C_Dunning_ID) "
					+ "FROM C_BPartner bp"
					+ " INNER JOIN C_BP_Group bpg ON (bp.C_BP_Group_ID=bpg.C_BP_Group_ID) "
					+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID" +
							" AND (bp.DunningGrace IS NULL OR bp.DunningGrace<?)))";
		if (p_C_BPartner_ID != 0)
			sql += " AND i.C_BPartner_ID=?";	//	##5
		else if (p_C_BP_Group_ID != 0)
			sql += " AND EXISTS (SELECT * FROM C_BPartner bp "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND bp.C_BP_Group_ID=?)";	//	##5
		if (p_OnlySOTrx)
			sql += " AND i.IsSOTrx='Y'";
		if (!p_IsAllCurrencies) 
			sql += " AND i.C_Currency_ID=" + p_C_Currency_ID;
		if ( p_AD_Org_ID != 0 )
			sql += " AND i.AD_Org_ID=" + p_AD_Org_ID;
	//	log.info(sql);
		
		String sql2=null;
		
		// if sequentially we must check for other levels with smaller days for
		// which this invoice is not yet included!
		MDunningLevel[] previousLevels = null;
		if (m_level.getParent ().isCreateLevelsSequentially ()) {
			// Build a list of all topmost Dunning Levels
			previousLevels =  m_level.getPreviousLevels();
			if (previousLevels!=null && previousLevels.length>0) {
				String sqlAppend = "";
				for (int i=0; i<previousLevels.length; i++)
				{
					sqlAppend += " AND i.C_Invoice_ID IN (SELECT C_Invoice_ID FROM C_DunningRunLine WHERE " +
					"C_DunningRunEntry_ID IN (SELECT C_DunningRunEntry_ID FROM C_DunningRunEntry WHERE " +
					"C_DunningRun_ID IN (SELECT C_DunningRun_ID FROM C_DunningRun WHERE " +
					"C_DunningLevel_ID=" + previousLevels[i].get_ID () + ")) AND Processed<>'N')";
				}
				sql += sqlAppend;
			}
		}
		sql2 = "SELECT COUNT(*), COALESCE(TRUNC(?-MAX(dr.DunningDate)),0) "
			+ "FROM C_DunningRun dr"
			+ " INNER JOIN C_DunningRunEntry dre ON (dr.C_DunningRun_ID=dre.C_DunningRun_ID)"
			+ " INNER JOIN C_DunningRunLine drl ON (dre.C_DunningRunEntry_ID=drl.C_DunningRunEntry_ID) "
			+ "WHERE drl.Processed='Y' AND drl.C_Invoice_ID=? AND COALESCE(drl.C_InvoicePaySchedule_ID, 0)=?";

		
		BigDecimal DaysAfterDue = m_run.getLevel().getDaysAfterDue();
		int DaysBetweenDunning = m_run.getLevel().getDaysBetweenDunning();
		
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setTimestamp(1, m_run.getDunningDate());
			pstmt.setTimestamp(2, m_run.getDunningDate());
			pstmt.setInt (3, m_run.getAD_Client_ID());
			pstmt.setTimestamp(4, m_run.getDunningDate());
			pstmt.setInt(5, m_run.getC_DunningLevel_ID());
			pstmt.setTimestamp(6, m_run.getDunningDate());
			if (p_C_BPartner_ID != 0)
				pstmt.setInt (7, p_C_BPartner_ID);
			else if (p_C_BP_Group_ID != 0)
				pstmt.setInt (7, p_C_BP_Group_ID);
			//
			pstmt2 = DB.prepareStatement (sql2, get_TrxName());
			//
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int C_Invoice_ID = rs.getInt(1);
				int C_Currency_ID = rs.getInt(2);
				BigDecimal GrandTotal = rs.getBigDecimal(3);
				BigDecimal Open = rs.getBigDecimal(4);
				int DaysDue = rs.getInt(5);
				boolean IsInDispute = "Y".equals(rs.getString(6));
				int C_BPartner_ID = rs.getInt(7);
				int C_InvoicePaySchedule_ID = rs.getInt(8);
				log.fine("DaysAfterDue: " + DaysAfterDue.intValue() + " isShowAllDue: " + m_level.isShowAllDue());
				log.fine("C_Invoice_ID - DaysDue - GrandTotal: " + C_Invoice_ID + " - " + DaysDue + " - " + GrandTotal);
				log.fine("C_InvoicePaySchedule_ID: " + C_InvoicePaySchedule_ID);
				//
				if (!p_IncludeInDispute && IsInDispute)
					continue;
				if (DaysDue < DaysAfterDue.intValue() && !m_level.isShowAllDue ())
					continue;
				if (Env.ZERO.compareTo(Open) == 0)
					continue;
				//
				int TimesDunned = 0;
				int DaysAfterLast = 0;
				//	SubQuery
				pstmt2.setTimestamp(1, m_run.getDunningDate());
				pstmt2.setInt (2, C_Invoice_ID);
				pstmt2.setInt (3, C_InvoicePaySchedule_ID);
				ResultSet rs2 = pstmt2.executeQuery ();
				if (rs2.next())
				{
					TimesDunned = rs2.getInt(1);
					DaysAfterLast = rs2.getInt(2);
				}
				rs2.close();
				if(previousLevels != null){
					log.fine(TimesDunned + " - " + previousLevels.length);
				}
				//	SubQuery
				if (m_level.getParent().isCreateLevelsSequentially() && previousLevels!=null && TimesDunned>previousLevels.length
						 && !m_level.isShowAllDue() && !m_level.isShowNotDue()) {
					continue;
				}
				log.fine(DaysBetweenDunning + " - " + DaysAfterLast);
				if (DaysBetweenDunning != 0 && DaysAfterLast < DaysBetweenDunning && !m_level.isShowAllDue () && !m_level.isShowNotDue ())
					continue;
				//
				createInvoiceLine (C_Invoice_ID, C_InvoicePaySchedule_ID, C_Currency_ID, GrandTotal, Open,
					DaysDue, IsInDispute, C_BPartner_ID, 
					TimesDunned, DaysAfterLast);
				count++;
			}
 
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "addInvoices", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null; pstmt2 = null;
		}
		return count;
	}	//	addInvoices

	/**
	 * 	Create Invoice Line
	 *	@param C_Invoice_ID
	 *	@param C_Currency_ID
	 *	@param GrandTotal
	 *	@param Open
	 *	@param DaysDue
	 *	@param IsInDispute
	 *	@param C_BPartner_ID
	 *	@param TimesDunned
	 *	@param DaysAfterLast
	 */
	private void createInvoiceLine (int C_Invoice_ID, int C_InvoicePaySchedule_ID, int C_Currency_ID, 
		BigDecimal GrandTotal, BigDecimal Open, 
		int DaysDue, boolean IsInDispute, 
		int C_BPartner_ID, int TimesDunned, int DaysAfterLast)
	{
		MDunningRunEntry entry = m_run.getEntry (C_BPartner_ID, p_C_Currency_ID, p_SalesRep_ID);
		if (entry.get_ID() == 0)
			if (!entry.save())
				throw new IllegalStateException("Cannot save MDunningRunEntry");
		//
		MDunningRunLine line = new MDunningRunLine (entry);
		line.setInvoice(C_Invoice_ID, C_Currency_ID, GrandTotal, Open, 
			new BigDecimal(0), DaysDue, IsInDispute, TimesDunned, 
			DaysAfterLast);
		line.setC_InvoicePaySchedule_ID(C_InvoicePaySchedule_ID);
		if (!line.save())
			throw new IllegalStateException("Cannot save MDunningRunLine");
		MInvoice invoice = new MInvoice(getCtx(),C_Invoice_ID, get_TrxName());
		invoice.setC_DunningLevel_ID(m_run.getC_DunningLevel_ID());
		if (!invoice.save())
			throw new IllegalStateException("Cannot update dunning level information in invoice");
	}	//	createInvoiceLine

	
	/**************************************************************************
	 * 	Add Payments to Run
	 *	@return no of payments
	 */
	private int addPayments()
	{
		String sql = "SELECT C_Payment_ID, C_Currency_ID, PayAmt,"
			+ " paymentAvailable(C_Payment_ID), C_BPartner_ID "
			+ "FROM C_Payment_v p "
			+ "WHERE AD_Client_ID=?"			//	##1
			+ " AND IsAllocated='N' AND C_BPartner_ID IS NOT NULL"
			+ " AND C_Charge_ID IS NULL"
			+ " AND DocStatus IN ('CO','CL')"
		//	Only BP with Dunning defined
			+ " AND EXISTS (SELECT * FROM C_BPartner bp "
				+ "WHERE p.C_BPartner_ID=bp.C_BPartner_ID"
				+ " AND bp.C_Dunning_ID=(SELECT C_Dunning_ID FROM C_DunningLevel WHERE C_DunningLevel_ID=?))";	// ##2
		if (p_C_BPartner_ID != 0)
			sql += " AND C_BPartner_ID=?";		//	##3
		else if (p_C_BP_Group_ID != 0)
			sql += " AND EXISTS (SELECT * FROM C_BPartner bp "
				+ "WHERE p.C_BPartner_ID=bp.C_BPartner_ID AND bp.C_BP_Group_ID=?)";	//	##3
		if (p_OnlySOTrx)
			sql += " AND IsReceipt='Y'";
		if ( p_AD_Org_ID != 0 )
			sql += " AND p.AD_Org_ID=" + p_AD_Org_ID;
		
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_Client_ID());
			pstmt.setInt (2, m_run.getC_DunningLevel_ID());
			if (p_C_BPartner_ID != 0)
				pstmt.setInt (3, p_C_BPartner_ID);
			else if (p_C_BP_Group_ID != 0)
				pstmt.setInt (3, p_C_BP_Group_ID);

			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int C_Payment_ID = rs.getInt(1);
				int C_Currency_ID = rs.getInt(2);
				BigDecimal PayAmt = rs.getBigDecimal(3).negate();
				BigDecimal OpenAmt = rs.getBigDecimal(4).negate();
				int C_BPartner_ID = rs.getInt(5);
				//
				if (Env.ZERO.compareTo(OpenAmt) == 0)
					continue;
				//
				createPaymentLine (C_Payment_ID, C_Currency_ID, PayAmt, OpenAmt,
					C_BPartner_ID);
				count++;
			}
 		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return count;
	}	//	addPayments

	/**
	 * 	Create Payment Line
	 *	@param C_Payment_ID
	 *	@param C_Currency_ID
	 *	@param PayAmt
	 *	@param OpenAmt
	 *	@param C_BPartner_ID
	 */
	private void createPaymentLine (int C_Payment_ID, int C_Currency_ID, 
		BigDecimal PayAmt, BigDecimal OpenAmt, int C_BPartner_ID)
	{
		MDunningRunEntry entry = m_run.getEntry (C_BPartner_ID, p_C_Currency_ID, p_SalesRep_ID);
		if (entry.get_ID() == 0)
			if (!entry.save())
				throw new IllegalStateException("Cannot save MDunningRunEntry");
		//
		MDunningRunLine line = new MDunningRunLine (entry);
		line.setPayment(C_Payment_ID, C_Currency_ID, PayAmt, OpenAmt);
		if (!line.save())
			throw new IllegalStateException("Cannot save MDunningRunLine");
	}	//	createPaymentLine

	private void addFees()
	{
		MDunningRunEntry [] entries = m_run.getEntries (true);
		if (entries!=null && entries.length>0) {
			for (int i=0;i<entries.length;i++) {
				MDunningRunLine line = new MDunningRunLine (entries[i]);
				line.setFee (p_C_Currency_ID, m_level.getFeeAmt ());
				if (!line.save())
					throw new IllegalStateException("Cannot save MDunningRunLine");
				entries[i].setQty (entries[i].getQty ().subtract (new BigDecimal(1)));
			}
		}
	}
	
}	//	DunningRunCreate
