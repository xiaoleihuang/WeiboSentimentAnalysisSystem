package model_mallet.topicmodel;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model_feature.FeatureCombinerAndWriter;
import model_feature.Dictionary.LoadSentimentDictionary;
import model_feature.Dictionary.WordInitalWeightedComputation;
import model_svm.LibSvmUtils;

/**
 * This model is based on topic model.<br/>
 * @SentenceLevel
 * This model is based on assumptions that every keywords will influence other words around it.
 * The sentiment contains in the keywords will grant different level of sentiment to the words around it.
 * Each word around the keyword will convey the meaning of the keyword depended on the distance of both word.
 * @TopicLevel
 * The keywords will have influences on each topics. The assumptions here are same as assumptions in sentence level.
 * @author xiaolei
 */
public class JointSentimentPropagationTopicModel {
	public static HashSet<String> suicideDictionaries;
	public static HashSet<String> negativeDictionaries;
	public static HashSet<String> howNetNegativeDictionaries;
	public static HashSet<String> howNetPositiveDictionaries;
	
	//PRE-load Dictionaries
	static{
		try {
			suicideDictionaries=LoadSentimentDictionary.getSuicideWords();
			negativeDictionaries=LoadSentimentDictionary.getUpsetWords();
			howNetNegativeDictionaries=LoadSentimentDictionary.getHowNetNegativeWords();
			howNetPositiveDictionaries=LoadSentimentDictionary.getHowNetPositiveWords();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public LDACompute lda;
	public List<HashSet<String>> topicWords;
	public HashMap<Integer, List<Double>> features;
	public HashMap<String, Double> wordWeightMap;
	public WordInitalWeightedComputation wwc=new WordInitalWeightedComputation();
	/**
	 * Constructor
	 * @param numTopic The number of topics
	 * @throws IOException
	 */
	public JointSentimentPropagationTopicModel(int numTopic) throws IOException{
		//initialization and preparation
		lda=new LDACompute(numTopic);
		topicWords=lda.getTopicWords();
		features=lda.getFeatures();
		wordWeightMap=wwc.getAllWordWeightMap();
		
		//iterate each topic to 
		for(int topic=0;topic<numTopic;topic++){
			HashSet<String> set=topicWords.get(topic);
			double sum=0;
			
			for(String word:set){
				if(this.wordWeightMap.containsKey(word)){
					double temp=wordWeightMap.get(word);
					//we have two options here
					//1st just sum negative
					if(temp>=1)
						sum=sum+temp;
					/**
					 * 2nd sum both negative and positive
					 * ratio 
					 */
//					else{
//						sum=sum-1/temp;
//					}
				}
			}
			
			
			
			//if sum is not equal to 0, we will change the topic distribution
			if(sum!=0){
				sum=Math.log1p(sum);
				List<Double> feature=this.features.get(topic);
				for(int i=0;i<feature.size();i++){
					feature.set(i, feature.get(i)*Math.pow(10,sum));
				}
				this.features.put(topic, feature);
			}
			
		}
//		for(int topic=0;topic<topicWords.size();topic++){
//			
//		}
	}
	
	/**
	 * Format and Save features for SVM
	 * @throws IOException
	 */
	public void SaveFeaturesForSVM() throws IOException{
		FeatureCombinerAndWriter.FormatFeaturesForSVM(features, true);
	}
	
	/**
	 * Format and Save features for SVM
	 * @throws IOException
	 */
	public void SaveFeaturesForWeka() throws IOException{
		FeatureCombinerAndWriter.FormatFeaturesForWeka(features, true);
	}
	
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		JointSentimentPropagationTopicModel test=new JointSentimentPropagationTopicModel(500);
		test.SaveFeaturesForSVM();
		System.out.println(LibSvmUtils.CrossValidattion(10, "./resource/UnigramFeaturesSVM.txt"));
	}
}