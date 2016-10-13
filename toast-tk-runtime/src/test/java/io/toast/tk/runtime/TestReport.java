package io.toast.tk.runtime;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.dao.domain.DaoBeanFactory;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.runtime.report.ThymeLeafHTMLReporter;

/**
 * Test see included files in generated report
 */
public class TestReport {

	@Test
	public void test() {
		String textInSecondSubPage = "Do something in second included page";
		String textInSubPage = "Do something in included page";
		String textInPage = "Do something";

		ITestPage secondSubTestPage = DaoBeanFactory.getBean(ITestPage.class);
		assert secondSubTestPage != null;
		TestBlock secondSubTestBlock = new TestBlock();
		secondSubTestBlock.addLine(textInSecondSubPage, "expected", "comment");
		secondSubTestPage.addBlock(secondSubTestBlock);

		ITestPage subTestPage = DaoBeanFactory.getBean(ITestPage.class);
		assert subTestPage != null;
		TestBlock subTestBlock = new TestBlock();
		subTestBlock.addLine(textInSubPage, "expected", "comment");
		subTestPage.addBlock(subTestBlock);
		subTestPage.addBlock(secondSubTestPage);

		ITestPage testPage = DaoBeanFactory.getBean(ITestPage.class);
		assert testPage != null;
		TestBlock testBlock = new TestBlock();
		testBlock.addLine(textInPage, "expected", "comment");
		testPage.addBlock(testBlock);
		testPage.addBlock(subTestPage);

		ThymeLeafHTMLReporter thymeLeafHTMLReporter = new ThymeLeafHTMLReporter();
		String s = thymeLeafHTMLReporter.generatePageHtml(testPage);

		Assert.assertTrue(s.contains(textInPage));

		Assert.assertTrue(s.contains(textInSubPage));

		Assert.assertTrue(s.contains(textInSecondSubPage));
	}
}
