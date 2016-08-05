package io.toast.tk.test.runtime;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.parse.ProjectParser;

/**
 * Test with a project definition
 */
public class TestParserTestCase_6 {

    @Test
    public void test() {
        String filename = "test_file_4_project.txt";
        IProject project = null;

        try {
            project = new ProjectParser().parse(filename);
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
        Assert.assertEquals("Test case 1", testCases.get(0).getName());
        Assert.assertEquals("Test case 2", testCases.get(1).getName());
    }

}
