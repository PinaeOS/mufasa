package org.pinae.mufasa.client.codec.encoder;

import java.io.IOException;

import org.pinae.mufasa.client.http.HttpClientResponse;

public interface Encoder {
	
	public Object encode(HttpClientResponse response, Class<?> returnCls)  throws IOException;
	
}
