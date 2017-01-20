package it.idempiere.base.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.model.MSysConfig;

/** Variabili di configurazione applicabili all'implementazione standard iDempiere
 *  
 * @author Silvano Trinchero, www.freepath.it
 *         Monica Bean, www.freepath.it
 *
 */
public class STDSysConfig
{
	public static final String	INVOICEGENERATE_BREAK_BY_BILL_CONTACT = "INVOICEGENERATE_BREAK_BY_BILL_CONTACT";
	public static final String	INVOICEGENERATE_BREAK_BY_SALESREP = "INVOICEGENERATE_BREAK_BY_SALESREP";
	public static final String	ROLE_AUTO_UPDATE_DOCACTIONACCESS = "ROLE_AUTO_UPDATE_DOCACTIONACCESS";
	public static final String	SYS_BACKUP_IMPORT = "F3P_BACKUP_PACKIN";
	public static final String	FILTER_SEARCH_QUERY = "FILTER_SEARCH_QUERY";
	public static final String	F3P_CASHLINE_USE_DEFAULT_CASHTYPE = "F3P_CASHLINE_USE_DEFAULT_CASHTYPE";
	
	public static final String LIT_ISSO_VAT_INVOICE_UNFORCED = "LIT_ISSO_VAT_INVOICE_UNFORCED";
	
	public static final String LIT_INV_CONV_DATE_TERM = "LIT_INV_CONV_DATE_TERM";
	
	public static final String F3P_CHECKINVLINEAMT = "F3P_CHECKINVLINEAMT";
	
	public static final String F3P_ALLOW_ASI_INSUFFICENT_QTY_ON_ORDER  = "F3P_ALLOW_ASI_INSUFFICENT_QTY_ON_ORDER";
	
	public static final String F3P_INOUTGENERATE_USE_WORKFLOW = "F3P_INOUTGENERATE_USE_WORKFLOW";
	public static final String F3P_ALLOWCPLUSRETURNS_WORMA = "F3P_ALLOWC+RETURNS_WORMA";
	public static final String F3P_SKIP_SUBJECT_IN_HTMLMAILBODY = "F3P_SKIP_SUBJECT_IN_HTML_MAIL_BODY";
	
	public static final String SYSCFG_OVERRIDE_INOUT_LINE_NO = "F3P_OVERRIDE_GENERATED_INOUT_LINE_NO";
	public static final String F3P_EXPLODE_BOM_SERVICE = "F3P_EXPLODE_BOM_SERVICE";
	public static final String F3P_CREATE_REVERSE_ORDER = "F3P_CREATE_REVERSE_ORDER";
	//LS variabile per gestire i termini pag fatture prima del completa
	public static final String LIT_PAYSCHEDULEINV_BEFORE_COMPLETE = "LIT_PAYSCHEDULEINV_BEFORE_COMPLETE";
	
	/**
	 * Parametro che indica se bisogna ripetere il soggetto come sottotitolo nell'email
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return booleano che indica se bisogna saltare la dichiarazione di un sottotitolo nell'email
	 */
	public static boolean isSubjectInHtmlBody(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_SKIP_SUBJECT_IN_HTMLMAILBODY, false,AD_Client_ID,AD_Org_ID);
	}
	
	/**
	 * Parametro che indica se l'AD_User_ID deve essere utilizzato come parametro di rottura per la generazione automatica fatture
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return true se deve essere fatta rottura
	 */
	public static boolean isUserBreakInvoice(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(INVOICEGENERATE_BREAK_BY_BILL_CONTACT, true, AD_Client_ID, AD_Org_ID);
	}

	public static boolean isIsSoVatInvoiceUnforced(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_ISSO_VAT_INVOICE_UNFORCED, false, AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * Parametro che indica la data da cui iniziare a usare dateInvoice per la conversione della fattura
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return la data da cui iniziare a usare dateInvoice per la conversione della fattura
	 */
	public static Timestamp getInvConvDateTerm(int AD_Client_ID,int AD_Org_ID) throws ParseException
	{
		Timestamp convDateTerm = null;
		
		String sDate = MSysConfig.getValue(LIT_INV_CONV_DATE_TERM, null, AD_Client_ID, AD_Org_ID);
		
		if(sDate != null)
		{
			SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
			Date d = sdt.parse(sDate);
			
			if(d != null)
			{
				convDateTerm = new Timestamp(d.getTime());
			}
		}
		
		return convDateTerm;
	}
	
	/**
	 * Parametro che indica se il SalesRep_ID deve essere utilizzato come parametro di rottura per la generazione automatica fatture
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return true se deve essere fatta rottura
	 */
	public static boolean isSaleRepBreakInvoice(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(INVOICEGENERATE_BREAK_BY_SALESREP, false, AD_Client_ID, AD_Org_ID);
	}	
	
	/**
	 * Se true, tutti i ruoli vengono comunque aggiornati creando il DocActionAccess come se il ruolo fosse marcato automatico
	 * @param AD_Client_ID
	 * @return
	 */
	public static boolean isRoleAutoUpdateDocActionAccess(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(ROLE_AUTO_UPDATE_DOCACTIONACCESS, false, AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * Se true, tutti i ruoli vengono comunque aggiornati creando il DocActionAccess come se il ruolo fosse marcato automatico
	 * @param AD_Client_ID
	 * @return
	 */
	public static String isRoleAutoUpdateDocActionAccessAsString(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(ROLE_AUTO_UPDATE_DOCACTIONACCESS, "N", AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * Se true viene eseguito il backup durante il packin
	 * @param AD_Client_ID, AD_Org_ID
	 * @return
	 */
	public static boolean isBackupImport(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(SYS_BACKUP_IMPORT, true, AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * Se true vengono filtrati nelle query di ricerca gli spazi ed i seguenti caratteri 
	 * [.-+_,:''\"]
	 * 
	 * @param AD_Client_ID, AD_Org_ID
	 * @return
	 */
	public static boolean isFilterQuery(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(FILTER_SEARCH_QUERY, false, AD_Client_ID, AD_Org_ID);
	}

	/**
	 * Se true  verifica il CashType, se non è valido imposta CASHTYPE_GeneralExpense (v. MCashLine.beforeSave )
	 * 
	 * @param AD_Client_ID, AD_Org_ID
	 * @return
	 */
	public static boolean isUseDefaultCashType(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_CASHLINE_USE_DEFAULT_CASHTYPE, true, AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * Se true  verifica che il prezzo sia != 0 nelle righe fattura senza prodotto/addebito
	 * 
	 * @param AD_Client_ID, AD_Org_ID
	 * @return
	 */
	public static boolean isCheckInvoiceLineAmt(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_CHECKINVLINEAMT, false, AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * Se true, consente l'uso di lotto (ASI) vuoto, allo scopo di battezzarlo con l'ordine.
	 * 
	 * @param AD_Client_ID, AD_Org_ID
	 * @return true se consentito, false altrimenti
	 */
	public static boolean isAllowAsiInsufficentQtyOnOrder(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_ALLOW_ASI_INSUFFICENT_QTY_ON_ORDER, false, AD_Client_ID, AD_Org_ID);
	}	
	
	public static boolean isInOutGenerateUseWorkflow(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(F3P_INOUTGENERATE_USE_WORKFLOW, false, AD_Client_ID);		
	}
	
	public static boolean isAllowCPLUSRetursWORma(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_ALLOWCPLUSRETURNS_WORMA, false, AD_Client_ID, AD_Org_ID);		
	}
	
	/**
	 * Parametro che indica se le linee della bolla devono esser rinumerate
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return 
	 */
	public static boolean isOverrideGeneratedInOutLineNo(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(SYSCFG_OVERRIDE_INOUT_LINE_NO, false, AD_Client_ID);
	}
	
	/** Regola il comportamento dell'esplosione della bom, per i prodotti servizio
	 * 
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return Y: esplode service, N: non esplode, X: non esplode nessuna riga di bom (indifferentemente dal tipo prodotto)
	 */
	public static String getIsExplodeBOMService(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getValue(F3P_EXPLODE_BOM_SERVICE, "Y", AD_Client_ID, AD_Org_ID);
	}

	// LS
	public static final boolean isPayScheduleInvBeforeComplete(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_PAYSCHEDULEINV_BEFORE_COMPLETE, false, AD_Client_ID, AD_Org_ID);
	}
	
	/** Check this variable before create the reverse for onCreditOrder,warehouseOrder or POSOrder
	 * 
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return create or not
	 */
	public static final boolean isOrderCreateReverse(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_CREATE_REVERSE_ORDER, true, AD_Client_ID, AD_Org_ID);
	}
}
