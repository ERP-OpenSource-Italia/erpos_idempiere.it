/******************************************************************************
 * Copyright (C) 2013 Heng Sin Low                                            *
 * Copyright (C) 2013 Trek Global                 							  *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.idempiere.server.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.adempiere.server.IServerFactory;
import org.compiere.model.MRequestProcessor;
import org.compiere.server.RequestProcessor;

/**
 * @author hengsin
 *
 */
public class DefaultRequestProcessorFactory implements IServerFactory<RequestProcessor, MRequestProcessor> {

	/**
	 * 
	 */
	public DefaultRequestProcessorFactory() {
	}

	/* (non-Javadoc)
	 * @see org.adempiere.server.IServerFactory#create(java.util.Properties)
	 */
	@Override
	public RequestProcessor[] create(Properties ctx) {
		MRequestProcessor[] requestModels = MRequestProcessor.getActive(ctx);
		List<RequestProcessor> list = new ArrayList<RequestProcessor>();
		for (MRequestProcessor pModel : requestModels)
		{
			RequestProcessor processor = create(ctx, pModel);
			list.add(processor);
		}
		return list.toArray(new RequestProcessor[0]);
	}

	@Override
	public Class<MRequestProcessor> getProcessorClass() {
		return MRequestProcessor.class;
	}

	@Override
	public RequestProcessor create(Properties ctx, MRequestProcessor serverModel) {
		return new RequestProcessor(serverModel);
	}
}
