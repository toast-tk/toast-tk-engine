package io.toast.tk.dao.domain.impl.repository;

import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;

public class ReportHelper {
	
	public static boolean isSuccess(final ITestPage testPage) {
		return (testPage.getTechnicalErrorNumber() + testPage.getTestFailureNumber()) == 0;
	}
	
	public static int getTotalOk(final ITestPlan project) {
		int total = 0;
		for(ICampaign campaign : project.getCampaigns()) {
			for(ITestPage testPage : campaign.getTestCases()) {
				if(ReportHelper.isSuccess(testPage)) {
					++total;
				}
			}
		}
		return total;
	}
	
	public static int getTotalKo(final ITestPlan project) {
		int total = 0;
		for(ICampaign campaign : project.getCampaigns()) {
			for(ITestPage testPage : campaign.getTestCases()) {
				if(!ReportHelper.isSuccess(testPage)) {
					++total;
				}
			}
		}
		return total;
	}
}