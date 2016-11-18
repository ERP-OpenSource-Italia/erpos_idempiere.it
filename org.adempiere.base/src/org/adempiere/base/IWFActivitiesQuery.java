package org.adempiere.base;

import org.compiere.util.WhereClauseAndParams;

public interface IWFActivitiesQuery 
{
	public WhereClauseAndParams refineQuery(WhereClauseAndParams cap);
}
