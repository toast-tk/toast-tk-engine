package com.synaptix.toast.dao.service.init;

import com.github.jmkgreen.morphia.Datastore;

public interface DbStarter {

	Datastore getDatabaseByName(final String db);
}