package com.synaptix.toast.test.runtime;

import org.junit.BeforeClass;

public class TestParserTestCase_2 {

	static StringBuilder b = new StringBuilder();

	@BeforeClass
	public static void init() {
		b.append("$var:=select 1 from dual").append("\n");
		b.append("|| scenario || swing ||").append("\n");
		b.append("| @swing Saisir *valeur* dans *ChooseApplicationRusDialog.applicationBox* |").append("\n");
		b.append("| @service Cliquer sur *ChooseApplicationRusDialog.OK* |").append("\n");
		b.append("| @service Cliquer sur *ChooseApplicationRusDialog.FIN* |").append("\n");
		b.append("| @toto Cliquer sur *ChooseApplicationRusDialog.FIN* |").append("\n");
		b.append("| Cliquer sur *ChooseApplicationRusDialog.KO* |").append("\n");
		b.append("| @swing:connector Saisir *valeur* dans *ChooseApplicationRusDialog.applicationBox* |").append("\n");
	}

	// static check only
	/*private void testReportImageDisplay() {
		ITestPage page = new TestPage("test");
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage capture;
		try {
			capture = new java.awt.Robot().createScreenCapture(screenRect);
			TestResult result = new TestResult("failure", null);
			result.setContextualTestSentence("test");
			TestBlock block = new TestBlock();
			block.addLine("some test", "", "");
			block.getBlockLines().get(0).setTestResult(result);
			page.addBlock(block);
			ThymeLeafHTMLReporter reporter = new ThymeLeafHTMLReporter();
			String generatePageHtml = reporter.generatePageHtml(page);
			reporter.writeFile(generatePageHtml, "go", "c:\\temp");
		}
		catch(AWTException e) {
			e.printStackTrace();
		}
	}*/
}
