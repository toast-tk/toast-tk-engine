package io.toast.tk.dao.domain.impl.common;

import com.google.inject.assistedinject.Assisted;

import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;

public interface IServiceFactory<E extends AbstractMongoDaoService<?>> {
	
	E create(@Assisted final String dbName);

}
