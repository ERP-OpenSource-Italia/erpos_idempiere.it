package it.idempiere.base.acct;

import java.util.List;

import org.adempiere.base.event.EventManager;
import org.adempiere.base.event.IEventManager;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.acct.Doc;
import org.compiere.model.MAcctSchema;
import org.compiere.model.PO;
import org.osgi.service.event.Event;

import it.idempiere.base.util.BooleanEventHandler;

public class AccountingPreCheck
{
	public static final String TOPIC_ISPOSTABLE = "it/idempiere/base/acct/ispostableprecheckevent";
	
	private static IEventManager eventManager = null;
	
	public static boolean isPostable(PO po, Doc doc, MAcctSchema schema, boolean before)
	{
		if(eventManager == null)
			eventManager = EventManager.getInstance();
		
		IsPostablePreCheckEvent eventData = new IsPostablePreCheckEvent(po,doc,schema, before);
		
		Event	event = EventManager.newEvent(TOPIC_ISPOSTABLE, eventData);
		eventManager.sendEvent(event);
		
		boolean isPostable = true;
		
		@SuppressWarnings("unchecked")
		List<String> errors = (List<String>) event.getProperty(IEventManager.EVENT_ERROR_MESSAGES);
		if (errors != null && !errors.isEmpty())
		{
			for(String error:errors)
			{
				if(error.equals(BooleanEventHandler.FALSE))
				{
					isPostable = false;
				}
				else
				{
					throw new AdempiereException(error);
				}
			}
		}

		return isPostable;
	}

}
