package io.toast.tk.test.runtime;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.parse.TestParser;

/**
 * Test including files
 */
public class TestParserTestCase_4 {

    @Test
    public void test() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL testFileUrl = classLoader.getResource("test_file_1.txt");
        Assert.assertNotNull(testFileUrl);
        String path = testFileUrl.getPath();
        System.out.println("path = " + path);
        ITestPage testPage = null;
        try {
            testPage = new TestParser().parse(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("testPage = " + testPage);

        Assert.assertNotNull(testPage);
        Assert.assertEquals("test_file_1.txt", testPage.getName());

    }

}
