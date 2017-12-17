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


import backend.Logger;
import backend.ReadWrite;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;

/**
 * Created by Glenn on 2017-06-04.
 */
public class GuildMemberEvents {
	public static void GuildMemberJoin(GuildMemberJoinEvent event){
		
		Member joinedMember = event.getMember();
		Guild guildJoined = event.getGuild();
		
		try{
			MessageChannel channel = guildJoined.getTextChannelsByName("general", true).get(0);
			String welcomeMessage = ReadWrite.getKey("welcome"+guildJoined.getId()).getAsString();
			
			if(welcomeMessage==null){
				welcomeMessage="Welcome *"+joinedMember.getAsMention()+"* to "+guildJoined.getName()+"!";
			}
			else{
				welcomeMessage=welcomeMessage.replace(";mention;",joinedMember.getAsMention());
			}
			
			channel.sendMessage(welcomeMessage).queue();
			
			if(guildJoined.getTextChannelsByName("modlog", true).size()>0){
				guildJoined.getTextChannelsByName("modlog", true).get(0).sendMessage("The user **"+joinedMember.getUser().getName()+
						"** ("+joinedMember.getUser().getAsMention()+") with the # id **"+ joinedMember.getUser().getDiscriminator() + "** and long id as **"+joinedMember.getUser().getId()
						+"** just joined us").queue();
			}
			else {
				if(!guildJoined.getOwner().getUser().hasPrivateChannel()){
					guildJoined.getOwner().getUser().openPrivateChannel().complete(true);
				}
				guildJoined.getOwner().getUser().openPrivateChannel().complete().sendMessage("A user (\"**"+joinedMember.getUser().getName()+"#"+
						joinedMember.getUser().getDiscriminator()+"\"** with long id: **"+joinedMember.getUser().getId()+"**) just joined your guild \"**"+
						guildJoined.getName()+"**\". *If you don't want to receive these as private messages, create a channel called \"modlog\", and I will post"
						+ " this information there*").queue();
			}
		}
		catch (Exception e) {
			Logger.logError(e, "Guild: "+guildJoined.getName()+", User: "+joinedMember.getUser().getName()+"#"+joinedMember.getUser().getDiscriminator()
					, "Unknown error in onGuildMemeberJoin", null);
		}
	}
	
	public static void GuildMemberLeave(GuildMemberLeaveEvent event){
		
		Member leavedMember = event.getMember();
		Guild guildLeft = event.getGuild();
		
		try{
			MessageChannel channel = guildLeft.getTextChannelsByName("general", true).get(0);
			channel.sendMessage("Bye bye *"+leavedMember.getUser().getName()+"("+leavedMember.getAsMention()+")*!").queue();
			
			if(guildLeft.getTextChannelsByName("modlog", true).size()>0){
				guildLeft.getTextChannelsByName("modlog", true).get(0).sendMessage("The user **"+leavedMember.getUser().getName()+
						"** ("+leavedMember.getUser().getAsMention()+") with the # id **"+ leavedMember.getUser().getDiscriminator() + "** and long id as **"+leavedMember.getUser().getId()
						+"** just left us").queue();
			}
			else {
				if(!guildLeft.getOwner().getUser().hasPrivateChannel()){
					guildLeft.getOwner().getUser().openPrivateChannel().complete(true);
				}
				guildLeft.getOwner().getUser().openPrivateChannel().complete().sendMessage("A user (\"**"+leavedMember.getUser().getName()+"#"+
						leavedMember.getUser().getDiscriminator()+"\"** with long id: **"+leavedMember.getUser().getId()+"**) just left your guild \"**"+
						guildLeft.getName()+"**\". *If you don't want to receive these as private messages, create a channel called \"modlog\", and I will post"
						+ " this information there*").queue();
			}
		}
		catch (Exception e) {
			Logger.logError(e, "Guild: "+guildLeft.getName()+", User: "+leavedMember.getUser().getName()+"#"+leavedMember.getUser().getDiscriminator()
					, "Unknown error in onGuildMemeberLeave", null);
		}
	}
	
	
}
