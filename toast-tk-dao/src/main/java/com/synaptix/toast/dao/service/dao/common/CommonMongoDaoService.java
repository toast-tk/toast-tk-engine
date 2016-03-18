package com.synaptix.toast.dao.service.dao.common;

import java.util.List;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.Query;
import com.synaptix.toast.dao.domain.impl.common.TagImpl;

public class CommonMongoDaoService {

	public static <E> List<E> getTaggedItems(
		final com.github.jmkgreen.morphia.Datastore ds,
		final Class<E> resultClass,
		final TagImpl tag
	) {
		final Query<E> q = ds.createQuery(resultClass).field("tags").hasThisOne(tag);
		final List<E> items = q.asList();
		return items;
	}

	public static void indexEntity(
		final Datastore ds,
		final String collection,
		final String idObject,
		final Object object
	) {
		throw new Error("Not implemented !");
	}
}