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
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class DailyDose {
	@SuppressWarnings("WeakerAccess")
	long recentlyChecked = 0;
	private DailyDose(@SuppressWarnings("SameParameterValue") String subreddit, MessageChannel channel) {
		//Connect to reddit.com/r/*subreddit*
		Document doc;
		try {
//			channel=channel.getJDA().getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);
			channel.sendMessage("Here's your daily dose of /r/"+ subreddit).queue();

			doc = Jsoup.connect(Return.convertUrl("https://reddit.com/r/"+subreddit.toLowerCase()+"/top/?sort=top&t=day")).userAgent("Chrome").get();
			for (int i = 0; i < 3; i++) {
				channel.sendMessage("**"+doc.select(".entry > .title > a").get(i).html() + "** "+doc.select(".thing").get(i).attr("data-url")).queue();
			}			
		}
		catch (Exception e) {
			new ErrorLogg(e, "Error with DailyDose class", "Unknown error", null);
		}
	}
	public DailyDose(JDA jda) {
		if(System.currentTimeMillis() >= (recentlyChecked + 86400000)) {
			recentlyChecked =System.currentTimeMillis();
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
					for (int i = 0; i < jda.getTextChannelsByName("aww", true).size(); i++) {
						new DailyDose("aww", jda.getTextChannelsByName("aww", true).get(i));
					}
					ReadWrite.setKey("dailyMs", Long.toString(System.currentTimeMillis()));
				}
			} catch (Exception e) {
				new ErrorLogg(e, "Error in onEvent", "Unknown error caught", null);
			}
		}
	}
}
