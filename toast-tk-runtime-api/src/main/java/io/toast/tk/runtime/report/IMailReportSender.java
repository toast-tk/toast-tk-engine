package io.toast.tk.runtime.report;

import java.util.List;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.runtime.utils.ResultObject;

public interface IMailReportSender {

    /**
     * Send a mail report, only if property "mail.send" is true.
     * Properties are in file toast.properties. Recipients are defined in property "mail.to".
     * Multiple recipients must be separated by commas. Sender is defined by property "mail.from".
     *
     * @param testPlan result
     */
    void sendMailReport(final ITestPlan testPlan);
    
    void sendMailReport(final List<ITestPage> testPage, final ResultObject res);

}