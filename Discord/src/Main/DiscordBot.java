package Main;
/* ----------TODO

Add prefix to help command, and reply with just the prefix if not authorized
--------

Divide all these methods into classes in Main package

 */


import Main.RetrieveSetting.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.events.message.priv.*;
import net.dv8tion.jda.core.hooks.*;

public class DiscordBot extends ListenerAdapter{
	

	public static void main(String[] args) {
		//		new Test();
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(RetrieveSetting.getKey("oath",JSONDocument.secret)).addListener(new DiscordBot()).buildBlocking();
		} catch (Exception e) {
			LoggExceptions.Logg(e, "JDA Builder", "JDA Builder", null);
		}
		TextChannel channel=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);

		channel.sendMessage("Sucessfully logged in!").queue();

		if(System.getProperty("os.name").toLowerCase().contains("linux")){
		RetrieveSetting.setKey("runCount", Integer.toString(Integer.parseInt(RetrieveSetting.getKey("runCount", JSONDocument.setting))+1));
		}

	}

	public void onReady(ReadyEvent event) {
		super.onReady(event);
		event.getJDA().getPresence().setGame(Game.of("Send ;help"));
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		event.getJDA().getPresence().setGame(Game.of("Send ;help"));
		print("Reconected", false);
	}
	
	public static void print(String message, Boolean isErrPrint) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("d/M - HH:mm:ss");
		String currentTime =sdf.format(cal.getTime());
		
		if(isErrPrint){
			System.err.println("Printed: " + message + " at: "+currentTime);
		}
		else{
			System.out.println("Printed: " + message + " at: "+currentTime);
		}
		
	}

	public DiscordBot(){

		//		System.exit(3);
	}

	public void onMessageReceived(MessageReceivedEvent event){

		if(!event.getAuthor().getName().equals("Kakan's Bot")){
			String  contentCase=event.getMessage().getContent(), content = event.getMessage().getContent().toLowerCase();
			MessageChannel channel = event.getChannel();

			channel.sendTyping();

			if(content.toLowerCase().equals("prefix")){
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//Private channel
					channel.sendMessage("The current prefix on our private chat is :\""+getPrefix(channel.getId())+"\"").queue();
				}
				else{
					//Not private
					channel.sendMessage("The current prefix on "+event.getGuild().getName()+" is :\""+getPrefix(event.getGuild().getId())+"\"").queue();
				}
			}

			//Reddit command
			if((content.contains("://reddit")||content.contains("://www.reddit"))&&(content.substring(0,"https://www.reddit".length()).contains("https://www.reddit")||
					content.substring(0,"http://www.reddit".length()).contains("http://www.reddit")||
					content.substring(0,"https://reddit".length()).contains("https://reddit")||
					content.substring(0,"http://reddit".length()).contains("http://reddit"))){
				reddit(channel, event, content);
				return;
			}

			//Get's prefix
			String prefix=";";
			if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
				//if privatechannel ---> no guildId
				prefix=getPrefix(channel.getId());
			}
			else {
				//Not private --> has guild Id
				prefix=getPrefix(event.getGuild().getId());
			}

			onMessageReceivedPrefix(event, prefix, content, channel);




		}
	}

	private void onMessageReceivedPrefix(MessageReceivedEvent event, String prefix, String content, MessageChannel channel) {
		// Made so that it can check ; as a prefix, if the first check fails. This way, ; is always a prefix

		if(content.length()>prefix.length()&&content.substring(0,prefix.length()).equals(prefix)){
			//; commands

			String command = content.substring(prefix.length()), afterCommand = "";
			if(command.contains(" ")){
				command=command.split(" ")[0];
				afterCommand = content.substring(prefix.length()+command.length()+1);
				print("Aftercommand=\""+afterCommand+"\"", false);
			}

			switch (command) {
			case "clean":
				clean(channel, event, content);
				break;

			case "gif":
				gif(channel, event, content);
				break;

			case "source":
				source(channel);
				break;

			case "prefix":
				prefix(channel, event, content, afterCommand);
				break;

			case "up":
				//					up(channel, event, content);
				break;

			case "help":
				help(event, content);
				break;
			}

			//				else{
			//					channel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
			//				}
		}
		else {
			if(!prefix.equals(";")){
				//Call event but with ; as command
				onMessageReceivedPrefix(event, ";", content, channel);
			}
		}
	}

	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

		PrivateChannel channel=event.getChannel();
		String content = event.getMessage().getContent();

		if(!event.getAuthor().getName().equals("Kakan's Bot")){

			channel.sendTyping();

			//Already checked if private
			String prefix=getPrefix(channel.getId());

			if(content.equals("prefix")){
				return;
			}

			else if(content.length()>=prefix.length()){
				if(!content.substring(0, prefix.length()).equals(prefix)&&!Character.toString(content.charAt(0)).equals(";")){
					channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
							+ "commands starting with "+prefix+". You can use "+prefix+"help for example, to see what commands you can use."
							+ " Uppercase or lowercase does not matter").queue();
					return;
				}
			}
			else {
				if(!Character.toString(content.charAt(0)).equals(";"))
				channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
						+ "commands starting with "+prefix+". You can use \""+prefix+"help\" for example, to see what commands you can use."
						+ " Uppercase or lowercase does not matter").queue();
				return;
			}
		}
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		MessageChannel channel = event.getGuild().getTextChannelsByName("general", true).get(0);
		channel.sendMessage("Welcome *"+event.getMember().getAsMention()+"* to "+event.getGuild().getName()+"!").queue();

		event.getGuild().getTextChannelsByName("modlog", true).get(0).sendMessage("The user **"+event.getMember().getUser().getName()+
				"** with the # id **"+ event.getMember().getUser().getDiscriminator() + "** and long id as **"+event.getMember().getUser().getId()
				+"** just joined us").queue();
	}

	public void clean(MessageChannel messageChannel, MessageReceivedEvent event, String content){

		TextChannel channel =event.getTextChannel();
		String[] roleList ={"Moderator", "Commissioner", "Server Owner"};

		if(isAuthorized(channel, event, content, roleList)){
			int amount = 1;
			String argument1="all", argument2="all";

			//Arguments
			if(content.contains(" ")){
				try {
					amount=Integer.parseInt(content.split(" ")[1]);
					if (amount>100) {
						amount=100;
					}
				} catch (Exception e) {
					// FIXME: handle exception

					event.getAuthor().getPrivateChannel().sendMessage("Error in argument, deleting one").queue();
					LoggExceptions.Logg(e, content, event.getMessage().getId(), event);
				}
				if(content.split(" ").length>=3){
					//If at least length=3
					if(content.split(" ")[2].toLowerCase().equals("bots")||content.split(" ")[2].toLowerCase().equals("users")||
							content.split(" ")[2].toLowerCase().equals("all")){
						argument1=content.split(" ")[2].toLowerCase();
					}

				}
				if(content.split(" ").length>=4){
					argument2=content.substring(content.indexOf(content.split(" ")[3].toLowerCase()));
					print(argument2 + " == arg 2", false);
					Boolean hasMemeber=false;
					for (int i = 0; i < channel.getMembers().size(); i++) {
						if(channel.getMembers().get(i).getUser().getName().toLowerCase().equals(argument2)){
							print("CHANNEL HAS USER", false);
							hasMemeber=true;
						}
					}
					if(!hasMemeber){
						channel.sendMessage("This channel does not have a user with that name. Aborting command").queue();
						return;
					}
				}
				//				else if (content.split(" ").length>5) {
				//					channel.sendMessage("To many arguments *"+event.getAuthor().getAsMention() +"*, aborting command").queue();
				//					return;
				//				}
			}
			else{
				//					event.getAuthor().getPrivateChannel().sendMessage("No amount specified, deleting one").queue();;
			}

			//				channel.getHistory().

			MessageHistory history = channel.getHistory();
			channel.getHistory().retrievePast(100).queue();
			List<Message> historyList = null;
			try {
				historyList=history.retrievePast(100).complete(true);
			} catch (Exception e) {
				// FIXME Auto-generated catch block
				channel.sendMessage("Error, contact Kakan with id: "+event.getMessage().getId());
				LoggExceptions.Logg(e, content, event.getMessage().getId(), event);
			}
			event.getMessage().delete().queue();
			for (int i = 1; i < amount+1; i++) {
				if(argument1.equals("bots")){
					if(historyList.get(i).getAuthor().isBot()){
						if(argument2.equals("all")){
							historyList.get(i).delete().queue();
						}
						else {
							if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
								historyList.get(i).delete().queue();
							}
							else {
								amount++;
							}
						}
					}
					else{
						amount++;
					}
				}
				else if (argument1.equals("users")) {
					if(!historyList.get(i).getAuthor().isBot()){
						if(argument2.equals("all")){
							historyList.get(i).delete().queue();
						}
						else {
							if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
								historyList.get(i).delete().queue();
							}
							else {
								amount++;
							}
						}
					}
					else{
						amount++;
					}
				}
				else {
					//if all user's messages shall go
					if(argument2.equals("all")){
						historyList.get(i).delete().queue();
					}
					else {
						if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
							historyList.get(i).delete().queue();
						}
						else {
							amount++;
						}
					}
				}
			}
			print(amount+" = amount", true);
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
			
			URL uri = new URL(event.getMessage().getContent());
			URI uri2 = new URI(uri.getProtocol(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getRef());
			
			doc = Jsoup.connect(uri2.toASCIIString()).userAgent("Chrome").get();
			if(doc.toString().toLowerCase().contains("8+ to view this community")){
				//-- if NSFW sub --
				doc = Jsoup.connect(uri2.toASCIIString()+".rss").userAgent("Mozilla").get();

				url=doc.toString().substring(doc.toString().indexOf("span&gt;&lt;a href=")+"span&gt;&lt;a href=".length()+1,
						doc.toString().indexOf("&gt;[link]&lt;/a&gt;&lt;")-1).replaceAll("amp;amp;", "");
				title=doc.select("title").get(1).text();


				if(!url.contains("www.reddit.com")){

					//if not textpost

					print(url + " = RUL", false);

					if(url.contains("imgur.com")){

						String url2=null;

						if(url.length()-url.lastIndexOf(".")<=5){

							print("<=5", false);

							//Makes imgur.com/IDNUMBER.mp4 --> imgur.com/IDNUMBER.gifv
							if(url.substring(url.lastIndexOf(".")).equals(".mp4")){
								url2=url.replace(".mp4", ".gifv");
							}
							//Makes imgur.com/IDNUMBER.gif --> imgur.com/IDNUMBER.gifv
							else if (url.substring(url.lastIndexOf(".")).equals(".gif")) {
								url2=url.replace(".gif",".gifv");
							}
							else {
								url2=url;
							}
							print(url2 + " ==== URL2", false);
						}
						if(url.length()-url.lastIndexOf(".")>5){
							print(">5", false);
							try {
								//Tries to get .zoom class (the class of the link if it's a picture

								Document doc2=null;

								try {
									URL uri3 = new URL(url);
									URI uri4 = new URI(uri3.getProtocol(), uri3.getUserInfo(), uri3.getHost(), uri3.getPort(),
											uri3.getPath(), uri3.getQuery(), uri3.getRef());
									
									doc2 = Jsoup.connect(uri2.toASCIIString()).userAgent("Chrome").get();
								} catch (Exception e) {
									// FIXME Auto-generated catch block;
									event.getChannel().sendMessage("Error was caught. Contact Kakan with id "+event.getMessage().getId());
									LoggExceptions.Logg(e, content, event.getMessage().getId(), event);
								}

								doc2.select(".zoom").attr("href");
								url2="http:"+doc2.select(".zoom").attr("href");
								print(url2+" = URL2", false);
								if(url2.length()<7){
									//Fails because .zoom does not exist --> not picture
									print("ERROR", false);
									throw new Exception();
								}

							} catch (Exception e) {
								// FIXME: handle exception
								//Moving image: Gif, Gifv, mp4...
								url2=url+".gifv";

							}
						}
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared (**NSFW POST**): **"+title+"** - "+url2).queue();
						event.getMessage().delete().queue();

					}
					else{
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared (**NSFW POST**): **"+title+"** - "+url).queue();
						event.getMessage().delete().queue();
					}
				}
			}
			else{
				//if SFW sub
				//url=doc.toString().substring(doc.toString().indexOf("span&gt;&lt;a href=")+"span&gt;&lt;a href=".length()+1,
				//doc.toString().indexOf("&gt;[link]&lt;/a&gt;&lt;")-1).replaceAll("amp;amp;", "");

				url = doc.select(".title > a").attr("href");
				title=doc.select(".title > a").text();
				print(url + " = RUL", false);
				if(!url.substring(0,3).equals("/r/")){
					//If not textpost

					if(url.contains("imgur.com")){

						String url2=null;

						if(url.length()-url.lastIndexOf(".")<=5){

							print("<=5", false);

							//Makes imgur.com/IDNUMBER.mp4 --> imgur.com/IDNUMBER.gifv
							if(url.substring(url.lastIndexOf(".")).equals(".mp4")){
								url2=url.replace(".mp4", ".gifv");
							}
							//Makes imgur.com/IDNUMBER.gif --> imgur.com/IDNUMBER.gifv
							else if (url.substring(url.lastIndexOf(".")).equals(".gif")) {
								url2=url.replace(".gif",".gifv");
							}
							else {
								url2=url;
							}
							print(url2 + " ==== URL2", false);
						}
						if(url.length()-url.lastIndexOf(".")>5){
							print(">5", false);
							try {
								//Tries to get .zoom class (the class of the link if it's a picture

								Document doc2=null;

								try {
									URL uri3 = new URL(url);
									URI uri4 = new URI(uri3.getProtocol(), uri3.getUserInfo(), uri3.getHost(), uri3.getPort(),
											uri3.getPath(), uri3.getQuery(), uri3.getRef());
									
									doc2 = Jsoup.connect(uri2.toASCIIString()).userAgent("Chrome").get();
								} catch (Exception e) {
									// FIXME Auto-generated catch block
									event.getChannel().sendMessage("Error was caught. Contact Kakan with id "+event.getMessage().getId());
									LoggExceptions.Logg(e, content, event.getMessage().getId(), event);
								}

								doc2.select(".zoom").attr("href");
								url2="http:"+doc2.select(".zoom").attr("href");
								print(url2+" = URL2", false);
								if(url2.length()<7){
									//Fails because .zoom does not exist --> not picture
									print("ERROR", false);
									throw new Exception();
								}

							} catch (Exception e) {
								// FIXME: handle exception
								//Moving image: Gif, Gifv, mp4...
								url2=url+".gifv";
							}
						}
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared: **"+title+"** - "+url2).queue();
						event.getMessage().delete().queue();

					}

					else{
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared: **"+title+"** - "+url).queue();
						event.getMessage().delete().queue();
					}
				}
			}
		} catch (Exception e) {
			event.getChannel().sendMessage("Error was caught. Contact Kakan with id "+event.getMessage().getId());
			LoggExceptions.Logg(e, content, event.getMessage().getId(), event);
		}

		return;

	}

	public void gif(MessageChannel channel, MessageReceivedEvent event, String content) {
		Document doc=null;
		String url=null, query=null;
		try {
			query = content.substring(5, content.length()).replace(" ", "-");
			
			URL uri3 = new URL("https://www.tenor.co/search/"+query+"-gifs");
			URI uri4 = new URI(uri3.getProtocol(), uri3.getUserInfo(), uri3.getHost(), uri3.getPort(),
					uri3.getPath(), uri3.getQuery(), uri3.getRef());
			
			doc = Jsoup.connect(uri4.toASCIIString()).userAgent("Chrome").get();
			url = "https://www.tenor.co/"+doc.select("#view > div > div.center-container.search > div > div > div:nth-child(1) > figure:nth-child(1) > a").attr("href");

			channel.sendMessage("*"+event.getAuthor().getName()+"* shared a .gif of *'"+query.replace("-", " ") + "'*: " +url).queue();
			try {
				if(!event.getChannel().getType().equals(ChannelType.PRIVATE)){
					event.getMessage().delete().queue();;
				}
			} catch (Exception e) {
				// FIXME: handle exception
				event.getChannel().sendMessage("Error was caught. Contact Kakan with id "+event.getMessage().getId());
				LoggExceptions.Logg(e, content, event.getMessage().getId(), event);
			}

		} catch (Exception e) {
			// FIXME Auto-generated catch block
			channel.sendMessage("Error with ;gif command. Use ';help gif' to get help with the command, wither here or in PM").queue();

			LoggExceptions.Logg(e, content, event.getMessage().getId(), event);

		}
		return;

	}

	public void source(MessageChannel channel){
		channel.sendMessage("Nice, you are qurious! Here's the link to github: https://github.com/kakan9898/DiscordBot").queue();
		return;
	}

	public void prefix(MessageChannel channel, MessageReceivedEvent event, String content, String newPrefix) {
		String[] roleslist ={"Moderator", "Commissioner", "Server Owner"};
		if(isAuthorized(event.getTextChannel(), event, content, roleslist)){
			print("HALLELULIA", false);
			if(!newPrefix.equals("")){
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//if privatechannel ---> no guildId
					setPrefix(channel.getId(),newPrefix);
				}
				else {
					//Not private --> has guild Id
					setPrefix(event.getGuild().getId(),newPrefix);
				}
				channel.sendMessage("Prefix successfully changed to \""+newPrefix+"\"").queue();
			}
		}
	}

	public void up(MessageChannel channel, MessageReceivedEvent event, String content){
		channel.sendMessage("Yes, I am online. I am on the following channels: ").queue();
		for (int i = 0; i < event.getJDA().getTextChannels().size(); i++) {
			channel.sendMessage(event.getJDA().getTextChannels().get(i).getName() + " - "+channel.getJDA().getTextChannels().get(i).getGuild().getName()).queue();
		}
		return;


	}

	public void help(MessageReceivedEvent event, String content) {

		PrivateChannel privateChannel=null;

		if(event.getAuthor().hasPrivateChannel()){
			privateChannel=event.getAuthor().getPrivateChannel();
		}
		else {
			event.getAuthor().openPrivateChannel().queue();
			privateChannel=event.getAuthor().getPrivateChannel();
		}

		if(content.length()>";help".length()){
			//Doesn't work with other prefixes?

			if(!Character.toString(content.charAt(";help".length())).equals(" ")){
				//if the character after ;help is not *space*
				privateChannel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
				return;
			}
			String argument = content.substring(6).toLowerCase();
			if(argument.equals("reddit")||argument.equals(";reddit")){
				//if help about reddit feature
				privateChannel.sendMessage("The **Reddit feature** is very simple. You just post a reddit link for an image/gif, and I will"
						+ " send the direct link to the content, resulting in the content being visible in the chat. Textposts will just be ignored").queue();
				return;
			}
			else if (argument.equals("gif")||argument.equals(";gif")) {
				//if help about ;gif
				privateChannel.sendMessage("With the **;gif** feature, you just follow the command with a space, and then type your search quotas for the gif. "
						+ "I will then send the first gif meeting that criteria. You can either separate the quotas with spaces, or with -").queue();
				return;
			}
			//			else if (argument.equals("up")||argument.equals(";up")) {
			//				//if help about ;up
			//				privateChannel.sendMessage("The **;up** command is only used to check if I am awake. If more than one of me replies,"
			//						+ " or I don't reply att all, something is spooky").queue();
			//				return;
			//			}
			else if (argument.equals("source")||argument.equals(";source")) {
				privateChannel.sendMessage("You can send **;source** to get the link to my source code").queue();
				return;
			}

			else if (argument.equals("clean")||argument.equals(";clean")) {
				privateChannel.sendMessage("Whith **;clean**, you can remove a certain amount of messages, from the privateChannel. You can specify both the ammount, and "
						+ "messages from what kind of account that shall be removed. You must be authorized to use this."
						+ " Use like: ;clean [1-100] [bots,users,all]").queue();
				return;
			}

			else if (argument.length()>1) {
				privateChannel.sendMessage("Sorry, but your argument did not get a match").queue();
			}
		}
		String command = "";
		String[] features = {"Reddit",";gif",";source",";clean"};
		for (int i = 0; i < features.length; i++) {
			command = command+ features[i]+", ";
		}
		command=command+" ;help";
		privateChannel.sendMessage("Hello. I am a very friendly bot. I have some special features (**"+command+"**) that you can use. Send a ;help "
				+ "followed by one of the features listed, to see specified help for that command. Can also be done in PM").queue();

		return;

	}

	@SuppressWarnings("unchecked")
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
			LoggExceptions.Logg(e, "In setPrefix", "Here's string Id: --"+id+"--, and Prefix: --"+prefix+"--", null);
		}
	}

	public String getPrefix(String id) {

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
			LoggExceptions.Logg(e, "In getPrefix", "Here's string Id: --"+id+"--", null);
			return ";";
		}
	}

	public Boolean isAuthorized(TextChannel textChannel, MessageReceivedEvent event, String content, String[] roleList) {
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
