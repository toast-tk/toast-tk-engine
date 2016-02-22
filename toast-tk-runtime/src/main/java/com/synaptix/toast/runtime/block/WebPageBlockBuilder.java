package com.synaptix.toast.runtime.block;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.web.component.DefaultWebPage;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.WebPageConfigLine;
import com.synaptix.toast.runtime.IActionItemRepository;

public  class WebPageBlockBuilder implements IBlockRunner<WebPageBlock>{
	
	@Inject
	IActionItemRepository objectRepository;
	
	@Override
	public void run(final WebPageBlock block) {
		final DefaultWebPage webPage = new DefaultWebPage();
		block.getBlockLines().stream().forEach(line -> addElement(webPage, line));
		objectRepository.addWebPage(block.getFixtureName(), webPage);
	}

	private static void addElement(
		final DefaultWebPage webPage,
		final WebPageConfigLine line
	) {
		webPage.addElement(
			line.getElementName(),
			line.getType(),
			line.getMethod(),
			line.getLocator(),
			line.getPosition());
	}

	@Override
	public void setInjector(final Injector injector) {
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}
}