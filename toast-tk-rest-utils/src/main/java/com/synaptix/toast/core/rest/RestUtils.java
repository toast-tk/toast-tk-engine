package com.synaptix.toast.core.rest;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

import javax.naming.AuthenticationException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestUtils {

	private static final Logger LOG = LogManager.getLogger(RestUtils.class);
	public static final String WEBAPP_ADDR = "toast.webapp.addr";
	public static final String WEBAPP_PORT = "toast.webapp.port";
	
	public static void get(
		String url) {
		Client httpClient = Client.create();
		WebResource webResource = httpClient.resource(StringEscapeUtils.escapeHtml3(url));
		ClientResponse response = webResource.get(ClientResponse.class);
		int statusCode = response.getStatus();
		if(LOG.isDebugEnabled()) {
			LOG.debug("Client response code: " + statusCode);
		}
	}

	public static void postPage(
		String webAppAddr,
		String webAppPort,
		String value,
		Object[] selectedValues) {
		Client httpClient = Client.create();
		String webappURL = getWebAppURI(webAppAddr, webAppPort);
		WebResource webResource = httpClient.resource(webappURL + "/saveNewInspectedPage");
		InspectPage requestEntity = new InspectPage(value, Arrays.asList(selectedValues));
		Gson gson = new Gson();
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
			.post(ClientResponse.class, gson.toJson(requestEntity));
		int statusCode = response.getStatus();
		LOG.info("Client response code: " + statusCode);
	}

	public static String downloadRepositoyAsWiki() {
		String webappURL = getWebAppURI();
		return downloadRepository(webappURL + "/loadWikifiedRepository");
	}

	public static String downloadRepository(
		String uri) {
		Client httpClient = Client.create();
		String jsonResponse = getJsonResponseAsString(uri, httpClient);
		JSONArray jsonResult;
		try {
			jsonResult = new JSONArray(jsonResponse);
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < jsonResult.length(); i++) {
				String page = jsonResult.getString(i);
				builder.append(page);
			}
			return builder.toString();
		}
		catch(JSONException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getJsonResponseAsString(
		String uri,
		Client httpClient) {
		WebResource webResource = httpClient.resource(uri);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		if(statusCode == 401) {
			try {
				throw new AuthenticationException("Invalid Username or Password");
			}
			catch(AuthenticationException e) {
				e.printStackTrace();
			}
		}
		String jsonResponse = response.getEntity(String.class);
		return jsonResponse;
	}

	public static boolean postScenario(
		String scenarioName,
		String webAppHost,
		String webAppPort,
		String scenarioSteps) {
		try {
			Client httpClient = Client.create();
			String webappURL = getWebAppURI(webAppHost, webAppPort);
			WebResource webResource = httpClient.resource(webappURL + "/saveNewInspectedScenario");
			Gson gson = new Gson();
			InspectScenario scenario = new InspectScenario(scenarioName, scenarioSteps);
			String json = gson.toJson(scenario);
			System.out.println(json);
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, json);
			int statusCode = response.getStatus();
			LOG.info("Client response code: " + statusCode);
			return statusCode >= 200 && statusCode < 400;
		}
		catch(Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
	}

	public static boolean postScenario(
		String scenarioName,
		String scenarioSteps) {
		return postScenario(scenarioName, scenarioSteps, "one", "two");
	}

	public static String getWebAppURI(
		String host,
		String port) {
		return "http://" + host + ":" + port;
	}

	public static String getWebAppURI() {
		String webAppAddr = System.getProperty(WEBAPP_ADDR);
		if(webAppAddr == null || webAppAddr.isEmpty()) {
			throw new RuntimeException(WEBAPP_ADDR + " system property isn't defined !");
		}
		String webAppPort = System.getProperty(WEBAPP_PORT);
		if(webAppPort == null || webAppPort.isEmpty()) {
			throw new RuntimeException(WEBAPP_PORT + " system property isn't defined !");
		}
		return getWebAppURI(webAppAddr, webAppPort);
	}

	public static Collection<ImportedScenario> getListOfScenario() {
		try {
			Client httpClient = Client.create();
			String webappURL = getWebAppURI();
			String response = getJsonResponseAsString(webappURL + "/loadScenariiList", httpClient);
			Gson g = new Gson();
			Type typeOfT = new TypeToken<Collection<ImportedScenario>>() {
			}.getType();
			Collection<ImportedScenario> scenarioList = g.fromJson(response, typeOfT);
			return scenarioList;
		}
		catch(Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public static ImportedScenarioDescriptor getScenario(
		ImportedScenario scenarioRef) {
		try {
			Client httpClient = Client.create();
			String webappURL = getWebAppURI();
			String response = getJsonResponseAsString(
				webappURL + "/loadScenarioSteps/" + scenarioRef.getId(),
				httpClient);
			Gson g = new Gson();
			ImportedScenarioDescriptor scenarioDescriptor = g.fromJson(response, ImportedScenarioDescriptor.class);
			return scenarioDescriptor;
		}
		catch(Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public static boolean post(
		String url,
		String jsonFixtureDescriptor) {
		Client httpClient = Client.create();
		WebResource webResource = httpClient.resource(url);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
			.post(ClientResponse.class, jsonFixtureDescriptor);
		return response.equals(200);
	}

	public static void main(
		String[] args) {
		RestUtils.postScenario("newtest", "localhost", "9000", "a step");
	}
}