package org.pinae.mufasa.client.codec.encoder;

import java.io.IOException;

import org.pinae.mufasa.client.http.Http;
import org.pinae.mufasa.client.http.HttpClientResponse;

import com.alibaba.fastjson.JSON;

public final class JsonEncoder extends AbstractEncoder {
	
	public Object encode(HttpClientResponse response, Class<?> returnCls) throws IOException {
		
		Object returnObj = super.encode(response, returnCls);
		
		if (returnObj == null) {
			if (Http.APPLICATION_JSON.equalsIgnoreCase(response.getContentType())) {
				returnObj = JSON.parseObject(response.getContentString(), returnCls);
			} else {
				throw new IOException("Unsupport Return Type " + returnCls.getName());
			}
		}
		
		return returnObj;
		
	}
	
}
