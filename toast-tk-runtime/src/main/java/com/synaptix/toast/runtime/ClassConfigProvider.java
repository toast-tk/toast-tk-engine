package com.synaptix.toast.runtime;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;
import com.synaptix.toast.adapter.utils.ActionAdapterHelper;
import com.synaptix.toast.runtime.bean.TestComponentConfig;
import com.synaptix.toast.runtime.bean.TestEntityProperty;

@Singleton
public class ClassConfigProvider {

	private final Map<String, TestComponentConfig> classesConfig = new HashMap<>();

	public Map<String, TestComponentConfig> getConfigMap() {
		return classesConfig;
	}

	public String getTranslatedPropertyName(
		String testClassName,
		String testPropertyName
	) {
		testClassName = ActionAdapterHelper.parseTestString(testClassName);
		testPropertyName = ActionAdapterHelper.parseTestString(testPropertyName);
		final TestEntityProperty testEntityProperty = classesConfig.get(testClassName).getFieldNameMap().get(testPropertyName);
		if(testEntityProperty != null) {
			return testEntityProperty.appName;
		}
		return testPropertyName;
	}
}