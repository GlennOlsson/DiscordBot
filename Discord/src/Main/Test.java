package Main;

import Main.RetrieveSetting.JSONDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Test extends ListenerAdapter{

	public static void main(String[] args) {
		// FIXME Auto-generated method stub

		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(RetrieveSetting.getKey("oath",JSONDocument.secret)).addListener(new Test()).buildBlocking();

		} catch (Exception e) {
			
			LoggExceptions.Logg(e, "JDA Fail in Test", "JDA Fail in Test");
			
		}

		TextChannel channels=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);

		System.out.println(RetrieveSetting.getKey("prefix283333281477558272", JSONDocument.setting));
		
	}

	public Test(){

	}
	public void onMessageReceived(MessageReceivedEvent event){	

		if(event.getAuthor().getName().equals("Kakan")){
			
			System.out.println(event.getChannel().getType().equals(ChannelType.PRIVATE));
			
			String content = event.getMessage().getContent().toLowerCase();

			if(content.contains(";sup")&&content.substring(0, ";sup".length()).equals(";sup")){

				event.getMessage().delete().queue();

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




