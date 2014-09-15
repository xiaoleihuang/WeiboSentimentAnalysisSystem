package model_keyword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrieval_extractor.GetAllWeiboPosts;

/**
 * Trying to use both unigram and bigram
 * @author xiaolei
 */
public class WordFeatures {
	//load dictionaries from local files
	static{
		List<HashSet<String>> dictionaries=new ArrayList<HashSet<String>>();
		GetAllWeiboPosts all=new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/tempTrainData");
		try {
			dictionaries.add(LoadSentimentDictionary.getSuicideWords());
			dictionaries.add(LoadSentimentDictionary.getUpsetWords());
			dictionaries.add(LoadSentimentDictionary.getHowNetNegativeWords());
			dictionaries.add(LoadSentimentDictionary.getHowNetPositiveWords());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void GetFeatures(){
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}