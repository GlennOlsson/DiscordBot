package commands;

import backend.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

public class Prefix {
	public Prefix(MessageChannel channel, MessageReceivedEvent event, String content, String newPrefix) {
		String[] roleslist ={"Moderator", "Commissioner", "Server Owner"};
		if(ReadWrite.isAuthorized(event.getTextChannel(), event, roleslist)){
			new Print("HALLELULIA", false);
			if(!newPrefix.equals("")){
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//if privatechannel ---> no guildId
					ReadWrite.setPrefix(channel.getId(),newPrefix);
				}
				else {
					//Not private --> has guild Id
					ReadWrite.setPrefix(event.getGuild().getId(),newPrefix);
				}
				channel.sendMessage("Prefix successfully changed to \""+newPrefix+"\"").queue();
			}
		}
		else {
			if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
				channel.sendMessage("Unauthorized. Current prefix: "+ReadWrite.getPrefix(channel.getId())).queue();
			}
			else{
				channel.sendMessage("Unauthorized. Current prefix: "+ReadWrite.getPrefix(event.getGuild().getId())).queue();
			}
		}
	}

	
}
