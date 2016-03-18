package com.synaptix.toast.runtime.report;

import java.util.List;

import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.repository.ReportHelper;
import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public class TemplateReportHelper {

	public static double[][] getExecTrendData(
		final Project project,
		final List<Project> projectsHistory
	) {
		final double[][] array = new double[projectsHistory.size()+1][2];
		int projectIndex = 0;
		for(final Project p : projectsHistory) {
			long executionTotal = 0;
			for(final ICampaign campaign : p.getCampaigns()) {
				for(final ITestPage testPage : campaign.getTestCases()) {
					executionTotal += testPage.getExecutionTime();
				}
			}
			array[projectIndex][0] = p.getIteration();
			array[projectIndex][1] = executionTotal / 1000;
			projectIndex++;	
		}
		long executionTotal = 0;
		for(final ICampaign campaign : project.getCampaigns()) {
			for(final ITestPage testPage : campaign.getTestCases()) {
				executionTotal += testPage.getExecutionTime();
			}
		}
		array[projectIndex][0] = project.getIteration();
		array[projectIndex][1] = executionTotal / 1000;
		return array;
	}
	
	public static double[][] getResultTrendData(
		final Project project,
		final List<Project> projectsHistory
	) {
		final double[][] array = new double[projectsHistory.size()+1][5];
		int projectIndex = 0;
		
		for(final Project p : projectsHistory) {
			majTrendData(array, projectIndex, p);
			projectIndex++;
		}
		
		majTrendData(array, projectIndex, project);

		return array;
	}

	private static void majTrendData(
		final double[][] array, 
		final int projectIndex,
		final Project p
	) {
		array[projectIndex][0] = p.getIteration();
		array[projectIndex][1] = ReportHelper.getTotalOk(p);
		array[projectIndex][2] = ReportHelper.getTotalKo(p);
		array[projectIndex][3] = 0.0;
		array[projectIndex][4] = 0.0;
	}
}