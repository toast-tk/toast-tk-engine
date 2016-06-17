package com.synaptix.toast.runtime.block;

import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.runtime.IActionItemRepository;

public interface IBlockRunner<E extends IBlock> {
	
	void run(final E block);
	
	void setRepository(final IActionItemRepository repository);

}