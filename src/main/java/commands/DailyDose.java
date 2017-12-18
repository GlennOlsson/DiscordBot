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
import backend.Return;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.Timer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static commands.Reddit.*;

public class DailyDose {
	@SuppressWarnings("WeakerAccess")
	
	public static void DailyDose(@SuppressWarnings("SameParameterValue") String subreddit, MessageChannel channel) {
		//Connect to reddit.com/r/*subreddit*
		
		Logger.print("DailyDose!");
		
		Document doc;
		ArrayList<Message> messages = new ArrayList<>();
		try {
			//channel=channel.getJDA().getTextChannelById(Test.idKakanisatanGeneral);
			
			doc = Jsoup.connect(Return.convertUrl("https://reddit.com/r/"+subreddit.toLowerCase()+"/top/?sort=top&t=day")).userAgent("Chrome").get();
			for (int i = 1; i < 6; i+=2) {
				
				String urlOfPost =  doc.select(".thing:nth-of-type("+(i)+") > div.entry.unvoted > div.top-matter > ul > li.first > a")
						.attr("href");
				
				String[] mediaURLAndTitleOfPost = getRedditMediaURLAndTitle(Return.convertUrl(urlOfPost));
				
				String mediaURLofPost = mediaURLAndTitleOfPost[0];
				String titleOfPost = mediaURLAndTitleOfPost[1];
				
				Logger.print("Daily dose "+(i+1)/2 +": " + Return.convertUrl(urlOfPost));
				
				MessageBuilder message = new MessageBuilder();
				
				message.append("**");
				message.append(titleOfPost);
				message.append("** - ");
				message.append(mediaURLofPost);
				
				messages.add(message.build());
				
			}
			
			channel.sendMessage("Here's your daily dose of /r/"+ subreddit).queue();
			
			for(Message message : messages){
				channel.sendMessage(message).submit();
			}
			
		}
		catch (Exception e) {
			Logger.logError(e, "Error with DailyDose class", "Unknown error", null);
		}
	}
	
	public static void checkDose(JDA jda){
		try {
			List<Guild> listOfGuilds = jda.getGuilds();
			
			JsonObject guildsJSONObject = ReadWrite.getGuildsObject();
			
			for(Guild currentGuild : listOfGuilds){
				JsonObject currentJSONGuild = guildsJSONObject.getAsJsonObject(currentGuild.getId()).getAsJsonObject();
				JsonArray dailyDosesArray = currentJSONGuild.get("dailyDoses").getAsJsonArray();
				
				for(JsonElement dailyDoseElement : dailyDosesArray){
					JsonObject dailyDoseObject = dailyDoseElement.getAsJsonObject();
					
					//Getting the object's last send date and send time, to compare to current time. If it one
					//day later, a new DailyDose is sent
					String dateSent = dailyDoseObject.get("lastSent").getAsString();
					String timeToSend = dailyDoseObject.get("sendTime").getAsString();
					
					DateFormat fullCalendarFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					
					String lastSentString = dateSent + " " + timeToSend;
					
					Date lastSent = fullCalendarFormat.parse(lastSentString);
					Calendar lastSentCalendar = Calendar.getInstance();
					lastSentCalendar.setTime(lastSent);
					
					Calendar now = Calendar.getInstance();
					
					Calendar sentPlusOneDay = (Calendar) lastSentCalendar.clone();
					sentPlusOneDay.add(Calendar.DAY_OF_MONTH, 1);
					
					//Checking that one day has passed, then preforming the DailyDose for channel and subreddit
					if(now.after(sentPlusOneDay)){
						String subreddit = dailyDoseObject.get("subreddit").getAsString();
						long channelID = dailyDoseObject.get("channel").getAsLong();
						TextChannel channel = jda.getTextChannelById(channelID);
						
						DailyDose(subreddit,channel);
						
						DateFormat calendarFormat = new SimpleDateFormat("yyyy-MM-dd");
						
						String formattedDate = calendarFormat.format(now);
						
						dailyDoseObject.addProperty("lastSent", formattedDate);
						
						Logger.print("New DailyDose success");
					}
				}
			}
		} catch (Exception e) {
			Logger.logError(e, "Error in onEvent", "Unknown error caught", null);
		}
	}
	
	public static void DailyDose(JDA jda) {
		//Every 15th min
		Timer timer = new Timer(1000 * 60 * 15, e -> checkDose(jda));
		timer.start();
	}
}
