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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Clean {
	public static void Clean(MessageChannel messageChannel, MessageReceivedEvent event, String content){

		for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
			if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
				//Is KakansBot
				if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
					Logger.print("Clean command, but I don't have MESSAGE_MANAGE permission in "+event.getChannel().getName()
							+" channel in "+event.getGuild().getName()+" guild");
					messageChannel.sendMessage("I can't delete the message, I need MESSAGE_MANAGE permission for that").queue();
				return;
				}
				i=event.getTextChannel().getMembers().size()+5;
			}
		}

		TextChannel channel = event.getTextChannel();

		if(ReadWrite.isAuthorized(event, Permission.MESSAGE_MANAGE)){
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
					
					event.getAuthor().openPrivateChannel().complete().sendMessage("Error in argument, deleting one").queue();
					Logger.logError(e, content, event.getMessage().getId(), event);
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
					Logger.print(argument2 + " == arg 2");
					boolean hasMember=false;
					for (int i = 0; i < channel.getMembers().size(); i++) {
						if(channel.getMembers().get(i).getUser().getName().toLowerCase().equals(argument2)){
							Logger.print("CHANNEL HAS USER");
							hasMember=true;
						}
					}
					if(!hasMember){
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
				channel.sendMessage("Error, contact "+event.getJDA().retrieveUserById("165507757519273984").complete().getAsMention()+" with id: "+event.getMessage().getId());
				Logger.logError(e, content, event.getMessage().getId(), event);
			}
			event.getMessage().delete().queue();
			for (int i = 1; i < amount+1; i++) {
				switch (argument1) {
					case "bots":
						if(historyList.get(i).getAuthor().isBot()) {
							if(argument2.equals("all")) {
								historyList.get(i).delete().queue();
							} else {
								if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)) {
									historyList.get(i).delete().queue();
								} else {
									amount++;
								}
							}
						} else {
							amount++;
						}
						break;
					case "users":
						if(!historyList.get(i).getAuthor().isBot()) {
							if(argument2.equals("all")) {
								historyList.get(i).delete().queue();
							} else {
								if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)) {
									historyList.get(i).delete().queue();
								} else {
									amount++;
								}
							}
						} else {
							amount++;
						}
						break;
					default:
						//if all user's messages shall go
						if(argument2.equals("all")) {
							historyList.get(i).delete().queue();
						} else {
							if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)) {
								historyList.get(i).delete().queue();
							} else {
								amount++;
							}
						}
						break;
				}
			}
			Logger.printError(amount+" = amount");
		}
		else {
			//				channel.sendMessage("You are not authorized to execute that command, *"+event.getAuthor().getAsMention()+"*. Contact a Moderator").queue();
			//noinspection UnnecessaryReturnStatement
			return;
		}
	}

}
