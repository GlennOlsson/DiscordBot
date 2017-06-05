/*
 * Copyright 2017 Glenn Olsson
 *
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software
 *  without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom
 *  the Software is furnished to do so, subject to the following
 *  conditions:
 *
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
 * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class Print {
	
	public Print(Object message){
		new Print(message, false);
	}
	
	public Print(Object message, Boolean isErrPrint) {
		
		if(System.getProperty("os.name").toLowerCase().contains("linux")){
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("d/M - HH:mm:ss");
			String currentTime =sdf.format(cal.getTime());
			
			String currentContent="",newContent;
			
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
				currentContent=currentContent.
						replace("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body> " +
								"<pre><code>", "");
				
				try(FileWriter file = new FileWriter(path+"Print.md")){
					file.write("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body> " +
							"<pre><code>"+newContent+""+currentContent);
				}
				catch (Exception e) {
					new ErrorLogg(e, "Error writing Print.md", "In File.write", null);
				}
			}
			catch (Exception e) {
				new ErrorLogg(e, "Error reading Print.md", "In File.read and others", null);
			}
		}
		else {
			if(isErrPrint) {
				System.err.println(message);
			} else {
				System.out.println(message);
			}
		}
	}
}
