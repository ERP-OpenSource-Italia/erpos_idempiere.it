package org.adempiere.webui.util;

import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.Callback;
import org.adempiere.util.FeedbackContainer;
import org.adempiere.util.FeedbackRequest;
import org.adempiere.webui.window.FDialog;
import org.zkoss.zk.ui.Component;

public class UIFeedbackNotifier
{
	private final FeedbackContainer container;
	private final	Callback<UIFeedbackNotifier>	callAfterFeedback;
	private final int								windowNo;
	private final Component					component;
	
	private final List<FeedbackRequest>		warnings;
	private final List<FeedbackRequest>		questions;
	private FeedbackRequest								currentRequest = null;

	public UIFeedbackNotifier(int WindowNo, Component component, FeedbackContainer container, Callback<UIFeedbackNotifier> callAfterFeedback)
	{
		this.container = container;
		this.callAfterFeedback = callAfterFeedback;
		this.windowNo = WindowNo;
		this.component = component;
		
		this.warnings = container.getByType(FeedbackRequest.TYPE_WARNING);
		this.questions = container.getByType(FeedbackRequest.TYPE_ASK);		
	}
	
	public void processFeedback()
	{
		processFeedbackInt(null);
	}
	
	private void processFeedbackInt(Boolean response)
	{
		if(response != null && currentRequest != null)
			currentRequest.setResponse(response.booleanValue());
		
		currentRequest = null;
		
		if(questions.size() > 0)
		{
			currentRequest = questions.remove(0);
		}
		
		if(currentRequest == null && warnings.size() > 0)
		{
			currentRequest = warnings.remove(0);
		}
		
		if(currentRequest != null) // No more request, invoke callback
		{
			if(currentRequest.getTitle() == null)
				throw new AdempiereException("request title cannot be null");
				
			if(currentRequest.getType() == FeedbackRequest.TYPE_ASK)
			{
				FDialog.ask(windowNo, component, currentRequest.getTitle(), null, currentRequest.getMessage(), new AskCallback());
			}
			else
			{
				FDialog.warn(windowNo,component,  null, currentRequest.getMessage(), currentRequest.getTitle(), new WarningCallback());
			}			
		}
		else
		{
			try
			{
				FeedbackContainer.setCurrent(container);
				callAfterFeedback.onCallback(this);
			}
			finally
			{
				FeedbackContainer.setCurrent(null);
			}
		}
	}
	
	private class AskCallback implements Callback<Boolean>
	{
		@Override
		public void onCallback(Boolean result)
		{
			processFeedbackInt(result);			
		}
	}
	
	private class WarningCallback implements Callback<Integer>
	{
		@Override
		public void onCallback(Integer result)
		{
			processFeedbackInt(null);			
		}
	}


}
