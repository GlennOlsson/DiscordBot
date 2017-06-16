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
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Glenn on 2017-06-05.
 */
public class Restart {
	
	public static void main(String[] args) throws Exception{
		Process proc = Runtime.getRuntime().exec("tmux new -s \"temp\"");
		proc.waitFor();
	}
	
	public Restart(MessageChannel channel, MessageReceivedEvent event){
		User author = event.getMessage().getAuthor();
		if(author.getId().equals("165507757519273984")){
			channel.sendMessage("Alright boss, shutting down...").submit();
			event.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
			try{
				Process proc = Runtime.getRuntime().exec("tmux new -s \"temp\"");
				BufferedReader reader =
						new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = "";
				while((line = reader.readLine()) != null) {
					System.out.print(line + "\n");
				}
				proc.waitFor();
				
				Process proc1 = Runtime.getRuntime().exec(
						"tmux send-keys -t \"temp\" 'sh ~/DiscordBot/DiscordBot/Discord/restart.sh' Enter");
				BufferedReader reader1 =
						new BufferedReader(new InputStreamReader(proc1.getInputStream()));
				String line1 = "";
				while((line1 = reader1.readLine()) != null) {
					System.out.print(line1 + "\n");
				}
				proc1.waitFor();
			}
			catch (Exception e){
				new ErrorLogg(e, "Error with Restart", "Could not execute restart command", event);
			}
		}
		else {
			//Is not Kakan
			String guildText = "the "+event.getGuild().getName()+" server",
					mentionText = " Hey "+event.getJDA().getUserById("165507757519273984").getAsMention() +
							", do you know what "+author.getName() + " did?!";
			
			if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
				guildText="a private message";
				mentionText="";
			}
			
			channel.sendMessage("Nuh uh, you are not **The Kakan**! He will hear about this!"+mentionText).
					queue();
			new Print("The user "+ author.getName() + "#"+author.getDiscriminator()
					+"("+author.getId()+") tried to restart the bot in "+ guildText +", in the "
					+event.getChannel().getName() + " channel. KILL THAT PERSON!!", true);
		}
	}
}
