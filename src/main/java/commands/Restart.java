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
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Glenn on 2017-06-05.
 */
public class Restart {
	
	public static void Restart(MessageChannel channel, MessageReceivedEvent event){
		User author = event.getMessage().getAuthor();
		if(author.getId().equals(Test.idKakan)){
			channel.sendMessage("Alright boss, shutting down...").submit();
			event.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
			try{
				Process proc = Runtime.getRuntime().exec("tmux new -s \"temp\"");
				BufferedReader reader =
						new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line;
				while((line = reader.readLine()) != null) {
					System.out.print(line + "\n");
				}
				proc.waitFor();
				
				Process proc1 = Runtime.getRuntime().exec(
						"tmux send-keys -t \"temp\" 'sh ~/DiscordBot/DiscordBot/Discord/restart.sh' Enter");
				BufferedReader reader1 =
						new BufferedReader(new InputStreamReader(proc1.getInputStream()));
				String line1;
				while((line1 = reader1.readLine()) != null) {
					System.out.print(line1 + "\n");
				}
				proc1.waitFor();
				
				Process proc2 = Runtime.getRuntime().exec(
						"echo fuck");
				BufferedReader reader2 =
						new BufferedReader(new InputStreamReader(proc2.getInputStream()));
				String line2;
				while((line2 = reader2.readLine()) != null) {
					System.out.print(line2 + "\n");
				}
				proc2.waitFor();
				
			}
			catch (Exception e){
				Logger.logError(e, "Error with Restart", "Could not execute restart command", event);
			}
		}
		else {
			//Is not Kakan
			String guildText = "", mentionText = "";
			
			if(!event.getChannel().getType().equals(ChannelType.PRIVATE)) {
				guildText = "the " + event.getGuild().getName() + " server";
				
				mentionText = "Hey " + event.getJDA().retrieveUserById(Test.idKakan).complete().getAsMention() +
						", do you know what " + author.getName() + " did?!";
			}
			else{
				//is private channel
				guildText="a private message";
				mentionText="";
			}
			
			channel.sendMessage("Nuh uh, you are not **The Kakan**! He will hear about this! "+mentionText).
					queue();
			Logger.printError("The user "+ author.getName() + "#"+author.getDiscriminator()
					+"("+author.getId()+") tried to restart the bot in "+ guildText +", in the "
					+event.getChannel().getName() + " channel. KILL THAT PERSON!!");
		}
	}
}
