package io.toast.tk.runtime.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;

import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.dao.domain.api.test.ITestResult.ResultKind;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;

public class TemplateHelper {

	private static int Html_Length_Max = 40;
	private static int Html_Length_Full = 100000;
	//private static final Logger LOG = LogManager.getLogger(TemplateHelper.class);

	
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
			return prettyText(message);
		}
		return "&nbsp;";
	}

	public static String getSmallExpectedResult(final TestLine line) {
		return substringText(getExpectedResult(line), Html_Length_Max);
	}
	
	public static String getExpectedResult(final TestLine line) {
		String message = line.getExpected(); 
		message = message != null ? returnResult(message) : "";
		return prettyText(message);
	}
	
	public static String getSmallStepSentence(final TestLine line) {
		return substringText(getStepSentence(line), 2*Html_Length_Max);
	}
	
	public static String getStepSentence(final TestLine line) {
		String contextualTestSentence = line.getTestResult() != null ? line.getTestResult().getContextualTestSentence() : null;
		contextualTestSentence = contextualTestSentence != null ? contextualTestSentence : line.getTest();		
		String message = returnResult(contextualTestSentence);
		return prettyText(message);
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

	private static String prettyText(String str) {
		String separator = "\\*";
		String[] lines = str.split(separator);
		List<String> res = new ArrayList<String>();
		for(String line : lines) {
			if(line.endsWith(">") && line.startsWith("<")) {
				res.add(System.lineSeparator());
				res.add(prettyXmlText(line));
				res.add(System.lineSeparator());
			} else {
				res.add(line);
			}
		}
		return String.join("*", res);
	}
	private static String prettyXmlText(String str) {
		String separator = System.lineSeparator();
		String[] lines = str.split(separator);
		List<String> res = new ArrayList<String>();
		int tabNb = 0;
		for(String line : lines) {
			boolean isInside = true;
			boolean lastIsInside = true;
			boolean asToClose = false;
			if(line.contains("<") && line.contains(">")) {
				String lineTemp = line.trim()
						.replace("\t", "")
						.replace("\n", "")
						.replace("\r", "");
				int index = lineTemp.indexOf("<");
				String firstPart = lineTemp.substring(0, index).trim();
				if(!firstPart.equals("")) {
					res.add(firstPart);
				}
				
				String secondPart = lineTemp.substring(index);
				for(String lineSplit : secondPart.split("<|>")) {
					String lineResult = "";
					lineSplit = lineSplit.trim();
					if(lineSplit.equals("")) {
						isInside = true;
						lastIsInside = isInside;
						continue;
					} else {
						isInside = lastIsInside ? false : true;
					}
					if(lineSplit.startsWith("/")) {
						tabNb += -1;
					}
					for(int i = 1; i <= tabNb; i++) {
						lineResult = "\t" + lineResult;
					}
					if(!lineSplit.startsWith("/") & !lineSplit.endsWith("/") 
							& !lineSplit.startsWith("?") & !lineSplit.endsWith("?")
							& lastIsInside) {
						tabNb += +1;
					}
					lineResult += lastIsInside ? "<" + lineSplit + ">" : lineSplit;
					if(!lastIsInside || isInside || asToClose){
						String lineResultTemp = res.get(res.size()-1) + lineResult.trim();
						res.set(res.size()-1, lineResultTemp);
						asToClose = asToClose ? false : true;
					} else {
						res.add(lineResult);
					}
					lastIsInside = isInside;
				} 
			} else {
				if(!line.equals("")) {
					res.add(line);
				}
			}
			
		}
		return String.join(separator, res);
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