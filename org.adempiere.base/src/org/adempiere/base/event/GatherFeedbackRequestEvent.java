package org.adempiere.base.event;

import java.io.Serializable;

import org.adempiere.util.FeedbackContainer;
import org.compiere.model.PO;

public class GatherFeedbackRequestEvent implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3355886307341452286L;
	
	private final FeedbackContainer container;
	private final PO								po;
	private final String						feedbackForEventTopic;

	public GatherFeedbackRequestEvent(FeedbackContainer container, PO po, String topic)
	{
		this.container = container;
		this.po = po;
		this.feedbackForEventTopic = topic;		
	}

	public FeedbackContainer getContainer()
	{
		return container;
	}

	public PO getPo()
	{
		return po;
	}

	public String getFeedbackForEventTopic()
	{
		return feedbackForEventTopic;
	}

}
