
import java.io.InputStream;

import org.jsoup.*;
import org.jsoup.nodes.*;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Test extends ListenerAdapter{

	public static void main(String[] args) {
		//		new Test();
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken("MjgyMTE2NTYzMjY2NDM3MTIw.C4m_Kw.R-8jmpM6wycnqX0xGvv_wNYjoJ0").addListener(new Test()).buildBlocking();
		} catch (Exception e) {
			e.printStackTrace();
		}

		TextChannel channel = jda.getTextChannels().get(0);
		channel.sendMessage("Sucessfully logged in!").queue();

	}

	public Test(){

		//		System.exit(3);
	}

	public void onMessageReceived(MessageReceivedEvent event){

		if(!event.getAuthor().getName().equals("KakansBot")){
			String content = event.getMessage().getContent().toLowerCase(), contentCase=event.getMessage().getContent();
			MessageChannel channel = event.getChannel();
			InputStream input=null;

			//Reddit command
				if(content.contains("reddit")&&(content.substring(0,"https://www.reddit".length()).contains("https://www.reddit")||
				   content.substring(0,"http://www.reddit".length()).contains("http://www.reddit")||
					content.substring(0,"https://reddit".length()).contains("https://reddit")||
					content.substring(0,"http://reddit".length()).contains("http://reddit"))){
					//If reddit post
						
					if(content.contains(" ")){
					String[] split = content.split(" ");
					if(split.length>2||split[1].length()>0){
					return;	
					}
					content=split[0];	
					}
						
					Document doc;
					String url = null, title = null;
					try {
						doc = Jsoup.connect(event.getMessage().getContent()).userAgent("Chrome").get();
						url = doc.select(".title > a").attr("href");
						title=doc.select(".title > a").text();
						System.out.println(url + " = RUL");
						if(!url.substring(0,3).equals("/r/")){
							//If not textpost
							channel.sendMessage("*"+event.getAuthor().getName()+"* shared: **"+title+"** "+url).queue();
							event.getMessage().deleteMessage().queue();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			
			if(Character.toString(content.charAt(0)).equals(";")){

				//Gif command
				if(content.toLowerCase().contains(";gif")&&content.toLowerCase().substring(0, 4).equals(";gif")){
					Document doc=null;
					String url=null, query=null;
					try {
						query = content.substring(5, content.length()).replace(" ", "-");
						doc = Jsoup.connect("https://www.tenor.co/search/"+query+"-gifs").userAgent("Chrome").get();
						url = "https://www.tenor.co/"+doc.select("#view > div > div.center-container.search > div > div > div:nth-child(1) > figure:nth-child(1) > a").attr("href");

						channel.sendMessage("*"+event.getAuthor().getName()+"* shared a .gif of *'"+query.replace("-", " ") + "'*: " +url).queue();	
						try {
							event.getMessage().deleteMessage();
						} catch (Exception e) {
							// FIXME: handle exception
							System.out.println("Cannot delete message, probably because of private message channel");
						}

					} catch (Exception e) {
						// FIXME Auto-generated catch block
						channel.sendMessage("Error with ;gif command. Use ';help gif' to get help with the command, wither here or in PM").queue();
					}
					return;
				}

				//Up command
				else if(content.toLowerCase().equals(";up")){
					channel.sendMessage("Yes, I am online. I am on the following channels: ").queue();
					for (int i = 0; i < event.getJDA().getTextChannels().size(); i++) {
						channel.sendMessage(event.getJDA().getTextChannels().get(i).getName() + " - "+channel.getJDA().getTextChannels().get(i).getGuild().getName()).queue();
					}
					return;
				}

				//Help command
				if(content.toLowerCase().contains(";help")&&content.toLowerCase().substring(0, 5).equals(";help")){
					if(content.length()>";help".length()){
						String argument = content.substring(6).toLowerCase();
						if(argument.equals("reddit")||argument.equals(";reddit")){
							//if help about reddit feature
							channel.sendMessage("The **Reddit feature** is very simple. You just post a reddit link for an image/gif, and I will"
									+ " send the direct link to the content, resulting in the content being visible in the chat. Textposts will just be ignored").queue();
							return;
						}
						else if (argument.equals("gif")||argument.equals(";gif")) {
							//if help about ;gif
							channel.sendMessage("With the **;gif** feature, you just follow the command with a space, and then type your search quotas for the gif. "
									+ "I will then send the first gif meeting that criteria. You can either separate the quotas with spaces, or with -").queue();
							return;
						}
						else if (argument.equals("up")||argument.equals(";up")) {
							//if help about ;up
							channel.sendMessage("The **;up** command is only used to check if I am awake. If more than one of me replies,"
									+ " or I don't reply att all, something is spooky").queue();
							return;
						}
						else if (argument.length()>1) {
							channel.sendMessage("Sorry, but your argument did not get a match").queue();
						}
					}
					String command = "";
					String[] features = {"Reddit",";gif",";up"};
					for (int i = 0; i < features.length; i++) {
						command = command+ features[i]+", ";	
					}
					command=command+" ;help";
					channel.sendMessage("Hello. I am a very friendly bot. I have some special features (**"+command+"**) that you can use. Send a ;help "
							+ "followed by one of the features listed, to see specified help for that command. Can also be done in PM").queue();
					return;
				}

				else{
					channel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
				}
			}
		}
	}

	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

		PrivateChannel channel=event.getChannel();
		String content = event.getMessage().getContent();

		if(!event.getAuthor().getName().equals("KakansBot")){
			if(!Character.toString(content.charAt(0)).equals(";")){
				channel.sendMessage("Sorry, you did not start your message with the \";\" character. I am a bot, and will only accept commands starting with ;"
						+ " You can use ;help for example, to see what commands you can use. Uppercase or lowercase does not matter").queue();
				return;
			}
		}
	}














}
