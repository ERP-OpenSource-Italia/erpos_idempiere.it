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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Cash Book Journal Callouts
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutCashJournal.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class CalloutCashJournal extends CalloutEngine
{
	/**
	 *  Cash Journal Line Invoice.
	 *  when Invoice selected
	 *  - set C_Currency, DiscountAnt, Amount, WriteOffAmt
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String invoice (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";

		Integer C_Invoice_ID = (Integer)value;
		if (C_Invoice_ID == null || C_Invoice_ID.intValue() == 0)
		{
			mTab.setValue("C_Currency_ID", null);
			return "";
		}
		
		int C_InvoicePaySchedule_ID = 0;
		if (Env.getContextAsInt (ctx, WindowNo, Env.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID.intValue ()
			&& Env.getContextAsInt (ctx, WindowNo, Env.TAB_INFO, "C_InvoicePaySchedule_ID") != 0)
			C_InvoicePaySchedule_ID = Env.getContextAsInt (ctx, WindowNo, Env.TAB_INFO, "C_InvoicePaySchedule_ID");

		//  Date
		Timestamp ts = Env.getContextAsDate(ctx, WindowNo, "DateAcct");     //  from C_Cash
		if (ts == null)
			ts = new Timestamp(System.currentTimeMillis());
		//
		String sql = "SELECT C_BPartner_ID, C_Currency_ID,"		//	1..2
			+ "invoiceOpen(C_Invoice_ID, ?), IsSOTrx, "			//	3..4
			+ "invoiceDiscount(C_Invoice_ID,?,?) "              //  5
			+ "FROM C_Invoice WHERE C_Invoice_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt (1, C_InvoicePaySchedule_ID);
			pstmt.setTimestamp (2, ts);
			pstmt.setInt (3, C_InvoicePaySchedule_ID);
			pstmt.setInt(4, C_Invoice_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				mTab.setValue("C_BPartner_ID", new Integer(rs.getInt(1))); //F3P:from genied adempiere
				mTab.setValue("C_Currency_ID", new Integer(rs.getInt(2)));
				BigDecimal PayAmt = rs.getBigDecimal(3);
				BigDecimal DiscountAmt = rs.getBigDecimal(5);
				boolean isSOTrx = "Y".equals(rs.getString(4));
				if (!isSOTrx)
				{
					PayAmt = PayAmt.negate();
					DiscountAmt = DiscountAmt.negate();
				}
				//
				mTab.setValue("Amount", PayAmt.subtract(DiscountAmt));
				mTab.setValue("DiscountAmt", DiscountAmt);
				mTab.setValue("WriteOffAmt", Env.ZERO);
				mTab.setValue ("OverUnderAmt", Env.ZERO); //F3P:from genied adempiere
				Env.setContext(ctx, WindowNo, "InvTotalAmt", PayAmt.toString());
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "invoice", e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return "";
	}	//	CashJournal_Invoice

	
	/**
	 *  Cash Journal Line Invoice Amounts.
	 *  when DiscountAnt, Amount, WriteOffAmt change
	 *  making sure that add up to InvTotalAmt (created by CashJournal_Invoice)
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String amounts (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		//  Needs to be Invoice
		if (isCalloutActive() || !"I".equals(mTab.getValue("CashType")))
			return "";
		//  Check, if InvTotalAmt exists
		String total = Env.getContext(ctx, WindowNo, "InvTotalAmt");
		if (total == null || total.length() == 0)
			return "";
		//BigDecimal InvTotalAmt = new BigDecimal(total); //F3P:from genied adempiere

		BigDecimal PayAmt = (BigDecimal)mTab.getValue("Amount");
		BigDecimal DiscountAmt = (BigDecimal)mTab.getValue("DiscountAmt");
		BigDecimal WriteOffAmt = (BigDecimal)mTab.getValue("WriteOffAmt");
		BigDecimal OverUnderAmt = (BigDecimal)mTab.getValue ("OverUnderAmt"); //F3P:from genied adempiere
		BigDecimal InvoiceOpenAmt = new BigDecimal(total); //F3P:from genied adempiere
		
		String colName = mField.getColumnName();
		if (log.isLoggable(Level.FINE)) log.fine(colName + " - Invoice/Order=" + InvoiceOpenAmt
			+ " - Amount=" + PayAmt + ", Discount=" + DiscountAmt + ", WriteOff=" + WriteOffAmt);

		/* F3P:from genied adempiere
		//  Amount - calculate write off
		if (colName.equals("Amount"))
		{
			WriteOffAmt = InvTotalAmt.subtract(PayAmt).subtract(DiscountAmt);
			mTab.setValue("WriteOffAmt", WriteOffAmt);
		}
		else    //  calculate PayAmt
		{
			PayAmt = InvTotalAmt.subtract(DiscountAmt).subtract(WriteOffAmt);
			mTab.setValue("Amount", PayAmt);
		}
		*/
		
		Integer C_Invoice_ID = mTab.getValue("C_Invoice_ID") == null ? 0 : (Integer)mTab.getValue("C_Invoice_ID");
		Integer C_Order_ID = mTab.getValue("C_Order_ID") == null ? 0 : (Integer)mTab.getValue("C_Order_ID");
		
		if (C_Invoice_ID == 0 && C_Order_ID == 0)
			   
		{
			if (Env.ZERO.compareTo (DiscountAmt) != 0)
				mTab.setValue ("DiscountAmt", Env.ZERO);
			if (Env.ZERO.compareTo (WriteOffAmt) != 0)
				mTab.setValue ("WriteOffAmt", Env.ZERO);
			if (Env.ZERO.compareTo (OverUnderAmt) != 0)
				mTab.setValue ("OverUnderAmt", Env.ZERO);
		} 
		else 
		{
			boolean processed = mTab.getValueAsBoolean(MCashLine.COLUMNNAME_Processed);
			// Always put difference to OverUnderAmt
			if (colName.equals ("Amount")
				&& (!processed))
			{
				OverUnderAmt = InvoiceOpenAmt.subtract (PayAmt).subtract (
					DiscountAmt).subtract (WriteOffAmt);
				mTab.setValue ("OverUnderAmt", OverUnderAmt);
			}
			// Added Lines By Goodwill (02-03-2006)
			// Reason: we must make the callout is called just when docstatus is
			// draft
			// Old Code : else // calculate PayAmt
			// New Code :
			else if ((!processed)) // calculate
			// PayAmt
			// End By Goodwill
			{
				PayAmt = InvoiceOpenAmt.subtract (DiscountAmt).subtract (
					WriteOffAmt).subtract (OverUnderAmt);
				mTab.setValue ("Amount", PayAmt);
			}
		}
		
		//F3P:from genied adempiere end

		return "";
	}	//	amounts
	
	//F3P:from genied adempiere
	/**
	 * Payment_Order. when Waiting Payment Order selected - set C_Currency_ID -
	 * C_BPartner_ID - DiscountAmt = C_Invoice_Discount (ID, DateTrx) - PayAmt =
	 * invoiceOpen (ID) - Discount - WriteOffAmt = 0
	 * @param ctx context
	 * @param WindowNo current Window No
	 * @param mTab Grid Tab
	 * @param mField Grid Field
	 * @param value New Value
	 * @return null or error message
	 */
	public String order(Properties ctx, int WindowNo, GridTab mTab,
		GridField mField, Object value)
	{
		Integer C_Order_ID = (Integer)value;
		if (isCalloutActive () // assuming it is resetting value
			|| C_Order_ID == null || C_Order_ID.intValue () == 0)
			return "";
		mTab.setValue ("C_Invoice_ID", null);
		mTab.setValue ("C_Charge_ID", null);
		mTab.setValue ("IsPrepayment", Boolean.TRUE);
		//
		mTab.setValue ("DiscountAmt", Env.ZERO);
		mTab.setValue ("WriteOffAmt", Env.ZERO);
		mTab.setValue ("IsOverUnderPayment", Boolean.FALSE);
		mTab.setValue ("OverUnderAmt", Env.ZERO);
		//
		String sql = "SELECT COALESCE(Bill_BPartner_ID, C_BPartner_ID) as C_BPartner_ID "
			+ ", C_Currency_ID "
			+ ", GrandTotal "
			+ ", IsSOTrx "
			+ "FROM C_Order WHERE C_Order_ID=?"; // #1
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, C_Order_ID.intValue ());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				mTab.setValue ("C_BPartner_ID", new Integer (rs.getInt (1)));
				int C_Currency_ID = rs.getInt (2); // Set Order Currency
				mTab.setValue ("C_Currency_ID", new Integer (C_Currency_ID));
				//
				BigDecimal GrandTotal = rs.getBigDecimal (3); // Set Pay
				// Amount
				if (GrandTotal == null)
					GrandTotal = Env.ZERO;
				boolean isSOTrx = "Y".equals(rs.getString(4));
				if (!isSOTrx)
				{
					GrandTotal = GrandTotal.negate();
				}
				mTab.setValue ("Amount", GrandTotal);
				Env.setContext(ctx, WindowNo, "InvTotalAmt", GrandTotal.toString());
			}
		}
		catch (SQLException e)
		{
			log.log (Level.SEVERE, sql, e);
			return e.getLocalizedMessage ();
		}
		finally
		{
			DB.close (rs, pstmt);
		}
		return "";
	} // order
	//F3P:from genied adempiere end

}	//	CalloutCashJournal
