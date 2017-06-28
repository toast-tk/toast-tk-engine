package com.synaptix.toast.test.runtime;

import io.toast.tk.runtime.AbstractScenarioRunner;

public class MainTestReportGeneration extends AbstractScenarioRunner { 


	public void tearDownEnvironment() {
	}

	
	public void beginTest() { 
	}

	
	public void endTest() {
	}

	
	public void initEnvironment() {
	}

	public static void main(String[] args) throws Exception {
		MainTestReportGeneration testRunner = new MainTestReportGeneration();
		testRunner.runScenario("test_report.md");
	}
	
}
