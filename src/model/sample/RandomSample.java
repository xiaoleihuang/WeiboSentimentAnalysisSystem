package model.sample;

import io.BasicReader;
import io.WriterUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
		
		List<String> list=BasicReader.basicRead("./resource/AdditionalNoneSuicidalpart.txt");

		list=getSamplePosts(list,6650);
		WriterUtils.write2file(list, "RandomSampledPosts.txt");
	}
}