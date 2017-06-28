package io.toast.tk.swing.agent.interpret;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import com.google.common.base.Strings;
import com.mongodb.WriteConcern;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.dao.domain.impl.repository.ElementImpl;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.repository.RepositoryImpl;
import io.toast.tk.dao.service.dao.access.repository.RepositoryDaoService;

public class MongoRepositoryCacheWrapper {

	private static final Logger LOG = LogManager.getLogger(MongoRepositoryCacheWrapper.class);

	Collection<RepositoryImpl> cache = null;

	private String host;

	private String port;

	private RepositoryDaoService service;
	
	public MongoRepositoryCacheWrapper(RepositoryDaoService service){
		this.service = service;
		initCache();
	}
	
	private void initCache() {
		try {
			cache = service.find().asList();
		}
		catch(Exception e) {
			LOG.error(String.format("WebApp not active at address %s:%s", host, port), e);
		}
	}
	
	public void saveRepository(RepositoryImpl repo) {
		synchronized (service) {
			service.save(repo, WriteConcern.ACKNOWLEDGED);
			initCache();
		}
	}
	
	public ElementImpl findElement(RepositoryImpl container, WebEventRecord eventRecord) {
		String locator = eventRecord.getTarget();
		for(RepositoryImpl repImpl : cache) {
			if(repImpl.getName().equals(container.getName()) 
					&& Objects.nonNull(repImpl.rows)) {
				for(ElementImpl element : repImpl.rows) {
					if(element.locator.equalsIgnoreCase(locator.toLowerCase())) {
						return element;
					}
				}
			}
		}
		ElementImpl element = buildElement(eventRecord, locator);
		container.rows.add(element);
		return element;
	}

	private ElementImpl buildElement(WebEventRecord eventRecord, String locator) {
		ElementImpl impl = new ElementImpl();
		String name = eventRecord.getComponentName();
		String type = eventRecord.getComponent();
		impl.locator = locator;
		if(locator.contains(":")) {
			impl.setName(locator.split(":")[1]);
		} else {
			String elementName = name == null ? type + "-" + UUID.randomUUID().toString() : name;
			impl.setName(elementName);
		}
		impl.setName(formatLabel(impl.getName()));
		impl.type = getAdjustedType(type);
		impl.method= getMethod(eventRecord);
		impl.setId(ObjectId.get());
		return impl;
	}
	

	public RepositoryImpl findContainer(
		String lastKnownContainer, String type, ProjectImpl project) {
		
		String container = formatLabel(lastKnownContainer);
		
		for(RepositoryImpl page : cache) {
			if(page.getName().equals(container)) {
				if(Objects.nonNull(page.project)){
					if(project.getId().toString().equals(project.getId().toString())){
						return page;
					}
				}
			}
		}
	
		RepositoryImpl page = new RepositoryImpl();
		page.setName(container);
		page.project = project;
		page.type = type;
		cache.add(page);
		
		return page;
	}

	private String formatLabel(
		String name) {
		return name.trim().replace(" ", "_").replace("'", "_").replace("°", "_");
	}

	
	private String getMethod(WebEventRecord eventRecord) {
		if (!Strings.isNullOrEmpty(eventRecord.getPath())){
			return "XPATH";
		}
		return "CSS";
	}

	public String getAdjustedType(String type){
		if("text".equals(type)){
			return "input";
		}
		return type;
	}
}
