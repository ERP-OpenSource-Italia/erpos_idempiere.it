package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;

public class MDepreciationTableHeader extends X_A_Depreciation_Table_Header
{
	private static final long serialVersionUID = 1L;
	
	/**		Static Cache: A_Depreciation_Table_Header.A_Depreciation_Table_Header_ID-> MDepreciationTableHeader					*/
	private static CCache<Integer,MDepreciationTableHeader> s_cache = new CCache<Integer,MDepreciationTableHeader>(Table_Name, 10, 0);
	
	protected	List<X_A_Depreciation_Table_Detail>	m_lstDetails = null;
	protected	int																	m_iGreatestPeriod = -1;
	
	public MDepreciationTableHeader(Properties ctx,
			int A_Depreciation_Table_Header_ID, String trxName)
	{
		super(ctx, A_Depreciation_Table_Header_ID, trxName);		
	}

	public MDepreciationTableHeader(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**	Get Asset Type
	 *	@param	ctx context
	 *	@param	A_Asset_Type_ID
	 *	@return asset type object
	 */
	public static MDepreciationTableHeader get (Properties ctx, int A_Depreciation_Table_Header_ID)
	{
		if (A_Depreciation_Table_Header_ID <= 0)
			return null;
		MDepreciationTableHeader o = s_cache.get(A_Depreciation_Table_Header_ID);
		if (o != null)
			return o;
		o = new MDepreciationTableHeader(ctx, A_Depreciation_Table_Header_ID, null);
		if (o.get_ID() > 0) {
			s_cache.put(A_Depreciation_Table_Header_ID, o);
			return o;
		}
		return null;
	}
	
	public int getGreatestPeriod()
	{
		return m_iGreatestPeriod;
	}
	
	public List<X_A_Depreciation_Table_Detail>	getDetails(String sTrxName)
	{
		if(m_lstDetails == null)
		{
			Query	q = new Query(getCtx(), I_A_Depreciation_Table_Detail.Table_Name, "A_Depreciation_Table_Header_ID = ?", sTrxName);
			q.setOnlyActiveRecords(true);
			q.setOrderBy("A_Period");
			q.setParameters(getA_Depreciation_Table_Header_ID());
			m_lstDetails = q.<X_A_Depreciation_Table_Detail>list();
			
			if(m_lstDetails != null)
			{
				for(X_A_Depreciation_Table_Detail detail:m_lstDetails)
				{
					if(detail.getA_Period() > m_iGreatestPeriod)
						m_iGreatestPeriod = detail.getA_Period();
				}
			}			
		}
		
		return m_lstDetails;		
	}
	
	public	I_A_Depreciation_Table_Detail	getDetailByPeriod(int iPeriod,String sTrxName)
	{
		I_A_Depreciation_Table_Detail	retDetail = null;
		
		for(X_A_Depreciation_Table_Detail detail:getDetails(sTrxName))
		{
			if(detail.getA_Period() == iPeriod)
			{
				retDetail = detail;
				break;
			}
		}
		
		return retDetail;
	}
	

}
