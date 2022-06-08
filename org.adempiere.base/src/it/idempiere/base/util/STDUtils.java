package it.idempiere.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAttachment;
import org.compiere.model.MClientInfo;
import org.compiere.model.MColumn;
import org.compiere.model.MConversionRate;
import org.compiere.model.MConversionType;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 *  Utility applicabili all'implementazione Standard iDempiere
 * 
 * @author Silvano Trinchero, www.freepath.it
 *
 */
public class STDUtils {
	
	/** Column name IsIntraVat */
	public static final String COLUMNNAME_IsIntraVat = "IsIntraVat";
	
	private static final CLogger s_log = CLogger.getCLogger(STDUtils.class);

	/**
	 * Inserisce un oggetto in un'arraylist nel primo slot libero a partire
	 * dall'indice indicato. Se necessario, l'arraylist viene riallocato.
	 * 
	 * @param ar
	 *            ArrayList cui aggiungere l'elemento
	 * @param idx
	 *            indice cui verra aggiunto, se l'array deve crescere verra
	 *            riempito di null nei vuoti, se la posizione e' occupata viene
	 *            inserito nel primo slot libero
	 * @param ob
	 *            oggetto da inserire
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addToArrayListAndScroll(ArrayList ar, int idx, Object ob) {
		ar.ensureCapacity(idx + 1);

		if (ar.size() >= idx + 1) {
			if (ar.get(idx) != null) {
				addToArrayListAndScroll(ar, idx + 1, ob);
			} else {
				ar.set(idx, ob);
			}
		} else {
			while (ar.size() < idx)
				ar.add(null);

			ar.add(ob);
		}
	}

	/**
	 * Return an Integer with the meaning of an ID ( if iID <= 0 returns null)
	 * 
	 * @param iID
	 *            the value be treated as an ID
	 * @return the ID
	 */
	public static Integer asID(int iID) {
		Integer iVal = null;

		if (iID > 0) {
			iVal = Integer.valueOf(iID);
		}

		return iVal;
	}

	/**
	 * Return a boolean value from object
	 * 
	 * @param bool
	 *            the value be treated as a boolean
	 * @return boolean value of bool or false is bool is not valid or null
	 */
	public static boolean asBoolean(Object bool) {
		boolean bRet = false, bError = false;

		if (bool == null)
			return false;

		if (bool instanceof Boolean) {
			bRet = ((Boolean) bool).booleanValue();
		} else if (bool instanceof String) {
			if (((String) bool).equalsIgnoreCase("N")
					|| ((String) bool).equalsIgnoreCase("false")
					|| ((String) bool).equalsIgnoreCase("0")) {
				bRet = false;
			} else if (((String) bool).equalsIgnoreCase("Y")
					|| ((String) bool).equalsIgnoreCase("true")
					|| ((String) bool).equalsIgnoreCase("1")) {
				bRet = true;
			} else {
				bError = true;
			}
		} else if (bool instanceof Integer) {
			if ((Integer) bool == 0) {
				bRet = false;
			} else if ((Integer) bool == 1) {
				bRet = true;
			} else {
				bError = true;
			}
		} else {
			bError = true;
		}

		if (bError) {
			throw new AdempiereException("@Invalid@" + bool);
		}

		return bRet;
	}

	public static int getCurrencyOf(int AD_Client_ID, int AD_Org_ID) {
		// AccountSchema Info (first)
		String sql = "SELECT * " + "FROM C_AcctSchema a, AD_ClientInfo c "
				+ "WHERE a.C_AcctSchema_ID=c.C_AcctSchema1_ID "
				+ "AND c.AD_Client_ID=?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int C_Currency_ID = -1;

		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();

			if (!rs.next()) {
				C_Currency_ID = -1;
			} else {
				// Accounting Info
				C_Currency_ID = rs.getInt("C_Currency_ID");
			}
			rs.close();
			pstmt.close();

			/** Define AcctSchema , Currency, HasAlias for Multi AcctSchema **/
			MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(Env.getCtx(),
					AD_Client_ID);
			if (ass != null && ass.length > 1) {
				for (MAcctSchema as : ass) {
					if (as.getAD_OrgOnly_ID() != 0) {
						if (as.isSkipOrg(AD_Org_ID)) {
							continue;
						} else {
							C_Currency_ID = as.getC_Currency_ID();
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			s_log.log(Level.SEVERE, "loadPreferences", e);
		} finally {
			DB.close(rs, pstmt);
		}

		return C_Currency_ID;
	}

	public static int getAcctSchemaOf(int AD_Client_ID, int AD_Org_ID) {
		int C_AcctSchema_ID = -1;

		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(Env.getCtx(),
				AD_Client_ID);
		if (ass != null && ass.length > 1) {
			for (MAcctSchema as : ass) {
				if (as.getAD_OrgOnly_ID() != 0) {
					if (as.isSkipOrg(AD_Org_ID)) {
						continue;
					} else {
						C_AcctSchema_ID = as.getC_AcctSchema_ID();
						break;
					}
				}
			}
			if (C_AcctSchema_ID < 0)
				C_AcctSchema_ID = MClientInfo.get(Env.getCtx(), AD_Client_ID)
						.getC_AcctSchema1_ID();

		} else if (ass != null && ass.length == 1) {
			C_AcctSchema_ID = ass[0].getC_AcctSchema_ID();
		}

		return C_AcctSchema_ID;
	}

	/**
	 * Get Conversion Rate
	 * 
	 * @param CurFrom_ID
	 *            The C_Currency_ID FROM
	 * @param CurTo_ID
	 *            The C_Currency_ID TO
	 * @param ConvDate
	 *            The Conversion date - if null - use current date
	 * @param ConversionType_ID
	 *            Conversion rate type - if 0 - use Default
	 * @param AD_Client_ID
	 *            client
	 * @param AD_Org_ID
	 *            organization
	 * @return MConversionRate
	 */
	public static MConversionRate getRate(Properties ctx, int CurFrom_ID,
			int CurTo_ID, Timestamp ConvDate, int ConversionType_ID,
			int AD_Client_ID, int AD_Org_ID, String sTrx) {
		if (CurFrom_ID == CurTo_ID)
			return null;

		// Conversion Type
		int C_ConversionType_ID = ConversionType_ID;
		if (C_ConversionType_ID == 0)
			C_ConversionType_ID = MConversionType.getDefault(AD_Client_ID);
		// Conversion Date
		if (ConvDate == null)
			ConvDate = new Timestamp(System.currentTimeMillis());

		// Get Rate
		String sql = "SELECT C_Conversion_Rate_ID " + "FROM C_Conversion_Rate "
				+ "WHERE C_Currency_ID=?" // #1
				+ " AND C_Currency_ID_To=?" // #2
				+ " AND	C_ConversionType_ID=?" // #3
				+ " AND	? BETWEEN ValidFrom AND ValidTo" // #4 TRUNC (?)
															// ORA-00932:
															// inconsistent
															// datatypes:
															// expected NUMBER
															// got TIMESTAMP
				+ " AND AD_Client_ID IN (0,?)" // #5
				+ " AND AD_Org_ID IN (0,?) " // #6
				+ "ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, ValidFrom DESC";
		int C_Conversion_Rate_ID = -1;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DB.prepareStatement(sql, sTrx);
			pstmt.setInt(1, CurFrom_ID);
			pstmt.setInt(2, CurTo_ID);
			pstmt.setInt(3, C_ConversionType_ID);
			pstmt.setTimestamp(4, ConvDate);
			pstmt.setInt(5, AD_Client_ID);
			pstmt.setInt(6, AD_Org_ID);

			rs = pstmt.executeQuery();

			if (rs.next())
				C_Conversion_Rate_ID = rs.getInt(1);
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "getRate", e);
		} finally {
			DB.close(rs, pstmt);
		}

		MConversionRate mConvRate = null;

		if (C_Conversion_Rate_ID > 0) {
			mConvRate = PO.get(ctx, MConversionRate.Table_Name,
					C_Conversion_Rate_ID, sTrx);
		} else {
			s_log.info("getRate - not found - CurFrom="
					+ CurFrom_ID
					+ ", CurTo="
					+ CurTo_ID
					+ ", "
					+ ConvDate
					+ ", Type="
					+ ConversionType_ID
					+ (ConversionType_ID == C_ConversionType_ID ? "" : "->"
							+ C_ConversionType_ID) + ", Client=" + AD_Client_ID
					+ ", Org=" + AD_Org_ID);
		}

		return mConvRate;
	} // getRate

	/**
	 * Check if table has column
	 * 
	 * @param AD_Table_ID
	 *            AD_Table_ID
	 * @param sColumnName
	 *            column name to check
	 * @return true if table contains sColumnName
	 */
	public static boolean hasColumn(int AD_Table_ID, String sColumnName) {
		String sql = "SELECT columnname FROM AD_Column WHERE AD_TABLE_ID = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean bHasColumn = false;

		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String sCol = rs.getString(MColumn.COLUMNNAME_ColumnName);

				if (sCol.equals(sColumnName)) {
					bHasColumn = true;
					break;
				}
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			s_log.log(Level.SEVERE, "hasColumn", e);
		} finally {
			DB.close(rs, pstmt);
		}

		return bHasColumn;
	}

	public static String buildSqlINClauseString(int[] vals) {
		StringBuilder sb = new StringBuilder("(");

		if (vals.length > 0) {
			for (Object o : vals) {
				sb.append(o).append(',');
			}

			sb.setCharAt(sb.length() - 1, ')');
		} else {
			sb.append(')');
		}

		return sb.toString();
	}

	public static Timestamp getConvDate(MInvoice invoice) {

		if (!asBoolean(invoice.get_Value(COLUMNNAME_IsIntraVat))) {
			try {
				Timestamp dateAcct = invoice.getDateAcct(), convDateTerm = STDSysConfig
						.getInvConvDateTerm(invoice.getAD_Client_ID(),
								invoice.getAD_Org_ID());

				if (convDateTerm != null
						&& dateAcct.compareTo(convDateTerm) <= 0) {
					return invoice.getDateAcct();
				}
			} catch (ParseException e) {
				s_log.log(Level.SEVERE, "getConvDate", e);
			}
		}

		return invoice.getDateInvoiced();
	}
	
	/**
	 * @param BigDecimal,VarSysConfig,AD_Client_ID,AD_Org_ID,DefaultRound,DefaultDecimal
	 * @return BigDecimal
	 */
	public static BigDecimal roundFromSysConfig(BigDecimal value,String nameVarSysConfig,int AD_Client_ID, int AD_Org_ID,int defDecimal,String defRound) 
	{
		String valueSysConfig=MSysConfig.getValue(nameVarSysConfig,AD_Client_ID , AD_Org_ID);
		int    numDecimal = defDecimal;
		String sRound     = defRound;
		
		if(valueSysConfig!=null)
		{
			int indexComma=valueSysConfig.indexOf(",");
			
			if(indexComma>0)
			{
				String sDecimal=valueSysConfig.substring(0,indexComma);
				numDecimal=Integer.valueOf(sDecimal);
				sRound=valueSysConfig.substring(indexComma+1).trim();
			}
			else
			{
				numDecimal=Integer.valueOf(valueSysConfig);
			}
		}
	
		RoundingMode roundingMode=RoundingMode.valueOf(sRound);
		BigDecimal valueRounded=value.setScale(numDecimal, roundingMode);
		
		return valueRounded;
	}
	
	/** Get the raw value of a column. So for a boolean it will return Y/N
	 *  Throws exception if the colum does not exists
	 *  
	 * @param model				model to get value from
	 * @param columnName	column
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPORawValue (PO model,String columnName)
  {
          int index = model.get_ColumnIndex(columnName);
          
          if (index < 0)
          {
          	throw new AdempiereException("@not.found@ @AD_Column_ID@: @" + columnName + "@");
          }
          
          return (T)model.get_Value (index);
  }
	
	/**
	 * Returns true if this order has at least one shipment with status equals to CO or CL,
	 * false otherwise.
	 * @return boolean
	 */
	public static boolean hasCompletedShipment(MOrder order)
	{
		MInOut inOutArray[] = order.getShipments();
		for(MInOut inOut: inOutArray)
		{
			if(inOut.getDocStatus().equals(MInOut.DOCSTATUS_Completed)
					|| inOut.getDocStatus().equals(MInOut.DOCSTATUS_Closed))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns true if this order has at least one invoice with status equals to CO or CL,
	 * false otherwise.
	 * @return boolean
	 */
	public static boolean hasCompletedInvoice(MOrder order)
	{
		MInvoice invoiceArray[] = order.getInvoices();
		for(MInvoice invoice: invoiceArray)
		{
			if(invoice.getDocStatus().equals(MInvoice.DOCSTATUS_Completed)
					|| invoice.getDocStatus().equals(MInvoice.DOCSTATUS_Closed))
				return true;
		}
		return false;
	}	
	
	/** Return error message, considering null pointer exception null message
	 * 
	 * @param t
	 * @return error
	 */
	public static String getThrowableMessage(Throwable t)
	{
		if(t instanceof NullPointerException)
			return t.getClass().getName();
		
		return t.getMessage();
	}
	
	public static void setEnvGeneric(Properties ctx, int WindowNo, String name, Object oVal)
	{
		if(oVal == null)
			return;
		
		if(oVal instanceof Timestamp)
		{
			Env.setContext(ctx,WindowNo,name, (Timestamp)oVal);
		}
		else if(oVal instanceof Integer)
		{
			Env.setContext(ctx,WindowNo,name, (Integer)oVal);
		}
		else if(oVal instanceof Boolean)
		{
			Env.setContext(ctx,WindowNo,name, (Boolean)oVal);
		}
		else
			Env.setContext(ctx,WindowNo,name, oVal.toString());
	}
	
	public static int getAD_Org_IDFromRecord(MAttachment mAttachment)
	{
		MTable table = MTable.get(mAttachment.getCtx(), mAttachment.getAD_Table_ID());
		String keyColName = table.getKeyColumns()[0]; //multy key not allowed
		
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(keyColName).append("=?");
		
		PO po = new Query(mAttachment.getCtx(), table, whereClause.toString(),mAttachment.get_TrxName())
		.setParameters(mAttachment.getRecord_ID()).first();
		
		return po.getAD_Org_ID();
	}
	
	/** Calcola il valore propozionale:
	 * 
	 * toDivisor : x = fromDivisor : fromDividend
	 * 
	 * X = (toDivisor * fromDividend) / fromDivisor
	 * 
	 * @param toScale
	 * @param propResult
	 * @param propScale
	 * @return
	 */
	public static BigDecimal proportionallValue(BigDecimal toDivisor, BigDecimal fromDivisor, BigDecimal fromDividend, int scale)
	{
		BigDecimal mult = toDivisor.multiply(fromDividend);
	
		return mult.divide(fromDivisor, scale, RoundingMode.HALF_UP);
	}
	
	/** Applica la percentuael e scala il risultato finale
	 * 
	 * @param bdValue valore
	 * @param bdPerc percentaule
	 * @param scale precisione del risultato
	 * 
	 * @return valore in percentuale
	 */
	public static BigDecimal percentage(BigDecimal bdValue, BigDecimal bdPerc, int precision)
	{		
		BigDecimal m = bdValue.multiply(bdPerc);
		BigDecimal calculated = m.divide(Env.ONEHUNDRED, precision, RoundingMode.HALF_UP);
		
		return calculated;		
	}
	
	/** Ottiene una sys config come lista di interi, utilizzando ';' come separatore
	 * 
	 * @param Name nome delle sysconfig
	 * @param defaultValue	valore di default, se contiene ';' viene spezzato in valori
	 * @param AD_Client_ID client
	 * @param AD_Org_ID org
	 * @return lista di valori, vuota se nessun valore presente (non ritorna mai null)
	 */
	public static List<Integer> getSysConfigAsIntList(String Name, String defaultValue, int AD_Client_ID, int AD_Org_ID)
	{
		String value = MSysConfig.getValue(Name, defaultValue, AD_Client_ID, AD_Org_ID);
		List<Integer> intValues = new ArrayList<Integer>();
								
		if(value != null)
		{
			String[] tmplistString = value.split(";");
			
			if(tmplistString.length > 0)
			{
				for(String tmpString : tmplistString)
				{
					tmpString = tmpString.trim();
					
					if(Util.isEmpty(tmpString, false) == false)
					{
						int iVal = Integer.parseInt(tmpString);
						intValues.add(iVal);
					}
				}
			}			 
		}
			
		return intValues;
	}
	
	// From com.f3p.commons.FileStreamUtils.convertToSafeFilename
	
	private static final String	FILENAME_SAFE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_+=#. ";
	
	public static String	convertToSafeFilename(String sFilename,char chInvalidReplace)
	{		
		if(Util.isEmpty(sFilename))
			return null;
		
		StringBuilder	sbName = new StringBuilder(convertNonAscii(sFilename.trim()));
		
		for(int i=0; i < sbName.length(); i++)
		{
			char cVal = sbName.charAt(i);
			
			if(FILENAME_SAFE.indexOf(cVal) < 0)
			{
				sbName.setCharAt(i, chInvalidReplace);
			}
		}		
		
		return sbName.toString();
	}
	
	// From com.f3p.commons.Utils.convertNonAscii
	
	private static final String PLAIN_ASCII =
		    "AaEeIiOoUu"    // grave
		  + "AaEeIiOoUuYy"  // acute
		  + "AaEeIiOoUuYy"  // circumflex
		  + "AaOoNn"        // tilde
		  + "AaEeIiOoUuYy"  // umlaut
		  + "Aa"            // ring
		  + "Cc"            // cedilla
		  + "OoUu"          // double acute
		  ;
		
	private static final String ACC_UNICODE =
		"\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"             
		+ "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD" 
		+ "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177" 
		+ "\u00C3\u00E3\u00D5\u00F5\u00D1\u00F1"
		+ "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF" 
		+ "\u00C5\u00E5"                                                             
		+ "\u00C7\u00E7" 
		+ "\u0150\u0151\u0170\u0171" ;


	  // remove accentued from a string and replace with ascii equivalent
	  public static String convertNonAscii(String s) {
	     if (s == null) return null;
	     StringBuilder sb = new StringBuilder();
	     int n = s.length();
	     for (int i = 0; i < n; i++) {
	        char c = s.charAt(i);
	        int pos = ACC_UNICODE.indexOf(c);
	        if (pos > -1){
	            sb.append(PLAIN_ASCII.charAt(pos));
	        }
	        else {
	            sb.append(c);
	        }
	     }
	     return sb.toString();
	  }
	  
	  public static String getSQLValueBuilderToString (String trxName, String sql, Object... params)
	  {
		StringBuilder retValue = new StringBuilder();
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	try
    	{
    		pstmt = DB.prepareStatement(sql, trxName);
    		DB.setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
    		while (rs.next())
    		{
    			retValue.append(rs.getString(1)).append("\n");
    		}
    	}
    	catch (SQLException e)
    	{
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		DB.close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	
    	return retValue.toString();
	  }
	  
	  public static void getSQLValueBuilderToStringEx (String trxName, String sql, Object... params)
	  {
		  String returnMsg = getSQLValueBuilderToString(trxName, sql, params);
		  
		  if(returnMsg != null && Util.isEmpty(returnMsg, true) == false)
		  {
			  throw new AdempiereException(returnMsg);
		  }
	  }
	  
	  public static void getSQLValueBuilderToStringExHeader (String trxName, String exceptionHeader, String sql, Object... params)
	  {
		  String returnMsg = getSQLValueBuilderToString(trxName, sql, params);
		  
		  if(returnMsg != null && Util.isEmpty(returnMsg, true) == false)
		  {
			  throw new AdempiereException(exceptionHeader+"\n"+returnMsg);
		  }
	  }
	  
	  public static PO setPOFieldsFromResultSet(ResultSet rs, PO po) throws SQLException
	  {
		  ResultSetMetaData	rsMeta = rs.getMetaData();
		  int iCount = rsMeta.getColumnCount();
		
		  for(int i=0;i<iCount;i++)
		  {
			  String sColName = rsMeta.getColumnName(i+1);						
			  Object oColValue = rs.getObject(sColName);
				
			  if(oColValue != null && isValidPOColumn(sColName, po))
			  {
				  po.set_ValueOfColumn(sColName, oColValue);
			  }						
		  }	
		
		  return po;
	  }
		
	  public static boolean isValidPOColumn(String columnName, PO po)
	  {
		  boolean valid = true;
		  
		  if(po.get_ColumnIndex(columnName)<0)
		  {
			  valid = false;
		  }
			
		  return valid;
	  }
}
