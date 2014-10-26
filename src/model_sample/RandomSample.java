package model_sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
		System.out.println(posts.size());
		while(list.size()!=num){
			int m=(int) (Math.random()*posts.size());
			if(!set.contains(m)){
				list.add(posts.get(m));
				set.add(m);
			}
		}
		return list;
	}
	
	public static void main(String[] args) throws IOException, ParseException{
		// TODO Auto-generated method stub
		
		List<String> list=new ArrayList<String>();
		BufferedReader reader=new BufferedReader(new FileReader("./resource/AdditionalNoneSuicidalpart.txt"));
		reader.readLine();
		String line;
		while((line=reader.readLine())!=null){
			String[] info=line.split("	");
			
			if(info.length!=4)
				continue;
			if(info[1].trim().length()<5)
				continue;
			list.add(line+"\t0");
		}
		reader.close();	
		
		List<String>list1 = null;
		list1=getSamplePosts(list,6650);
		WeiboWriter.write2file(list1, "RandomSampledPosts.txt");
	}
}