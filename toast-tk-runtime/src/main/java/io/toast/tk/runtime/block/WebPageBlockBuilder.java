package io.toast.tk.runtime.block;

import com.google.inject.Inject;

import io.toast.tk.adapter.web.component.DefaultWebPage;
import io.toast.tk.dao.domain.impl.test.block.WebPageBlock;
import io.toast.tk.dao.domain.impl.test.block.line.WebPageConfigLine;
import io.toast.tk.runtime.IActionItemRepository;

public  class WebPageBlockBuilder implements IBlockRunner<WebPageBlock>{
	
	@Inject
	private	IActionItemRepository objectRepository;
	
	@Override
	public void run(final WebPageBlock block) {
		final DefaultWebPage webPage = new DefaultWebPage();
		block.getBlockLines().forEach(line -> addElement(webPage, line));
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
	public void setRepository(final IActionItemRepository objectRepository) {
		this.objectRepository = objectRepository;
	}
}