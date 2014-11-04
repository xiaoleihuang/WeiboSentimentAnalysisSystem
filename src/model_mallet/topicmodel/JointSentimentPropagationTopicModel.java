package model_mallet.topicmodel;

import java.io.IOException;

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
	
	/**
	 * Constructor
	 * @throws IOException
	 */
	public JointSentimentPropagationTopicModel() throws IOException{
		lda=new LDACompute("./resource/LDATrainData.csv", 500, 0, 0);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}