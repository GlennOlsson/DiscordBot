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

public class RedditPost {
	
	private String mediaUrl;
	private String title;
	private String postUrl;
	
	public RedditPost(String jsonString){
		JsonObject json = ReadWrite.parseStringToJSON(jsonString);
		
		//If is link
		if(json.has("kind") && json.get("kind").equals("t3")){
			JsonObject dataJson = json.getAsJsonObject("data");
			title = dataJson.get("title").getAsString();
			mediaUrl = dataJson.get("url").getAsString();
			postUrl = dataJson.get("permalink").getAsString();
		}
		else{
			Logger.print("Not a link input. Either does not have \"kind\" field or it is not equal to t3");
			throw new IllegalArgumentException("Not a link");
		}
		
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
