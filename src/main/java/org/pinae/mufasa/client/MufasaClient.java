package org.pinae.mufasa.client;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import org.pinae.mufasa.client.codec.decoder.Decoder;
import org.pinae.mufasa.client.codec.encoder.Encoder;
import org.pinae.mufasa.client.handler.DefaultMethodHandler;
import org.pinae.mufasa.client.handler.HttpRequestHandler;
import org.pinae.mufasa.client.request.Timeout;

public abstract class MufasaClient {

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		
		private String host;
		
		private Timeout timeout;

		private Encoder encoder;

		private Decoder decoder;

		public Builder host(String host) {
			this.host = host;
			return this;
		}
		
		public Builder decoder(Timeout timeout) {
			this.timeout = timeout;
			return this;
		}

		public Builder encoder(Encoder encoder) {
			this.encoder = encoder;
			return this;
		}

		public Builder decoder(Decoder decoder) {
			this.decoder = decoder;
			return this;
		}

		@SuppressWarnings("unchecked")
		public <T> T build(Class<T> targetCls) {
			List<DefaultMethodHandler> defaultMethodHandlers = new LinkedList<DefaultMethodHandler>();

			for (Method method : targetCls.getMethods()) {
				if (method.getDeclaringClass() == Object.class) {
					continue;
				} else if (method.isDefault() && targetCls.isInterface()) {
					DefaultMethodHandler handler = new DefaultMethodHandler(method);
					defaultMethodHandlers.add(handler);
				}
			}

			HttpRequestHandler handler = new HttpRequestHandler(this.host);
			handler.setEncoder(this.encoder);
			handler.setDecoder(this.decoder);
			handler.setTimeout(this.timeout);

			ClassLoader clsLoader = MufasaClient.class.getClassLoader();

			T proxy = (T) Proxy.newProxyInstance(clsLoader, new Class<?>[] { targetCls }, handler);

			for (DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
				defaultMethodHandler.bindTo(proxy);
			}

			return proxy;
		}
		
	}

}
