package commands;

import java.net.URI;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import main.IO;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Commands{

	public static void main(String[] args) {
		// FIXME Auto-generated method stub

	}
	public static void clean(MessageChannel messageChannel, MessageReceivedEvent event, String content){


		TextChannel channel =event.getTextChannel();
		String[] roleList ={"Moderator", "Commissioner", "Server Owner"};

		if(IO.isAuthorized(channel, event, content, roleList)){
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
					IO.Logg(e, content, event.getMessage().getId(), event);
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
					IO.print(argument2 + " == arg 2", false);
					Boolean hasMemeber=false;
					for (int i = 0; i < channel.getMembers().size(); i++) {
						if(channel.getMembers().get(i).getUser().getName().toLowerCase().equals(argument2)){
							IO.print("CHANNEL HAS USER", false);
							hasMemeber=true;
						}
					}
					if(!hasMemeber){
						channel.sendMessage("This channel does not have a user with that name. Aborting command").queue();
						return;
					}
				}
			}
			
			MessageHistory history = channel.getHistory();
			channel.getHistory().retrievePast(100).queue();
			List<Message> historyList = null;
			try {
				historyList=history.retrievePast(100).complete(true);
			} catch (Exception e) {
				// FIXME Auto-generated catch block
				channel.sendMessage("Error, contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id: "+event.getMessage().getId());
				IO.Logg(e, content, event.getMessage().getId(), event);
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
			IO.print(amount+" = amount", true);
		}
		else {
			//				channel.sendMessage("You are not authorized to execute that command, *"+event.getAuthor().getAsMention()+"*. Contact a Moderator").queue();
			return;
		}
	}

	public static void reddit(MessageChannel channel, MessageReceivedEvent event, String content){

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

					IO.print(url + " = RUL", false);

					if(url.contains("imgur.com")){

						String url2=null;

						if(url.length()-url.lastIndexOf(".")<=5){

							IO.print("<=5", false);

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
							IO.print(url2 + " ==== URL2", false);
						}
						if(url.length()-url.lastIndexOf(".")>5){
							IO.print(">5", false);
							try {
								//Tries to get .zoom class (the class of the link if it's a picture

								Document doc2=null;

								try {
									URL uri3 = new URL(url);
									URI uri4 = new URI(uri3.getProtocol(), uri3.getUserInfo(), uri3.getHost(), uri3.getPort(),
											uri3.getPath(), uri3.getQuery(), uri3.getRef());
									
									doc2 = Jsoup.connect(uri4.toASCIIString()).userAgent("Chrome").get();
								} catch (Exception e) {
									// FIXME Auto-generated catch block;
									event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
									IO.Logg(e, content, event.getMessage().getId(), event);
								}

								doc2.select(".zoom").attr("href");
								url2="http:"+doc2.select(".zoom").attr("href");
								IO.print(url2+" = URL2", false);
								if(url2.length()<7){
									//Fails because .zoom does not exist --> not picture
									IO.print("ERROR", false);
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
				IO.print(url + " = RUL", false);
				if(!url.substring(0,3).equals("/r/")){
					//If not textpost

					if(url.contains("imgur.com")){

						String url2=null;

						if(url.length()-url.lastIndexOf(".")<=5){

							IO.print("<=5", false);

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
							IO.print(url2 + " ==== URL2", false);
						}
						if(url.length()-url.lastIndexOf(".")>5){
							IO.print(">5", false);
							try {
								//Tries to get .zoom class (the class of the link if it's a picture

								Document doc2=null;

								try {
									URL uri3 = new URL(url);
									URI uri4 = new URI(uri3.getProtocol(), uri3.getUserInfo(), uri3.getHost(), uri3.getPort(),
											uri3.getPath(), uri3.getQuery(), uri3.getRef());
									
									doc2 = Jsoup.connect(uri4.toASCIIString()).userAgent("Chrome").get();
								} catch (Exception e) {
									// FIXME Auto-generated catch block
									event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
									IO.Logg(e, content, event.getMessage().getId(), event);
								}

								doc2.select(".zoom").attr("href");
								url2="http:"+doc2.select(".zoom").attr("href");
								IO.print(url2+" = URL2", false);
								if(url2.length()<7){
									//Fails because .zoom does not exist --> not picture
									IO.print("ERROR", false);
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
			event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
			IO.Logg(e, content, event.getMessage().getId(), event);
		}

		return;

	}

	public static void gif(MessageChannel channel, MessageReceivedEvent event, String content) {
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
				event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
				IO.Logg(e, content, event.getMessage().getId(), event);
			}

		} catch (Exception e) {
			// FIXME Auto-generated catch block
			channel.sendMessage("Error with ;gif command. Use ';help gif' to get help with the command, wither here or in PM").queue();

			IO.Logg(e, content, event.getMessage().getId(), event);

		}
		return;

	}

	public static void source(MessageChannel channel){
		channel.sendMessage("Nice, you are qurious! Here's the link to github: https://github.com/kakan9898/DiscordBot").queue();
		return;
	}

	public static void prefix(MessageChannel channel, MessageReceivedEvent event, String content, String newPrefix) {
		String[] roleslist ={"Moderator", "Commissioner", "Server Owner"};
		if(IO.isAuthorized(event.getTextChannel(), event, content, roleslist)){
			IO.print("HALLELULIA", false);
			if(!newPrefix.equals("")){
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//if privatechannel ---> no guildId
					IO.setPrefix(channel.getId(),newPrefix);
				}
				else {
					//Not private --> has guild Id
					IO.setPrefix(event.getGuild().getId(),newPrefix);
				}
				channel.sendMessage("Prefix successfully changed to \""+newPrefix+"\"").queue();
			}
		}
	}

	public static void up(MessageChannel channel, MessageReceivedEvent event, String content){
		channel.sendMessage("Yes, I am online. I am on the following channels: ").queue();
		for (int i = 0; i < event.getJDA().getTextChannels().size(); i++) {
			channel.sendMessage(event.getJDA().getTextChannels().get(i).getName() + " - "+channel.getJDA().getTextChannels().get(i).getGuild().getName()).queue();
		}
		return;


	}

	public static void help(MessageReceivedEvent event, String content) {

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


	
}
