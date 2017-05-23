package io.toast.tk.runtime;


public class TestReportRunner extends AbstractScenarioRunner {
	
	public void tearDownEnvironment() {
	}

	@Override
	public void beginTest() {
	}

	@Override
	public void endTest() {
	}

	@Override
	public void initEnvironment() {
	}

	public static void main(String[] args) throws Exception {
		TestReportRunner testRunner = new TestReportRunner();
		testRunner.run("test_report.md");
	}


}
