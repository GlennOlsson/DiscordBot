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
import backend.ReadWrite;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by glenn on 2017-06-29.
 */
public class WelcomeMessage {
	public static void WelcomeMessage(MessageReceivedEvent event, MessageChannel channel, String afterCommand){
		
		String message = afterCommand;
		
		if(channel.getType().equals(ChannelType.PRIVATE)){
			//Private channel, maybe be able to set on another server
			return;
		}
		
		if(!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)&&
				!event.getAuthor().getId().equals("165507757519273984")){
			channel.sendMessage("Sorry, only members with the **Administrator** permission can set the welcome message").queue();
			return;
		}
		
		if(message.contains("\"")){
			channel.sendMessage("You can absolutely not have any \" in the message. That would really fuck up a lot. you may use ' instead").queue();
			return;
		}
		
		if(message.equals("?")){
			String currentWelcome = ReadWrite.getKey("welcome"+event.getGuild().getId());
			if(currentWelcome==null){
				currentWelcome="Welcome **;mention;** to "+event.getGuild().getName()+"!";
			}
			else{
				currentWelcome=getReadableMessage(currentWelcome, event.getJDA());
			}
			channel.sendMessage("The current welcome message is:\n\n"+currentWelcome+"\n\nNote that **;mention;** will be replaced with the " +
					"a mention of the joined user").queue();
			return;
		}
		
		if(message==null||message.equals("")){
			channel.sendMessage("You have removed the welcome message for **"+event.getGuild().getName()+"**").queue();
		}
		else {
			message = getReadableMessage(message, event.getJDA());
			
			channel.sendMessage("You have set the following message as the welcome message for **" + event.getGuild().getName() + "**: \n\n" +
					message + "\n\nIf there are ;mention; blocks in blod text, it means " +
					"that the mention will work. Otherwise, something is wrong, and you should try to see if you misspelled or something.").queue();
		}
		ReadWrite.setKey("welcome"+event.getGuild().getId(),afterCommand);
	}
	
	private static String getReadableMessage(String originalMessage, JDA jda){
		String transformedMessage = originalMessage;
		
		while ((transformedMessage.contains("<@") && transformedMessage.contains(">"))) {
			int firstIndex = transformedMessage.indexOf("<@"), lastIndex = transformedMessage.indexOf(">");
			
			String maybeUserId = transformedMessage.substring(firstIndex + 2, lastIndex);
			Logger.print(maybeUserId + "---");
			try {
				User user = jda.getUserById(maybeUserId);
				transformedMessage = transformedMessage.replaceFirst("<@" + maybeUserId + ">", "**@" + user.getName() + "#" +
						user.getDiscriminator() + "**");
			} catch (Exception e) {
				Logger.print("Not a user ID");
				transformedMessage = transformedMessage.replaceFirst("<@", "*<*@");
			}
		}
		return transformedMessage.replace(";mention;","**;mention;**");
	}
}
