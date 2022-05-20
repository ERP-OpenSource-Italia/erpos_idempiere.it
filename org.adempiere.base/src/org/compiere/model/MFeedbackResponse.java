package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MFeedbackResponse extends X_AD_Feedback_Response
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2161161758592033514L;


	public MFeedbackResponse(Properties ctx, int AD_Feedback_Response, String trxName)
	{
		super(ctx, AD_Feedback_Response, trxName);
	}

	public MFeedbackResponse(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
}