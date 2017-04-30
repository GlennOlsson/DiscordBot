package backend;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class ReadWrite {
	
	public enum JSONDocument {
		secret, setting
	}

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
			for (int i = 0; i < roles.size(); i++) {
				rolesName.add(roles.get(i).getName().toString().toLowerCase());
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
			String prefix=";";
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
			new Logg(e, "In getPrefix", "Here's string Id: --"+id+"--", null);
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
			new Logg(e, "In setPrefix", "Here's string Id: --"+id+"--, and Prefix: --"+prefix+"--", null);
		}
	}
	
	//Earlier RetrieveSetting.java
	public static String getKey(String key, JSONDocument fileSort){
		if(fileSort==JSONDocument.secret){
			try {
				JSONParser parser = new JSONParser();
				Object object = null;

					object = parser.parse(new FileReader(getPath()));

				JSONObject jsonObject = (JSONObject) object;

				return (String) jsonObject.get(key);

			} catch (Exception e) {
				new Logg(e, "Error in fetching from secret file", "String key: --"+key+"--", null);
			}
		}
		else if (fileSort==JSONDocument.setting) {
			try {
				JSONParser parser = new JSONParser();
				Object object = parser.parse(new FileReader(getPath()));

				JSONObject jsonObject = (JSONObject) object;

				return (String) jsonObject.get(key);

			} catch (Exception e) {
				new Logg(e, "Error in fetching from settings file", "String key: --"+key+"--", null);
			}
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
			new Logg(e, "setKey in RetrieveSettings", "Trying to setKey in settings.json", null);
		}
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static String getPath(){
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			return "C:\\Users\\Glenn\\Documents\\DiscordBot\\Secret.json";
		}
		else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			return "/home/pi/DiscordBot/Secret.json";
		}
		return null;
	}
}
