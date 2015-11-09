package com.synaptix.toast.dao.service.dao.common;

import java.util.List;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.Query;
import com.synaptix.toast.dao.domain.impl.common.TagImpl;

public class CommonMongoDaoService {

	public <E> List<E> getTaggedItems(
		com.github.jmkgreen.morphia.Datastore ds,
		Class<E> resultClass,
		TagImpl tag) {
		Query<E> q = ds.createQuery(resultClass).field("tags").hasThisOne(tag);
		List<E> items = q.asList();
		return items;
	}

	public void indexEntity(
		Datastore ds,
		String collection,
		String idObject,
		Object object) {
		throw new Error("Not implemented !");
	}
}
