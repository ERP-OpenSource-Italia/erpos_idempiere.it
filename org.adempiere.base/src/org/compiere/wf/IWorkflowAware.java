package org.compiere.wf;

import org.compiere.wf.MWFActivity;

/** Interface for elements aware of the the related workflow
 * 
 * @author Silvano Trinchero, www.freepath.it
 * 			<li>IDEMPIERE-3209 changed functions to public to improve integration support
 *
 */
public interface IWorkflowAware
{
	public void setWFActivity(MWFActivity activity);
	public boolean isShouldRefreshActivityList();
}
