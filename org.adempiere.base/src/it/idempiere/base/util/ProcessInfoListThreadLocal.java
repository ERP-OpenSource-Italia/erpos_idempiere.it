package it.idempiere.base.util;

import java.util.ArrayList;
import java.util.List;

import org.compiere.process.ProcessInfo;

public class ProcessInfoListThreadLocal extends ThreadLocal<List<ProcessInfo>>
{

	@Override
	protected List<ProcessInfo> initialValue()
	{
		return new ArrayList<ProcessInfo>();
	}

}
