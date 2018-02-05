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
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.logging.Level;

import org.adempiere.model.ImportValidator;
import org.adempiere.process.ImportProcess;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.PO;
import org.compiere.model.X_I_Order;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import it.idempiere.base.util.STDUtils;

/**
 *	Import Order from I_Order
 *  @author Oscar Gomez
 * 			<li>BF [ 2936629 ] Error when creating bpartner in the importation order
 * 			<li>https://sourceforge.net/tracker/?func=detail&aid=2936629&group_id=176962&atid=879332
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportOrder.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 * 
 *  @author freepath  IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
 */
public class ImportOrder extends SvrProcess implements ImportProcess
{
	/**	Client to be imported to		*/
	protected int				m_AD_Client_ID = 0;
	/**	Organization to be imported to		*/
	protected int				m_AD_Org_ID = 0;
	/**	Delete old Imported				*/
	protected boolean			m_deleteOldImported = false;
	/**	Document Action					*/
	protected String			m_docAction = MOrder.DOCACTION_Prepare;
	/** F3P: Document no*/
	protected String 			m_documentNo;

	/** Effective						*/
	protected Timestamp		m_DateValue = null;

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
			else
				// F3P: this process can be overrided to be driven by external import, reduced level of log for unknown params
				log.log(Level.INFO, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuilder sql = null;
		int no = 0;
		String clientCheck = getWhereClause();//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuilder ("DELETE I_Order ")
				  .append("WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuilder ("UPDATE I_Order ")
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

		//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_BEFORE_VALIDATE);
		
		sql = new StringBuilder ("UPDATE I_Order o ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Org, '")
			.append("WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0")
			.append(" OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))")
			.append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Org=" + no);

		//	Document Type - PO - SO
		sql = new StringBuilder ("UPDATE I_Order o ")	//	PO Document Type Name
			  .append("SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName")
			  .append(" AND d.DocBaseType='POO' AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set PO DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")	//	SO Document Type Name
			  .append("SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName")
			  .append(" AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set SO DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName")
			  .append(" AND d.DocBaseType IN ('SOO','POO') AND o.AD_Client_ID=d.AD_Client_ID) ")
			//+ "WHERE C_DocType_ID IS NULL AND IsSOTrx IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck);
			  .append("WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Order ")	//	Error Invalid Doc Type Name
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid DocTypeName, ' ")
			  .append("WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid DocTypeName=" + no);
		//	DocType Default
		sql = new StringBuilder ("UPDATE I_Order o ")	//	Default PO
			  .append("SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'")
			  .append(" AND d.DocBaseType='POO' AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set PO Default DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")	//	Default SO
			  .append("SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'")
			  .append(" AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set SO Default DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'")
			  .append(" AND d.DocBaseType IN('SOO','POO') AND o.AD_Client_ID=d.AD_Client_ID) ")
			  .append("WHERE C_DocType_ID IS NULL AND IsSOTrx IS NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default DocType=" + no);
		sql = new StringBuilder ("UPDATE I_Order ")	// No DocType
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No DocType, ' ")
			  .append("WHERE C_DocType_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("No DocType=" + no);

		//	Set IsSOTrx
		sql = new StringBuilder ("UPDATE I_Order o SET IsSOTrx='Y' ")
			  .append("WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID)")
			  .append(" AND C_DocType_ID IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set IsSOTrx=Y=" + no);
		sql = new StringBuilder ("UPDATE I_Order o SET IsSOTrx='N' ")
			  .append("WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='POO' AND o.AD_Client_ID=d.AD_Client_ID)")
			  .append(" AND C_DocType_ID IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set IsSOTrx=N=" + no);

		//	Price List
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'")
			  .append(" AND p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default Currency PriceList=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'")
			  .append(" AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default PriceList=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p ")
			  .append(" WHERE p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Currency PriceList=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p ")
			  .append(" WHERE p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set PriceList=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No PriceList, ' ")
			  .append("WHERE M_PriceList_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("No PriceList=" + no);

		// @Trifon - Import Order Source
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_OrderSource_ID=(SELECT C_OrderSource_ID FROM C_OrderSource p")
			  .append(" WHERE o.C_OrderSourceValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_OrderSource_ID IS NULL AND C_OrderSourceValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Order Source=" + no);
		// Set proper error message
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Not Found Order Source, ' ")
			  .append("WHERE C_OrderSource_ID IS NULL AND C_OrderSourceValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("No OrderSource=" + no);
		
		// angelo (genied) Payment Term moved after BPartner

		// angelo (genied) assign Project from ProjectValue
		sql = new StringBuilder ("UPDATE I_Order o ")
			.append("SET C_Project_ID=(SELECT C_Project_ID FROM C_Project p")
			.append(" WHERE o.ProjectValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			.append("WHERE C_Project_ID IS NULL AND ProjectValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Project=" + no);
		sql = new StringBuilder ("UPDATE I_Order ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Project, ' ")
			.append("WHERE C_Project_ID IS NULL AND (ProjectValue IS NOT NULL)")
			.append(" AND I_IsImported<>'Y'").append (clientCheck)
			.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Project=" + no);
		//	angelo (genied) assign Activity from ActivityValue
		sql = new StringBuilder ("UPDATE I_Order o ")
			.append("SET C_Activity_ID=(SELECT C_Activity_ID FROM C_Activity p")
			.append(" WHERE o.ActivityValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			.append("WHERE C_Activity_ID IS NULL AND ActivityValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Activity=" + no);
	
		sql = new StringBuilder ("UPDATE I_Order ")
			.append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Activity, ' ")
			.append("WHERE C_Activity_ID IS NULL AND (ActivityValue IS NOT NULL)")
			.append(" AND I_IsImported<>'Y'").append (clientCheck)
			.append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Activity=" + no);
		//angelo end

		//	Warehouse
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_Warehouse_ID=(SELECT MAX(M_Warehouse_ID) FROM M_Warehouse w")
			  .append(" WHERE o.AD_Client_ID=w.AD_Client_ID AND o.AD_Org_ID=w.AD_Org_ID) ")
			  .append("WHERE M_Warehouse_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());	//	Warehouse for Org
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set Warehouse=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_Warehouse_ID=(SELECT M_Warehouse_ID FROM M_Warehouse w")
			  .append(" WHERE o.AD_Client_ID=w.AD_Client_ID) ")
			  .append("WHERE M_Warehouse_ID IS NULL")
			  .append(" AND EXISTS (SELECT AD_Client_ID FROM M_Warehouse w WHERE w.AD_Client_ID=o.AD_Client_ID GROUP BY AD_Client_ID HAVING COUNT(*)=1)")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			if (log.isLoggable(Level.FINE)) log.fine("Set Only Client Warehouse=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Warehouse, ' ")
			  .append("WHERE M_Warehouse_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("No Warehouse=" + no);

		//	BP from EMail
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u")
			  .append(" WHERE o.EMail=u.EMail AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) ")
			  .append("WHERE C_BPartner_ID IS NULL AND EMail IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP from EMail=" + no);
		//	BP from ContactName
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u")
			  .append(" WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) ")
			  .append("WHERE C_BPartner_ID IS NULL AND ContactName IS NOT NULL")
			  .append(" AND EXISTS (SELECT Name FROM AD_User u WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL GROUP BY Name HAVING COUNT(*)=1)")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP from ContactName=" + no);
		//	BP from Value
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp")
			  .append(" WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) ")
			  .append("WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP from Value=" + no);
		//	Default BP
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_BPartner_ID=(SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo c")
			  .append(" WHERE o.AD_Client_ID=c.AD_Client_ID) ")
			  .append("WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NULL AND Name IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default BP=" + no);

		// angelo (genied) Payment Term moved after BPartner
		// Payment Term
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_PaymentTerm_ID=(SELECT C_PaymentTerm_ID FROM C_PaymentTerm p")
			  .append(" WHERE o.PaymentTermValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_PaymentTerm_ID IS NULL AND PaymentTermValue IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set PaymentTerm=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_PaymentTerm_ID=(SELECT MAX(C_PaymentTerm_ID) FROM C_PaymentTerm p")
			  .append(" WHERE p.IsDefault='Y' AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE C_PaymentTerm_ID IS NULL AND o.PaymentTermValue IS NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Default PaymentTerm=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No PaymentTerm, ' ")
			  .append("WHERE C_PaymentTerm_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("No PaymentTerm=" + no);
		
		//	Existing Location ? Exact Match
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET (BillTo_ID,C_BPartner_Location_ID)=(SELECT C_BPartner_Location_ID,C_BPartner_Location_ID")
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
		//	Set Bill Location from BPartner
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET BillTo_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l")
			  .append(" WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID")
			  .append(" AND ((l.IsBillTo='Y' AND o.IsSOTrx='Y') OR (l.IsPayFrom='Y' AND o.IsSOTrx='N'))")
			  .append(") ")
			  .append("WHERE C_BPartner_ID IS NOT NULL AND BillTo_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP BillTo from BP=" + no);
		//	Set Location from BPartner
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_BPartner_Location_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l")
			  .append(" WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID")
			  .append(" AND ((l.IsShipTo='Y' AND o.IsSOTrx='Y') OR o.IsSOTrx='N')")
			  .append(") ")
			  .append("WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set BP Location from BP=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No BP Location, ' ")
			  .append("WHERE C_BPartner_ID IS NOT NULL AND (BillTo_ID IS NULL OR C_BPartner_Location_ID IS NULL)")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("No BP Location=" + no);

		//	Set Country
		/**
		sql = new StringBuffer ("UPDATE I_Order o "
			  + "SET CountryCode=(SELECT MAX(CountryCode) FROM C_Country c WHERE c.IsDefault='Y'"
			  + " AND c.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND CountryCode IS NULL AND C_Country_ID IS NULL"
			  + " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Country Default=" + no);
		**/
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c")
			  .append(" WHERE o.CountryCode=c.CountryCode AND c.AD_Client_ID IN (0, o.AD_Client_ID)) ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL AND CountryCode IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Country=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Country, ' ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Country=" + no);

		//	Set Region
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("Set RegionName=(SELECT MAX(Name) FROM C_Region r")
			  .append(" WHERE r.IsDefault='Y' AND r.C_Country_ID=o.C_Country_ID")
			  .append(" AND r.AD_Client_ID IN (0, o.AD_Client_ID)) ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Region Default=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r")
			  .append(" WHERE r.Name=o.RegionName AND r.C_Country_ID=o.C_Country_ID")
			  .append(" AND r.AD_Client_ID IN (0, o.AD_Client_ID)) ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Region=" + no);
		//
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Region, ' ")
			  .append("WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL ")
			  .append(" AND EXISTS (SELECT * FROM C_Country c")
			  .append(" WHERE c.C_Country_ID=o.C_Country_ID AND c.HasRegion='Y')")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Region=" + no);

		//	Product
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p")
			  .append(" WHERE o.ProductValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Product from Value=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p")
			  .append(" WHERE o.UPC=p.UPC AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_Product_ID IS NULL AND UPC IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Product from UPC=" + no);
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p")
			  .append(" WHERE o.SKU=p.SKU AND o.AD_Client_ID=p.AD_Client_ID) ")
			  .append("WHERE M_Product_ID IS NULL AND SKU IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Product fom SKU=" + no);
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Product, ' ")
			  .append("WHERE M_Product_ID IS NULL AND (ProductValue IS NOT NULL OR UPC IS NOT NULL OR SKU IS NOT NULL)")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Product=" + no);
		
		sql = new StringBuilder ("UPDATE I_Order ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Product Not On PriceList, ' ")
				  .append(" WHERE (M_Product_ID IS NOT NULL OR M_Product_ID > 0 ) AND ")
				  .append(" (M_PriceList_ID IS NOT NULL OR M_PriceList_ID > 0) AND ")
				  .append(" NOT EXISTS (SELECT 'ok' FROM M_PriceList pl inner join M_PriceList_Version plv on (pl.M_PriceList_ID = plv.M_PriceList_ID ) ")
				  .append(" left join M_ProductPrice pp on (pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID)")
				  .append(" left join  M_ProductPriceVendorBreak ppvb on (ppvb.M_PriceList_Version_ID = plv.M_PriceList_Version_ID and")
				  .append(" I_Order.dateOrdered between ppvb.validfrom and ppvb.validto )")
				  .append(" where coalesce(ppvb.m_product_id,pp.m_product_id) = I_Order.m_product_id and plv.isactive = 'Y' and ")
				  .append(" pl.m_pricelist_id=i_order.m_pricelist_id and " )
				  .append(" plv.validfrom <= I_Order.dateOrdered AND NOT EXISTS (Select 'ok' from M_PriceList_Version v where v.M_PriceList_ID = plv.M_PriceList_ID ")
			      .append(" and v.validfrom > plv.validfrom and v.M_PriceList_Version_ID != plv.M_PriceList_Version_ID )) ")
				  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				  .append(getDocumentNoFilter()); //F3P: added filter by docNo
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.warning ("Product Not On PriceList =" + no);

		//	Charge
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_Charge_ID=(SELECT C_Charge_ID FROM C_Charge c")
			  .append(" WHERE o.ChargeName=c.Name AND o.AD_Client_ID=c.AD_Client_ID) ")
			  .append("WHERE C_Charge_ID IS NULL AND ChargeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Charge=" + no);
		sql = new StringBuilder ("UPDATE I_Order ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Charge, ' ")
				  .append("WHERE C_Charge_ID IS NULL AND (ChargeName IS NOT NULL)")
				  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Charge=" + no);
		//
		
		sql = new StringBuilder ("UPDATE I_Order ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Product and Charge, ' ")
				  .append("WHERE M_Product_ID IS NOT NULL AND C_Charge_ID IS NOT NULL ")
				  .append(" AND I_IsImported<>'Y'").append (clientCheck)
				  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Product and Charge exclusive=" + no);

		//	Tax
		sql = new StringBuilder ("UPDATE I_Order o ")
			  .append("SET C_Tax_ID=(SELECT MAX(C_Tax_ID) FROM C_Tax t")
			  .append(" WHERE o.TaxIndicator=t.TaxIndicator AND o.AD_Client_ID=t.AD_Client_ID) ")
			  .append("WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Set Tax=" + no);
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Tax, ' ")
			  .append("WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		//F3P: added control for unique index c_order_documentno
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=DocumentNo Already Exist, ' ")
			  .append("WHERE DocumentNo IS NOT NULL AND C_BPartner_ID IS NOT NULL ")
			  .append(" AND C_DocType_ID IS NOT NULL AND (DateOrdered IS NOT NULL OR DateAcct IS NOT NULL)")
			  .append(" AND EXISTS (SELECT 'KO' FROM C_Order o WHERE o.DocumentNo = I_Order.DocumentNo ")
			  .append(" AND o.C_BPartner_ID = I_Order.C_BPartner_ID AND o.C_DocType_ID = I_Order.C_DocType_ID ")
			  .append(" AND to_char(o.DateAcct,'YYYY') = to_char(COALESCE (I_Order.DateOrdered,I_Order.DateAcct),'YYYY'))")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid DocumentNo=" + no);
		//F3P End
		
		//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_AFTER_VALIDATE);

		commitEx();
		
		//	-- New BPartner ---------------------------------------------------

		//	Go through Order Records w/o C_BPartner_ID
		sql = new StringBuilder ("SELECT * FROM I_Order ")
			  .append("WHERE I_IsImported='N' AND C_BPartner_ID IS NULL").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				X_I_Order imp = new X_I_Order (getCtx (), rs, get_TrxName());
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
					bpl.setC_Location_ID (loc.getC_Location_ID ());
					if (!bpl.save ())
						continue;
				}
				imp.setC_Location_ID (bpl.getC_Location_ID ());
				imp.setBillTo_ID (bpl.getC_BPartner_Location_ID ());
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
			log.log(Level.SEVERE, "BP - " + sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		sql = new StringBuilder ("UPDATE I_Order ")
			  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No BPartner, ' ")
			  .append("WHERE C_BPartner_ID IS NULL")
			  .append(" AND I_IsImported<>'Y'").append (clientCheck)
			  .append(getDocumentNoFilter()); //F3P: added filter by docNo
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("No BPartner=" + no);

		commitEx();
		
		//	-- New Orders -----------------------------------------------------

		int noInsert = 0;
		int noInsertLine = 0;
		int noProcessError = 0;

		//	Go through Order Records w/o
		sql = new StringBuilder ("SELECT * FROM I_Order ").append("WHERE I_IsImported='N'")
			//F3P: Import only full-order. Does not proceed with order that has at least one line with error 
			.append("AND NOT EXISTS (SELECT * FROM I_ORDER o WHERE I_ORDER.documentno = o.documentNo AND I_IsImported='E'")
			.append(clientCheck).append(")")
			//F3P: End
			.append (clientCheck)
			.append(getDocumentNoFilter()) //F3P: added filter by docNo
			.append(" ORDER BY C_BPartner_ID, BillTo_ID, C_BPartner_Location_ID, I_Order_ID");

		LinkedList<X_I_Order> lstImportedIOrder = new LinkedList<X_I_Order>(); //F3P from adempiere
		X_I_Order potentialErrorImp=null;
		MOrder order = null;//F3P: moved here to be able to delete it if import fails
		
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			rs = pstmt.executeQuery ();
			//
			int oldC_BPartner_ID = 0;
			int oldBillTo_ID = 0;
			int oldC_BPartner_Location_ID = 0;
			String oldDocumentNo = "";
			//
			int lineNo = 0;
			
			//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
			X_I_Order lastImp=null;
			X_I_Order imp=null;
			
			
			boolean orderWasAlreadyCreated = false; // F3P: ci segniamo se l'ordine e' nuovo
			
			while (rs.next ())
			{
				imp = new X_I_Order (getCtx (), rs, get_TrxName());
				
				String cmpDocumentNo = imp.getDocumentNo();
				if (cmpDocumentNo == null)
					cmpDocumentNo = "";
				//	New Order
				if (oldC_BPartner_ID != imp.getC_BPartner_ID() 
					|| oldC_BPartner_Location_ID != imp.getC_BPartner_Location_ID()
					|| oldBillTo_ID != imp.getBillTo_ID() 
					|| !oldDocumentNo.equals(cmpDocumentNo))
				{
					if (order != null)
					{
						potentialErrorImp=lastImp;// F3P: mark processing imp
						//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
						if(orderWasAlreadyCreated == false) // F3P: non lanciamo validate se l'ordine era pre-esistente
							ModelValidationEngine.get().fireImportValidate(this, lastImp, order, ImportValidator.TIMING_AFTER_IMPORT);
						
						if(processDocument(order) == false)
							noProcessError++;

						lstImportedIOrder.clear(); //F3P from adempiere
					}
					oldC_BPartner_ID = imp.getC_BPartner_ID();
					oldC_BPartner_Location_ID = imp.getC_BPartner_Location_ID();
					oldBillTo_ID = imp.getBillTo_ID();
					oldDocumentNo = imp.getDocumentNo();
					if (oldDocumentNo == null)
						oldDocumentNo = "";
					
					potentialErrorImp=imp;// F3P: mark processing imp
					
					// F3P: non creiamo l'ordine se gia' legato alla riga
					
					if(imp.getC_Order_ID() == 0)
					{
						orderWasAlreadyCreated = false;
						
						//
						order = new MOrder (getCtx(), 0, get_TrxName());
						order.setClientOrg (imp.getAD_Client_ID(), imp.getAD_Org_ID());
						order.setC_DocTypeTarget_ID(imp.getC_DocType_ID());
						order.setIsSOTrx(imp.isSOTrx());
						if (imp.getDeliveryRule() != null ) {
							order.setDeliveryRule(imp.getDeliveryRule());
						}
						if (imp.getDocumentNo() != null)
							order.setDocumentNo(imp.getDocumentNo());
						//	Ship Partner
						order.setC_BPartner_ID(imp.getC_BPartner_ID());
						order.setC_BPartner_Location_ID(imp.getC_BPartner_Location_ID());
						if (imp.getAD_User_ID() != 0)
							order.setAD_User_ID(imp.getAD_User_ID());
						//	Bill Partner
						order.setBill_BPartner_ID(imp.getC_BPartner_ID());
						order.setBill_Location_ID(imp.getBillTo_ID());
						//
						if (imp.getDescription() != null)
							order.setDescription(imp.getDescription());
						order.setC_PaymentTerm_ID(imp.getC_PaymentTerm_ID());
						order.setM_PriceList_ID(imp.getM_PriceList_ID());
						order.setM_Warehouse_ID(imp.getM_Warehouse_ID());
						if (imp.getM_Shipper_ID() != 0)
							order.setM_Shipper_ID(imp.getM_Shipper_ID());
						//	SalesRep from Import or the person running the import
						if (imp.getSalesRep_ID() != 0)
							order.setSalesRep_ID(imp.getSalesRep_ID());
						if (order.getSalesRep_ID() == 0)
							order.setSalesRep_ID(getAD_User_ID());
						//
						if (imp.getAD_OrgTrx_ID() != 0)
							order.setAD_OrgTrx_ID(imp.getAD_OrgTrx_ID());
						if (imp.getC_Activity_ID() != 0)
							order.setC_Activity_ID(imp.getC_Activity_ID());
						if (imp.getC_Campaign_ID() != 0)
							order.setC_Campaign_ID(imp.getC_Campaign_ID());
						if (imp.getC_Project_ID() != 0)
							order.setC_Project_ID(imp.getC_Project_ID());
						//
						if (imp.getDateOrdered() != null)
							order.setDateOrdered(imp.getDateOrdered());
						if (imp.getDateAcct() != null)
							order.setDateAcct(imp.getDateAcct());
						
						// Set Order Source
						if (imp.getC_OrderSource() != null)
							order.setC_OrderSource_ID(imp.getC_OrderSource_ID());
	
						//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
						ModelValidationEngine.get().fireImportValidate(this, imp, order, ImportValidator.TIMING_BEFORE_IMPORT);
						//
						order.saveEx();
						noInsert++;
					}
					else // C_Order_ID == 0
					{
						orderWasAlreadyCreated = true;
						order = PO.get(getCtx(), MOrder.Table_Name, imp.getC_Order_ID(), get_TrxName());
					}
					lineNo = 10;
				}
				
				imp.setC_Order_ID(order.getC_Order_ID());
				
				MOrderLine line = null;
												
				if(imp.getC_OrderLine_ID() == 0)
				{
					//	New OrderLine
					line = new MOrderLine (order);

					line.setLine(lineNo);
					lineNo += 10;
					if (imp.getM_Product_ID() != 0)
						line.setM_Product_ID(imp.getM_Product_ID(), true);
					if (imp.getC_Charge_ID() != 0)
						line.setC_Charge_ID(imp.getC_Charge_ID());
					line.setQty(imp.getQtyOrdered());
					line.setPrice();
					if (imp.getPriceActual().compareTo(Env.ZERO) != 0)
						line.setPrice(imp.getPriceActual());
					if (imp.getC_Tax_ID() != 0)
						line.setC_Tax_ID(imp.getC_Tax_ID());
					else
					{
						line.setTax();
						imp.setC_Tax_ID(line.getC_Tax_ID());
					}
					if (imp.getFreightAmt() != null)
						line.setFreightAmt(imp.getFreightAmt());
					if (imp.getLineDescription() != null)
						line.setDescription(imp.getLineDescription());
					// SVT set project on line
					if (imp.getC_Project_ID() != 0)
						line.setC_Project_ID(imp.getC_Project_ID());
					
					//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
					ModelValidationEngine.get().fireImportValidate(this, imp, line, ImportValidator.TIMING_BEFORE_IMPORT);
					
					line.saveEx();
					
					//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
					ModelValidationEngine.get().fireImportValidate(this, imp, line, ImportValidator.TIMING_AFTER_IMPORT);
					
					imp.setC_OrderLine_ID(line.getC_OrderLine_ID());
				}
				
				//F3P: save this line as imported
				lstImportedIOrder.add(imp);
								
				imp.setI_IsImported(true);
				imp.setProcessed(true);
				//
				if (imp.save())
					noInsertLine++;
				
				//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
				lastImp = imp;
			}
			if (order != null)
			{
				//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
				if(orderWasAlreadyCreated == false) // F3P: non lanciamo validate se l'ordine era pre-esistente
					ModelValidationEngine.get().fireImportValidate(this, imp, order, ImportValidator.TIMING_AFTER_IMPORT);
				
				if(processDocument(order) == false)
					noProcessError++;

			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Order - " + sql.toString(), e);
			
			//F3P: Doesn't allow partially imported order
			//delete previosly created order

			if(order.getC_Order_ID() > 0)
				noInsert--;
			
			order.delete(false);
						
			//mark all already imported lines as not imported
			for(X_I_Order impOrderLine : lstImportedIOrder)
			{
				impOrderLine.setI_IsImported(false);
				impOrderLine.setI_ErrorMsg("ERR=another line has a problem");
				impOrderLine.setProcessed(false);
				impOrderLine.save();
				
				noInsertLine--;
			}
			//save exception in actual line
			
			// F3P: error on right column
			
			if(potentialErrorImp != null)
			{
				String sMessage = e.getMessage();
				
				if(sMessage == null)
					sMessage = "NullPointer";
				
				sql = new StringBuilder("UPDATE I_Order ")
				  .append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR='||")
				  .append(DB.TO_STRING(sMessage)).append(", Processed='N' ")
				  .append("WHERE I_Order_ID =?");
				  			
				// Object params[] = {e.getMessage(), };
				DB.executeUpdate(sql.toString(), potentialErrorImp.getI_Order_ID(), false, get_TrxName());
			}
			//F3P end
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		//F3P: errors from order was not logged in I_Order, fixed
		//	Set Error to indicator to not imported
		sql = new StringBuilder ("UPDATE I_Order ")
			.append("SET I_IsImported='E' ")
			.append("WHERE I_IsImported<>'Y'").append(clientCheck)
			.append(getDocumentNoFilter()); //F3P: added filter by docNo;
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noProcessError), "@C_Order_ID@: @Errors@ @UnprocessedDocs@"); // F3P: aggiunti errori di elaborazione
		//
		addLog (0, null, new BigDecimal (noInsert), "@C_Order_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertLine), "@C_OrderLine_ID@: @Inserted@");
		StringBuilder msgreturn = new StringBuilder("#").append(noInsert).append("/").append(noInsertLine);
		return msgreturn.toString();
	}	//	doIt

	//F3P: added filter by docNo
	/**
	 * Returns sql used to filter by documentNo, if it was populated in the 
	 * process startup window. Otherwise it will returns an empty string that 
	 * will not affect any query.
	 * @return
	 */
	public String getDocumentNoFilter(){
		String sqlFilter = "";
		if(m_documentNo != null && m_documentNo.length() > 0)
			sqlFilter = " AND DocumentNo = '" + m_documentNo + "'";
		
		return sqlFilter;
	}
	//F3P: End
	
	//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
	@Override
	public String getImportTableName() {
		return X_I_Order.Table_Name;
	}

	//IDEMPIERE-3313 - ImportOrder does not implement ImportProcess interface
	@Override
	public String getWhereClause() {
		StringBuilder whereClause = new StringBuilder(" AND AD_Client_ID=").append(m_AD_Client_ID);
		return whereClause.toString();
	}
	
	public boolean processDocument(MOrder order) throws SQLException
	{
		String processMsg = null;
		boolean bHasError = false;
		
		if (m_docAction != null && m_docAction.length() > 0) // F3P: review processing: rollack
		{
			boolean shouddProcess = false;
			String orderDocStatus = order.getDocStatus();
			
			// F3P: verifichiamo la compatibilita' dello stato documento
			
			if(m_docAction.equals(MOrder.DOCACTION_Prepare) || m_docAction.equals(MOrder.DOCACTION_Complete))
			{
				if(orderDocStatus.equals(MOrder.DOCSTATUS_Drafted) || 
						orderDocStatus.equals(MOrder.DOCSTATUS_Invalid) ||
						orderDocStatus.equals(MOrder.DOCSTATUS_InProgress))
				{
					shouddProcess = true;
				}
			}
			
			if(shouddProcess)
			{
				Trx trx = Trx.get(get_TrxName(), false);
				Savepoint processSavepoint = trx.setSavepoint(null);
	
				
				try
				{
					order.setDocAction(m_docAction);
					if(!order.processIt (m_docAction)) 
					{
						log.warning("Order Process Failed: " + order.getDocumentNo() + " - " + order.getProcessMsg());
						processMsg = "Order Process Failed: " + order.getDocumentNo() + " - " + order.getProcessMsg();
						bHasError = true;
					}
				}
				catch(Throwable t)
				{
					bHasError = true;
					processMsg = "Order Process Failed: " + order.getDocumentNo() + " - " + STDUtils.getThrowableMessage(t);
				}
				finally
				{
					if(bHasError)
						trx.rollback(processSavepoint);
					
					trx.releaseSavepoint(processSavepoint);
					processSavepoint = null;
				}					
				
				if(bHasError)
					order.setDocStatus(MOrder.DOCSTATUS_Invalid);
			}
		}

		try
		{
			order.saveEx();
		}
		catch(Exception e)
		{
			bHasError = true;
			processMsg = STDUtils.getThrowableMessage(e);
		}
		
		if(bHasError)
		{
			Object params[] = {processMsg, order.getC_Order_ID()};
			String sSQLError = "UPDATE I_Order set I_IsImported='E', I_ErrorMsg = I_ErrorMsg||' ERR='||? WHERE C_Order_ID = ?";						
			DB.executeUpdateEx(sSQLError, params, get_TrxName());
		}
		
		return !bHasError;
	}

}	//	ImportOrder
