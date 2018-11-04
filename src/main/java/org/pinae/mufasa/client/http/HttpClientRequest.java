package org.pinae.mufasa.client.http;

import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.TreeMap;

public class HttpClientRequest {
	
	private boolean ssl;

	private X509Certificate cert;
	
	private int connectTimeout;
	
	private int readTimeout;
	
	private String url;
	
	private String method;
	
	private Map<String, String> headers = new TreeMap<String, String>();
	
	private Map<String, String> parameters = new TreeMap<String, String>();
	
	private String contentType;
	
	private String charset;
	
	private String content;
	
	public HttpClientRequest(X509Certificate cert) {
		this.cert = cert;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void addHeader(String key, String value) {
		this.headers.put(key, value);
	}
	
	public void addHeaders(Map<String, String> headers) {
		this.headers.putAll(headers);
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(String key, String value) {
		this.parameters.put(key, value);
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	

	
}
