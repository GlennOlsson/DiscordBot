package commands;

import org.jsoup.*;
import org.jsoup.nodes.*;

import backend.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

public class Gif {
	public Gif(MessageChannel channel, MessageReceivedEvent event, String content) {
		Document doc=null;
		String url=null, query=null;
		try {
			query = content.substring(5, content.length()).replace(" ", "-");

			doc = Jsoup.connect(Return.convertUrl("https://www.tenor.co/search/"+query+"-gifs")).userAgent("Chrome").get();
			url = "https://www.tenor.co/"+doc.select("#view > div > div.center-container.search > div > div > div:nth-child(1) > figure:nth-child(1) > a").attr("href");

			channel.sendMessage("*"+event.getAuthor().getName()+"* shared a .gif of *'"+query.replace("-", " ") + "'*: " +url).queue();
			try {
				if(!event.getChannel().getType().equals(ChannelType.PRIVATE)){
					event.getMessage().delete().queue();;
				}
			} catch (Exception e) {
				// FIXME: handle exception
				event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
				new Logg(e, content, event.getMessage().getId(), event);
			}

		} catch (Exception e) {
			// FIXME Auto-generated catch block
			channel.sendMessage("Error with ;gif command. Use ';help gif' to get help with the command, wither here or in PM").queue();

			new Logg(e, content, event.getMessage().getId(), event);

		}
		return;

	}

	
}
