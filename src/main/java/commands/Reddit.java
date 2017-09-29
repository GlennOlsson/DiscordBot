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

/*
 Reddit test länkar. imgur [.*NONE*, .mp4, .jpg, .png, .gif(v)], gfycat, youtube, reddit [.jpg, .png, .mp4, .gif], streamable,
 NSFW post as wel as text post
 
 https://www.reddit.com/r/worldnews/comments/734bbp/puerto_rico_rejects_loan_offers_accusing_hedge/ - any other media
 
 https://www.reddit.com/r/SiliconValleyHBO/comments/730qcz/how_did_jareddonald_celebrate/ - text post
 
 https://www.reddit.com/r/woahdude/comments/730nv5/escher_circle_limit/ - imgur.*NONE*
 https://www.reddit.com/r/oddlysatisfying/comments/735eei/this_group_of_gentlemen_bobbing_their_heads/ - imgur.gifv
 https://www.reddit.com/r/mildlyinteresting/comments/7349fj/hurricane_irma_eroded_away_the_dune_this_pine/ - imgur.jpg
 https://www.reddit.com/r/AdviceAnimals/comments/733lgf/never_hurts_to_use_the_google/ - imgur.png
 
 https://www.reddit.com/r/pics/comments/733k4v/the_mount_rushmore_of_selfies/ - reddit.jpg
 https://www.reddit.com/r/me_irl/comments/733ufb/meirl/ - reddit.png
 https://www.reddit.com/r/firstworldanarchists/comments/730fpq/mad_lass/ - reddit.gif
 
 https://www.reddit.com/r/videos/comments/734xd0/fucking_shit/ - streamamble
 
 https://www.reddit.com/r/AbruptChaos/comments/730528/give_us_another_one/ - youtube
 
 https://www.reddit.com/r/WTF/comments/732qhd/thats_some_serious_faith_you_have_there/ - gfycat
 
 https://www.reddit.com/r/gifs/comments/734jy3/synchronized_backflip/ - NSFW imgur.gifv
 
 String redditTests[] = {
 "https://www.reddit.com/r/SiliconValleyHBO/comments/730qcz/how_did_jareddonald_celebrate/",
 "https://www.reddit.com/r/worldnews/comments/734bbp/puerto_rico_rejects_loan_offers_accusing_hedge/",
 "https://www.reddit.com/r/woahdude/comments/730nv5/escher_circle_limit/",
 "https://www.reddit.com/r/oddlysatisfying/comments/735eei/this_group_of_gentlemen_bobbing_their_heads/",
 "https://www.reddit.com/r/mildlyinteresting/comments/7349fj/hurricane_irma_eroded_away_the_dune_this_pine/",
 "https://www.reddit.com/r/AdviceAnimals/comments/733lgf/never_hurts_to_use_the_google/",
 "https://www.reddit.com/r/pics/comments/733k4v/the_mount_rushmore_of_selfies/",
 "https://www.reddit.com/r/me_irl/comments/733ufb/meirl/",
 "https://www.reddit.com/r/firstworldanarchists/comments/730fpq/mad_lass/",
 "https://www.reddit.com/r/videos/comments/734xd0/fucking_shit/",
 "https://www.reddit.com/r/AbruptChaos/comments/730528/give_us_another_one/",
 "https://www.reddit.com/r/WTF/comments/732qhd/thats_some_serious_faith_you_have_there/",
 "https://www.reddit.com/r/gifs/comments/734jy3/synchronized_backflip/"};
 
 length = 13
 
 */

package commands;

import backend.ErrorLogg;
import backend.Print;
import backend.Return;
import events.MessageEvents;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.rmi.server.ExportException;

public class Reddit {
	
	public Reddit(){
	
	}
	
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
		
		String[] redditMediaURLAndTitle = getRedditMediaURLAndTitle(content);
		
		if(redditMediaURLAndTitle[0].equals("")){
			return;
		}
		
		String redditMediaURL = redditMediaURLAndTitle[0];
		String redditTitile = redditMediaURLAndTitle[1];
		
		
		channel.sendMessage("*" + event.getAuthor().getName() + "* shared: **" + redditTitile + "** - " + redditMediaURL).queue();
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
		
	}
	
	//Returns a list of [RedditMediaURL, RedditTitle]
	public String[] getRedditMediaURLAndTitle(String redditURL) {
		
		Document RedditHTMLocument;
		String url, redditTitle;
		try {
			RedditHTMLocument = Jsoup.connect(Return.convertUrl(redditURL)).userAgent("Chrome").followRedirects(false)
					.cookie("over18", "1").get();
			
			redditTitle = RedditHTMLocument.select(".title > a").text();
			
			url = RedditHTMLocument.select(".title > a").attr("href");
			new Print(url + " = Reddit URL", false);
			if(!url.substring(0, 3).equals("/r/")) {
				//If not textpost
				
				if(url.contains("imgur.com")) {
					
					String imgurURL = null;
					
					if(url.length() - url.lastIndexOf(".") <= 5) {
						
						new Print("<=5", false);
						
						//Makes imgur.com/IDNUMBER.mp4 --> imgur.com/IDNUMBER.gifv
						switch (url.substring(url.lastIndexOf("."))) {
							case ".mp4":
								imgurURL = url.replace(".mp4", ".gifv");
								break;
							//Makes imgur.com/IDNUMBER.gif --> imgur.com/IDNUMBER.gifv
							case ".gif":
								imgurURL = url.replace(".gif", ".gifv");
								break;
							default:
								imgurURL = url;
								break;
						}
						new Print(imgurURL + " = IMGUR URL", false);
					}
					if(url.length() - url.lastIndexOf(".") > 5) {
						new Print(">5", false);
						try {
							//Tries to get .zoom class (the class of the link if it's a picture
							
							Document imgurHTMLDocument = null;
							
							try {
								
								imgurHTMLDocument = Jsoup.connect(Return.convertUrl(url)).userAgent("Chrome").get();
							} catch (Exception e) {
								new ErrorLogg(e, redditURL, "Error in .getRedditMediaURL", null);
							}
							
							imgurHTMLDocument.select(".zoom").attr("href");
							imgurURL = "http:" + imgurHTMLDocument.select(".zoom").attr("href");
							new Print(imgurURL + " = URL2", false);
							if(imgurURL.length() < 7) {
								//Fails because .zoom does not exist --> not picture
								new Print("ERROR", false);
								throw new Exception();
							}
							
						} catch (Exception e) {
							//Moving image: Gif, Gifv, mp4...
							imgurURL = url + ".gifv";
						}
					}
					String urlAndTitle[] = {imgurURL, redditTitle};
					
					
					return urlAndTitle;
					
				} else {
					String urlAndTitle[] = {url, redditTitle};
					return urlAndTitle;
				}
			} else {
				new Print("Is textpost");
			}
		} catch (Exception e) {
			new ErrorLogg(e, "Error in .getRedditMediaURL", "Returning null as RedditMediaURL", null);
		}
		String nulling[] = {"",""};
		return nulling;
	}
}
