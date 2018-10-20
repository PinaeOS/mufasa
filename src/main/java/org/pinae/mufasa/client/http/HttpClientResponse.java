package org.pinae.mufasa.client.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class HttpClientResponse {
	
	private int statusCode;
	
	private Map<String, String> headers = new HashMap<String, String>();
	
	private String charset;
	
	private String contentType;
	
	private InputStream contentStream;
	
	private String contentString;

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

	public InputStream getContentStream() {
		return contentStream;
	}

	public void setContentStream(InputStream contentStream) {
		this.contentStream = contentStream;
	}
	
	public void close() throws IOException {
		if (this.contentStream != null) {
			this.contentStream.close();
		}
	}
	
	public Reader getContentReader() throws IOException {
		Reader reader = null;
		
		if (this.charset != null) {
			reader = new InputStreamReader(this.contentStream, this.charset);
		} else {
			reader = new InputStreamReader(this.contentStream);
		}
		
		return reader;
	}
	
	public String getContentString() throws IOException {
		
		if (StringUtils.isEmpty(this.contentString)) {
			
			Reader reader = getContentReader();
			BufferedReader buffer = new BufferedReader(reader);
	
			StringBuffer response = new StringBuffer();
			String line = " ";
			while ((line = buffer.readLine()) != null) {
				response.append(line + "\n");
			}
			this.contentString = response.toString();
		}
		
		return this.contentString;
	}
}
