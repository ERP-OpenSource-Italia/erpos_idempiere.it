package org.compiere.grid;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.GridTab;
import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.MRole;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.TimeUtil;

public abstract class PaymentFormCash extends PaymentForm {
	public static final String PAYMENTRULE = MInvoice.PAYMENTRULE_Cash;
	
	public PaymentFormCash(int WindowNo, GridTab mTab) {
		super(WindowNo, mTab);
	}
	
	public KeyNamePair selectedBankAccount;
	public ArrayList<KeyNamePair> getBankAccountList() {
		selectedBankAccount = null;
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		
		/**
		 *  Load Bank Accounts
		 */
		String SQL = MRole.getDefault().addAccessSQL(
			"SELECT C_BankAccount_ID, ba.Name || ' ' || AccountNo, IsDefault "
			+ "FROM C_BankAccount ba"
			+ " INNER JOIN C_Bank b ON (ba.C_Bank_ID=b.C_Bank_ID) "
			+ "WHERE b.IsActive='Y'",
			"ba", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int key = rs.getInt(1);
				String name = rs.getString(2);
				KeyNamePair pp = new KeyNamePair(key, name);
				list.add(pp);
				if (key == m_C_BankAccount_ID)
					selectedBankAccount = pp;
				if (selectedBankAccount == null && rs.getString(3).equals("Y"))    //  Default
					selectedBankAccount = pp;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException ept)
		{
			log.log(Level.SEVERE, SQL, ept);
		}
		
		return list;
	}
	
	public KeyNamePair selectedCashBook;
	public ArrayList<KeyNamePair> getCashBookList() {
		selectedCashBook = null;
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		
		/**
		 *  Load Cash Books
		 */
		String SQL = MRole.getDefault().addAccessSQL(
			"SELECT C_CashBook_ID, Name, AD_Org_ID FROM C_CashBook WHERE IsActive='Y'",
			"C_CashBook", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int key = rs.getInt(1);
				String name = rs.getString(2);
				KeyNamePair pp = new KeyNamePair(key, name);
				list.add(pp);
				if (key == m_C_CashBook_ID)
					selectedCashBook = pp;
				if (selectedCashBook == null && key == m_AD_Org_ID)       //  Default Org
					selectedCashBook = pp;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException epc)
		{
			log.log(Level.SEVERE, SQL, epc);
		}
		
		if (selectedCashBook != null)
		{
			if (m_C_CashBook_ID == 0)
				m_C_CashBook_ID = selectedCashBook.getKey();  //  set to default to avoid 'cashbook changed' message
		}
		
		return list;
	}
	
	public String processMsg;
	public boolean save(int newC_BankAccount_ID, int newC_CashBook_ID, Timestamp newDateAcct, BigDecimal newAmount, String trxName)
	{
		processMsg = null;
		boolean error = false;
		int newC_CashLine_ID = m_C_CashLine_ID;
		
		/***********************
		 *  Changed PaymentRule
		 */
		if (!PAYMENTRULE.equals(m_PaymentRule))
		{
			log.fine("Changed PaymentRule: " + m_PaymentRule + " -> " + PAYMENTRULE);
			//  We had a CashBook Entry
			log.fine("Old Cash - " + m_cashLine);
			if (m_cashLine != null)
			{
				MCashLine cl = m_cashLine.createReversal();
				if (cl.save())
					log.config( "CashCancelled");
				else
				{
					processMsg = "CashNotCancelled";
					error = true;
				}
			}
			newC_CashLine_ID = 0;      //  reset
		}
		
		//  Get Order and optionally Invoice
		int C_Order_ID = Env.getContextAsInt(Env.getCtx(), getWindowNo(), "C_Order_ID");
		int C_Invoice_ID = Env.getContextAsInt(Env.getCtx(), getWindowNo(), "C_Invoice_ID");
		if (C_Invoice_ID == 0 && m_DocStatus.equals("CO"))
			C_Invoice_ID = getInvoiceID (C_Order_ID);

		//  Amount sign negative, if ARC (Credit Memo) or API (AP Invoice)
		boolean negateAmt = false;
		MInvoice invoice = null;
		if (C_Invoice_ID != 0)
		{
			invoice = new MInvoice (Env.getCtx(), C_Invoice_ID, null);
			negateAmt = invoice.isCreditMemo();
		}
		MOrder order = null;
		if (invoice == null && C_Order_ID != 0)
			order = new MOrder (Env.getCtx(), C_Order_ID, null);
		
		BigDecimal payAmount = m_Amount;
		

		if (negateAmt)
			payAmount = m_Amount.negate();
		// Info
		log.config("C_Order_ID=" + C_Order_ID + ", C_Invoice_ID=" + C_Invoice_ID + ", NegateAmt=" + negateAmt);
		
		/***********************
		 *  CashBook
		 */
		if (!m_Cash_As_Payment)
		{
			log.fine("Cash");
			
			if (C_Invoice_ID == 0 && order == null)
			{
				log.config("No Invoice!");
				processMsg = "CashNotCreated";
				error = false;
			}
			else
			{
				payAmount = newAmount;
				//  Changed Amount
				if (m_cashLine != null
					&& payAmount.compareTo(m_cashLine.getAmount()) != 0)
				{
					log.config("Changed CashBook Amount");
					m_cashLine.setAmount(newAmount);
					m_cashLine.saveEx();
				}
				//	Different Date/CashBook
				if (m_cashLine != null
					&& (newC_CashBook_ID != m_C_CashBook_ID 
						|| !TimeUtil.isSameDay(m_cashLine.getStatementDate(), newDateAcct)))
				{
					log.config("Changed CashBook/Date: " + m_C_CashBook_ID + "->" + newC_CashBook_ID);
					MCashLine reverse = m_cashLine.createReversal();
					reverse.saveEx();
					m_cashLine = null;
				}
				
				//	Create new
				if (m_cashLine == null)
				{
					log.config("New CashBook");
					int C_Currency_ID = 0;
					if (invoice != null)
						C_Currency_ID = invoice.getC_Currency_ID();
					if (C_Currency_ID == 0 && order != null)
						C_Currency_ID = order.getC_Currency_ID();
					MCash cash = null;
					if (newC_CashBook_ID != 0)
						cash = MCash.get (Env.getCtx(), newC_CashBook_ID, newDateAcct, null);
					else	//	Default
						cash = MCash.get (Env.getCtx(), m_AD_Org_ID, newDateAcct, C_Currency_ID, null);
					if (cash == null || cash.get_ID() == 0)
					{
						processMsg = CLogger.retrieveErrorString("CashNotCreated");
						error = true;
					}
					else
					{
						MCashLine cl = new MCashLine (cash);
						// cl.setAmount(new BigDecimal(bAmountField.getText()));
						//ADialog.info(m_WindowNo, this, "m_cashLine - New Cashbook", "Amount: "+cl.getAmount());
						if (invoice != null)
							cl.setInvoice(invoice);	// overrides amount
						if (order != null)
						{
							cl.setOrder(order, null); // overrides amount
							m_needSave = true;
						}
						cl.setAmount(newAmount);
						cl.saveEx();
						log.config("CashCreated");						
						if (invoice == null && C_Invoice_ID != 0)
						{
							invoice = new MInvoice (Env.getCtx(), C_Invoice_ID, null);	
						}
						if (invoice != null) {
							invoice.setC_CashLine_ID(cl.getC_CashLine_ID());
							invoice.saveEx(trxName);
						}	
						if (order == null && C_Order_ID != 0)
						{
							order = new MOrder (Env.getCtx(), C_Order_ID, null);
						}
						if (order != null) {
							order.setC_CashLine_ID(cl.getC_CashLine_ID());
							order.saveEx(trxName);
						}
						log.config("Update Order & Invoice with CashLine");						
					}
				}
			}	//	have invoice
		}
		
		/***********************
		 *  Payments
		 */
		if (m_Cash_As_Payment)
		{
			log.fine("Payment - " + PAYMENTRULE);
			//  Set Amount
			m_mPayment.setAmount(m_C_Currency_ID, payAmount);
			// Get changes to cash amount
			m_mPayment.setTenderType(MPayment.TENDERTYPE_Cash);
			m_mPayment.setBankCash(newC_BankAccount_ID, m_isSOTrx, MPayment.TENDERTYPE_Cash);
			m_mPayment.setAmount(m_C_Currency_ID, payAmount);
			m_mPayment.setC_BPartner_ID(m_C_BPartner_ID);
			m_mPayment.setC_Invoice_ID(C_Invoice_ID);
			if (order != null)
			{
				m_mPayment.setC_Order_ID(C_Order_ID);
				m_needSave = true;
			}
			m_mPayment.setDateTrx(m_DateAcct);
			m_mPayment.setDateAcct(m_DateAcct);
			m_mPayment.saveEx();
			
			//  Save/Post
			if (m_mPayment.get_ID() > 0 && MPayment.DOCSTATUS_Drafted.equals(m_mPayment.getDocStatus()))
			{
				boolean ok = m_mPayment.processIt(DocAction.ACTION_Complete);
				m_mPayment.saveEx();
				if (ok)
					processMsg = m_mPayment.getDocumentNo();
				else
				{
					processMsg = "PaymentNotCreated";
					error = true;
				}
			}
			else
				log.fine("NotDraft " + m_mPayment);
		}
		
		/**********************
		 *	Save Values to mTab
		 */
		log.config("Saving changes");
		//
		if (!newDateAcct.equals(m_DateAcct))
			getGridTab().setValue("DateAcct", newDateAcct);
		//	Set Payment
		if (m_mPayment.getC_Payment_ID() != m_C_Payment_ID)
		{
			if (m_mPayment.getC_Payment_ID() == 0)
				getGridTab().setValue("C_Payment_ID", null);
			else
				getGridTab().setValue("C_Payment_ID", new Integer(m_mPayment.getC_Payment_ID()));
		}
		//	Set Cash
		if (newC_CashLine_ID != m_C_CashLine_ID)
		{
			if (newC_CashLine_ID == 0)
				getGridTab().setValue("C_CashLine_ID", null);
			else
				getGridTab().setValue("C_CashLine_ID", new Integer(newC_CashLine_ID));
		}
		return !error;
	}
}
