package com.synaptix.toast.adapter.swing;

import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.AddValueInVar;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.ClickOn;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.ClickOnIn;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.DiviserVarByValue;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.GetComponentValue;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.MultiplyVarByValue;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.RemplacerVarParValue;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.SelectContectualMenu;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.SelectMenuPath;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.SelectSubMenu;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.SelectTableRow;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.SelectValueInList;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.StoreComponentValueInVar;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.SubstractValueFromVar;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.TypeValue;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.TypeValueInInput;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.TypeVarIn;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.VALUE_REGEX;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.Wait;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.synaptix.toast.adapter.swing.component.DefaultSwingPage;
import com.synaptix.toast.adapter.swing.component.SwingDateElement;
import com.synaptix.toast.adapter.swing.component.SwingInputElement;
import com.synaptix.toast.adapter.swing.component.SwingListElement;
import com.synaptix.toast.adapter.swing.component.SwingTableElement;
import com.synaptix.toast.adapter.swing.utils.SwingAutoUtils;
import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.adapter.web.HasStringValue;
import com.synaptix.toast.adapter.web.HasSubItems;
import com.synaptix.toast.adapter.constant.Property;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.adapter.AutoSwingType;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequestQueryCriteria;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.runtime.IActionItemRepository;

@ActionAdapter(value = ActionAdapterKind.swing, name = "")
public abstract class AbstractSwingActionAdapter {

	protected IActionItemRepository repo;

	protected IRemoteSwingAgentDriver driver;

	public AbstractSwingActionAdapter(
		IActionItemRepository repo,
		IRemoteSwingAgentDriver driver) {
		this.repo = repo;
		this.driver = driver;
		try {
			for(IFeedableSwingPage page : repo.getSwingPages()) {
				((DefaultSwingPage) page).setDriver(driver);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected SwingAutoElement getPageField(
		String pageName,
		String fieldName)
		throws IllegalAccessException {
		if(repo.getSwingPage(pageName) == null) {
			throw new IllegalAccessException(pageName + " swing page not found in repository !");
		}
		DefaultSwingPage page = (DefaultSwingPage) repo.getSwingPage(pageName);
		SwingAutoElement autoElement = page.getAutoElement(fieldName);
		if(autoElement == null) {
			throw new IllegalAccessException(pageName + "." + fieldName + " not found in repository !");
		}
		return autoElement;
	}

	@Action(action = TypeValue, description = "Saisir une chaine de caractère au clavier")
	public TestResult typeValue(
		String text)
		throws Exception {
		driver.process(new CommandRequest.CommandRequestBuilder(null).with(null).ofType(null).sendKeys(text).build());
		return new TestResult();
	}

	@Action(action = TypeValueInInput, description = "Saisir une valeur dans un composant graphique")
	public TestResult typeIn(
		String text,
		String pageName,
		String widgetName)
		throws Exception {
		SwingAutoElement pageField = getPageField(pageName, widgetName);
		if(pageField instanceof SwingInputElement) {
			SwingInputElement input = (SwingInputElement) pageField;
			input.setInput(text);
		}
		else if(pageField instanceof SwingDateElement) {
			SwingDateElement input = (SwingDateElement) pageField;
			input.setDateText(text);
		}
		else {
			throw new IllegalAccessException(String.format(
				"%s.%s is not handled to type values in !",
				pageName,
				pageField));
		}
		return new TestResult();
	}

	@Action(action = ClickOnIn, description = "Cliquer sur un composant présent dans un contenant de composant")
	public TestResult clickOnIn(
		String pageName,
		String widgetName,
		String parentPage,
		String parentWidgetName)
		throws Exception {
		HasSubItems input = (HasSubItems) getPageField(parentPage, parentWidgetName);
		SwingAutoElement subElement = (SwingAutoElement) getPageField(pageName, widgetName);
		input.clickOn(subElement.getWrappedElement().getLocator());
		return new TestResult();
	}

	@Action(action = ClickOn, description = "Cliquer sur un composant graphique")
	public TestResult clickOn(
		String pageName,
		String widgetName)
		throws Exception {
		HasClickAction input = (HasClickAction) getPageField(pageName, widgetName);
		boolean click = input.click();
		return new TestResult(String.valueOf(click), click ? ResultKind.SUCCESS : ResultKind.ERROR);
	}

	@Action(action = "(\\w+).(\\w+) exists", description = "Verifier qu'un composant graphique existe")
	public TestResult exists(
		String pageName,
		String widgetName)
		throws Exception {
		SwingAutoElement input = getPageField(pageName, widgetName);
		if(input.exists()) {
			return new TestResult("true", ResultKind.SUCCESS);
		}
		else {
			return new TestResult("false", ResultKind.ERROR);
		}
	}

	@Action(action = "Count (\\w+).(\\w+) results", description = "Compter le nombre de ligne dans un tableau")
	public TestResult count(
		String pageName,
		String widgetName)
		throws Exception {
		SwingTableElement table = (SwingTableElement) getPageField(pageName, widgetName);
		return new TestResult(table.count());
	}

	@Action(action = TypeVarIn, description = "Saisir la valeur d'une variable dans un champs graphique de type input")
	public TestResult typeVarIn(
		String variable,
		String pageName,
		String widgetName)
		throws Exception {
		typeIn(variable, pageName, widgetName);
		return new TestResult();
	}

	@Action(action = GetComponentValue, description = "Lire la valeur d'un composant graphique")
	public TestResult getComponentValue(
		String pageName,
		String widgetName)
		throws Exception {
		SwingAutoElement pageField = getPageField(pageName, widgetName);
		if(!(pageField instanceof HasStringValue)) {
			throw new IllegalAccessException(pageName + "." + widgetName + " isn't supporting value fetching !");
		}
		HasStringValue stringValueProvider = (HasStringValue) pageField;
		String value = stringValueProvider.getValue();
		return new TestResult(value, ResultKind.SUCCESS);
	}

	@Action(
		action = StoreComponentValueInVar,
		description = "Lire la valeur d'un composant graphique et la stocker dans une variable")
	public TestResult selectComponentValue(
		String pageName,
		String widgetName,
		String variable)
		throws Exception {
		SwingAutoElement pageField = getPageField(pageName, widgetName);
		if(!(pageField instanceof HasStringValue)) {
			throw new IllegalAccessException(pageName + "." + widgetName + " isn't supporting value fetching !");
		}
		HasStringValue stringValueProvider = (HasStringValue) pageField;
		String value = stringValueProvider.getValue();
		repo.getUserVariables().put(variable, value);
		return new TestResult(value, ResultKind.SUCCESS);
	}

	@Action(action = Wait, description = "Attendre n secondes avant la prochaine action")
	public TestResult wait(
		String time)
		throws Exception {
		Thread.sleep(Integer.valueOf(time) * 1000);
		return new TestResult();
	}

	@Action(action = SelectMenuPath, description = "Selectionner un menu, avec / comme séparateur")
	public TestResult selectPath(
		String menu)
		throws Exception {
		String[] locator = menu.split(" / ");
		SwingAutoUtils.confirmExist(driver, locator[0], AutoSwingType.menu.name());
		CommandRequest request = new CommandRequest.CommandRequestBuilder(UUID.randomUUID().toString())
			.with(locator[0])
			.ofType(AutoSwingType.menu.name()).select(locator[1]).build();
		String waitForValue = driver.processAndWaitForValue(request);
		return ResultKind.FAILURE.name().equals(waitForValue) ? new TestResult("Menu {" + menu + "} not found !",
			ResultKind.FAILURE)
			: new TestResult("", ResultKind.SUCCESS);
	}

	@Action(action = SelectSubMenu, description = "Selectionner un sous menu")
	public TestResult select(
		String pageName,
		String widgetName,
		String parentPage,
		String parentWidgetName)
		throws Exception {
		return clickOnIn(pageName, widgetName, parentPage, parentWidgetName);
	}

	@Action(action = SelectValueInList, description = "Selectionner une valeur dans une liste")
	public TestResult selectIn(
		String value,
		String pageName,
		String widgetName)
		throws Exception {
		SwingListElement list = (SwingListElement) getPageField(pageName, widgetName);
		list.select(value);
		return new TestResult();
	}

	@Action(action = SelectTableRow, description = "Selectionner une ligne de tableau avec critères")
	public ITestResult selectMission(
		String pageName,
		String widgetName,
		String tableColumnFinder)
		throws Exception {
		SwingTableElement table = (SwingTableElement) getPageField(pageName, widgetName);
		List<TableCommandRequestQueryCriteria> tableCriteria = new ArrayList<TableCommandRequestQueryCriteria>();
		String[] criteria = tableColumnFinder.split(Property.TABLE_CRITERIA_SEPARATOR);
		if(criteria.length > 0) {
			for(String criterion : criteria) {
				TableCommandRequestQueryCriteria tableCriterion = buildTableCriterion(criterion);
				tableCriteria.add(tableCriterion);
			}
		}
		else {
			TableCommandRequestQueryCriteria tableCriterion = buildTableCriterion(tableColumnFinder);
			tableCriteria.add(tableCriterion);
		}
		String outputVal = table.find(tableCriteria);
		return new TestResult(outputVal, ResultKind.SUCCESS);
	}

	private TableCommandRequestQueryCriteria buildTableCriterion(
		String criterion) {
		String col = criterion.split(Property.TABLE_KEY_VALUE_SEPARATOR)[0];
		String val = criterion.split(Property.TABLE_KEY_VALUE_SEPARATOR)[1];
		TableCommandRequestQueryCriteria tableCriterion = new TableCommandRequestQueryCriteria(col, val);
		return tableCriterion;
	}

	@Action(action = SelectContectualMenu, description = "selectionner un menu dans une popup contextuelle")
	public TestResult selectCtxMenu(
		String menu)
		throws Exception {
		driver.process(new CommandRequest.CommandRequestBuilder(null).with(menu).ofType(AutoSwingType.menu.name())
			.select(menu).build());
		return new TestResult();
	}

	@Action(action = "Affichage dialogue \'([\\w\\W]+)\'", description = "Afichage d'une dialogue")
	public TestResult waitForDialogDisplay(
		String dialogName)
		throws Exception {
		CommandRequest request = new CommandRequest.CommandRequestBuilder(UUID.randomUUID().toString())
			.ofType(AutoSwingType.dialog.name())
			.with(dialogName).exists().build();
		driver.process(request);
		boolean waitForExist = driver.waitForExist(request.getId());
		return waitForExist ? new TestResult("", ResultKind.SUCCESS) : new TestResult("Dialogue " + dialogName
			+ " pas disponible !",
			ResultKind.ERROR);
	}

	// ///////////////////////////////////////
	// TO MOVE IN A DRIVER AGNOSTIC FIXUTRE
	// ////////////////////////////////////////
	@Action(action = AddValueInVar, description = "Additionner deux valeurs numériques")
	public TestResult addValueToVar(
		String value,
		String var)
		throws Exception {
		Object object = repo.getUserVariables().get(var);
		if(object == null) {
			throw new IllegalAccessException("Variable not defined !");
		}
		if(object instanceof String) { // for the time being we store only
										// strings !!
			Double v = Double.valueOf(value);
			Double d = Double.valueOf((String) object);
			d = d + v;
			repo.getUserVariables().put(var, d.toString());
			return new TestResult(d.toString(), ResultKind.SUCCESS);
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> "
				+ object.getClass().getSimpleName());
		}
	}

	@Action(action = SubstractValueFromVar, description = "Soustraire deux valeurs numériques")
	public TestResult substractValueToVar(
		String value,
		String var)
		throws Exception {
		Object object = repo.getUserVariables().get(var);
		if(object == null) {
			throw new IllegalAccessException("Variable not defined !");
		}
		if(object instanceof String) { // for the time being we store only
										// strings !!
			Double v = Double.valueOf(value);
			Double d = Double.valueOf((String) object);
			d = d - v;
			repo.getUserVariables().put(var, d.toString());
			return new TestResult(d.toString(), ResultKind.SUCCESS);
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> "
				+ object.getClass().getSimpleName());
		}
	}

	@Action(action = MultiplyVarByValue, description = "Multiplier deux valeurs")
	public TestResult multiplyVarByBal(
		String var,
		String value)
		throws Exception {
		if(var == null) {
			throw new IllegalAccessException("Variable not defined !");
		}
		if(var instanceof String) { // for the time being we store only
									// strings !!
			Double v = Double.valueOf(value);
			Double d = Double.valueOf((String) var);
			d = d * v;
			return new TestResult(d.toString(), ResultKind.SUCCESS);
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> " + var);
		}
	}

	@Action(action = DiviserVarByValue, description = "Diviseur le premier argument par le deuxième")
	public TestResult divideVarByValue(
		String var,
		String value)
		throws Exception {
		if(var == null) {
			throw new IllegalAccessException("Variable not defined !");
		}
		if(var instanceof String) { // for the time being we store only
									// strings !!
			Double v = Double.valueOf(value);
			Double d = Double.valueOf(var);
			d = d / v;
			return new TestResult(d.toString(), ResultKind.SUCCESS);
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> " + var);
		}
	}

	@Action(action = RemplacerVarParValue, description = "Assigner une valeur à une variable")
	public TestResult replaceVarByVal(
		String var,
		String value)
		throws Exception {
		repo.getUserVariables().put(var, value);
		return new TestResult();
	}

	@Action(
		action = "Ajuster date (\\w+).(\\w+) à plus (\\w+) jours",
		description = "Rajouter n Jours à au composant graphique de date")
	public TestResult setDate(
		String pageName,
		String widgetName,
		String days)
		throws Exception {
		SwingDateElement input = (SwingDateElement) getPageField(pageName, widgetName);
		input.setInput(days);
		return new TestResult();
	}

	@Action(action = VALUE_REGEX + " == " + VALUE_REGEX, description = "Comparer deux variables")
	public TestResult VarEqVar(
		String var1,
		String var2)
		throws Exception {
		if(var1.equals(var2)) {
			return new TestResult(Boolean.TRUE.toString(), ResultKind.SUCCESS);
		}
		else {
			return new TestResult(String.format("%s == %s => %s", var1, var2, Boolean.FALSE.toString()),
				ResultKind.FAILURE);
		}
	}

	@Action(
		action = VALUE_REGEX + " égale à " + VALUE_REGEX,
		description = "Comparer une valeur à une variable")
	public TestResult ValueEqVar(
		String value,
		String var)
		throws Exception {
		if(value.equals(var)) {
			return new TestResult(Boolean.TRUE.toString(), ResultKind.SUCCESS);
		}
		else {
			return new TestResult(String.format("%s == %s => %s", value, var, Boolean.FALSE.toString()),
				ResultKind.FAILURE);
		}
	}

	@Action(action = "Assigner " + VALUE_REGEX + " à " + VALUE_REGEX, description = "Assigner valeur à variable")
	public TestResult setValToVar(
		String value,
		String var)
		throws Exception {
		repo.getUserVariables().put(var, value);
		return new TestResult();
	}

	@Action(action = "Clear {{input:component:swing}}", description = "Effacer le contenu d'un composant input graphique")
	public TestResult clear(
		String pageName,
		String widgetName)
		throws Exception {
		SwingInputElement input = (SwingInputElement) getPageField(pageName, widgetName);
		input.clear();
		return new TestResult();
	}
}
