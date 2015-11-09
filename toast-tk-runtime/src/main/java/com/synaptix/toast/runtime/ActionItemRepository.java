package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.synaptix.toast.adapter.swing.component.DefaultSwingPage;
import com.synaptix.toast.adapter.utils.ActionAdapterHelper;
import com.synaptix.toast.adapter.web.component.DefaultWebPage;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.runtime.bean.TestComponentConfig;
import com.synaptix.toast.runtime.bean.TestEntityProperty;

public class ActionItemRepository implements IActionItemRepository {

	private static final Log LOG = LogFactory.getLog(ActionItemRepository.class);

	private final Map<String, TestComponentConfig> classesConfig;

	HashMap<String, IFeedableWebPage> pages = new HashMap<String, IFeedableWebPage>();

	HashMap<String, IFeedableSwingPage> swingpages = new HashMap<String, IFeedableSwingPage>();

	private final Map<String, Class<?>> services = new HashMap<String, Class<?>>();

	public Map<String, Object> getUserVariables() {
		return userVariables;
	}

	public void setUserVariables(
		Map<String, Object> userVariables) {
		this.userVariables = userVariables;
	}

	private Map<String, Object> userVariables;


	@Inject
	public ActionItemRepository(
		ClassConfigProvider classConfigProvider) {
		this.classesConfig = classConfigProvider.getConfigMap();
		this.userVariables = new HashMap<String, Object>();
	}

	/**
	 * Configure a new class.
	 *
	 * @param className       Full class name in the application (i.e. "fr.gefco.tli.psc.ref.model.ICountry").
	 * @param greenpepperName Name of the class in the test files.
	 * @param searchBy
	 * @return
	 */
	public TestResult addClass(
		String className,
		String greenpepperName,
		String searchBy) {
		greenpepperName = ActionAdapterHelper.parseTestString(greenpepperName);
		searchBy = ActionAdapterHelper.parseTestString(searchBy);
		if(classesConfig.containsKey(greenpepperName)) {
			return new TestResult("This entity has already been configured", ResultKind.INFO);
		}
		classesConfig.put(greenpepperName, new TestComponentConfig(className, searchBy));
		TestComponentConfig testComponentConfig = classesConfig.get(greenpepperName);
		if(testComponentConfig != null) {
			if(testComponentConfig.isError) {
				return new TestResult(testComponentConfig.error, ResultKind.ERROR);
			}
		}
		return new TestResult("Done", ResultKind.INFO);
	}

	/**
	 * Configure a property for a component.
	 *
	 * @param testClassName    Test name of the component
	 * @param testPropertyName Test name of the property
	 * @param appPropertyName  Property name in the application
	 * @param objectType       Test name of the component association
	 * @return
	 */
	public TestResult addProperty(
		String testClassName,
		String testPropertyName,
		String appPropertyName,
		String objectType) {
		testClassName = ActionAdapterHelper.parseTestString(testClassName);
		testPropertyName = ActionAdapterHelper.parseTestString(testPropertyName);
		objectType = ActionAdapterHelper.parseTestString(objectType);
		TestComponentConfig entityConfig = classesConfig.get(testClassName);
		if(entityConfig != null) {
			if(entityConfig.addProperty(testPropertyName, appPropertyName, objectType)) {
				return new TestResult("Done", ResultKind.INFO);
			}
			else {
				return new TestResult("The property " + appPropertyName + " does not exist in "
					+ entityConfig.componentClass, ResultKind.ERROR);
			}
		}
		return new TestResult("Class has not be configured.");
	}

	/**
	 * Checks if a column is correctly setup (with config entity table).
	 *
	 * @param entityName
	 * @param content
	 */
	public boolean checkThatColumnNameExists(
		String entityName,
		String content) {
		entityName = ActionAdapterHelper.parseTestString(entityName);
		content = ActionAdapterHelper.parseTestString(content);
		if(classesConfig.containsKey(entityName)) {
			if(classesConfig.get(entityName).propertiesMap.containsKey(content)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkThatEntityExists(
		String entityName) {
		return classesConfig.containsKey(ActionAdapterHelper.parseTestString(entityName));
	}



	public Class<?> getComponentClassFullNameFromTestName(
		String testName) {
		testName = ActionAdapterHelper.parseTestString(testName);
		TestComponentConfig config = this.classesConfig.get(testName);
		if(config != null) {
			return config.componentClass;
		}
		return null;
	}

	public String getPropertyName(
		String className,
		String testName) {
		TestComponentConfig testComponentConfig = this.classesConfig.get(testName);
		if(testComponentConfig != null) {
			TestEntityProperty testEntityProperty = testComponentConfig.propertiesMap.get(testName);
			if(testEntityProperty != null) {
				return testEntityProperty.appName;
			}
		}
		return null;
	}

	public String getPropertyType(
		String className,
		String testName) {
		TestComponentConfig testComponentConfig = this.classesConfig.get(testName);
		if(testComponentConfig != null) {
			TestEntityProperty testEntityProperty = testComponentConfig.propertiesMap.get(testName);
			if(testEntityProperty != null) {
				return testEntityProperty.entityType;
			}
		}
		return null;
	}

	public void addPage(
		String entityName) {
		pages.put(entityName, new DefaultWebPage());
	}

	public void addSwingPage(
		String fixtureName) {
		swingpages.put(fixtureName, new DefaultSwingPage());
	}

	@Override
	public IFeedableSwingPage getSwingPage(
		String entityName) {
		return swingpages.get(entityName);
	}

	public IFeedableWebPage getPage(
		String entityName) {
		return pages.get(entityName);
	}

	@Override
	public Collection<IFeedableSwingPage> getSwingPages() {
		return swingpages.values();
	}

	@Override
	public Collection<IFeedableWebPage> getWebPages() {
		return pages.values();
	}

	/**
	 * @param type
	 * @param className
	 */
	public TestResult addService(
		String type,
		String className) {
		type = ActionAdapterHelper.parseTestString(type);
		Class<?> forName;
		try {
			forName = Class.forName(className);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			return new TestResult("Class not found", ResultKind.ERROR);
		}
		services.put(type, forName);
		return new TestResult("Done", ResultKind.INFO);
	}

	public Class<?> getService(
		String testName) {
		return services.get(ActionAdapterHelper.parseTestString(testName));
	}

	/**
	 *
	 */
	public void clean() {
		classesConfig.clear();
		services.clear();
		pages.clear();
		swingpages.clear();
	}
}