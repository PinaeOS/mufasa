package org.pinae.mufasa.client;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import org.pinae.mufasa.client.codec.encoder.Encoder;
import org.pinae.mufasa.client.codec.encoder.JsonEncoder;
import org.pinae.mufasa.client.handler.DefaultMethodHandler;
import org.pinae.mufasa.client.handler.HttpRequestHandler;

public class MufasaClient {
	
	@SuppressWarnings("unchecked")
	public static <T> T execute(Class<T> targetCls, String host, Encoder encoder) {
		List<DefaultMethodHandler> defaultMethodHandlers = new LinkedList<DefaultMethodHandler>();

		for (Method method : targetCls.getMethods()) {
			if (method.getDeclaringClass() == Object.class) {
				continue;
			} else if (method.isDefault() && targetCls.isInterface()) {
				DefaultMethodHandler handler = new DefaultMethodHandler(method);
				defaultMethodHandlers.add(handler);
			}
		}

		HttpRequestHandler handler = new HttpRequestHandler(host);
		handler.setEncoder(encoder);

		ClassLoader clsLoader = MufasaClient.class.getClassLoader();

		T proxy = (T) Proxy.newProxyInstance(clsLoader, new Class<?>[] { targetCls }, handler);
		
		for (DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
			defaultMethodHandler.bindTo(proxy);
		}
		
		return proxy;
	}

	public static <T> T execute(Class<T> targetCls, String host) {
		return execute(targetCls, host, new JsonEncoder());
	}

}
