package com.synaptix.toast.runtime.block;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.test.block.VariableBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.BlockLine;
import com.synaptix.toast.runtime.IActionItemRepository;

public class VariableBlockBuilder implements IBlockRunner<VariableBlock>{

	@Inject
	IActionItemRepository objectRepository;
	
	@Override
	public void run(VariableBlock block) {
		List<BlockLine> blockLines = block.getBlockLines();
		for(BlockLine blockLine : blockLines) {
			String varName = getCellAt(0, blockLine);
			String varValue = getCellAt(1, blockLine);
			objectRepository.getUserVariables().put(varName, varValue);
		}
	}
	

	public String getCellAt(
		int index, BlockLine blockLine) {
		final List<String> cells = blockLine.getCells();
		if(index < 0 || index >= cells.size()) {
			return null;
		}
		return cells.get(index);
	}
	
	@Override
	public void setInjector(Injector injector) {
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}
}
