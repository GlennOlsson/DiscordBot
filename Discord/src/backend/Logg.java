package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import backend.ReadWrite.JSONDocument;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Logg {
	public Logg(Exception exception, String content, String id, MessageReceivedEvent event) {

		if(System.getProperty("os.name").toLowerCase().contains("linux")){
			//if linux (RasPi)
			String currentContent = "", newContent = "";

			new Print("An error was caught", true);

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
				@SuppressWarnings("resource")
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
					new Error(e, "Error writing file", content, id);
				}

				//Succeded to write, now shall commit and push

				try {

					@SuppressWarnings("resource")
					Git git = new Git(new FileRepository("/home/pi/DiscordBot/DiscordBot/.git"));

					git.commit().setOnly("Discord/Files/Errorlog.md").setMessage("Error caught").call();

					CredentialsProvider cp = new UsernamePasswordCredentialsProvider("kakan9898", ReadWrite.getKey("gitPass", JSONDocument.secret));

					git.push().setRemote("origin").setCredentialsProvider(cp).call();

					new Print("Push succesfull", false);

				} catch (Exception e) {
					// FIXME Auto-generated catch block
					new Error(e, "Error with git", content, id);
				}

			}
			catch (Exception e) {
				// FIXME: handle exception
				new Error(e, "Error with reading, maybe", content, id);
			}
		}
		else {
			//if windows
			exception.printStackTrace();
		}
	}	
}
