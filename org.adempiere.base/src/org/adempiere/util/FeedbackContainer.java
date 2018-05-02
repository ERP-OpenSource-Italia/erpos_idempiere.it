package org.adempiere.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.event.EventManager;
import org.adempiere.base.event.GatherFeedbackRequestEvent;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MFeedbackResponse;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

public class FeedbackContainer implements Serializable
{
	public static final String INFO_SEPARATOR = " - ";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2472325200502656692L;
	
	private static CLogger	log = CLogger.getCLogger(FeedbackContainer.class);
	
	private Map<String,FeedbackRequest> requests = new LinkedHashMap<>();
	
	private static ThreadLocal<FeedbackContainer> threadContainer = new ThreadLocal<>();
	
	private static PO feebackForPO = null;
	
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
	
	public String getInfoFeedback()
	{
		StringBuilder sbInfo = new StringBuilder();
		List<FeedbackRequest> infoReqs = getByType(FeedbackRequest.TYPE_INFO);
		
		for(FeedbackRequest info:infoReqs)
		{
			if(sbInfo.length() > 0)
			{
				sbInfo.append(INFO_SEPARATOR);
			}
			
			sbInfo.append(info.getMessage());
		}
		
		if(sbInfo.length() == 0)
			return null;
				
		return sbInfo.toString();
	}
	
	public void appendInfoFeedback(StringBuilder sbInfo)
	{
		String additionalInfo = getInfoFeedback();
		
		if(additionalInfo != null)
		{
			if(sbInfo.length() > 0)
				sbInfo.append(INFO_SEPARATOR);
			
			sbInfo.append(additionalInfo);
		}		
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
		
		feebackForPO = po;
		
		return container;
	}
	
	//Managed feedback container save
	public void saveFeedbackRequest()
	{
		saveFeedbackRequest(-1);
	}
	
	public void saveFeedbackRequest(int recordID)
	{
		Collection<FeedbackRequest> cont = getAll();
		
		Properties ctx = Env.getCtx();
		int AD_User_ID = Env.getAD_User_ID(ctx);
		int AD_Table_ID = feebackForPO.get_Table_ID();
		
		if(recordID < 0)
			recordID = feebackForPO.get_ID();
		else if(recordID == 0)
			return ;
		
		for(FeedbackRequest feedback: cont)
		{
			if(feedback.isPersistRequest())
			{
				try
				{
					MFeedbackResponse feedbackResponse = PO.create(ctx, MFeedbackResponse.Table_Name, null);
					
					feedbackResponse.setAD_User_ID(AD_User_ID);
					feedbackResponse.setAD_Table_ID(AD_Table_ID);
					
					feedbackResponse.setRecord_ID(recordID);
					feedbackResponse.setFeedback_RequestID(feedback.getId());
					
					if(feedback.getTitle() != null)
						feedbackResponse.setTitle(feedback.getTitle());
					else
						feedbackResponse.setTitle("-");
				
					feedbackResponse.setFeedbackType(feedback.getType());
					feedbackResponse.setMsgRequest(feedback.getMessage());
					
					String response = String.valueOf(feedback.getResponse());
					
					feedbackResponse.setResponse(response);
					feedbackResponse.setMsgError(feedback.getMessageError());
					
					feedbackResponse.saveEx(null);
				}
				catch (AdempiereException e) 
				{
					log.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		
	}
}
