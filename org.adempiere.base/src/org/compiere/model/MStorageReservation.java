package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MStorageReservation extends X_M_StorageReservation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8114446879871270122L;
	private static CLogger s_log = CLogger.getCLogger(MStorageReservation.class);

	public MStorageReservation(Properties ctx, int M_StorageReservation_ID,
			String trxName) {
		super(ctx, M_StorageReservation_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MStorageReservation(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Get Storage Info for Product on specified Warehouse
	 * @param ctx
	 * @param m_Warehouse_ID
	 * @param m_Product_ID
	 * @param i
	 * @param get_TrxName
	 * @return
	 */
	public static MStorageReservation[] get(Properties ctx, int m_Warehouse_ID,
			int m_Product_ID, int i, String trxName) {
		String sqlWhere = "M_Product_ID=? AND M_Warehouse_ID=?";
		
		List<MStorageReservation> list = new Query(ctx, MStorageReservation.Table_Name, sqlWhere, trxName)
								.setParameters(m_Product_ID, m_Warehouse_ID)
								.list();
		
		MStorageReservation[] retValue = new MStorageReservation[list.size()];
		list.toArray(retValue);
		return retValue;
	}
	
	/**
	 * 	Get Storage Info for Product across warehouses
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trxName transaction
	 *	@return existing or null
	 */
	public static MStorageReservation[] getOfProduct (Properties ctx, int M_Product_ID, String trxName)
	{
		String sqlWhere = "M_Product_ID=?";
		
		List<MStorageReservation> list = new Query(ctx, MStorageReservation.Table_Name, sqlWhere, trxName)
								.setParameters(M_Product_ID)
								.list(); 
		
		MStorageReservation[] retValue = new MStorageReservation[list.size()];
		list.toArray(retValue);
		return retValue;
		
	}	//	getOfProduct
	
	/**
	 * 	Get Available Qty.
	 * 	The call is accurate only if there is a storage record 
	 * 	and assumes that the product is stocked 
	 *	@param M_Warehouse_ID wh
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID masi
	 *	@param trxName transaction
	 *	@return qty available (QtyOnHand-QtyReserved) or null
	 * @deprecated Since 331b. Please use {@link #getQtyAvailable(int, int, int, int, String)}.
	 */
	public static BigDecimal getQtyAvailable (int M_Warehouse_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, String trxName)
	{
		return getQtyAvailable(M_Warehouse_ID, 0, M_Product_ID, M_AttributeSetInstance_ID, trxName);
	}
	
	/**
	 * Get Warehouse/Locator Available Qty.
	 * The call is accurate only if there is a storage record 
	 * and assumes that the product is stocked 
	 * @param M_Warehouse_ID wh (if the M_Locator_ID!=0 then M_Warehouse_ID is ignored)
	 * @param M_Locator_ID locator (if 0, the whole warehouse will be evaluated)
	 * @param M_Product_ID product
	 * @param M_AttributeSetInstance_ID masi
	 * @param trxName transaction
	 * @return qty available (QtyOnHand-QtyReserved) or null if error
	 */
	public static BigDecimal getQtyAvailable (int M_Warehouse_ID, int M_Locator_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID, String trxName)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("");
		
		if (M_Locator_ID != 0) {
			MLocator locator = new MLocator(Env.getCtx(), M_Locator_ID, trxName);
			MWarehouse wh = new MWarehouse(Env.getCtx(), locator.getM_Warehouse_ID(), trxName);
			if (wh.get_ValueAsInt("M_ReserveLocator_ID") != M_Locator_ID) {
				sql.append("SELECT COALESCE(SUM(s.QtyOnHand),0)")
						.append(" FROM M_StorageOnHand s")
						.append(" WHERE s.M_Product_ID=? AND s.M_Locator_ID=?");
				
				params.add(M_Product_ID);
				params.add(M_Locator_ID);
				
				// With ASI
				if (M_AttributeSetInstance_ID != 0) {
					sql.append(" AND s.M_AttributeSetInstance_ID=?");
					params.add(M_AttributeSetInstance_ID);
				}
			}
			else {
				sql.append("SELECT COALESCE(SUM(s.QtyOnHand),0)-COALESCE(SUM(r.Qty),0)")
					.append(" FROM M_StorageOnHand s")
					.append(" LEFT JOIN M_StorageReservation r ON s.M_Product_ID=r.M_Product_ID")
					.append(" WHERE s.M_Product_ID=? AND AND s.M_Locator_ID=?")
					.append(" AND EXISTS (SELECT 1 FROM M_Locator l JOIN M_Warehouse w ON w.M_ReserveLocator_ID=l.M_Locator_ID")
					.append(" WHERE s.M_Locator_ID=l.M_Locator_ID AND l.M_Warehouse_ID=r.M_Warehouse_ID");

				params.add(M_Product_ID, M_Locator_ID);
				
				// With ASI
				if (M_AttributeSetInstance_ID != 0) {
					sql.append(" AND s.M_AttributeSetInstance_ID=? AND r.M_AttributeSetInstance_ID=?");
					params.add(M_AttributeSetInstance_ID, M_AttributeSetInstance_ID);
				}
			}
		
		} else {
			sql.append("SELECT COALESCE(SUM(s.QtyOnHand),0)-COALESCE(SUM(r.Qty),0)")
			.append(" FROM M_StorageOnHand s")
			.append(" LEFT JOIN M_StorageReservation r ON s.M_Product_ID=r.M_Product_ID")
			.append(" WHERE s.M_Product_ID=?")
			.append(" AND EXISTS (SELECT 1 FROM M_Locator l")
			.append(" WHERE s.M_Locator_ID=l.M_Locator_ID AND l.M_Warehouse_ID=r.M_Warehouse_ID")
			.append(" AND l.M_Warehouse_ID=?)");
			
			params.add(M_Product_ID, M_Warehouse_ID);		
			// With ASI
			if (M_AttributeSetInstance_ID != 0) {
				sql.append(" AND s.M_AttributeSetInstance_ID=? AND r.M_AttributeSetInstance_ID=?");
				params.add(M_AttributeSetInstance_ID, M_AttributeSetInstance_ID);
			}
		}

		//
		BigDecimal retValue = DB.getSQLValueBD(trxName, sql.toString(), params);
		if (CLogMgt.isLevelFine())
			s_log.fine("M_Warehouse_ID=" + M_Warehouse_ID + ", M_Locator_ID=" + M_Locator_ID 
				+ ",M_Product_ID=" + M_Product_ID + " = " + retValue);
		return retValue;
	}	//	getQtyAvailable
}
