package model_keyword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_writer.WeiboWriter;
/**
 * Trying to use both unigram and bigram
 * @author xiaolei
 */
public class WordFeatures {
	public static List<HashSet<String>> dictionaries=new ArrayList<HashSet<String>>();
	public static List<String> contents=new ArrayList<String>();
	//load dictionaries from local files
	static{
		//load all posts' contents
		GetAllWeiboPosts all=new GetAllWeiboPosts("./resource/Segmentedall.txt");
		
		for(int i=0;i<all.getList().size();i++){
			contents.add(all.getList().get(i).getContent());
		}
		
		//load all dictionaries from ./resource/ folder
		try {
			dictionaries.add(LoadSentimentDictionary.getSuicideWords());
//			dictionaries.add(LoadSentimentDictionary.getUpsetWords());
//			dictionaries.add(LoadSentimentDictionary.getHowNetNegativeWords());
//			dictionaries.add(LoadSentimentDictionary.getHowNetPositiveWords());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructing feature matrix
	 */
	public static HashMap<Integer,List<Integer>> GetFeatures(){
		HashMap<Integer,List<Integer>> featureMap=new HashMap<Integer,List<Integer>>();
		for(int i=0;i<contents.size();i++){
			List<Integer> list=new ArrayList<Integer>();
			for(HashSet<String> dic:dictionaries){
				for(String word:dic){
					if(contents.get(i).contains(word))
						list.add(1);
					else
						list.add(0);
				}
			}
			featureMap.put(i, list);
		}
		System.out.println(featureMap.get(0).size());
		return featureMap;
	}
	
	//delete all columns where contains all 0
	private static HashMap<Integer, List<Integer>> FilterMap(
			HashMap<Integer, List<Integer>> featureMap) {
		// TODO Auto-generated method stub
		Set<Integer> keys=featureMap.keySet();
		List<List<Integer>>list=new ArrayList<List<Integer>>();
		for(int i=0;i<featureMap.get(0).size();i++){
			list.add(new ArrayList<Integer>(keys.size()));
		}
		
		for(int key:keys){
			List<Integer> row=featureMap.get(key);
			for(int i=0;i<row.size();i++){
				list.get(i).add(row.get(i));
			}
		}
		
		for(int i=0;i<list.size();i++){
			List<Integer> column=list.get(i);
			boolean flag=true;
			for(int feature:column){
				if(feature==1){
					flag=false;
					break;
				}
			}
			
			if(flag==false){
				continue;
			}else{
				for(int key:keys){
					featureMap.get(key).remove(i);
				}
			}
		}
		
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
			svmFeatures.add(sb.toString().trim());
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
		featureMap=FilterMap(featureMap);
		Set<Integer> keys=featureMap.keySet();
		StringBuilder sb=new StringBuilder();
		sb.append("key,");
		for(int i=0;i<featureMap.get(0).size();i++){
			sb.append("feature"+i+",");
		}
		sb.append("class");
		wekaFeatures.add(sb.toString());
		
		for(int key:keys){
			sb=new StringBuilder();
			List<Integer> list=featureMap.get(key);
			
			sb.append(key+",");
			
			for(int m=0;m<list.size();m++){
				sb.append(list.get(m)+",");
			}
			//set labels
			if(key<614){
				sb.append("1");
			}else{
				sb.append("0");
			}
			
			wekaFeatures.add(sb.toString());
		}
		System.out.println(featureMap.get(0).size());
		if(Write2File){
			WeiboWriter.write2file(wekaFeatures, "UnigramFeaturesWeka.txt");
		}
		return wekaFeatures;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FormatFeaturesForSVM(true);
	}
}