/******************************************************************************
 * Copyright (C) 2012 Elaine Tan                                              *
 * Copyright (C) 2012 Trek Global
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MBPBankAccount;
import org.compiere.model.MBPartner;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * 
 * @author Elaine
 *
 */
public class PaymentUtil {

	private static final CLogger logger = CLogger.getCLogger(PaymentUtil.class);

	public static MBPBankAccount[] getBankAccounts(MBPartner bpartner, String creditCardNo, int C_PaymentProcessor_ID) {
		ArrayList<MBPBankAccount> list = new ArrayList<MBPBankAccount>();
		String sql = "SELECT * FROM C_BP_BankAccount WHERE C_BPartner_ID=? AND CreditCardNumber=? AND C_PaymentProcessor_ID = ? AND IsActive='Y' ORDER BY Created";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, bpartner.get_TrxName());
			pstmt.setInt(1, bpartner.getC_BPartner_ID());
			pstmt.setString(2, creditCardNo);
			pstmt.setInt(3, C_PaymentProcessor_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPBankAccount(bpartner.getCtx(), rs, bpartner
						.get_TrxName()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, sql, e);
		} finally {
			DB.close(rs, pstmt);
		}

		MBPBankAccount[] m_accounts = new MBPBankAccount[list.size()];
		list.toArray(m_accounts);
		return m_accounts;
	}

	public static String encrpytCreditCard(String value) {
		if (value == null)
			return "";
		else

		if (value.length() <= 4)
			return value;

		Integer valueLength = value.length();

		StringBuilder encryptedCC = new StringBuilder();

		for (int i = 0; i < (valueLength - 4); i++) {
			encryptedCC.append("0");
		}

		encryptedCC.append(value.substring(valueLength - 4, valueLength));

		return encryptedCC.toString();
	}

	public static String encrpytCvv(String creditCardVV) {
		if (creditCardVV == null)
			return "";
		else {
			Integer valueLength = creditCardVV.length();

			StringBuilder encryptedCC = new StringBuilder();

			for (int i = 0; i < valueLength; i++) {
				encryptedCC.append("0");
			}
			return encryptedCC.toString();
		}
	}

	public static boolean isNumeric(String str) {
		if (str != null && str.length() > 0) {
			NumberFormat formatter = NumberFormat.getInstance();
			ParsePosition pos = new ParsePosition(0);

			formatter.parse(str, pos);
			return str.length() == pos.getIndex();
		}
		return true;
	}
	
	public static int getPayAmtInCents(BigDecimal payAmt)
	{
		if (payAmt == null)
			return 0;
		
		BigDecimal bd = payAmt.multiply(Env.ONEHUNDRED);
		return bd.intValue();
	}
	
	public static String getCreditCardExp(int creditCardExpMM, int creditCardExpYY, String delimiter)
	{
		String mm = String.valueOf(creditCardExpMM);
		String yy = String.valueOf(creditCardExpYY);

		StringBuilder retValue = new StringBuilder();
		if (mm.length() == 1)
			retValue.append("0");
		retValue.append(mm);
		//
		if (delimiter != null)
			retValue.append(delimiter);
		//
		if (yy.length() == 1)
			retValue.append("0");
		retValue.append(yy);
		//
		return (retValue.toString());
	}
}
