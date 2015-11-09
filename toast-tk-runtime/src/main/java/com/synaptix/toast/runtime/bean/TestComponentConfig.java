package com.synaptix.toast.runtime.bean;

import java.util.HashMap;
import java.util.Map;

import com.synaptix.toast.runtime.utils.ClassHelper;

public class TestComponentConfig {

	public Class<?> componentClass;

	public Class<Enum<?>> enumClass;

	public String searchBy;

	public final Map<String, TestEntityProperty> propertiesMap;

	private final boolean isDomain;

	private String tableName;

	public boolean isError;

	public String error;

	/**
	 * Constructor for entities configuration.
	 *
	 * @param appClassName
	 * @param searchBy
	 */
	@SuppressWarnings("unchecked")
	public TestComponentConfig(
		String appClassName,
		String searchBy) {
		this.propertiesMap = new HashMap<String,TestEntityProperty>();
		this.searchBy = searchBy;
		this.isDomain = false;
		this.isError = false;
		this.error = null;
		Class<?> entityClass = null;
		try {
			entityClass = Class.forName(appClassName);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			isError = true;
			error = "Class not found. Please check entity configuration.";
		}
		if(entityClass != null/* && IEntity.class.isAssignableFrom(entityClass) */) {
			this.componentClass = entityClass;
		}
	}

	/**
	 * Constructor for domains configuration.
	 *
	 * @param appClassName
	 */
	public TestComponentConfig(
		String appClassName) {
		this.propertiesMap = new HashMap<String, TestEntityProperty>();
		this.isDomain = true;
		Class<?> domainInterfaceClass = null;
		try {
			domainInterfaceClass = Class.forName(appClassName);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(domainInterfaceClass != null) {
			enumClass = getEnumSubClass(domainInterfaceClass);
			componentClass = domainInterfaceClass;
			addProperty("code", "code", null);
			addProperty("meaning", "meaning", null);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<Enum<?>> getEnumSubClass(
		Class<?> domainClass) {
		Class<?>[] classes = domainClass.getClasses();
		for(Class<?> class1 : classes) {
			if(class1.isEnum()) {
				return (Class<Enum<?>>) class1;
			}
		}
		return null;
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
		String testPropertyName,
		String appPropertyName,
		String objectType) {
		if(ClassHelper.hasProperty(componentClass, appPropertyName)) {
			propertiesMap.put(testPropertyName, new TestEntityProperty(testPropertyName, appPropertyName,
				objectType));
			return true;
		}
		else {
			return false;
		}
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

	public void setTableName(
		String tableName) {
		this.tableName = tableName;
	}
}
