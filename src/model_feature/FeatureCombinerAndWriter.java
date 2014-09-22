package model_feature;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_writer.WeiboWriter;

/**
 * Combine and Write all features to file
 * @author xiaolei
 */
public class FeatureCombinerAndWriter {
	public static HashMap<Integer,List<Integer>> featureMap=new HashMap<Integer,List<Integer>>();
	
	/**
	 * Format all features as a list of features
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static HashMap<Integer,List<Integer>> CombineAllFeatures() throws IOException, ParseException{
		featureMap.putAll(WordFeatures.GetFeatures());
		GetAllWeiboPosts all=new GetAllWeiboPosts("./resource/Segmentedall.txt");
		List<Integer> timeFeature=TimeFeature.GetTimeFeatureList(all.getList());
		List<Integer> typeFeature=PostingTypeFeature.getTypeFeatureList(all.getList());
		
		Set<Integer> set=featureMap.keySet();
		for(int i:set){
			List<Integer> list=featureMap.get(i);
			list.add(timeFeature.get(i));
			list.add(typeFeature.get(i));
			featureMap.put(i, list);
		}
		return featureMap;
	}
	
	/**
	 * Formatted features for SVM
	 * @return
	 * @throws IOException 
	 */
	public static List<String> FormatFeaturesForSVM(HashMap<Integer,List<Integer>> features,boolean Write2File) throws IOException{
		List<String> svmFeatures=new ArrayList<String>();
		Set<Integer> keys=features.keySet();
		
		for(int key:keys){
			StringBuilder sb=new StringBuilder();
			List<Integer> list=features.get(key);
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
	public static List<String> FormatFeaturesForWeka(HashMap<Integer,List<Integer>> features,boolean Write2File) throws IOException{
		List<String> wekaFeatures=new ArrayList<String>();
//		List<Integer> filter=FilterList(featureMap);
		
		Set<Integer> keys=features.keySet();
		StringBuilder sb=new StringBuilder();
		sb.append("key,");
		for(int i=0;i<features.get(0).size();i++){
//			if(filter.contains(i)){
//				continue;
//			}
			sb.append("feature"+i+",");
		}
		sb.append("class");
		wekaFeatures.add(sb.toString());
		
		for(int key:keys){
			sb=new StringBuilder();
			List<Integer> list=features.get(key);
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
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}