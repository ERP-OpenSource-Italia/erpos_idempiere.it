package it.idempiere.base.util;

import java.util.ListIterator;

import org.compiere.process.ProcessInfo;

public class StartedFromPrintPreview 
{
	private static final ProcessInfoListThreadLocal piList = new ProcessInfoListThreadLocal();
	
	
	
	/**
	 * Add process info to list of started from print preview process
	 * 
	 * @param process info
	 */
	
	public static void addProcessInfo(ProcessInfo pi)
	{
		piList.get().add(pi);
	}
	
	/**
	 * Check if process info is started from print preview process
	 * @param process info
	 * @return true if started from print preview process, false otherwise
	 */
	
	public static boolean  isPrintPreview(ProcessInfo pi)
	{		
		for(ProcessInfo o :  piList.get())
		{
			if(o == pi)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Remove process info from list of process info started from print preview process
	 * @param process info
	 */
	
	public static void removeProcessInfo(ProcessInfo pi)
	{
			ListIterator<ProcessInfo> it = piList.get().listIterator();
			
			while(it.hasNext())
			{
				ProcessInfo o = it.next();
				
				if(o == pi)
				{
					it.remove();
					break;
				}			
			}
	}
}
