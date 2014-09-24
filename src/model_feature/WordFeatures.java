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
			dictionaries.add(LoadSentimentDictionary.getUpsetWords());
			dictionaries.add(LoadSentimentDictionary.getHowNetNegativeWords());
			dictionaries.add(LoadSentimentDictionary.getHowNetPositiveWords());
			dictionaries.add(LoadSentimentDictionary.getBigramWords());
			dictionaries.add(LoadSentimentDictionary.getTigramWords());
			
			BufferedReader reader=new BufferedReader(new FileReader("./tempTrainData"));
			String line;
			List<String> list=new ArrayList<String>();
			while((line=reader.readLine())!=null){
				list.add(line.split("\t")[1].trim());
			}
			reader.close();
			
			//Remove all words that does not contains in Training corpus
			for(int i=0;i<dictionaries.size();i++){
				if(i>3)
					break;
				HashSet<String> dic=dictionaries.get(i);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructing feature matrix
	 */
	public static HashMap<Integer,List<Double>> GetFeatures(){		
		HashMap<Integer,List<Double>> featureMap=new HashMap<Integer,List<Double>>();
		for(int i=0;i<contents.size();i++){
			//negative VS. positive ratio, negativeCount is the one to count negative word number
//			double ratio=0.0,positiveCount=0.0,negativeCount=0.0;
			
			List<Double> list=new ArrayList<Double>();
			//Set value for each dictionary
			for(int m=0;m<dictionaries.size();m++){
				HashSet<String> dic=dictionaries.get(m);
				if(m==0)
					for(String word:dic){
						if(contents.get(i).contains(word)){
							list.add(28.0);
//							negativeCount++;
						}
						else
							list.add(0.0);
					}
				else if(m==1)
					for(String word:dic){
						if(contents.get(i).contains(word)){
							list.add(13.0);
//							negativeCount++;
						}
						else
							list.add(0.0);
					}
				else if(m==2)
					for(String word:dic){
						if(contents.get(i).contains(word)){
							list.add(10.0);
//							negativeCount++;
						}
						else
							list.add(0.0);
					}
				else if(m==3)
					for(String word:dic){
						if(contents.get(i).contains(word)){
							list.add(7.5);
//							positiveCount++;
						}
						else
							list.add(0.0);
					}
				else if(m==4)//BiGram Dictionary
					for(String str:dic){
						String[] words=str.split(",");
						boolean flag=true;
						for(String word:words){
							if(!contents.get(i).contains(word)){
								flag=false;
								break;
							}
						}
						if(flag){
							list.add(50.0);
//							negativeCount++;
						}
						else
							list.add(0.0);
					}
				else if(m==5)//TriGram Dictionary
					for(String str:dic){
						String[] words=str.split(",");
						boolean flag=true;
						for(String word:words){
							if(!contents.get(i).contains(word)){
								flag=false;
								break;
							}
						}
						if(flag){
							list.add(18.0);
//							negativeCount++;
						}
						else
							list.add(0.0);
					}
			}
//			if(positiveCount>negativeCount)
//				ratio=-10;
//			else
//				ratio=10;
			
//			list.add(ratio);
			featureMap.put(i, list);
		}
		System.out.println("Words Feature Sizes: "+featureMap.get(0).size());		
		dictionaries.clear();
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
	 * compute weight for each dictionary
	 * @return
	 */
	public static double DicWeightComputation(HashSet<String> dic){
		//initial weight
		double weight=10.0;

		
		return weight;
	}
	
	/**
	 * Formatted features for SVM
	 * @return
	 * @throws IOException 
	 */
	public static List<String> FormatFeaturesForSVM(boolean Write2File) throws IOException{
		HashMap<Integer,List<Double>> featureMap=GetFeatures();
		return FeatureCombinerAndWriter.FormatFeaturesForSVM(featureMap, Write2File);
	}
	
	/**
	 * Formatted features for Weka
	 * @return
	 * @throws IOException 
	 */
	public static List<String> FormatFeaturesForWeka(boolean Write2File) throws IOException{
		HashMap<Integer,List<Double>> featureMap=GetFeatures();
		return FeatureCombinerAndWriter.FormatFeaturesForWeka(featureMap, Write2File);
	}
	
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FormatFeaturesForSVM(true);
	}
}