package org.pinae.mufasa.client.request;

public class Url {

	private String protocol;
	
	private String domain;
	
	private int port;
	
	private Url() {
		
	}
	
	private Url(String url) {
		new Url(url);
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
