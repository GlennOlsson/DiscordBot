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

package backend;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	
	private static String path = "/var/www/Website-Pi/html/DiscordBot/";
	private static String printFile = "Print.md";
	private static String errorFile = "Errorlog.md";
	
	public static void print(Object whatToPrint){
		try{
			if(isLinux()){
				
				String currentTime = getDate();
				
				StringBuilder newContent = new StringBuilder();
				
				newContent.append("["+currentTime+"] " + whatToPrint + "\n");
				
				saveToPath(newContent.toString(), printFile);
			}
			else{
				//If on testing device, ex. mac
				System.out.println(whatToPrint);
			}
		}
		catch (Exception e){
			System.err.println("ERROR!!!");
			e.printStackTrace();
		}
	}
	
	public static void printError(Object whatToPrint){
		if(isLinux()) {
			print("(ERR) " + whatToPrint);
		}
		else{
			System.err.println(whatToPrint);
		}
	}
	
	public static void logError(Exception exception, String content, String id, MessageReceivedEvent event){
		if(isLinux()){
			try{
				printError("An error was caught");
				
				String currentDate = getDate();
				
				String newContent;
				
				//Parses the stackTrace as string for it to be saved
				StringWriter errors = new StringWriter();
				exception.printStackTrace(new PrintWriter(errors));
				
				String eventMessage = "";
				String guild;
				
				if(event!=null){
					if(!event.getChannel().getType().equals(ChannelType.PRIVATE)){
						guild=", in the "+event.getGuild().getName()+" guild";
					}
					else{
						guild =", in a Private message group";
					}
					
					eventMessage="\nThe sender was "+event.getAuthor().getName()+"#"+event.getAuthor().getDiscriminator()+" in the " +event.getChannel().getName()+
							" channel"+guild;
				}
				
				newContent="##New error at "+currentDate+
						"\nMessage was: "+content+
						"\nId: "+id+eventMessage+
						"\n"+errors.toString()+"\n---------------\n\n";
				
				saveToPath(newContent, errorFile);
				
			}
			catch (Exception e){
				System.err.println("ERROR!!!");
				e.printStackTrace();
			}
		}
		else {
			System.err.println("Content: " + content);
			System.err.println("Id: " + id);
			exception.printStackTrace();
		}
	}
	
	private static void saveToPath(String whatToSave, String pathName) throws IOException{
		Path fullPath = Paths.get(path + pathName);
		Files.write(fullPath, whatToSave.getBytes(), StandardOpenOption.APPEND);
	}
	
	private static String getDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M - HH:mm:ss");
		String currentTime = simpleDateFormat.format(cal.getTime());
		
		return currentTime;
	}
	
	private static boolean isLinux(){
		return System.getProperty("os.name").toLowerCase().contains("linux");
	}
}
