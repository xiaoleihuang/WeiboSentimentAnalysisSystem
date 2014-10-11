package model_weka;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
 * do WEKA evaluation
 * @author xiaolei
 */
public class WekaCrossValidation {
	/**
	 * 10-folds CrossValidation
	 * @throws Exception 
	 */
	public static void CrossValidation10(Instances data,Classifier classifier) throws Exception{
		CrossValidation(data, classifier, 10);
	}
	
	/**
	 * CrossValidation
	 * @throws Exception 
	 */
	public static void CrossValidation(Instances data,Classifier classifier,int folds) throws Exception{
		Evaluation evaluator=new Evaluation(data);
		evaluator.setDiscardPredictions(false);
		evaluator.crossValidateModel(classifier, data, folds, new Random(1));
	}
	
	/**
	 * Test
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CrossValidation10(WekaFileReaderUtils.getInstances("C:\\Users\\Administrator\\Desktop\\UnigramFeaturesWeka.arff"), NaiveBayesClassifier.getNaiveBayesClassifer());
	}
}