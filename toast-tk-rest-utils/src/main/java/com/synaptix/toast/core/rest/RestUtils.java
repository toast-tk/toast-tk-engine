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
	
	public static void get(final String url) {
		final Client httpClient = Client.create();
		final WebResource webResource = httpClient.resource(StringEscapeUtils.escapeHtml3(url));
		final ClientResponse response = webResource.get(ClientResponse.class);
		final int statusCode = response.getStatus();
		LOG.debug("Client response code: {}", statusCode);
	}

	public static void postPage(
		final String webAppAddr,
		final String webAppPort,
		final String value,
		final Object[] selectedValues
	) {
		final Client httpClient = Client.create();
		final String webappURL = getWebAppURI(webAppAddr, webAppPort);
		final WebResource webResource = httpClient.resource(webappURL + "/saveNewInspectedPage");
		final InspectPage requestEntity = new InspectPage(value, Arrays.asList(selectedValues));
		final Gson gson = new Gson();
		final ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
			.post(ClientResponse.class, gson.toJson(requestEntity));
		final int statusCode = response.getStatus();
		LOG.info("Client response code: {}", statusCode);
	}

	public static String downloadRepositoryAsWiki() {
		final String webappURL = getWebAppURI();
		return downloadRepository(webappURL + "/loadWikifiedRepository");
	}

	public static String downloadRepository(final String uri) {
		final Client httpClient = Client.create();
		final String jsonResponse = getJsonResponseAsString(uri, httpClient);
		JSONArray jsonResult;
		try {
			jsonResult = new JSONArray(jsonResponse);
			final StringBuilder builder = new StringBuilder(4096);
			for(int i = 0; i < jsonResult.length(); i++) {
				final String page = jsonResult.getString(i);
				builder.append(page);
			}
			return builder.toString();
		}
		catch(final JSONException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getJsonResponseAsString(
		final String uri,
		final Client httpClient
	) {
		final WebResource webResource = httpClient.resource(uri);
		final ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		final int statusCode = response.getStatus();
		if(statusCode == 401) {
			try {
				throw new AuthenticationException("Invalid Username or Password");
			}
			catch(final AuthenticationException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return response.getEntity(String.class);
	}

	public static boolean postScenario(
		final String scenarioName,
		final String webAppHost,
		final String webAppPort,
		final String scenarioSteps
	) {
		try {
			final Client httpClient = Client.create();
			final String webappURL = getWebAppURI(webAppHost, webAppPort);
			final WebResource webResource = httpClient.resource(webappURL + "/saveNewInspectedScenario");
			final Gson gson = new Gson();
			final InspectScenario scenario = new InspectScenario(scenarioName, scenarioSteps);
			final String json = gson.toJson(scenario);
			LOG.debug(json);
			final ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);
			final int statusCode = response.getStatus();
			LOG.info("Client response code: {}", statusCode);
			return statusCode >= 200 && statusCode < 400;
		}
		catch(final Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
	}

	public static boolean postScenario(
		final String scenarioName,
		final String scenarioSteps
	) {
		return postScenario(scenarioName, scenarioSteps, "one", "two");
	}

	public static String getWebAppURI(
		final String host,
		final String port
	) {
		return "http://" + host + ":" + port;
	}

	public static String getWebAppURI() {
		final String webAppAddr = System.getProperty(WEBAPP_ADDR);
		if(webAppAddr == null || webAppAddr.isEmpty()) {
			throw new RuntimeException(WEBAPP_ADDR + " system property isn't defined !");
		}
		final String webAppPort = System.getProperty(WEBAPP_PORT);
		if(webAppPort == null || webAppPort.isEmpty()) {
			throw new RuntimeException(WEBAPP_PORT + " system property isn't defined !");
		}
		return getWebAppURI(webAppAddr, webAppPort);
	}

	public static Collection<ImportedScenario> getListOfScenario() {
		try {
			final Client httpClient = Client.create();
			final String webappURL = getWebAppURI();
			final String response = getJsonResponseAsString(webappURL + "/loadScenariiList", httpClient);
			final Gson g = new Gson();
			final Type typeOfT = new TypeToken<Collection<ImportedScenario>>() {/*NOOP*/}.getType();
			final Collection<ImportedScenario> scenarioList = g.fromJson(response, typeOfT);
			return scenarioList;
		}
		catch(final Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public static ImportedScenarioDescriptor getScenario(final ImportedScenario scenarioRef) {
		try {
			final Client httpClient = Client.create();
			final String webappURL = getWebAppURI();
			final String response = getJsonResponseAsString(webappURL + "/loadScenarioSteps/" + scenarioRef.getId(), httpClient);
			final Gson g = new Gson();
			final ImportedScenarioDescriptor scenarioDescriptor = g.fromJson(response, ImportedScenarioDescriptor.class);
			return scenarioDescriptor;
		}
		catch(final Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public static boolean post(
		final String url,
		final String jsonFixtureDescriptor
	) {
		final Client httpClient = Client.create();
		final WebResource webResource = httpClient.resource(url);
		final ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonFixtureDescriptor);
		return response.equals(200);
	}

	public static void main(final String[] args) {
		RestUtils.postScenario("newtest", "localhost", "9000", "a step");
	}
}