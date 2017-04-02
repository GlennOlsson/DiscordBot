package backend;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Print {
	public Print(String message, Boolean isErrPrint) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("d/M - HH:mm:ss");
		String currentTime =sdf.format(cal.getTime());

		if(isErrPrint){
			System.err.println("["+currentTime+"] (ERR) "+ message);
		}
		else{
			System.out.println("["+currentTime+"] "+ message);
		}

	}


}
