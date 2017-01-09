package io.toast.tk.core.rest;

public class HttpRequest {

	private final String uri;
	private String apiKey;
	private String json;
	private String proxyAdress;
	private int proxyPort;
	private String proxyUser;
	private String proxyPassword;
	
	
	public HttpRequest(String uri) {
		this.uri = uri;
	}
	
	public HttpRequest(String uri, String json) {
		this(uri);
		this.json = json;
	}

	public void setProxyInfo(String proxyHost, 
			int proxyPort2, 
			String proxyUser2, 
			String proxyPassword2) {
		this.proxyAdress = proxyHost;
		this.proxyPort = proxyPort2;
		this.proxyUser = proxyUser2;
		this.proxyPassword = proxyPassword2;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUri() {
		return this.uri;
	}
	
	public String getApiKey() {
		return this.apiKey;
	}
	
	public String getJson() {
		return this.json;
	}

	public String getProxyAdress() {
		return this.proxyAdress;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public String getProxyUser() {
		return this.proxyUser;
	}

	public String getProxyPassword() {
		return this.proxyPassword;
	}
}
