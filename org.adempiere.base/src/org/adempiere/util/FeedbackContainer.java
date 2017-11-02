package org.adempiere.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.base.event.EventManager;
import org.adempiere.base.event.GatherFeedbackRequestEvent;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.osgi.service.event.Event;

public class FeedbackContainer implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2472325200502656692L;
	
	private Map<String,FeedbackRequest> requests = new LinkedHashMap<>();
	
	private static ThreadLocal<FeedbackContainer> threadContainer = new ThreadLocal<>();
	
	public static FeedbackContainer getCurrent()
	{
		return threadContainer.get();
	}
	
	public static void setCurrent(FeedbackContainer container)
	{
		threadContainer.set(container);
	}
	
	public void addRequest(FeedbackRequest request)
	{
		if(requests.containsKey(request.getId()))
				throw new AdempiereException("Feedback request id already added");
		
		requests.put(request.getId(), request);
	}

	public FeedbackRequest getRequest(String id)
	{
		return requests.get(id);
	}
	
	public Collection<FeedbackRequest> getAll()
	{
		return requests.values();
	}
	
	public boolean isEmpty()
	{
		return requests.isEmpty();
	}
	
	public List<FeedbackRequest> getByType(int type)
	{
		ArrayList<FeedbackRequest> listByType = new ArrayList<>();
		
		for(FeedbackRequest r:requests.values())
		{
			if(r.getType() == type)
				listByType.add(r);
		}
		
		return listByType;
	}
	
	public static String getEventTopicForDocEent(String docStatus)
	{
		switch(docStatus)
		{
		case DocAction.ACTION_Prepare: return IEventTopics.DOC_BEFORE_PREPARE;
		case DocAction.ACTION_Void: return IEventTopics.DOC_BEFORE_VOID;
		case DocAction.ACTION_Close: return IEventTopics.DOC_BEFORE_CLOSE;
		case DocAction.ACTION_ReActivate: return IEventTopics.DOC_BEFORE_REACTIVATE;
		case DocAction.ACTION_Complete: return IEventTopics.DOC_BEFORE_COMPLETE;
		case DocAction.ACTION_Reverse_Accrual: return IEventTopics.DOC_BEFORE_REVERSEACCRUAL;
		case DocAction.ACTION_Reverse_Correct: return IEventTopics.DOC_BEFORE_REVERSECORRECT;
		default: return null;
		}
	}
	
	public static FeedbackContainer gatherFeedback(PO po, String gatherForEventTopic)
	{
		FeedbackContainer container = new FeedbackContainer();
		GatherFeedbackRequestEvent event = new GatherFeedbackRequestEvent(container, po, gatherForEventTopic);
		
		Event evt =  EventManager.newEvent(IEventTopics.GATHER_FEEDBACK, event);
		EventManager.getInstance().sendEvent(evt);
		
		return container;
	}
}
