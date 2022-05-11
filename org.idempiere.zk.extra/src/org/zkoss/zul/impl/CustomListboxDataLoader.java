package org.zkoss.zul.impl;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;

public class CustomListboxDataLoader extends ListboxDataLoader{
	//LS ListboxDataLoader does not save offset (getOffset() return 0) and limit (getLimit() return 50) 
	protected int _limit;
	protected int _offset;
	
	@Override
	public void init(Component owner, int offset, int limit) {
		super.init(owner, offset, limit);
		_offset = offset;
		_limit = limit;
	}

	@Override
	public int getOffset() {
		return _offset;
	}

	@Override
	public int getLimit() {
		return ((Listbox)getCropOwner()).getRows() > 0 ? ((Listbox)getCropOwner()).getRows() + 5 : _limit; 
	}

	
}
