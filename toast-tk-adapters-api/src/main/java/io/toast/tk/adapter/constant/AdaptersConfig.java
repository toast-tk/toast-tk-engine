package io.toast.tk.adapter.constant;

import java.util.List;

public class AdaptersConfig {

    private final String webDriver;

    private final String webDriverPath;

    private final String browserPath;

    private String reportsFolderPath;

    private final boolean isSSl;

    /**
     * Report mail recipients
     */
    private List<String> mailTo;

    /**
     * Report mail sender
     */
    private String mailFrom;

    /**
     * Send mail report
     */
    private boolean mailSendReport;

    public AdaptersConfig(
            final String webDriver,
            final String webDriverPath,
            final String browserPath,
            final boolean isSSl,
            final String reportsFolderPath
    ) {
        this.webDriver = webDriver;
        this.webDriverPath = webDriverPath;
        this.browserPath = browserPath;
        this.isSSl = isSSl;
        this.reportsFolderPath = reportsFolderPath;
    }

    public void setReportsFolderPath(String reportsFolderPath) {
        this.reportsFolderPath = reportsFolderPath;
    }

    public boolean isSSl() {
        return isSSl;
    }

    public String getWebDriver() {
        return webDriver;
    }

    public String getWebDriverPath() {
        return webDriverPath;
    }

    public String getBrowserPath() {
        return browserPath;
    }

    public String getReportsFolderPath() {
        return reportsFolderPath;
    }

    public List<String> getMailTo() {
        return mailTo;
    }

    public void setMailTo(List<String> mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public boolean isMailSendReport() {
        return mailSendReport;
    }

    public void setMailSendReport(boolean mailSendReport) {
        this.mailSendReport = mailSendReport;
    }
}