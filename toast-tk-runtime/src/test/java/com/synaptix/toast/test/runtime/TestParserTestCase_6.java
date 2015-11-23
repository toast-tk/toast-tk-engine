package com.synaptix.toast.test.runtime;

import com.synaptix.toast.dao.domain.impl.test.block.*;
import com.synaptix.toast.runtime.parse.ProjectParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
        IProject project = null;
        try {
            project = new ProjectParser().parse(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(project);
        Assert.assertEquals(1, project.getCampaigns().size());
        ICampaign campaign = project.getCampaigns().get(0);
        Assert.assertEquals("TNR v1.0",campaign.getName());
        List<ITestPage> testCases = campaign.getTestCases();
        Assert.assertNotNull(testCases.size());
        Assert.assertEquals(2, testCases.size());
        for (ITestPage testPage : testCases) {
            Assert.assertNotNull(testPage.getBlocks());
        }
        Assert.assertEquals("test_file_2.txt",testCases.get(0).getName());
        Assert.assertEquals("test_file_3.txt",testCases.get(1).getName());

    }

}
