package backend;

import java.net.URI;
import java.net.URL;

import org.jsoup.Jsoup;

public class Return {
	public static String convertUrl(String urlToConvert){
		try {
			//If this works, then the first provided url works
			Jsoup.connect(urlToConvert).userAgent("Chrome").get();
			return urlToConvert;
		} catch (Exception e) {
			//If provided url does not work
			try {
				URL url = new URL(urlToConvert);
				URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

				return uri.toASCIIString();
			} catch (Exception e2) {
				// FIXME: handle exception
				new Logg(e2, "Error in convertUrl", "Error with URL -> URI  -> URI.toASCIIString", null);
			}

		}
		return null;


	}


}
