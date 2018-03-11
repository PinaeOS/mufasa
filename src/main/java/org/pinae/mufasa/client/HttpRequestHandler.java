package org.pinae.mufasa.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.pinae.mufasa.client.annotation.Body;
import org.pinae.mufasa.client.annotation.HeaderMap;
import org.pinae.mufasa.client.annotation.Headers;
import org.pinae.mufasa.client.annotation.Params;
import org.pinae.mufasa.client.annotation.Request;

import com.alibaba.fastjson.JSON;

public class HttpRequestHandler implements InvocationHandler {

	private static Logger logger = Logger.getLogger(HttpRequestHandler.class);

	private boolean ssl;

	private String url;

	private X509Certificate cert;

	public HttpRequestHandler(boolean ssl, String url, X509Certificate cert) {
		this.url = url;
		this.cert = cert;
		this.ssl = ssl;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Request httpRequest = method.getAnnotation(Request.class);

		String uri = httpRequest.uri();
		String httpMethod = httpRequest.method();

		Map<String, String> headers = new TreeMap<String, String>();
		
		Map<String, String> parameters = new TreeMap<String, String>();
		
		String contentType = null;
		String charset = null;
		String content = null;
		
		if (method.isAnnotationPresent(Headers.class)) {
			Headers headerValues = method.getAnnotation(Headers.class);
			headers.putAll(parseHeader(headerValues.value()));
		}

		Parameter params[] = method.getParameters();
		for (int i = 0; i < params.length; i++) {
			Params param = params[i].getAnnotation(Params.class);
			if (param != null) {
				String paramName = param.value();
				String paramValue = args[i].toString();

				parameters.put(paramName, paramValue);
			}
			
			Body body = params[i].getAnnotation(Body.class);
			if (body != null) {
				contentType = body.contentType();
				charset = body.charset();
				
				Object value = args[i];
				if (value instanceof String) {
					content = value.toString();
				} else {
					if (contentType.equals(Http.APPLICATION_JSON)) {
						content = JSON.toJSONString(value);
					} else {
						content = value.toString();
					}
				}
			}
			
			HeaderMap headerMap = method.getAnnotation(HeaderMap.class);
			if (headerMap != null) {
				Object value = args[i];
				if (value instanceof Map) {
					try {
						headers.putAll((Map<String, String>)value);
					} catch (Exception e) {
						
					}
				}
			}
		}
		
		String reqUrl = this.url + uri;
		HttpClientResponse response = execute(httpMethod, reqUrl, headers, parameters, contentType, charset, content);

		return null;
	}
	
	private Map<String, String> parseHeader(String[] headerValues) {
		Map<String, String> headerMap = new TreeMap<String, String>();
		for (String headerValue : headerValues) {
			String val[] = headerValue.split(":");
			if (val != null && val.length == 2) {
				try {
					headerMap.put(val[0].trim(), val[1].trim());
				} catch (NullPointerException e1) {

				}
			}
		}
		return headerMap;
	}

	private HttpClientResponse execute(String method, String url, Map<String, String> headers,
			Map<String, String> parameters, String contentType, String charset, String content)
					throws HttpClientException {
		HttpClient client = null;
		if (this.ssl) {
			client = createSSLClientDefault();
		} else {
			client = HttpClients.createDefault();
		}

		if (method == null) {
			throw new NullPointerException("Http Method is null");
		}

		if (url == null) {
			throw new NullPointerException("Host or Service Uri is null");
		}

		HttpUriRequest httpRequest = null;

		URIBuilder uriBuilder = null;
		try {
			uriBuilder = new URIBuilder(url);
			if (parameters != null && parameters.size() > 0) {
				Set<String> keySet = parameters.keySet();
				for (String key : keySet) {
					String value = parameters.get(key);
					uriBuilder.setParameter(key, value);
				}
			}

			if (uriBuilder != null) {
				URI uri = uriBuilder.build();
				if (HttpGet.METHOD_NAME.equals(method)) {
					httpRequest = new HttpGet(uri);
				} else if (HttpPost.METHOD_NAME.equals(method)) {
					httpRequest = new HttpPost(uri);
					if (content != null) {
						HttpEntity entity = new StringEntity((String) content, ContentType.create(contentType, charset));
						HttpPost httpPost = (HttpPost) httpRequest;
						if (entity != null) {
							httpPost.setEntity(entity);
						}
					}
				} else if (HttpPut.METHOD_NAME.equals(method)) {
					httpRequest = new HttpPut(uri);
				} else if (HttpDelete.METHOD_NAME.equals(method)) {
					httpRequest = new HttpDelete(uri);
				} else if (HttpTrace.METHOD_NAME.equals(method)) {
					httpRequest = new HttpTrace(uri);
				} else if (HttpOptions.METHOD_NAME.equals(method)) {
					httpRequest = new HttpOptions(uri);
				} else {
					throw new HttpClientException(String.format("Unknown HTTP Method : %s", method));
				}

				if (httpRequest != null) {

					if (headers != null) {
						Set<String> headerKeySet = headers.keySet();
						for (String headerKey : headerKeySet) {
							String headerValue = headers.get(headerKey);
							if (headerValue != null) {
								httpRequest.setHeader(headerKey, headerValue);
							} else {
								httpRequest.removeHeaders(headerKey);
							}
						}
					}
				}
			}
		} catch (URISyntaxException e) {
			throw new HttpClientException(e);
		}

		try {
			HttpClientResponse response = new HttpClientResponse();

			HttpResponse httpResponse = client.execute(httpRequest);

			HttpEntity entity = httpResponse.getEntity();

			StatusLine statusLine = httpResponse.getStatusLine();
			response.setStatusCode(statusLine.getStatusCode());

			ContentType respContentType = ContentType.getOrDefault(entity);
			if (respContentType != null) {
				response.setContentType(respContentType.getMimeType());
			}
			Charset respCharset = respContentType.getCharset();
			if (respCharset != null) {
				response.setCharset(respCharset.displayName());
			}
			response.setContent(entity.getContent());

			Header[] respHeaders = httpResponse.getAllHeaders();
			for (Header respHeader : respHeaders) {
				response.setHeader(respHeader.getName(), respHeader.getValue());
			}

			return response;

		} catch (ClientProtocolException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
	}

	private CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					if (cert != null) {
						for (X509Certificate chainCert : chain) {
							if (chainCert.equals(cert)) {
								return true;
							}
						}
						return false;
					}
					return true;
				}
			}).build();
			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(factory).build();
		} catch (GeneralSecurityException e) {
			logger.error(String.format("SSL connection create Fail : %s", e.getMessage()));
		}
		return HttpClients.createDefault();
	}

}
