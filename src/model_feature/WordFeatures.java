package model_feature;

import java.io.BufferedReader;
import java.io.FileReader;
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
			dictionaries.add(LoadSentimentDictionary.getHowNetPositiveWords());
			BufferedReader reader=new BufferedReader(new FileReader("./tempTrainData"));
			String line;
			List<String> list=new ArrayList<String>();
			while((line=reader.readLine())!=null){
				list.add(line.split("\t")[1].trim());
			}
			reader.close();
			
			//Remove all words that does not contains in Training corpus
			for(HashSet<String> dic:dictionaries){
				List<String> d=new ArrayList<String>(dic);
				for(String word:d){
					boolean flag=true;
					for(String str:list){
						if(str.contains(word.trim())){
							flag=true;
							break;
						}
						else
							flag=false;
					}
					if(flag==false)
						dic.remove(word);
				}
				System.out.println(d.size());
			}
			System.out.println("Write Features");
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
		System.out.println("Feature Sizes: "+featureMap.get(0).size());
		
		return featureMap;
	}
	
	//delete all columns where contains all 0
	public static List<Integer> FilterList(
			HashMap<Integer, List<Integer>> featureMap) {
		// TODO Auto-generated method stub
		Set<Integer> keys=featureMap.keySet();
		List<List<Integer>>list=new ArrayList<List<Integer>>();
		List<Integer> filter=new ArrayList<Integer>();
		for(int i=0;i<featureMap.get(0).size();i++){
			list.add(new ArrayList<Integer>());
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
				filter.add(i);
			}
		}
		
		return filter;
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
//		List<Integer> filter=FilterList(featureMap);
		
		Set<Integer> keys=featureMap.keySet();
		StringBuilder sb=new StringBuilder();
		sb.append("key,");
		for(int i=0;i<featureMap.get(0).size();i++){
//			if(filter.contains(i)){
//				continue;
//			}
			sb.append("feature"+i+",");
		}
		sb.append("class");
		wekaFeatures.add(sb.toString());
		
		for(int key:keys){
			sb=new StringBuilder();
			List<Integer> list=featureMap.get(key);
			if(list.size()==0)
				continue;
			
			sb.append(key+",");
			
			for(int m=0;m<list.size();m++){
//				if(filter.contains(m))
//					continue;
				sb.append(list.get(m)+",");
			}
			//set labels
			if(key<614){
				sb.append("1");
			}else{
				sb.append("0");
			}
			if(sb.toString().contains(","))
				wekaFeatures.add(sb.toString());
		}
//		System.out.println(filter.size());
		if(Write2File){
			WeiboWriter.write2file(wekaFeatures, "UnigramFeaturesWeka.csv");
		}
		return wekaFeatures;
	}
	
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FormatFeaturesForWeka(true);
	}
}