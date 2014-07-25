package model_sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrieval_extractor.Regex;
import retrieval_writer.WeiboWriter;

/**
 * Sample non-suicidal posts from data
 * @author xiaolei
 * @version 1.0
 */
public class RandomSample {
	/**
	 * Sample posts from input data list
	 * @param posts the data input: a list of posts
	 * @param num user define how many sample posts he needs
	 * @return a sampled data
	 */
	public static List<String> getSamplePosts(List<String> posts,int num){
		List<String> list=new ArrayList<String>();
		HashSet<Integer> set=new HashSet<Integer>();
		int length=posts.size();
		while(list.size()!=num){
			int m=(int) (Math.random()*length);
			if(set.contains(m))
				continue;
			set.add(m);
			list.add(posts.get(m));
		}
		return list;
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<String>();
		BufferedReader reader=new BufferedReader(new FileReader("/home/xiaolei/Desktop/dataset/自杀者未标注为自杀倾向的微薄/allno2.txt"));
		String line;
		while((line=reader.readLine())!=null){
			try{
				String temp=line.split("\t")[0];
				line=line.split("\t")[1];
				line=line.replaceAll("(http//.+)\\s+", "");
				line=line.replaceAll("(http://.+)\\s+", "");
				line=Regex.removeRetweetName(line);
				list.add(temp+"\t"+"0"+"\t"+line);
			}catch(Exception e){
				continue;
			}
		}
		reader.close();
		
		reader=new BufferedReader(new FileReader("/home/xiaolei/Desktop/dataset/suicide/未自杀posts.txt"));
		
		while((line=reader.readLine())!=null){
			try{
				String temp;
				temp=line.split("\t")[2];
				line=line.split("\t")[3];
				line=line.replaceAll("(http//.+)\\s+", "");
				line=line.replaceAll("(http://.+)\\s+", "");
				line=Regex.removeRetweetName(line);
				list.add(temp+"\t"+"0"+"\t"+line);
			}catch(Exception e){
				continue;
			}
		}
		reader.close();
		
		list=getSamplePosts(list,5000);
		WeiboWriter.write2file(list, "RandomSampledPosts");
	}
}