
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

public class Test2 extends ListenerAdapter{

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
			jda = new JDABuilder(AccountType.BOT).setToken("MjgyMTE2NTYzMjY2NDM3MTIw.C4m_Kw.R-8jmpM6wycnqX0xGvv_wNYjoJ0").addListener(new Test2()).buildBlocking();

		} catch (Exception e) {
			e.printStackTrace();
		}

		TextChannel channel=jda.getGuildsByName("Kakanistan",true).get(0).getTextChannels().get(0);
		channel.sendMessage("Test bot is live").queue();



	}

	public Test2(){

	}

	public void onMessageReceived(MessageReceivedEvent event){
		if(!event.getAuthor().getName().equals("Kakan's Bot")){
			TextChannel channel=event.getTextChannel();
			String content = event.getMessage().getContent().toLowerCase();

			if(event.getMessage().getContent().toString().contains(";clean")&&content.substring(0,";clean".length()).equals(";clean")){
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
					String argument="all";
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
						if(content.split(" ").length==3){
							if(content.split(" ")[2].toLowerCase().equals("bots")||content.split(" ")[2].toLowerCase().equals("users")||
									content.split(" ")[2].toLowerCase().equals("all")){
								argument=content.split(" ")[2].toLowerCase();
							}

						}
						if(content.split(" ").length>=4){
							channel.sendMessage("To many arguments *"+event.getAuthor().getAsMention() +"*, aborting command").queue();
							return;
						}
					}
					else{
//						event.getAuthor().getPrivateChannel().sendMessage("No amount specified, deleting one").queue();;
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
					
					for (int i = 0; i < amount; i++) {
						if(argument.equals("bots")){
							if(historyList.get(i).getAuthor().isBot()){
								historyList.get(i).deleteMessage().queue();	
							}
							else{
								amount++;
							}
						}
						else if (argument.equals("users")) {
							if(!historyList.get(i).getAuthor().isBot()){
								historyList.get(i).deleteMessage().queue();	
							}
							else{
								amount++;
							}
						}
						else {
							//if all user's messages shall go
							historyList.get(i).deleteMessage().queue();	
						}
					}
					System.err.println(amount+" = amount");
				}
				else {
//					channel.sendMessage("You are not authorized to execute that command, *"+event.getAuthor().getAsMention()+"*. Contact a Moderator").queue();
					return;
				}
			}
		}
	}
}






