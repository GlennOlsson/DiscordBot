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

package backend;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ReadWrite {
	public static Boolean isAuthorized(MessageReceivedEvent event, Permission permission) {
		Member memeber = event.getMember();
		
		if(event.getAuthor().getId().equals(Test.idKakan)){
			return true;
		}
		
		return memeber.hasPermission(permission);
	}
	
	public static String getPrefix(String id) {
		
		//Check prefix
		try {
			byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
			String contentOfFile = new String(fileInBytes);
			
			JsonObject jsonObject = parseStringToJSON(contentOfFile);
			
			JsonObject allGuilds = jsonObject.get("guilds").getAsJsonObject();
			JsonElement guildElem = allGuilds.get(id);
			if(guildElem == null)
				return ";";

			JsonObject thisGuild = guildElem.getAsJsonObject();
			
			JsonElement prefix = thisGuild.get("prefix");
			if(prefix == null || prefix.getAsString().length() == 0){
				//No prefix
				return ";";
			}
			//Else
			return prefix.getAsString();
			
		} catch (Exception e) {
			
			Logger.printError("ERROR WITH PREFIX, RETURNING \";\"");
			Logger.logError(e, "In getPrefix", "Here's string Id: --"+id+"--", null);
			return ";";
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void setPrefix(String id, String prefix) {
		try {
			byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
			String contentOfFile = new String(fileInBytes);
			
			JsonObject jsonObject = parseStringToJSON(contentOfFile);
			
			JsonObject allGuilds = jsonObject.get("guilds").getAsJsonObject();
			JsonObject thisGuild = allGuilds.get(id).getAsJsonObject();
			
			thisGuild.addProperty("prefix", prefix);
			
			String beautyJSON = beautifyJSON(jsonObject);
			
			//WRITING JSON
			Path path = Paths.get(getPath());
			
			Files.write(path, beautyJSON.getBytes());
			
			Logger.print("Successfully wrote prefix " + prefix + " for id " + id);
		}
		catch (Exception e) {
			Logger.printError("-- ERROR IN WRITING IN settings.json --");
			Logger.logError(e, "In setPrefix", "Here's string Id: --"+id+"--, and Prefix: --"+prefix+"--", null);
		}
	}
	
	//Earlier RetrieveSetting.java
	public static JsonElement getKey(String key){
		try {
			byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
			String contentOfFile = new String(fileInBytes);
			
			JsonObject jsonObject = parseStringToJSON(contentOfFile);
			
			JsonElement valueOfKey = jsonObject.get(key);
			
			return valueOfKey;
			
		} catch (Exception e) {
			Logger.logError(e, "Error in fetching from settings file", "String key: --"+key+"--", null);
		}
		
		return null;
	}
	
	public static JsonObject getGuildsObject(){
		JsonObject guilds = getKey("guilds").getAsJsonObject();
		return guilds;
	}
	
	public static void addEditedGuild(Guild guild, JsonObject guildJSON){
		JsonObject currentGuilds = getGuildsObject();
		currentGuilds.add(guild.getId(), guildJSON);
		setKey("guilds", currentGuilds);
	}
	
	public static JsonObject getGuild(Guild guild){
		JsonObject guilds = getGuildsObject();
		return guilds.get(guild.getId()).getAsJsonObject();
	}
	
	@SuppressWarnings("unchecked")
	public static void setKey(String key, JsonElement value){
		try {
			byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
			String contentOfFile = new String(fileInBytes);
			
			JsonObject jsonObject = parseStringToJSON(contentOfFile);
			
			jsonObject.add(key, value);
			
			String beautyJSON = beautifyJSON(jsonObject);
			
			Path path = Paths.get(getPath());
			
			Files.write(path, beautyJSON.getBytes());
			
			Logger.print("Successfully wrote {\""+key+"\":\""+value+"\"}");
			
		} catch (Exception e) {
			Logger.logError(e, "setKey in RetrieveSettings", "Trying to setKey in settings.json", null);
		}
	}
	
	public static void setKey(String key, String value){
		try {
			byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
			String contentOfFile = new String(fileInBytes);
			
			JsonObject jsonObject = parseStringToJSON(contentOfFile);
			
			jsonObject.addProperty(key, value);
			
			String beautyJSON = beautifyJSON(jsonObject);
			
			Path path = Paths.get(getPath());
			
			Files.write(path, beautyJSON.getBytes());
			
			Logger.print("Successfully wrote {\""+key+"\":\""+value+"\"}");
			
		} catch (Exception e) {
			Logger.logError(e, "setKey in RetrieveSettings", "Trying to setKey in settings.json", null);
		}
	}
	
	public static void setKey(String key, int value){
		try {
			byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
			String contentOfFile = new String(fileInBytes);
			
			JsonObject jsonObject = parseStringToJSON(contentOfFile);
			
			jsonObject.addProperty(key, value);
			
			String beautyJSON = beautifyJSON(jsonObject);
			
			Path path = Paths.get(getPath());
			
			Files.write(path, beautyJSON.getBytes());
			
			Logger.print("Successfully wrote {\""+key+"\":\""+value+"\"}");
			
		} catch (Exception e) {
			Logger.logError(e, "setKey in RetrieveSettings", "Trying to setKey in settings.json", null);
		}
	}
	
	public static JsonObject parseStringToJSON(String theString) throws JsonSyntaxException {
		JsonElement jsonElement = new JsonParser().parse(theString);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		return jsonObject;
	}
	
	public static String beautifyJSON(JsonObject json){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		return gson.toJson(json);
	}
	
	@SuppressWarnings("unchecked")
	private static String getPath(){
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			return "C:\\Users\\Glenn\\Documents\\DiscordBot\\Secret.json";
		}
		else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			return "/home/tau/DiscordBot/Secret.json";
		}
		else if(System.getProperty("os.name").toLowerCase().contains("mac os x")){
			return "/Users/glenn/Documents/Idea projects/Secret.json";
			
		}
		return null;
	}
}
