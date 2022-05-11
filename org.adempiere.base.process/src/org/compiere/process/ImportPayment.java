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
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.model.ImportValidator;
import org.adempiere.process.ImportProcess;
import org.compiere.model.MBankAccount;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.X_I_Payment;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;


/**
 * 	Import Payments
 *	
 *  @author Jorg Janke
 *  @version $Id: ImportPayment.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Contributor(s):
 *    Carlos Ruiz - globalqss - FR [ 1992542 ] Import Payment doesn't have DocAction parameter
 */
public class ImportPayment extends SvrProcess implements ImportProcess
{
	/**	Organization to be imported to	*/
	protected int				p_AD_Org_ID = 0;
	/** Default Bank Account			*/
	protected int				p_C_BankAccount_ID = 0;
	/**	Delete old Imported				*/
	protected boolean			p_deleteOldImported = false;
	/**	Document Action					*/
	protected String			m_docAction = null;

	/** Properties						*/
	protected Properties 		m_ctx;

	// Angelo Dabala' (genied) - nectosoft - added IsValidateOnly and IsImportOnlyNoErrors parameters
	/**	Don't import					*/
	protected boolean			m_IsValidateOnly = false;
	/** Import if no Errors				*/
	protected boolean			m_IsImportOnlyNoErrors = true;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_BankAccount_ID"))
				p_C_BankAccount_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_deleteOldImported = "Y".equals(para[i].getParameter());
			else if (name.equals("DocAction"))
				m_docAction = (String)para[i].getParameter();
			// Angelo Dabala' (genied) - nectosoft
			else if (name.equals("IsValidateOnly"))
				m_IsValidateOnly = "Y".equals(para[i].getParameter()); 
			else if (name.equals("IsImportOnlyNoErrors"))
				m_IsImportOnlyNoErrors = "Y".equals(para[i].getParameter());
			// Angelo Dabala' (genied) - nectosoft end
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_ctx = Env.getCtx();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		if (log.isLoggable(Level.INFO))  // Angelo Dabala' (genied) - nectosoft - added IsValidateOnly and IsImportOnlyNoErrors parameters
			log.info("C_BankAccount_ID=" + p_C_BankAccount_ID + " IsValidateOnly=" + m_IsValidateOnly + ", IsImportOnlyNoErrors=" + m_IsImportOnlyNoErrors);

		MBankAccount ba = MBankAccount.get(getCtx(), p_C_BankAccount_ID);
		if (p_C_BankAccount_ID == 0 || ba.get_ID() != p_C_BankAccount_ID)
			throw new AdempiereUserError("@NotFound@ @C_BankAccount_ID@ - " + p_C_BankAccount_ID);
		 if (p_AD_Org_ID != ba.getAD_Org_ID() && ba.getAD_Org_ID() != 0 && p_AD_Org_ID == 0)
			p_AD_Org_ID = ba.getAD_Org_ID();
		
		
		if (log.isLoggable(Level.INFO)) log.info("AD_Org_ID=" + p_AD_Org_ID);
		
		StringBuilder sql = null;
		int no = 0;

		//	****	Prepare	****

		//	Delete Old Imported
		if (p_deleteOldImported)
		{
			sql = new StringBuilder ("DELETE I_Payment ")
				  .append("WHERE I_IsImported='Y'");
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuilder ("UPDATE I_Payment ")
			  .append("SET AD_Client_ID = COALESCE (AD_Client_ID,").append (ba.getAD_Client_ID()).append ("),")
			  .append(" AD_Org_ID = COALESCE (AD_Org_ID,").append (p_AD_Org_ID).append ("),");
		sql.append(" IsActive = COALESCE (IsActive, 'Y'),")
			  .append(" Created = COALESCE (Created, SysDate),")
			  .append(" CreatedBy = COALESCE (CreatedBy, 0),")
			  .append(" Updated = COALESCE (Updated, SysDate),")
			  .append(" UpdatedBy = COALESCE (UpdatedBy, 0),")
			  .append(" I_ErrorMsg = ' ',")
			  .append(" I_IsImported = 'N' ")
			  .append("WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL OR AD_Client_ID IS NULL OR AD_Org_ID IS NULL OR AD_Client_ID=0 OR AD_Org_ID=0) ")
			  .append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.INFO)) log.info ("Reset=" + no);
		
		// F3P: added validate event
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_BEFORE_VALIDATE);

		sql = new StringBuilder ("UPDATE I_Payment o ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Org, '")
			.append("WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0")
			.append(" OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))")
			.append(" AND I_IsImported<>'Y'").append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Org=" + no);
			
		//	Set Bank Account
		sql = new StringBuilder("UPDATE I_Payment i ")
			.append("SET C_BankAccount_ID=")
			.append("( ")
			.append(" SELECT C_BankAccount_ID ")
			.append(" FROM C_BankAccount a, C_Bank b ")
			.append(" WHERE b.IsOwnBank='Y' ")
			.append(" AND a.AD_Client_ID=i.AD_Client_ID ")
			.append(" AND a.AD_Org_ID=i.AD_Org_ID ")//add ad_org
			.append(" AND a.C_Bank_ID=b.C_Bank_ID ")
			.append(" AND a.AccountNo=i.BankAccountNo ")
			.append(" AND b.RoutingNo=i.RoutingNo ")
			.append(" OR b.SwiftCode=i.RoutingNo ")
			.append(") ")
			.append("WHERE i.C_BankAccount_ID IS NULL ")
			.append("AND (i.I_IsImported<>'Y' ")
			.append("OR i.I_IsImported IS NULL)").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Bank Account (With Routing No)=" + no);
		//
		sql = new StringBuilder("UPDATE I_Payment i ") 
		 	.append("SET C_BankAccount_ID=")
			.append("( ")
			.append(" SELECT C_BankAccount_ID ")
			.append(" FROM C_BankAccount a, C_Bank b ")
			.append(" WHERE b.IsOwnBank='Y' ")
			.append(" AND a.C_Bank_ID=b.C_Bank_ID ") 
			.append(" AND a.AccountNo=i.BankAccountNo ")
			.append(" AND a.AD_Org_ID=i.AD_Org_ID ")//add ad_org
			.append(" AND a.AD_Client_ID=i.AD_Client_ID ")
			.append(") ")
			.append("WHERE i.C_BankAccount_ID IS NULL ")
			.append("AND (i.I_isImported<>'Y' ")
			.append("OR i.I_isImported IS NULL)").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Bank Account (Without Routing No)=" + no);
		//
		sql = new StringBuilder("UPDATE I_Payment i ")
			.append("SET C_BankAccount_ID=(SELECT C_BankAccount_ID FROM C_BankAccount a WHERE a.C_BankAccount_ID=").append(p_C_BankAccount_ID);
		sql.append(" and a.AD_Client_ID=i.AD_Client_ID) ")
			.append("WHERE i.C_BankAccount_ID IS NULL ")
			.append("AND i.BankAccountNo IS NULL ")
			.append("AND (i.I_isImported<>'Y' ")
			.append("OR i.I_isImported IS NULL)").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Bank Account=" + no);
		//	
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET I_isImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Bank Account, ' ")
			.append("WHERE C_BankAccount_ID IS NULL ")
			.append("AND (I_isImported<>'Y' ")
			.append("OR I_isImported IS NULL)").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("Invalid Bank Account=" + no);
		 
		//	Set Currency
		sql = new StringBuilder ("UPDATE I_Payment i ")
			.append("SET C_Currency_ID=(SELECT C_Currency_ID FROM C_Currency c")
			.append(" WHERE i.ISO_Code=c.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) ")
			.append("WHERE C_Currency_ID IS NULL")
			.append(" AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Set Currency=" + no);
		//
		sql = new StringBuilder("UPDATE I_Payment i ")
			.append("SET C_Currency_ID=(SELECT C_Currency_ID FROM C_BankAccount WHERE C_BankAccount_ID=i.C_BankAccount_ID) ")
			.append("WHERE i.C_Currency_ID IS NULL ")
			.append("AND i.ISO_Code IS NULL").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Set Currency=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Payment ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Currency,' ")
			.append("WHERE C_Currency_ID IS NULL ")
			.append("AND I_IsImported<>'E' ")
			.append(" AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("No Currency=" + no);
		 
		//	Set Amount
		sql = new StringBuilder("UPDATE I_Payment ")
		 	.append("SET ChargeAmt=0 ")
			.append("WHERE ChargeAmt IS NULL ")
			.append("AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Charge Amount=" + no);
		//
		sql = new StringBuilder("UPDATE I_Payment ")
		 	.append("SET TaxAmt=0 ")
			.append("WHERE TaxAmt IS NULL ")
			.append("AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Tax Amount=" + no);
		//
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET WriteOffAmt=0 ")
			.append("WHERE WriteOffAmt IS NULL ")
			.append("AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("WriteOff Amount=" + no);
		//
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET DiscountAmt=0 ")
			.append("WHERE DiscountAmt IS NULL ")
			.append("AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Discount Amount=" + no);
		//
			
		//	Set Date
		sql = new StringBuilder("UPDATE I_Payment ")
		 	.append("SET DateTrx=Created ")
			.append("WHERE DateTrx IS NULL ")
			.append("AND I_isImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Trx Date=" + no);
		
		sql = new StringBuilder("UPDATE I_Payment ")
		 	.append("SET DateAcct=DateTrx ")
			.append("WHERE DateAcct IS NULL ")
			.append("AND I_isImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Acct Date=" + no);
		
		// Angelo Dabala' (genied) - nectosoft - Assign BPartner before Invoice
		//	BPartner
		//	BPartner
		sql = new StringBuilder ("UPDATE I_Payment i ")
			  .append("SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp")
			  .append(" WHERE i.BPartnerValue=bp.Value AND i.AD_Client_ID=bp.AD_Client_ID) ")
			  .append("WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set BP from Value=" + no);
		
		//	Invoice

		// Angelo Dabala' (genied) - nectosoft - Add BPartner match to Invoice
		// F3P: added check for ispaid and ad_org_id
		sql = new StringBuilder ("UPDATE I_Payment i ")
			  .append("SET C_Invoice_ID=(SELECT MAX(C_Invoice_ID) FROM C_Invoice ii")
			  .append(" WHERE i.InvoiceDocumentNo=ii.DocumentNo AND i.InvoiceDateInvoiced=ii.DateInvoiced AND i.AD_Client_ID=ii.AD_Client_ID ") // F3P: aggiunto date invoiced
			  .append(" AND i.C_BPartner_ID = ii.C_BPartner_ID AND ii.ispaid = 'N' AND ii.AD_Org_ID = ")
			  .append(p_AD_Org_ID).append(") ")
			  .append("WHERE C_Invoice_ID IS NULL AND InvoiceDocumentNo IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set Invoice from DocumentNo=" + no);
		
		//	Check invoice
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'Err=No Invoice, ' ")
			.append("WHERE InvoiceDocumentNo IS NOT null and C_Invoice_ID IS NULL AND I_IsImported<>'Y'")
			.append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("No invoice=" + no);				
		
		sql = new StringBuilder ("UPDATE I_Payment i ")
			  .append("SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_Invoice ii")
			  .append(" WHERE i.C_Invoice_ID=ii.C_Invoice_ID AND i.AD_Client_ID=ii.AD_Client_ID) ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Invoice_ID IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set BP from Invoice=" + no);
		
		sql = new StringBuilder ("UPDATE I_Payment ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No BPartner,' ")
			.append("WHERE C_BPartner_ID IS NULL ")
			.append("AND I_IsImported<>'E' ")
			.append(" AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("No BPartner=" + no);
		
		
		//	Check Payment<->Invoice combination
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'Err=Invalid Payment<->Invoice, ' ")
			.append("WHERE I_Payment_ID IN ")
				.append("(SELECT I_Payment_ID ")
				.append("FROM I_Payment i")
				.append(" INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) ")
				.append("WHERE i.C_Invoice_ID IS NOT NULL ")
				.append(" AND p.C_Invoice_ID IS NOT NULL ")
				.append(" AND p.C_Invoice_ID<>i.C_Invoice_ID) ")
			.append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Payment<->Invoice Mismatch=" + no);
			
		//	Check Payment<->BPartner combination
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'Err=Invalid Payment<->BPartner, ' ")
			.append("WHERE I_Payment_ID IN ")
				.append("(SELECT I_Payment_ID ")
				.append("FROM I_Payment i")
				.append(" INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) ")
				.append("WHERE i.C_BPartner_ID IS NOT NULL ")
				.append(" AND p.C_BPartner_ID IS NOT NULL ")
				.append(" AND p.C_BPartner_ID<>i.C_BPartner_ID) ")
			.append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Payment<->BPartner Mismatch=" + no);
			
		//	Check Invoice<->BPartner combination
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'Err=Invalid Invoice<->BPartner, ' ")
			.append("WHERE I_Payment_ID IN ")
				.append("(SELECT I_Payment_ID ")
				.append("FROM I_Payment i")
				.append(" INNER JOIN C_Invoice v ON (i.C_Invoice_ID=v.C_Invoice_ID) ")
				.append("WHERE i.C_BPartner_ID IS NOT NULL ")
				.append(" AND v.C_BPartner_ID IS NOT NULL ")
				.append(" AND v.C_BPartner_ID<>i.C_BPartner_ID) ")
			.append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Invoice<->BPartner Mismatch=" + no);
			
		//	Check Invoice.BPartner<->Payment.BPartner combination
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'Err=Invalid Invoice.BPartner<->Payment.BPartner, ' ")
			.append("WHERE I_Payment_ID IN ")
				.append("(SELECT I_Payment_ID ")
				.append("FROM I_Payment i")
				.append(" INNER JOIN C_Invoice v ON (i.C_Invoice_ID=v.C_Invoice_ID)")
				.append(" INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) ")
				.append("WHERE p.C_Invoice_ID<>v.C_Invoice_ID")
				.append(" AND v.C_BPartner_ID<>p.C_BPartner_ID) ")
			.append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("Invoice.BPartner<->Payment.BPartner Mismatch=" + no);
			
		//	TrxType
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET TrxType='S' ")	//	MPayment.TRXTYPE_Sales
			.append("WHERE TrxType IS NULL ")
			.append("AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("TrxType Default=" + no);
		
		//	TenderType
		sql = new StringBuilder("UPDATE I_Payment ")
			.append("SET TenderType='K' ")	//	MPayment.TENDERTYPE_Check
			.append("WHERE TenderType IS NULL ")
			.append("AND I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.INFO)) log.info("TenderType Default=" + no);

		//	Document Type
		sql = new StringBuilder ("UPDATE I_Payment i ")
			  .append("SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=i.DocTypeName")
			  .append(" AND d.DocBaseType IN ('ARR','APP') AND i.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Payment ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid DocTypeName, ' ")
			  .append("WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid DocTypeName=" + no);
		sql = new StringBuilder ("UPDATE I_Payment ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No DocType, ' ")
			  .append("WHERE C_DocType_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("No DocType=" + no);
		
		// F3P: added validate event
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_AFTER_VALIDATE);

		commitEx();
		
		// Angelo Dabala' (genied) - nectosoft - Check for errors and/or return if validate only
		int errors = DB.getSQLValue(get_TrxName(), 
			"SELECT COUNT(*) FROM I_Payment WHERE I_IsImported NOT IN ('Y','N')" + getWhereClause());
		if (errors != 0)
		{
			if (m_IsValidateOnly || m_IsImportOnlyNoErrors)
				throw new Exception ("@Errors@=" + errors);
		}
		else if (m_IsValidateOnly)
			return "@Errors@=" + errors;

		log.info("Validation Errors=" + errors);
		
		//Import Bank Statement
		sql = new StringBuilder("SELECT * FROM I_Payment")
			.append(" WHERE I_IsImported='N'").append(getWhereClause())
			.append(" ORDER BY C_BankAccount_ID, CheckNo, DateTrx, R_AuthCode");
			
		MBankAccount account = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noInsert = 0;
		int processErr = 0;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();
				
			while (rs.next())
			{ 
				X_I_Payment imp = new X_I_Payment(m_ctx, rs, get_TrxName());
				//	Get the bank account
				if (account == null || account.getC_BankAccount_ID() != imp.getC_BankAccount_ID())
				{
					account = MBankAccount.get (m_ctx, imp.getC_BankAccount_ID());
					if (log.isLoggable(Level.INFO)) log.info("New Account=" + account.getAccountNo());
				}
				
				//	New Payment
				MPayment payment = new MPayment (m_ctx, 0, get_TrxName());
				payment.setAD_Org_ID(imp.getAD_Org_ID());
				payment.setDocumentNo(imp.getDocumentNo());
				payment.setPONum(imp.getPONum());
				
				payment.setTrxType(imp.getTrxType());
				payment.setTenderType(imp.getTenderType());
				
				payment.setC_BankAccount_ID(imp.getC_BankAccount_ID());
				payment.setRoutingNo(imp.getRoutingNo());
				payment.setAccountNo(imp.getAccountNo());
				payment.setIBAN(imp.getIBAN());
				payment.setSwiftCode(imp.getSwiftCode());
				payment.setCheckNo(imp.getCheckNo());
				payment.setMicr(imp.getMicr());
				
				if (imp.getCreditCardType() != null)
					payment.setCreditCardType(imp.getCreditCardType());
				payment.setCreditCardNumber(imp.getCreditCardNumber());
				if (imp.getCreditCardExpMM() != 0)
					payment.setCreditCardExpMM(imp.getCreditCardExpMM());
				if (imp.getCreditCardExpYY() != 0)
					payment.setCreditCardExpYY(imp.getCreditCardExpYY());
				payment.setCreditCardVV(imp.getCreditCardVV());
				payment.setSwipe(imp.getSwipe());
				
				payment.setDateAcct(imp.getDateTrx());
				payment.setDateTrx(imp.getDateTrx());
			//	payment.setDescription(imp.getDescription());
				//Cristiano Lazzaro (genied) - Save in the description the invoice number not found
				if (imp.getC_Invoice_ID() == 0)
					payment.setDescription(imp.getInvoiceDocumentNo());
				//
				payment.setC_BPartner_ID(imp.getC_BPartner_ID());
				payment.setC_Invoice_ID(imp.getC_Invoice_ID());
				payment.setC_DocType_ID(imp.getC_DocType_ID());
				payment.setC_Currency_ID(imp.getC_Currency_ID());
			//	payment.setC_ConversionType_ID(imp.getC_ConversionType_ID());
				payment.setC_Charge_ID(imp.getC_Charge_ID());
				payment.setChargeAmt(imp.getChargeAmt());
				payment.setTaxAmt(imp.getTaxAmt());
				
				payment.setPayAmt(imp.getPayAmt());
				payment.setWriteOffAmt(imp.getWriteOffAmt());
				payment.setDiscountAmt(imp.getDiscountAmt());
				payment.setWriteOffAmt(imp.getWriteOffAmt());
				// Angelo Dabala' (genied) - nectosoft - Assign missing fields
				payment.setOverUnderAmt(imp.getOverUnderAmt());
				payment.setIsOverUnderPayment(imp.isOverUnderPayment());
				
				//	Copy statement line reference data
				payment.setA_City(imp.getA_City());
				payment.setA_Country(imp.getA_Country());
				payment.setA_EMail(imp.getA_EMail());
				payment.setA_Ident_DL(imp.getA_Ident_DL());
				payment.setA_Ident_SSN(imp.getA_Ident_SSN());
				payment.setA_Name(imp.getA_Name());
				payment.setA_State(imp.getA_State());
				payment.setA_Street(imp.getA_Street());
				payment.setA_Zip(imp.getA_Zip());
				payment.setR_AuthCode(imp.getR_AuthCode());
				payment.setR_Info(imp.getR_Info());
				payment.setR_PnRef(imp.getR_PnRef());
				payment.setR_RespMsg(imp.getR_RespMsg());
				payment.setR_Result(imp.getR_Result());
				payment.setOrig_TrxID(imp.getOrig_TrxID());
				payment.setVoiceAuthCode(imp.getVoiceAuthCode());
				
				ModelValidationEngine.get().fireImportValidate(this,imp, payment, ImportValidator.TIMING_BEFORE_IMPORT);
				
				//	Save payment
				if (payment.save())
				{
					ModelValidationEngine.get().fireImportValidate(this,imp, payment, ImportValidator.TIMING_AFTER_IMPORT);
					
					imp.setC_Payment_ID(payment.getC_Payment_ID());
					imp.setI_IsImported(true);
					imp.setProcessed(true);
					imp.saveEx();
					noInsert++;

					//GM: Set DOCSTATUS_Invalid if non Completed
					if (payment != null && m_docAction != null && m_docAction.length() > 0)
					{
						Trx trx = Trx.get(get_TrxName(), false);
						Savepoint processSavepoint = trx.setSavepoint(null);
						StringBuilder errMsg= null;
						try
						{
							if(!payment.processIt (m_docAction)) 
							{
								errMsg = new StringBuilder("Order Process Failed: ")
										.append(payment.getDocumentNo()).append(" - ")
										.append(payment.getProcessMsg());
								throw new IllegalStateException("Payment Process Failed: " + payment + " - " + payment.getProcessMsg());
							}
						}
						catch(Exception e)
						{
							log.warning(e.getMessage());
						}
						finally
						{
							if(errMsg != null)
								trx.rollback(processSavepoint);

							trx.releaseSavepoint(processSavepoint);
							processSavepoint = null;
						}					

						if(errMsg != null){
							payment.setDocStatus(MPayment.DOCSTATUS_Invalid);	
							processErr++;
						}
						payment.saveEx();
					}
				}
			}
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		//	Set Error to indicator to not imported
		sql = new StringBuilder ("UPDATE I_Payment ")
			.append("SET I_IsImported='N', Updated=SysDate ")
			.append("WHERE I_IsImported<>'Y'").append(getWhereClause());
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		//GM: Error on process
		addLog (0, null, new BigDecimal(processErr), "@Errors@: @Process@");
		addLog (0, null, new BigDecimal (noInsert), "@C_Payment_ID@: @Inserted@");
		StringBuilder msgReturn = new StringBuilder("#").append(noInsert);
		return msgReturn.toString();
	}	//	doIt

	@Override
	public String getImportTableName() {
		return X_I_Payment.Table_Name;
	}

	@Override
	public String getWhereClause() {
		StringBuilder whereClause = new StringBuilder(" AND AD_Org_ID in (0,").append(p_AD_Org_ID).append(") ");
		return whereClause.toString();
	}
	
}	//	ImportPayment
