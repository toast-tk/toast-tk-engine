package com.synaptix.toast.runtime.block;

import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.runtime.IActionItemRepository;

public interface IBlockRunner<E extends IBlock> {
	
	public void run(E block) throws FatalExcecutionError;
	
	void setRepository(final IActionItemRepository repository);

}