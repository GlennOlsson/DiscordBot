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

import backend.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.exceptions.*;

public class Help {
	public Help(MessageReceivedEvent event, String content) {

		PrivateChannel privateChannel=null;

		if(event.getAuthor().hasPrivateChannel()){
			privateChannel=event.getAuthor().getPrivateChannel();
		}
		else {
			try {
				event.getAuthor().openPrivateChannel().complete(true);
			} catch (RateLimitedException e) {
				new ErrorLogg(e, content, "Could not create PrivateChannel", event);
			}
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
						+ "I will then send the first gif meeting that criteria. You can either separate the quotas with spaces, or with -. Use like: ;gif <quotas>").queue();
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
			else if (argument.equals("prefix")||argument.equals(";prefix")) {
				privateChannel.sendMessage("If you send **;prefix**, followed by whatever, you can change the prefix (the sign before the command, here, ; is the prefix)"
						+ " for the server, or private channel. Although, **;** will always be standard, and can always be used. "
						+ "Also, if you would send only **prefix**, without any actuall prefix, I will reply with the current prefix of the server/private channel. "
						+ "Use like: ;prefix <newPrefix>").queue();
				return;
			}
			
			else if (argument.equals("game")||argument.equals(";game")) {
				privateChannel.sendMessage("This command enables you to set roles for yourself, consisting of games. " +
						"You choose the games that you actively play, so that members on the server can mention you, " +
						"when they just want to notify people on specific games. Send **;game**, and I'll show you available games" +
						"on the server, and then you choose which to add, one at a time.").queue();
				return;
			}
			
			else if (argument.equals("editgame")||argument.equals(";editgame")) {
				privateChannel.sendMessage("This command is used by people that has the ability to manage roles in the server." +
						" By sending **;editgame [add/remove] [name of game]**, I will add a role with the name, and " +
						"make it possible for the users to add that role themselves.").queue();
				return;
			}
			
			else if (argument.length()>1) {
				privateChannel.sendMessage("Sorry, but your argument did not get a match").queue();
			}
		}
		String command = "";
		String[] features = {"Reddit",";gif",";source",";clean",";prefix",";game",";editgame"};
		for (int i = 0; i < features.length; i++) {
			command = command+ features[i]+", ";
		}
		command+=";help";
		privateChannel.sendMessage("Hello. I am a very friendly bot. I have some special features (**"+command+"**) that you " +
				"can use. Send a **;help** followed by one of the features listed, to see specified help for that command. Some can" +
				" also be done in PM. If you need more assistance, contact the developer, "+event.getJDA().
				getUserById("165507757519273984").getAsMention()+" by PM. He'll be happy to assist you").queue();
		return;

	}


}
