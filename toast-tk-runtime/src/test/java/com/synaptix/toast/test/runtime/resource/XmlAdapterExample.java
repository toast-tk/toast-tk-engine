package com.synaptix.toast.test.runtime.resource;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.TestResult;

@ActionAdapter(value = ActionAdapterKind.service, name ="xml-service-adapter")
public class XmlAdapterExample {

	@Action(action="Intégrer {{com.synaptix.toast.test.runtime.resource.ProjetFlux:value:xml}}", description="Intégrer xml")
	public TestResult integrerXml(ProjetFlux fluxFromXml){
		return new TestResult();
	}
	
	@Action(
			id="integrate2",
			action="Integrate {{value:xml}}", 
			description="Intégrer xml avec type non defini")
	public TestResult integrateNameLessXml(ProjetFlux fluxFromXml){
		return new TestResult();
	}
	
	@Action(
			id="integrate3",
			action="Integrate {{value:xml}} as {{value}}", 
			description="Intégrer xml avec type non defini")
	public TestResult integrateXmlAsVar(ProjetFlux fluxFromXml, String variable){
		return new TestResult();
	}
	
	@Action(
			id="compare1",
			action="{{value}} == {{value}}", 
			description="comparer une valeur à une autre")
	public TestResult integrateXmlAsVar(String var1, String var2){
		return new TestResult();
	}
}
