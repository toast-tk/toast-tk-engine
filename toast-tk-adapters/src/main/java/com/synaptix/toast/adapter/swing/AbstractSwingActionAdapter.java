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
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.SWING_COMPONENT_REGEX;

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
import com.synaptix.toast.adapter.web.HasValueBase;
import com.synaptix.toast.adapter.constant.Property;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.adapter.AutoSwingType;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequestQueryCriteria;
import com.synaptix.toast.core.report.ErrorResult;
import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult.ResultKind;
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


	@Action(action = TypeValue, description = "Saisir une chaine de caractère au clavier")
	public ITestResult typeValue(
		String text)
		throws Exception {
		driver.process(new CommandRequest.CommandRequestBuilder(null).with(null).ofType(null).sendKeys(text).build());
		return new SuccessResult();
	}

	@Action(action = TypeValueInInput, description = "Saisir une valeur dans un composant graphique")
	public ITestResult typeIn(
		String text,SwingAutoElement pageField)
		throws Exception {
		if(pageField instanceof SwingInputElement) {
			SwingInputElement input = (SwingInputElement) pageField;
			return input.setInput(text);
		}
		else if(pageField instanceof SwingDateElement) {
			SwingDateElement input = (SwingDateElement) pageField;
			return input.setDateText(text);
		}
		else {
			throw new IllegalAccessException(String.format(
				"%s is not handled to type values in !",
				pageField));
		}
	}

	@Action(action = ClickOnIn, description = "Cliquer sur un composant présent dans un contenant de composant")
	public ITestResult clickOnIn(SwingAutoElement elementField,SwingAutoElement containerField)
		throws Exception {
		HasSubItems input = (HasSubItems) containerField;
		SwingAutoElement subElement = (SwingAutoElement) elementField;
		input.clickOn(subElement.getWrappedElement().getLocator());
		return new SuccessResult();
	}

	@Action(action = ClickOn, description = "Cliquer sur un composant graphique")
	public ITestResult clickOn(SwingAutoElement pageField)
		throws Exception {
		HasClickAction input = (HasClickAction) pageField;
		ITestResult clickResult = input.click();
		return clickResult;
	}

	@Action(action = SWING_COMPONENT_REGEX + " exists", description = "Verifier qu'un composant graphique existe")
	public ITestResult exists(SwingAutoElement pageField)
		throws Exception {
		SwingAutoElement input = pageField;
		if(input.exists()) {
			return new SuccessResult("true");
		}
		else {
			return new ErrorResult("false");
		}
	}

	@Action(action = "Count " + SWING_COMPONENT_REGEX + " results", description = "Compter le nombre de ligne dans un tableau")
	public ITestResult count(SwingAutoElement pageField)
		throws Exception {
		SwingTableElement table = (SwingTableElement)pageField;
		ITestResult countResult = table.count();
		return new SuccessResult(countResult.getMessage(),countResult.getScreenShot());
	}

	@Action(action = TypeVarIn, description = "Saisir la valeur d'une variable dans un champs graphique de type input")
	public ITestResult typeVarIn(
		String variable, SwingAutoElement pageField
		)
		throws Exception {
		typeIn(variable, pageField);
		return new SuccessResult();
	}

	@Action(action = GetComponentValue, description = "Lire la valeur d'un composant graphique")
	public ITestResult getComponentValue(SwingAutoElement pageField)
		throws Exception {
		if(!(pageField instanceof HasValueBase<?>)) {
			throw new IllegalAccessException("Field isn't supporting value fetching !");
		}
		HasValueBase<?> result = (HasValueBase<?>) pageField;
		if(result.getValue() instanceof ITestResult){
			ITestResult value = (ITestResult)result.getValue();
			return value;
		}else{
			String value = result.getValue().toString();
			return new SuccessResult(value);
		}
	}

	@Action(action = StoreComponentValueInVar,
		description = "Lire la valeur d'un composant graphique et la stocker dans une variable")
	public ITestResult selectComponentValue(SwingAutoElement pageField,String variable)
		throws Exception {
		if(!(pageField instanceof HasValueBase)) {
			throw new IllegalAccessException("Field isn't supporting value fetching !");
		}
		HasValueBase<ITestResult> stringValueProvider = (HasValueBase<ITestResult>) pageField;
		ITestResult result = stringValueProvider.getValue();
		repo.getUserVariables().put(variable, result.getMessage());
		return result;
	}

	@Action(action = Wait, description = "Attendre n secondes avant la prochaine action")
	public ITestResult wait(
		String time)
		throws Exception {
		Thread.sleep(Integer.valueOf(time) * 1000);
		return new SuccessResult();
	}

	@Action(action = SelectMenuPath, description = "Selectionner un menu, avec / comme séparateur")
	public ITestResult selectPath(
		String menu)
		throws Exception {
		String[] locator = menu.split(" / ");
		SwingAutoUtils.confirmExist(driver, locator[0], AutoSwingType.menu.name());
		CommandRequest request = new CommandRequest.CommandRequestBuilder(UUID.randomUUID().toString())
			.with(locator[0])
			.ofType(AutoSwingType.menu.name()).select(locator[1]).build();
		ITestResult waitForValue = driver.processAndWaitForValue(request);
		return waitForValue.isError() ? new ErrorResult("Menu {" + menu + "} not found !"): new SuccessResult();
	}

	@Action(action = SelectSubMenu, description = "Selectionner un sous menu")
	public ITestResult select(SwingAutoElement elementField, SwingAutoElement containerField)
		throws Exception {
		return clickOnIn(elementField, containerField);
	}

	@Action(action = SelectValueInList, description = "Selectionner une valeur dans une liste")
	public ITestResult selectIn(
		String value,
		SwingAutoElement pageField)
		throws Exception {
		SwingListElement list = (SwingListElement) pageField;
		list.select(value);
		return new SuccessResult();
	}

	@Action(action = SelectTableRow, description = "Selectionner une ligne de tableau avec critères")
	public ITestResult selectMission(
		SwingAutoElement pageField,
		String tableColumnFinder)
		throws Exception {
		SwingTableElement table = (SwingTableElement) pageField;
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
		ITestResult searchResult = table.find(tableCriteria);
		return searchResult;
	}

	private TableCommandRequestQueryCriteria buildTableCriterion(
		String criterion) {
		String col = criterion.split(Property.TABLE_KEY_VALUE_SEPARATOR)[0];
		String val = criterion.split(Property.TABLE_KEY_VALUE_SEPARATOR)[1];
		TableCommandRequestQueryCriteria tableCriterion = new TableCommandRequestQueryCriteria(col, val);
		return tableCriterion;
	}

	@Action(action = SelectContectualMenu, description = "selectionner un menu dans une popup contextuelle")
	public ITestResult selectCtxMenu(
		String menu)
		throws Exception {
		String uid = UUID.randomUUID().toString();
		CommandRequest commandRequest = new CommandRequest.CommandRequestBuilder(uid).with(menu).ofType(AutoSwingType.menu.name())
			.select(menu).build();
		ITestResult result = driver.processAndWaitForValue(commandRequest);
		return result;
	}

	@Action(action = "Affichage dialogue {{value:string}}", description = "Afichage d'une dialogue")
	public ITestResult waitForDialogDisplay(
		String dialogName)
		throws Exception {
		CommandRequest request = new CommandRequest.CommandRequestBuilder(UUID.randomUUID().toString())
			.ofType(AutoSwingType.dialog.name())
			.with(dialogName).exists().build();
		driver.process(request);
		boolean waitForExist = driver.waitForExist(request.getId());
		return waitForExist ? new SuccessResult() : new ErrorResult("Dialogue " + dialogName+ " pas disponible !");
	}

	// ///////////////////////////////////////
	// TO MOVE IN A DRIVER AGNOSTIC FIXUTRE
	// ////////////////////////////////////////
	@Action(action = AddValueInVar, description = "Additionner deux valeurs numériques")
	public ITestResult addValueToVar(
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
			return new SuccessResult(d.toString());
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> "
				+ object.getClass().getSimpleName());
		}
	}

	@Action(action = SubstractValueFromVar, description = "Soustraire deux valeurs numériques")
	public ITestResult substractValueToVar(
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
			return new SuccessResult(d.toString());
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> "
				+ object.getClass().getSimpleName());
		}
	}

	@Action(action = MultiplyVarByValue, description = "Multiplier deux valeurs")
	public ITestResult multiplyVarByBal(
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
			return new SuccessResult(d.toString());
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> " + var);
		}
	}

	@Action(action = DiviserVarByValue, description = "Diviseur le premier argument par le deuxième")
	public ITestResult divideVarByValue(
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
			return new SuccessResult(d.toString());
		}
		else {
			throw new IllegalAccessException("Variable not in a proper format: current -> " + var);
		}
	}

	@Action(action = RemplacerVarParValue, description = "Assigner une valeur à une variable")
	public ITestResult replaceVarByVal(
		String var,
		String value)
		throws Exception {
		repo.getUserVariables().put(var, value);
		return new SuccessResult();
	}

	@Action(
		action = "Ajuster date (\\w+).(\\w+) à plus (\\w+) jours",
		description = "Rajouter n Jours à au composant graphique de date")
	public ITestResult setDate(SwingAutoElement pageField,String days)
		throws Exception {
		SwingDateElement input = (SwingDateElement) pageField;
		return input.setInput(days);
	}

	@Action(action = VALUE_REGEX + " == " + VALUE_REGEX, description = "Comparer deux variables")
	public ITestResult VarEqVar(
		String var1,
		String var2)
		throws Exception {
		if(var1.equals(var2)) {
			return new SuccessResult(Boolean.TRUE.toString());
		}
		else {
			return new ErrorResult(String.format("%s == %s => %s", var1, var2, Boolean.FALSE.toString()));
		}
	}

	@Action(
		action = VALUE_REGEX + " égale à " + VALUE_REGEX,
		description = "Comparer une valeur à une variable")
	public ITestResult ValueEqVar(
		String value,
		String var)
		throws Exception {
		if(value.equals(var)) {
			return new SuccessResult(Boolean.TRUE.toString());
		}
		else {
			return new ErrorResult(String.format("%s == %s => %s", value, var, Boolean.FALSE.toString()));
		}
	}

	@Action(action = "Assigner " + VALUE_REGEX + " à " + VALUE_REGEX, description = "Assigner valeur à variable")
	public com.synaptix.toast.dao.domain.api.test.ITestResult setValToVar(
		String value,
		String var)
		throws Exception {
		repo.getUserVariables().put(var, value);
		return new SuccessResult();
	}

	@Action(action = "Clear {{input:component:swing}}", description = "Effacer le contenu d'un composant input graphique")
	public ITestResult clear(SwingAutoElement pageField)
		throws Exception {
		SwingInputElement input = (SwingInputElement) pageField;
		input.clear();
		return new SuccessResult();
	}
}
