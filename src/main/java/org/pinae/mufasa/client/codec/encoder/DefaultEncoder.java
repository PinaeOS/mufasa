package org.pinae.mufasa.client.codec.encoder;

import java.io.IOException;

import org.pinae.mufasa.client.http.HttpClientResponse;

public final class DefaultEncoder extends AbstractEncoder {

	public Object encode(HttpClientResponse response, Class<?> returnCls) throws IOException {
		return super.encode(response, returnCls);
	}

}
