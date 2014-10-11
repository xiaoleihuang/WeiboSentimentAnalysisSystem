package model_weka;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;

/**
 * use NaiveBayes classifer from WEKA
 * @author xiaolei
 *
 */
public class NaiveBayesClassifier {
	public static Classifier getNaiveBayesClassifer() throws Exception{
		NaiveBayes nb=new NaiveBayes();
		String[] options={"-K"};
		nb.setOptions(options);
		return nb;
	}
}