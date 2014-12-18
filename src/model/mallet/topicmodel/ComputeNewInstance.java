package model.mallet.topicmodel;

import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;

/**
 * By using TopicInferencer, it computes new document
 * @author xiaolei
 */
public class ComputeNewInstance {
	public static double[] getTopicDistribution(Instance instance,TopicInferencer interferencer){
		return interferencer.getSampledDistribution(instance, 100, 10, 10);
	}
}