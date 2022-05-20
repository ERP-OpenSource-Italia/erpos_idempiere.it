package it.idempiere.base.util;

public class StateTerminatedException extends RuntimeException
{
	public static final String PREFIX = ":F3P-STE:";

	/**
	 * 
	 */
	private static final long serialVersionUID = -302920871814270289L;

	public StateTerminatedException(String message)
	{
		super(PREFIX + message);
	}
	
	
	/**
	 * Since exception thrown from model validators are re-thrown as AdempiereException, there is the need to know if an exception was (or is) a StateTerminatedException
	 * 
	 * @param e	exception to check
	 * @return if the exception was a StateTerminatedException
	 */
	public static final boolean wasStateTerminatedException(Throwable e)
	{
		if(e instanceof StateTerminatedException)
			return true;
		
		String sMsg = e.getLocalizedMessage();
		
		if(sMsg != null)
		{
			return (sMsg.indexOf(PREFIX) > 0);
		}
		else
		{
			return false;
		}
	}
	
	public static final String getOriginalMessage(Throwable e)
	{
		String sMsg = e.getLocalizedMessage();
		
		if(sMsg != null)
		{
			int idx = sMsg.indexOf(PREFIX);
			
			if(idx > 0)
			{
				idx += PREFIX.length();
				
				return sMsg.substring(idx);
			}
		}

		return sMsg;
	}
}
