package com.synaptix.toast.dao.service.dao.access.repository;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.dao.domain.impl.repository.ElementImpl;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class ElementDaoService extends AbstractMongoDaoService<ElementImpl> {
	public interface Factory {
		ElementDaoService create(final @Nullable @Assisted String dbName);
	}


	static final String CONTAINER_TYPE = "swing page";

	@Inject
	public ElementDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Named("default_db") String default_db,
		final @Nullable @Assisted String dbName
	) {
		super(ElementImpl.class, starter.getDatabaseByName(dbName != null ? dbName : default_db), cService);
	}

}