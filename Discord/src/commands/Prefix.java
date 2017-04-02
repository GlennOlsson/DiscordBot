package commands;

import main.IO;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

public class Prefix {
	public Prefix(MessageChannel channel, MessageReceivedEvent event, String content, String newPrefix) {
		String[] roleslist ={"Moderator", "Commissioner", "Server Owner"};
		if(IO.isAuthorized(event.getTextChannel(), event, content, roleslist)){
			IO.print("HALLELULIA", false);
			if(!newPrefix.equals("")){
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//if privatechannel ---> no guildId
					IO.setPrefix(channel.getId(),newPrefix);
				}
				else {
					//Not private --> has guild Id
					IO.setPrefix(event.getGuild().getId(),newPrefix);
				}
				channel.sendMessage("Prefix successfully changed to \""+newPrefix+"\"").queue();
			}
		}
		else {
			if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
				channel.sendMessage("Unauthorized. Current prefix: "+IO.getPrefix(channel.getId())).queue();
			}
			else{
				channel.sendMessage("Unauthorized. Current prefix: "+IO.getPrefix(event.getGuild().getId())).queue();
			}
		}
	}

	
}
