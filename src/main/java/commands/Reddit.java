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

import RedditAPI.RedditPost;
import backend.Logger;
import backend.Return;
import main.DiscordBot;
import main.Test;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Reddit {
	
	public Reddit(){
	
	}
	
	public static void Reddit(MessageChannel channel, MessageReceivedEvent event, String content){
		
		//If reddit post
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e1) {
//			Logger.logError(e1, "Cannot sleep", "Error with Thread.sleep in Reddit", null);
//		}
		
		if(content.contains(" ")){
			String[] split = content.split(" ");
			if(split.length>2||split[1].length()>0){
				return;
			}
			content=split[0];
		}
		
		String id = getIdOfURL(content);
		
		if(id == null){
			Logger.logError(new IllegalStateException("No match found"), "Could not find ID in url", "in getIdOfURL(), url: " + content, event);
			Logger.print("Could not find id in reddit url: " + content);
			channel.sendMessage("Sorry, could not parse the link to the reddit url").submit();
		}
		
		RedditPost post = DiscordBot.redditClient.getPostWithID(id);
		
		if(post.isTextpost()){
			return;
		}
		
		channel.sendMessage("*" + event.getAuthor().getName() + "* shared: **" + post.getTitle() + "** - " + post.getMediaUrl()).queue();
		
		//Check if I have MESSAGE_MANAGE permission, before trying to delete
		if(!event.getGuild().getMemberById(Test.idKakansBot).hasPermission(Permission.MESSAGE_MANAGE)) {
			Logger.print("Cannot delete initial reddit URL message in " + event.getChannel().getName()
					+ " channel in " + event.getGuild().getName() + " guild, because lack of MESSAGE_MANAGE");
		} else {
			event.getMessage().delete().queue();
		}
	}
	
	public static String getIdOfURL(String redditURL) {
		Pattern pattern = Pattern.compile("comments/((([0-9]|[A-z])[A-z]*[0-9]*){5})");
		Matcher matcher = pattern.matcher(redditURL);
		if (matcher.find()) {
			return matcher.group(1);
		}
		else {
			return null;
		}
	}
}
