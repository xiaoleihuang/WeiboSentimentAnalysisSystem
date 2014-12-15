package model_feature;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import model_mallet.topicmodel.JointSentimentPropagationTopicModelCopy;
import model_mallet.topicmodel.LDACompute;
import model_svm.LibSvmUtils;
import retrieval_extractor.GetAllWeiboPosts;
import retrieval_writer.WeiboWriter;

/**
 * Combine and Write all features to file
 * @author xiaolei
 */
public class FeatureCombinerAndWriter {
	public static HashMap<Integer,List<Double>> featureMap=new HashMap<Integer,List<Double>>();
	
	/**
	 * Format all features as a list of features
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static HashMap<Integer,List<Double>> CombineAllFeatures() throws IOException, ParseException{
		featureMap.putAll(WordFeatures.GetFeatures());
		GetAllWeiboPosts all=new GetAllWeiboPosts("./resource/Segmentedall.txt");
		List<Double> timeFeature=TimeFeature.GetTimeFeatureList(all.getList());
		List<Double> typeFeature=PostingTypeFeature.getTypeFeatureList(all.getList());
		LDACompute lda=new LDACompute(500);
		HashMap<Integer,List<Double>> ldaFeatures=lda.getFeatures();
		
		Set<Integer> set=featureMap.keySet();
		for(int i:set){
			List<Double> list=featureMap.get(i);
			list.add(timeFeature.get(i));
			list.add(typeFeature.get(i));
			list.addAll(ldaFeatures.get(i));
			featureMap.put(i, list);
		}
		System.out.println("Feature Size: "+ldaFeatures.get(0).size());
		return featureMap;
	}
	
	/**
	 * Formatted features for SVM
	 * @return
	 * @throws IOException 
	 */
	public static List<String> FormatFeaturesForSVM(HashMap<Integer,List<Double>> features,boolean Write2File,int label) throws IOException{
		List<String> svmFeatures=new ArrayList<String>();
		Set<Integer> keys=features.keySet();
		
		for(int key:keys){
			StringBuilder sb=new StringBuilder();
			List<Double> list=features.get(key);
			//set labels
			if(key<label){
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
			WeiboWriter.write2file(svmFeatures, "FeaturesForSVM.txt");
		}
		return svmFeatures;
	}
	
	/**
	 * Formatted features for Weka
	 * @return
	 * @throws IOException 
	 */
	public static List<String> FormatFeaturesForWeka(HashMap<Integer,List<Double>> features,boolean Write2File,int label) throws IOException{
		List<String> wekaFeatures=new ArrayList<String>();
//		List<Integer> filter=FilterList(featureMap);
		
		Set<Integer> keys=features.keySet();
		StringBuilder sb=new StringBuilder();
		sb.append("@relation 'FeaturesForWeka'\n");

		for(int i=0;i<features.get(0).size();i++){
//			if(filter.contains(i)){
//				continue;
//			}
			sb.append("@attribute "+i+" numberic\n");
		}
		sb.append("@attribute class {0,1}\n");
		sb.append("\n");
		sb.append("@data\n");
		
		wekaFeatures.add(sb.toString());
		
		for(int key:keys){
			sb=new StringBuilder();
			sb.append("{");
			List<Double> list=features.get(key);
			if(list.size()==0)
				continue;
			
			for(int m=0;m<list.size();m++){
//				if(filter.contains(m))
//					continue;
				if(list.get(m)==0)
					continue;
				sb.append(m+" "+list.get(m)+",");
			}
			//set labels
			if(key<label){
				sb.append(list.size()+" 1}");
				if(sb.toString().contains(","))
					wekaFeatures.add(sb.toString());
			}else{
				sb.append(list.size()+" 0}");
				if(sb.toString().contains(","))
					wekaFeatures.add(sb.toString());
			}
		}
//		System.out.println(filter.size());
		if(Write2File){
			WeiboWriter.write2file(wekaFeatures, "FeaturesForWeka.arff");
		}
		return wekaFeatures;
	}
	
	public static void SaveFeaturesAsCSV(HashMap<Integer,List<Double>> features,int label) throws IOException{
		List<String> csvFeatures=new ArrayList<String>();
		Set<Integer> keys=features.keySet();
		StringBuilder sb=new StringBuilder();
		
		for(int i=0;i<features.get(0).size();i++){
			sb.append("Feature"+i+",");
		}
		sb.append("class");
		csvFeatures.add(sb.toString());
		for(int key:keys){
			sb=new StringBuilder();
			List<Double> list=features.get(key);
			if(list.size()==0)
				continue;
			
			for(int m=0;m<list.size();m++){
//				if(filter.contains(m))
//					continue;
				if(list.get(m)==0)
					continue;
				sb.append(list.get(m)+",");
			}
			//set labels
			if(key<label){
				sb.append("1");
				if(sb.length()>1)
					csvFeatures.add(sb.toString());
			}else{
				sb.append("0");
				if(sb.length()>1)
					csvFeatures.add(sb.toString());
			}
		}
		WeiboWriter.write2file(csvFeatures, "FeaturesForWeka.csv");
	}
	
	/**
	 * Test
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
//		FormatFeaturesForSVM(CombineAllFeatures(), true);
//		FormatFeaturesForWeka(CombineAllFeatures(),true,664);
		JointSentimentPropagationTopicModelCopy test=new JointSentimentPropagationTopicModelCopy(500);
		featureMap.putAll(test.getFeatures());
		GetAllWeiboPosts all=new GetAllWeiboPosts("./resource/Segmentedall.txt");
		List<Double> timeFeature=TimeFeature.GetTimeFeatureList(all.getList());
		List<Double> typeFeature=PostingTypeFeature.getTypeFeatureList(all.getList());
		Set<Integer> set=featureMap.keySet();
		for(int i:set){
			List<Double> list=featureMap.get(i);
			list.add(timeFeature.get(i));
			list.add(typeFeature.get(i));
			featureMap.put(i, list);
		}
		
//		FormatFeaturesForSVM(featureMap,true, 664);
//		FormatFeaturesForWeka(featureMap,true,664);
		SaveFeaturesAsCSV(featureMap,664);
//		System.out.println(LibSvmUtils.CrossValidattion(10, "./resource/FeaturesForSVM.txt"));
	}
}