/*
 *
 *  * Copyright 2017 Glenn Olsson
 *  *
 *  * Permission is hereby granted, free of charge, to any
 *  * person obtaining a copy of this software and associated
 *  *  documentation files (the "Software"), to deal in the Software
 *  *  without restriction, including without limitation the rights to
 *  *  use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  *  sell copies of the Software, and to permit persons to whom
 *  *  the Software is furnished to do so, subject to the following
 *  *  conditions:
 *  *
 *  * The above copyright notice and this permission notice shall
 *  * be included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
 *  * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 *  * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *  * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 *  * OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package events;

import backend.Logger;
import backend.ReadWrite;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.regexp.internal.RE;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import sun.rmi.runtime.Log;

public class GuildEvents {
	public static void GuildJoin(GuildJoinEvent event){
		Logger.print("Joined guild " + event.getGuild().getName());
		
		Guild guild = event.getGuild();
		JsonObject newGuildObject = new JsonObject();
		
		newGuildObject.addProperty("name", guild.getName());
		newGuildObject.addProperty("welcomeMessage", "");
		newGuildObject.addProperty("prefix", "");
		
		JsonArray emptyArray = new JsonArray();
		
		newGuildObject.add("games", emptyArray);
		newGuildObject.add("dailyDoses", emptyArray);
		
		JsonObject guildsObject = ReadWrite.getKey("guilds").getAsJsonObject();
		guildsObject.add(guild.getId(), newGuildObject);
		
		ReadWrite.setKey("guilds", guildsObject);
		
	}
	public static void GuildLeave(GuildLeaveEvent event){
		Logger.print("Left guild " + event.getGuild().getName());
		
		JsonObject guildsObject = ReadWrite.getKey("guilds").getAsJsonObject();
		guildsObject.remove(event.getGuild().getId());
		ReadWrite.setKey("guilds", guildsObject);
	}
}
