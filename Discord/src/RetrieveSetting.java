import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RetrieveSetting {

	enum JSONDocument {
		secret, setting;
	}
	public static void main(String[] args) {
		System.out.println(getKey(null, JSONDocument.secret));
	}

	public static String getKey(String key, JSONDocument whichFile){

		if(whichFile==JSONDocument.secret){
			try {
				String prefix=";";
				JSONParser parser = new JSONParser();
				Object object = null;

				if(System.getProperty("os.name").toLowerCase().contains("windows")){
					object = parser.parse(new FileReader("C:\\Users\\Glenn\\Documents\\DiscordBot\\Secret.json"));
				}
				else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
					object =  parser.parse(new FileReader("~/DiscordBot/Secret.json"));
				}

				JSONObject jsonObject = (JSONObject) object;

				return (String) jsonObject.get("oath");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (whichFile==JSONDocument.setting) {

		}


		return null;
	}

}
