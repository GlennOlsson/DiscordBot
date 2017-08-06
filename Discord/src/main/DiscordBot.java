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

*/


import backend.*;
import events.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


class DiscordBot extends ListenerAdapter{
	
	public static void main(String[] args) {
		try {
			
			try {
				JDA jda = new JDABuilder(AccountType.BOT).setToken(ReadWrite.getKey("oath")).addListener(new DiscordBot()).buildBlocking();
			} catch (Exception e) {
				new ErrorLogg(e, "JDA Builder", "JDA Builder", null);
			}
		} catch (Exception e) {
			new ErrorLogg(e, "Error in Main", "Unknown error", null);
		}
	}
	
	private DiscordBot(){}
	
	//JDA Events
	public void onReady(ReadyEvent event) {
		JDAEvents.Ready(event);
	}
	
	public void onReconnect(ReconnectedEvent event) {
		JDAEvents.Reconnect(event);
	}
	
	public void onShutdown(ShutdownEvent event){
		JDAEvents.Shutdown(event);
	}
	
	//Message Events
	public void onMessageReceived(MessageReceivedEvent event){
		MessageEvents.MessageReceived(event);
	}
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		MessageEvents.PrivateMessage(event);
	}
	
	//Guild Member Events
	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		GuildMemberEvents.GuildMemberJoin(event);
	}
	
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		GuildMemberEvents.GuildMemberLeave(event);
	}
	
	//Generic Events
	public void onGenericEvent(Event event) {
		GenericEvents.GenericEvent(event);
	}
}
