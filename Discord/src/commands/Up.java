package commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

public class Up {
	public Up(MessageChannel channel, MessageReceivedEvent event, String content){
		channel.sendMessage("Yes, I am online. I am on the following channels: ").queue();
		for (int i = 0; i < event.getJDA().getTextChannels().size(); i++) {
			channel.sendMessage(event.getJDA().getTextChannels().get(i).getName() + " - "+channel.getJDA().getTextChannels().get(i).getGuild().getName()).queue();
		}
		return;


	}

	
}
