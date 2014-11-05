package model_mallet.topicmodel;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import model_feature.Dictionary.LoadSentimentDictionary;

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
	
	/**
	 * Constructor
	 * @param numTopic The number of topics
	 * @throws IOException
	 */
	public JointSentimentPropagationTopicModel(int numTopic) throws IOException{
		lda=new LDACompute(numTopic);
		topicWords=lda.getTopicWords();
		features=lda.getFeatures();
		
		//iterate each topic to 
		for(int topic=0;topic<numTopic;topic++){
			
		}
//		for(int topic=0;topic<topicWords.size();topic++){
//			
//		}
	}
	
	
	
	public static void main(String[] args) throws IOException {
		new JointSentimentPropagationTopicModel(50);
	}
}