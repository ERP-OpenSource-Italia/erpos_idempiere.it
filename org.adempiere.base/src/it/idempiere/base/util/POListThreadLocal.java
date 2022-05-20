package it.idempiere.base.util;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.PO;

public class POListThreadLocal extends ThreadLocal<List<PO>>
{

	@Override
	protected List<PO> initialValue()
	{
		return new ArrayList<PO>();
	}

}
