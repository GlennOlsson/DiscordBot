package main;

import java.io.*;
import java.text.*;
import java.util.*;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.internal.storage.file.*;
import org.eclipse.jgit.transport.*;

import main.RetrieveSetting.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

public class LoggExceptions {

	public static void main(String[] args) {
		try {
			ArrayList<String> list =null;
			list.add("foo");
		} catch (Exception e) {
			// FIXME: handle exception
			Logg(e, "Test from main", "Main in LoggExeption", null);
		}

	}

	public static void Logg(Exception exception, String content, String id, MessageReceivedEvent event) {

		if(System.getProperty("os.name").toLowerCase().contains("linux")){
			//if linux (RasPi)
			String currentContent = "", newContent = "";

			DiscordBot.print("An error was caught", true);
			
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
					
					DiscordBot.print("Push succesfull", false);

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
		DiscordBot.print(" ---- FUCK FUCK FUCK ----", true);
		exception.printStackTrace();
	}

}
