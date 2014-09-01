package retrieval_extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrieval_writer.WeiboWriter;

/**
 * Sun Aug 19 00:15:39 +0800 2012
 * @author xiaolei
 *
 */
public class DateFormatter {
	public static String parser(String date) throws ParseException{
		SimpleDateFormat format=new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy");
		Date d = format.parse(date);
		format=new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return format.format(d);
	}
	public static void main(String[] args) throws IOException, ParseException{
		File f=new File("/home/xiaolei/Desktop/dataset/suicide/allNoneSuicidal.txt");
		BufferedReader reader=new BufferedReader(new FileReader(f));
		String line;
		List<String> list=new ArrayList<String>();
		list.add(reader.readLine());
		while((line=reader.readLine())!=null){
			String[] info=line.split("\t");
			try{
				info[2]=DateFormatter.parser(info[2]);
			}catch(ParseException e){
				if(Integer.parseInt(info[3])==1){
					info[3]="0";
				}else{
					info[3]="1";
				}
				if(info[1].trim().length()>=1)
					list.add(info[0].trim()+"\t"+info[1].trim()+"\t"+info[2].trim()+"\t"+info[3].trim());
				if(info[1].trim().length()<1)
					System.out.println(line);
				continue;
			}
			if(info[1].trim().length()>=1)
				list.add(info[0].trim()+"\t"+info[1].trim()+"\t"+info[2].trim()+"\t"+info[3].trim());
		}
		reader.close();
		WeiboWriter.write2file(list, "test.txt");
	}
}