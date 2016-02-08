package com.synaptix.toast.test.runtime.resource;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

@ActionAdapter(value = ActionAdapterKind.service, name ="json-service-adapter")
public class JsonAdapterExample {
	
	@Action(action="Intégrer {{com.synaptix.toast.test.runtime.resource.ProjetFlux:value:json}}", description="Intégrer json")
	public ITestResult integrerJson(ProjetFlux fluxFromJson){
		return new SuccessResult();
	}
}
