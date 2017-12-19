/*
 *
 *  * Copyright 2017 Glenn Olsson
 *  *
 *  * Permission is hereby granted, free of charge, to any
 *  * person obtaining a copy of this software and associated
 *  *  documentation files (the "Software"), to deal in the Software
 *  *  without restriction, including without limitation the rights to
 *  *  use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  *  sell copies of the Software, and to permit persons to whom
 *  *  the Software is furnished to do so, subject to the following
 *  *  conditions:
 *  *
 *  * The above copyright notice and this permission notice shall
 *  * be included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
 *  * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 *  * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *  * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 *  * OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package server;

import backend.Logger;
import backend.ReadWrite;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.regexp.internal.RE;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;

import static spark.Spark.*;

public class Listener {
	
	public Listener(JDA jda){
		
		port(8080);
		
		get("/:guild", (req, res) -> {
			try{
				String guildName = req.params(":guild");
				
				List<Guild> guildList = jda.getGuildsByName(guildName, true);
				
				if(guildList.size() == 0){
					//No guild with that name
					return "There's no guild with that name";
				}
				
				JsonObject objectOfGuild = ReadWrite.getGuild(guildList.get(0));
				
				//Reading all JSON so it will be readable
				String name = objectOfGuild.get("name").getAsString();
				String welcomeMessage = objectOfGuild.get("welcomeMessage").getAsString();
				String prefix = objectOfGuild.get("prefix").getAsString();
				
				JsonArray gamesArray = objectOfGuild.get("games").getAsJsonArray();
				
				JsonArray dailyDoseArray = objectOfGuild.get("dailyDoses").getAsJsonArray();
				
				StringBuilder contentBuilder = new StringBuilder();
				
				contentBuilder.append("<pre>");
				
				contentBuilder.append("Name: ");
				contentBuilder.append(name);
				
				contentBuilder.append("\n\n");
				
				contentBuilder.append("Welcome message: ");
				contentBuilder.append(welcomeMessage);
				
				contentBuilder.append("\n\n");
				
				
				contentBuilder.append("Prefix: ");
				contentBuilder.append(prefix);
				
				contentBuilder.append("\n\n");
				
				
				contentBuilder.append("Games: ");
				for(JsonElement jsonElement : gamesArray){
					String game = jsonElement.getAsString();
					contentBuilder.append("\n     ");
					contentBuilder.append(game);
				}
				
				contentBuilder.append("\n\n");
				
				contentBuilder.append("Daily doses: ");
				for(JsonElement jsonElement : dailyDoseArray){
					JsonObject dailyDoseObject = jsonElement.getAsJsonObject();
					
					String subreddit = dailyDoseObject.get("subreddit").getAsString();
					String sendTime = dailyDoseObject.get("sendTime").getAsString();
					String lastSent = dailyDoseObject.get("lastSent").getAsString();
					long channelID = dailyDoseObject.get("channel").getAsLong();
					
					Channel channel = jda.getTextChannelById(channelID);
					
					String channelName = channel.getName();
					
					contentBuilder.append("\n  Subredddit:    /r/");
					contentBuilder.append(subreddit);
					
					contentBuilder.append(",\n  Send time     ");
					contentBuilder.append(sendTime);
					
					contentBuilder.append(",\n  Last sent     ");
					contentBuilder.append(lastSent);
					
					contentBuilder.append(",\n  Channel       ");
					contentBuilder.append(channelName);
				}
				
				contentBuilder.append("\n");
				
				
				contentBuilder.append("</pre>");
				
				return contentBuilder.toString();
				
			}
			catch (Exception e){
				Logger.logError(e, "Error with server", "Req parameter: " + req.params(":guild"), null);
				return "ERROR - 500";
			}
		});
	}
	
}
