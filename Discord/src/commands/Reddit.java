package commands;

import org.jsoup.*;
import org.jsoup.nodes.*;

import backend.*;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

public class Reddit {

	public static void main(String[] args) {
		// FIXME Auto-generated method stub

	}
	public Reddit(MessageChannel channel, MessageReceivedEvent event, String content){

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

			doc = Jsoup.connect(Return.convertUrl(event.getMessage().getContent())).userAgent("Chrome").get();
			if(doc.toString().toLowerCase().contains("8+ to view this community")){
				//-- if NSFW sub --
				doc = Jsoup.connect(Return.convertUrl(event.getMessage().getContent())+".rss").userAgent("Mozilla").get();

				url=doc.toString().substring(doc.toString().indexOf("span&gt;&lt;a href=")+"span&gt;&lt;a href=".length()+1,
						doc.toString().indexOf("&gt;[link]&lt;/a&gt;&lt;")-1).replaceAll("amp;amp;", "");
				title=doc.select("title").get(1).text();


				if(!url.contains("www.reddit.com")){

					//if not textpost

					new Print(url + " = RUL", false);

					if(url.contains("imgur.com")){

						String url2=null;

						if(url.length()-url.lastIndexOf(".")<=5){

							new Print("<=5", false);

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
							new Print(url2 + " ==== URL2", false);
						}
						if(url.length()-url.lastIndexOf(".")>5){
							new Print(">5", false);
							try {
								//Tries to get .zoom class (the class of the link if it's a picture

								Document doc2=null;

								try {

									doc2 = Jsoup.connect(Return.convertUrl(url)).userAgent("Chrome").get();
								} catch (Exception e) {
									// FIXME Auto-generated catch block;
									event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
									new Logg(e, content, event.getMessage().getId(), event);
								}

								doc2.select(".zoom").attr("href");
								url2="http:"+doc2.select(".zoom").attr("href");
								new Print(url2+" = URL2", false);
								if(url2.length()<7){
									//Fails because .zoom does not exist --> not picture
									new Print("ERROR", false);
									throw new Exception();
								}

							} catch (Exception e) {
								// FIXME: handle exception
								//Moving image: Gif, Gifv, mp4...
								url2=url+".gifv";

							}
						}
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared (**NSFW POST**): **"+title+"** - "+url2).queue();
						//Check if I have MESSAGE_MANAGE permission, before trying to delete

						for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
							if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
								//Is KakansBot
								if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
									new Print("Cannot delete initial reddit URL message in "+event.getChannel().getName()
											+" channel in "+event.getGuild().getName()+" guild, because lack of MESSAGE_MANAGE", false);
								}
								else {
									event.getMessage().delete().queue();
								}
								i=event.getTextChannel().getMembers().size()+5;
							}
						}

					}
					else{
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared (**NSFW POST**): **"+title+"** - "+url).queue();
						//Check if I have MESSAGE_MANAGE permission, before trying to delete

						for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
							if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
								//Is KakansBot
								if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
									new Print("Cannot delete initial reddit URL message in "+event.getChannel().getName()
											+" channel in "+event.getGuild().getName()+" guild, because lack of MESSAGE_MANAGE", false);
								}
								else {
									event.getMessage().delete().queue();
								}

								i=event.getTextChannel().getMembers().size()+5;
							}
						}
					}
				}
			}
			else{
				//if SFW sub
				//url=doc.toString().substring(doc.toString().indexOf("span&gt;&lt;a href=")+"span&gt;&lt;a href=".length()+1,
				//doc.toString().indexOf("&gt;[link]&lt;/a&gt;&lt;")-1).replaceAll("amp;amp;", "");

				url = doc.select(".title > a").attr("href");
				title=doc.select(".title > a").text();
				new Print(url + " = RUL", false);
				if(!url.substring(0,3).equals("/r/")){
					//If not textpost

					if(url.contains("imgur.com")){

						String url2=null;

						if(url.length()-url.lastIndexOf(".")<=5){

							new Print("<=5", false);

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
							new Print(url2 + " ==== URL2", false);
						}
						if(url.length()-url.lastIndexOf(".")>5){
							new Print(">5", false);
							try {
								//Tries to get .zoom class (the class of the link if it's a picture

								Document doc2=null;

								try {

									doc2 = Jsoup.connect(Return.convertUrl(url)).userAgent("Chrome").get();
								} catch (Exception e) {
									// FIXME Auto-generated catch block
									event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
									new Logg(e, content, event.getMessage().getId(), event);
								}

								doc2.select(".zoom").attr("href");
								url2="http:"+doc2.select(".zoom").attr("href");
								new Print(url2+" = URL2", false);
								if(url2.length()<7){
									//Fails because .zoom does not exist --> not picture
									new Print("ERROR", false);
									throw new Exception();
								}

							} catch (Exception e) {
								// FIXME: handle exception
								//Moving image: Gif, Gifv, mp4...
								url2=url+".gifv";
							}
						}
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared: **"+title+"** - "+url2).queue();
						//Check if I have MESSAGE_MANAGE permission, before trying to delete

						for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
							if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
								//Is KakansBot
								if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
									new Print("Cannot delete initial reddit URL message in "+event.getChannel().getName()
											+" channel in "+event.getGuild().getName()+" guild, because lack of MESSAGE_MANAGE", false);
								}
								else {
									event.getMessage().delete().queue();
								}
								i=event.getTextChannel().getMembers().size()+5;
							}
						}

					}

					else{
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared: **"+title+"** - "+url).queue();
						//Check if I have MESSAGE_MANAGE permission, before trying to delete

						for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
							if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
								//Is KakansBot
								if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
									new Print("Cannot delete initial reddit URL message in "+event.getChannel().getName()
											+" channel in "+event.getGuild().getName()+" guild, because lack of MESSAGE_MANAGE", false);
								}
								else {
									event.getMessage().delete().queue();
								}
								i=event.getTextChannel().getMembers().size()+5;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId());
			new Logg(e, content, event.getMessage().getId(), event);
		}
		return;
	}	
}
