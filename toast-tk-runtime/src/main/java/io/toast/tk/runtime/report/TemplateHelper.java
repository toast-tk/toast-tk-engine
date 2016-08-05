package io.toast.tk.runtime.report;

import org.joda.time.LocalDateTime;

import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.dao.domain.api.test.ITestResult.ResultKind;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;

public class TemplateHelper {

	public static String getBlockName(final IBlock block) {
		if(block instanceof ITestPage) {
			return ((ITestPage)block).getName();
		}
		return null;
	}

	public static LocalDateTime getStartTime(final IBlock block) {
		if(block instanceof ITestPage){
			final long startTime = ((ITestPage)block).getStartDateTime();
			return new LocalDateTime(startTime);
		}
		return null;
	}

	public static Long getExecutionTime(final IBlock block) {
		if(block instanceof ITestPage) {
			return ((ITestPage)block).getExecutionTime();
		}
		return 0L;
	}
	
	public static int getTechnicalErrorNumber(final IBlock block) {
		if(block instanceof ITestPage) {
			return ((ITestPage)block).getTechnicalErrorNumber();
		}
		return 0;
	}

	public static int getTestFailureNumber(final IBlock block) {
		if(block instanceof ITestPage) {
			return ((ITestPage)block).getTestFailureNumber();
		}
		return 0;
	}

	public static int getTestSuccessNumber(final IBlock block) {
		if(block instanceof ITestPage) {
			return ((ITestPage)block).getTestSuccessNumber();
		}
		return 0;
	}
	
	public static String getResultKindAsString(final ITestResult testResult) {
		if(testResult != null) {
			return getResultKindAsString(testResult.getResultKind());
		}
		return "";
	}

	public static String getResultKindAsString(final ResultKind resultKind) {
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

	public static String getResultScreenshotAsBase64(final ITestResult testResult) {
		return testResult.getScreenShot();
	}

	public static String formatStringToHtml(final TestLine line) {
		if(line.getTestResult() != null) {
			final String message = line.getTestResult().getMessage();
			return message != null ? message.replace("\n", "<br>") : "";
		}
		return "&nbsp;";
	}

	public static String getStepSentence(final TestLine line) {
		final String contextualTestSentence = line.getTestResult() != null ? line.getTestResult().getContextualTestSentence() : null;
		return contextualTestSentence == null ? line.getTest() : contextualTestSentence;
	}

	public static boolean hasScreenShot(final ITestResult testResult) {
		return testResult != null && testResult.getScreenShot() != null;
	}
	
	public static String getPieChartJson(
		final int success, 
		final int error, 
		final int failure
	) {
		final StringBuilder builder = new StringBuilder();
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