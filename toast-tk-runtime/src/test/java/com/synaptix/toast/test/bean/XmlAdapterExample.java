package com.synaptix.toast.test.bean;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.TestResult;

@ActionAdapter(value = ActionAdapterKind.service, name ="xml-service-adapter")
public class XmlAdapterExample {

	@Action(action="Intégrer {{com.synaptix.toast.test.runtime:value:xml}}", description="Intégrer xml")
	public TestResult integrerXml(ProjetFlux fluxFromXml){
		return new TestResult();
	}
	
	@Action(action="Intégrer {{com.synaptix.toast.test.runtime:value:json}}", description="Intégrer json")
	public TestResult integrerJson(ProjetFlux fluxFromJson){
		return new TestResult();
	}
}
