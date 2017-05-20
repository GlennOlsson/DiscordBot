/*
 * Copyright 2017 Glenn Olsson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
