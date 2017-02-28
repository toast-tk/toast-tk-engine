package io.toast.tk.core.rest;

public class HttpRequest {

	private final String uri;
	private String apiKey;
	private String json;
	private String proxyAddress;
	private int proxyPort;
	private String proxyUser;
	private String proxyPassword;

	public static class Builder {
		private String uri;
		private String apiKey;
		private String json;

		private Builder(){
		}

		public static Builder create(){
			return new Builder();
		}

		public Builder uri(String uri){
			this.uri = uri;
			return this;
		}

		public Builder json(String json){
			this.json = json;
			return this;
		}

		public Builder withKey(String apiKey){
			this.apiKey = apiKey;
			return this;
		}

		public HttpRequest build(){
			HttpRequest request = new HttpRequest(uri, json);
			request.setApiKey(apiKey);
			return request;
		}
	}

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
		this.proxyAddress = proxyHost;
		this.proxyPort = proxyPort2;
		this.proxyUser = proxyUser2;
		this.proxyPassword = proxyPassword2;
	}

	private void setApiKey(String apiKey) {
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

	public String getProxyAddress() {
		return this.proxyAddress;
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
