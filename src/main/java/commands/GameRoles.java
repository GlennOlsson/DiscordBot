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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.Permission;
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
	public static void GameRoles(MessageChannel channel, MessageReceivedEvent event, String afterCommand) {
		String[] games;
		try{
			JsonObject guild = ReadWrite.getGuild(event.getGuild());
			JsonArray gamesArray = guild.get("games").getAsJsonArray();
			
			if(gamesArray.size() == 0){
				throw new Exception();
			}
		
		if(afterCommand.length() < 1) {
			noGameSpecified(channel, "Choose from these games: ", event);
			return;
		}
		
		for(JsonElement jsonElement : gamesArray){
			String thisGame = jsonElement.getAsString();
			if(thisGame.toLowerCase().equals(afterCommand.toLowerCase())){
				gameSpecified(thisGame, channel, event);
				return;
			}
		}
		
		noGameSpecified(channel, "That game is not available as a role on this server." +
				" Please refer to the following games. Be careful with spelling and spaces, but it is not case sensitive.", event);
			
		}
		catch (Exception e){
			channel.sendMessage("This channel does not have any games set. Have someone with permission to manage" +
					" roles to add games, with **;editgame [add/remove] [NAME_OF_GAME]**").queue();
			return;
		}
	}
	
	private static void noGameSpecified(MessageChannel channel, String message, MessageReceivedEvent event) {
		
		JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
		JsonArray gamesArray = guildObject.get("games").getAsJsonArray();
		
		for (JsonElement gameElement : gamesArray) {
			String game = gameElement.getAsString();
			message += "\n**" + game + " **";
		}
		message += "\nSend a message like **;game [NAME_OF_GAME]** to enable/disable the role for you.";
		
		channel.sendMessage(message).queue();
		
	}
	
	private static void gameSpecified(String game, MessageChannel channel, MessageReceivedEvent event) {
		
		List<Role> roles = event.getGuild().getRoles();
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < roles.size(); i++) {
			if(roles.get(i).getName().toLowerCase().equals(game.toLowerCase())){
				GuildController controller = new GuildController(event.getGuild());
				List<Role> userRoles = event.getGuild().getMember(event.getAuthor()).getRoles();
				//noinspection ForLoopReplaceableByForEach
				for(int i1 = 0; i1 < userRoles.size(); i1++){
					if(userRoles.get(i1).getName().toLowerCase().equals(roles.get(i).getName().toLowerCase())){
						//Has role, removing
						controller.removeRolesFromMember(event.getGuild().getMember(event.getAuthor()),roles.get(i)).queue();
						channel.sendMessage(event.getAuthor().getAsMention()+", I removed **"+ roles.get(i).getName() + "** " +
								"from you, so you will no longer be notified when someone mentions @"+roles.get(i).getName()).queue();
						Logger.print("Removed " + roles.get(i).getName() + " from user "+event.getAuthor().getName() + " in " +
								event.getGuild().getName() + " Guild");
						return;
					}
				}
				//Doesn't have the game as role, adding
				controller.addRolesToMember(event.getGuild().getMember(event.getAuthor()),roles.get(i)).queue();
				channel.sendMessage(event.getAuthor().getAsMention()+", I added **"+ roles.get(i).getName() + "** " +
						"to you, so you will be notified when someone mentions @"+roles.get(i).getName()).queue();
				Logger.print("Added " + roles.get(i).getName() + " to user "+event.getAuthor().getName() + " in " +
						event.getGuild().getName() + " Guild");
				return;
			}
		}
		User owner = event.getGuild().getOwner().getUser();
		
		channel.sendMessage(event.getAuthor().getAsMention() + ", sadly enough has "+ owner.getName() + " not added "+ game
				+ " as a role on this sever, so I cannot help you. I have contacted said person though.").queue();
		
		if(!owner.hasPrivateChannel()) owner.openPrivateChannel().queue();
		owner.openPrivateChannel().complete().sendMessage("A users in your server "+ event.getGuild().getName() +
				" wanted to add the game " + game + " as a role, but could not. Please add the role to your server.").queue();
	}
	public static void editRoles(MessageReceivedEvent event, MessageChannel channel, String afterCommand){
		if(!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.MANAGE_ROLES)){
			channel.sendMessage("You are not authorized to use this command").queue();
			return;
		}
		if(!event.getGuild().getMember(event.getJDA().getSelfUser()).hasPermission(Permission.MANAGE_ROLES)){
			channel.sendMessage("I am not authorized to use this command. I need to be able to manage roles first").queue();
			return;
		}
		if(afterCommand.contains(",")) {
			channel.sendMessage("Please, "+event.getAuthor().getAsMention() + ", don't fuck with me. " +
					"No *,* in the name. Please").queue();
			return;
		}
		if(Character.toString(afterCommand.charAt(0)).equals(" ")||Character.toString(
				afterCommand.charAt(afterCommand.length()-1)).equals(" ")) {
			channel.sendMessage("Fuck no, don't start nor end the game with a space, dammit!").queue();
			return;
		}
		if(afterCommand.equals("")) {
			channel.sendMessage("You must specify whether to add or remove a game as a role. Do this by sending:" +
					"**;editgame [add/remove] [the game]**").queue();
			return;
		}
		
		Logger.print("-"+afterCommand+"-");
		
		if(afterCommand.toLowerCase().split(" ")[0].equals("add")){
			//ADD
			String game=afterCommand.substring(4);
			try{
				JsonObject guild = ReadWrite.getGuild(event.getGuild()).getAsJsonObject();
				JsonArray gamesArray = guild.get("games").getAsJsonArray();
				
				for(JsonElement gameJSON : gamesArray){
					String gameString = gameJSON.getAsString();
					if(game.toLowerCase().equals(gameString.toLowerCase())){
						channel.sendMessage("That game has already been added").queue();
						return;
					}
				}
				
				//If game does not exist
				gamesArray.add(game);
				
				guild.add("games", gamesArray);
				
				ReadWrite.addEditedGuild(event.getGuild(), guild);
				
				Logger.print("currentgames for "+event.getGuild().getName() + " guild: " + gamesArray.toString());
				try{
					Logger.print("Created role "+game);
					GuildController controller = new GuildController(event.getGuild());
					controller.createRole().setName(game).setMentionable(true).complete();
				}
				catch (Exception e){
					Logger.logError(e, "Could not create role", game, event);
					channel.sendMessage("Sorry, but I could not create a role. You'll have to do it manually").queue();
				}
				
				
			}
			catch (Exception e){
				Logger.print("");
				Logger.logError(e, "Error with add game", "In " + event.getGuild().getName() + " guild", event);
			}
		}
		else if(afterCommand.toLowerCase().split(" ")[0].equals("remove")){
			//Remove
			try {
				String game = afterCommand.substring(7);
				
				JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
				JsonArray gamesArray = guildObject.get("games").getAsJsonArray();
				
				int arrayLength = gamesArray.size();
				
				for(JsonElement jsonElement : gamesArray){
					String thisGame = jsonElement.getAsString();
					if(thisGame.toLowerCase().equals(game.toLowerCase())){
						//Array contains game
						gamesArray.remove(jsonElement);
					}
				}
				
				if(arrayLength == gamesArray.size()){
					//Element has not been removed, as the game was not found
					throw new Exception();
				}
				//Game was removed
				
				guildObject.add("games", gamesArray);
				ReadWrite.addEditedGuild(event.getGuild(), guildObject);
				
				Logger.print("currentgames for "+event.getGuild().getName() + " guild: " + gamesArray.toString());
				
				try{
					event.getGuild().getRolesByName(game, true).get(0).delete().complete();
					Logger.print("Removed role " + game);
				}
				catch (Exception e){
					Logger.logError(e, "Could not remove role", game, event);
					channel.sendMessage("Sorry, but I could not remove the role. You'll have to do it manually").queue();
				}
				
			}
			catch (Exception e){
				channel.sendMessage("You cannot remove a game that yet has not been added").queue();
				return;
			}
		}
		else{
			channel.sendMessage("You must choose either **add** or **remove**").queue();
			Logger.print(afterCommand.toLowerCase());
			return;
		}
		noGameSpecified(channel,"These are now the current games on this server",event);
	}
}