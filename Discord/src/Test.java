import java.io.InputStream;
import org.jsoup.*;
import org.jsoup.nodes.*;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Test extends ListenerAdapter{

	public static void main(String[] args) {
		//		new Test();
		JDA jda = null;
		try {
			jda = new JDABuilder(AccountType.BOT).setToken("MjgyMTE2NTYzMjY2NDM3MTIw.C4m_Kw.R-8jmpM6wycnqX0xGvv_wNYjoJ0").addListener(new Test()).buildBlocking();
		} catch (Exception e) {
			e.printStackTrace();
		}

		TextChannel channel = jda.getTextChannels().get(0);
		channel.sendMessage("Sucessfully logged in!").queue();

		//		InputStream input=null;
		//		try {
		//			input = new URL("https://video.twimg.com/ext_tw_video/832625181017378816/pu/vid/720x1280/L_wImVqsGhKH-K3o.mp4").openStream();
		//		} catch (MalformedURLException e) {
		//			// FIXME Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			// FIXME Auto-generated catch block
		//			e.printStackTrace();
		//		}

		//		channel.sendFile(input, "", null).queue();;



		//		EmbedBuilder messageEmbed = new EmbedBuilder()
		//				.setAuthor("Glenn", "https://i.imgur.com/VnMNmTm.jpg", "https://s-media-cache-ak0.pinimg.com/originals/76/df/0e/76df0e6607a4eee8018759d72ff4ce84.jpg")
		////				.setThumbnail("https://i.imgur.com/VnMNmTm.jpg")
		//				.setTitle("hey")
		////				.setUrl("https://www.youtube.com/watch?v=kQjaUW9diyA")
		//				.addBlankField(true)
		//				.addField("hey","SUP",true)
		//				.addBlankField(true)
		//				.setVideo("https://www.youtube.com/watch?v=AuA2EAgAegE&t=6s")
		////				.setImage("https://i.imgur.com/VnMNmTm.jpg")
		//				.setColor(Color.black)
		//				.setProvider("Youtube", "https://www.youtube.com")
		//				;

		//		messageEmbed.setVideo("https://video.twimg.com/ext_tw_video/832625181017378816/pu/vid/720x1280/L_wImVqsGhKH-K3o.mp4");

		//		MessageEmbed ms = messageEmbed.build();
		//		
		//		System.err.println(ms.getSiteProvider().getName());
		//		
		//		channel.sendMessage(ms).queue();;

		//		Document doc;
		//		try {
		//			doc = Jsoup.parse(new URL("https://www.reddit.com/r/shittyrainbow6/comments/5uon69/best_tactic_for_blitz/?st=izb6fq4z&sh=ef62c48f"), 10000);
		//			System.out.println(doc.getElementsByClass("may-blank"));
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}




	}

	public Test(){

		//		System.exit(3);
	}

	public void onMessageReceived(MessageReceivedEvent event){

		if(!event.getAuthor().getName().equals("KakansBot")){

			MessageChannel channel = event.getChannel();
			InputStream input=null;
			String message=event.getMessage().getContent();
			//			try {
			////				input = new URL("https://i.redd.it/r24hirgxehgy.png").openStream();
			//				Document doc = Jsoup.parse(new URL("https://www.reddit.com/r/shittyrainbow6/comments/5uon69/best_tactic_for_blitz/?st=izb6fq4z&sh=ef62c48f"), 10000);
			//				System.out.println(doc.getElementsByClass("may-blank"));
			//				
			//				
			//			} catch (Exception e) {
			//				e.printStackTrace();
			//			}
			if(message.contains("https://www.reddit")||message.contains("http://www.reddit")||message.contains("https://reddit")||message.contains("http://reddit")){
				Document doc;
				String url = null, title = null;
				try {
					doc = Jsoup.connect(event.getMessage().getContent()).userAgent("Chrome").get();
					url = doc.select(".title > a").attr("href");
					title=doc.select(".title > a").text();
					System.out.println(url + " = RUL");
					if(!url.substring(0,3).equals("/r/")){
						if(url.toLowerCase().contains("imgur")&&!url.toLowerCase().contains("gif")&&!url.toLowerCase().contains("gifv")){
							System.out.println("CONTAINS");
							url=url+"gifv";
						}
						channel.sendMessage("*"+event.getAuthor().getName()+"* shared: **"+title+"** "+url).queue();
						event.getMessage().deleteMessage().queue();
						//					input = new URL(url).openStream();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				//			channel.sendFile(input,"gif", null).queue();
			}
		}
	}















}
