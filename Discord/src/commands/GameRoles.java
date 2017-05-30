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
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.RoleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Glenn on 2017-05-20.
 */
public class GameRoles {
	public GameRoles(MessageChannel channel, MessageReceivedEvent event, String afterCommand) {
//		String[] games = {"GTA V", "Siege", "TLOU", "Rocket League"};
		String[] games = {};
		try{
			games = ReadWrite.getKey("games"+event.getGuild().getId()).split(",");
		}
		catch (Exception e){
			channel.sendMessage("This channel does not have any games set. Have someone with permission to manage" +
					" roles to add games, with **;editgame [add/remove] [gamename]**").queue();
			return;
		}
		
		if(games[0].length()<1){
			channel.sendMessage("This channel does not have any games set. Have someone with permission to manage" +
					" roles to add games, with **;editgame [add/remove] [gamename]**").queue();
			return;
		}
		
		if(afterCommand.equals("")) {
			noGameSpecified(channel, "Choose from these games:",event);
			return;
		}
		
		for (int i = 0; i < games.length; i++) {
			if(afterCommand.toLowerCase().equals(games[i].toLowerCase())) {
				gameSpecified(games[i], channel, event);
				return;
			}
		}
		noGameSpecified(channel, "That game is not available as a role on this server." +
				" Please refer to the following games. Be careful with spelling and spaces, but it is not case sensitive.", event);
	}
	
	public static void noGameSpecified(MessageChannel channel, String message, MessageReceivedEvent event) {
		String[] games = ReadWrite.getKey("games"+event.getGuild().getId()).split(",");
		
		if(games.length==1&&games[0].equals("")){
			channel.sendMessage("There are no longer any games to set on this server").queue();
			return;
		}
		new Print(games.length,false);
		for (int i = 0; i < games.length; i++) {
			message += "\n**" + games[i] + " **";
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
		
		new Print("-"+afterCommand+"-",false);
		
		if(afterCommand.toLowerCase().split(" ")[0].equals("add")){
			//ADD
			String game=afterCommand.substring(4);
			try{
				ArrayList<String> games = new ArrayList<>();
				String[] list = ReadWrite.getKey("games"+event.getGuild().getId()).split(",");
				
				for(int i = 0; i < list.length; i++){
					games.add(list[i]);
					if(game.toLowerCase().equals(games.get(i).toLowerCase())){
						channel.sendMessage("That game has already been added").queue();
						return;
					}
				}
				//If game does not exist
				
				String currentGames="";
				if(!games.get(0).equals("")) {
					for (int i = 0; i < games.size(); i++) {
						currentGames += games.get(i) + ",";
					}
				}
				currentGames+=game;
				
				ReadWrite.setKey("games"+event.getGuild().getId(),currentGames);
				
				new Print("currentgames for "+event.getGuild().getName() + " guild: "+currentGames, false);
				try{
					new Print("Created role "+game,false);
					GuildController controller = new GuildController(event.getGuild());
					controller.createRole().setName(game).setMentionable(true).complete();
				}
				catch (Exception e){
					new ErrorLogg(e, "Could not create role", game, event);
					channel.sendMessage("Sorry, but I could not create a role. You'll have to do it manually").queue();
				}
				
				
			}
			catch (Exception e){
				new Print("Creating games"+event.getGuild().getId()+
						" in Secret.json, for "+ event.getGuild().getName(),false);
				ReadWrite.setKey("games"+event.getGuild().getId(), game);
			}
		}
		else if(afterCommand.toLowerCase().split(" ")[0].equals("remove")){
			//Remove
			try {
				String game = afterCommand.substring(7);
				int index = -50;
				ArrayList<String> games = new ArrayList<>();
				String[] list = ReadWrite.getKey("games" + event.getGuild().getId()).split(",");
				for (int i = 0; i < list.length; i++) {
					games.add(list[i]);
					if(game.toLowerCase().equals(games.get(i).toLowerCase())) {
						index = i;
					}
				}
				//If game does not exist
				if(index < 0) throw new Exception();
				
				games.remove(index);
				String currentGames = "";
				if(games.size() > 0){
					for (int i = 0; i < games.size(); i++) {
						currentGames += games.get(i) + ",";
					}
					currentGames = currentGames.substring(0, currentGames.length() - 1);
				}
				new Print(currentGames,false);
				
				ReadWrite.setKey("games"+event.getGuild().getId(),currentGames);
				
				new Print("currentgames for "+event.getGuild().getName() + " guild: "+currentGames, false);
				
				try{
					event.getGuild().getRolesByName(game, true).get(0).delete().complete();
					new Print("Removed role "+game,false);
				}
				catch (Exception e){
					new ErrorLogg(e, "Could not create role", game, event);
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
			new Print(afterCommand.toLowerCase(),false);
			return;
		}
		noGameSpecified(channel,"These are now the current games on this server",event);
	}
}