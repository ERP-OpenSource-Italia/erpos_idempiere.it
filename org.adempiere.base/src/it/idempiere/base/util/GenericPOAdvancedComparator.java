package it.idempiere.base.util;

import java.util.ArrayList;
import java.util.Comparator;

import org.compiere.model.PO;

public class GenericPOAdvancedComparator implements Comparator<PO>
{
	private ArrayList<String> lstColNames = new ArrayList<String>();
	private ArrayList<Boolean> lstNullLast = new ArrayList<Boolean>();
	private ArrayList<Boolean> lstOrderDesc = new ArrayList<Boolean>();
	
	/** Add order by column, null last false 
	 * @param colName
	 */
	public void addOrderColumn(String colName)
	{
		addOrderColumn(colName, true, false);
	}
	
	/** Add order by column
	 * @param colName
	 * @param nullLast
	 */
	public void addOrderColumn(String colName, boolean nullLast)
	{
		addOrderColumn(colName, nullLast, false);
	}
	
	/** Add order by column
	 * @param colName
	 * @param nullLast
	 */
	public void addOrderColumn(String colName, boolean nullLast, boolean orderDesc)
	{
		lstColNames.add(colName);
		lstNullLast.add(nullLast);
		lstOrderDesc.add(orderDesc);
	}
	
	/** Add order by column
	 * @param colName
	 * @param nullLast
	 * @param index
	 */
	public void addOrderColumn(String colName, boolean nullLast, boolean orderDesc, int index)
	{
		lstColNames.add(index, colName);
		lstNullLast.add(index, nullLast);
		lstOrderDesc.add(index, orderDesc);
	}
	
	@SuppressWarnings("unchecked")
	public int compare(PO po1, PO po2)
	{
		int iCmp = 0;
		
		if(po1 == null || po2 == null)
		{
			//PO null last
			if(po1 == null && po2 != null)
				iCmp = 1;
			else if(po1 != null && po2 == null)
				iCmp = -1;
		}
		else
		{
			for(int i=0; i < lstColNames.size(); i++)
			{
				Object value1 = po1.get_Value(lstColNames.get(i));
				Object value2 = po2.get_Value(lstColNames.get(i));
				
				iCmp = compareValue(value1, value2, lstNullLast.get(i), lstOrderDesc.get(i));
				
				if(iCmp != 0)
					break;
			}
		}
		
		return iCmp;
	}
	
	protected int compareValue(Object value1, Object value2, boolean nullLast, boolean orderDesc)
	{
		int iCmp = 0;
		
		if(value1 != null)
		{
			if(value2 == null)
			{
				if(nullLast)
					iCmp = -1;
				else
					iCmp = 1;
			}
			else
			{
				if(value1 instanceof Comparable)
					iCmp = ((Comparable) value1).compareTo(value2);
				else
					iCmp = value1.toString().compareTo(value2.toString());
				
				if(orderDesc)
					iCmp = -iCmp;
			}
		}
		else if(value2 != null)
		{
			if(nullLast)
				iCmp = 1;
			else
				iCmp = -1;
		}
		
		return iCmp;
	}
}
