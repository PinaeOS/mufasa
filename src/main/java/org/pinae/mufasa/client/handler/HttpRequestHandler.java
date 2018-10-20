package org.pinae.mufasa.client.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.pinae.mufasa.client.annotation.Body;
import org.pinae.mufasa.client.annotation.Headers;
import org.pinae.mufasa.client.annotation.Params;
import org.pinae.mufasa.client.annotation.Request;
import org.pinae.mufasa.client.codec.encoder.Encoder;
import org.pinae.mufasa.client.http.Http;
import org.pinae.mufasa.client.http.HttpClientRequest;
import org.pinae.mufasa.client.http.HttpClientResponse;
import org.pinae.mufasa.client.http.HttpClientUtils;

import com.alibaba.fastjson.JSON;

public class HttpRequestHandler implements InvocationHandler {
	
	private String host;

	private X509Certificate cert;
	
	private Encoder encoder;

	public HttpRequestHandler(String host) {
		this.host = host;
	}
	
	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
	
	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if (StringUtils.isBlank(this.host)) {
			throw new NullPointerException("Host is null");
		}
		
		if (this.encoder == null) {
			throw new NullPointerException("Response Encoder is null");
		}

		HttpClientRequest httpRequest = new HttpClientRequest(this.cert);
		
		if (this.host.startsWith(Http.HTTPS_PROTOCOL_PREFIX)) {
			httpRequest.setSsl(true);
		}

		Request methodRequest = method.getAnnotation(Request.class);

		httpRequest.setUrl(this.host + methodRequest.path());
		httpRequest.setMethod(methodRequest.method());

		if (method.isAnnotationPresent(Headers.class)) {
			Headers headerValues = method.getAnnotation(Headers.class);
			httpRequest.addHeaders(parseKeyValuePairs(headerValues.value()));
		}

		Parameter params[] = method.getParameters();
		
		for (int i = 0; i < params.length; i++) {
			Params param = params[i].getAnnotation(Params.class);
			if (param != null) {
				String paramKey = param.value();
				String paramValue = args[i].toString();

				httpRequest.addParameter(paramKey, paramValue);
			}

			Body body = params[i].getAnnotation(Body.class);
			if (body != null) {
				httpRequest.setContentType(body.contentType());
				httpRequest.setCharset(body.charset());

				Object value = args[i];
				
			}

		}

		HttpClientResponse httpResponse = HttpClientUtils.execute(httpRequest);

		Object executeResult = this.encoder.encode(httpResponse, method.getReturnType());

		return executeResult;
	}

	private Map<String, String> parseKeyValuePairs(String[] pairs) {
		
		Map<String, String> valueMap = new TreeMap<String, String>();
		for (String pair : pairs) {
			String separator = null;
			if (pair.contains(":")) {
				separator = ":";
			} else if (pair.contains("=")) {
				separator = "=";
			}
			
			if (separator != null) {
				String key = StringUtils.substringBefore(pair, separator);
				String value = StringUtils.substringAfter(pair, separator);
				if (key != null && value != null) {
					try {
						valueMap.put(key, value);
					} catch (NullPointerException e1) {
	
					}
				}
			}
		}
		
		return valueMap;
	}

}
