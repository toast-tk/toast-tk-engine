package com.synaptix.toast.dao.domain.impl.repository;

import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.IProject;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public class ReportHelper {
	
	public static boolean isSuccess(final ITestPage testPage) {
		return (testPage.getTechnicalErrorNumber() + testPage.getTestFailureNumber()) == 0;
	}
	
	public static int getTotalOk(final IProject project) {
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
	
	public static int getTotalKo(final IProject project) {
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