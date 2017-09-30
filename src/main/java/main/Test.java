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

import backend.ErrorLogg;
import backend.Print;
import backend.ReadWrite;
import backend.Return;
import commands.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.impl.AudioManagerImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Test extends ListenerAdapter {
	
	public static String idKakan = "165507757519273984", idKakansBot = "282116563266437120",
	idKakanisatanGeneral = "282109399617634304", idKakanistanGuild = "282109399617634304";
	
	public static void main(String[] args) {
//		JDA jda = null;
//		try {
//			jda = new JDABuilder(AccountType.BOT)
//					.setToken(ReadWrite.getKey("oath"))
//					.addEventListener(new Test())
//					.buildBlocking();
//
//		} catch (Exception e) {
//
//			new ErrorLogg(e, "JDA Fail in Test", "JDA Fail in Test", null);
//
//		}
//
//		Guild kakanistan = jda.getGuildById("282109399617634304");
//		//General kakanistan
//		TextChannel general = jda.getTextChannelById("282109399617634304");
//
//		AudioManager audioManager = new AudioManagerImpl(kakanistan);
//		audioManager.openAudioConnection(jda.getGuildsByName("Kakanistan", true).get(0).getVoiceChannels().get(0));
		
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
				"https://www.reddit.com/r/gifs/comments/734jy3/synchronized_backflip/"
		};
		
		
		new Print(new Reddit().getRedditMediaURLAndTitle("https://www.reddit.com/r/Rainbow6/comments/738ijh/only_the_realest_og_ogs_remember_this")[0]);
		
	}
	private Test(){
		
	}
	
	public void onMessageReceived(MessageReceivedEvent event){
		
		String content = event.getMessage().getRawContent(), afterCommand="", command = content.substring(";".length());
		
		if(event.getAuthor().getId().equals("165507757519273984")){
			new Reddit(event.getChannel(), event, content);
		}
	}
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event){
		
		String content = event.getMessage().getContent().toLowerCase();
		
		if(content.contains(";testing")&&content.substring(0, ";testing".length()).equals(";testing")){
			event.getChannel().sendMessage(event.getJDA().getUserById("165507757519273984").getAsMention()).queue();
		}
		
		if(content.contains(";sup")&&content.substring(0, ";sup".length()).equals(";sup")){
			System.out.println();
		}
		
		if(content.contains(";send")&&content.substring(0, ";send".length()).equals(";send")){
			String user= event.getAuthor().getName().toLowerCase() + "#"+event.getAuthor().getDiscriminator().toLowerCase();
			if (event.getJDA().getUserById(idKakan)==event.getAuthor()) {
				System.err.println("AUTHORIZED");
				if(!content.contains(" ")){
					event.getChannel().sendMessage("-- YOU CAN MESSAGE THE FOLLOWING CHANNELS --").queue();
					List<TextChannel> channels = event.getJDA().getTextChannels();
					
					List<String> channelNames=new ArrayList<>();
					
					for (TextChannel channel : event.getJDA().getTextChannels()) {
						channelNames.add(channel.getGuild().getName() +" - "+ channel.getName());
						
					}
					
					channelNames.sort(String::compareToIgnoreCase);
					int i=0;
					for (String string : channelNames) {
						i++;
						event.getChannel().sendMessage(i+". "+string).queue();
					}
					
					event.getChannel().sendMessage("-- THAT IS ALL --").queue();
				}
				else if (content.split(" ").length==2) {
					String arg = content.split(" ")[1];
					
					List<TextChannel> channels = event.getJDA().getTextChannels();
					List<String> channelNames=new ArrayList<>();
					
					for (TextChannel channel : event.getJDA().getTextChannels()) {
						channelNames.add(channel.getGuild().getName() +" - "+ channel.getName());
						
					}
					
					channelNames.sort(String::compareToIgnoreCase);
					
					System.out.println(channelNames.get(Integer.parseInt(arg)-1));
					
				}
			}
		}
		
	}
	
}




