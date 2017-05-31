package io.toast.tk.runtime.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;

import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.dao.domain.api.test.ITestResult.ResultKind;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;

public class TemplateHelper {

	private static int Html_Length_Max = 40;
	private static int Html_Length_Full = 100000;
	private static final Logger LOG = LogManager.getLogger(TemplateHelper.class);

	
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

	public static String getStartTimeFormat(final ITestPage testPage) {
		Long time = testPage.getStartDateTime();
		Date date = new Date(time);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return formatter.format(date);
	}

	public static Long getExecutionTime(final IBlock block) {
		if(block instanceof ITestPage) {
			return ((ITestPage)block).getExecutionTime();
		}
		return 0L;
	}
	
	public static String getExecutionTimeFormat(final ITestPage testPage) {
		Long time = testPage.getExecutionTime();
		time = time - TimeZone.getDefault().getOffset(time);
		Date date = new Date(time);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		return formatter.format(date);
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

	public static String getLineId(final TestLine line) {
		return line.getId();
	}

	public static String returnResult(String message) {
		return message.length() > Html_Length_Full ? substringText(message, Html_Length_Full) : message;
	}
	
	public static String formatSmallStringToHtml(final TestLine line) {
		return substringText(formatStringToHtml(line), Html_Length_Max);
	}
	
	public static String formatStringToHtml(final TestLine line) {
		if(line.getTestResult() != null) {
			String message = line.getTestResult().getMessage();
			message = message != null ? returnResult(message) : "";
			return prettyXmlText(message);
		}
		return "&nbsp;";
	}

	public static String getSmallExpectedResult(final TestLine line) {
		return substringText(getExpectedResult(line), Html_Length_Max);
	}
	
	public static String getExpectedResult(final TestLine line) {
		String message = line.getExpected(); 
		message = message != null ? returnResult(message) : "";
		return prettyXmlText(message);
	}
	
	public static String getSmallStepSentence(final TestLine line) {
		return substringText(getStepSentence(line), 2*Html_Length_Max);
	}
	
	public static String getStepSentence(final TestLine line) {
		String contextualTestSentence = line.getTestResult() != null ? line.getTestResult().getContextualTestSentence() : null;
		contextualTestSentence = contextualTestSentence != null ? contextualTestSentence : line.getTest();		
		return returnResult(contextualTestSentence);
	}

	public static String substringText(String text, int maxLength) {
		int textLength = text.length();
		if(textLength < maxLength) {
			return text;
		}
		else {
			return text.substring(0, maxLength - 3) + "...";
		}
	}
	
	private static String prettyXmlText(String str) {
		String[] lines = str.split(System.lineSeparator());
		List<String> res = new ArrayList<String>();
		int tabNb = 0;
		for(String line : lines) {
			String lineTemp = line.trim();
			if(lineTemp.startsWith("<([A-Z][A-Z0-9]*)\b[^>]*>")) {
				tabNb = tabNb + 1;
			} else if(lineTemp.startsWith("</([A-Z][A-Z0-9]*)\b[^>]*>")) {
				tabNb = tabNb - 1;
			}
			for(int i = 1; i < tabNb; i++) {
				lineTemp = "\t" + lineTemp;
			}
			res.add(lineTemp);
		}
		return String.join(System.lineSeparator(), res);
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