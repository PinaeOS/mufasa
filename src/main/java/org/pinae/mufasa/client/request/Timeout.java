package org.pinae.mufasa.client.request;

public class Timeout {
	
    private int connectTimeout = 10 * 1000;
    
    private int readTimeout = 60 * 1000;
    
    public Timeout(int connectTimeout, int readTimeout) {
    	this.connectTimeout = connectTimeout;
    	this.readTimeout = readTimeout;
    }

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}
    
}
