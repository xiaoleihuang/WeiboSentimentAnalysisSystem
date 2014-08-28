package model_sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrieval_extractor.ReadUidFile;
import retrieval_writer.WeiboWriter;

public class UidSampler {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ReadUidFile reader=new ReadUidFile();
		List<String> uids=reader.readUid();
		List<String> temp=new ArrayList<String>();
		int i=0;
		for(String s:uids){
			temp.add(s);
			if(temp.size()%5==0){
				WeiboWriter.write2file(temp, "posts/未掌握数据"+i+".txt");
				i++;
				temp.clear();
			}
		}
		WeiboWriter.write2file(temp, "posts/未掌握数据.txt");
	}
}