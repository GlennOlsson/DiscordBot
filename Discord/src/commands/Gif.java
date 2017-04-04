package commands;

import org.jsoup.*;
import org.jsoup.nodes.*;

import backend.*;
import net.dv8tion.jda.core.Permission;
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
					//Check if I have MESSAGE_MANAGE permission, before trying to delete

					for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
						if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
							//Is KakansBot
							if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
								new Print("Cannot delete initial Gif command message in "+event.getChannel().getName()
										+" channel in "+event.getGuild().getName()+" guild, because lack of MESSAGE_MANAGE", false);
							}
							else {
								event.getMessage().delete().queue();
							}
							i=event.getTextChannel().getMembers().size()+5;
						}
					}

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
