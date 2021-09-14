package it.idempiere.base;

import java.util.Properties;

import org.compiere.model.MAttributeSetInstance;

public interface IAttributeSetInstanceUtil {
	
	/**
	 * @param ctx
	 * @param M_Product_ID
	 * @param Lot
	 * @param trxName
	 * @return
	 * Genera un MAttributeSetInstance
	 */
	public int generateASI(Properties ctx,  int M_Product_ID, String Lot, String trxName);
	
	/**
	 * @param mASI new M_AttributeSetInstance
	 * @param trxName 
	 * @return -1 se non esiste un M_AttributeSetInstance con stesso stesso:
	 * 	SerNo (se popolato)
	 *  M_lot_ID (se popolato e SerNo non popolato)
	 *  Lot (se popolato e SerNo e M_lot_id non popolati)
	 */
	public int getASI_IDIfExists(MAttributeSetInstance mASI, String trxName);
}
