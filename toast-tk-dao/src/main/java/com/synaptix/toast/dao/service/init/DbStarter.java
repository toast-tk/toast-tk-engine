package com.synaptix.toast.dao.service.init;

import com.github.jmkgreen.morphia.Datastore;

public interface DbStarter {

	public Datastore getDatabaseByName(
		String db);
}
