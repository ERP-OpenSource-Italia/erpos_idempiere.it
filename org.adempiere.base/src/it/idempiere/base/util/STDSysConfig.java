package it.idempiere.base.util;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.compiere.model.MOrder;
import org.compiere.model.MSysConfig;
import org.compiere.util.Ini;
import org.compiere.util.Util;

/** Variabili di configurazione applicabili all'implementazione standard iDempiere
 *  
 * @author Silvano Trinchero, www.freepath.it
 *         Monica Bean, www.freepath.it
 *
 */
public class STDSysConfig
{
	public static final String	STD_VALUES_SEPARATOR = ",";
	public static final String	STD_VALUES_SEPARATOR_REGEX = Pattern.quote(STD_VALUES_SEPARATOR);
	
	public static final String	INVOICEGENERATE_BREAK_BY_BILL_CONTACT = "INVOICEGENERATE_BREAK_BY_BILL_CONTACT";
	public static final String	INVOICEGENERATE_BREAK_BY_SALESREP = "INVOICEGENERATE_BREAK_BY_SALESREP";
	public static final String	ROLE_AUTO_UPDATE_DOCACTIONACCESS = "ROLE_AUTO_UPDATE_DOCACTIONACCESS";
	public static final String	SYS_BACKUP_IMPORT = "F3P_BACKUP_PACKIN";
	public static final String	FILTER_SEARCH_QUERY = "FILTER_SEARCH_QUERY";
	public static final String	FILTER_SPECIAL_LETTERS = "FILTER_SPECIAL_LETTERS";
	public static final String	F3P_CASHLINE_USE_DEFAULT_CASHTYPE = "F3P_CASHLINE_USE_DEFAULT_CASHTYPE";
	
	public static final String LIT_ISSO_VAT_INVOICE_UNFORCED = "LIT_ISSO_VAT_INVOICE_UNFORCED";
	
	public static final String LIT_INV_CONV_DATE_TERM = "LIT_INV_CONV_DATE_TERM";
	
	public static final String F3P_CHECKINVLINEAMT = "F3P_CHECKINVLINEAMT";
	
	public static final String F3P_ALLOW_ASI_INSUFFICENT_QTY_ON_ORDER  = "F3P_ALLOW_ASI_INSUFFICENT_QTY_ON_ORDER";
	
	public static final String F3P_INOUTGENERATE_USE_WORKFLOW = "F3P_INOUTGENERATE_USE_WORKFLOW";
	public static final String F3P_ALLOWCPLUSRETURNS_WORMA = "F3P_ALLOWC+RETURNS_WORMA";
	public static final String F3P_SKIP_SUBJECT_IN_HTMLMAILBODY = "F3P_SKIP_SUBJECT_IN_HTML_MAIL_BODY";
	
	public static final String SYSCFG_OVERRIDE_INOUT_LINE_NO = "F3P_OVERRIDE_GENERATED_INOUT_LINE_NO";
	public static final String SYSCFG_OVERRIDE_INVOICE_LINE_NO = "F3P_OVERRIDE_GENERATED_INVOICE_LINE_NO";
	
	public static final String F3P_EXPLODE_BOM_SERVICE = "F3P_EXPLODE_BOM_SERVICE";
	public static final String F3P_CREATE_REVERSE_ORDER = "F3P_CREATE_REVERSE_ORDER";
	//LS variabile per gestire i termini pag fatture prima del completa
	public static final String LIT_PAYSCHEDULEINV_BEFORE_COMPLETE = "LIT_PAYSCHEDULEINV_BEFORE_COMPLETE";
	public static final String LIT_PAYSCHEDULEORD_BEFORE_COMPLETE = "LIT_PAYSCHEDULEORD_BEFORE_COMPLETE";
	// Angelo Dabala' (genied) add support for Desktop Mail Client
	private static final String LIT_USE_DESKTOP_EMAIL = "LIT_USE_DESKTOP_EMAIL"; 
	//F3P: default doc action
	protected static final String  F3P_DEFAULT_DOC_ACTION = "F3P_Default_DocAction_On_Create";
	/** F3P: show only order with service line */
	public static final String F3P_CREATEFROMORDER_ONLYSERVICE = "F3P_CREATEFROMORDER_ONLYSERVICE";	
	
  	public static final String USERELEM1_SQL_CONF = "REPORTSOURCE_USERELEMENT1_VALUESQL";
  	public static final String USERELEM2_SQL_CONF = "REPORTSOURCE_USERELEMENT2_VALUESQL";
  	
  	public static final String F3P_EXPENSE_PROCESS_BREAKBYBP = "F3P_EXPENSE_PROCESS_BREAKBYBP";
  	public static final String SYS_TAX_ID = "F3P_EXPENSE_PROCESS_TAX";
 	public static final String MANDATORY_SALESREGION = "F3P_BP_MANDATORYSALESREGION";
 	
 	public static final String SYSCFG_OVERRIDE_LINE_NO = "F3P_OVERRIDE_GENERATED_INVOICE_LINE_NO";
 	public static final String SYSCFG_CHARGE_TO_ADD = "F3P_DELIVERY_CHARGE_TO_ADD";
 	public static final String SYSCFG_CHARGE_TO_REMOVE = "F3P_DELIVERY_CHARGE_TO_REMOVE";
 	
 	public static final String FA_ADDITION_ALLOW_ACCDEPR_ALWAYS = "FA_ADDITION_ALLOW_ACCDEPR_ALWAYS";
 	public static final String FA_DEPRECIATIONEXP_SKIPZEROVALUES = "FA_DEPRECIATIONEXP_SKIPZEROVALUES";
 	
 	public static final String PRICEVENDORBREAK_IGNORE_THRESHOLD = "F3P_PRICEVENDORBREAK_IGNORETRESHOLD";
 	
 	private static final String LIT_ADVANCEDDISCOUNT_MAN = "LIT_ADVANCEDDISCOUNT_MAN";

	//LS variabile per usare data diversa da data fattura per la generazione del progr. pagamenti fattura
	public static final String LIT_PAYSCHEDULEINV_CUSTOM_DATE_FIELD = "LIT_PAYSCHEDULEINV_CUSTOM_DATE_FIELD";
	
	public static final String	FACTACCT_HASCURRENCYRATE = "LIT_FACTACCT_HASCURRENCYRATE";
	
 	public static final String REPORT_HOME_KEY = "REPORT_HOME_PATH";
 	
 	public static final String LIT_WEB_SERVICE_COMPATIBILITY_MODE = "LIT_WEB_SERVICE_COMPATIBILITY_MODE";
 	
 	public static final String F3P_ALLOW_SINGLE_SCHEDULE = "F3P_ALLOW_SINGLE_SCHEDULE";
 	
 	public static final String LIT_COMMISSION_RULE_MINOR_SEQUENCE = "LIT_COMMISSION_RULE_MINOR_SEQUENCE";
 	
 	public static final String LIT_COST_ENABLESEED = "LIT_COST_ENABLESEED";
 	
 	public static final String LIT_CHECK_RELDOC_ON_REOPEN_ORDER = "LIT_CHECK_RELDOC_ON_REOPEN_ORDER";
 	 		
 	public static final String LIT_IS_ADVANCED_PACKIN_BACKUP = "LIT_IS_ADVANCED_PACKIN_BACKUP";
 	
 	public static final String FIN_FILTER_BY_ORG="FIN_FILTER_BY_ORG";
 	
 	public static final String LIT_INVOICE_REVERSE_USE_NEW_VATLEDGERNO = "LIT_INVOICE_REVERSE_USE_NEW_VATLEDGERNO";
 	
 	public static final String F3P_INFOPRODUCT_LISTVERSIONS = "F3P_INFOPRODUCT_LISTVERSIONS";
 	
	public static final String	F3P_INFOPRODUCT_LISTVERSIONS_YES = "Y",
								F3P_INFOPRODUCT_LISTVERSIONS_NO = "N",
								F3P_INFOPRODUCT_LISTVERSIONS_NO_PURCHASE = "P";
	
	public static final String LIT_PROJ_PHASE_ORDER_DOCSUBTYPESO = "LIT_PROJ_PHASE_ORDER_DOCSUBTYPESO";
 	
	public static boolean isCommissionRuleMinorSequence(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_COMMISSION_RULE_MINOR_SEQUENCE , false,AD_Client_ID,AD_Org_ID);
	}
 	
 	public static Boolean getAllowSingleSchedule(int AD_Client_ID,int AD_Org_ID)
 	{
 		return MSysConfig.getBooleanValue(F3P_ALLOW_SINGLE_SCHEDULE, false, AD_Client_ID, AD_Org_ID);
 	}
 	
 	public static Boolean getWebServiceCompatibilityMode(int AD_Client_ID,int AD_Org_ID)
 	{
 		return MSysConfig.getBooleanValue(LIT_WEB_SERVICE_COMPATIBILITY_MODE, false, AD_Client_ID, AD_Org_ID);
 	}
 	
 	public static String getOverrideLineNo(int AD_Client_ID,int AD_Org_ID)
 	{
 		return MSysConfig.getValue(SYSCFG_OVERRIDE_LINE_NO, null, AD_Client_ID, AD_Org_ID);
 	}
 	
 	public static String getChargeToRemove(int AD_Client_ID,int AD_Org_ID)
 	{
 		return MSysConfig.getValue(SYSCFG_CHARGE_TO_REMOVE, null, AD_Client_ID, AD_Org_ID);
 	}
 	
 	public static int getChargeToAdd_ID(int AD_Client_ID,int AD_Org_ID)
 	{
 		return MSysConfig.getIntValue(SYSCFG_CHARGE_TO_ADD, 0, AD_Client_ID, AD_Org_ID);
 	}
  	
  	public static boolean isSalesRegionMandatory(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(MANDATORY_SALESREGION, false, AD_Client_ID, AD_Org_ID);
	}
  	
  	public static int getSysTaxID (int AD_Client_ID,int AD_Org_ID)
  	{
  		return MSysConfig.getIntValue(SYS_TAX_ID, -1,  AD_Client_ID,AD_Org_ID);
  	}
  	public static boolean isBreakByBP(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(F3P_EXPENSE_PROCESS_BREAKBYBP, true, AD_Client_ID);
	}
  	
  	public static String getReportSourceUserElement1ValueSQL(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(USERELEM1_SQL_CONF, AD_Client_ID,AD_Org_ID);
	}
  	
  	public static String getReportSourceUserElement2ValueSQL(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(USERELEM2_SQL_CONF, AD_Client_ID,AD_Org_ID);
	}
  	
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
	 * Se true, quando il filtro dei caratteri speciali e' abilitato vengono filtrate anche le
	 * le lettere speciali (??,??,??,??,??,??,??,??,??,??,??,??,??,??,??,??,??,??,??,??,??,??) 
	 * 
	 * @param AD_Client_ID, AD_Org_ID
	 * @return
	 */
	public static boolean isFilterSpecialLetter(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(FILTER_SPECIAL_LETTERS, false, AD_Client_ID, AD_Org_ID);
	}

	/**
	 * Se true  verifica il CashType, se non ?? valido imposta CASHTYPE_GeneralExpense (v. MCashLine.beforeSave )
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
	
	/**
	 * Parametro che indica se le linee della fattura devono esser rinumerate
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return 
	 */
	public static boolean isOverrideGeneratedInvoiceLineNo(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(SYSCFG_OVERRIDE_INVOICE_LINE_NO, false, AD_Client_ID);
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
	
	public static final boolean isPayScheduleOrdBeforeComplete(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_PAYSCHEDULEORD_BEFORE_COMPLETE, false, AD_Client_ID, AD_Org_ID);
	}
	
	/** Check this variable before create the reverse for onCreditOrder,warehouseOrder or POSOrder
	 * 
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return create or not
	 */
	public static boolean isOrderCreateReverse(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_CREATE_REVERSE_ORDER, true, AD_Client_ID, AD_Org_ID);
	}
	
	public static boolean isUseDesktopEMail(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_USE_DESKTOP_EMAIL, false, AD_Client_ID, AD_Org_ID);
	}
	
	public static String getDefaultDocAction(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(F3P_DEFAULT_DOC_ACTION, null, AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * F3P: show only order with at least one service line or charge
	 * @return
	 */
	public static boolean isShowOnlyServiceOrder()
	{
		//LS backward compatibility
		return isShowOnlyServiceOrder(0);
	}
	//LS search at least by ad_client_id to manage multi client installations (like other SysConfigs)
	public static boolean isShowOnlyServiceOrder(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(F3P_CREATEFROMORDER_ONLYSERVICE, false, AD_Client_ID);
	}
	
	public static boolean isFAAdditionAllowAccDeprAlways(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(FA_ADDITION_ALLOW_ACCDEPR_ALWAYS, false, AD_Client_ID, AD_Org_ID);
	}
	
	public static boolean isFADepreciationExpSkipZeroValues(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(FA_DEPRECIATIONEXP_SKIPZEROVALUES, false, AD_Client_ID, AD_Org_ID);
	}
	
	/** Check this variable before create the reverse for onCreditOrder,warehouseOrder or POSOrder
	 * 
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return report home path
	 */
	public static String getReportHome(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(REPORT_HOME_KEY, Ini.getAdempiereHome() + File.separator + "reports",AD_Client_ID,AD_Org_ID);
	}
		
	public static int getPriceVendorBreakIgnoreTreshold()
	{
		return MSysConfig.getIntValue(PRICEVENDORBREAK_IGNORE_THRESHOLD, -1);
	}	
	
	public static boolean isAdvancedDiscountMan(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(LIT_ADVANCEDDISCOUNT_MAN, false, AD_Client_ID);
	}
	
	public static String getInvoicePayScheduleCustomDateField(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getValue(LIT_PAYSCHEDULEINV_CUSTOM_DATE_FIELD, "", AD_Client_ID, AD_Org_ID);
	}
	
	public static final boolean hasFactAcctCurrencyRate(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(FACTACCT_HASCURRENCYRATE, false, AD_Client_ID);
	}
	
 	// Cambia la sequenza di determinazione dei prezzi dal listino e vendor break (usata da MProductPrice)
 	// A: (default) standard adempiere/idempiere, se richiesto l'uso dei vb, questi hanno priorita sui product price
 	// L: default 'localizzazione italiana': se richiesto il pv, la priorita' e' per versione (vendor break, poi product, se non c'e scalo sul listino)
 	
 	public static final String LIT_PRICELIST_DET_SEQUENCE = "LIT_PRICELIST_DET_SEQUENCE";
 	public static final String LIT_PRICELIST_DET_SEQUENCE_Adempiere = "A";
 	public static final String LIT_PRICELIST_DET_SEQUENCE_Lit = "L";

	public static final String getPriceListDetSequence(int AD_Client_ID)
	{
		return MSysConfig.getValue(LIT_PRICELIST_DET_SEQUENCE, LIT_PRICELIST_DET_SEQUENCE_Adempiere, AD_Client_ID);
	}
	
	public static boolean isCostSeedEnabled(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_COST_ENABLESEED, false, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String DISPLAY_QTY_WARNING_ON_MAT_MOVEMENT= "DISPLAY_QTY_WARNING_ON_MAT_MOVEMENT";
	
	public static boolean isDisplayQtyWarningOnMatMovement(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(DISPLAY_QTY_WARNING_ON_MAT_MOVEMENT, true, AD_Client_ID, AD_Org_ID);
	}
		
	public static final String LIT_CREATE_COUNTER_FOR_REVERSAL= "LIT_CREATE_COUNTER_FOR_REVERSAL";
	
	public static boolean isCreateCounterForReversal(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_CREATE_COUNTER_FOR_REVERSAL, true, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String COPY_DOCNO_FROM_WAREHOUSEORDER_TO_INOUT = "ERPOS_COPY_DOCNO_FROM_WAREHOUSEORDER_TO_INOUT"; 

	public static boolean isCopyDocNoFromWhOrderToInout(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(COPY_DOCNO_FROM_WAREHOUSEORDER_TO_INOUT, false, AD_Client_ID, AD_Org_ID);
	}
		
	public static final String WAREHOUSELOCATORCHECK_SKIP_INOUTDOCTYPES = "ERPOS_WAREHOUSELOCATORCHECK_SKIP_INOUTDOCTYPES";
	
	public static boolean	isInOutDocTypeInWarehouseLocatorCheckSkipList(int C_DocType_ID, int AD_Client_ID, int AD_Org_ID)
	{
		String docTypes = MSysConfig.getValue(WAREHOUSELOCATORCHECK_SKIP_INOUTDOCTYPES, null, AD_Client_ID, AD_Org_ID);
		
		if(docTypes == null)
			return false;
		
		int ids[] = null;
		
		if(docTypes.indexOf(STD_VALUES_SEPARATOR) > 0)
		{
			String types[] = docTypes.split(STD_VALUES_SEPARATOR_REGEX);
			ids = new int[types.length];
			int i=0;
			
			for(String type:types)
			{
				int id = -1;
				
				if(Util.isEmpty(type,true) == false)
				{
					id = Integer.parseInt(type.trim());
				}
				
				ids[i++] = id;
			}
		}
		else
		{
			ids = new int[] {Integer.parseInt(docTypes.trim())};
		}
		
		boolean found = false;
		
		for(int i=0;i<ids.length;i++)
		{
			if(ids[i] > 0 && ids[i] == C_DocType_ID)
			{
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	
	/** check if prevent re-open when exist invoices or shipments the reverse for onCreditOrder,warehouseOrder or POSOrder
	 * 
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return prevent or not
	 */
	public static boolean isCheckRelDocOnReopenOrder(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_CHECK_RELDOC_ON_REOPEN_ORDER, false, AD_Client_ID, AD_Org_ID);
	}
	/** If true save new values and sql statement
	 * 
	 * @return advanced backup or not
	 */
	public static boolean isAdvancedPackinBackup()
	{
		return MSysConfig.getBooleanValue(LIT_IS_ADVANCED_PACKIN_BACKUP, false);
	}
	
	/** If true add org filter to financial report
	 * 
	 * @return if add org filter
	 */
	public static boolean isFinFilterByOrg(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(FIN_FILTER_BY_ORG, false, AD_Client_ID);
	}
	
	/** If true reversed invoice use new VATLedgerNo
	 * 
	 * @return if reversed invoice use new VATLedgerNo
	 */
	public static boolean isReversedInvoiceUseNewVATLedgerNo(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_INVOICE_REVERSE_USE_NEW_VATLEDGERNO, true, AD_Client_ID, AD_Org_ID);
	}
	
	public static String getInfoProductListVersions(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(F3P_INFOPRODUCT_LISTVERSIONS, F3P_INFOPRODUCT_LISTVERSIONS_YES, AD_Client_ID, AD_Org_ID);
	}
	
	/**
	 * 
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return docSubTypeSO for ProjectPhaseGenOrder
	 */
	public static String getProjectPhaseOrderDocSubTypeSO(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(LIT_PROJ_PHASE_ORDER_DOCSUBTYPESO, MOrder.DocSubTypeSO_Proposal, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String LIT_FILTER_ORDERLINE_FROM_REPLANISH_ACTIVE = "LIT_FILTER_ORDERLINE_FROM_REPLANISH_ACTIVE";

	public static boolean getFilterOrderLineFromReplenishActive(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LIT_FILTER_ORDERLINE_FROM_REPLANISH_ACTIVE, false, AD_Client_ID, AD_Org_ID);
	}

	public static final String LIT_SHARE_DISCOUNT_TO_LINE_NOT_BOM = "LIT_SHARE_DISCOUNT_TO_LINE_NOT_BOM";
	
	public static boolean isShareDiscountToBomLine(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LIT_SHARE_DISCOUNT_TO_LINE_NOT_BOM, false, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String LIT_CREATE_FROM_INV_SHIP_DATA_DISPLAY = "LIT_CREATE_FROM_INV_SHIP_DATA_DISPLAY";
	
	public static String getCreateFromInvShipDataDisplay(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getValue(LIT_CREATE_FROM_INV_SHIP_DATA_DISPLAY, null, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String LIT_ERROR_WITHNEGATIVE_COMPOSITEDISCOUNT = "LIT_ERROR_WITHNEGATIVE_COMPOSITEDISCOUNT";
	
	public static boolean IsErrorWithNegativeCompositeDiscount(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_ERROR_WITHNEGATIVE_COMPOSITEDISCOUNT, false, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String LIT_OVERWRITE_DATA_WHEN_EXPLODE_BOM = "LIT_OVERWRITE_DATA_WHEN_EXPLODE_BOM";

	public static boolean isOverwriteDataWhenExplodeBOM(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LIT_OVERWRITE_DATA_WHEN_EXPLODE_BOM, true, AD_Client_ID, AD_Org_ID);
	}
	
	private static final String LIT_GENINOUT_IGNORESHIPPERONCONSOLIDATE = "LIT_GENINOUT_IGNORESHIPPERONCONSOLIDATE";
	
	public static boolean isGenInOutIgnoreShipperOnConsolidate(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_GENINOUT_IGNORESHIPPERONCONSOLIDATE, false, AD_Client_ID, AD_Org_ID);
	}
	
	private static final String LIT_LINENO_INCREMENT = "LIT_LINENO_INCREMENT";

	public static int getAddLineNoOverride(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getIntValue(LIT_LINENO_INCREMENT, 10, AD_Client_ID, AD_Org_ID);
	}
	
	private static final String LIT_ALLOCATION_CHECTRXDATES = "LIT_ALLOCATION_CHECTRXDATES";
	
	public static boolean isAllocationCheckTrxDates(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LIT_ALLOCATION_CHECTRXDATES, true, AD_Client_ID, AD_Org_ID);
	}
	
	private static final String LIT__BLOCK_CONCURRENT_UPDATE_SAME_USER = "LIT_BLOCK_CONCURRENT_UPDATE_SAME_USER";

	public static boolean isBlockConcurrentUpdateSameUser(int AD_Client_ID)
	{
		return MSysConfig.getBooleanValue(LIT__BLOCK_CONCURRENT_UPDATE_SAME_USER, false, AD_Client_ID);
	}
	
	/*private static final String LIT_IS_SHOW_ORDER_WITH_QTY_OVER_ZERO = "LIT_IS_SHOW_ORDER_WITH_QTY_OVER_ZERO";

	public static boolean isShowOrderWithQtyOverZero(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_IS_SHOW_ORDER_WITH_QTY_OVER_ZERO, false, AD_Client_ID,AD_Org_ID);
	}*/

	
	private static final String LIST_DOC_TYPE_ID_SHOW_NEGATIVE_QTY_ORDERED = "LIST_DOC_TYPE_ID_SHOW_NEGATIVE_QTY_ORDERED";
	
	public static List<Integer> getListDocTypeIDShowNegativeQtyOrdered(int AD_Client_ID, int AD_Org_ID)
	{
		String sListValue =  MSysConfig.getValue(LIST_DOC_TYPE_ID_SHOW_NEGATIVE_QTY_ORDERED, null, AD_Client_ID, AD_Org_ID);
		ArrayList<Integer> listDocTypes = null;
		
		if(sListValue != null)
		{
		
			listDocTypes = new ArrayList<Integer>();
			
			for(String value:sListValue.split(";"))
			{
				listDocTypes.add(Integer.parseInt(value));
			}
		}
		
		return listDocTypes;
	}

	public static final String LIT_FILTER_CREATE_FROM_INVOICE_MOVEMENT_TYPE = "LIT_FILTER_CREATE_FROM_INVOICE_MOVEMENT_TYPE";

	public static boolean isFilterCreateFromInvoiceMovementType(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LIT_FILTER_CREATE_FROM_INVOICE_MOVEMENT_TYPE, false, AD_Client_ID,AD_Org_ID); 
	}
	
	public static final String LIT_FINDWINDOW_FULLLIKE = "LIT_FINDWINDOW_FULLLIKE";

	public static boolean isFindWindowFullLike(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LIT_FINDWINDOW_FULLLIKE, false, AD_Client_ID,AD_Org_ID); 
	}
	
	public static final String LIT_FINDWINDOW_FULLHEIGHT = "LIT_FINDWINDOW_FULLHEIGHT";
	
	public static boolean isFindWindowFullHeight(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LIT_FINDWINDOW_FULLHEIGHT, false, AD_Client_ID,AD_Org_ID); 
	}
	

	public static final String LIT_CHANGE_PRICELIST_AND_DATE = "LIT_CHANGE_PRICELIST_AND_DATE";
	
	public static boolean isChangeDateAndPriceList(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(LIT_CHANGE_PRICELIST_AND_DATE, false, AD_Client_ID,AD_Org_ID);
	}
	
	public static final String LGS_AUTOGENERATE_ASI_ON_DOC_PREPARE = "LGS_AUTOGENERATE_ASI_ON_DOC_PREPARE";

	public static boolean isAutoGenerateASIOnDocPrepare(int AD_Client_ID, int AD_Org_ID) 
	{
		return MSysConfig.getBooleanValue(LGS_AUTOGENERATE_ASI_ON_DOC_PREPARE, false, AD_Client_ID,AD_Org_ID); 
	}
	
	public static final String COPY_DOCNO_FROM_ORDER_TO_INVOICE = "LIT_COPY_DOCNO_FROM_ORDER_TO_INVOICE"; 

	public static boolean isCopyDocNoFromOrderToInvoice(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(COPY_DOCNO_FROM_ORDER_TO_INVOICE, false, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String LS_CALLOUT_WHEN_COPY_RECORD = "LS_CALLOUT_WHEN_COPY_RECORD";

	public static boolean isLSCalloutWhenCopyRecord() 
	{
		return MSysConfig.getBooleanValue(LS_CALLOUT_WHEN_COPY_RECORD, true); 
	}
	
	public static final String COPY_DOCNO_FROM_ORDER_TO_INOUT = "LIT_COPY_DOCNO_FROM_ORDER_TO_INOUT"; 
	
	public static boolean isCopyDocNoFromOrderToInout(int AD_Client_ID, int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(COPY_DOCNO_FROM_ORDER_TO_INOUT, false, AD_Client_ID, AD_Org_ID);
	}
	
	public static final String F3P_ACCT_NO_CLEARINGS_PAYMENTALLOCATION = "POST_PAYMENT_CLEARING";

	public static boolean isAcctNoClearingsPaymentAlloc(int AD_Client_ID,int AD_Org_ID)
	{
		return MSysConfig.getBooleanValue(F3P_ACCT_NO_CLEARINGS_PAYMENTALLOCATION, false, 
				AD_Client_ID,AD_Org_ID);
	}
	
}
