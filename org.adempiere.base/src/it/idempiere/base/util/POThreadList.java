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
	
	public PO addPO(PO po)
	{
			poList.get().add(po);
			return po;
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
	
	public PO removePO(PO po)
	{
		ListIterator<PO> it = poList.get().listIterator();
		PO removed = null;
		
		while(it.hasNext())
		{
			PO o = it.next();
			
			if(o == po)
			{
				it.remove();
				removed = po;
				break;
			}			
		}
		
		return removed;
	}
}
