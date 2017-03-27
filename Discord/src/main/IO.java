package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import main.RetrieveSetting.JSONDocument;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class IO {

	public static void Logg(Exception exception, String content, String id, MessageReceivedEvent event) {

		if(System.getProperty("os.name").toLowerCase().contains("linux")){
			//if linux (RasPi)
			String currentContent = "", newContent = "";

			print("An error was caught", true);

			//Parses the stackTrace as string for it to be saved
			StringWriter errors = new StringWriter();
			exception.printStackTrace(new PrintWriter(errors));


			//Saves title and such to newContent, including current time
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("d/M - HH:mm:ss");
			String currentTime =sdf.format(cal.getTime());

			String eventMessage=null, guild=null;

			if(event!=null){
				if(!event.getChannel().getType().equals(ChannelType.PRIVATE)){
					guild=", in the "+event.getGuild().getName()+" guild";
				}
				else{
					guild =", in a Private message group";
				}

				eventMessage="\nThe sender was "+event.getAuthor().getName()+"#"+event.getAuthor().getDiscriminator()+" in the " +event.getChannel().getName()+ 
						" channel"+guild;
			}

			newContent="##New error at "+currentTime+"\nMessage was: "+content+"\nId: "+id+eventMessage+"\n"+errors.toString()+"\n---------------\n\n";

			try{
				FileReader reader = new FileReader("Files/Errorlog.md");
				BufferedReader br = new BufferedReader(reader); 
				Iterator<String> iterator= br.lines().iterator();
				//Saves the earlier content to currentContent string
				while (iterator.hasNext()) {
					currentContent+=iterator.next()+"\n";
				}
				//Writes old + new content
				try(FileWriter file = new FileWriter("Files/Errorlog.md")){
					file.write(newContent+""+currentContent);
				}
				catch (Exception e) {
					// FIXME: handle exception
					error(e, "Error writing file", content, id);
				}

				//Succeded to write, now shall commit and push

				try {

					Git git = new Git(new FileRepository("/home/pi/DiscordBot/DiscordBot/.git"));

					git.commit().setOnly("Discord/Files/Errorlog.md").setMessage("Updated Errorlog").call();

					CredentialsProvider cp = new UsernamePasswordCredentialsProvider("kakan9898", RetrieveSetting.getKey("gitPass", JSONDocument.secret));

					git.push().setRemote("origin").setCredentialsProvider(cp).call();

					print("Push succesfull", false);

				} catch (Exception e) {
					// FIXME Auto-generated catch block
					error(e, "Error with git", content, id);
				}

			}
			catch (Exception e) {
				// FIXME: handle exception
				error(e, "Error with reading, maybe", content, id);
			}
		}
		else {
			//if windows
			exception.printStackTrace();
		}
	}

	public static void error(Exception exception, String whyThough, String content, String id) {
		//Skicka mail till mig
		print(" ---- FUCK FUCK FUCK ----", true);
		exception.printStackTrace();
	}


	public static void print(String message, Boolean isErrPrint) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("d/M - HH:mm:ss");
		String currentTime =sdf.format(cal.getTime());

		if(isErrPrint){
			System.err.println("Printed: " + message + " at: "+currentTime);
		}
		else{
			System.out.println("["+currentTime+"] "+ message);
		}

	}

	public static void setPrefix(String id, String prefix) {

		try {
			JSONParser parser = new JSONParser();
			Object object = parser.parse(new FileReader("Files/settings.json"));

			JSONObject jsonObject = (JSONObject) object;

			jsonObject.put("prefix"+id, prefix);

			//WRITING JSON
			try (FileWriter file = new FileWriter("Files/settings.json")){
				file.write(jsonObject.toJSONString());
				print("Successfully wrote {\"prefix"+id+"\":\""+prefix+"\"", false);
			}
		}
		catch (Exception e) {
			// FIXME: handle exception

			print("-- ERROR IN WRITING IN settings.json --", true);
			Logg(e, "In setPrefix", "Here's string Id: --"+id+"--, and Prefix: --"+prefix+"--", null);
		}
	}

	public static String getPrefix(String id) {

		//Check prefix
		try {
			String prefix=";";
			JSONParser parser = new JSONParser();
			Object object = parser.parse(new FileReader("Files/settings.json"));
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

			print("ERROR WITH PREFIX, RETURNING \";\"", true);
			Logg(e, "In getPrefix", "Here's string Id: --"+id+"--", null);
			return ";";
		}
	}

	public static Boolean isAuthorized(TextChannel textChannel, MessageReceivedEvent event, String content, String[] roleList) {
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
				for (int j = 0; j < roleList.length; j++) {
					if(rolesName.contains(roleList[j].toLowerCase())){
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

}
