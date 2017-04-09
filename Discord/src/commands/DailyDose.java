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
			
			channel.sendMessage("Here's your daily dose of /r/"+ subreddit).queue();

			doc = Jsoup.connect(Return.convertUrl("https://reddit.com/r/"+subreddit.toLowerCase()+"/top/?sort=top&t=day")).userAgent("Chrome").get();
			for (int i = 0; i < 3; i++) {
				channel.sendMessage("**"+doc.select(".entry > .title > a").get(i).html() + "** "+doc.select(".thing").get(i).attr("data-url")).queue();
			}			
		}
		catch (Exception e) {
			// FIXME: handle exception
			new Logg(e, "Error with DailyDose class", "Unknown error", null);
		}
	}
}
