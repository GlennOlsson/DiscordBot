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

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class ReadWrite {
	public static Boolean isAuthorized(TextChannel textChannel, MessageReceivedEvent event, String[] roleList) {
		List<Role> roles = null;

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
			JSONParser parser = new JSONParser();
			Object object = parser.parse(new FileReader(getPath()));
			JSONObject jsonObject = (JSONObject) object;

			if(jsonObject.containsKey("prefix"+id)){
				prefix=(String) jsonObject.get("prefix"+id);
				return prefix;
			}
			else {
				//If specific prefix for channel doesn't exist
				return ";";
			}
		} catch (Exception e) {

			new Print("ERROR WITH PREFIX, RETURNING \";\"", true);
			new ErrorLogg(e, "In getPrefix", "Here's string Id: --"+id+"--", null);
			return ";";
		}
	}

	@SuppressWarnings("unchecked")
	public static void setPrefix(String id, String prefix) {

		try {
			JSONParser parser = new JSONParser();
			Object object = parser.parse(new FileReader(getPath()));

			JSONObject jsonObject = (JSONObject) object;

			jsonObject.put("prefix"+id, prefix);

			//WRITING JSON
			try (FileWriter file = new FileWriter(getPath())){
				file.write(jsonObject.toJSONString());
				new Print("Successfully wrote {\"prefix"+id+"\":\""+prefix+"\"", false);
			}
		}
		catch (Exception e) {
			new Print("-- ERROR IN WRITING IN settings.json --", true);
			new ErrorLogg(e, "In setPrefix", "Here's string Id: --"+id+"--, and Prefix: --"+prefix+"--", null);
		}
	}
	
	//Earlier RetrieveSetting.java
	public static String getKey(String key){
			try {
				JSONParser parser = new JSONParser();
				Object object = parser.parse(new FileReader(getPath()));

				JSONObject jsonObject = (JSONObject) object;

				return (String) jsonObject.get(key);

			} catch (Exception e) {
				new ErrorLogg(e, "Error in fetching from settings file", "String key: --"+key+"--", null);
			}

		return null;
	}
	@SuppressWarnings("unchecked")
	public static void setKey(String key, String value){

		try {
			JSONParser parser = new JSONParser();
			Object object = parser.parse(new FileReader(getPath()));

			JSONObject jsonObject = (JSONObject) object;

			jsonObject.put(key, value);

			try (FileWriter file = new FileWriter(getPath())){
				file.write(jsonObject.toJSONString());
				new Print("Successfully wrote {\""+key+"\":\""+value+"\"}", false);
			}

		} catch (Exception e) {
			new ErrorLogg(e, "setKey in RetrieveSettings", "Trying to setKey in settings.json", null);
		}
	}

	@SuppressWarnings("unchecked")
	private static String getPath(){
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			return "C:\\Users\\Glenn\\Documents\\DiscordBot\\Secret.json";
		}
		else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			return "/home/pi/DiscordBot/Secret.json";
		}
		else if(System.getProperty("os.name").toLowerCase().contains("mac os x")){
			return "/Users/glenn/Documents/Idea projects/Secret.json";
			
		}
		return null;
	}
}
