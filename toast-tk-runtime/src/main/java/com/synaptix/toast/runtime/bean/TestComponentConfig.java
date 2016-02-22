package com.synaptix.toast.runtime.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.runtime.utils.ClassHelper;

public class TestComponentConfig {

	private static final Logger LOG = LogManager.getLogger(TestComponentConfig.class);
	
	public Class<?> componentClass;

	public Class<Enum<?>> enumClass;

	public String searchBy;

	public final Map<String, TestEntityProperty> propertiesMap;

	private String tableName;

	/**
	 * Constructor for entities configuration.
	 *
	 * @param appClassName
	 * @param searchBy
	 */
	public TestComponentConfig(
		final String appClassName,
		final String searchBy
	) {
		this.propertiesMap = new HashMap<>();
		this.searchBy = searchBy;
		final Class<?> entityClass = loadApplicationClass(appClassName);
		if(entityClass != null) {
			this.componentClass = entityClass;
		}
	}

	private static Class<?> loadApplicationClass(final String appClassName) {
		try {
			return Class.forName(appClassName);
		}
		catch(final ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Constructor for domains configuration.
	 *
	 * @param appClassName
	 */
	public TestComponentConfig(
		final String appClassName
	) {
		this.propertiesMap = new HashMap<>();
		final Class<?> domainInterfaceClass = loadApplicationClass(appClassName);
		if(domainInterfaceClass != null) {
			this.enumClass = getEnumSubClass(domainInterfaceClass);
			this.componentClass = domainInterfaceClass;
			addProperty("code", "code", null);
			addProperty("meaning", "meaning", null);
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<Enum<?>> getEnumSubClass(final Class<?> domainClass) {
		return (Class<Enum<?>>) Arrays.stream(domainClass.getClasses()).filter(c -> c.isEnum()).findFirst().get();
	}

	/**
	 * Returns false if the property of name "appPropertyName" exists in the component. Else add the property in the map.
	 *
	 * @param testPropertyName
	 * @param appPropertyName
	 * @param objectType
	 * @return
	 */
	public boolean addProperty(
		final String testPropertyName,
		final String appPropertyName,
		final String objectType
	) {
		if(ClassHelper.hasProperty(componentClass, appPropertyName)) {
			propertiesMap.put(testPropertyName, new TestEntityProperty(testPropertyName, appPropertyName, objectType));
			return true;
		}
		return false;
	}

	public Map<String, TestEntityProperty> getFieldNameMap() {
		return propertiesMap;
	}

	public Class<?> getComponentClass() {
		return componentClass;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}
}