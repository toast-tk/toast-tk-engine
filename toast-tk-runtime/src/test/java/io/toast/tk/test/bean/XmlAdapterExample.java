package io.toast.tk.test.bean;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;
import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;

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
