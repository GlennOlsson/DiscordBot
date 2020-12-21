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

package events;



import backend.Logger;
import backend.ReadWrite;
// import net.dv8tion.jda.api.entities.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Created by Glenn on 2017-06-04.
 */
public class JDAEvents extends ListenerAdapter {
	
	public static void Ready(ReadyEvent event) {
		try {
			TextChannel channel=event.getJDA().getTextChannelById("282109399617634304");
			channel.sendMessage("Successfully logged in!").complete();
			try{
				if(System.getProperty("os.name").toLowerCase().contains("linux")){
					int newRun = ReadWrite.getKey("runCount").getAsInt() + 1;
					Logger.print("\n		New run: Nr. " + newRun + "\n");
					ReadWrite.setKey("runCount", newRun);
				}
			}catch (Exception e) {
				//Probably could not convert string -> int
				Logger.logError(e, "Error in JDAEvents.Ready", "Run count: " + ReadWrite.getKey("runCount"), null);
			}
			// event.getJDA().getPresence().setGame(Game.of(Game.GameType.DEFAULT,"Send ;help"));
		}catch (Exception e) {
			Logger.logError(e, "Error in JDAEvents.Ready", "Unknown error", null);
		}
	}
	
	public static void Reconnect(ReconnectedEvent event) {
		try {
			// event.getJDA().getPresence().setGame(Game.of(Game.GameType.DEFAULT,"Send ;help"));
			Logger.print("Reconnected");
		} catch (Exception e) {
			Logger.logError(e, "Error in JDAEvents.Reconnect", "Unknown error", null);
		}
	}
	
	public static void Shutdown(ShutdownEvent event){
		try{
			event.getJDA().getTextChannelById("282109399617634304").sendMessage("Shutting down").submit();
		}
		catch (Exception e){
			Logger.logError(e, "Error in JDAEvents.Shutdown", "Unknown error", null);
		}
	}
}
