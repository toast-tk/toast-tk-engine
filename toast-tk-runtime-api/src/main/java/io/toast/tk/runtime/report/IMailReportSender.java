package io.toast.tk.runtime.report;

import io.toast.tk.dao.domain.impl.test.block.ITestPlan;

@FunctionalInterface
public interface IMailReportSender {

    /**
     * Send a mail report, only if property "mail.send" is true.
     * Properties are in file toast.properties. Recipients are defined in property "mail.to".
     * Multiple recipients must be separated by commas. Sender is defined by property "mail.from".
     *
     * @param testPage test page result
     */
    void sendMailReport(final ITestPlan testPage);

}