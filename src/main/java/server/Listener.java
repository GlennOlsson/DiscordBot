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

package server;

import backend.Logger;
import backend.ReadWrite;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.regexp.internal.RE;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;

import static spark.Spark.*;

public class Listener {
	
	public Listener(JDA jda){
		
		port(8080);
		
		get("/Discord/Settings/:guild", (req, res) -> {
			try{
				String guildName = req.params(":guild");
				
				List<Guild> guildList = jda.getGuildsByName(guildName, true);
				
				if(guildList.size() == 0){
					//No guild with that name
					return "There's no guild with that name";
				}
				
				JsonObject objectOfGuild = ReadWrite.getGuild(guildList.get(0));
				
				String guildJSON = objectOfGuild.toString();
				JsonObject guildJSONAsObject = ReadWrite.parseStringToJSON(guildJSON);
				
				guildJSON = ReadWrite.beautifyJSON(guildJSONAsObject);
				
				return "<html> <pre>" + guildJSON + " </pre> </html>";
				
			}
			catch (Exception e){
				Logger.logError(e, "Error with server", "Req parameter: " + req.params(":guild"), null);
				return "ERROR - 500";
			}
		});
	}
	
}
