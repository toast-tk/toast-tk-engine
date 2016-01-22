package com.synaptix.toast.runtime.report;

import java.util.List;

import org.joda.time.LocalDateTime;

import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;

public class TemplateHelper {

	public static String getBlockName(IBlock block){
		if(block instanceof ITestPage){
			return ((ITestPage)block).getName();
		}
		return null;
	}
	
	public static LocalDateTime getStartTime(IBlock block){
		if(block instanceof ITestPage){
			long startTime =  ((ITestPage)block).getStartDateTime();
			return new LocalDateTime(startTime);
		}
		return null;
	}
	
	public static Long getExecutionTime(IBlock block){
		if(block instanceof ITestPage){
			return ((ITestPage)block).getExecutionTime();
		}
		return 0L;
	}
	
	public static int getTechnicalErrorNumber(IBlock block){
		if(block instanceof ITestPage){
			return ((ITestPage)block).getTechnicalErrorNumber();
		}
		return 0;
	}

	public static int getTestFailureNumber(IBlock block){
		if(block instanceof ITestPage){
			return ((ITestPage)block).getTestFailureNumber();
		}
		return 0;
	}

	public static int getTestSuccessNumber(IBlock block){
		if(block instanceof ITestPage){
			return ((ITestPage)block).getTestSuccessNumber();
		}
		return 0;
	}
	
	public static String getResultKindAsString(
		TestResult testResult) {
		if(testResult != null) {
			return getResultKindAsString(testResult.getResultKind());
		}
		else {
			return "";
		}
	}

	public static String getResultKindAsString(
		ResultKind resultKind) {
		if(ResultKind.SUCCESS.equals(resultKind)) {
			return "success";
		}
		else if(ResultKind.ERROR.equals(resultKind)) {
			return "warning";
		}
		else if(ResultKind.FAILURE.equals(resultKind)) {
			return "danger";
		}
		else if(ResultKind.INFO.equals(resultKind)) {
			return "info";
		}
		return "";
	}

	public static String getResultScreenshotAsBase64(
		TestResult testResult) {
		String screenShot = testResult.getScreenShot();
		return screenShot;
	}

	public static String formatStringToHtml(
		TestLine line) {
		if(line.getTestResult() != null) {
			String message = line.getTestResult().getMessage();
			return message != null ? message.replace("\n", "<br>") : "";
		}
		return "&nbsp;";
	}

	public static String getStepSentence(
		TestLine line) {
		String contextualTestSentence = line.getTestResult() != null ? line.getTestResult().getContextualTestSentence() : null;
		return contextualTestSentence == null ? line.getTest() : contextualTestSentence;
	}

	public static boolean hasScreenShot(
		TestResult testResult) {
		return testResult != null && testResult.getScreenShot() != null;
	}
	
	//FIXME add in template
	private void setExecStatistics(ITestPage testPage, List<TestResult> results) {
		testPage.setTechnicalErrorNumber(getTotal(results, ResultKind.ERROR));
		testPage.setTestSuccessNumber(getTotal(results, ResultKind.SUCCESS));
		testPage.setTestFailureNumber(getTotal(results, ResultKind.FAILURE));
	}
	
	private int getTotal(List<TestResult> results, ResultKind resultKindFilter) {
		int count = 0;
		for (TestResult testResult : results) {
			if (resultKindFilter.equals(testResult.getResultKind())) {
				count++;
			}
		}
		return count;
	}
	
	public static String getPieChartJson(int success, int error, int failure){
		StringBuilder builder = new StringBuilder();
		builder.append("[ {");
		builder.append("\"value\" : \"" + success + "\",");
		builder.append("\"color\" : \"#5cb85c\",");
		builder.append("\"title\" : \"Success\"},");	
		builder.append("{");
		builder.append("\"value\" : \"" + failure + "\",");
		builder.append("\"color\" : \"#d9534f\",");
		builder.append("\"title\" : \"Errors\"}");	
		builder.append("]");	
		return builder.toString();
	}
}
