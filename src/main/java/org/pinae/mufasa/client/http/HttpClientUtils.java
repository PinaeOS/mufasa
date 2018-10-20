package org.pinae.mufasa.client.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

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

public class HttpClientUtils {
	
	private static Logger logger = Logger.getLogger(HttpClientUtils.class);
	
	public static HttpClientResponse execute(HttpClientRequest request)
					throws HttpClientException {
		
		HttpClient client = null;
		if (request.isSsl()) {
			client = createSSLClientDefault(request.getCert());
		} else {
			client = HttpClients.createDefault();
		}
		
		String url = request.getUrl();
		
		String method = request.getMethod();
		
		Map<String, String> headers = request.getHeaders();
		
		Map<String, String> parameters = request.getParameters();
		
		String contentType = request.getContentType();
		
		String charset = request.getCharset();
		
		String content = request.getContent();
		
		if (url == null) {
			throw new NullPointerException("Request URL is null");
		}

		if (method == null) {
			throw new NullPointerException("Request Method is null");
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
						HttpEntity entity = new StringEntity(content, ContentType.create(contentType, charset));
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
			response.setContentStream(entity.getContent());

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

	private static CloseableHttpClient createSSLClientDefault(final X509Certificate cert) {
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
