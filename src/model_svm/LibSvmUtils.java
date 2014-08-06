package model_svm;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrieval_writer.WeiboWriter;

/**
 * LibSvm tools
 * @author xiaolei
 * @throws IOException
 */
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
		String accuracy=null;
		svm_train.main(TrainArgs);
		accuracy=System.getProperty("CrossAcurracy");
		return accuracy;
	}
	
	/**
	 * Training a model and then validate with using test data. Save the model under current project path
	 * @param TrainFile The file of train data
	 * @param TestFile The file of test data
	 * @param ResultFile The answer of TestFile
	 * @return The Accuracy
	 * @throws IOException
	 */
	public static String Predict(String TrainFile,String TestFile,String ResultFile) throws IOException{
		String[] trainArgs = {TrainFile};//directory of training file
		String modelFile = svm_train.main(trainArgs);
		String[] testArgs = {TestFile, modelFile, "UCI-breast-cancer-result"};//directory of test file, model file, result file
		Double accuracy = svm_predict.main(testArgs);
		
		System.out.println("SVM Classification is done! The accuracy is " + accuracy);
		return modelFile;
	}
	
	public static String TrainModel(String TrainFile) throws IOException{
		String[] trainArgs = {TrainFile};//directory of training file
		String modelFile = svm_train.main(trainArgs);
		return modelFile;
	}
	
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		//Test for svm_train and svm_predict
		//svm_train: 
		//	  param: String[], parse result of command line parameter of svm-train
		//    return: String, the directory of modelFile
		//svm_predect:
		//	  param: String[], parse result of command line parameter of svm-predict, including the modelfile
		//    return: Double, the accuracy of SVM classification
		//String modelFile;
//		String[] trainArgs = {"UCI-breast-cancer-tra"};//directory of training file
//		modelFile = svm_train.main(trainArgs);
//		String[] testArgs = {"UCI-breast-cancer-test", modelFile, "UCI-breast-cancer-result"};//directory of test file, model file, result file
//		Double accuracy = svm_predict.main(testArgs);
//		
//		System.out.println("SVM Classification is done! The accuracy is " + accuracy);

//		Test for cross validation
//		String[] crossValidationTrainArgs = {"-v", "10", "test.txt"};// 10 fold cross validation
//		modelFile = svm_train.main(crossValidationTrainArgs);
//		System.out.println(System.getProperty("CrossAcurracy"));
//		
		//Test for LDA data
		File dir=new File("./resource/ldaSvm");
		List<String> list=new ArrayList<String>();
		System.out.println(dir.list().length);
		for(File f:dir.listFiles()){
			System.out.println(f.getName());
			String[] crossValidationTrainArgs = {"-v", "10", f.getAbsolutePath()};// 10 fold cross validation
			svm_train.main(crossValidationTrainArgs);
			System.err.println(System.getProperty("CrossAcurracy"));
			list.add(System.getProperty("CrossAcurracy"));
		}
		WeiboWriter.write2file(list, "accuracy.txt");
//		
//		
//		System.out.print("Cross validation is done! The modelFile is " + modelFile);
	}
}