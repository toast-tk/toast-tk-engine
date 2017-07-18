package io.toast.tk.runtime.report;

import io.toast.tk.adapter.constant.AdaptersConfig;
import io.toast.tk.adapter.constant.AdaptersConfigProvider;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.runtime.mail.MailSender;
import io.toast.tk.runtime.utils.ResultObject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
 * Send test reports by email
 */
public class MailReportSender implements IMailReportSender {
	
	private static final Logger LOG = LogManager.getLogger(MailReportSender.class);
	
	@Override
	public void sendMailReport(ITestPlan testPage) {
		AdaptersConfig config = getConfig();

		for (final ICampaign campaign : testPage.getCampaigns()) {
			send(campaign, config.getMailTo(), config.getMailFrom());
		}
	}

	@Override
	public void sendMailReport(List<ITestPage> testPage, ResultObject res) {
		AdaptersConfig config = getConfig();

		send(testPage, res, config.getMailTo(), config.getMailFrom());		
	}

	private AdaptersConfig getConfig() {
		return new AdaptersConfigProvider().get();
	}

	private void send(ICampaign campaign, List<String> mailTo, String mailFrom) {		
		String body = getSubject();
		String subject = getMailBody(campaign);
		
		MailSender sender = new MailSender();
		try {
			sender.send(subject, body, mailTo, mailFrom);
		}
		catch( Exception e) {
			LOG.error(e.getMessage());
		}
	}

	private void send(List<ITestPage> testPage, ResultObject res, List<String> mailTo, String mailFrom) {		
		String body = getSubject();
		String subject = getMailBody(testPage, res);
		
		MailSender sender = new MailSender();
		try {
			sender.send(subject, body, mailTo, mailFrom);
		}
		catch( Exception e) {
			LOG.error(e.getMessage());
		}
	}

	private String getSubject() {
		return "Test report " + getDateAsString(LocalDateTime.now());
	}

	private String getDateAsString(LocalDateTime localDateTime) {
		return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}

	private String getMailBody(ICampaign campaign) {
		StrBuilder sb = new StrBuilder();

		sb.append("Test report for the test campaign: ").append(campaign.getName());
		sb.appendNewLine();

		// First test start time
		if (CollectionUtils.isNotEmpty(campaign.getTestCases())) {
			long startDateMillis = campaign.getTestCases().get(0).getStartDateTime();
			LocalDateTime startDateTime =
					LocalDateTime.ofInstant(Instant.ofEpochMilli(startDateMillis), ZoneId.systemDefault());
			sb.append("Start time: ").append(getDateAsString(startDateTime)).appendNewLine();
		}
		sb.appendNewLine();

		// Success number
		sb.append(getSuccessNumber(campaign)).append(" tests in success").appendNewLine();

		// Failure number
		long failureNumber = getFailureNumber(campaign);
		sb.append(failureNumber).append(" tests failed").appendNewLine();
		sb.appendNewLine();

		if (failureNumber > 0) {
			sb.append("Failed test cases:").appendNewLine();

			for (final ITestPage testPage : campaign.getTestCases()) {
				if (!testPage.isSuccess()) {
					sb.append("- ").append(testPage.getName()).appendNewLine();
				}
			}
		}
		return sb.toString();
	}


	private String getMailBody(List<ITestPage> testPages, ResultObject res) {
		StrBuilder sb = new StrBuilder();

		sb.append("Test report for ").append(testPages.size()).append(" the test pages.");
		sb.appendNewLine();

		// First test start time
		if (CollectionUtils.isNotEmpty(testPages)) {
			long startDateMillis = testPages.get(0).getStartDateTime();
			LocalDateTime startDateTime =
					LocalDateTime.ofInstant(Instant.ofEpochMilli(startDateMillis), ZoneId.systemDefault());
			sb.append("Start time: ").append(getDateAsString(startDateTime)).appendNewLine();
		}
		sb.appendNewLine();

		// Success number
		sb.append(res.getTotalSuccess()).append(" tests in success").appendNewLine();

		// Failure number
		long failureNumber = res.getTotalErrors() + res.getTotalTechnical();
		sb.append(failureNumber).append(" tests failed").appendNewLine();
		sb.appendNewLine();

		if (failureNumber > 0) {
			sb.append("Failed test cases:").appendNewLine();

			for (final ITestPage testPage : testPages) {
				if (!testPage.isSuccess()) {
					sb.append("- ").append(testPage.getName()).appendNewLine();
				}
			}
		}
		return sb.toString();
	}
	
	private long getFailureNumber(ICampaign campaign) {
		return campaign.getTestCases().stream().filter(t -> !t.isSuccess()).count();
	}

	private long getSuccessNumber(ICampaign campaign) {
		return campaign.getTestCases().stream().filter(ITestPage::isSuccess).count();
	}

}
