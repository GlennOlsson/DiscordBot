package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class LoggExceptions {

	public static void main(String[] args) {
		try {
			ArrayList<String> list =null;
			list.add("foo");
		} catch (Exception e) {
			// FIXME: handle exception
			Logg(e);
		}

	}

	public static void Logg(Exception exception) {
		exception.printStackTrace();
		
		String currentContent = "", newContent = "";

		//Parses the stackTrace as string for it to be saved
		StringWriter errors = new StringWriter();
		exception.printStackTrace(new PrintWriter(errors));
		
		
		//Saves title and such to newContent, including current time
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M - H:m:s");
       String currentTime =sdf.format(cal.getTime());
		
		newContent="##New error at "+currentTime+"\n "+errors.toString()+"\n---------------\n\n";
		
		try{
			FileReader reader = new FileReader("Files/Errorlog.md");
			BufferedReader br = new BufferedReader(reader); 
			Iterator<String> iterator= br.lines().iterator();
			//Saves the earlier content to currentContent string
			while (iterator.hasNext()) {
				currentContent+=iterator.next()+"\n";
			}
			//Writes old + new content
			try(FileWriter file = new FileWriter("Files/Errorlog.md")){
				file.write(newContent+""+currentContent);
			}
			catch (Exception e) {
				// FIXME: handle exception
				error(e);
			}
			
		}
		catch (Exception e) {
			// FIXME: handle exception
			error(e);
		}
	}
	public static void error(Exception exception) {
		//Skicka mail till mig
	}

}
