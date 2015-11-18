package com.synaptix.toast.test.runtime;

import com.synaptix.toast.dao.domain.impl.test.block.CampaignBlock;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.parse.TestParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Test with a project definition
 */
public class TestParserTestCase_6 {

    @Test
    public void test() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL testFileUrl = classLoader.getResource("test_file_4_project.txt");
        Assert.assertNotNull(testFileUrl);
        String path = testFileUrl.getPath();
        System.out.println("path = " + path);
        ITestPage testPage = null;
        try {
            testPage = new TestParser().parse(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("testPage = " + testPage);

        Assert.assertNotNull(testPage);
        Assert.assertEquals(testPage.getBlocks().size(), 1);

        IBlock block = testPage.getBlocks().get(0);
        Assert.assertEquals("campaign", block.getBlockType());
        if (block instanceof CampaignBlock) {
            CampaignBlock campaignBlock = (CampaignBlock) block;
            Assert.assertEquals(2, campaignBlock.getTestCases().size());
        }


    }

}
