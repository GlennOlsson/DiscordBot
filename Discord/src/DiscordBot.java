
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.events.message.priv.*;
import net.dv8tion.jda.core.hooks.*;

public class DiscordBot extends ListenerAdapter{

	public static void main(String[] args) {
		//		new Test();
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken("MjgyMTE2NTYzMjY2NDM3MTIw.C4m_Kw.R-8jmpM6wycnqX0xGvv_wNYjoJ0").addListener(new DiscordBot()).buildBlocking();

		} catch (Exception e) {
			e.printStackTrace();
		}

		//		jda.getGuildsByName("Kakanistan", true).get(0).getTextChannels().get(0);
		TextChannel channel=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);
		//		channel = jda.getTextChannels().get(0);
		channel.sendMessage("Sucessfully logged in!").queue();
	}

	public DiscordBot(){

		//		System.exit(3);
	}

	public void onMessageReceived(MessageReceivedEvent event){

		if(!event.getAuthor().getName().equals("Kakan's Bot")){
			String  contentCase=event.getMessage().getContent(), content = event.getMessage().getContent().toLowerCase();
			MessageChannel channel = event.getChannel();

			channel.sendTyping();

			//Reddit command
			if((content.contains("://reddit")||content.contains("://www.reddit"))&&(content.substring(0,"https://www.reddit".length()).contains("https://www.reddit")||
					content.substring(0,"http://www.reddit".length()).contains("http://www.reddit")||
					content.substring(0,"https://reddit".length()).contains("https://reddit")||
					content.substring(0,"http://reddit".length()).contains("http://reddit"))){
				reddit(channel, event, content);
			}

			if(Character.toString(content.charAt(0)).equals(";")){

				//Clean command
				if(content.toLowerCase().contains(";clean")&&content.toLowerCase().substring(0,";clean".length()).equals(";clean")){
					clean(channel, event, content);
					return;
				}

				//Gif command
				else if(content.toLowerCase().contains(";gif")&&content.toLowerCase().substring(0, 4).equals(";gif")){
					gif(channel, event, content);
					return;
				}

				//Source command
				else if(content.toLowerCase().equals(";source")){
					source(channel);
					return;
				}

				//Help command
				else if(content.toLowerCase().contains(";help")&&content.toLowerCase().substring(0, 5).equals(";help")){
					help(channel, event, content);
					return;
				}

				//Up command
				else if(content.toLowerCase().equals(";up")){
					//					up(channel, event, content);
					return;
				}

				//				else{
				//					channel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
				//				}
			}		
		}
	}

	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

		PrivateChannel channel=event.getChannel();
		String content = event.getMessage().getContent();

		if(!event.getAuthor().getName().equals("Kakan's Bot")){

			channel.sendTyping();

			if(!Character.toString(content.charAt(0)).equals(";")){
				channel.sendMessage("Sorry, you did not start your message with the \";\" character. I am a bot, and will only accept commands starting with ;"
						+ " You can use ;help for example, to see what commands you can use. Uppercase or lowercase does not matter").queue();
				return;
			}
		}
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		MessageChannel channel = event.getGuild().getTextChannelsByName("general", true).get(0);
		channel.sendMessage("Welcome *"+event.getMember().getAsMention()+"* to "+event.getGuild().getName()+"!").queue();
	}

	public void clean(MessageChannel messageChannel, MessageReceivedEvent event, String content){

		TextChannel channel =event.getTextChannel();

		List<Role> roles = null;
		for (int i = 0; i < channel.getMembers().size(); i++) {		
			if(channel.getMembers().get(i).getUser()==event.getAuthor()){
				roles =channel.getMembers().get(i).getRoles();
				i= channel.getMembers().size()+5;
			}
		}

		String[] roleslist ={"Moderator", "Commissioner", "Server Owner"};
		Boolean permission=false;
		for (int i = 0; i < roles.size(); i++) {
			if(roles.get(i).getName().toLowerCase().equals("moderator")||roles.get(i).getName().toLowerCase().equals("commissioner")||
					roles.get(i).getName().toLowerCase().equals("server owner")){
				permission=true;
				i=roles.size()+5;
			}
		}
		if(permission){
			int amount = 1;
			String argument1="all";
			if(content.contains(" ")){
				try {
					amount=Integer.parseInt(content.split(" ")[1]);
					if (amount>100) {
						amount=100;
					}
				} catch (Exception e) {
					// FIXME: handle exception
					event.getAuthor().getPrivateChannel().sendMessage("Error in argument, deleting one").queue();;
				}
				if(content.split(" ").length>1){
					//If at least length=3
					if(content.split(" ")[2].toLowerCase().equals("bots")||content.split(" ")[2].toLowerCase().equals("users")||
							content.split(" ")[2].toLowerCase().equals("all")){
						argument1=content.split(" ")[2].toLowerCase();
					}

				}
				if(content.split(" ").length==4){
	
					for (int i = 0; i < channel.getMembers().size(); i++) {
						if(channel.getMembers().get(i).getNickname().toLowerCase().equals(content.split(" ")[2].toLowerCase())){
							
						}
					}
				}
				else if (content.split(" ").length>4) {
					channel.sendMessage("To many arguments *"+event.getAuthor().getAsMention() +"*, aborting command").queue();
					return;
				}
			}
			else{
				//					event.getAuthor().getPrivateChannel().sendMessage("No amount specified, deleting one").queue();;
			}

			//				channel.getHistory().

			MessageHistory history = channel.getHistory();
			channel.getHistory().retrievePast(100).queue();
			List<Message> historyList = null;
			try {
				historyList=history.retrievePast(100).block();
			} catch (Exception e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
			event.getMessage().deleteMessage().queue();
			for (int i = 1; i < amount+1; i++) {
				if(argument1.equals("bots")){
					if(historyList.get(i).getAuthor().isBot()){
						historyList.get(i).deleteMessage().queue();	
					}
					else{
						amount++;
					}
				}
				else if (argument1.equals("users")) {
					if(!historyList.get(i).getAuthor().isBot()){
						historyList.get(i).deleteMessage().queue();	
					}
					else{
						amount++;
					}
				}
				else {
					//if all user's messages shall go
					historyList.get(i).deleteMessage().queue();	
				}
			}
			System.err.println(amount+" = amount");
		}
		else {
			//				channel.sendMessage("You are not authorized to execute that command, *"+event.getAuthor().getAsMention()+"*. Contact a Moderator").queue();
			return;
		}
	}

	public void reddit(MessageChannel channel, MessageReceivedEvent event, String content){

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
			if(doc.toString().toLowerCase().contains("8+ to view this community")){
				//if NSFW sub
				doc = Jsoup.connect(event.getMessage().getContent()+".rss").userAgent("Mozilla").get();
				url=doc.toString().substring(doc.toString().indexOf("span&gt;&lt;a href=")+"span&gt;&lt;a href=".length()+1,
						doc.toString().indexOf("&gt;[link]&lt;/a&gt;&lt;")-1).replaceAll("amp;amp;", "");
				title=doc.select("title").get(1).text();

				if(!url.contains("www.reddit.com")){
					//if not textpost
					System.out.println(url + " = RUL");

					System.out.println(url.charAt(url.length()-5) + "  -- "+url.charAt(url.length()-4));

					if(url.contains("imgur.com")&&(Character.toString(url.charAt(url.length()-5)).equals(".")||
							Character.toString(url.charAt(url.length()-4)).equals("."))){
						url=url.substring(0,url.lastIndexOf("."));
					}

					channel.sendMessage("*"+event.getAuthor().getName()+"* shared (**NSFW POST**): **"+title+"** - "+url).queue();
					event.getMessage().deleteMessage().queue();
				}
			}
			else{
				//if SFW sub
				//url=doc.toString().substring(doc.toString().indexOf("span&gt;&lt;a href=")+"span&gt;&lt;a href=".length()+1,
				//doc.toString().indexOf("&gt;[link]&lt;/a&gt;&lt;")-1).replaceAll("amp;amp;", "");

				url = doc.select(".title > a").attr("href");
				title=doc.select(".title > a").text();
				System.out.println(url + " = RUL");
				if(!url.substring(0,3).equals("/r/")){
					//If not textpost

					if(url.contains("imgur.com")&&(Character.toString(url.charAt(url.length()-5)).equals(".")||
							Character.toString(url.charAt(url.length()-4)).equals("."))&&url.substring(url.lastIndexOf(".")+1,url.length()).equals("mp4")){
						url=url.substring(0,url.lastIndexOf(".mp4"));
					}

					channel.sendMessage("*"+event.getAuthor().getName()+"* shared: **"+title+"** - "+url).queue();
					event.getMessage().deleteMessage().queue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;

	}

	public void gif(MessageChannel channel, MessageReceivedEvent event, String content) {
		if(content.length()>";gif".length()&&!Character.toString(content.charAt(";gif".length())).equals(" ")){
			//if the character after ;help is not *space*
			channel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
			return;
		}
		Document doc=null;
		String url=null, query=null;
		try {
			query = content.substring(5, content.length()).replace(" ", "-");
			doc = Jsoup.connect("https://www.tenor.co/search/"+query+"-gifs").userAgent("Chrome").get();
			url = "https://www.tenor.co/"+doc.select("#view > div > div.center-container.search > div > div > div:nth-child(1) > figure:nth-child(1) > a").attr("href");

			channel.sendMessage("*"+event.getAuthor().getName()+"* shared a .gif of *'"+query.replace("-", " ") + "'*: " +url).queue();	
			try {
				event.getMessage().deleteMessage().queue();;
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

	public void source(MessageChannel channel){
		channel.sendMessage("Nice, you are qurious! Here's the link to github: https://github.com/kakan9898/DiscordBot").queue();
		return;
	}

	public void up(MessageChannel channel, MessageReceivedEvent event, String content){
		channel.sendMessage("Yes, I am online. I am on the following channels: ").queue();
		for (int i = 0; i < event.getJDA().getTextChannels().size(); i++) {
			channel.sendMessage(event.getJDA().getTextChannels().get(i).getName() + " - "+channel.getJDA().getTextChannels().get(i).getGuild().getName()).queue();
		}
		return;


	}

	public void help(MessageChannel channel, MessageReceivedEvent event, String content) {
		if(content.length()>";help".length()){
			if(!Character.toString(content.charAt(";help".length())).equals(" ")){
				//if the character after ;help is not *space*
				channel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
				return;
			}
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
			//			else if (argument.equals("up")||argument.equals(";up")) {
			//				//if help about ;up
			//				channel.sendMessage("The **;up** command is only used to check if I am awake. If more than one of me replies,"
			//						+ " or I don't reply att all, something is spooky").queue();
			//				return;
			//			}
			else if (argument.equals("source")||argument.equals(";source")) {
				channel.sendMessage("You can send **;source** to get the link to my source code").queue();
				return;
			}

			else if (argument.equals("clean")||argument.equals(";clean")) {
				channel.sendMessage("Whith **;clean**, you can remove a certain amount of messages, from the channel. You can specify both the ammount, and "
						+ "messages from what kind of account that shall be removed. You must be authorized to use this."
						+ " Use like: ;clean [1-100] [bots,users,all]").queue();
				return;
			}

			else if (argument.length()>1) {
				channel.sendMessage("Sorry, but your argument did not get a match").queue();
			}
		}
		String command = "";
		String[] features = {"Reddit",";gif",";source",";clean"};
		for (int i = 0; i < features.length; i++) {
			command = command+ features[i]+", ";	
		}
		command=command+" ;help";
		channel.sendMessage("Hello. I am a very friendly bot. I have some special features (**"+command+"**) that you can use. Send a ;help "
				+ "followed by one of the features listed, to see specified help for that command. Can also be done in PM").queue();

		return;

	}



}
