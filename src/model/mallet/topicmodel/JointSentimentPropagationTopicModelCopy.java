package model.mallet.topicmodel;

import io.WriterUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model.Dictionary.DictionarySentimentComputation;
import model.Dictionary.WordInitalWeightedComputation;
import model.feature.FeatureCombinerAndWriter;
import model.svm.LibSvmUtils;

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
public class JointSentimentPropagationTopicModelCopy {
	public LDACompute lda;
	public List<HashSet<String>> topicWords;
	public HashMap<Integer, List<Double>> features=new HashMap<Integer, List<Double>>();
	public HashMap<String, Double> wordWeightMap;
	public WordInitalWeightedComputation wwc=new WordInitalWeightedComputation();
	public DictionarySentimentComputation dsc=new DictionarySentimentComputation(false);
	/**
	 * Constructor
	 * @param numTopic The number of topics
	 * @throws IOException
	 */
	public JointSentimentPropagationTopicModelCopy(int numTopic) throws IOException{
		//initialization and preparation
		lda=new LDACompute(numTopic);
		topicWords=lda.getTopicWords();
		features=lda.getFeatures();
//		wordWeightMap=wwc.getAllWordWeightMap();
		wordWeightMap=dsc.getSentimentPolarityMap();
		// the ratio here is ratio of suicide posts and none.
		int ratio=10;
		
		//iterate each topic to recompute each topic's distribution
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
					 * 2nd sum both negative and positive.
					 * the ratio here is ratio of suicide posts and none.
					 * The ratio is to normalize the data.
					 */
					else{
						sum=sum-(1/temp)/ratio;
					}
				}
			}
						
			//if sum is not equal to 0, we will change the topic distribution
			if(sum!=0){
				if(sum>0)
					sum=Math.log1p(sum);
				else{
					sum=Math.log(1/(sum*(-1)));
				}
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
	
	public HashMap<Integer, List<Double>> getFeatures(){
		return features;
	}
	/**
	 * Format and Save features for SVM
	 * @throws IOException
	 */
	public void SaveFeaturesForSVM(int label) throws IOException{
		FeatureCombinerAndWriter.FormatFeaturesForSVM(features, true,label);
	}
	
	/**
	 * Format and Save features for SVM
	 * @throws IOException
	 */
	public void SaveFeaturesForWeka(int label) throws IOException{
		FeatureCombinerAndWriter.FormatFeaturesForWeka(features, true,label);
	}
	
	/**
	 * Save LDA model generated features
	 * @throws IOException
	 */
	public void SaveComparisonLDAFeatures(int label) throws IOException{
		FeatureCombinerAndWriter.FormatFeaturesForSVM(lda.getFeatures(), true,label);
	}
	
	/**
	 * Save topic words to files
	 * @throws IOException
	 */
	public void SaveTopicWord() throws IOException{
		lda.WriteTopicWords2File();
	}
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		List<String> result=new ArrayList<String>();
		BufferedWriter writer=new BufferedWriter(new FileWriter("Comparison.txt",true));
		for(int topic=100;topic<=1000;topic+=100){
		JointSentimentPropagationTopicModelCopy test=new JointSentimentPropagationTopicModelCopy(topic);		
		result.add("Topic"+topic);
		String resultsSVM="";
		
		//control group
//		test.SaveComparisonLDAFeatures(664);
//		result.add(LibSvmUtils.CrossValidattion(10, "./resource/UnigramFeaturesSVM.txt"));
		
		//our model
		test.SaveFeaturesForSVM(664);
		resultsSVM=LibSvmUtils.CrossValidattion(10, "./resource/UnigramFeaturesSVM.txt");
		writer.write(resultsSVM+"\n");
		result.add(resultsSVM);
		result.add("\n");
		writer.flush();
		}
		writer.close();
		WriterUtils.write2file(result, "Comparison Result");
	}
}