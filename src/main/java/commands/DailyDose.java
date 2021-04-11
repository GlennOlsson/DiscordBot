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


import RedditAPI.RedditClient;
import RedditAPI.RedditPost;
import backend.Logger;

import backend.ReadWrite;
import backend.Return;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.DiscordBot;
import main.Test;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.Timer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static commands.Reddit.*;

public class DailyDose {
	
	public static void dailyCommand(MessageReceivedEvent event, String aftercommand, MessageChannel channel){
		
		String[] commands = aftercommand.split(" ");
		
		if(aftercommand.length()==0){
			//No aftercommand, printing current ones for guild
			JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
			JsonArray dailyDosesArray = guildObject.get("dailyDoses").getAsJsonArray();
			
			StringBuilder messageBuilder = new StringBuilder();
			
			if(dailyDosesArray.size() != 0) {
				messageBuilder.append("These are the current Daily Doses for this server");
			}
			else{
				messageBuilder.append("There are no Daily Doses for this server at the moment. If you have ");
				messageBuilder.append("**Manage Server** permission, you can add Daily Doses with the **;dailydose** command. ");
				messageBuilder.append("Send **;help dailydose** for more info");
			}
			
			for(JsonElement jsonElement : dailyDosesArray){
				JsonObject currentDailyDose = jsonElement.getAsJsonObject();
				
				String subreddit = currentDailyDose.get("subreddit").getAsString();
				long channelID = currentDailyDose.get("channel").getAsLong();
				TextChannel sendToChannel = event.getJDA().getTextChannelById(channelID);
				String timeToSend = currentDailyDose.get("sendTime").getAsString();
				
				messageBuilder.append("\n\n");
				messageBuilder.append("Daily dose from subreddit ");
				messageBuilder.append(subreddit);
				messageBuilder.append(" to a channel with the name ");
				messageBuilder.append(sendToChannel.getName());
				messageBuilder.append(" is supposed to be sent at ");
				messageBuilder.append(timeToSend);
				messageBuilder.append(" (CET) every day");
				
			}
			channel.sendMessage(messageBuilder.toString()).queue();
		}
		else if(aftercommand.startsWith("add")){
			//Must be "add SUBREDDIT HOUR:MINUTE
			if(commands.length != 3){
				channel.sendMessage("Wrong amount of arguments! To add a Daily Dose, use it like \n" +
						";dailydose add SUBREDDIT HOURTOSEND:MINUTETOSEND").queue();
				return;
			}
			try{
				String subreddit = commands[1].replace("/r/", "");
				String timeToSend = commands[2];
				
				//Must be HH:MM
				if(timeToSend.length() != 5 || timeToSend.split(":").length != 2){
					throw new Exception();
				}
				
				JsonObject newDailyDoseObject = new JsonObject();
				newDailyDoseObject.addProperty("subreddit", subreddit);
				newDailyDoseObject.addProperty("sendTime", timeToSend);
				newDailyDoseObject.addProperty("lastSent", "1970-01-01");
				newDailyDoseObject.addProperty("channel", channel.getIdLong());
				
				JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
				JsonArray dailyDosesArray = guildObject.get("dailyDoses").getAsJsonArray();
				dailyDosesArray.add(newDailyDoseObject);
				
				ReadWrite.addEditedGuild(event.getGuild(), guildObject);
				
				Logger.print("Added new Daily Dose to " + event.getGuild().getName() + " guild, with subreddit "
						+ subreddit + " and on the time " + timeToSend);
				
				channel.sendMessage("Added daily dose for /r/" + subreddit).queue();
			}
			catch (Exception e){
				Logger.logError(e, "Could not add new Daily Dose", "Aftercommand: " + aftercommand, event);
				channel.sendMessage("Could not add Daily Dose").queue();
			}
			
		}
		else if(aftercommand.startsWith("remove")){
			//Must be "remove SUBREDDIT
			if(commands.length != 2){
				channel.sendMessage("Wrong amount of arguments! To remove a Daily Dose, use it like \n" +
						";dailydose remove SUBREDDIT").queue();
				return;
			}
			try{
				String subreddit = commands[1].replace("/r/", "");
				
				JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
				JsonArray dailyDosesArray = guildObject.get("dailyDoses").getAsJsonArray();
				
				for(JsonElement jsonElement : dailyDosesArray){
					JsonObject currentDailyDoseObject = jsonElement.getAsJsonObject();
					String thisSubreddit = currentDailyDoseObject.get("subreddit").getAsString();
					
					if(thisSubreddit.toLowerCase().equals(subreddit.toLowerCase())){
						dailyDosesArray.remove(jsonElement);
						break;
					}
				}
				
				ReadWrite.addEditedGuild(event.getGuild(), guildObject);
				
				Logger.print("Removed Daily Dose with subreddit " + subreddit);
				
				channel.sendMessage("Removed daily dose for /r/" + subreddit).queue();
				
			}
			catch (Exception e){
				Logger.logError(e, "Could not remove new Daily Dose", "Aftercommand: " + aftercommand, event);
				channel.sendMessage("Could not remove Daily Dose").queue();
			}
		}
		else if(aftercommand.startsWith("force")){
			//Must be me!!
			if(event.getAuthor().getId().equals(Test.idKakan)) {
				if(commands.length != 2){
					channel.sendMessage("Must be one and only one parameter. ;dailydose SUBREDDIT").queue();
					return;
				}
				
				String subreddit = commands[1].replace("/r/", "");
				
				DailyDose(subreddit, channel);
			}
		}
		else{
			channel.sendMessage("I do not understand what you mean");
		}
	}
	
	@SuppressWarnings("WeakerAccess")
	public static void DailyDose(@SuppressWarnings("SameParameterValue") String subreddit, MessageChannel channel) {
		//Connect to reddit.com/r/*subreddit*
		
		Logger.print("DailyDose!");
		
		ArrayList<Message> messages = new ArrayList<>();
		try {
			//channel=channel.getJDA().getTextChannelById(Test.idKakanisatanGeneral);
			
			ArrayList<RedditPost> posts = DiscordBot.redditClient.getTopPosts(subreddit, 3, SubredditSort.TOP, TimePeriod.DAY);
			
			for(RedditPost post : posts){
				MessageBuilder message = new MessageBuilder();
				
				message.append("**");
				message.append(post.getTitle());
				message.append("** - ");
				message.append(post.getMediaUrl());
				
				messages.add(message.build());
			}
			
			channel.sendMessage("Here's your daily dose of /r/"+ subreddit).submit();
			
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
						
						String formattedDate = calendarFormat.format(now.getTime());
						
						dailyDoseObject.addProperty("lastSent", formattedDate);
						
						ReadWrite.addEditedGuild(currentGuild, currentJSONGuild);
						
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
