import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.syndication.io.SyndFeedInput;

public class Test2 {

	public static void main(String[] args) {
		// FIXME Auto-generated method stub

		String url = "https://www.reddit.com/r/ImGoingToHellForThis/comments/5v9667/hate_when_this_happens/";
		
		Document doc=null;
		//		try {
		//			doc = Jsoup.connect("http://www.reddit.com/r/shittyrainbow6/comments/5uon69/best_tactic_for_blitz/").userAgent("Chrome").get();
		//			System.out.println(doc.select("#media-preview-5uon69 > div > a").attr("href"));
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//	}
		try {
			doc = Jsoup.connect(url+".rss").userAgent("Chrome").get();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
		SyndFeedInput feedInput = new SyndFeedInput();
	}
}
