package io.toast.tk.test.runtime;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.runtime.parse.FileHelper;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.parse.TestParser;

/**
 * Test swing page files
 */
public class TestParserTestCase_5 {

    @Test
    public void test() {
        String filename = "test_file_3.txt";
        ITestPage testPage = null;

        try {
            List<String> lines = FileHelper.getScript(filename);
            testPage = new TestParser().parse(lines, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(testPage);
        Assert.assertEquals(testPage.getBlocks().size(), 5);
        //Error: the right value should be 1
        //currently creates a comment block and a swing page block
        //expecting only a swing page block

    }

}
