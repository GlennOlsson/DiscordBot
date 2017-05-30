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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import backend.*;
import net.dv8tion.jda.core.entities.MessageChannel;

public class DailyDose {

	public static void main(String[] args) {
		// FIXME Auto-generated method stub
		//		new DailyDose("aww", null);
	}
	public DailyDose(String subreddit, MessageChannel channel) {
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
			// FIXME: handle exception
			new ErrorLogg(e, "Error with DailyDose class", "Unknown error", null);
		}
	}
}
