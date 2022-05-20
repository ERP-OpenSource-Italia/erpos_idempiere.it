package it.idempiere.base.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MLot;
import org.compiere.model.MProduct;
import org.compiere.model.MRule;
import org.compiere.model.PO;
import org.compiere.model.X_M_AttributeSet;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Util;

import it.idempiere.base.util.BaseMessages;

public class LGSMAttributeSetInstance
{
	private static CLogger		s_log = CLogger.getCLogger (LGSMAttributeSetInstance.class);
	
	public static MAttributeSetInstance generateLot(Properties ctx, MProduct product, PO po, String trxName)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int M_AttributeSetInstance_ID = 0;
		String lotName = null;
		MAttributeSetInstance asi = null;
		Timestamp guaranteeDate = null;
		
		MRule rule = PO.get(ctx, MRule.Table_Name, LGSMAttributeSet.getAD_Rule_ID((X_M_AttributeSet) product.getM_AttributeSet()), trxName);
		String ruleParsed = Env.parseVariable(rule.getScript(), po, trxName, true);
		try {
			pstmt = DB.prepareStatement(ruleParsed, trxName);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				M_AttributeSetInstance_ID = rs.getInt(1);
				lotName = rs.getString(2);
				guaranteeDate = rs.getTimestamp(3);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, ruleParsed, e);
			throw new AdempiereException(e);
		} finally {
			DB.close(rs, pstmt);
		}
		
		if (M_AttributeSetInstance_ID > 0) {
			asi = PO.get(ctx, MAttributeSetInstance.Table_Name, M_AttributeSetInstance_ID, trxName);
		}else if (Util.isEmpty(lotName) == false){
			asi = PO.create(ctx, MAttributeSetInstance.Table_Name, trxName);
			asi.setM_AttributeSet_ID(product.getM_AttributeSet_ID());
			// Create new Lot
			if (asi.getM_AttributeSet_ID() > 0)
			{
				getLot(asi, true, product.get_ID(), lotName, trxName);  //F3P: use trx
			}
			//
			asi.setGuaranteeDate(guaranteeDate);
			asi.setDescription();
			asi.saveEx(trxName);
		}else {
			throw new AdempiereException(Msg.getMsg(ctx, BaseMessages.MSG_ERR_AUTOLOT_NO_RESULTS));
		}
		
		return asi;
	}

	public static String getLot (MAttributeSetInstance asi, boolean getNew, int M_Product_ID, String lotName, String trxName)
	{
		if (getNew)
			createLot(asi, M_Product_ID, lotName, trxName);
		return asi.getLot();
	}

	public static KeyNamePair createLot(MAttributeSetInstance asi, int M_Product_ID, String lotName, String trxName) {
		KeyNamePair retValue = null;
		
		//create lot
		MLot lot = PO.create(asi.getCtx(), MLot.Table_Name, trxName);
		lot.setM_Product_ID(M_Product_ID);
		lot.setName(lotName);
		lot.saveEx(trxName);

		asi.setM_Lot_ID (lot.getM_Lot_ID());
		asi.setLot (lot.getName());
		
		retValue = new KeyNamePair(lot.getM_Lot_ID(), lot.getName());

		return retValue;
	} // createLot
}
