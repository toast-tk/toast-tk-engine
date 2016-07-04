package io.toast.tk.test.runtime.resource;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;
import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;

@ActionAdapter(value = ActionAdapterKind.service, name ="json-service-adapter")
public class JsonAdapterExample {
	
	@Action(action="Intégrer {{com.synaptix.toast.test.runtime.resource.ProjetFlux:value:json}}", description="Intégrer json")
	public ITestResult integrerJson(ProjetFlux fluxFromJson){
		return new SuccessResult();
	}
}
