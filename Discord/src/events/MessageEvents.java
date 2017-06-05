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

package events;

import backend.ErrorLogg;
import backend.Print;
import backend.ReadWrite;
import commands.*;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Created by Glenn on 2017-06-04.
 */
public class MessageEvents {
	
	public static void MessageReceived(MessageReceivedEvent event){
		try{
			if(!event.getAuthor().equals(event.getJDA().getSelfUser())){
				String  contentCase=event.getMessage().getContent(), content = event.getMessage().getContent().toLowerCase();
				MessageChannel channel = event.getChannel();
				
				channel.sendTyping();
				
				if(content.toLowerCase().equals("prefix")){
					if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
						//Private channel
						channel.sendMessage("The current prefix on our private chat is :\""+ ReadWrite.getPrefix(channel.getId())+"\"").queue();
					}
					else{
						//Not private
						channel.sendMessage("The current prefix on "+event.getGuild().getName()+" is :\""+ReadWrite.getPrefix(event.getGuild().getId())+"\"").queue();
					}
				}
				//Reddit command
				if((content.contains("://reddit")||content.contains("://www.reddit"))&&(content.substring(0,"https://www.reddit".length()).contains("https://www.reddit")||
						content.substring(0,"http://www.reddit".length()).contains("http://www.reddit")||
						content.substring(0,"https://reddit".length()).contains("https://reddit")||
						content.substring(0,"http://reddit".length()).contains("http://reddit"))){
					try {
						new Reddit(channel, event, content);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with reddit command", event);
					}
					
					return;
				}
				
				//Gets prefix
				String prefix;
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//if privateChannel ---> no guildId
					prefix=ReadWrite.getPrefix(channel.getId());
				}
				else {
					//Not private --> has guild Id
					prefix=ReadWrite.getPrefix(event.getGuild().getId());
				}
				try {
					onMessageReceivedPrefix(event, prefix, content, channel, contentCase);
				} catch (Exception e) {
					new ErrorLogg(e, "Prefix: "+prefix+", content: "+content, "Error with onMessageReceivedPrefix for "+channel.getName()+ " channel", event);
				}
			}
		}catch (Exception e) {
			new ErrorLogg(e, "Error with onMessageReceivedEvent","Unknown error", event);
		}
	}
	private static void onMessageReceivedPrefix(MessageReceivedEvent event, String prefix, String content, MessageChannel channel,
	                                     String contentCase) {
		// Made so that it can check ; as a prefix, if the first check fails. This way, ; is always a prefix
		
		if(content.length()>prefix.length()&&content.substring(0,prefix.length()).equals(prefix)){
			//; commands
			
			String command = content.substring(prefix.length()), afterCommand = "";
			if(command.contains(" ")){
				command=command.split(" ")[0];
				afterCommand = contentCase.substring(prefix.length()+command.length()+1);
				new Print("Aftercommand=\""+afterCommand+"\"", false);
			}
			
			switch (command) {
				case "clean":
					try {
						new Clean(channel, event, content);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with clean command", event);
					}
					break;
				
				case "gif":
					try {
						new Gif(channel, event, afterCommand);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with gif command", event);
					}
					break;
				
				case "source":
					try {
						new Source(channel);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with source command", event);
					}
					break;
				
				case "prefix":
					try {
						new Prefix(channel, event, afterCommand);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with prefix command", event);
					}
					break;
				
				case "game":
					try {
						if(!event.getChannelType().equals(ChannelType.PRIVATE)) {
							new GameRoles(channel, event, afterCommand);
						}
					}
					catch (Exception e){
						new ErrorLogg(e, content, "Error with game command", event);
					}
					break;
				
				case "restart":
					try {
						new Restart(channel, event);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with Restart command", event);
					}
					break;
				
				case "editgame":
					try {
						if(!event.getChannelType().equals(ChannelType.PRIVATE)) {
							GameRoles.editRoles(event, channel, afterCommand);
						}
					}
					catch (Exception e){
						new ErrorLogg(e, content, "Error with editgame command", event);
					}
					break;
				case "help":
					
					try {
						new Help(event, content);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with help command", event);
					}
					break;
			}
			
			//				else{
			//					channel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
			//				}
		}
		else {
			if(!prefix.equals(";")){
				//Call event but with ; as prefix
				onMessageReceivedPrefix(event, ";", content, channel, contentCase);
			}
		}
	}
	
	public static void PrivateMessage(PrivateMessageReceivedEvent event){
		try{
			PrivateChannel channel=event.getChannel();
			String content = event.getMessage().getContent();
			
			if(!event.getAuthor().equals(event.getJDA().getSelfUser())){
				
				channel.sendTyping();
				
				//Already checked if private
				String prefix=ReadWrite.getPrefix(channel.getId());
				
				if(content.equals("prefix")){
					//noinspection UnnecessaryReturnStatement
					return;
				}
				
				else if(content.length()>=prefix.length()){
					if(!content.substring(0, prefix.length()).equals(prefix)&&!Character.toString(content.charAt(0)).equals(";")){
						channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
								+ "commands starting with "+prefix+". You can use "+prefix+"help for example, to see what commands you can use."
								+ " Uppercase or lowercase does not matter").queue();
						//noinspection UnnecessaryReturnStatement
						return;
					}
				}
				else {
					if(!Character.toString(content.charAt(0)).equals(";"))
						channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
								+ "commands starting with "+prefix+". You can use \""+prefix+"help\" for example, to see what commands you can use."
								+ " Uppercase or lowercase does not matter").queue();
					//noinspection UnnecessaryReturnStatement
					return;
				}
			}
		}catch (Exception e) {
			new ErrorLogg(e, "Content: "+event.getMessage().getContent()+ ", Author: "+event.getAuthor().getName() + "#"+event.getAuthor().getDiscriminator()+
					", channel: +"+event.getChannel().getName()+", MessageID: "+event.getMessage().getId(), "Unknown error in onPrivateMessageReceived", null);
		}
	}
}
