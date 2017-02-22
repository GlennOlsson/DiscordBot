
import org.jsoup.nodes.*;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageHistory;
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

			if(event.getMessage().getContent().toString().contains("!clean")&&content.substring(0,"!clean".length()).equals("!clean")){
				int amount;
				if(content.contains(" ")){
					try {
						amount=Integer.parseInt(content.split(" ")[1]);
					} catch (Exception e) {
						// FIXME: handle exception
						event.getAuthor().getPrivateChannel().sendMessage("Error in argument");
					}
				}else{
					event.getAuthor().getPrivateChannel().sendMessage("No amount specified, deleting one");
					amount=1;
				}
				
				MessageHistory history = channel.getHistory();
				channel.getHistoryAround("!clean", 10).queue();
				history.retrievePast(10).queue();
				
				String[] roles ={"Moderator", "Commissioner", "Server Owner"};
				
				System.err.println("CLEAN");
				System.out.println(" -- HE");
				for (int i = 0; i < channel.getMembers().size(); i++) {

					if(channel.getMembers().get(i).getUser()==event.getAuthor()){
						System.out.println(channel.getMembers().get(i).getRoles());
						System.out.println(event.getAuthor());
					}
					//			System.out.println(channel.getPermissionOverride();
				}
			}
		}
	}
}






