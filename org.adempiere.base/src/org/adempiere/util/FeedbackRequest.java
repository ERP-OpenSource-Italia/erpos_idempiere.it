package org.adempiere.util;

import java.io.Serializable;

public class FeedbackRequest implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5379698791233271051L;

	public static final int TYPE_WARNING = 1,
													TYPE_ASK = 2,
													TYPE_INFO = 3;
	
	private final int			type;
	private final String	id;
	private final Object	data;

	private boolean response = true;
	private String	title;
	private String	message;
	private String messageError;
	
	private boolean persistRequest = true;
	private boolean defaultResponse = false;
		
	public FeedbackRequest(int type,String id,Object data)
	{
		this.type = type;
		this.id = id;
		this.data = data;
	}

	public boolean getResponse()
	{
		return response;
	}

	public void setResponse(boolean response)
	{
		this.response = response;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public int getType()
	{
		return type;
	}

	public String getId()
	{
		return id;
	}

	public Object getData()
	{
		return data;
	}

	public boolean isPersistRequest()
	{
		return persistRequest;
	}

	public void setPersistRequest(boolean persistRequest)
	{
		this.persistRequest = persistRequest;
	}

	public boolean getDefaultResponse()
	{
		return defaultResponse;
	}

	public void setDefaultResponse(boolean defaultResponse)
	{
		this.defaultResponse = defaultResponse;
	}
	
	public String getMessageError()
	{
		return messageError;
	}

	public void setMessageError(String messageError)
	{
		this.messageError = messageError;
	}
}
