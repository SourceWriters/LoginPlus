package com.syntaxphoenix.loginplus.premium;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConnection {
	public static URL getConnection(String url) {
		try {
			return new URL(url); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HttpURLConnection setConnection(URL url) throws IOException {
	    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	    connection.setConnectTimeout(15000);
	    connection.setReadTimeout(15000);
	    connection.setUseCaches(true);
	    return connection;
	}
}
