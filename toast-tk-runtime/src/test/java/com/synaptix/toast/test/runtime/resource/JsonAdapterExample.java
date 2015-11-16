package com.synaptix.toast.test.runtime.resource;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.TestResult;

@ActionAdapter(value = ActionAdapterKind.service, name ="json-service-adapter")
public class JsonAdapterExample {
	
	@Action(action="Intégrer {{com.synaptix.toast.test.runtime.resource.ProjetFlux:value:json}}", description="Intégrer json")
	public TestResult integrerJson(ProjetFlux fluxFromJson){
		return new TestResult();
	}
}
