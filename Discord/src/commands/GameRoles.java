/*
* Copyright 2017 Glenn Olsson
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package commands;

import backend.*;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glenn on 2017-05-20.
 */
public class GameRoles {
	public GameRoles(MessageChannel channel, MessageReceivedEvent event, String afterCommand) {
		String[] games = {"GTA V", "Siege", "TLOU", "Rocket League"};
		
		if(afterCommand.equals("")) {
			noGameSpecified(channel, games,"Choose from these games:");
			return;
		}
		
		for (int i = 0; i < games.length; i++) {
			if(afterCommand.toLowerCase().equals(games[i].toLowerCase())) {
				gameSpecified(games[i], channel, event);
				return;
			}
		}
		noGameSpecified(channel, games, "That game is not available as a role on this server." +
				" Please refer to the following games. Be careful with spelling and backspaces, but it is not case sensitive.");
	}
	
	public void noGameSpecified(MessageChannel channel, String[] games, String message) {
		for (int i = 0; i < games.length; i++) {
			message += "\n**" + games[i] + "**";
		}
		message += "\nSend a message with the game's name to enable/disable the role for you.";
		
		channel.sendMessage(message).queue();
		
	}
	
	public void gameSpecified(String game, MessageChannel channel, MessageReceivedEvent event) {
		List<Role> roles = event.getGuild().getRoles();
		for (int i = 0; i < roles.size(); i++) {
			if(roles.get(i).getName().toLowerCase().equals(game.toLowerCase())){
				GuildController controller = new GuildController(event.getGuild());
				List<Role> userRoles = event.getGuild().getMember(event.getAuthor()).getRoles();
				for(int i1 = 0; i1 < userRoles.size(); i1++){
					if(userRoles.get(i1).getName().toLowerCase().equals(roles.get(i).getName().toLowerCase())){
						//Has role, removing
						controller.removeRolesFromMember(event.getGuild().getMember(event.getAuthor()),roles.get(i)).queue();
						channel.sendMessage(event.getAuthor().getAsMention()+", I removed **"+ roles.get(i).getName() + "** " +
								"from you, so you will no longer be notified when someone mentions @"+roles.get(i).getName()).queue();
						new Print("Removed " + roles.get(i).getName() + " from user "+event.getAuthor().getName() + " in " +
								event.getGuild().getName() + " Guild",false);
						return;
					}
				}
				//Doesn't have the game as role, adding
				controller.addRolesToMember(event.getGuild().getMember(event.getAuthor()),roles.get(i)).queue();
				channel.sendMessage(event.getAuthor().getAsMention()+", I added **"+ roles.get(i).getName() + "** " +
						"to you, so you will be notified when someone mentions @"+roles.get(i).getName()).queue();
				new Print("Added " + roles.get(i).getName() + " to user "+event.getAuthor().getName() + " in " +
						event.getGuild().getName() + " Guild",false);
				return;
			}
		}
		User owner = event.getGuild().getOwner().getUser();
		
		channel.sendMessage(event.getAuthor().getAsMention() + ", sadly enough has "+ owner.getName() + " not added "+ game
				+ " as a role on this sever, so I cannot help you. I have contacted said person though.").queue();
		
		if(!owner.hasPrivateChannel()) owner.openPrivateChannel().queue();
		owner.getPrivateChannel().sendMessage("A users in your server "+ event.getGuild().getName() +
				" wanted to add the game " + game + " as a role, but could not. Please add the role to your server.").queue();
	}
}