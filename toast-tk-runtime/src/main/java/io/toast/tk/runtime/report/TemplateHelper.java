package io.toast.tk.runtime.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.joda.time.LocalDateTime;

import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.dao.domain.api.test.ITestResult.ResultKind;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;

public class TemplateHelper {
	
	private static int HTML_LENGTH_MAX = 40;
	
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

	public static String formatSmallStringToHtml(final TestLine line) {
		return substringText(formatStringToHtml(line), HTML_LENGTH_MAX);
	}
	
	public static String formatStringToHtml(final TestLine line) {
		if(line.getTestResult() != null) {
			final String message = line.getTestResult().getMessage();
			return message != null ? prettyXmlText(message) : "";
		}
		return "&nbsp;";
	}

	public static String getSmallExpectedResult(final TestLine line) {
		return substringText(getExpectedResult(line), HTML_LENGTH_MAX);
	}
	
	public static String getExpectedResult(final TestLine line) {
		final String message = line.getExpected(); 
		return message != null ? prettyXmlText(message) : "";		
	}
	
	public static String getSmallStepSentence(final TestLine line) {
		return substringText(getStepSentence(line), 2*HTML_LENGTH_MAX);
	}
	
	public static String getStepSentence(final TestLine line) {
		final String contextualTestSentence = line.getTestResult() != null ? line.getTestResult().getContextualTestSentence() : null;
		return contextualTestSentence != null ? contextualTestSentence : line.getTest();		
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
		SAXBuilder builder = new SAXBuilder();
		
		//File fichierXML = new File("d:\\diagramme.xml");
		InputStream stream;
		try {
			stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
			Document document;
		
			/* Parsing of the file */
			//document = builder.build(fichierXML);
			document = builder.build(stream);
	
			Element rootNode = document.getRootElement();

			XMLOutputter output = new XMLOutputter();
			output.setFormat(Format.getPrettyFormat());
			String result = output.outputString(rootNode);
			return result;
		} catch (JDOMException | IOException e) {
			return str;
		}
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