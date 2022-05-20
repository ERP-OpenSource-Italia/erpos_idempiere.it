package it.idempiere.base.util;

import org.adempiere.base.event.AbstractEventHandler;
import org.osgi.service.event.Event;

/** Handler con ritorno booleano.
 *  L'uso e' raro, quindi per non riscrivere event manager usiamo un errore codificato per propagare il 'false'
 *  
 * @author strinchero
 *
 */
public abstract class BooleanEventHandler extends AbstractEventHandler
{
	public static final String FALSE = "event.result.false";

	public void setFalse(Event event)
	{
		addErrorMessage(event, FALSE);
	}
}
