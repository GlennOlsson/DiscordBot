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


import backend.Logger;

import backend.ReadWrite;
import backend.Return;
import com.sun.org.apache.regexp.internal.RE;
import main.Test;
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
		
		Logger.print("DailyDose!");
		
		Document doc;
		ArrayList<Message> messages = new ArrayList<>();
		try {
			//channel=channel.getJDA().getTextChannelById(Test.idKakanisatanGeneral);
			
			doc = Jsoup.connect(Return.convertUrl("https://reddit.com/r/"+subreddit.toLowerCase()+"/top/?sort=top&t=day")).userAgent("Chrome").get();
			for (int i = 1; i < 6; i+=2) {
				
				String urlOfPost =  doc.select(".thing:nth-of-type("+(i)+") > div.entry.unvoted > div.top-matter > ul > li.first > a")
						.attr("href");
				
				String[] mediaURLAndTitleOfPost = new Reddit().getRedditMediaURLAndTitle(Return.convertUrl(urlOfPost));
				
				String mediaURLofPost = mediaURLAndTitleOfPost[0];
				String titleOfPost = mediaURLAndTitleOfPost[1];
				
				Logger.print("Daily dose "+(i+1)/2 +": " + Return.convertUrl(urlOfPost));
				
				MessageBuilder message = new MessageBuilder();
				
				message.append("**");
				message.append(titleOfPost);
				message.append("** - ");
				message.append(mediaURLofPost);
				
				messages.add(message.build());
				
			}
			
			channel.sendMessage("Here's your daily dose of /r/"+ subreddit).queue();
			
			for(Message message : messages){
				channel.sendMessage(message).submit();
			}
			
		}
		catch (Exception e) {
			Logger.logError(e, "Error with DailyDose class", "Unknown error", null);
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
					Logger.print("Error with converting string -> long. Returning method and setting dailyMs JSON key to currentTimeMillis");
					ReadWrite.setKey("dailyMs", Long.toString(System.currentTimeMillis()));
					return;
				}
				if(System.currentTimeMillis() >= (lastMs + 86400000)) {
					for (int i = 0; i < jda.getTextChannelsByName("aww", true).size(); i++) {
						new DailyDose("aww", jda.getTextChannelsByName("aww", true).get(i));
					}
					ReadWrite.setKey("dailyMs", Long.toString(System.currentTimeMillis()));
				}
			} catch (Exception e) {
				Logger.logError(e, "Error in onEvent", "Unknown error caught", null);
			}
		}
	}
}
