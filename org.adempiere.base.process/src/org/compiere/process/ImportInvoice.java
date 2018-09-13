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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;

import org.adempiere.model.ImportValidator;
import org.adempiere.process.ImportProcess;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.X_I_Invoice;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Import Invoice from I_Invoice
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportInvoice.java,v 1.1 2007/09/05 09:27:31 cruiz Exp $
 */
//F3P: added implements ImportProcess to fire model validator engine
public class ImportInvoice extends SvrProcess implements ImportProcess 
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Organization to be imported to		*/
	private int				m_AD_Org_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;
	/**	Document Action					*/
	private String			m_docAction = MInvoice.DOCACTION_Prepare;


	/** Effective						*/
	private Timestamp		m_DateValue = null;
	/** F3P: Document no*/
	private String 			m_documentNo;
	/** F3P: avoid partial import */
	private boolean 		m_bAvoidPartialImport = false;
	/** F3P: lista delle fatture da rollbackare */
	private Set<InvoiceForRollback> m_invoiceForRollback = new HashSet<InvoiceForRollback>();

	/** LS: sovrascrive o no il prezzo sulla linea della fattura dal listino prezzi**/
	private boolean m_bNotOverWritePrice = false;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("AD_Org_ID"))
				m_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else if (name.equals("DocAction"))
				m_docAction = (String)para[i].getParameter();
			else if (name.equals("DocumentNo")) //F3P: param added 
				m_documentNo = (String)para[i].getParameter();
			else if (name.equals("IsAvoidPartialImport")) //F3P: param added 
				m_bAvoidPartialImport = "Y".equals(para[i].getParameter());
			else if (name.equals("IsNotOverWritePrice")) //F3P: param added 
				m_bNotOverWritePrice = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return clear Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		boolean bErrorsOccurred = false; //F3P: remember if something gone wrong
		
		StringBuilder sql = null;
		int no = 0;
		StringBuilder clientCheck = new StringBuilder(getWhereClause()); // F3P: was " AND AD_Client_ID=" + m_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuilder ("DELETE I_Invoice ")
				  .append("WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET AD_Client_ID = COALESCE (AD_Client_ID,").append (m_AD_Client_ID).append ("),")
			  .append(" AD_Org_ID = COALESCE (AD_Org_ID,").append (m_AD_Org_ID).append ("),")
			  .append(" IsActive = COALESCE (IsActive, 'Y'),")
			  .append(" Created = COALESCE (Created, SysDate),")
			  .append(" CreatedBy = COALESCE (CreatedBy, 0),")
			  .append(" Updated = COALESCE (Updated, SysDate),")
			  .append(" UpdatedBy = COALESCE (UpdatedBy, 0),")
			  .append(" I_ErrorMsg = ' ',")
			  .append(" I_IsImported = 'N' ")
			  .append("WHERE I_IsImported<>'Y' OR I_IsImported IS NULL")
				.append(getDocumentNoFilter()); //F3P: added filter by docNo

		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.INFO)) log.info ("Reset=" + no);
		//F3P: fire import model validator
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_BEFORE_VALIDATE);

		sql = new StringBuilder ("UPDATE I_Invoice o ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Org, '")
			.append("WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0")
			.append(" OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))")
			.append(" AND I_IsImported<>'Y'").append (clientCheck)
			.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0) {
			log.warning ("Invalid Org=" + no);
			bErrorsOccurred = true; //F3P
		}

		//	Document Type - PO - SO
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName")
			  .append(" AND d.DocBaseType IN ('API','APC') AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set PO DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName")
			  .append(" AND d.DocBaseType IN ('ARI','ARC') AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set SO DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName")
			  .append(" AND d.DocBaseType IN ('API','ARI','APC','ARC') AND o.AD_Client_ID=d.AD_Client_ID) ")
			//+ "WHERE C_DocType_ID IS NULL AND IsSOTrx IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck);
			  .append("WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid DocTypeName, ' ")
			  .append("WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0) {
			log.warning ("Invalid DocTypeName=" + no);
			bErrorsOccurred = true; //F3P
		}
		//	DocType Default
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'")
			  .append(" AND d.DocBaseType='API' AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set PO Default DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'")
			  .append(" AND d.DocBaseType='ARI' AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set SO Default DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'")
			  .append(" AND d.DocBaseType IN('ARI','API') AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx IS NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set Default DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No DocType, ' ")
			  .append("WHERE C_DocType_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0) {
			log.warning ("No DocType=" + no);
			bErrorsOccurred = true; //F3P
		}

		//	Set IsSOTrx
		sql = new StringBuilder ("UPDATE I_Invoice o SET IsSOTrx='Y' ")
			  .append("WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='ARI' AND o.AD_Client_ID=d.AD_Client_ID)")
			  .append(" AND C_DocType_ID IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set IsSOTrx=Y=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o SET IsSOTrx='N' ")
			  .append("WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='API' AND o.AD_Client_ID=d.AD_Client_ID)")
			  .append(" AND C_DocType_ID IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set IsSOTrx=N=" + no);

		//	Price List
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'")
			  .append(" AND p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default Currency PriceList=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'")
			  .append(" AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default PriceList=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p ")
			  .append(" WHERE p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Currency PriceList=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p ")
			  .append(" WHERE p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set PriceList=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No PriceList, ' ")
			  .append("WHERE M_PriceList_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0) {
			log.warning("No PriceList=" + no);
			bErrorsOccurred = true; //F3P
		}

		// angelo (genied) Payment Term moved after BPartner

		// globalqss - Add project and activity
		//	Project
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_Project_ID=(SELECT C_Project_ID FROM C_Project p")
			  .append(" WHERE o.ProjectValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_Project_ID IS NULL AND ProjectValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Project=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Project, ' ")
				  .append("WHERE C_Project_ID IS NULL AND (ProjectValue IS NOT NULL)")
				  .append(" AND I_IsImported<>'Y'").append (clientCheck)
					.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Project=" + no);
		//	Activity
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_Activity_ID=(SELECT C_Activity_ID FROM C_Activity p")
			  .append(" WHERE o.ActivityValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_Activity_ID IS NULL AND ActivityValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Activity=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Activity, ' ")
				  .append("WHERE C_Activity_ID IS NULL AND (ActivityValue IS NOT NULL)")
				  .append(" AND I_IsImported<>'Y'").append (clientCheck)
					.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Activity=" + no);
		// globalqss - add charge
		//	Charge
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_Charge_ID=(SELECT C_Charge_ID FROM C_Charge p")
			  .append(" WHERE o.ChargeName=p.Name AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_Charge_ID IS NULL AND ChargeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Charge=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Charge, ' ")
				  .append("WHERE C_Charge_ID IS NULL AND (ChargeName IS NOT NULL)")
				  .append(" AND I_IsImported<>'Y'").append (clientCheck)
					.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Charge=" + no);
		//
		
		//	BP from EMail
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u")
			  .append(" WHERE o.EMail=u.EMail AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) ")
			  .append("WHERE C_BPartner_ID IS NULL AND EMail IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP from EMail=" + no);
		//	BP from ContactName
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u")
			  .append(" WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) ")
			  .append("WHERE C_BPartner_ID IS NULL AND ContactName IS NOT NULL")
			  .append(" AND EXISTS (SELECT Name FROM AD_User u WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL GROUP BY Name HAVING COUNT(*)=1)")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP from ContactName=" + no);
		//	BP from Value
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp")
			  .append(" WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) ")
			  .append("WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP from Value=" + no);
		//	Default BP
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_BPartner_ID=(SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo c")
			  .append(" WHERE o.AD_Client_ID=c.AD_Client_ID) ")
			  .append("WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NULL AND Name IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default BP=" + no);
		
//	Payment Term
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_PaymentTerm_ID=(SELECT C_PaymentTerm_ID FROM C_PaymentTerm p")
			  .append(" WHERE o.PaymentTermValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_PaymentTerm_ID IS NULL AND PaymentTermValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		// angelo (genied) set PaymentTerm from BPartner 
		sql = new StringBuilder ("UPDATE I_Invoice o ")
					.append("SET C_PaymentTerm_ID=(SELECT C_PaymentTerm_ID FROM C_BPartner bp")
					.append(" WHERE o.C_BPartner_ID=bp.C_BPartner_ID AND o.AD_Client_ID=bp.AD_Client_ID) ")
					.append("WHERE C_PaymentTerm_ID IS NULL AND C_BPartner_ID IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
					.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set PaymentTerm from BPartner=" + no);
				
		if (log.isLoggable(Level.FINE)) log.fine("Set PaymentTerm=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_PaymentTerm_ID=(SELECT MAX(C_PaymentTerm_ID) FROM C_PaymentTerm p")
			  .append(" WHERE p.IsDefault='Y' AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_PaymentTerm_ID IS NULL AND o.PaymentTermValue IS NULL AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default PaymentTerm=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No PaymentTerm, ' ")
			  .append("WHERE C_PaymentTerm_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0) {
			log.warning ("No PaymentTerm=" + no);
			bErrorsOccurred = true; //F3P
		}

		//	Existing Location ? Exact Match
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_BPartner_Location_ID=(SELECT C_BPartner_Location_ID")
			  .append(" FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)")
			  .append(" WHERE o.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=o.AD_Client_ID")
			  .append(" AND DUMP(o.Address1)=DUMP(l.Address1) AND DUMP(o.Address2)=DUMP(l.Address2)")
			  .append(" AND DUMP(o.City)=DUMP(l.City) AND DUMP(o.Postal)=DUMP(l.Postal)")
			  .append(" AND o.C_Region_ID=l.C_Region_ID AND o.C_Country_ID=l.C_Country_ID) ")
			  .append("WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL")
			  .append(" AND I_IsImported='N'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Found Location=" + no);
		//	Set Location from BPartner
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_BPartner_Location_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l")
			  .append(" WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID")
			  .append(" AND ((l.IsBillTo='Y' AND o.IsSOTrx='Y') OR o.IsSOTrx='N')")
			  .append(") ")
			  .append("WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP Location from BP=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No BP Location, ' ")
			  .append("WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0) {
			log.warning ("No BP Location=" + no);
			bErrorsOccurred = true; //F3P
		}

		//	Set Country
		/**
		sql = new StringBuffer ("UPDATE I_Invoice o "
			  + "SET CountryCode=(SELECT MAX(CountryCode) FROM C_Country c WHERE c.IsDefault='Y'"
			  + " AND c.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND CountryCode IS NULL AND C_Country_ID IS NULL"
			  + " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Country Default=" + no);
		**/
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c")
			  .append(" WHERE o.CountryCode=c.CountryCode AND c.AD_Client_ID IN (0, o.AD_Client_ID)) ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL AND CountryCode IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Country=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Country, ' ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0){
			log.warning ("Invalid Country=" + no);
			bErrorsOccurred = true; //F3P
		}

		//	Set Region
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("Set RegionName=(SELECT MAX(Name) FROM C_Region r")
			  .append(" WHERE r.IsDefault='Y' AND r.C_Country_ID=o.C_Country_ID")
			  .append(" AND r.AD_Client_ID IN (0, o.AD_Client_ID)) ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Region Default=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r")
			  .append(" WHERE r.Name=o.RegionName AND r.C_Country_ID=o.C_Country_ID")
			  .append(" AND r.AD_Client_ID IN (0, o.AD_Client_ID)) ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Region=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Region, ' ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL ")
			  .append(" AND EXISTS (SELECT * FROM C_Country c")
			  .append(" WHERE c.C_Country_ID=o.C_Country_ID AND c.HasRegion='Y')")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0){
			log.warning ("Invalid Region=" + no);
			bErrorsOccurred = true; //F3P
		}

		//	Product
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p")
			  .append(" WHERE o.ProductValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Product from Value=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p")
			  .append(" WHERE o.UPC=p.UPC AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_Product_ID IS NULL AND UPC IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Product from UPC=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p")
			  .append(" WHERE o.SKU=p.SKU AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_Product_ID IS NULL AND SKU IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Product fom SKU=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Product, ' ")
			  .append("WHERE M_Product_ID IS NULL AND (ProductValue IS NOT NULL OR UPC IS NOT NULL OR SKU IS NOT NULL)")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0){
			log.warning ("Invalid Product=" + no);
			bErrorsOccurred = true; //F3P
		}

		// globalqss - charge and product are exclusive
		sql = new StringBuilder ("UPDATE I_Invoice ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Product and Charge, ' ")
				  .append("WHERE M_Product_ID IS NOT NULL AND C_Charge_ID IS NOT NULL ")
				  .append(" AND I_IsImported<>'Y'").append (clientCheck)
					.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0){
			log.warning ("Invalid Product and Charge exclusive=" + no);
			bErrorsOccurred = true; //F3P
		}

			//	Tax
		sql = new StringBuilder ("UPDATE I_Invoice o ")
			  .append("SET C_Tax_ID=(SELECT MAX(C_Tax_ID) FROM C_Tax t")
			  .append(" WHERE o.TaxIndicator=t.TaxIndicator AND o.AD_Client_ID=t.AD_Client_ID) ")
			  .append("WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Tax=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Tax, ' ")
			  .append("WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0){
			log.warning ("Invalid Tax=" + no);
			bErrorsOccurred = true; //F3P
		}
		
		// Set 1099 Box
		sql = new StringBuilder ("UPDATE I_Invoice o ")
				.append("SET C_1099Box_ID=(SELECT C_1099Box_ID FROM C_1099Box a")
				.append(" WHERE o.C_1099Box_Value=a.Value AND a.AD_Client_ID = o.AD_Client_ID) ")
				.append(" WHERE C_1099Box_ID IS NULL and C_1099Box_Value IS NOT NULL")
				.append(" AND I_IsImported<>'Y' AND IsSOTrx='N'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set C_1099Box_ID=" + no);
		sql = new StringBuilder ("UPDATE I_Invoice ")
				.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid C_1099Box_Value, ' ")
				.append("WHERE C_1099Box_ID IS NULL AND (C_1099Box_Value IS NOT NULL)")
				.append(" AND I_IsImported<>'Y' AND IsSOTrx='N'").append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0){
			log.warning ("Invalid C_1099Box_Value=" + no);
			bErrorsOccurred = true; //F3P
		}
		
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_AFTER_VALIDATE); //F3P
		
		commitEx();
		
		//	-- New BPartner ---------------------------------------------------

		//	Go through Invoice Records w/o C_BPartner_ID
		sql = new StringBuilder ("SELECT * FROM I_Invoice ")
			  .append("WHERE I_IsImported='N' AND C_BPartner_ID IS NULL").append (clientCheck)
	  		.append(getDocumentNoFilter()); //F3P: added filter by docNo;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				X_I_Invoice imp = new X_I_Invoice (getCtx(), rs, get_TrxName());
				if (imp.getBPartnerValue () == null)
				{
					if (imp.getEMail () != null)
						imp.setBPartnerValue (imp.getEMail ());
					else if (imp.getName () != null)
						imp.setBPartnerValue (imp.getName ());
					else
						continue;
				}
				if (imp.getName () == null)
				{
					if (imp.getContactName () != null)
						imp.setName (imp.getContactName ());
					else
						imp.setName (imp.getBPartnerValue ());
				}
				//	BPartner
				MBPartner bp = MBPartner.get (getCtx(), imp.getBPartnerValue(), get_TrxName());
				if (bp == null)
				{
					bp = new MBPartner (getCtx (), -1, get_TrxName());
					bp.setClientOrg (imp.getAD_Client_ID (), imp.getAD_Org_ID ());
					bp.setValue (imp.getBPartnerValue ());
					bp.setName (imp.getName ());
					if (!bp.save ())
						continue;
				}
				imp.setC_BPartner_ID (bp.getC_BPartner_ID ());
				
				//	BP Location
				MBPartnerLocation bpl = null; 
				MBPartnerLocation[] bpls = bp.getLocations(true);
				for (int i = 0; bpl == null && i < bpls.length; i++)
				{
					if (imp.getC_BPartner_Location_ID() == bpls[i].getC_BPartner_Location_ID())
						bpl = bpls[i];
					//	Same Location ID
					else if (imp.getC_Location_ID() == bpls[i].getC_Location_ID())
						bpl = bpls[i];
					//	Same Location Info
					else if (imp.getC_Location_ID() == 0)
					{
						MLocation loc = bpls[i].getLocation(false);
						if (loc.equals(imp.getC_Country_ID(), imp.getC_Region_ID(), 
								imp.getPostal(), "", imp.getCity(), 
								imp.getAddress1(), imp.getAddress2()))
							bpl = bpls[i];
					}
				}
				if (bpl == null)
				{
					//	New Location
					MLocation loc = new MLocation (getCtx (), 0, get_TrxName());
					loc.setAddress1 (imp.getAddress1 ());
					loc.setAddress2 (imp.getAddress2 ());
					loc.setCity (imp.getCity ());
					loc.setPostal (imp.getPostal ());
					if (imp.getC_Region_ID () != 0)
						loc.setC_Region_ID (imp.getC_Region_ID ());
					loc.setC_Country_ID (imp.getC_Country_ID ());
					if (!loc.save ())
						continue;
					//
					bpl = new MBPartnerLocation (bp);
					bpl.setC_Location_ID (imp.getC_Location_ID() > 0 ? imp.getC_Location_ID() : loc.getC_Location_ID());
					if (!bpl.save ())
						continue;
				}
				imp.setC_Location_ID (bpl.getC_Location_ID ());
				imp.setC_BPartner_Location_ID (bpl.getC_BPartner_Location_ID ());
				
				//	User/Contact
				if (imp.getContactName () != null 
					|| imp.getEMail () != null 
					|| imp.getPhone () != null)
				{
					MUser[] users = bp.getContacts(true);
					MUser user = null;
					for (int i = 0; user == null && i < users.length;  i++)
					{
						String name = users[i].getName();
						if (name.equals(imp.getContactName()) 
							|| name.equals(imp.getName()))
						{
							user = users[i];
							imp.setAD_User_ID (user.getAD_User_ID ());
						}
					}
					if (user == null)
					{
						user = new MUser (bp);
						if (imp.getContactName () == null)
							user.setName (imp.getName ());
						else
							user.setName (imp.getContactName ());
						user.setEMail (imp.getEMail ());
						user.setPhone (imp.getPhone ());
						if (user.save ())
							imp.setAD_User_ID (user.getAD_User_ID ());
					}
				}
				imp.saveEx();
			}	//	for all new BPartners
			//
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "CreateBP", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		sql = new StringBuilder ("UPDATE I_Invoice ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No BPartner, ' ")
			  .append("WHERE C_BPartner_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo;

		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0){
			log.warning ("No BPartner=" + no);
			bErrorsOccurred = true; //F3P
		}
		
		commitEx();
		
		//	-- New Invoices -----------------------------------------------------

		int noInsert = 0;
		int noInsertLine = 0;
		
		//	Go through Invoice Records w/o
		// Angelo Dabala' (genied) nectosoft add invoice date and documentno to order clause
		sql = new StringBuilder ("SELECT * FROM I_Invoice ")
			.append( "WHERE I_IsImported='N'")
			//F3P: Import only full-doc. Does not proceed with ones that has at least one line with error 
			.append("AND NOT EXISTS (SELECT * FROM I_INVOICE i WHERE I_INVOICE.documentno = i.documentNo AND I_IsImported='E'")
			.append(clientCheck).append(")")
			//F3P: End
			.append (clientCheck)
			.append(getDocumentNoFilter()) //F3P: added filter by docNo
			.append(" ORDER BY C_BPartner_ID, C_BPartner_Location_ID, DateInvoiced, DocumentNo, C_DocType_ID, I_Invoice_ID");
		
		
		MInvoice invoice = null; //F3P: moved here to be able to delete it if import fails
		X_I_Invoice imp = null,  //F3P: moved definition outside of while cycle
								previousImp = null, //F3P: needed to keep track of last impoirt line of current invoice when there is a break
								potentialErrorImp = null; // F3P: needed to keep track of wich imp may be causing errors
		LinkedList<X_I_Invoice> lstImportedIInvoice = new LinkedList<X_I_Invoice>(); //F3P

		try
		{
			//F3P
			boolean bSingleLineImportOk = true;

			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			rs = pstmt.executeQuery ();
			//	Group Change
			int oldC_BPartner_ID = 0;
			int oldC_BPartner_Location_ID = 0;
			String oldDocumentNo = "";

			int lineNo = 0;
			while (rs.next ())
			{
					imp = new X_I_Invoice (getCtx (), rs, get_TrxName()); // F3P: get in transaction
					potentialErrorImp = imp; // F3P: mark processing imp
				String cmpDocumentNo = imp.getDocumentNo();
				if (cmpDocumentNo == null)
					cmpDocumentNo = "";
				//	New Invoice
				if (oldC_BPartner_ID != imp.getC_BPartner_ID() 
					|| oldC_BPartner_Location_ID != imp.getC_BPartner_Location_ID()
					|| !oldDocumentNo.equals(cmpDocumentNo)	)
					{
						//salviamo la fattura precedente
						if (bSingleLineImportOk && invoice != null)
					{
							//F3P: fire import model validator
							potentialErrorImp = previousImp; // F3P: mark processing imp
							ModelValidationEngine.get().fireImportValidate(this, previousImp, invoice, ImportValidator.TIMING_AFTER_IMPORT);
						if (!invoice.processIt(m_docAction)) {
							log.warning("Invoice Process Failed: " + invoice + " - " + invoice.getProcessMsg());
							throw new IllegalStateException("Invoice Process Failed: " + invoice + " - " + invoice.getProcessMsg());
							
						}
						invoice.saveEx();
							commitEx();
							//F3P:End
					}
						potentialErrorImp = imp; // F3P: mark processing imp
					//	Group Change
					oldC_BPartner_ID = imp.getC_BPartner_ID();
					oldC_BPartner_Location_ID = imp.getC_BPartner_Location_ID();
					oldDocumentNo = imp.getDocumentNo();
					if (oldDocumentNo == null)
						oldDocumentNo = "";
					//
						//F3P: reset list
						lstImportedIInvoice = new LinkedList<X_I_Invoice>();
					invoice = new MInvoice (getCtx(), 0, null);
					invoice.setClientOrg (imp.getAD_Client_ID(), imp.getAD_Org_ID());
					invoice.setC_DocTypeTarget_ID(imp.getC_DocType_ID());
					invoice.setIsSOTrx(imp.isSOTrx());
					if (imp.getDocumentNo() != null)
						invoice.setDocumentNo(imp.getDocumentNo());
					//
					invoice.setC_BPartner_ID(imp.getC_BPartner_ID());
					invoice.setC_BPartner_Location_ID(imp.getC_BPartner_Location_ID());
					if (imp.getAD_User_ID() != 0)
						invoice.setAD_User_ID(imp.getAD_User_ID());
					//
					if (imp.getDescription() != null)
						invoice.setDescription(imp.getDescription());
					invoice.setC_PaymentTerm_ID(imp.getC_PaymentTerm_ID());
					invoice.setM_PriceList_ID(imp.getM_PriceList_ID());
					//	SalesRep from Import or the person running the import
					if (imp.getSalesRep_ID() != 0)
						invoice.setSalesRep_ID(imp.getSalesRep_ID());
					if (invoice.getSalesRep_ID() == 0)
						invoice.setSalesRep_ID(getAD_User_ID());
					//
					if (imp.getAD_OrgTrx_ID() != 0)
						invoice.setAD_OrgTrx_ID(imp.getAD_OrgTrx_ID());
					if (imp.getC_Activity_ID() != 0)
						invoice.setC_Activity_ID(imp.getC_Activity_ID());
					if (imp.getC_Campaign_ID() != 0)
						invoice.setC_Campaign_ID(imp.getC_Campaign_ID());
					if (imp.getC_Project_ID() != 0)
						invoice.setC_Project_ID(imp.getC_Project_ID());
					//
					if (imp.getDateInvoiced() != null)
						invoice.setDateInvoiced(imp.getDateInvoiced());
					if (imp.getDateAcct() != null)
						invoice.setDateAcct(imp.getDateAcct());
					//
					//F3P: replaced with saveEx
					//invoice.saveEx();
					invoice.saveEx(get_TrxName());
					previousImp = imp;						
					noInsert++;
					lineNo = 10;
				}
				imp.setC_Invoice_ID (invoice.getC_Invoice_ID());
				//	New InvoiceLine
				MInvoiceLine line = new MInvoiceLine (invoice);
				
				if (imp.getLineDescription() != null)
					line.setDescription(imp.getLineDescription());
				line.setLine(lineNo);
				lineNo += 10;
				if (imp.getM_Product_ID() != 0)
					line.setM_Product_ID(imp.getM_Product_ID(), true);
				// globalqss - import invoice with charges
				if (imp.getC_Charge_ID() != 0)
					line.setC_Charge_ID(imp.getC_Charge_ID());
				// globalqss - [2855673] - assign dimensions to lines also in case they're different 
				if (imp.getC_Activity_ID() != 0)
					line.setC_Activity_ID(imp.getC_Activity_ID());
				if (imp.getC_Campaign_ID() != 0)
					line.setC_Campaign_ID(imp.getC_Campaign_ID());
				if (imp.getC_Project_ID() != 0)
					line.setC_Project_ID(imp.getC_Project_ID());
				//
				line.setQty(imp.getQtyOrdered());
				
				line.setPrice();

				BigDecimal price = imp.getPriceActual();
				if (price != null && Env.ZERO.compareTo(price) != 0)
					line.setPrice(price);
				else if(m_bNotOverWritePrice) //LS: overwrite price when is 0
					line.setPrice(price);
				
				BigDecimal pricelist = imp.getPriceList();
				if (pricelist != null && Env.ZERO.compareTo(pricelist) != 0)
					line.setPriceList(pricelist);
				else if(m_bNotOverWritePrice) //LS: overwrite price when is 0
					line.setPriceList(pricelist);
				
				if (imp.getC_Tax_ID() != 0)
					line.setC_Tax_ID(imp.getC_Tax_ID());
				else
				{
					line.setTax();
					imp.setC_Tax_ID(line.getC_Tax_ID());
				}
				BigDecimal taxAmt = imp.getTaxAmt();
				if (taxAmt != null && Env.ZERO.compareTo(taxAmt) != 0)
					line.setTaxAmt(taxAmt);
				line.setC_1099Box_ID(imp.getC_1099Box_ID());
				
				ModelValidationEngine.get().fireImportValidate(this, imp, line, ImportValidator.TIMING_BEFORE_IMPORT);
				
				//F3P: was saveEx)=
				line.saveEx(get_TrxName());
				//F3P: save this line as imported
				lstImportedIInvoice.add(imp);
				//
				imp.setC_InvoiceLine_ID(line.getC_InvoiceLine_ID());
				imp.setI_IsImported(true);
				imp.setProcessed(true);
				//
					//F3P: was save
	//				if (imp.save()) 
	//					noInsertLine++;
					
					imp.saveEx(get_TrxName());
					noInsertLine++;
					bSingleLineImportOk = true;
					//nel caso in cui ci sia stato un errore in precedenza e non siano abilitate le importazioni parziali,
					//marchiamo questa linea da rollbackare
					if(m_bAvoidPartialImport && bErrorsOccurred)
						markInvoiceForRollback(invoice, lstImportedIInvoice, imp, "another line has a problem");
					//F3P: End
			}
			if (bSingleLineImportOk && invoice != null)
			{
				// F3P: added validator
				potentialErrorImp = imp;
				ModelValidationEngine.get().fireImportValidate(this, imp, invoice, ImportValidator.TIMING_AFTER_IMPORT);
				
				if(!invoice.processIt (m_docAction)) {
					log.warning("Invoice Process Failed: " + invoice + " - " + invoice.getProcessMsg());
					throw new IllegalStateException("Invoice Process Failed: " + invoice + " - " + invoice.getProcessMsg());
					
				}
					//F3P: replaced with saveEx
					//invoice.save();
					invoice.saveEx(get_TrxName());
			}
		}
		catch (Exception e)
		{
			rollback();
			log.log(Level.SEVERE, "CreateInvoice", e);
			bErrorsOccurred = true;
			//F3P: Doesn't allow partially imported doc
			markInvoiceForRollback(invoice, lstImportedIInvoice, potentialErrorImp, e.getMessage());
			
			if(potentialErrorImp != null)
			{
				sql = new StringBuilder ("UPDATE I_Invoice ")
					.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR='||?||', ' ")
					.append(" WHERE I_Invoice_ID =?");
				  			
				Object params[] = {e.getMessage(), potentialErrorImp.getI_Invoice_ID()}; 
				DB.executeUpdate(sql.toString(), params, false, null);
			}
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		//F3P: errors from invoice was not logged in I_Invoice, fixed
		//	Set Error to indicator to not imported
		/*sql = new StringBuffer ("UPDATE I_Invoice "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		*/
				
		sql = new StringBuilder ("UPDATE I_Invoice ")
				.append("SET I_IsImported='E' ")
				.append("WHERE I_IsImported<>'Y'")
				.append (clientCheck)
				.append(getDocumentNoFilter()); //F3P: added filter by docNo;
		
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		//if error occurred and partial imports are not allowed, rollback
		if(m_bAvoidPartialImport && no > 0)
		{
			rollback();
			noInsert = 0;
			noInsertLine = 0;
		}
		//le linee che rientrano in questa casistica non sono state processate per errori su altre linee
		sql = new StringBuilder ("UPDATE I_Invoice ")
				  .append("SET I_ErrorMsg='ERR=another line has a problem' ")
				  .append("WHERE I_IsImported<>'Y' AND (I_ErrorMsg IS NULL OR I_ErrorMsg = ' ' OR I_ErrorMsg = '')")
					.append (clientCheck)
					.append(getDocumentNoFilter()); //F3P: added filter by docNo;
		
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		//gli errori della singola linea di importazione vengono popolati in questo punto,
		//dopo l'eventule rollback, altrimenti verrebbero persi  
		rollbackMarkedInvoice();
		//F3P: End
		addLog (0, null, new BigDecimal (no), "@Errors@");
		//
		addLog (0, null, new BigDecimal (noInsert), "@C_Invoice_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertLine), "@C_InvoiceLine_ID@: @Inserted@");
		return "";
	}	//	doIt
	
	//F3P
	private void markInvoiceForRollback(MInvoice invoice, LinkedList<X_I_Invoice> lstImportedIInvoice, X_I_Invoice imp, String errorMsg)
	{
		m_invoiceForRollback.add(new InvoiceForRollback(invoice, lstImportedIInvoice, imp, errorMsg));
	}
	
	private void rollbackMarkedInvoice()
	{
		for(InvoiceForRollback entry : m_invoiceForRollback)
		{
			MInvoice invoice = entry.getInvoice();
			LinkedList<X_I_Invoice> lstImportedIInvoice = entry.getLstImportedIInvoice(); 
			X_I_Invoice imp = entry.getImp(); 
			String errorMsg = entry.getErrorMsg();
			
			//delete previosly created doc
			invoice.delete(false);
			//mark all already imported lines as not imported
			for(X_I_Invoice impInvLine : lstImportedIInvoice)
			{
				impInvLine.setI_IsImported(false);
				impInvLine.setI_ErrorMsg("ERR=another line has a problem");
				impInvLine.setProcessed(false);
				impInvLine.saveEx(get_TrxName());
			}
			//save exception in actual line
			imp.setI_ErrorMsg("ERR=" + errorMsg);
			imp.setProcessed(false);
			imp.setC_Invoice_ID(0);
			imp.setC_InvoiceLine_ID(0);
			imp.saveEx(get_TrxName());
		}
	}
	
	@Override
	public String getImportTableName() {
		return X_I_Invoice.Table_Name;
	}

	@Override
	public String getWhereClause() {
		return " AND AD_Client_ID=" + m_AD_Client_ID;
	}
	
	/**
	 * Returns sql used to filter by documentNo, if it was populated in the 
	 * process startup window. Otherwise it will returns an empty string that 
	 * will not affect any query.
	 * @return
	 */
	public String getDocumentNoFilter() {
		String sqlFilter = "";
		if(m_documentNo != null && m_documentNo.length() > 0)
			sqlFilter = " AND DocumentNo = '" + m_documentNo + "'";
		
		return sqlFilter;
	}
	
	private class InvoiceForRollback{
		protected MInvoice invoice;
		protected LinkedList<X_I_Invoice> lstImportedIInvoice; 
		protected X_I_Invoice imp; 
		protected String errorMsg;
		
		public InvoiceForRollback(MInvoice invoice, LinkedList<X_I_Invoice> lstImportedIInvoice, X_I_Invoice imp, String errorMsg) {
			this.invoice = invoice;
			this.lstImportedIInvoice = lstImportedIInvoice;
			this.imp = imp;
			this.errorMsg = errorMsg;
		}

		public MInvoice getInvoice() {
			return invoice;
		}

		public LinkedList<X_I_Invoice> getLstImportedIInvoice() {
			return lstImportedIInvoice;
		}

		public X_I_Invoice getImp() {
			return imp;
		}

		public String getErrorMsg() {
			return errorMsg;
		}
	}
	//F3P: End

}	//	ImportInvoice