package it.idempiere.base.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class LITMBPartner
{
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MBPartner.class);
	
	public static BigDecimal getTotalOpenBalanceDB(MBPartner bPartner, boolean isUsePaymentsAvailable)
	{
		BigDecimal totalOpenBalance = DB.getSQLValueBD(bPartner.get_TrxName(), "SELECT ls_calcSOCreditUsed(?,?,?,?) FROM DUAL",
				bPartner.getC_BPartner_ID(), bPartner.getAD_Client_ID(), bPartner.getAD_Org_ID(), isUsePaymentsAvailable?"Y":"N");
		return totalOpenBalance;
	}
	
	public static BigDecimal getTotalOpenBalanceDB(int C_BPartner_ID, boolean isUsePaymentsAvailable, String trxName)
	{
		BigDecimal totalOpenBalance = DB.getSQLValueBD(trxName, 
				"SELECT ls_calcSOCreditUsed(C_BPartner_ID,AD_Client_ID,AD_Org_ID,?) FROM C_BPartner WHERE C_BPartner_ID = ? ",
				isUsePaymentsAvailable?"Y":"N", C_BPartner_ID);
		return totalOpenBalance;
	}
	
	public static void setTotalOpenBalanceDB(MBPartner bPartner, boolean saveData, String trxName)
	{
		if (bPartner == null || bPartner.getC_BPartner_ID() == 0)
			return;
		
		if(bPartner.isCustomer() == false)
			return;
		
		BigDecimal SO_CreditUsed = null;
		BigDecimal TotalOpenBalance = null;
		String sql = "SELECT ls_calcSOCreditUsed(bp.C_BPArtner_ID,bp.AD_Client_ID,bp.AD_Org_ID, 'N') , ls_calcSOCreditUsed(bp.C_BPArtner_ID,bp.AD_Client_ID,bp.AD_Org_ID, 'Y') "
				+ "FROM C_BPartner bp "
				+ "WHERE bp.C_BPartner_ID=? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, bPartner.get_TrxName());
			pstmt.setInt (1, bPartner.getC_BPartner_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				SO_CreditUsed = rs.getBigDecimal(1);
				TotalOpenBalance = rs.getBigDecimal(2);
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (SO_CreditUsed != null)
			bPartner.setSO_CreditUsed (SO_CreditUsed);
		if (TotalOpenBalance != null)
			bPartner.setTotalOpenBalance(TotalOpenBalance);
		bPartner.setSOCreditStatus();
		
		if (saveData){
			if (trxName == null){
				trxName = bPartner.get_TrxName();
			}
			bPartner.saveEx(trxName);
		}
	}
	
	public static final BigDecimal getNotInvoicedAmt (int C_BPartner_ID, boolean withVAT)
	{
		if (withVAT)
			return MBPartner.getNotInvoicedAmt(C_BPartner_ID);
		
		//LS should be added VAT as on MOrder.prepareIt() that checks getGrandTotal()
		BigDecimal retValue = null;
		String sql = "SELECT currencyRound(COALESCE(SUM(COALESCE("
			+ "currencyBase(((ol.QtyDelivered-ol.QtyInvoiced)*ol.PriceActual)*(1+t.rate/100),o.C_Currency_ID,o.DateOrdered, o.AD_Client_ID,o.AD_Org_ID) ,0)),0),?,null) "
			+ "FROM C_OrderLine ol"
			+ " INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) "
			+ " INNER JOIN C_tax t on (ol.C_Tax_ID = t.C_Tax_ID) "
			+ "WHERE o.IsSOTrx='Y' AND Bill_BPartner_ID=?";			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt(1, Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID"));
			pstmt.setInt (2, C_BPartner_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getBigDecimal(1);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		return retValue;
	}	//	getNotInvoicedAmt
}
