package it.idempiere.base.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

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
	
	public static void setTotalOpenBalanceDB(MBPartner bPartner)
	{
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
	}
}
