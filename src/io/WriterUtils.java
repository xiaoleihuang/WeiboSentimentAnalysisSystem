package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import crawler.extractor.ParseProvinceXMLFile;

/**
 * Write data to File with given path
 * @author xiaolei
 */
public class WriterUtils {	
	/**
	 * Write Data directly to file
	 * @param list the list of data
	 * @param filename define the file name
	 * @throws IOException
	 */
	public static void write2file(List<String>list,String filename) throws IOException{
		BufferedWriter writer=new BufferedWriter(new FileWriter("./resource/"+filename));
		for(String str:list){
			writer.append(str+"\n");
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * Parse the location of SinaWeibo user
	 * @param province
	 * @param city
	 * @return
	 */
	public static String getLocation(int province, int city) {
		// TODO Auto-generated method stub
		try{
			XMLParser p=new XMLParser("province","provinces.xml");
			ParseProvinceXMLFile parser=new ParseProvinceXMLFile(p.getList());
			return parser.SearchAllName(province, city);
		}catch(Exception e){
			return "null";
		}
	}
	

}