package io.toast.tk.runtime.block;

import com.google.inject.Inject;

import io.toast.tk.core.runtime.IFeedableSwingPage;
import io.toast.tk.dao.domain.impl.test.block.SwingPageBlock;
import io.toast.tk.runtime.IActionItemRepository;

public class SwingPageBlockBuilder implements IBlockRunner<SwingPageBlock> {

	@Inject
	private	IActionItemRepository objectRepository;

	@Override
	public void run(final SwingPageBlock block) {
		objectRepository.addSwingPage(block.getFixtureName());
		final IFeedableSwingPage swingPage = objectRepository.getSwingPage(block.getFixtureName());
		block.getBlockLines().forEach(line -> swingPage.addElement(line.getElementName(), line.getType(), line.getLocator()));
	}
	
	@Override
	public void setRepository(final IActionItemRepository objectRepository) {
		this.objectRepository = objectRepository;
	}
}