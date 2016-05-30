package com.synaptix.toast.runtime.block;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.multibindings.MapBinder;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.SwingPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.VariableBlock;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;

public class BlockRunnerProvider {

	private static final Logger LOG = LogManager.getLogger(BlockRunnerProvider.class);

	private Map<Class<? extends IBlock>, IBlockRunner<? extends IBlock>> blockMap;

	@Inject
	public BlockRunnerProvider() {
		this.blockMap = new HashMap<>();
		initBlockMap();
	}

	private void initBlockMap() {
		blockMap.put(WebPageBlock.class, new WebPageBlockBuilder());
		blockMap.put(TestBlock.class, new TestBlockRunner());
		blockMap.put(SwingPageBlock.class, new SwingPageBlockBuilder());
		blockMap.put(VariableBlock.class, new VariableBlockBuilder());
	}

	public IBlockRunner<? extends IBlock> getBlockRunner(final Class<? extends IBlock> clazz) {
		final IBlockRunner<? extends IBlock> runner = blockMap.get(clazz);
		if (runner == null) {
			LOG.warn("No runner found for : {}", clazz.getSimpleName());
		}
		return runner;
	}
}