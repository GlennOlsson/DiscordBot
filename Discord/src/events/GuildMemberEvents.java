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
import backend.ReadWrite;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;

/**
 * Created by Glenn on 2017-06-04.
 */
public class GuildMemberEvents {
	public static void GuildMemberJoin(GuildMemberJoinEvent event){
		try{
			MessageChannel channel = event.getGuild().getTextChannelsByName("general", true).get(0);
			String welcomeMessage = ReadWrite.getKey("welcome"+event.getGuild().getId());
			
			if(welcomeMessage==null){
				welcomeMessage="Welcome *"+event.getMember().getAsMention()+"* to "+event.getGuild().getName()+"!";
			}
			else{
				welcomeMessage=welcomeMessage.replace(";mention;",event.getMember().getAsMention());
			}
			
			channel.sendMessage(welcomeMessage).queue();
			
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
						event.getGuild().getName()+"**\". *If you don't want to receive these as private messages, create a channel called \"modlog\", and I will post"
						+ " this information there*").queue();
			}
		}
		catch (Exception e) {
			new ErrorLogg(e, "Guild: "+event.getGuild().getName()+", User: "+event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator()
					, "Unknown error in onGuildMemeberJoin", null);
		}
	}
	
	public static void GuildMemberLeave(GuildMemberLeaveEvent event){
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
						event.getGuild().getName()+"**\". *If you don't want to receive these as private messages, create a channel called \"modlog\", and I will post"
						+ " this information there*").queue();
			}
		}
		catch (Exception e) {
			new ErrorLogg(e, "Guild: "+event.getGuild().getName()+", User: "+event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator()
					, "Unknown error in onGuildMemeberLeave", null);
		}
	}
	
	
}
