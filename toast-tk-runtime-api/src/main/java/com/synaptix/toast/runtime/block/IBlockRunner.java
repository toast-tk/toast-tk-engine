package com.synaptix.toast.runtime.block;

import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;

public interface IBlockRunner<E extends IBlock> {
	
	void run(final E block) throws IllegalAccessException, ClassNotFoundException;

	void setInjector(final Injector injector);

}