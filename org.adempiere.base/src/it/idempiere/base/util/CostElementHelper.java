package it.idempiere.base.util;

import org.compiere.model.MCostElement;
import org.compiere.model.MRefList;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_M_CostElement;
import org.compiere.util.CLogger;

public class CostElementHelper
{
	private static final CLogger	s_log = CLogger.getCLogger(CostElementHelper.class);
	/**
	 * 	Get Material Cost Element or create it
	 *	@param po parent
	 *	@param CostingMethod method
	 *  @param sCostElementType type
	 *	@return cost element
	 */
	public static MCostElement getCostElementByType (PO po, String CostingMethod, String sCostElementType)
	{
		if (CostingMethod == null || CostingMethod.length() == 0)
		{
			s_log.severe("No CostingMethod");
			return null;
		}
		//
		final String whereClause = "AD_Client_ID=? AND CostingMethod=? AND CostElementType=?";
		MCostElement retValue = new Query(po.getCtx(), X_M_CostElement.Table_Name, whereClause, po.get_TrxName())
			.setParameters(po.getAD_Client_ID(), CostingMethod, sCostElementType)
			.setOrderBy("AD_Org_ID")
			.firstOnly();
		if (retValue != null)
			return retValue;
		
		//	Create New
		retValue = new MCostElement (po.getCtx(), 0, po.get_TrxName());
		String name = MRefList.getListName(po.getCtx(), X_M_CostElement.COSTINGMETHOD_AD_Reference_ID, CostingMethod);
		if (name == null || name.length() == 0)
			name = CostingMethod;
		retValue.setName(name);
		retValue.setCostElementType(sCostElementType);
		retValue.setCostingMethod(CostingMethod);
		retValue.saveEx();
		
		//
		return retValue;
	}	//	getMaterialCostElement

}
