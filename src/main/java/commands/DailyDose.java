/*
 * Copyright 2017 Glenn Olsson
 *
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software
 *  without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom
 *  the Software is furnished to do so, subject to the following
 *  conditions:
 *
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
 * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package commands;

import backend.ErrorLogg;
import backend.Print;
import backend.ReadWrite;
import backend.Return;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class DailyDose {
	@SuppressWarnings("WeakerAccess")
	
	long recentlyChecked = 0;
	private DailyDose(@SuppressWarnings("SameParameterValue") String subreddit, MessageChannel channel) {
		//Connect to reddit.com/r/*subreddit*
		new Print("Daily dose!");
		Document doc;
		ArrayList<Message> messages = new ArrayList<>();
		try {
			channel=channel.getJDA().getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);
			
			doc = Jsoup.connect(Return.convertUrl("https://reddit.com/r/"+subreddit.toLowerCase()+"/top/?sort=top&t=day")).userAgent("Chrome").get();
			for (int i = 0; i < 3; i++) {
				
				Element postTitleElement = doc.select("div.entry.unvoted > div.top-matter > p.title > a").get(i);
				
				new Print("Daily dose "+(i+1) +": " + Return.convertUrl(
						"https://reddit.com"+postTitleElement.attr("href")));
				
				String postUrl;
				
				if(postTitleElement.attr("href").contains("/r/aww")){
					Document currentPost = Jsoup.connect(Return.convertUrl(
							"https://reddit.com"+postTitleElement.attr("href"))).userAgent("Chrome").get();
					
					postUrl = currentPost.select("div.entry.unvoted > div.top-matter > p.title > a").attr("href");
				}
				else{
					postUrl=postTitleElement.attr("href");
				}
				
				MessageBuilder message = new MessageBuilder();
				
				message.append("**");
				message.append(postTitleElement);
				message.append("**");
				message.append(postUrl);
				
				messages.add(message.build());
				
			}
			
			channel.sendMessage("Here's your daily dose of /r/"+ subreddit).queue();
			
			new Print("Size of messages: "+messages.size());
			
			for(Message message : messages){
				channel.sendMessage(message).submit();
				new Print("Daily Dose message: "+message.getContent());
			}
			
		}
		catch (Exception e) {
			new ErrorLogg(e, "Error with DailyDose class", "Unknown error", null);
		}
	}
	public DailyDose(JDA jda) {
		if(System.currentTimeMillis() >= (recentlyChecked + 86400000)) {
			recentlyChecked = System.currentTimeMillis();
			try {
				String lastMsString = ReadWrite.getKey("dailyMs");
				if(lastMsString == null || lastMsString.equals("")) {
					ReadWrite.setKey("dailyMs", "0");
					return;
				}
				long lastMs;
				try {
					lastMs = Long.parseLong(lastMsString);
				} catch (Exception e) {
					new Print("Error with converting string -> long. Returning method and setting dailyMs JSON key to currentTimeMillis", false);
					ReadWrite.setKey("dailyMs", Long.toString(System.currentTimeMillis()));
					return;
				}
				if(System.currentTimeMillis() >= (lastMs + 86400000)) {
					ReadWrite.setKey("dailyMs", Long.toString(System.currentTimeMillis()));
					for (int i = 0; i < jda.getTextChannelsByName("aww", true).size(); i++) {
						new DailyDose("aww", jda.getTextChannelsByName("aww", true).get(i));
					}
				}
			} catch (Exception e) {
				new ErrorLogg(e, "Error in onEvent", "Unknown error caught", null);
			}
		}
	}
}
