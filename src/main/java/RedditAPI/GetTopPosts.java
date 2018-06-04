/*
 *
 *  * Copyright 2017 Glenn Olsson
 *  *
 *  * Permission is hereby granted, free of charge, to any
 *  * person obtaining a copy of this software and associated
 *  *  documentation files (the "Software"), to deal in the Software
 *  *  without restriction, including without limitation the rights to
 *  *  use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  *  sell copies of the Software, and to permit persons to whom
 *  *  the Software is furnished to do so, subject to the following
 *  *  conditions:
 *  *
 *  * The above copyright notice and this permission notice shall
 *  * be included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
 *  * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 *  * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *  * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 *  * OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package RedditAPI;

import backend.Logger;
import backend.ReadWrite;
import backend.Return;
import com.google.gson.JsonObject;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.misc.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GetTopPosts {
	
	enum sortType{
		hour, day, week, month, year, all
	}
	
	private static final String USER_AGENT = "KakansBot/1.0";
	
	private static String accessToken;
	
	private static String mainUrl = "http://oauth.reddit.com/r/";
	
	//Max of amount is 25
	//Logger.logError(e, "Error with getting top of /r/" + subreddit + " on type " + sort.name(), "Error in GetTopPost", null);
	public static RedditPost[] getTop(String subreddit, int amount, sortType sort) throws IOException{
		RedditPost[] posts = new RedditPost[amount];
		
		String url = mainUrl + subreddit + "/top.json?t=" + sort.name();
		
		//System.out.println(GET(url).length() + ", " + url);
		
		newAccessToken();
		
		for(int i = 0; i < amount; i++){
			
		}
		return posts;
	}
	
	public static void main(String[] args) throws IOException{
		getTop("aww", 3, sortType.day);
	}
	
	public static Response GET(String URL){
		try{
			
			//GET
			//Setting a timeout for 4 seconds
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(4 * 1000).build();
			HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			HttpGet request = new HttpGet(URL);
			
			// add request header
			request.addHeader("User-Agent", USER_AGENT);
			HttpResponse response = client.execute(request);
			
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer resultString = new StringBuffer();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				resultString.append(line);
			}
			
			Response result = new Response(new String(resultString), response.getStatusLine().getStatusCode());
			
			return result;
			
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static void newAccessToken(){
		String clientSecret = ReadWrite.getKey("client-secret").getAsString();
		String clientID = ReadWrite.getKey("client-id").getAsString();
		
		String auth64 = Base64.getEncoder().encodeToString((clientID + ":" + clientSecret).getBytes());
		
		String url = "https://www.reddit.com/api/v1/access_token";
		
		System.out.println(POST(url, auth64).getResponseString());
		
	}
	
	public static Response POST(String URL, String credentials) {
		try {
			//POST
			String url = URL;
			
			HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
			
			HttpPost post = new HttpPost(url);
			
			post.addHeader("User-Agent", USER_AGENT);
			post.addHeader("Authorization", "Basic " + credentials);
			
			
			System.out.println(post.getAllHeaders()[1]);
			
			String params = "grant_type=client_credentials";
			
			StringEntity body = new StringEntity(params, ContentType.APPLICATION_FORM_URLENCODED);
			post.setEntity(body);
			
			
			System.out.println(post.getRequestLine());
			
			HttpResponse response = client.execute(post);
			
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer resultString = new StringBuffer();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				resultString.append(line);
			}
			
			Response result = new Response(new String(resultString), response.getStatusLine().getStatusCode());
			
			return result;
			
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	
}

class Response {
	private int responseCode;
	private String responseString;
	
	public Response(String responseString, int responseCode){
		this.responseString = responseString;
		this.responseCode = responseCode;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
	public String getResponseString() {
		return responseString;
	}
}