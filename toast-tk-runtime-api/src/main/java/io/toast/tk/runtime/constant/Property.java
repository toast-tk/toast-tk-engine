package io.toast.tk.runtime.constant;

import java.io.File;

public final class Property {

	private Property() {

	}
	
	public static final String PATH_DELIMITER = "/";
	
	public static final String TOAST_CONTACT = "contact.toast-tk@talan.com";
	
	public static final String DEFAULT_WEBAPP_ADDR_PORT = "http://localhost:9000";

	public static final String WEBAPP_ADDR = "toast.webapp.addr";

	public static final String WEBAPP_PORT = "toast.webapp.port";

	public static final String TOAST_PLUGIN_DIR_PROP = "toast.plugin.dir";

	public static final String MONGO_HOST = "toast.mongo.addr";

	public static final String MONGO_PORT = "toast.mongo.port";

	public static final String TOAST_RUNTIME_TYPE = "toast.runtime.type";

	public static final String TOAST_RUNTIME_CMD = "toast.runtime.command";

	public static final String TOAST_RUNTIME_AGENT = "toast.runtime.agent";

	public static final String TOAST_HOME_DIR_NAME = ".toast";

	public static final String TOAST_HOME_DIR = System.getProperty("user.home") + File.separatorChar + TOAST_HOME_DIR_NAME + File.separatorChar;

	public static final String TOAST_PLUGIN_DIR = System.getProperty("user.home") + File.separatorChar + TOAST_HOME_DIR_NAME
		+ File.separatorChar + "plugins";

	public static final String TOAST_RUNTIME_DIR = System.getProperty("user.home") + File.separatorChar + TOAST_HOME_DIR_NAME
		+ File.separatorChar + "runtime";

	public static final String TOAST_TARGET_DIR = System.getProperty("user.home") + File.separatorChar + TOAST_HOME_DIR_NAME
		+ File.separatorChar + "target" + File.separatorChar + "toast-test-results" + File.separatorChar;
	
	public static final String TOAST_TEMP_DIR = System.getProperty("user.home") + File.separatorChar + TOAST_HOME_DIR_NAME
			+ File.separatorChar + "temp" + File.separatorChar;

	public static final String TOAST_LOG_DIR = System.getProperty("user.home") + File.separatorChar + TOAST_HOME_DIR_NAME + File.separatorChar + "logs" + File.separatorChar;

	public static final String DOWNLOAD_DIR = System.getProperty("user.home") + File.separatorChar + "Downloads" + File.separatorChar;

	public static final String TOAST_PROPERTIES_FILE = Property.TOAST_HOME_DIR + "toast.properties";

	public static final String AGENT_JAR_NAME = "toast-tk-agent-standalone.jar";

	public static final String TOAST_SUT_RUNNER_BAT = "run_sut.bat";

	public static final String REDPEPPER_AUTOMATION_SETTINGS_DEFAULT_DIR = "settings/toast_descriptor.json";

	public static final String JNLP_RUNTIME_HOST = "toast.jnlp.runtime.host";

	public static final String JNLP_RUNTIME_FILE = "toast.jnlp.runtime.file";

	public static final String AGENT_DEBUG_AGRS = "toast.sut.debug.args";

	public static final String TABLE_CRITERIA_SEPARATOR = ";";

	public static final String TABLE_KEY_VALUE_SEPARATOR = "=";

	public static final String JLIST_CRITERIA_SEPARATOR = ";";

	public static final String DEFAULT_PARAM_SEPARATOR = ";";

	public static final String DEFAULT_PARAM_INPUT_SEPARATOR = "<-";

	public static final int TOAST_AGENT_PORT = 7676;
	
	public static final String ACTION_ITEM_VAR_REGEX_1 = "(\\(\\$[\\w|\\.]+\\))";
	
	public static final String ACTION_ITEM_VAR_REGEX_2 = "(\\$[\\w|\\.]+)";
}