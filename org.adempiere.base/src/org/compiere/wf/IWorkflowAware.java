package org.compiere.wf;

import org.compiere.wf.MWFActivity;

/** Interface for elements aware of the the related workflow
 * 
 * @author Silvano Trinchero, www.freepath.it
 * 			<li>IDEMPIERE-3209 new interface for workflow-aware objects
 *
 */
public interface IWorkflowAware
{
	public void setWFActivity(MWFActivity activity);
	public boolean isShouldRefreshActivityList();
}
