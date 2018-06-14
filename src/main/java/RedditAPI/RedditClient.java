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

import backend.ReadWrite;
import com.sun.org.apache.regexp.internal.RE;
import commands.Reddit;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.RedditIterable;

import java.util.ArrayList;

public class RedditClient {
	
	private net.dean.jraw.RedditClient client;
	
	public RedditClient(){
		String username = "KakansBot";
		String password = ReadWrite.getKey("bot-pass").getAsString();
		String clientId = ReadWrite.getKey("client-id").getAsString();
		String clientSecret = ReadWrite.getKey("client-secret").getAsString();
		
		Credentials oauthCreds = Credentials.script(username, password, clientId, clientSecret);
		UserAgent userAgent = new UserAgent("bot", "se.glennolsson.KakansBot", "1.0.0", "KakansBot");
		
		this.client = OAuthHelper.automatic(new OkHttpNetworkAdapter(userAgent), oauthCreds);
	}
	
	
	public ArrayList<RedditPost> getTopPosts(String subreddit, int amount, SubredditSort sort, TimePeriod time){
		DefaultPaginator<Submission> page = client.subreddit(subreddit).posts().
									sorting(sort).
									timePeriod(time).
									limit(amount).build();
		
		Listing<Submission> postIterator = page.next();
		
		ArrayList<RedditPost> postList = new ArrayList<>(amount);

		int index = 0;
		
		postIterator.forEach(post -> {
			postList.add(new RedditPost(post));
		});
		
		return postList;
	}
	
	public RedditPost getPostWithID(String id){
		Submission submission = client.submission(id).inspect();
		RedditPost post = new RedditPost(submission);
		return post;
	}
	
	enum sortType{
		hour, day, week, month, year, all
	}
	
	
	public static void main(String[] args) {
	    RedditClient client = new RedditClient();
	    ArrayList<RedditPost> posts = client.getTopPosts("aww", 3, SubredditSort.TOP, TimePeriod.DAY);
	    
	    for(RedditPost post : posts){
		    System.out.println("**" + post.getTitle() + "** - " + post.getMediaUrl());
	    }
	}
}
