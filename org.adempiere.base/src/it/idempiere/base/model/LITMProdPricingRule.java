package it.idempiere.base.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MRole;
import org.compiere.model.MRule;
import org.compiere.util.DB;

/** Classe di supporto alle regole di applicazione prezzo. La classe effettiva e' customizzazione italiana,
 *  ma siccome deve essere usata nel core aggiungiamo una classe di supporto per evitare di dover aggiungere factory al core
 * 
 * @author strinchero
 *
 */
public class LITMProdPricingRule
{
	private static final String Table_Name = "LIT_ProdPricingRule";
	private static final String Q_RULES = "SELECT AD_Rule_ID FROM LIT_ProdPricingRule WHERE IsActive='Y' ORDER BY SeqNo";
	
	public static List<MRule> getRules(Properties ctx, String trxName)
	{
		String sql = MRole.getDefault().addAccessSQL(Q_RULES, Table_Name,true, false);
		List<Integer> ruleIDs = new ArrayList<>();
		
		PreparedStatement pstmt = DB.prepareStatement(sql,trxName);
		ResultSet rs = null;
		
		try
		{
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				int AD_Rule_ID = rs.getInt("AD_Rule_ID");  
				ruleIDs.add(AD_Rule_ID);
			}			
		}
		catch(SQLException e)
		{
			throw new AdempiereException(e);
		}
		finally
		{
			DB.close(rs, pstmt);
		}
		
		List<MRule> rules = new ArrayList<>();
		
		for(Integer AD_Rule_ID:ruleIDs)
		{
			MRule rule = MRule.get(ctx, AD_Rule_ID);
			rules.add(rule);
		}
		
		return rules;
	}
}
