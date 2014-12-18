package model.system;

import io.BasicReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import libsvm.svm_model;
import libsvm.svm_node;
import model.feature.WordFeatures;
import model.svm.LibSvmUtils;

/**
 * SVM predictor 
 * @author xiaolei
 *
 */
public class SentencePredictor_SVM {
	/**
	 * load SVM model
	 * @param path path of model
	 * @return
	 * @throws IOException
	 */
	public static svm_model loadSVMModel(String path) throws IOException{
		return LibSvmUtils.loadSVMModel(path);
	}
	
	/**
	 * derive features for list of sentences
	 * @param contents list of sentences
	 * @return features for sentences
	 * @throws IOException
	 */
	public static HashMap<Integer,List<Double>> deriveFeature(List<String> contents) throws IOException{
		HashMap<Integer, List<Double>>  featureMap=WordFeatures.GetFeatures(contents);
		return featureMap;
	}
	
	/**
	 * predictor
	 * @param model svm_model
	 * @param features one instance of features
	 * @return prediction results
	 */
	public static double predict(svm_model model, List<Double> features){
		//load SVM model
		svm_node[] nodes=new svm_node[features.size()];
		for(int i=0;i<features.size();i++){
			nodes[i]=new svm_node();
			nodes[i].index=i;
			nodes[i].value=features.get(i);
		}
		return LibSvmUtils.predict(model, nodes);
	}
	
	/**
	 * Train language model 
	 * @throws IOException 
	 */
	public static void trainSVM_languageModel_default() throws IOException{
		// TODO not completed yet
		List<String> train=WordFeatures.getTrainContents();
		
		//set labels
		double[] labels=new double[train.size()];
		for(int i=0;i<labels.length;i++){
			if(i<664)
				labels[i]=1;
			else
				labels[i]=0;
		}
		String Modelpath="./resource/model/model.model";
		HashMap<Integer,List<Double>> map=WordFeatures.GetFeatures(train);
//		FeatureCombinerAndWriter.FormatFeaturesForSVM(map, true, 663);
		LibSvmUtils.trainModelFromDataFlow(
				map, 
				LibSvmUtils.setSVMParameter(true, null), Modelpath, labels);
	}
	
	/**
	 * Train topic model
	 */
	public static void trainSVM_topicModel(){
		// TODO not completed yet
		
	}
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String testfilePath="./resource/test/test.txt";
		String modelPath="./resource/model/model.model";
		
		System.err.println("Train Default model");
//		SentencePredictor_SVM.trainSVM_languageModel_default();
		
		System.err.println("load Default model");
		svm_model model=SentencePredictor_SVM.loadSVMModel(modelPath);
		
		System.err.println("load test file");
		List<String> sentences=BasicReader.basicRead(testfilePath);
		
		
		System.err.println("Derive train data's Features");
		HashMap<Integer,List<Double>> featureMap=SentencePredictor_SVM.deriveFeature(sentences);
		
		System.err.println("Output reault");
		//0 represent none suicide, 1 represent suicide
		for(List<Double> features:featureMap.values())
			System.out.println(SentencePredictor_SVM.predict(model, features));
	}
}
