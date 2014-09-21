package model_segmentation;
import java.io.*;
import java.util.*;

import retrieval_writer.WeiboWriter;

public class CopyOfTest {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader reader=new BufferedReader(new FileReader("./resource/Segmentedall.txt"));
		List<String> list=new ArrayList<String>();
		String line;
		while((line=reader.readLine())!=null){
			String infos[]=line.split("\t");
				infos[3]="1";
				infos[4]="0";
				list.add(infos[0]+"\t"+infos[4]+"\t"+infos[1]);
		}
		
		WeiboWriter.write2file(list, "LDATrain.csv");
		reader.close();
	}

}
