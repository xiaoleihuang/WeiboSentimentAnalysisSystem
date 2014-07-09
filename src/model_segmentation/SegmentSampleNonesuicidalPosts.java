package model_segmentation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;

import retrieval_writer.WeiboWriter;
import model_buildIndex.GetStopWords;

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
			
			List<Term> terms=s.getSegmentationResults(content);
			StringBuffer buffer=new StringBuffer();
			for(Term t:terms){
				buffer.append(t.getName().trim()+" ");
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
		WeiboWriter.write2file(segmentedList, "SampledSegmentedNonSuicialPosts.txt");
	}
}