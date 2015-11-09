package com.synaptix.toast.runtime.block;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.SwingPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.VariableBlock;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;

public class BlockRunnerProvider {

	private static final Logger LOG = LogManager.getLogger(BlockRunnerProvider.class);
	private Map<Class<? extends IBlock>, IBlockRunner<? extends IBlock>> map;
	
	@Inject
	public BlockRunnerProvider(){
		map = new HashMap<Class<? extends IBlock>, IBlockRunner<? extends IBlock>>();
		map.put(WebPageBlock.class, new WebPageBlockBuilder());
		map.put(TestBlock.class, new TestBlockRunner());
		map.put(SwingPageBlock.class, new SwingPageBlockBuilder());
		map.put(VariableBlock.class, new VariableBlockBuilder());
	}
		
	public IBlockRunner<? extends IBlock> getBlockRunner(Class<? extends IBlock> clazz, Injector injector) throws IllegalAccessException{
		IBlockRunner<? extends IBlock> runner = map.get(clazz);
		if(runner != null){
			runner.setInjector(injector);
		}else{
			LOG.warn("No runner found for : " + clazz.getSimpleName());
		}
		return runner;
	}
	
}
