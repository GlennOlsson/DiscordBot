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
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import main.Test;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Glenn on 2017-05-20.
 */
public class GameRoles {
	
	public static void messageReactedTo(GuildMessageReactionAddEvent event){
		if(!event.getUser().getId().equals(Test.idKakansBot)) {
			JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
			JsonElement reactionMessageID = guildObject.get("gamesMessageID");
			if(reactionMessageID != null && !reactionMessageID.isJsonNull() && event.getMessageId().equals(reactionMessageID.getAsString())) {
				genericReaction(event, EditType.ADD, guildObject);
			}
		}
	}
	
	public static void reactionRemovedFromMessage(GuildMessageReactionRemoveEvent event){
		if(!event.getUser().getId().equals(Test.idKakansBot)) {
			JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
			JsonElement reactionMessageID = guildObject.get("gamesMessageID");
			if(reactionMessageID != null && !reactionMessageID.isJsonNull() && event.getMessageId().equals(reactionMessageID.getAsString())) {
				genericReaction(event, EditType.REMOVE, guildObject);
			}
		}
	}
	
	public static void genericReaction(GenericGuildMessageReactionEvent event, EditType editType, JsonObject guildObject){
		String emoteName = getEmoteName(event);
		System.out.println("Generic reaction with emote " + emoteName);
		
		JsonObject gamesObject = guildObject.get("games").getAsJsonObject();
		JsonElement gameElement = gamesObject.get(emoteName);
		
		//Guard
		if(gameElement == null || gameElement.isJsonNull()){
			//Remove emote
			return;
		}
		
		String game = gameElement.getAsString();
		User user = event.getUser();
		
		toggleGameRole(user, game, event.getGuild(), editType);
		
		System.out.println(emoteName);
	}
	
	private static void toggleGameRole(User user, String game, Guild guild, EditType editType){
		List<Role> roles = guild.getRolesByName(game, true);
		if(roles.size() > 0){
			GuildController controller = new GuildController(guild);
			Member member = guild.getMember(user);
			
			Role role = roles.get(0);
			if(editType == EditType.REMOVE){
				System.out.println("Removing " + role.getName());
				controller.removeSingleRoleFromMember(member, role).queue();
			}
			else{
				System.out.println("Adding " + role.getName());
				controller.addRolesToMember(member, role).queue();
			}
		}
	}
	
	public static void sendReactToMessage(TextChannel channel){
		JsonObject guildObject = ReadWrite.getGuild(channel.getGuild());
		JsonObject gamesObject = guildObject.get("games").getAsJsonObject();
		
		Set<Map.Entry<String, JsonElement>> emojiGameSet = gamesObject.entrySet();
		
		StringBuilder messageStringBuilder = new StringBuilder();
		
		for(Map.Entry<String, JsonElement> emojiGamePair : emojiGameSet){
			String emojiKey = emojiGamePair.getKey();
			String emoji = getEmoji(emojiKey, channel.getGuild());
			messageStringBuilder.append(emoji);
			messageStringBuilder.append(" - ");
			messageStringBuilder.append(emojiGamePair.getValue().getAsString());
			messageStringBuilder.append("\n");
		}
		
		messageStringBuilder.append("\n");
		messageStringBuilder.append("React to this message with the games you want to be associated with");
		
		MessageBuilder messageBuilder = new MessageBuilder();
		Message message = messageBuilder.append(messageStringBuilder.toString()).build();
		
		try{
			if(channel.getGuild().getMember(channel.getJDA().getSelfUser()).hasPermission(Permission.MESSAGE_MANAGE)){
				try{
					String reactionMessageID = guildObject.get("gamesMessageID").getAsString();
					channel.getMessageById(reactionMessageID).complete().delete().queue();
				}
				catch (Exception e){
					//Do nothing
					channel.sendMessage("Could not delete old message, if there was one you must delete it yourself").complete();
				}
			}
			else {
				channel.sendMessage("Could not delete old message, if there was one you must delete it yourself").complete();
			}
			
			Message sentMessage = channel.sendMessage(message).submit().get();
			
			String messageID = sentMessage.getId();
			System.out.println(messageID);
			
			guildObject.addProperty("gamesMessageID", messageID);
			ReadWrite.addEditedGuild(channel.getGuild(), guildObject);
			//		Message sentMessage = channel.getMessageById(messageID).complete();
			for(Map.Entry<String, JsonElement> emojiGamePair : emojiGameSet){
				addEmoteToMessage(emojiGamePair.getKey(), channel.getGuild(), sentMessage);
			}
		}
		catch (Exception e){
			Logger.logError(e, "Could not get message in sendReactToMessage", "", null);
		}
	}
	
	/**
	 *
	 * @param name name or id
	 * @return
	 */
	private static void addEmoteToMessage(String name, Guild guild, Message message){
		try{
			long id = Long.parseLong(name);
			//If successful, is imported emoji, otherwise standard
			Emote emote = guild.getEmoteById(id);
			message.addReaction(emote).complete();
		}
		catch (Exception e){
			message.addReaction(name).complete();
		}
	}
	
	private static String getEmoji(String name, Guild guild){
		try{
			long id = Long.parseLong(name);
			//If successful, is imported emoji, otherwise standard
			Emote emote = guild.getEmoteById(id);
			return emote.getAsMention();
		}
		catch (Exception e){
			//Name is the unicode of the emoji
			return name;
		}
	}
	
	private static String getEmojiID(String name, Guild guild){
		List<Emote> emotes = guild.getEmotes();
		
		for(Emote emote : emotes){
			if(emote.getAsMention().toLowerCase().equals(name.toLowerCase())){
				return emote.getId();
			}
		}
		return name;
	}
	
	private static String getEmoteName(GenericGuildMessageReactionEvent event){
		MessageReaction.ReactionEmote emote = event.getReactionEmote();
		//If the emote is a standard emote
		if(emote.getId() == null){
			//Returns the "unicode" char
			return emote.getName();
		}
		//Else, if an imported one
		return emote.getId();
	}
	
	private enum EditType{
		REMOVE, ADD
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
		if(afterCommand.length() < 1){
			channel.sendMessage("You must specify all three parts of the command, like " +
					"**;editgame [add/remove] [the emoji] [the game]**").queue();
			return;
		}
		if(afterCommand.equals("")) {
			channel.sendMessage("You must specify whether to add or remove a game as a role. Do this by sending:" +
					"**;editgame [add/remove/create] [the game]**").queue();
			return;
		}
		
		String[] commandArray = afterCommand.split(" ");
		
		if(commandArray.length < 1){
			channel.sendMessage("You must specify whether to add or remove a game as a role. Do this by sending:" +
					"**;editgame [add/remove] [the game]**").queue();
			return;
		}
		
		if(commandArray[0].toLowerCase().equals("create")){
			sendReactToMessage(event.getTextChannel());
			return;
		}
		
		Logger.print("-"+afterCommand+"-");
		
		String emojiString = commandArray[1].toLowerCase();
		
		if(!isEmoji(emojiString, event.getGuild())){
			channel.sendMessage("Must be an emoji!").queue();
			return;
		}
		
		String emoji = getEmojiID(emojiString, event.getGuild());
		
		
		if(commandArray[0].toLowerCase().equals("add")){
			//ADD
			
			if(commandArray.length < 3){
				channel.sendMessage("You must specify all three parts of the command, like " +
						"**;editgame add [the emoji] [the game]**").queue();
				return;
			}
			
			String game = afterCommand.replace(commandArray[0] + " " + commandArray[1] + " ", "");
			
			if(game.startsWith(" ") || game.endsWith(" ")) {
				channel.sendMessage("Fuck no, don't start nor end the game with a space, dammit!").queue();
				return;
			}
			
			
			try{
				JsonObject guild = ReadWrite.getGuild(event.getGuild()).getAsJsonObject();
				JsonObject gamesObject = guild.get("games").getAsJsonObject();
				
				JsonElement gameBefore = gamesObject.get(emoji);
				if(gameBefore != null && !gameBefore.isJsonNull()){
					channel.sendMessage("There already is a game associated to that emoji").queue();
					return;
				}
				//If game does not exist
				gamesObject.addProperty(emoji, game);
				
				ReadWrite.addEditedGuild(event.getGuild(), guild);
				
				Logger.print("Added " + game + " to " + event.getGuild().getName() + " with emoji " + emoji);
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
				Logger.logError(e, "Error with add game", "In " + event.getGuild().getName() + " guild", event);
			}
		}
		else if(commandArray[0].toLowerCase().equals("remove")){
			//Remove
			try {
				
				JsonObject guildObject = ReadWrite.getGuild(event.getGuild());
				JsonObject gamesObject = guildObject.get("games").getAsJsonObject();
				
				System.out.println(emoji);
				
				JsonElement gameOfEmoji = gamesObject.remove(emoji);
				
				if(gameOfEmoji == null || gameOfEmoji.isJsonNull()){
					channel.sendMessage("Cannot remove a game that has not been added").submit();
					return;
				}
				//Game was removed
				ReadWrite.addEditedGuild(event.getGuild(), guildObject);
				
				String game = gameOfEmoji.getAsString();
				
				try{
					event.getGuild().getRolesByName(game, true).get(0).delete().complete();
					Logger.print("Removed role " + game);
					channel.sendMessage("Successfully removed game").submit();
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
		sendReactToMessage(event.getTextChannel());
	}
	
	private static boolean isEmoji(String emoji, Guild guild){
		if(EmojiManager.isEmoji(emoji)){
			return true;
		}
		//Else, could be id of a guild emoji
		List<Emote> emotes = guild.getEmotes();
		for(Emote emote : emotes){
			if(emoji.toLowerCase().equals(emote.getAsMention().toLowerCase())){
				return true;
			}
		}
//		guild.getEm
		return false;
	}
}