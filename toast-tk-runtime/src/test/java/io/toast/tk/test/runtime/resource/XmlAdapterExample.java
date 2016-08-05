package io.toast.tk.test.runtime.resource;

import org.junit.Assert;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;
import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;

@ActionAdapter(value = ActionAdapterKind.service, name = "xml-service-adapter")
public class XmlAdapterExample {

	@Action(action = "Intégrer {{io.toast.tk.test.runtime.resource.ProjetFlux:value:xml}}", description =
			"Intégrer xml")
	public ITestResult integrerXml(ProjetFlux fluxFromXml) {
		return new SuccessResult();
	}
	
	@Action(id="integrate2",
			action="Integrate {{value:xml}}", 
			description="Intégrer xml avec type non defini")
	public ITestResult integrateNameLessXml(ProjetFlux fluxFromXml){
		return new SuccessResult();
	}
	
	@Action(id="integrate3",
			action="Integrate {{value:xml}} as {{value}}", 
			description="Intégrer xml avec type non defini")
	public ITestResult integrateXmlAsVar(ProjetFlux fluxFromXml, String variable){
		return new SuccessResult();
	}

	@Action(
			id = "compare1",
			action = "{{value}} == {{value}}",
			description = "comparer une valeur à une autre")
	public Boolean compare(String var1, String var2) {
		return var1.equals(var2);
	}

	@Action(id = "echo", action = "echo {{value}}", description = "afficher valeur autre")
	public String returnString(String var1) {
		return var1;
	}

	@Action(
			id = "assertValue",
			action = "assert {{value}}",
			description = "faire une assertion")
	public void assertTest(String var1) {
		Assert.assertNotNull(var1);
	}
	
	@Action(
			id = "errorValue",
			action = "assert not {{value}}",
			description = "faire une assertion")
	public void assertErrorTest(String var1) {
		Assert.assertNull(var1);
	}

	@Action(
			id = "sayHello",
			action = "Say hello",
			description = "dire bonjour")
	public String sayHello() {
		return "Hello";
	}
}
