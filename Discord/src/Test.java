
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoup.nodes.*;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Test extends ListenerAdapter{

	public static void main(String[] args) {
		// FIXME Auto-generated method stub

		String url = "https://i.imgur.com/NVm3bNn.mp4";

		Document doc=null;
		//		try {
		//			doc = Jsoup.connect("http://www.reddit.com/r/shittyrainbow6/comments/5uon69/best_tactic_for_blitz/").userAgent("Chrome").get();
		//			System.out.println(doc.select("#media-preview-5uon69 > div > a").attr("href"));
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//	}
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken("MjgyMTE2NTYzMjY2NDM3MTIw.C4m_Kw.R-8jmpM6wycnqX0xGvv_wNYjoJ0").addListener(new Test()).buildBlocking();

		} catch (Exception e) {
			e.printStackTrace();
		}

		TextChannel channels=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);

		for (Member string : channels.getMembers()) {
			if(string.getUser().getName().toLowerCase().equals("kakan")){
				System.err.println(string.getUser());
			}
		}

		Game game = Game.of("Hayy!");
		
		jda.getPresence().setGame(game);
		
		//		System.out.println(jda.getShardInfo());

	}

	public Test(){

	}
	public void onMessageReceived(MessageReceivedEvent event){	
		String content = event.getMessage().getContent().toLowerCase();

		if(content.contains(";sup")&&content.substring(0, ";sup".length()).equals(";sup")){
			PrivateChannel privateChannel=null;
			
			if(event.getAuthor().hasPrivateChannel()){
				privateChannel=event.getAuthor().getPrivateChannel();
			}
			else {
				event.getAuthor().openPrivateChannel().queue();
				privateChannel=event.getAuthor().getPrivateChannel();
			}
			
			privateChannel.sendMessage("Sup").queue();
			
			
			
		}
	}
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event){

		String content = event.getMessage().getContent().toLowerCase();

		if(content.contains(";sup")&&content.substring(0, ";sup".length()).equals(";sup")){
			System.out.println();
		}

		if(content.contains(";send")&&content.substring(0, ";send".length()).equals(";send")){
			String user= event.getAuthor().getName().toLowerCase() + "#"+event.getAuthor().getDiscriminator().toLowerCase();
			if (user.equals("kakan#2926")) {
				System.err.println("AUTHORIZED");
				if(!content.contains(" ")){
					event.getChannel().sendMessage("-- YOU CAN MESSAGE THE FOLLOWING CHANNELS --").queue();
					List<TextChannel> channels = event.getJDA().getTextChannels();

					List<String> channelNames=new ArrayList<>();

					for (TextChannel channel : event.getJDA().getTextChannels()) {
						channelNames.add(channel.getGuild().getName() +" - "+ channel.getName());

					}

					Collections.sort(channelNames, new Comparator<String>() {
						@Override
						public int compare(String s1, String s2) {
							return s1.compareToIgnoreCase(s2);
						}
					});
					int i=0;
					for (String string : channelNames) {
						i++;
						event.getChannel().sendMessage(i+". "+string).queue();
					}

					event.getChannel().sendMessage("-- THAT IS ALL --").queue();
				}
				else if (content.split(" ").length==2) {
					String arg = content.split(" ")[1];

					List<TextChannel> channels = event.getJDA().getTextChannels();
					List<String> channelNames=new ArrayList<>();

					for (TextChannel channel : event.getJDA().getTextChannels()) {
						channelNames.add(channel.getGuild().getName() +" - "+ channel.getName());

					}

					Collections.sort(channelNames, new Comparator<String>() {
						@Override
						public int compare(String s1, String s2) {
							return s1.compareToIgnoreCase(s2);
						}
					});

					System.out.println(channelNames.get(Integer.parseInt(arg)-1));

				}
			}
		}

	}
}



