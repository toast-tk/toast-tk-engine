package io.toast.tk.test.runtime;

import io.toast.tk.runtime.dao.DAOManager;
import io.toast.tk.runtime.report.HTMLReporter;

public class TestParserTestCase_7 {

	public static void main(String[] args) {
		try {
			DAOManager.getInstance("10.23.252.131", 27017);
			String projectHTMLReport = HTMLReporter.getProjectHTMLReport("rus.3.7.campaign.script");
			System.out.println(projectHTMLReport.length());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
