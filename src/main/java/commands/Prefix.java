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

import backend.Print;
import backend.ReadWrite;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Prefix {
	public Prefix(MessageChannel channel, MessageReceivedEvent event, String newPrefix) {
		String[] roleslist ={"Moderator", "Commissioner", "Server Owner"};
		if(ReadWrite.isAuthorized(event.getTextChannel(), event, roleslist)){
			new Print("HALLELUJAH", false);
			if(!newPrefix.equals("")){
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//if privateChannel ---> no guildId
					ReadWrite.setPrefix(channel.getId(),newPrefix);
				}
				else {
					//Not private --> has guild Id
					ReadWrite.setPrefix(event.getGuild().getId(),newPrefix);
				}
				channel.sendMessage("Prefix successfully changed to \""+newPrefix+"\"").queue();
			}
		}
		else {
			if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
				channel.sendMessage("Unauthorized. Current prefix: "+ReadWrite.getPrefix(channel.getId())).queue();
			}
			else{
				channel.sendMessage("Unauthorized. Current prefix: "+ReadWrite.getPrefix(event.getGuild().getId())).queue();
			}
		}
	}

	
}
