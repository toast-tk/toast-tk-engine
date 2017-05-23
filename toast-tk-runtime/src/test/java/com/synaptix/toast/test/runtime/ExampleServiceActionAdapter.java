package com.synaptix.toast.test.runtime;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;
import io.toast.tk.dao.core.report.FailureResult;
import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;


@ActionAdapter(value = ActionAdapterKind.service, name = "example-service")
public class ExampleServiceActionAdapter {
    @Action(id = "contains", action = "{{value}} contains {{value}}", description = "")
    public ITestResult checkContains(String value1, String value2) {
        if (value1.contains(value2)) {
            return new SuccessResult();
        } else {
            return new FailureResult(value1 + " does not contain " + value2);
        }
    }
}
