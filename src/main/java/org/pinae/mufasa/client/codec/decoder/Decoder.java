package org.pinae.mufasa.client.codec.decoder;

import java.io.IOException;

public interface Decoder {
	
	public String decode(Object value, Class<?> paramCls)  throws IOException;

}
