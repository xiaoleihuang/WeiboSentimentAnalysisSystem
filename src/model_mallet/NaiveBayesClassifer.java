package model_mallet;

import java.io.IOException;

import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;

public class NaiveBayesClassifer {
	public static NaiveBayes getTrainer() throws IOException{
		NaiveBayesTrainer trainer=new NaiveBayesTrainer();
		return trainer.train(InstancesReader.getInstances("/home/xiaolei/Desktop/dataset/trainData/train.txt"));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
