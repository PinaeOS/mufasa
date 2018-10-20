package org.pinae.mufasa.client.http;

/**
 * 服务执行异常
 * 
 * @author Huiyugeng
 *
 */
public class HttpClientException extends Exception {

	private static final long serialVersionUID = -6089068431679048125L;

	public HttpClientException() {
		super();
	}

	public HttpClientException(String message) {
		super(message);
	}

	public HttpClientException(Throwable e) {
		super(e);
	}

	public HttpClientException(String requestId, Throwable e) {
		super(e);
	}

}
