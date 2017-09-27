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

import backend.ErrorLogg;
import backend.Print;
import backend.Return;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Gif {
	
	private String query = null;
	private String possiblyNumber="";
	private MessageChannel channel;
	private MessageReceivedEvent event;
	
	public Gif(MessageChannel channel, MessageReceivedEvent event, String content) {
		try {
			this.channel = channel;
			this.event = event;
			
			query = content.replace(" ", "-");
			
			if(query.contains("[") && query.contains("]")) {
				String insideBrackets = query.substring(query.indexOf("[")+1, query.indexOf("]"));
				//If there was a space before the [, the newly generated - will be removed
				query=query.contains("-[")?query.replace("-["+insideBrackets+"]",""):
						query.replace("["+insideBrackets+"]","");
				//If multiple gifs
				if(insideBrackets.contains("-")) {
					if(event.getChannel().getType().equals(ChannelType.PRIVATE)) {
						if(insideBrackets.split("-").length == 2) {
							//Just one -
							int lowLimit, highLimit;
							try {
								lowLimit = Integer.parseInt(insideBrackets.split("-")[0]);
								highLimit = Integer.parseInt(insideBrackets.split("-")[1]);
								if(lowLimit >= highLimit) throw new Exception();
							} catch (Exception e) {
								channel.sendMessage("Could not accept those integers. Try again").submit();
								return;
							}
							//We now got an upper limit, and a lower one.
							for (int i = lowLimit; i < highLimit+1; i++) {
								possiblyNumber=" ["+Integer.toString(i)+"]";
								fetchAndSend(i);
							}
							//noinspection UnnecessaryReturnStatement
							return;
						} else {
							//To many -
							channel.sendMessage("To many - inside the [ ]. You must specify like ;gif QUOTAS [" +
									"LOW_LIMIT-HIGH_LIMIT]").
									submit();
							//noinspection UnnecessaryReturnStatement
							return;
						}
					} else {
						//Not private channel
						if(!event.getAuthor().hasPrivateChannel()) {
							event.getAuthor().openPrivateChannel().complete();
						}
						event.getAuthor().openPrivateChannel().complete().sendMessage("You need to send the ;gif command here if you " +
								"are going to choose between multiple gifs!").complete();
						//noinspection UnnecessaryReturnStatement
						return;
					}
				}
				else{
					//If specific gif
					try{
						fetchAndSend(Integer.parseInt(insideBrackets));
					}
					catch (Exception e){
						if(!event.getAuthor().hasPrivateChannel()) {
						event.getAuthor().openPrivateChannel().complete();
					}
						event.getAuthor().openPrivateChannel().complete().sendMessage("Could not accept those integers. Try again").submit();
					}
				}
			}
			else {
				fetchAndSend(1);
			}
		}
		catch (Exception e) {
			new ErrorLogg(e, "Error in Gif.java", "Unknown error", event);
		}
	}
	private void fetchAndSend(int indexOfGif){
		try{
			Document doc = Jsoup.connect(Return.convertUrl("https://tenor.com/search/" + query + "-gifs")).userAgent("Chrome").get();
			String url = "https://tenor.com/" + doc.select("#view > div > div > div > div > div:nth-child(1) > figure:nth-child("+
					indexOfGif+") > a").attr("href");
			
			channel.sendMessage("*"+event.getAuthor().getName()+"* shared a .gif of *'"+query.replace("-", " ")
					.replace("["+indexOfGif+"]","")+ "'*: " + url +possiblyNumber).queue();
			try {
				if(!event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//Check if I have MESSAGE_MANAGE permission, before trying to delete
					for (int i =0;i<event.getTextChannel().getMembers().size();i++) {
						if(event.getTextChannel().getMembers().get(i).getUser().getId().equals(event.getJDA().getSelfUser().getId())){
							//Is KakansBot
							if(!event.getTextChannel().getMembers().get(i).hasPermission(Permission.MESSAGE_MANAGE)){
								new Print("Cannot delete initial Gif command message in "+event.getChannel().getName()
										+" channel in "+event.getGuild().getName()+" guild, because lack of MESSAGE_MANAGE",
										false);
							}
							else {
								event.getMessage().delete().queue();
							}
							i=event.getTextChannel().getMembers().size()+5;
						}
					}
					
				}
			} catch (Exception e) {
				event.getChannel().sendMessage("Error was caught. Contact "+event.getJDA().getUserById("165507757519273984").
						getAsMention()+" with id "+event.getMessage().getId());
				new ErrorLogg(e, event.getMessage().getContent(), event.getMessage().getId(), event);
			}
			//noinspection UnnecessaryReturnStatement
			return;
		}
		catch (Exception e){
			new ErrorLogg(e, "Error in fetchAndSend method in Gif", "Unknown error", event);
		}
	}
}
