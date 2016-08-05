package io.toast.tk.swing.agent.interpret;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.toast.tk.adapter.SentenceBuilder;
import io.toast.tk.core.adapter.ActionAdapterSentenceRef.Types;
import io.toast.tk.core.agent.interpret.AWTCapturedEvent;
import io.toast.tk.core.agent.interpret.DefaultEventInterpreter;
import io.toast.tk.dao.domain.impl.repository.RepositoryImpl;

/**
 * 1. Connect to MongoDB throught toast web app api and check for existing types
 * 2. store or update repository with new objects 3. query known syntax to build
 * and record automation sentence
 * 
 */
public class LiveRedPlayEventInterpreter extends DefaultEventInterpreter {

	private static final Logger LOG = LogManager
			.getLogger(LiveRedPlayEventInterpreter.class);

	SentenceBuilder sentenceBuilder = new SentenceBuilder();

	private MongoRepositoryCacheWrapper mongoRepoManager;

	private boolean isConnected = false;

	private String host;

	private String port;

	public LiveRedPlayEventInterpreter(
			final MongoRepositoryCacheWrapper mongoRepoManager, String host, String port) {
		this.mongoRepoManager = mongoRepoManager;
		try {
			this.host = host;
			this.port = port;
			mongoRepoManager.initCache(host, port);
			isConnected = true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public boolean isConnectedToWebApp() {
		return isConnected;
	}

	@Override
	public String onWindowDisplay(AWTCapturedEvent eventObject) {
		return super.onWindowDisplay(eventObject);
	}

	@Override
	public String onBringOnTop(AWTCapturedEvent eventObject) {
		return super.onBringOnTop(eventObject);
	}

	@Override
	public String onClick(AWTCapturedEvent eventObject) {
		RepositoryImpl container = mongoRepoManager
				.findContainer(eventObject.container, "swing page");
		String label = mongoRepoManager.find(container,
				convertToKnowType(eventObject.componentType),
				eventObject.componentName);
		return sentenceBuilder.ofType(Types.CLICK_ON)
				.withPage(container.getName()).withComponent(label).build();
	}

	@Override
	public String onCheckBoxClick(AWTCapturedEvent eventObject) {
		eventObject.componentType = "checkbox";
		return onKeyInput(eventObject);
	}

	@Override
	public String onButtonClick(AWTCapturedEvent eventObject) {
		eventObject.componentType = "button";
		return onClick(eventObject);
	}

	@Override
	public String onKeyInput(AWTCapturedEvent eventObject) {
		RepositoryImpl container = mongoRepoManager
				.findContainer(eventObject.container, "swing page");
		String label = mongoRepoManager.find(container,
				convertToKnowType(eventObject.componentType),
				eventObject.componentName);
		return sentenceBuilder.ofType(Types.TYPE_IN_INPUT)
				.withPage(container.getName()).withComponent(label)
				.withValue(eventObject.businessValue).build();
	}

	/**
	 * Convert the type to a known type (currently hosted on webapp side:
	 * "button", "input", "menu", "table", "timeline", "date", "list",
	 * "checkbox", "other" To expose through webservice
	 * 
	 * @param type
	 * @return
	 */
	private String convertToKnowType(String type) {
		if (type.toLowerCase().contains("button")) {
			return "button";
		}
		if (type.toLowerCase().contains("checkbox")) {
			return "checkbox";
		}
		if (type.toLowerCase().contains("text")) {
			return "input";
		}
		if (type.toLowerCase().contains("table")) {
			return "table";
		}
		if (type.toLowerCase().contains("combo")) {
			return "list";
		}
		return "other";
	}

	@Override
	public String onTableClick(AWTCapturedEvent eventObject) {
		RepositoryImpl container = mongoRepoManager
				.findContainer(eventObject.container, "swing page");
		String label = mongoRepoManager.find(container,
				convertToKnowType(eventObject.componentType),
				eventObject.componentLocator);
		return sentenceBuilder.ofType(Types.SELECT_TABLE_ROW)
				.withPage(container.getName()).withComponent(label)
				.withValue(eventObject.businessValue).build();
	}

	@Override
	public String onMenuClick(AWTCapturedEvent eventObject) {
		return sentenceBuilder.ofType(Types.SELECT_SUB_MENU)
				.withValue(eventObject.componentName).build();
	}

	@Override
	public String onComboBoxClick(AWTCapturedEvent eventObject) {
		RepositoryImpl container = mongoRepoManager
				.findContainer(eventObject.container, "swing page");
		String label = mongoRepoManager.find(container,
				convertToKnowType(eventObject.componentType),
				eventObject.componentName);
		return sentenceBuilder.ofType(Types.SELECT_VALUE_IN_LIST)
				.withPage(container.getName()).withComponent(label)
				.withValue(eventObject.businessValue).build();
	}

	@Override
	public String onPopupMenuClick(AWTCapturedEvent eventObject) {
		return sentenceBuilder.ofType(Types.SELECT_CONTEXTUAL_MENU)
				.withValue(eventObject.componentName).build();
	}

	public boolean saveObjectsToRepository() {
		return mongoRepoManager.saveCache(host, port);
	}
}
