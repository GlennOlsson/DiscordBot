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

package commands;

import backend.ErrorLogg;
import backend.Print;
import backend.Return;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Reddit {
	
	public Reddit(MessageChannel channel, MessageReceivedEvent event, String content){
		
		//If reddit post
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			new ErrorLogg(e1, "Cannot sleep", "Error with Thread.sleep in Reddit", null);
		}
		
		if(content.contains(" ")){
			String[] split = content.split(" ");
			if(split.length>2||split[1].length()>0){
				return;
			}
			content=split[0];
		}
		
		Document doc;
		String url, title;
		try {
			
			doc = Jsoup.connect(Return.convertUrl(event.getMessage().getContent())).userAgent("Chrome").followRedirects(false)
					.cookie("over18","1").get();
			
			url = doc.select(".title > a").attr("href");
			title=doc.select(".title > a").text();
			new Print(url + " = RUL", false);
			if(!url.substring(0,3).equals("/r/")) {
				//If not textpost
				
				if(url.contains("imgur.com")) {
					
					String url2 = null;
					
					if(url.length() - url.lastIndexOf(".") <= 5) {
						
						new Print("<=5", false);
						
						//Makes imgur.com/IDNUMBER.mp4 --> imgur.com/IDNUMBER.gifv
						switch (url.substring(url.lastIndexOf("."))) {
							case ".mp4":
								url2 = url.replace(".mp4", ".gifv");
								break;
							//Makes imgur.com/IDNUMBER.gif --> imgur.com/IDNUMBER.gifv
							case ".gif":
								url2 = url.replace(".gif", ".gifv");
								break;
							default:
								url2 = url;
								break;
						}
						new Print(url2 + " ==== URL2", false);
					}
					if(url.length() - url.lastIndexOf(".") > 5) {
						new Print(">5", false);
						try {
							//Tries to get .zoom class (the class of the link if it's a picture
							
							Document doc2 = null;
							
							try {
								
								doc2 = Jsoup.connect(Return.convertUrl(url)).userAgent("Chrome").get();
							} catch (Exception e) {
								event.getChannel().sendMessage("Error was caught. Contact " + event.getJDA().getUserById("165507757519273984").getAsMention() + " with id " + event.getMessage().getId()).submit();
								new ErrorLogg(e, content, event.getMessage().getId(), event);
							}
							
							doc2.select(".zoom").attr("href");
							url2 = "http:" + doc2.select(".zoom").attr("href");
							new Print(url2 + " = URL2", false);
							if(url2.length() < 7) {
								//Fails because .zoom does not exist --> not picture
								new Print("ERROR", false);
								throw new Exception();
							}
							
						} catch (Exception e) {
							//Moving image: Gif, Gifv, mp4...
							url2 = url + ".gifv";
						}
					}
					channel.sendMessage("*" + event.getAuthor().getName() + "* shared: **" + title + "** - " + url2).queue();
					//Check if I have MESSAGE_MANAGE permission, before trying to delete
					
					for (int i = 0; i < event.getTextChannel().getMembers().size(); i++) {
						if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
							//Is KakansBot
							if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)) {
								new Print("Cannot delete initial reddit URL message in " + event.getChannel().getName()
										+ " channel in " + event.getGuild().getName() + " guild, because lack of MESSAGE_MANAGE", false);
							} else {
								event.getMessage().delete().queue();
							}
							i = event.getTextChannel().getMembers().size() + 5;
						}
					}
					
				} else {
					channel.sendMessage("*" + event.getAuthor().getName() + "* shared: **" + title + "** - " + url).queue();
					//Check if I have MESSAGE_MANAGE permission, before trying to delete
					
					if(event.getPrivateChannel() != null) {
						new Print("Private channel, cannot delete!");
						return;
					}
					
					for (int i = 0; i < event.getTextChannel().getMembers().size(); i++) {
						if(event.getTextChannel().getMembers().get(i).getUser().equals(event.getJDA().getSelfUser())) {
							//Is KakansBot
							if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)) {
								new Print("Cannot delete initial reddit URL message in " + event.getChannel().getName()
										+ " channel in " + event.getGuild().getName() + " guild, because lack of MESSAGE_MANAGE", false);
							} else {
								event.getMessage().delete().queue();
							}
							i = event.getTextChannel().getMembers().size() + 5;
						}
					}
				}
			} else {
				new Print("Is textpost");
			}
		} catch (Exception e) {
			event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").getAsMention()+" with id "+event.getMessage().getId()).submit();
			new ErrorLogg(e, content, event.getMessage().getId(), event);
		}
		//noinspection UnnecessaryReturnStatement
		return;
	}
}
