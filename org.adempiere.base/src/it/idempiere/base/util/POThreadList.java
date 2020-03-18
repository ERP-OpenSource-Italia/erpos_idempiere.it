package it.idempiere.base.util;

import java.util.ListIterator;

import org.compiere.model.PO;

/** Utility class for list of PO saved in a thread.
 * 
 * @author strinchero
 *
 */

public class POThreadList
{	
	private final POListThreadLocal poList = new POListThreadLocal();
		
	/**
	 * Add po to list of saved objects
	 * 
	 * @param po
	 */
	
	public void addPO(PO po)
	{
			poList.get().add(po);
	}
	
	/**
	 * Check if po is saved from this source
	 * @param po
	 * @return true if saved by this source, false otherwise
	 */
	
	public boolean  hasPO(PO po)
	{		
		for(PO o :  poList.get())
		{
			if(o.equals(po))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Remove po from list of object kept by this source
	 * @param po
	 */
	
	public void removePO(PO po)
	{
			ListIterator<PO> it = poList.get().listIterator();
			
			while(it.hasNext())
			{
				PO o = it.next();
				
				if(o == po)
				{
					it.remove();
					break;
				}			
			}
	}
}
