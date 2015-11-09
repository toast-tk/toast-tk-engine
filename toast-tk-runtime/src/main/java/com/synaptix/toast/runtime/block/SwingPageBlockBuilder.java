package com.synaptix.toast.runtime.block;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.dao.domain.impl.test.block.SwingPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.SwingPageConfigLine;
import com.synaptix.toast.runtime.IActionItemRepository;

public class SwingPageBlockBuilder implements IBlockRunner<SwingPageBlock>{

	@Inject
	IActionItemRepository objectRepository;
	
	@Override
	public void run(SwingPageBlock block) {
		objectRepository.addSwingPage(block.getFixtureName());
		IFeedableSwingPage swingPage = objectRepository.getSwingPage(block.getFixtureName());
		for(SwingPageConfigLine line : block.getBlockLines()) {
			swingPage.addElement(line.getElementName(), line.getType(), line.getLocator());
		}		
	}
	
	@Override
	public void setInjector(Injector injector) {
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}
}
