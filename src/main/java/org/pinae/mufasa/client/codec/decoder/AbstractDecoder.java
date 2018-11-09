package org.pinae.mufasa.client.codec.decoder;

import java.io.IOException;

import org.pinae.mufasa.client.utils.GenericClassUtils;

public class AbstractDecoder implements Decoder {

	public String decode(Object value, Class<?> paramCls) throws IOException {
		return GenericClassUtils.toString(paramCls, value);
	}

}
