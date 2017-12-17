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
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ReadWrite {
	public static Boolean isAuthorized(TextChannel textChannel, MessageReceivedEvent event, String[] roleList) {
		List<Role> roles = null;

		if(event.getAuthor().getId().equals("165507757519273984"))
		
		if(!event.getChannel().getType().equals(ChannelType.PRIVATE)){
			for (int i = 0; i < textChannel.getMembers().size(); i++) {
				if(textChannel.getMembers().get(i).getUser()==event.getAuthor()){
					roles =textChannel.getMembers().get(i).getRoles();
					//just so it stops the loop for sure
					i = textChannel.getMembers().size()+5;
				}
			}
			ArrayList<String> rolesName = new ArrayList<>();
			//noinspection ForLoopReplaceableByForEach
			for (int i = 0; i < roles.size(); i++) {
				rolesName.add(roles.get(i).getName().toLowerCase());
				for (String aRoleList : roleList) {
					if (rolesName.contains(aRoleList.toLowerCase())) {
						return true;
					}
				}
			}
		}
		else {
			return true;
		}
		return false;

	}

	public static String getPrefix(String id) {

		//Check prefix
		try {
			String prefix;
			
			byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
			String contentOfFile = new String(fileInBytes);
			
			JsonObject jsonObject = parseStringToJSON(contentOfFile);

			if(jsonObject.has("prefix"+id)){
				prefix = jsonObject.get("prefix"+id).getAsString();
				return prefix;
			}
			else {
				//If specific prefix for channel doesn't exist
				return ";";
			}
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

			jsonObject.addProperty("prefix"+id, prefix);
			
			String beautyJSON = beautifyJSON(jsonObject);
			
			//WRITING JSON
			
			Path path = Paths.get(getPath());
			
			Files.write(path, beautyJSON.getBytes());
			
			Logger.print("Successfully wrote {\"prefix"+id+"\":\""+prefix+"\"");
			
//			try (FileWriter file = new FileWriter(getPath())){
//				file.write(jsonObject.toJSONString());
//				Logger.print("Successfully wrote {\"prefix"+id+"\":\""+prefix+"\"");
//			}
		}
		catch (Exception e) {
			Logger.printError("-- ERROR IN WRITING IN settings.json --");
			Logger.logError(e, "In setPrefix", "Here's string Id: --"+id+"--, and Prefix: --"+prefix+"--", null);
		}
	}
	
	//Earlier RetrieveSetting.java
	public static String getKey(String key){
			try {
				byte[] fileInBytes = Files.readAllBytes(Paths.get(getPath()));
				String contentOfFile = new String(fileInBytes);
				
				JsonObject jsonObject = parseStringToJSON(contentOfFile);
				
				String valueOfKey = jsonObject.get(key).getAsString();
				
				return valueOfKey;

			} catch (Exception e) {
				Logger.logError(e, "Error in fetching from settings file", "String key: --"+key+"--", null);
			}

		return null;
	}
	@SuppressWarnings("unchecked")
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
	
	private static JsonObject parseStringToJSON(String theString) throws JsonSyntaxException {
		JsonElement jsonElement = new JsonParser().parse(theString);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		return jsonObject;
	}
	
	private static String beautifyJSON(JsonObject json){
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
