package org.pinae.mufasa.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpClientResponse {
	
	private int statusCode;
	
	private Map<String, String> headers = new HashMap<String, String>();
	
	private String charset;
	
	private String contentType;
	
	private InputStream content;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public void setHeader(String name, String value) {
		this.headers.put(name, value);
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}
	
	public void close() throws IOException {
		if (this.content != null) {
			this.content.close();
		}
	}
}
