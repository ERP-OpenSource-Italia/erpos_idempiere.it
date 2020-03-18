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
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;

import it.idempiere.base.model.FreeOfCharge;
import it.idempiere.base.model.LITMPaySchedule;
import it.idempiere.base.util.STDSysConfig;

/**
 *	Invoice Payment Schedule Model 
 *	
 *  @author Jorg Janke
 *  
 *  @author Silvano Trinchero, Freepath www.freepath.it
 *		<li> FR [ 3432213 ]
 *  @version $Id: MInvoicePaySchedule.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInvoicePaySchedule extends X_C_InvoicePaySchedule
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4613382619117842586L;

	/**
	 * 	Get Payment Schedule of the invoice
	 * 	@param ctx context
	 * 	@param C_Invoice_ID invoice id (direct)
	 * 	@param C_InvoicePaySchedule_ID id (indirect)
	 *	@param trxName transaction
	 *	@return array of schedule
	 */
	public static MInvoicePaySchedule[] getInvoicePaySchedule(Properties ctx, 
		int C_Invoice_ID, int C_InvoicePaySchedule_ID, String trxName)
	{
		StringBuilder sql = new StringBuilder("SELECT * FROM C_InvoicePaySchedule ips WHERE IsActive='Y' ");
		if (C_Invoice_ID != 0)
			sql.append("AND C_Invoice_ID=? ");
		else
			sql.append("AND EXISTS (SELECT * FROM C_InvoicePaySchedule x")
			.append(" WHERE x.C_InvoicePaySchedule_ID=? AND ips.C_Invoice_ID=x.C_Invoice_ID) ");
		sql.append("ORDER BY DueDate");
		//
		ArrayList<MInvoicePaySchedule> list = new ArrayList<MInvoicePaySchedule>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trxName);
			if (C_Invoice_ID != 0)
				pstmt.setInt(1, C_Invoice_ID);
			else
				pstmt.setInt(1, C_InvoicePaySchedule_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				list.add (new MInvoicePaySchedule(ctx, rs, trxName));
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getInvoicePaySchedule", e); 
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		MInvoicePaySchedule[] retValue = new MInvoicePaySchedule[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getSchedule

	/** Static Logger					*/
	private static CLogger		s_log = CLogger.getCLogger (MInvoicePaySchedule.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_InvoicePaySchedule_ID id
	 *	@param trxName transaction
	 */
	public MInvoicePaySchedule (Properties ctx, int C_InvoicePaySchedule_ID, String trxName)
	{
		super(ctx, C_InvoicePaySchedule_ID, trxName);
		if (C_InvoicePaySchedule_ID == 0)
		{
		//	setC_Invoice_ID (0);
		//	setDiscountAmt (Env.ZERO);
		//	setDiscountDate (new Timestamp(System.currentTimeMillis()));
		//	setDueAmt (Env.ZERO);
		//	setDueDate (new Timestamp(System.currentTimeMillis()));
			setIsValid (false);
		}
	}	//	MInvoicePaySchedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MInvoicePaySchedule (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoicePaySchedule

	/**
	 * 	Parent Constructor
	 *	@param invoice invoice
	 *	@param paySchedule payment schedule
	 */
	public MInvoicePaySchedule (MInvoice invoice, MPaySchedule paySchedule)
	{
		super (invoice.getCtx(), 0, invoice.get_TrxName());
		m_parent = invoice;
		setClientOrg(invoice);
		setC_Invoice_ID(invoice.getC_Invoice_ID());
		setC_PaySchedule_ID(paySchedule.getC_PaySchedule_ID());
		
		//	Amounts
		int scale = MCurrency.getStdPrecision(getCtx(), invoice.getC_Currency_ID());
		BigDecimal due = invoice.getGrandTotal();
		
		// F3P: manage free of charge
		
		BigDecimal bdFreeOfCharge = FreeOfCharge.getFreeOfChargeAmt(invoice);
		
		if(bdFreeOfCharge.signum() != 0)
		{
			due = due.subtract(bdFreeOfCharge);
		}
		
		// F3P: end
		
		if (due.compareTo(Env.ZERO) == 0)
		{
			setDueAmt (Env.ZERO);
			setDiscountAmt (Env.ZERO);
			setIsValid(false);
		}
		else
		{
			due = due.multiply(paySchedule.getPercentage())
				.divide(Env.ONEHUNDRED, scale, RoundingMode.HALF_UP);
			setDueAmt (due);
			BigDecimal discount = due.multiply(paySchedule.getDiscount())
				.divide(Env.ONEHUNDRED, scale, RoundingMode.HALF_UP);
			setDiscountAmt (discount);
			setIsValid(true);
		}
		
		//	Dates		
		// FR3432213: if PaymentTerm is DueFixed at end of the month and scheduled at multiple of 30, add months
		
		//LS custom date field as starting date instead of DateInv
		//if missing use standard
		Timestamp customStartDate = null;
		String customFieldDate = STDSysConfig.getInvoicePayScheduleCustomDateField(
				Env.getAD_Client_ID(getCtx()), Env.getAD_Org_ID(getCtx()));
		if (!Util.isEmpty(customFieldDate)){
			Object date = invoice.get_Value(customFieldDate);
			if (date instanceof Timestamp){
				customStartDate = (Timestamp) date;
			}
		}
		if (customStartDate == null){
			customStartDate = invoice.getDateInvoiced();
		}
		
		if(LITMPaySchedule.isDueFixed(paySchedule))
		{
			MPaymentTerm payTerm = paySchedule.getParent();
			
			String sql = "SELECT paymenttermduedate(?, ?) FROM C_PAYMENTTERM WHERE C_PAYMENTTERM_ID = ?";
			Timestamp startDate = DB.getSQLValueTS(invoice.get_TrxName(), sql
					, payTerm.getC_PaymentTerm_ID(), customStartDate, payTerm.getC_PaymentTerm_ID());
			
			int months = LITMPaySchedule.getFixMonthOffset(paySchedule);

			// add months to calculate Due Date, adjust day if PaymentTerm > calDate.DAY
	
			Calendar calDate = GregorianCalendar.getInstance();
			calDate.setTime(startDate);
			calDate.add(Calendar.MONTH, months);
			
			int iMonthDay =  payTerm.getFixMonthDay();
			
			if(iMonthDay > calDate.get(Calendar.DAY_OF_MONTH))
			{
				iMonthDay = Math.min(iMonthDay, calDate.getActualMaximum(Calendar.DAY_OF_MONTH));				
				calDate.set(Calendar.DAY_OF_MONTH,iMonthDay);
			}
						
			setDueDate (TimeUtil.getDay(calDate.getTimeInMillis()));
		}
		else // old behaviour
		{		
//			Timestamp dueDate = TimeUtil.addDays(invoice.getDateInvoiced(), paySchedule.getNetDays());
			Timestamp dueDate = TimeUtil.addDays(customStartDate, paySchedule.getNetDays());
			setDueDate (dueDate);
		}
		
//		Timestamp discountDate = TimeUtil.addDays(invoice.getDateInvoiced(), paySchedule.getDiscountDays());
		Timestamp discountDate = TimeUtil.addDays(customStartDate, paySchedule.getDiscountDays());
		setDiscountDate (discountDate);
	}	//	MInvoicePaySchedule
	
	/**	Parent						*/
	private MInvoice	m_parent = null;

	
	/**
	 * @return Returns the parent.
	 */
	public MInvoice getParent ()
	{
		if (m_parent == null)
			//F3P po.get
			m_parent = PO.get(getCtx(), MInvoice.Table_Name, getC_Invoice_ID(), get_TrxName()); 
		return m_parent;
	}	//	getParent
	
	/**
	 * @param parent The parent to set.
	 */
	public void setParent (MInvoice parent)
	{
		m_parent = parent;
	}	//	setParent
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("MInvoicePaySchedule[");
		sb.append(get_ID()).append("-Due=").append(getDueDate()).append("/").append(getDueAmt())
			.append(";Discount=").append(getDiscountDate()).append("/").append(getDiscountAmt())
			.append("]");
		return sb.toString();
	}	//	toString
	
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (is_ValueChanged("DueAmt"))
		{
			log.fine("beforeSave");
			setIsValid(false);
		}
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (is_ValueChanged("DueAmt") || is_ValueChanged("IsActive"))
		{
			log.fine("afterSave");
			getParent();
			m_parent.validatePaySchedule();
			m_parent.saveEx();
		}
		return success;
	}	//	afterSave

	@Override
	protected boolean afterDelete(boolean success) {
		if (!success)
			return success;
		log.fine("afterDelete");
		getParent();
		m_parent.validatePaySchedule();
		m_parent.saveEx();
		return success;
	}	

}	//	MInvoicePaySchedule
