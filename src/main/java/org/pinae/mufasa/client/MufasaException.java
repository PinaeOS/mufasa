package org.pinae.mufasa.client;

/**
 * 服务执行异常
 * 
 * @author Huiyugeng
 *
 */
public class MufasaException extends Exception {

	private static final long serialVersionUID = -6089068431679048125L;

	public MufasaException() {
		super();
	}

	public MufasaException(String message) {
		super(message);
	}

	public MufasaException(Throwable e) {
		super(e);
	}

	public MufasaException(String requestId, Throwable e) {
		super(e);
	}

}
