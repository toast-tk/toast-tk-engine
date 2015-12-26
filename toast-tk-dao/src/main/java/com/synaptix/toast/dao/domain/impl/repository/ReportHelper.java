package com.synaptix.toast.dao.domain.impl.repository;

import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.IProject;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public class ReportHelper {

	
	public static boolean isSuccess(ITestPage testPage)
	{
		return (testPage.getTechnicalErrorNumber() + testPage.getTestFailureNumber()) == 0;
	}
	
	public static int getTotalOk(IProject project) {
		int total = 0;
		for(ICampaign campaign : project.getCampaigns()) {
			for(ITestPage testPage : campaign.getTestCases()) {
				if(ReportHelper.isSuccess(testPage)) {
					total++;
				}
			}
		}
		return total;
	}
	
	public static int getTotalKo(IProject project) {
		int total = 0;
		for(ICampaign campaign : project.getCampaigns()) {
			for(ITestPage testPage : campaign.getTestCases()) {
				if(!ReportHelper.isSuccess(testPage)) {
					total++;
				}
			}
		}
		return total;
	}
	
   /* @Override
    public IBlock getVarBlock(ITestPage testPage) {
        for (IBlock block : blocks) {
            if (block instanceof VariableBlock) {
                return block;
            }
        }
        return null;
    }*/
}
