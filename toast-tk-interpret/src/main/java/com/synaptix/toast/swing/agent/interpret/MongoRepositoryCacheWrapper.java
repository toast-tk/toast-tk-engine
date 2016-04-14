package com.synaptix.toast.swing.agent.interpret;

import java.util.Collection;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.WriteConcern;
import com.sun.jersey.api.client.ClientHandlerException;
import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.dao.RestMongoWrapper;
import com.synaptix.toast.dao.domain.impl.repository.ElementImpl;
import com.synaptix.toast.dao.domain.impl.repository.RepositoryImpl;
import com.synaptix.toast.dao.service.dao.access.repository.RepositoryDaoService;

public class MongoRepositoryCacheWrapper {

	private static final Logger LOG = LogManager.getLogger(MongoRepositoryCacheWrapper.class);

	Collection<RepositoryImpl> cache = null;

	private String host;

	private String port;

	private RepositoryDaoService service;
	
	private RepositoryImpl container;

	
	public RepositoryImpl getLastKnownContainer(){
		return container;
	}
	
	public RepositoryImpl saveLastKnownContainer(){
		service.save(container, WriteConcern.ACKNOWLEDGED);
		return container;
	}
	
	
	public void initCache(RepositoryDaoService service) {
		try {
			this.service = service;
			cache = service.find().asList();
		}
		catch(ClientHandlerException e) {
			LOG.error(
				String.format("WebApp not active at address %s:%s", host, port),
				e);
		}
	}
	
	public void initCache(String host, String port) {
		try {
			this.host = host;
			this.port = port;
			cache = RestMongoWrapper.loadRepository(host, port);
		}
		catch(ClientHandlerException e) {
			LOG.error(
				String.format("WebApp not active at address %s:%s", host, port),
				e);
		}
	}

	public String find(
		RepositoryImpl container,
		String type,
		String locator) {
		for(RepositoryImpl repImpl : cache) {
			if(repImpl.getName().equals(container.getName())) {
				if(repImpl.rows != null) {
					for(ElementImpl element : repImpl.rows) {
						if(element.locator.equalsIgnoreCase(locator.toLowerCase())) {
							return "".equals(element.name) || element.name == null ? element.locator : element.name;
						}
					}
				}
			}
		}
		ElementImpl impl = extractElement(type, locator);
		container.rows.add(impl);
		return impl.name;
	}
	

	private ElementImpl extractElement(
		String type,
		String locator) {
		ElementImpl impl = new ElementImpl();
		impl.locator = locator;
		if(locator.contains(":")) {
			impl.name = locator.split(":")[1];
		}
		else {
			impl.name = locator == null ? type + "-" + UUID.randomUUID().toString() : locator;
		}
		impl.name = formatLabel(impl.name);
		impl.type = type;
		return impl;
	}

	public RepositoryImpl findContainer(
		String lastKnownContainer, String type) {
		
		lastKnownContainer = formatLabel(lastKnownContainer);
		
		for(RepositoryImpl page : cache) {
			if(page.getName().equals(lastKnownContainer)) {
				if(this.container == null){
					this.container = page;
				}
				return page;
			}
		}
	
		RepositoryImpl page = new RepositoryImpl();
		page.setName(lastKnownContainer);
		page.type = type;
		cache.add(page);
		
		this.container = page;
	
		return page;
	}

	private String formatLabel(
		String name) {
		return name.trim().replace(" ", "_").replace("'", "_").replace("°", "_");
	}

	public void saveRepository(RepositoryImpl repo) {
		service.save(repo, WriteConcern.ACKNOWLEDGED);
		initCache(service);
	}
	
	public boolean saveCache(String host, String port) {
		boolean saveRepository = RestMongoWrapper.saveRepository(cache, host, port);
		initCache(host, port);
		return saveRepository;
	}

	public String getWikiFiedRepo(String host, String port) {
		if(cache == null) {
			initCache(host, port);
		}
		String res = "";
		for(RepositoryImpl page : cache) {
			res += "#Page id:" + page.getId().toString() + "\n";
			res += "|| auto setup ||\n";
			res += "| " + page.type + " | " + page.name + " |\n";
			res += "| name | type | locator |\n";
			if(page.rows != null) {
				for(ElementImpl row : page.rows) {
					res += "|" + row.name + "|" + row.type + "|" + row.locator + "|\n";
				}
			}
			res += "\n";
		}
		return res;
	}

	public ElementImpl find(RepositoryImpl container, WebEventRecord eventRecord) {
		String locator = eventRecord.target;
		for(RepositoryImpl repImpl : cache) {
			if(repImpl.getName().equals(container.getName())) {
				if(repImpl.rows != null) {
					for(ElementImpl element : repImpl.rows) {
						if(element.locator.equalsIgnoreCase(locator.toLowerCase())) {
							return element;
						}
					}
				}
			}
		}
		ElementImpl element = buildElement(eventRecord, locator);
		container.rows.add(element);
		//save
		return element;
	}

	private ElementImpl buildElement(WebEventRecord eventRecord, String locator) {
		ElementImpl impl = new ElementImpl();
		String name = eventRecord.componentName;
		String type = eventRecord.component;
		impl.locator = locator;
		if(locator.contains(":")) {
			impl.name = locator.split(":")[1];
		} else {
			impl.name = name == null ? type + "-" + UUID.randomUUID().toString() : name;
		}
		impl.name = formatLabel(impl.name);
		impl.type = type;
		impl.method="CSS";
		impl.setId(ObjectId.get());;
		return impl;
	}
	
	public String getAdjustedType(String type){
		if(type.equals("text")){
			return "input";
		}
		return type;
	}
}
