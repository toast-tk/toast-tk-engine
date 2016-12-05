package io.toast.tk.core.rest;

public class HttpRequest {

	public final String uri;
	public String apiKey;
	public String json;
	public String proxyAdress;
	public int proxyPort;
	public String proxyUser;
	public String proxyPassword;
	
	
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
	
}
