package model.svm;
import io.WriterUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import model.feature.FeatureCombinerAndWriter;

/**
 * LibSvm tools
 * @author xiaolei
 * @throws IOException
 */
@SuppressWarnings("unused")
public class LibSvmUtils {
	/**
	 * Return n-folds cross validation Test
	 * @param foldsNum the Number of folds
	 * @param filePath The File will be read for Test
	 * @return the accuracy of the Test
	 * @throws IOException
	 */
	public static String CrossValidattion(int foldsNum,String filePath) throws IOException{
		String[] TrainArgs={"-v",foldsNum+"",filePath};
		StringBuilder result=new StringBuilder();
		svm_train.main(TrainArgs);
		result.append("CrossAcurracy"+"\t"+System.getProperty("CrossAcurracy")+"\n");
		result.append("F-measure"+"\t"+System.getProperty("F")+"\n");
		result.append("Precision"+"\t"+System.getProperty("P")+"\n");
		result.append("Recall"+"\t"+System.getProperty("R")+"\n");
		result.append("Total Correct"+"\t"+System.getProperty("Total")+"\n");
		result.append("Matrix"+"\t"+System.getProperty("Matrix")+"\n");
		return result.toString();
	}
	
	/**
	 * Training a model and then validate with using test data. Save the model under current project path
	 * @param TrainFile The file of train data
	 * @param TestFile The file of test data
	 * @param ResultFile The answer of TestFile
	 * @return The Accuracy
	 * @throws IOException
	 */
	public static String PredictFromDataFile(String TrainFile,String TestFile,String ResultFile) throws IOException{
		String[] trainArgs = {TrainFile};//directory of training file
		String modelFile = svm_train.main(trainArgs);
		String[] testArgs = {TestFile, modelFile, ResultFile};//directory of test file, model file, result file
		Double accuracy = svm_predict.main(testArgs);
		
		System.out.println("SVM Classification is done! The accuracy is " + accuracy);
		return modelFile;
	}
	
	/**
	 * 
	 * @param TrainFile
	 * @return
	 * @throws IOException
	 */
	public static String trainModelFromDataFile(String TrainFile) throws IOException{
		String[] trainArgs = {TrainFile};//directory of training file
		String modelFile = svm_train.main(trainArgs);
		return modelFile;
	}
	
	/**
	 * Set Parameters for SVM
	 * @param useDefault use default parameters
	 * @param paramTemp temp values
	 * @return svm_parameter
	 */
	public static svm_parameter setSVMParameter(boolean useDefault,svm_parameter paramTemp){
		svm_parameter param = new svm_parameter();
		
		if(useDefault||paramTemp==null){
		// default values
			param.svm_type = svm_parameter.C_SVC;
			param.kernel_type = svm_parameter.RBF;
			param.degree = 3;
			param.gamma = 0;	// 1/num_features
			param.coef0 = 0;
			param.nu = 0.5;
			param.cache_size = 100;
			param.C = 1;
			param.eps = 1e-3;
			param.p = 0.1;
			param.shrinking = 1;
			param.probability = 0;
			param.nr_weight = 0;
			param.weight_label = new int[0];
			param.weight = new double[0];
		}else{
			param=paramTemp;
		}
		
		return param;
	}
	
	/**
	 * Load Model
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static svm_model loadSVMModel(String path) throws IOException{
		if(path==null)
			return null;
		BufferedReader reader=new BufferedReader(new FileReader(path));
		return svm.svm_load_model(reader);
	}
	
	/**
	 * 
	 * @param featureMap
	 * @return 
	 * @throws IOException 
	 */
	public static svm_model trainModelFromDataFlow(HashMap<Integer, List<Double>> featureMap,svm_parameter param, String Modelpath,double[] labels) throws IOException{
		svm_problem problem=new svm_problem();
		
		Set<Integer> keys = featureMap.keySet();
		Vector<svm_node[]> data=new Vector<svm_node[]>();
		int featureSize=featureMap.get(keys.iterator().next()).size();	
		
		for(int i:keys){
			List<Double> features = featureMap.get(i);
			svm_node[] x = new svm_node[features.size()];
			for(int m=0;m<features.size();m++){
				x[m]=new svm_node();
				x[m].index=m;
				x[m].value=features.get(m);
			}
			data.add(x);
		}
		
		problem.l=labels.length;
		problem.x = new svm_node[problem.l][];
		for(int i=0;i<problem.l;i++)
			problem.x[i] = data.elementAt(i);
		problem.y=labels;
		
		//check the input data
		System.err.println(svm.svm_check_parameter(problem, param));
		//train SVM
		svm_model model=svm.svm_train(problem, param);
		if(Modelpath!=null)
			svm.svm_save_model(Modelpath, model);
		return model;
	}
	
	/**
	 * predictor
	 * @param model
	 * @param nodes
	 * @return
	 */
	public static double predict(svm_model model,svm_node[] nodes){
		return svm.svm_predict(model, nodes);
	}
	
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		//String modelFile;

//		Test for cross validation
		String[] crossValidationTrainArgs = {"-v", "10", "./resource/UnigramFeaturesSVM.txt"};// 10 fold cross validation
		svm_train.main(crossValidationTrainArgs);
		System.out.println(System.getProperty("CrossAcurracy"));

		//Test for LDA data
//		File dir=new File("./resource/ldaSvm");
//		List<String> list=new ArrayList<String>();
//		System.out.println(dir.list().length);
//		for(File f:dir.listFiles()){
//			System.out.println(f.getName());
//			String[] crossValidationTrainArgs = {"-v", "10", f.getAbsolutePath()};// 10 fold cross validation
//			svm_train.main(crossValidationTrainArgs);
//			System.err.println(System.getProperty("CrossAcurracy"));
//			list.add(System.getProperty("CrossAcurracy"));
//		}
//		WeiboWriter.write2file(list, "accuracy.txt");
//		System.out.print("Cross validation is done! The modelFile is " + modelFile);
	}
}