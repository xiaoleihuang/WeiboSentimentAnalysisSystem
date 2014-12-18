package model.mallet.topicmodel;

import io.WriterUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

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
public class JointSentimentPropagationTopicModel {
	public LDACompute lda;
	public List<HashSet<String>> topicWords;
	public HashMap<Integer, List<Double>> LDAfeatures;
	public HashMap<Integer, List<Double>> features=new HashMap<Integer,List<Double>>();
	public HashMap<String, Double> wordWeightMap;
	public WordInitalWeightedComputation wwc=new WordInitalWeightedComputation();
	public DictionarySentimentComputation dsc=new DictionarySentimentComputation(false);
	public double[][] sentimentPlarity;
	public int topicNum=0;
	/**
	 * Constructor
	 * @param numTopic The number of topics
	 * @throws IOException
	 */
	public JointSentimentPropagationTopicModel(int numTopic) throws IOException{
		//initialization and preparation
		this.topicNum=numTopic;
		lda=new LDACompute(topicNum);
		topicWords=lda.getTopicWords();
		LDAfeatures=lda.getFeatures();
//		wordWeightMap=wwc.getAllWordWeightMap();
		wordWeightMap=dsc.getSentimentPolarityMap();
		this.sentimentPlarity=new double[1][numTopic];
		this.sentimentPlarity[0]= formatSentimentPolarity(wordWeightMap, topicWords);
		
		//iterate each topic to recompute each topic's distribution
//		for(int topic=0;topic<topicNum;topic++){
//			double sum=	this.sentimentPlarity[topic];
//			//if sum is not equal to 0, we will change the topic distribution
//			if(sum!=0){
//				if(sum>0)
//					sum=Math.log1p(sum);
//				else{
//					sum=Math.log(1/(sum*(-1)));
//				}
//				List<Double> feature=this.features.get(topic);
//				for(int i=0;i<feature.size();i++){
//					feature.set(i, feature.get(i)*Math.pow(10,sum));
//				}
//				this.features.put(topic, feature);
//			}
//			
//		}
		
		//matrix computation
		
		double[][] matrix=new double[LDAfeatures.size()][numTopic];
		for(int topic=0;topic<LDAfeatures.size();topic++){
			List<Double> feature=LDAfeatures.get(topic);
			for(int i=0;i<feature.size();i++){
				matrix[topic][i]=feature.get(i);
			}
		}
		double[][] temp= new double[numTopic][numTopic];
		temp[0]=this.sentimentPlarity[0];
		SimpleMatrix smatrix=new SimpleMatrix(matrix);
		SimpleMatrix sentimentlayer=new SimpleMatrix(temp).svd().getV();
		smatrix=smatrix.mult(sentimentlayer);
		System.out.println(smatrix.numCols()+"\t"+smatrix.numRows());
		for(int topic=0;topic<smatrix.numRows();topic++){
			List<Double> list=new ArrayList<Double>();
			for(int i=0;i<smatrix.numCols();i++){
				list.add(smatrix.get(topic, i));
			}
			features.put(topic, list);
		}
		System.out.println(features.size()+"\t"+features.get(0).size());
	}
	
	/**
	 * 
	 * @param weightMap word's polarity map
	 * @param tWords words associate with each topic
	 * @return polarity matrix, each topic contains a matrix;
	 */
	public double[] formatSentimentPolarity(HashMap<String, Double> weightMap,List<HashSet<String>> tWords){
		double[]matrix=new double[this.topicNum];
		// the ratio here is ratio of suicide posts and none.
		int ratio=10;
		double max=0,min=0;
		//iterate each topic to recompute each topic's sentiment polarity
		for(int topic=0;topic<topicNum;topic++){
			HashSet<String> set=tWords.get(topic);
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
						sum=sum-2*(1/temp)/ratio;
					}
				}
			}
			//recompute sum
			if(sum!=0){
				if(sum>0){
//					sum=Math.log1p(sum);
//					sum=Math.pow(ratio,sum);
//					sum=Math.log(Math.pow(ratio,sum));
				}
				else{
//					sum=Math.pow(sum,ratio-1);
//					sum=(-1)*Math.log(1/(sum*(-1)));
//					sum=Math.pow(ratio,sum);
				}
				if(sum>max)
					max=sum;
				if(sum<min)
					min=sum;
			}
			
			System.out.println(max+"\t"+min);
			matrix[topic]=sum;
		}
		return matrix;
	}
	
	/**
	 * each topic's sentiment polarity
	 * @return sentiment polarity 1*topic matrix
	 */
	public double[][] getSentimentPolarity(){
		return this.sentimentPlarity;
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
		FeatureCombinerAndWriter.FormatFeaturesForSVM(LDAfeatures, true,label);
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
		int topic=500;
		JointSentimentPropagationTopicModel test=new JointSentimentPropagationTopicModel(topic);		
		result.add("Topic"+topic);
		String resultsSVM="";
		BufferedWriter writer=new BufferedWriter(new FileWriter("Comparison.txt",true));
		//control group
//		test.SaveComparisonLDAFeatures(664);
//		resultsSVM=LibSvmUtils.CrossValidattion(10, "./resource/UnigramFeaturesSVM.txt");
		writer.write("Topic"+topic+"\n");
//		result.add(resultsSVM);
		
		//our model
		test.SaveFeaturesForSVM(664);
		resultsSVM=LibSvmUtils.CrossValidattion(10, "./resource/FeaturesForSVM.txt");
		writer.write(resultsSVM+"\n");
		result.add(resultsSVM);
		result.add("\n");
		writer.flush();
		writer.close();

		WriterUtils.write2file(result, "Comparison Result");
	}
}