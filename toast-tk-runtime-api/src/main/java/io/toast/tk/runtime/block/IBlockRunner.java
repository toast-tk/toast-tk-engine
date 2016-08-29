package io.toast.tk.runtime.block;

import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.runtime.IActionItemRepository;

public interface IBlockRunner<E extends IBlock> {
	
	void run(E block) throws FatalExcecutionError;
	
	void setRepository(final IActionItemRepository repository);

}