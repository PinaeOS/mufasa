package org.pinae.mufasa.client.request;

import java.security.cert.X509Certificate;

public class Certificate {
	
	private X509Certificate cert;

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
	
}
