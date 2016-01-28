package com.synaptix.toast.runtime.block;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.web.component.DefaultWebPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.WebPageConfigLine;
import com.synaptix.toast.runtime.IActionItemRepository;

public  class WebPageBlockBuilder implements IBlockRunner<WebPageBlock>{
	
	@Inject
	IActionItemRepository objectRepository;
	
	@Override
	public void run(WebPageBlock block) {
		DefaultWebPage webPage = new DefaultWebPage();
		for(WebPageConfigLine line : block.getBlockLines()) {
			webPage.addElement(
				line.getElementName(),
				line.getType(),
				line.getMethod(),
				line.getLocator(),
				line.getPosition());
		}	
		objectRepository.addPage(block.getFixtureName(), webPage);
	}

	@Override
	public void setInjector(Injector injector) {
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}

}
