package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class Print {
	public Print(String message, Boolean isErrPrint) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("d/M - HH:mm:ss");
		String currentTime =sdf.format(cal.getTime());

		String currentContent="",newContent="";
		
		if(isErrPrint==null){
			newContent="	"+message+"\n";
		}
		else if (isErrPrint) {
			newContent="["+currentTime+"] (ERR) "+ message+"\n";
		}
		else{
			newContent="["+currentTime+"] "+ message+"\n";
		}
		
		try{
			String path="/var/lib/tomcat7/webapps/ROOT/";
			FileReader reader = new FileReader(path+"Print.md");
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(reader); 
			Iterator<String> iterator= br.lines().iterator();
			//Saves the earlier content to currentContent string
			while (iterator.hasNext()) {
				currentContent+=iterator.next()+"\n";
			}
			//Writes old + new content

			currentContent=currentContent.replace("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><plaintext>", "");

			try(FileWriter file = new FileWriter(path+"Print.md")){
				file.write("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><plaintext>"+newContent+""+currentContent);
			}
			catch (Exception e) {
				// FIXME: handle exception
				new Logg(e, "Error writing Logg.md", "In File.write", null);
			}
		}
		catch (Exception e) {
			// FIXME: handle exception
			new Logg(e, "Error reading Logg.md", "In File.read and others", null);
		}
	}
}
