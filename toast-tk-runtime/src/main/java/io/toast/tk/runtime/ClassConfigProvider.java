package io.toast.tk.runtime;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import io.toast.tk.adapter.utils.ActionAdapterHelper;
import io.toast.tk.runtime.bean.TestComponentConfig;
import io.toast.tk.runtime.bean.TestEntityProperty;

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