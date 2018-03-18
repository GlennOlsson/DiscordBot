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

package commands;

import backend.ReadWrite;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class WordReactions {
	private static String reactTo(String content){
		JsonObject reactObject = ReadWrite.getKey("wordReactions").getAsJsonObject();
		JsonElement reactionToWordElement = reactObject.get(content.toLowerCase());
		
		if(reactionToWordElement != null && !reactionToWordElement.isJsonNull()){
			JsonArray replyArray = reactionToWordElement.getAsJsonArray();
			Random randomGenerator = new Random();
			int randomIndex = randomGenerator.nextInt(replyArray.size());
			
			String replyString = replyArray.get(randomIndex).getAsString();
			return replyString;
		}
		
		//If null or non-existing
		return "";
	}
	
	public static void tryToReact(MessageReceivedEvent event, String content){
		String reactString = reactTo(content);
		
		if(reactString != null && reactString.length() > 0){
			event.getChannel().sendMessage(reactString).queue();
		}
	}
}
