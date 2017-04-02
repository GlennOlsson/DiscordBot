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
				// FIXME Auto-generated catch block
				new Logg(e, content, "Could not create PrivateChannel", event);
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
			else if (argument.length()>1) {
				privateChannel.sendMessage("Sorry, but your argument did not get a match").queue();
			}
		}
		String command = "";
		String[] features = {"Reddit",";gif",";source",";clean",";prefix"};
		for (int i = 0; i < features.length; i++) {
			command = command+ features[i]+", ";
		}
		command+=";help";
		privateChannel.sendMessage("Hello. I am a very friendly bot. I have some special features (**"+command+"**) that you can use. Send a ;help "
				+ "followed by one of the features listed, to see specified help for that command. Can also be done in PM").queue();

		return;

	}


}
