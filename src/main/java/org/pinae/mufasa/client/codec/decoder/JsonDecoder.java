package org.pinae.mufasa.client.codec.decoder;

import java.io.IOException;

import com.alibaba.fastjson.JSON;

public final class JsonDecoder extends AbstractDecoder {
	public String decode(Object value, Class<?> paramCls) throws IOException {
		
		String paramValue = super.decode(value, paramCls);
		
		if (paramValue == null) {
			paramValue = JSON.toJSONString(value);
		}
		
		return paramValue;
	}
}
