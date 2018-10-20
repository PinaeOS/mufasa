package org.pinae.mufasa.client.codec.decoder;

import java.io.IOException;

public class AbstractDecoder implements Decoder {

	public String decode(Object value, Class<?> paramCls) throws IOException {
		String paramValue = null;
		
		if (paramCls.equals(String.class)) {
			paramValue = value.toString();
		}
		
		return paramValue;
	}

}
