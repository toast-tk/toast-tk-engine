package com.synaptix.toast.runtime.block;

import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public interface IBlockRunner<E extends IBlock> {
	
	public void run(E block) throws IllegalAccessException, ClassNotFoundException;

	public void setInjector(Injector injector);

}
