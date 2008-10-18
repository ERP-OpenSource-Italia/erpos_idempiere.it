/**
 *  Product: Posterita Web-Based POS and Adempiere Plugin
 *  Copyright (C) 2007  Posterita Ltd
 *  This file is part of POSterita
 *  
 *  POSterita is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* Created on Oct 10, 2006 by ashley
* 
*/

/**
	@author ashley
 */

package org.posterita.businesslogic.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MProduct;

import org.posterita.businesslogic.administration.ProductManager;
import org.posterita.businesslogic.core.AttachmentManager;
import org.posterita.exceptions.OperationException;

public class GenericProductImpl implements IProduct
{

	public String getProductName(Properties ctx, int productId, String trxName) throws OperationException
	{
		MProduct product = ProductManager.loadProduct(ctx, productId, trxName);
		
		return product.getName();
	}

	public String getBarcodeForSimilarProducts(Properties ctx, int productId, String trxName) throws OperationException
	{
		return null;
	}

	public HashMap<String, byte[]> getAllImages(Properties ctx, int productId, String trxName) throws OperationException
	{
		HashMap<String, byte[]> imagesMap = new HashMap<String, byte[]>();
		ArrayList<MAttachmentEntry> entryList = AttachmentManager.getAllImagesAttachmentEntries(ctx, MProduct.Table_ID, productId, trxName);
		
		for(MAttachmentEntry entry : entryList)
		{
			imagesMap.put(entry.getName(), entry.getData());
		}
		
		return imagesMap;
	}

}
