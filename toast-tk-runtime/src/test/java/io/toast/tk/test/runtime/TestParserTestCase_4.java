package io.toast.tk.test.runtime;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.runtime.parse.FileHelper;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.parse.TestParser;

/**
 * Test including files
 */
public class TestParserTestCase_4 {

    @Test
    public void test() {
        String filename = "test_file_1.txt";
        ITestPage testPage = null;

        try {
            testPage = new TestParser().parse(FileHelper.getScript(filename), filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("testPage = " + testPage);

        Assert.assertNotNull(testPage);
        Assert.assertEquals(filename, testPage.getName());

    }

}
