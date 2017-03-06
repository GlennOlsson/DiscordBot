package Main;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RetrieveSetting {

	enum JSONDocument {
		secret, setting;
	}

	public static String getKey(String key, JSONDocument fileSort){

		if(fileSort==JSONDocument.secret){
			try {
				String prefix=";";
				JSONParser parser = new JSONParser();
				Object object = null;

				if(System.getProperty("os.name").toLowerCase().contains("windows")){
					object = parser.parse(new FileReader("C:\\Users\\Glenn\\Documents\\DiscordBot\\Secret.json"));
				}
				else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
					object =  parser.parse(new FileReader("/home/pi/DiscordBot/Secret.json"));
				}

				JSONObject jsonObject = (JSONObject) object;

				return (String) jsonObject.get(key);

			} catch (Exception e) {
				LoggExceptions.Logg(e, "Error in fetching from secret file", "String key: --"+key+"--");
			}
		}
		else if (fileSort==JSONDocument.setting) {
			try {
				String prefix=";";
				JSONParser parser = new JSONParser();
				Object object = null;

					object = parser.parse(new FileReader("Files/settings.json"));

				JSONObject jsonObject = (JSONObject) object;

				return (String) jsonObject.get(key);

			} catch (Exception e) {

				LoggExceptions.Logg(e, "Error in fetching from settings file", "String key: --"+key+"--");
			}
		}


		return null;
	}

}
