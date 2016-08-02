package com.synaptix.toast.test.runtime;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.parse.ScriptHelper;
import com.synaptix.toast.runtime.parse.TestParser;

/**
 * Test swing page files
 */
public class TestParserTestCase_5 {

    @Test
    public void test() {
        String filename = "test_file_3.txt";
        ITestPage testPage = null;

        try {
            List<String> lines = ScriptHelper.getScript(filename);
            testPage = new TestParser().parse(lines, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("testPage = " + testPage);

        Assert.assertNotNull(testPage);
        Assert.assertEquals(testPage.getBlocks().size(), 5);
        //Error: the right value should be 1
        //currently creates a comment block and a swing page block
        //expecting only a swing page block

    }

}
