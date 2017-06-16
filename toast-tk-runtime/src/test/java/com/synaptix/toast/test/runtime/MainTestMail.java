package com.synaptix.toast.test.runtime;

import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.runtime.AbstractTestPlanRunner;
import io.toast.tk.runtime.parse.TestPlanParser;

import java.io.IOException;

public class MainTestMail extends AbstractTestPlanRunner {

    public void tearDownEnvironment() {
    }

    public void beginTest() {
    }

    public void endTest() {
    }

    public void initEnvironment() {
    }

	@Override
	public String getReportsOutputPath() {
		return "./toast/test-results";
	}

	public static void main(String[] args) throws Exception {
        MainTestMail testRunner = new MainTestMail();

        try {
            TestPlanParser projectParser = new TestPlanParser();
            ITestPlan testPlan = projectParser.parse("test_campaign.md");
            testRunner.execute(testPlan, false, null);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
