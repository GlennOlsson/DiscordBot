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
import commands.Reddit;
import net.dean.jraw.models.Submission;

public class RedditPost {
	
	private String mediaUrl;
	private String title;
	private String postUrl;
	private boolean isTextpost;
	
	public RedditPost(String mediaUrl, String title, String postUrl, boolean isTextpost) {
		this.mediaUrl = mediaUrl;
		this.title = title;
		this.postUrl = postUrl;
		this.isTextpost = isTextpost;
	}
	
	public RedditPost(Submission submission){
		this.mediaUrl = submission.getUrl();
		this.title = submission.getTitle();
		this.postUrl = submission.getPermalink();
		this.isTextpost = submission.isSelfPost();
	}
	
	public boolean isTextpost() {
		return isTextpost;
	}
	
	public String getMediaUrl() {
		return mediaUrl;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getPostUrl() {
		return postUrl;
	}
}
