package commands;

import java.util.List;

import backend.*;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

public class Clean {
	public Clean(MessageChannel messageChannel, MessageReceivedEvent event, String content){

		for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
			if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
				//Is KakansBot
				if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
					new Print("Clean command, but I don't have MESSAGE_MANAGE permission in "+event.getChannel().getName()
							+" channel in "+event.getGuild().getName()+" guild", false);
					messageChannel.sendMessage("I can't delete the message, I need MESSAGE_MANAGE permission for that").queue();
				return;
				}
				i=event.getTextChannel().getMembers().size()+5;
			}
		}

		TextChannel channel =event.getTextChannel();
		String[] roleList ={"Moderator", "Commissioner", "Server Owner"};

		if(ReadWrite.isAuthorized(channel, event, content, roleList)){
			int amount = 1;
			String argument1="all", argument2="all";

			//Arguments
			if(content.contains(" ")){
				try {
					amount=Integer.parseInt(content.split(" ")[1]);
					if (amount>100) {
						amount=100;
					}
				} catch (Exception e) {
					// FIXME: handle exception

					event.getAuthor().getPrivateChannel().sendMessage("Error in argument, deleting one").queue();
					new Logg(e, content, event.getMessage().getId(), event);
				}
				if(content.split(" ").length>=3){
					//If at least length=3
					if(content.split(" ")[2].toLowerCase().equals("bots")||content.split(" ")[2].toLowerCase().equals("users")||
							content.split(" ")[2].toLowerCase().equals("all")){
						argument1=content.split(" ")[2].toLowerCase();
					}

				}
				if(content.split(" ").length>=4){
					argument2=content.substring(content.indexOf(content.split(" ")[3].toLowerCase()));
					new Print(argument2 + " == arg 2", false);
					Boolean hasMemeber=false;
					for (int i = 0; i < channel.getMembers().size(); i++) {
						if(channel.getMembers().get(i).getUser().getName().toLowerCase().equals(argument2)){
							new Print("CHANNEL HAS USER", false);
							hasMemeber=true;
						}
					}
					if(!hasMemeber){
						channel.sendMessage("This channel does not have a user with that name. Aborting command").queue();
						return;
					}
				}
			}

			MessageHistory history = channel.getHistory();
			channel.getHistory().retrievePast(100).queue();
			List<Message> historyList = null;
			try {
				historyList=history.retrievePast(100).complete(true);
			} catch (Exception e) {
				// FIXME Auto-generated catch block
				channel.sendMessage("Error, contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id: "+event.getMessage().getId());
				new Logg(e, content, event.getMessage().getId(), event);
			}
			event.getMessage().delete().queue();
			for (int i = 1; i < amount+1; i++) {
				if(argument1.equals("bots")){
					if(historyList.get(i).getAuthor().isBot()){
						if(argument2.equals("all")){
							historyList.get(i).delete().queue();
						}
						else {
							if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
								historyList.get(i).delete().queue();
							}
							else {
								amount++;
							}
						}
					}
					else{
						amount++;
					}
				}
				else if (argument1.equals("users")) {
					if(!historyList.get(i).getAuthor().isBot()){
						if(argument2.equals("all")){
							historyList.get(i).delete().queue();
						}
						else {
							if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
								historyList.get(i).delete().queue();
							}
							else {
								amount++;
							}
						}
					}
					else{
						amount++;
					}
				}
				else {
					//if all user's messages shall go
					if(argument2.equals("all")){
						historyList.get(i).delete().queue();
					}
					else {
						if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
							historyList.get(i).delete().queue();
						}
						else {
							amount++;
						}
					}
				}
			}
			new Print(amount+" = amount", true);
		}
		else {
			//				channel.sendMessage("You are not authorized to execute that command, *"+event.getAuthor().getAsMention()+"*. Contact a Moderator").queue();
			return;
		}
	}

}
