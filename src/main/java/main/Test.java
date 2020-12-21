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

import RedditAPI.RedditClient;
import RedditAPI.RedditPost;
import backend.Logger;
import backend.ReadWrite;
import com.google.gson.JsonObject;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import commands.DailyDose;
import net.dean.jraw.models.Submission;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static commands.Reddit.*;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import server.Listener;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Test extends ListenerAdapter {
	
	public static String idKakan = "165507757519273984", idKakansBot = "282116563266437120",
			idKakanisatanGeneral = "282109399617634304", idKakanistanGuild = "282109399617634304";
	
	public static void main(String[] args) throws Exception {

//		Logger.print(EmojiManager.getForAlias(":-1:").getUnicode());
		
//		JDA jda = null;
//		try {
//			jda = new JDABuilder(AccountType.BOT)
//					.setToken(ReadWrite.getKey("oath").getAsString())
//					.addEventListener(new Test())
//					.buildBlocking();
//
			DiscordBot.redditClient = new RedditClient();
//
//		} catch (Exception e) {
//
//			Logger.logError(e, "JDA Fail in Test", "JDA Fail in Test", null);
//
//		}

//
//		Guild kakanistan = jda.getGuildById("282109399617634304");
//		//General kakanistan
//		TextChannel general = jda.getTextChannelById("282109399617634304");
		
		//DailyDose.DailyDose("aww", general);
		
//
//		AudioManager audioManager = new AudioManagerImpl(kakanistan);
//		audioManager.openAudioConnection(jda.getGuildsByName("Kakanistan", true).get(0).getVoiceChannels().get(0));


//
//		String lastSentDate = "2017-12-18";
//		String toSendTime = "15:64";
//
//		DateFormat calendarFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//		String lastSentString = lastSentDate + " " + toSendTime;
//
//		Date lastSent = calendarFormat.parse(lastSentString);
//		Calendar lastSentCalendar = Calendar.getInstance();
//		lastSentCalendar.setTime(lastSent);
//
//		Calendar now = Calendar.getInstance();
//		String formatted = calendarFormat.format(now.getTime());
//
//		Logger.print(formatted);
////
//		Calendar sentPlusOneDay = (Calendar) lastSentCalendar.clone();
//		sentPlusOneDay.add(Calendar.DAY_OF_MONTH, 1);
//
//		if(now.after(sentPlusOneDay)){
//			Logger.print("New DailyDose");
//		}
//		else{
//			Logger.print("NO NEW DAILY");
//		}
//
//		Logger.print("Now: " + now.getTime() + ", Then: " + sentPlusOneDay.getTime());
		
		//280772605072375809

//
//		List<TextChannel> channels = jda.getTextChannels();
////		Logger.print(channels.size());
//		for (TextChannel channel : channels) {
//			try {
//				Logger.print("Name: " + channel.getName() + ", ID: " + channel.getId());
//
//			} catch (Exception e) {
//			}
//		}
//
//		Logger.print(jda.getGuildById(idKakanisatanGeneral).getEmotesByName("<:discordemote:425013364809269269>", true));
		
		String redditTests[] = {
				"https://www.reddit.com/r/SiliconValleyHBO/comments/730qcz/how_did_jareddonald_celebrate/",
				"https://www.reddit.com/r/worldnews/comments/734bbp/puerto_rico_rejects_loan_offers_accusing_hedge/",
				"https://www.reddit.com/r/woahdude/comments/730nv5/escher_circle_limit/",
				"https://www.reddit.com/r/oddlysatisfying/comments/735eei/this_group_of_gentlemen_bobbing_their_heads/",
				"https://www.reddit.com/r/mildlyinteresting/comments/7349fj/hurricane_irma_eroded_away_the_dune_this_pine/",
				"https://www.reddit.com/r/AdviceAnimals/comments/733lgf/never_hurts_to_use_the_google/",
				"https://www.reddit.com/r/pics/comments/733k4v/the_mount_rushmore_of_selfies/",
				"https://www.reddit.com/r/me_irl/comments/733ufb/meirl/",
				"https://www.reddit.com/r/firstworldanarchists/comments/730fpq/mad_lass/",
				"https://www.reddit.com/r/videos/comments/734xd0/fucking_shit/",
				"https://www.reddit.com/r/AbruptChaos/comments/730528/give_us_another_one/",
				"https://www.reddit.com/r/WTF/comments/732qhd/thats_some_serious_faith_you_have_there/",
				"https://www.reddit.com/r/gifs/comments/734jy3/synchronized_backflip/",
				"https://www.reddit.com/r/yesyesyesyesno/comments/8qm2y9/almost_had_it/?st=jidc6ebo&sh=7c931f4b",
				"https://www.reddit.com/r/gaming/comments/8qzwul/no_you_cant_go_play_with_the_other_consoles/"
		};
		
		
		Pattern pattern = Pattern.compile("comments/((([0-9]|[A-z])[A-z]*[0-9]*){5})");
		
		for(String url : redditTests){
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {
				String id = matcher.group(1);
				Logger.print(id);
				
				RedditPost post = DiscordBot.redditClient.getPostWithID(id);
				
				if(!post.isTextpost())
					Logger.print(post.getTitle() + " - " + post.getMediaUrl());
				
				
			}
			else{
				Logger.print("None found");
			}
		}
		
		
		
}
	
//	public void onMessageReceived(MessageReceivedEvent event){
//
//		String content = event.getMessage().getContentRaw(), afterCommand="", command = content.substring(";".length());
//
//		Logger.print(content);
//
//		if(event.getAuthor().getId().equals("165507757519273984")){
//			Reddit(event.getChannel(), event, content);
//		}
//	}
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event){
		
		Logger.print(event.getChannel().getId());
		
		String content = event.getMessage().getContentRaw().toLowerCase();
		
		//Iterator<PrivateChannel> it = event.getJDA().getPrivateChannelCache().iterator();
		
		Iterator<Guild> it = event.getJDA().getGuilds().iterator();
		
		while(it.hasNext()){
			Guild pc = it.next();
			Logger.print(pc.getName());
		}
		
		List<PrivateChannel> channels = event.getJDA().getPrivateChannels();
//		Logger.print(channels.size());
		for (PrivateChannel channel : channels) {
			try {
				Logger.print("Name: " + channel.getName() + ", Content: " + channel.retrieveMessageById​(channel.getLatestMessageId()).complete().getContentRaw());
				
			} catch (Exception e) {
			}
		}
		
//		if(content.contains(";testing")&&content.substring(0, ";testing".length()).equals(";testing")){
//			event.getChannel().sendMessage(event.getJDA().retrieveUserById(getUserById("165507757519273984")).queue().getAsMention()).queue();
//		}
//
//		if(content.contains(";sup")&&content.substring(0, ";sup".length()).equals(";sup")){
//			Logger.print();
//		}
//
//		if(content.contains(";send")&&content.substring(0, ";send".length()).equals(";send")){
//			String user= event.getAuthor().getName().toLowerCase() + "#"+event.getAuthor().getDiscriminator().toLowerCase();
//			if (event.getJDA().retrieveUserById(getUserById(idKakan)).queue()==event.getAuthor()) {
//				System.err.println("AUTHORIZED");
//				if(!content.contains(" ")){
//					event.getChannel().sendMessage("-- YOU CAN MESSAGE THE FOLLOWING CHANNELS --").queue();
//					List<TextChannel> channels = event.getJDA().getTextChannels();
//
//					List<String> channelNames=new ArrayList<>();
//
//					for (TextChannel channel : event.getJDA().getTextChannels()) {
//						channelNames.add(channel.getGuild().getName() +" - "+ channel.getName());
//
//					}
//
//					channelNames.sort(String::compareToIgnoreCase);
//					int i=0;
//					for (String string : channelNames) {
//						i++;
//						event.getChannel().sendMessage(i+". "+string).queue();
//					}
//
//					event.getChannel().sendMessage("-- THAT IS ALL --").queue();
//				}
//				else if (content.split(" ").length==2) {
//					String arg = content.split(" ")[1];
//
//					List<TextChannel> channels = event.getJDA().getTextChannels();
//					List<String> channelNames=new ArrayList<>();
//
//					for (TextChannel channel : event.getJDA().getTextChannels()) {
//						channelNames.add(channel.getGuild().getName() +" - "+ channel.getName());
//
//					}
//
//					channelNames.sort(String::compareToIgnoreCase);
//
//					Logger.print(channelNames.get(Integer.parseInt(arg)-1));
//
//				}
//			}
//		}
		
	}
	
}




