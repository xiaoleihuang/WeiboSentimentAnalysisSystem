package model_keyword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;
import retrieval_writer.WeiboWriter;
/**
 * Trying to use both unigram and bigram
 * @author xiaolei
 */
public class WordFeatures {
	//load dictionaries from local files
	static{
		List<HashSet<String>> dictionaries=new ArrayList<HashSet<String>>();
		
		//load all posts' contents
		GetAllWeiboPosts all=new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/tempTrainData");
		List<String> contents=new ArrayList<String>();
		for(OneWeibo post:all.getList()){
			contents.add(post.getContent());
		}
		
		//load all dictionaries from ./resource/ folder
		try {
			dictionaries.add(LoadSentimentDictionary.getSuicideWords());
			dictionaries.add(LoadSentimentDictionary.getUpsetWords());
			dictionaries.add(LoadSentimentDictionary.getHowNetNegativeWords());
			dictionaries.add(LoadSentimentDictionary.getHowNetPositiveWords());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructing feature matrix
	 */
	public static HashMap<Integer,List<Integer>> GetFeatures(){
		HashMap<Integer,List<Integer>> featureMap=new HashMap<Integer,List<Integer>>();
		
		return featureMap;
	}
	
	/**
	 * Formatted features for SVM
	 * @return
	 * @throws IOException 
	 */
	public static List<String> FormatFeaturesForSVM(boolean Write2File) throws IOException{
		HashMap<Integer,List<Integer>> featureMap=GetFeatures();
		List<String> svmFeatures=new ArrayList<String>();
		Set<Integer> keys=featureMap.keySet();
		
		for(int key:keys){
			StringBuilder sb=new StringBuilder();
			List<Integer> list=featureMap.get(key);
			//set labels
			if(key<614){
				sb.append("1 ");
			}else{
				sb.append("0 ");
			}
			
			for(int m=0;m<list.size();m++){
				if(list.get(m)==0)
					continue;
				else
					sb.append(m+":"+list.get(m)+" ");
			}
			sb.append("\n");
		}
		
		if(Write2File){
			WeiboWriter.write2file(svmFeatures, "UnigramFeaturesSVM.txt");
		}
		return svmFeatures;
	}
	
	/**
	 * Formatted features for Weka
	 * @return
	 * @throws IOException 
	 */
	public static List<String> FormatFeaturesForWeka(boolean Write2File) throws IOException{
		HashMap<Integer,List<Integer>> featureMap=GetFeatures();
		List<String> wekaFeatures=new ArrayList<String>();
		Set<Integer> keys=featureMap.keySet();
		
		for(int key:keys){
			
		}
		
		if(Write2File){
			WeiboWriter.write2file(wekaFeatures, "UnigramFeaturesWeka.txt");
		}
		return wekaFeatures;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}