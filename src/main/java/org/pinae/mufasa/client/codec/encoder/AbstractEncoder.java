package org.pinae.mufasa.client.codec.encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.pinae.mufasa.client.http.HttpClientResponse;
import org.pinae.mufasa.client.utils.GenericClassUtils;

public abstract class AbstractEncoder implements Encoder {

	public Object encode(HttpClientResponse response, Class<?> returnCls) throws IOException {
		
		Object returnObj = null;

		if (GenericClassUtils.isGenericClass(returnCls)) {
			returnObj = GenericClassUtils.toObject(returnCls, response.getContentString());
		} else if (InputStream.class.isAssignableFrom(returnCls)) {
			returnObj = response.getContentStream();
		} else if (Reader.class.isAssignableFrom(returnCls)) {
			returnObj = response.getContentReader();
		}

		return returnObj;
	}

}
