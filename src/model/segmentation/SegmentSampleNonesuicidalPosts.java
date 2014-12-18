package model.segmentation;

import io.WriterUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.index.GetStopWords;

/**
 * Segment all sampled non-suicidal posts about 5000
 * @author xiaolei
 * @version 1.0
 */
public class SegmentSampleNonesuicidalPosts {
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		GetStopWords getstop = new GetStopWords();
		Segmentation s=new Segmentation(getstop.getWords());
		List<String> segmentedList=new ArrayList<String>();
		
		BufferedReader reader=new BufferedReader(new FileReader("./resource/RandomSampledPosts.txt"));
		String line;
		while((line=reader.readLine())!=null){
			
			String content;
			try{
				content=line.split("\t")[2];
			}catch(Exception e){
				System.out.println(line);
				continue;
			}
			String pid=line.split("\t")[0];
			
			List<String> terms=s.getSegmentationResults(content);
			StringBuffer buffer=new StringBuffer();
			for(String t:terms){
				buffer.append(t.trim()+" ");
			}
			String temp=buffer.toString().trim();
			if(temp==null||temp==""||temp==" "){
				System.out.println(line);
				continue;
			}
			temp=pid+"\t"+"0"+"\t"+temp;
			
			segmentedList.add(temp);
		}
		reader.close();
		WriterUtils.write2file(segmentedList, "SampledSegmentedNonSuicialPosts.txt");
	}
}