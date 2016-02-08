package com.synaptix.toast.test.bean;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

@ActionAdapter(value = ActionAdapterKind.service, name ="xml-service-adapter")
public class XmlAdapterExample {

	@Action(
			id="integrate",
			action="Intégrer {{value:xml}}", 
			description="Intégrer xml")
	public ITestResult integrerXml(ProjetFlux fluxFromXml){
		return new SuccessResult();
	}
	
	@Action(
			id="integrate2",
			action="Intégrer {{value:json}}", 
			description="Intégrer json")
	public ITestResult integrerJson(ProjetFlux fluxFromJson){
		return new SuccessResult();
	}
}
