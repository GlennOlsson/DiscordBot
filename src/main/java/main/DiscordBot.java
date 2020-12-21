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
Fix bot.glennolsson.se/SERVER_NAME to display the settings of the server, using spark server

Reddit API

Client ID
uGN5rXPLJsdZ2Q

https://www.reddit.com/api/v1/authorize?client_id=uGN5rXPLJsdZ2Q&response_type=code&state=suh&redirect_uri=https://github.com/kakan9898/DiscordBot&duration=temporary&scope=identity%20edit%20flair%20history%20modconfig%20modflair%20modlog%20modposts%20modwiki%20mysubreddits%20privatemessages%20read%20report%20save%20submit%20subscribe%20vote%20wikiedit%20wikiread

https://www.reddit.com/api/v1/authorize?client_id=uGN5rXPLJsdZ2Q&response_type=code&state=suh&redirect_uri=https://github.com/kakan9898/DiscordBot&duration=temporary&scope=identity%20edit%20flair%20history%20modconfig%20modflair%20modlog%20modposts%20modwiki%20mysubreddits%20privatemessages%20read%20report%20save%20submit%20subscribe%20vote%20wikiedit%20wikiread


*/


import RedditAPI.RedditClient;
import backend.*;
import commands.DailyDose;
import commands.GameRoles;
import events.*;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import server.Listener;
import net.dv8tion.jda.api.entities.Activity;


public class DiscordBot extends ListenerAdapter{
	
	GenericEvents genericEvents = new GenericEvents();
	
	public static RedditClient redditClient;
	
	public static void main(String[] args) {
		try {
			try {
				JDA jda = new JDABuilder(AccountType.BOT)
						.setToken(ReadWrite.getKey("oath").getAsString())
						.addEventListeners(new DiscordBot())
						.setActivity​(Activity.playing("Send ;help"))
						.build();
				
				DailyDose.DailyDose(jda);
				
				redditClient = new RedditClient();
				
				new Listener(jda);
				
			} catch (Exception e) {
				Logger.logError(e, "JDA Builder", "JDA Builder", null);
			}
		} catch (Exception e) {
			Logger.logError(e, "Error in Main", "Unknown error", null);
		}
	}
	
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
		MessageReceivedEvents.MessageReceived(event);
	}
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		MessageReceivedEvents.PrivateMessage(event);
	}
	
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){
		GameRoles.messageReactedTo(event);
	}
	
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event){
		GameRoles.reactionRemovedFromMessage(event);
	}
	
	//Guild Member Events
	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		GuildMemberEvents.GuildMemberJoin(event);
	}
	
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		GuildMemberEvents.GuildMemberLeave(event);
	}
	
	//Guild Events
	public void onGuildJoin(GuildJoinEvent event){GuildEvents.GuildJoin(event);}
	
	public void onGuildLeave(GuildLeaveEvent event){GuildEvents.GuildLeave(event);}
	
	//Guild Update Events
	public void onGuildUpdateName(GuildUpdateNameEvent event){GuildUpdateEvents.GuildUpdateName(event);}
	
	//Generic Events
	public void onGenericEvent(Event event) {
		genericEvents.GenericEvent(event);
	}
}
