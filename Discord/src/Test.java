
import java.util.List;

import org.jsoup.nodes.*;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
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

		TextChannel channel=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);

	}

	public Test(){

	}
	public void onMessageReceived(MessageReceivedEvent event){

		String content = event.getMessage().getContent().toLowerCase();
		if(content.toLowerCase().contains(";clean")&&content.toLowerCase().substring(0,";clean".length()).equals(";clean")){

			TextChannel channel =event.getTextChannel();
			List<Role> roles = null;
			for (int i = 0; i < channel.getMembers().size(); i++) {		
				if(channel.getMembers().get(i).getUser()==event.getAuthor()){
					roles =channel.getMembers().get(i).getRoles();
					i= channel.getMembers().size()+5;
				}
			}

			String[] roleslist ={"Moderator", "Commissioner", "Server Owner"};
			Boolean permission=false;
			for (int i = 0; i < roles.size(); i++) {
				if(roles.get(i).getName().toLowerCase().equals("moderator")||roles.get(i).getName().toLowerCase().equals("commissioner")||
						roles.get(i).getName().toLowerCase().equals("server owner")){
					permission=true;
					i=roles.size()+5;
				}
			}
			if(permission){
				int amount = 1;
				String argument1="all", argument2="all";

				//Arguments
				if(content.contains(" ")){
					try {
						amount=Integer.parseInt(content.split(" ")[1]);
						if (amount>100) {
							amount=100;
						}
					} catch (Exception e) {
						// FIXME: handle exception
						event.getAuthor().getPrivateChannel().sendMessage("Error in argument, deleting one").queue();;
					}
					if(content.split(" ").length>=3){
						//If at least length=3
						if(content.split(" ")[2].toLowerCase().equals("bots")||content.split(" ")[2].toLowerCase().equals("users")||
								content.split(" ")[2].toLowerCase().equals("all")){
							argument1=content.split(" ")[2].toLowerCase();
						}

					}
					if(content.split(" ").length>=4){
						argument2=content.substring(content.split(" ")[3].toLowerCase().indexOf(content.split(" ")[3].toLowerCase()));
						System.out.println(argument2 + " == arg 2");
						Boolean hasMemeber=false;
						for (int i = 0; i < channel.getMembers().size(); i++) {
							if(channel.getMembers().get(i).getUser().getName().toLowerCase().equals(content.split(" ")[3].toLowerCase())){
								System.out.println("AYYYY");
								hasMemeber=true;
								argument2=content.split(" ")[3].toLowerCase();
							}
						}
						if(!hasMemeber){
							channel.sendMessage("This channel does not have a user with that name. Aborting command").queue();
							return;
						}
					}
//					else if (content.split(" ").length>4) {
//						channel.sendMessage("To many arguments *"+event.getAuthor().getAsMention() +"*, aborting command").queue();
//						return;
//					}
				}
				else{
					//					event.getAuthor().getPrivateChannel().sendMessage("No amount specified, deleting one").queue();;
				}

				//				channel.getHistory().

				MessageHistory history = channel.getHistory();
				channel.getHistory().retrievePast(100).queue();
				List<Message> historyList = null;
				try {
					historyList=history.retrievePast(100).block();
				} catch (Exception e) {
					// FIXME Auto-generated catch block
					e.printStackTrace();
				}
				event.getMessage().deleteMessage().queue();
				for (int i = 1; i < amount+1; i++) {
					if(argument1.equals("bots")){
						if(historyList.get(i).getAuthor().isBot()){
							if(argument2.equals("all")){
								historyList.get(i).deleteMessage().queue();	
							}
							else {
								if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
									historyList.get(i).deleteMessage().queue();	
								}
								else {
									amount++;
								}
							}
						}
						else{
							amount++;
						}
					}
					else if (argument1.equals("users")) {
						if(!historyList.get(i).getAuthor().isBot()){
							if(argument2.equals("all")){
								historyList.get(i).deleteMessage().queue();	
							}
							else {
								if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
									historyList.get(i).deleteMessage().queue();	
								}
								else {
									amount++;
								}
							}
						}
						else{
							amount++;
						}
					}
					else {
						//if all user's messages shall go
						if(argument2.equals("all")){
							historyList.get(i).deleteMessage().queue();	
						}
						else {
							if(historyList.get(i).getAuthor().getName().toLowerCase().equals(argument2)){
								historyList.get(i).deleteMessage().queue();	
							}
							else {
								amount++;
							}
						}
					}
				}
				System.err.println(amount+" = amount");
			}
			else {
				//				channel.sendMessage("You are not authorized to execute that command, *"+event.getAuthor().getAsMention()+"*. Contact a Moderator").queue();
				return;
			}


		}
	}
}



