package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic Reader
 * @author xiaolei
 *
 */
public class BasicReader {
	/**
	 * Read source line by line
	 * @param path data path
	 * @return list of source
	 * @throws IOException
	 */
	public static List<String> basicRead(String path) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(path));
		String line=null;
		List<String> list=new ArrayList<String>();
		while((line=reader.readLine())!=null){
			line=line.trim();
			if(line!="")
				list.add(line);
		}
		reader.close();
		return list;
	}
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}