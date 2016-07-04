package io.toast.tk.runtime.block;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Injector;

import io.toast.tk.dao.domain.impl.test.block.VariableBlock;
import io.toast.tk.dao.domain.impl.test.block.line.BlockLine;
import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.block.IBlockRunner;

public class VariableBlockBuilder implements IBlockRunner<VariableBlock>{

	@Inject
	IActionItemRepository objectRepository;
	
	@Override
	public void run(final VariableBlock block) {
		block.getBlockLines().stream().forEach(blockLine -> putUserVariable(blockLine));
	}

	private void putUserVariable(final BlockLine blockLine) {
		objectRepository.getUserVariables().put(getCellAt(0, blockLine), getCellAt(1, blockLine));
	}

	public static String getCellAt(
		final int index, 
		final BlockLine blockLine
	) {
		final List<String> cells = blockLine.getCells();
		if(index < 0 || index >= cells.size()) {
			return null;
		}
		return cells.get(index);
	}
	
	@Override
	public void setRepository(final IActionItemRepository objectRepository) {
		this.objectRepository = objectRepository;
	}
}