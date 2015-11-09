package com.synaptix.toast.runtime.report;

import java.util.List;

import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.repository.ReportHelper;
import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;


public class TemplateReportHelper {

	public static double[][] getExecTrendData(
		Project project,
		List<Project> projectsHistory) {
		double[][] array = new double[projectsHistory.size()+1][2];
		int projectIndex = 0;
		for(Project p : projectsHistory) {
			long executionTotal = 0;
			for(ICampaign campaign : p.getCampaigns()) {
				for(ITestPage testPage : campaign.getTestCases()) {
					executionTotal += testPage.getExecutionTime();
				}
			}
			array[projectIndex][0] = p.getIteration();
			array[projectIndex][1] = executionTotal / 1000;
			projectIndex++;	
		}
		long executionTotal = 0;
		for(ICampaign campaign : project.getCampaigns()) {
			for(ITestPage testPage : campaign.getTestCases()) {
				executionTotal += testPage.getExecutionTime();
			}
		}
		array[projectIndex][0] = project.getIteration();
		array[projectIndex][1] = executionTotal / 1000;
		return array;
	}
	
	public static double[][] getResultTrendData(
		Project project,
		List<Project> projectsHistory) {
		double[][] array = new double[projectsHistory.size()+1][5];
		int projectIndex = 0;
		
		for(Project p : projectsHistory) {
			array[projectIndex][0] = p.getIteration();
			array[projectIndex][1] = ReportHelper.getTotalOk(p);
			array[projectIndex][2] = ReportHelper.getTotalKo(p);
			array[projectIndex][3] = 0.0;
			array[projectIndex][4] = 0.0;
			projectIndex++;
		}
		
		array[projectIndex][0] = project.getIteration();
		array[projectIndex][1] = ReportHelper.getTotalOk(project);
		array[projectIndex][2] = ReportHelper.getTotalKo(project);
		array[projectIndex][3] = 0.0;
		array[projectIndex][4] = 0.0;

		return array;
	}


}
