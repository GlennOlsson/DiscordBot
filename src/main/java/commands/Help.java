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


import backend.Logger;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

public class Help {
	public static void Help(MessageReceivedEvent event, String content) {

		PrivateChannel privateChannel;

		if(event.getAuthor().hasPrivateChannel()){
			privateChannel=event.getAuthor().openPrivateChannel().complete();
		}
		else {
			try {
				event.getAuthor().openPrivateChannel().complete(true);
			} catch (RateLimitedException e) {
				Logger.logError(e, content, "Could not create PrivateChannel", event);
			}
			privateChannel=event.getAuthor().openPrivateChannel().complete();
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
						+ "I will then send the first gif meeting that criteria. You can either separate the quotas with spaces, or with -. " +
						"You can also choose another gif than the first one of the search quota. To see the selection, send me a PM like ;gif " +
						"**QUOTAS** [**LOWER_LIMIT**-**UPPER_LIMIT**] (for example ;gif hello [1-54]). Then send the gif you want, by sending the corresponding number like " +
						";gif **QUOTAS** [**NUMBER**] (for example ;gif hello [45])").queue();
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
				privateChannel.sendMessage("With **;clean**, you can remove a certain amount of messages, from the privateChannel. You can specify both the amount, and "
						+ "messages from what kind of account that shall be removed. You must be authorized to use this."
						+ " Use like: ;clean [1-100] [bots,users,all]").queue();
				return;
			}
			else if (argument.equals("prefix")||argument.equals(";prefix")) {
				privateChannel.sendMessage("If you send **;prefix**, followed by whatever, you can change the prefix (the sign before the command, here, ; is the prefix)"
						+ " for the server, or private channel. Although, **;** will always be standard, and can always be used. "
						+ "Also, if you would send only **prefix**, without any actual prefix, I will reply with the current prefix of the server/private channel. "
						+ "Use like: ;prefix <newPrefix>").queue();
				return;
			}
			
//			else if (argument.equals("game")||argument.equals(";game")) {
//				privateChannel.sendMessage("This command enables you to set roles for yourself, consisting of games. " +
//						"You choose the games that you actively play, so that members on the server can mention you, " +
//						"when they just want to notify people on specific games. Send **;game**, and I'll show you available games " +
//						"on the server, and then you choose which to add, one at a time.").queue();
//				return;
//			}
			
			else if (argument.equals("editgame")||argument.equals(";editgame")) {
				privateChannel.sendMessage("This command is used by people that has the ability to manage roles in the server." +
						" By starting the command with **;editgame [add/remove] [emoji] [the game]**, I will add/remove a role with the name, " +
						"and make it possible for the users to add that role themselves. If you are removing a game, you don't have to provide" +
						" the game name, only the emoji. \n\nAfter you send this command, a message will be sent with all the available games " +
						"on the server with the corresponding emoji. The users can then react to this message to add that game to themselves. " +
						"If you want this message to be in another channel than the one you sent in, send **;editgame create** in the channel" +
						" of your desire!").queue();
				return;
			}
			
			else if (argument.equals("dailydose")||argument.equals(";dailydose")) {
				privateChannel.sendMessage("Use this command to add or remove daily doses for your server. The daily doses consists" +
						" of the 3 top posts of the day from a specified subreddit, and will be sent on a daily basis to the channel where " +
						"you add the command. You must also specify the time you want the dose to be sent, in CET timezone. \n" +
						"To add a Daily Dose, use **;dailydose add SUBREDDIT HOURTOSEND:MINUTETOSEND**\n" +
						"To remove a Daily Dose, use **;dailydose remove SUBREDDIT**\n" +
						"To list the current set Daily Doses for the server, use **;dailydose**").queue();
				return;
			}
			
			else if (argument.equals("welcome")||argument.equals(";welcome")) {
				privateChannel.sendMessage("Use this command to set a welcome message for your server for when people join. You" +
						" can use **;mention;** tags to mention the joined user. You can mention other users and channels without any" +
						" problem. \n" +
						"Use it like **;welcome [WELCOME_MESSAGE]** to set a new message.\n" +
						"Use it like **;welcome ?** to see the current message.\n" +
						"You cannot disable the welcome message").queue();
				return;
			}
			else if (argument.length()>1) {
				privateChannel.sendMessage("Sorry, but your argument did not get a match").queue();
			}
		}
		String command = "";
		String[] features = {"Reddit",";gif",";source",";clean",";prefix",";editgame",";welcome"};
		for (String feature : features) {
			command = command + feature + ", ";
		}
		command+=";help";
		privateChannel.sendMessage("Hello. I am a very friendly bot. I have some special features (**"+command+"**) that you " +
				"can use. Send a **;help** followed by one of the features listed, to see specified help for that command. Some can" +
				" also be done in PM. If you need more assistance, contact the developer, "+event.getJDA().
				retrieveUserById("165507757519273984").complete().getAsMention()+" by PM. He'll be happy to assist you").queue();
		//noinspection UnnecessaryReturnStatement
		return;

	} 


}
