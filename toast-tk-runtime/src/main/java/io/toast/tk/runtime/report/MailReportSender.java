package io.toast.tk.runtime.report;

import io.toast.tk.adapter.constant.AdaptersConfig;
import io.toast.tk.adapter.constant.AdaptersConfigProvider;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Properties;

/**
 * Send test reports by email
 */
public class MailReportSender implements IMailReportSender {

	private static final Logger LOG = LogManager.getLogger(MailReportSender.class);

	private static final String SMTP_PROPERTIES_FILE_PATH = "/smtp.properties";

	@Override
	public void sendMailReport(ITestPlan testPage) {
		AdaptersConfig config = getConfig();

		for (final ICampaign campaign : testPage.getCampaigns()) {
			send(campaign, config.getMailTo(), config.getMailFrom(), getMailSession());
		}
	}

	private Session getMailSession() {
		InputStream resourceAsStream = this.getClass().getResourceAsStream(SMTP_PROPERTIES_FILE_PATH);

		final Properties properties = new Properties();
		try (final Reader resourceFileReader = new InputStreamReader(resourceAsStream)) {
			properties.load(resourceFileReader);
		} catch (final IOException e) {
			LOG.error("Could not load smtp.properties file", e);
		}

		if (Boolean.parseBoolean(properties.getProperty
				("mail.smtp.auth"))) {
			return Session.getDefaultInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					String user = properties.getProperty("mail.smtp.user");
					String password = properties.getProperty("mail.smtp.password");
					return new PasswordAuthentication(user, password);
				}
			});
		} else {
			return Session.getDefaultInstance(properties);
		}
	}

	private AdaptersConfig getConfig() {
		return new AdaptersConfigProvider().get();
	}

	private void send(ICampaign campaign, List<String> mailTo, String mailFrom, Session session) {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			InternetAddress address = new InternetAddress();
			for (String recipient : mailTo) {
				address.setAddress(recipient);
				message.addRecipient(Message.RecipientType.TO, address);
			}
			message.setSubject(getSubject());
			message.setText(getMailBody(campaign));

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error(e.getMessage(), e);
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

	private long getFailureNumber(ICampaign campaign) {
		return campaign.getTestCases().stream().filter(t -> !t.isSuccess()).count();
	}

	private long getSuccessNumber(ICampaign campaign) {
		return campaign.getTestCases().stream().filter(ITestPage::isSuccess).count();
	}

}
