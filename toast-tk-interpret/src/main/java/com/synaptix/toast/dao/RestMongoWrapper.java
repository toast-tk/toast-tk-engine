package com.synaptix.toast.dao;

import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.synaptix.toast.core.rest.RestUtils;
import com.synaptix.toast.dao.domain.impl.repository.RepositoryImpl;



/*
 * now that it's included in the webapp, should store directly
 */
public class RestMongoWrapper extends RestUtils {

	public static Collection<RepositoryImpl> loadRepository(
		String host,
		String port) {
		String webAppResourceURI = getWebAppURI(host, port) + "/loadRepository";
		Client httpClient = Client.create();
		String response = getJsonResponseAsString(webAppResourceURI, httpClient);
		Gson g = new Gson();
		Type typeOfT = new TypeToken<Collection<RepositoryImpl>>() {
		}.getType();
		Collection<RepositoryImpl> repository = (Collection<RepositoryImpl>) g.fromJson(response, typeOfT);
		return repository;
	}

	public static boolean saveRepository(
		Collection<RepositoryImpl> repoToSave,
		String host,
		String port) {
		String webAppResourceURI = getWebAppURI(host, port) + "/saveRepository";
		Client httpClient = Client.create();
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeHierarchyAdapter(ObjectId.class, new com.google.gson.JsonSerializer<ObjectId>() {
			@Override
			public JsonElement serialize(
				ObjectId src,
				Type typeOfSrc,
				JsonSerializationContext context) {
				if(src == null) {
					return null;
				}
				return new JsonPrimitive(src.toString());
			}
		});
		String json = gson.create().toJson(repoToSave);
		WebResource webResource = httpClient.resource(webAppResourceURI);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);
		int statusCode = response.getStatus();
		return statusCode == Response.Status.OK.getStatusCode();
	}
}
