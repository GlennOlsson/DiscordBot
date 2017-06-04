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

package main;
/* ----------TODO
Make a script that automatically:
cd DiscordBot/DiscordBot/Discord
git pull
sh compile.sh
tmux attach
^C
run.sh

*/


import commands.*;
import backend.*;
import commands.GameRoles;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.events.message.priv.*;
import net.dv8tion.jda.core.hooks.*;


public class DiscordBot extends ListenerAdapter{
	
	public static void main(String[] args) {
		try {
			//		new Test();
			JDA jda = null;
			try {
				jda = new JDABuilder(AccountType.BOT).setToken(ReadWrite.getKey("oath")).addListener(new DiscordBot()).buildBlocking();
			} catch (Exception e) {
				new ErrorLogg(e, "JDA Builder", "JDA Builder", null);
			}
			TextChannel channel=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);
			
			channel.sendMessage("Sucessfully logged in!").queue();
			
			try{
				if(System.getProperty("os.name").toLowerCase().contains("linux")){
					new Print("\n		New run: Nr. "+Integer.toString(
							Integer.parseInt(ReadWrite.getKey("runCount"))+1)+"\n", null);
					ReadWrite.setKey("runCount", Integer.toString(Integer.parseInt(ReadWrite.getKey("runCount"))+1));
				}
			}catch (Exception e) {
				// FIXME: handle exception
				//Probably could not convert string -> int
				new ErrorLogg(e, "Error in Main", "Probably error with String -> int. runCount: \""+
						ReadWrite.getKey("runCount")+"\"", null);
			}
		} catch (Exception e) {
			new ErrorLogg(e, "Error in Main", "Unkown error", null);
		}
	}
	
	public DiscordBot(){}
	
	public void onReady(ReadyEvent event) {
		try {
			super.onReady(event);
			event.getJDA().getPresence().setGame(Game.of("Send ;help"));
		}catch (Exception e) {
			new ErrorLogg(e, "Error in onReady", "Unknown error", null);
		}
	}
	
	@Override
	public void onReconnect(ReconnectedEvent event) {
		try {
			super.onReconnect(event);
			event.getJDA().getPresence().setGame(Game.of("Send ;help"));
			
			new Print("Reconected", false);
		}catch (Exception e) {
			new ErrorLogg(e, "Error in onReconnect", "Unknown error", null);
		}
	}
	
	public void onMessageReceived(MessageReceivedEvent event){
		try{
			if(!event.getAuthor().equals(event.getJDA().getSelfUser())){
				String  contentCase=event.getMessage().getContent(), content = event.getMessage().getContent().toLowerCase();
				MessageChannel channel = event.getChannel();
				
				channel.sendTyping();
				
				if(content.toLowerCase().equals("prefix")){
					if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
						//Private channel
						channel.sendMessage("The current prefix on our private chat is :\""+ReadWrite.getPrefix(channel.getId())+"\"").queue();
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
				String prefix=";";
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//if privatechannel ---> no guildId
					prefix=ReadWrite.getPrefix(channel.getId());
				}
				else {
					//Not private --> has guild Id
					prefix=ReadWrite.getPrefix(event.getGuild().getId());
				}
				try {
					onMessageReceivedPrefix(event, prefix, content, channel, contentCase);
				} catch (Exception e) {
					// FIXME: handle exception
					new ErrorLogg(e, "Prefix: "+prefix+", content: "+content, "Error with onMessageRecievedPrefix for "+channel.getName()+ " channel", event);
				}
			}
		}catch (Exception e) {
			// FIXME: handle exception
			new ErrorLogg(e, "Error with onMessageRecievedEvent","Unknown error", event);
		}
	}
	
	private void onMessageReceivedPrefix(MessageReceivedEvent event, String prefix, String content, MessageChannel channel,
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
						new Prefix(channel, event, content, afterCommand);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with prefix command", event);
					}
					break;
				
				case "up":
					try {
						//					new Up(channel, event, content);
					} catch (Exception e) {
						new ErrorLogg(e, content, "Error with up command", event);
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
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		try{
			PrivateChannel channel=event.getChannel();
			String content = event.getMessage().getContent();
			
			if(!event.getAuthor().equals(event.getJDA().getSelfUser())){
				
				channel.sendTyping();
				
				//Already checked if private
				String prefix=ReadWrite.getPrefix(channel.getId());
				
				if(content.equals("prefix")){
					return;
				}
				
				else if(content.length()>=prefix.length()){
					if(!content.substring(0, prefix.length()).equals(prefix)&&!Character.toString(content.charAt(0)).equals(";")){
						channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
								+ "commands starting with "+prefix+". You can use "+prefix+"help for example, to see what commands you can use."
								+ " Uppercase or lowercase does not matter").queue();
						return;
					}
				}
				else {
					if(!Character.toString(content.charAt(0)).equals(";"))
						channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
								+ "commands starting with "+prefix+". You can use \""+prefix+"help\" for example, to see what commands you can use."
								+ " Uppercase or lowercase does not matter").queue();
					return;
				}
			}
		}catch (Exception e) {
			new ErrorLogg(e, "Content: "+event.getMessage().getContent()+ ", Author: "+event.getAuthor().getName() + "#"+event.getAuthor().getDiscriminator()+
					", channel: +"+event.getChannel().getName()+", MessageID: "+event.getMessage().getId(), "Unknown error in onPrivateMessageRecieved", null);
		}
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		try{
			MessageChannel channel = event.getGuild().getTextChannelsByName("general", true).get(0);
			channel.sendMessage("Welcome *"+event.getMember().getAsMention()+"* to "+event.getGuild().getName()+"!").queue();
			
			if(event.getGuild().getTextChannelsByName("modlog", true).size()>0){
				event.getGuild().getTextChannelsByName("modlog", true).get(0).sendMessage("The user **"+event.getMember().getUser().getName()+
						"** with the # id **"+ event.getMember().getUser().getDiscriminator() + "** and long id as **"+event.getMember().getUser().getId()
						+"** just joined us").queue();
			}
			else {
				if(!event.getGuild().getOwner().getUser().hasPrivateChannel()){
					event.getGuild().getOwner().getUser().openPrivateChannel().complete(true);
				}
				event.getGuild().getOwner().getUser().getPrivateChannel().sendMessage("A user (\"**"+event.getMember().getUser().getName()+"#"+
						event.getMember().getUser().getDiscriminator()+"\"** with long id: **"+event.getMember().getUser().getId()+"**) just joined your guild \"**"+
						event.getGuild().getName()+"**\". *If you don't want to recieve these as private messages, create a channel called \"modlog\", and I will post"
						+ " this information there*").queue();
			}
		}
		catch (Exception e) {
			new ErrorLogg(e, "Guild: "+event.getGuild().getName()+", User: "+event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator()
					, "Unknown error in onGuildMemeberJoin", null);
		}
	}
	
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		
		try{
			MessageChannel channel = event.getGuild().getTextChannelsByName("general", true).get(0);
			channel.sendMessage("Bye bye, *"+event.getMember().getAsMention()+"*!").queue();
			
			if(event.getGuild().getTextChannelsByName("modlog", true).size()>0){
				event.getGuild().getTextChannelsByName("modlog", true).get(0).sendMessage("The user **"+event.getMember().getUser().getName()+
						"** with the # id **"+ event.getMember().getUser().getDiscriminator() + "** and long id as **"+event.getMember().getUser().getId()
						+"** just left us").queue();
			}
			else {
				if(!event.getGuild().getOwner().getUser().hasPrivateChannel()){
					event.getGuild().getOwner().getUser().openPrivateChannel().complete(true);
				}
				event.getGuild().getOwner().getUser().getPrivateChannel().sendMessage("A user (\"**"+event.getMember().getUser().getName()+"#"+
						event.getMember().getUser().getDiscriminator()+"\"** with long id: **"+event.getMember().getUser().getId()+"**) just left your guild \"**"+
						event.getGuild().getName()+"**\". *If you don't want to recieve these as private messages, create a channel called \"modlog\", and I will post"
						+ " this information there*").queue();
			}
		}
		catch (Exception e) {
			new ErrorLogg(e, "Guild: "+event.getGuild().getName()+", User: "+event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator()
					, "Unknown error in onGuildMemeberLeave", null);
		}
		
	}
	
	public void onGenericEvent(Event event) {
		new DailyDose(event.getJDA());
	}
}
