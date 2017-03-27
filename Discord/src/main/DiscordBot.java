package main;
/* ----------TODO

Add prefix to help command, and reply with just the prefix if not authorized
--------

Divide all these methods into classes in Main package

 */


import commands.Commands;
import main.RetrieveSetting.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.events.message.priv.*;
import net.dv8tion.jda.core.hooks.*;

public class DiscordBot extends ListenerAdapter{
	

	public static void main(String[] args) {
		//		new Test();
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(RetrieveSetting.getKey("oath",JSONDocument.secret)).addListener(new DiscordBot()).buildBlocking();
		} catch (Exception e) {
			IO.Logg(e, "JDA Builder", "JDA Builder", null);
		}
		TextChannel channel=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);

		channel.sendMessage("Sucessfully logged in!").queue();

		if(System.getProperty("os.name").toLowerCase().contains("linux")){
		RetrieveSetting.setKey("runCount", Integer.toString(Integer.parseInt(RetrieveSetting.getKey("runCount", JSONDocument.setting))+1));
		}

	}

	public void onReady(ReadyEvent event) {
		super.onReady(event);
		event.getJDA().getPresence().setGame(Game.of("Send ;help"));
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		event.getJDA().getPresence().setGame(Game.of("Send ;help"));
		IO.print("Reconected", false);
	}

	public DiscordBot(){

		//		System.exit(3);
	}

	public void onMessageReceived(MessageReceivedEvent event){

		if(!event.getAuthor().getName().equals("Kakan's Bot")){
			String  contentCase=event.getMessage().getContent(), content = event.getMessage().getContent().toLowerCase();
			MessageChannel channel = event.getChannel();

			channel.sendTyping();

			if(content.toLowerCase().equals("prefix")){
				if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
					//Private channel
					channel.sendMessage("The current prefix on our private chat is :\""+IO.getPrefix(channel.getId())+"\"").queue();
				}
				else{
					//Not private
					channel.sendMessage("The current prefix on "+event.getGuild().getName()+" is :\""+IO.getPrefix(event.getGuild().getId())+"\"").queue();
				}
			}

			//Reddit command
			if((content.contains("://reddit")||content.contains("://www.reddit"))&&(content.substring(0,"https://www.reddit".length()).contains("https://www.reddit")||
					content.substring(0,"http://www.reddit".length()).contains("http://www.reddit")||
					content.substring(0,"https://reddit".length()).contains("https://reddit")||
					content.substring(0,"http://reddit".length()).contains("http://reddit"))){
				Commands.reddit(channel, event, content);
				return;
			}

			//Gets prefix
			String prefix=";";
			if(event.getChannel().getType().equals(ChannelType.PRIVATE)){
				//if privatechannel ---> no guildId
				prefix=IO.getPrefix(channel.getId());
			}
			else {
				//Not private --> has guild Id
				prefix=IO.getPrefix(event.getGuild().getId());
			}

			onMessageReceivedPrefix(event, prefix, content, channel);
			}
	}

	private void onMessageReceivedPrefix(MessageReceivedEvent event, String prefix, String content, MessageChannel channel) {
		// Made so that it can check ; as a prefix, if the first check fails. This way, ; is always a prefix

		if(content.length()>prefix.length()&&content.substring(0,prefix.length()).equals(prefix)){
			//; commands

			String command = content.substring(prefix.length()), afterCommand = "";
			if(command.contains(" ")){
				command=command.split(" ")[0];
				afterCommand = content.substring(prefix.length()+command.length()+1);
				IO.print("Aftercommand=\""+afterCommand+"\"", false);
			}

			switch (command) {
			case "clean":
				Commands.clean(channel, event, content);
				break;

			case "gif":
				Commands.gif(channel, event, content);
				break;

			case "source":
				Commands.source(channel);
				break;

			case "prefix":
				Commands.prefix(channel, event, content, afterCommand);
				break;

			case "up":
				//					up(channel, event, content);
				break;

			case "help":
				Commands.help(event, content);
				break;
			}

			//				else{
			//					channel.sendMessage("Sorry, I don't recognize that command. Try ;help though").queue();
			//				}
		}
		else {
			if(!prefix.equals(";")){
				//Call event but with ; as prefix
				onMessageReceivedPrefix(event, ";", content, channel);
			}
		}
	}

	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

		PrivateChannel channel=event.getChannel();
		String content = event.getMessage().getContent();

		if(!event.getAuthor().getName().equals("Kakan's Bot")){

			channel.sendTyping();

			//Already checked if private
			String prefix=IO.getPrefix(channel.getId());

			if(content.equals("prefix")){
				return;
			}

			else if(content.length()>=prefix.length()){
				if(!content.substring(0, prefix.length()).equals(prefix)&&!Character.toString(content.charAt(0)).equals(";")){
					channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
							+ "commands starting with "+prefix+". You can use "+prefix+"help for example, to see what commands you can use."
							+ " Uppercase or lowercase does not matter").queue();
					return;
				}
			}
			else {
				if(!Character.toString(content.charAt(0)).equals(";"))
				channel.sendMessage("Sorry, you did not start your message with \""+prefix+"\" character. I am a bot, and will only accept "
						+ "commands starting with "+prefix+". You can use \""+prefix+"help\" for example, to see what commands you can use."
						+ " Uppercase or lowercase does not matter").queue();
				return;
			}
		}
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		MessageChannel channel = event.getGuild().getTextChannelsByName("general", true).get(0);
		channel.sendMessage("Welcome *"+event.getMember().getAsMention()+"* to "+event.getGuild().getName()+"!").queue();

		if(event.getGuild().getTextChannelsByName("modlog", true).size()>0){
		event.getGuild().getTextChannelsByName("modlog", true).get(0).sendMessage("The user **"+event.getMember().getUser().getName()+
				"** with the # id **"+ event.getMember().getUser().getDiscriminator() + "** and long id as **"+event.getMember().getUser().getId()
				+"** just joined us").queue();
		}
		else {
			event.getGuild().getOwner().getUser().getPrivateChannel().sendMessage("A user (\"**"+event.getMember().getUser().getName()+"#"+
		event.getMember().getUser().getDiscriminator()+"\"** with long id: **"+event.getMember().getUser().getId()+"**) just joined your guild \"**"+
					event.getGuild().getName()+"**\". *If you don't want to recieve these as private messages, create a channel called \"modlog\", and I will post"
							+ " this information there*").queue();
		}
	}

}
