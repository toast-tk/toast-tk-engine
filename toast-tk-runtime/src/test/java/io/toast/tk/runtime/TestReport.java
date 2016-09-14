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
        String texteInSubPage = "Do something in included page";
        String texteInPage = "Do something";

        ITestPage subTestPage = DaoBeanFactory.getBean(ITestPage.class);
        assert subTestPage != null;
        TestBlock subTestBlock = new TestBlock();
        subTestBlock.addLine(texteInSubPage, "expected", "comment");
        subTestPage.addBlock(subTestBlock);

        ITestPage testPage = DaoBeanFactory.getBean(ITestPage.class);
        assert testPage != null;
        TestBlock testBlock = new TestBlock();
        testBlock.addLine(texteInPage, "expected", "comment");
        testPage.addBlock(testBlock);
        testPage.addBlock(subTestPage);

        ThymeLeafHTMLReporter thymeLeafHTMLReporter = new ThymeLeafHTMLReporter();
        String s = thymeLeafHTMLReporter.generatePageHtml(testPage);

        Assert.assertTrue(s.contains(texteInPage));

        Assert.assertTrue(s.contains(texteInSubPage));
    }

}
