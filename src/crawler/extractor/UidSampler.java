package crawler.extractor;

import io.WriterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crawler.io.ReadUidFile;

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
				WriterUtils.write2file(temp, "posts/未掌握数据"+i+".txt");
				i++;
				temp.clear();
			}
		}
		WriterUtils.write2file(temp, "posts/未掌握数据.txt");
	}
}