package crawler.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Log errors as XML
 * @author xiaolei
 */
public class ErrorLogger {
	/**
	 * log error message
	 * @param className the Class's name
	 * @param Exception exception occurs 
	 * @param content the analysis content, like posts could not be analyzed, if not leave it blank
	 */
	public static void ErrorLog(String className,String Exception,String content){
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter("./resource/error.txt",true));
			writer.append("<error>"+"\n");
			writer.append("\t<className>"+className+"</className>"+"\n");
			writer.append("\t<content>"+content+"</content>"+"\n");
			writer.append("\t<exception>"+Exception+"</exception>"+"\n");
			writer.append("\t<date>"+System.currentTimeMillis()+"</date>"+"\n");
			writer.append("</error>"+"\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}