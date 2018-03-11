package org.pinae.mufasa.client;

import org.pinae.mufasa.client.annotation.Headers;
import org.pinae.mufasa.client.annotation.Params;
import org.pinae.mufasa.client.annotation.Request;

public interface User {
	
	@Request(uri = "/user/name", method = Http.HTTP_GET)
	@Headers({"Request-Type: t101"})
	public String getName(@Params("id") String id);
	
}
