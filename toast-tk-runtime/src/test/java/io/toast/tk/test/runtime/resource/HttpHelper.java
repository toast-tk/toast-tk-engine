package io.toast.tk.test.runtime.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

public class HttpHelper {

	public static CloseableHttpResponse processHttpGet(final String url)
			throws Exception {
		final CloseableHttpClient httpClient = buildHttpClient();
		final HttpGet request = createHttpJsonGetRequest(url);
		final CloseableHttpResponse httpResponse = httpClient.execute(request);
		return httpResponse;
	}
	
	public static CloseableHttpResponse processHttpPost(final String url, final String jsonToPost)
			throws Exception {
		final CloseableHttpClient httpClient = buildHttpClient();
		final HttpPost request = createHttpPost(url, jsonToPost);
		final CloseableHttpResponse httpResponse = httpClient.execute(request);
		return httpResponse;
	}

	public static HttpPost createHttpPost(final String url,
			String jsonToPost) throws Exception {
		final HttpPost request = new HttpPost(url);
		final StringEntity jsonEntity = new StringEntity(
				new Gson().toJson(jsonToPost));
		request.setEntity(jsonEntity);
		request.addHeader("content-type", "application/json");
		request.addHeader("accept", "application/json");
		return request;
	}

	public static HttpGet createHttpJsonGetRequest(final String url) {
		final HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.addHeader("accept", "application/json");
		return request;
	}

	public static String encodeResponseEntityAsJsonString(
			final HttpResponse httpResponse) throws IOException {
		final BufferedReader rd = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent()));
		String response = "";
		String line;
		while ((line = rd.readLine()) != null) {
			response = response.concat(line);
		}
		return response;
	}

	public static int GET(final String request) throws IOException, Exception {
		try (final CloseableHttpResponse response = processHttpGet(request);) {
			return response.getStatusLine().getStatusCode();
		}
	}

	public static int POST(final String request, final String json) throws IOException, Exception {
		try (final CloseableHttpResponse response = processHttpPost(request, json)) {
			return response.getStatusLine().getStatusCode();
		}
	}

	public static CloseableHttpClient buildHttpClient() {
		return HttpClientBuilder.create().build();
	}
}
