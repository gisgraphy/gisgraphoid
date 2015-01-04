package com.gisgraphy.gisgraphoid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import android.util.Log;

import com.google.gson.Gson;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public class RestClient {
	{
		System.setProperty("http.agent", "gisgraphoid-1.0");
	}

	private static final String ENCODING = "UTF-8";
	private String webServiceUrl;

	public RestClient(String webServiceUrl) {
		if (webServiceUrl == null) {
			throw new IllegalArgumentException("can not create a restclient for null URL");
		}
		if (!webServiceUrl.endsWith("/")) {
			webServiceUrl = webServiceUrl + "/";
		}
		this.webServiceUrl = webServiceUrl;

	}

	public <T> T get(String methodName, Class<T> classToBeBound, Map<String, String> params) {
		if (methodName == null) {
			methodName = "";
		}
		if (methodName.startsWith("/")) {
			methodName = methodName.substring(1);
		}
		String getUrl = getWebServiceUrl() + methodName;

		int i = 0;
		StringBuffer sb = new StringBuffer(getUrl);
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}

				try {
					sb.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue(), ENCODING));
				} catch (UnsupportedEncodingException e) {
					Log.e("RestClient", "can not encode REST URL : " + e);
				}
				i++;
			}
		}
		String urlWithQueryString = sb.toString();
		InputStream in;
		Log.d("RestClient: ", "getUrl = " + urlWithQueryString);
		try {
			in = getRemoteContent(urlWithQueryString);
			T returnObjects = new Gson().fromJson(new BufferedReader(new InputStreamReader(in)), classToBeBound);
			// Log.d("result", String.valueOf(returnObjects));
			return returnObjects;

		} catch (IOException e) {
			String errorMessage = "Error during parsing of Gisgraphy response (has Gisgraphy API changed or feed is not json ?) : " + e;
			Log.d("RestClient", errorMessage, e);
			throw new RuntimeException(errorMessage,e);
		}

	}

	private InputStream getRemoteContent(String urlString) throws IOException {
		if (urlString == null) {
			throw new IOException("can not retrieve the content of a null url");
		}
		InputStream in = null;
		int responseCode = -1;

		URL url;
		try {
			url = new URL(urlString);
		} catch (Exception e) {
			throw new IOException(urlString + " is not a valid url");
		}
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		HttpURLConnection httpConn = (HttpURLConnection) conn;
		httpConn.setAllowUserInteraction(false);
		httpConn.setInstanceFollowRedirects(true);
		httpConn.setRequestMethod("GET");
		httpConn.connect();

		responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			in = httpConn.getInputStream();
			// String responseAsString = Helper.inputStreamToString(in);
		} else {
			String errorMessage = "calling " + urlString + " return an error : " + responseCode;
			Log.e("Restclient", errorMessage);
			throw new IOException(errorMessage);
		}

		return in;
	}

	public String getWebServiceUrl() {
		return webServiceUrl;
	}

}
