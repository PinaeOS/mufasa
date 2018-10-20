package org.pinae.mufasa.client;

import org.pinae.mufasa.client.annotation.Headers;
import org.pinae.mufasa.client.annotation.Params;
import org.pinae.mufasa.client.annotation.Request;
import org.pinae.mufasa.client.http.Http;

public interface User {
	
	@Request(path = "/demo/user", method = Http.HTTP_GET)
	@Headers({"Request-Type: t101"})
	public String getName(@Params("id") String id);
	
}
