package model_weka;

import java.io.File;
import java.io.IOException;

import weka.classifiers.Evaluation;  
import weka.classifiers.functions.LinearRegression;  
import weka.core.Instances;  
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;  
  
public class LinearRegressionCpmpute {
	public static String trainPath="";
	public static String testPath="";
	public static LinearRegression lr=new LinearRegression();
	
	/**
	 * Train LinearRegression model, <b style="color:red;">Label should be the 2nd place</b> in CSV file.
	 * @param trainPath training file path
	 * @return  LinearRegression model
	 * @throws Exception
	 */
	public static LinearRegression trainModel(String trainPath) throws Exception{
		CSVLoader loader=new CSVLoader();
		loader.reset();
		loader.setSource(new File(trainPath));
		Instances traindata=loader.getDataSet();
		traindata.setClassIndex(1);
		
		lr.buildClassifier(traindata);
		return lr;
	}
	
	/**
	 * Read Instances data from <b style="color:red;">CSV file</b>
	 * @return Instances
	 * @throws IOException
	 */
	public static Instances readInstances() throws IOException{
		CSVLoader loader=new CSVLoader();
		loader.reset();
		loader.setSource(new File(trainPath));
		return loader.getDataSet();
	}
	
	/**
	 * Read data from file, and return double score results
	 * @param testPath test data file path,the file must be <b style="color:red;">CSV file</b>
	 * @param lr LinearRegression model
	 * @return scores
	 * @throws Exception
	 */
	public static double[] Predict(String testPath, LinearRegression lr) throws Exception{
		CSVLoader loader=new CSVLoader();
		loader.setSource(new File(testPath));
		Instances insTest = loader.getDataSet();
		insTest.setClassIndex(1);
		double[] score=new double[insTest.size()];
		for(int i=0;i<insTest.size();i++){
			score[i]=lr.classifyInstance(insTest.get(i));
		}
		return score;
	}
	
	/**
	 * return an evaluator for training data. <br/>
	 * 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sample code is listed below:<br/>
	 * 	&nbsp;&nbsp;&nbsp;eval.evaluateModel(LinearRegression, TestInstances);//evaluate the LinearRegression Model<br/>
        &nbsp;&nbsp;&nbsp;System.out.println(eval.meanAbsoluteError());//Compute ＭＡＥ
	 * @param testPath
	 * @param trainPath
	 * @return
	 * @throws Exception
	 */
	public static Evaluation buildEvaluator(String trainPath) throws Exception{
		CSVLoader loader=new CSVLoader();
		loader.setSource(new File(trainPath));
		Instances traindata=loader.getDataSet();
		Evaluation ev=new Evaluation(traindata);
		return ev;
	}

    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
        // TODO Auto-generated method stub  
        DataSource train_data = new DataSource(trainPath);//读训练数据  
        DataSource test_data = new DataSource(testPath);//读测试数据  

        Instances insTrain = train_data.getDataSet();  
        Instances insTest = test_data.getDataSet();
          
        insTrain.setClassIndex(insTrain.numAttributes()-1);//设置训练集中，target的index  
        insTest.setClassIndex(insTest.numAttributes()-1);//设置测试集中，target的index  
          
        LinearRegression lr = new LinearRegression();//定义分类器的类型  
        lr.buildClassifier(insTrain);//训练分类器
          
        Evaluation eval=new Evaluation(insTrain);
        eval.evaluateModel(lr, insTest);//评估效果
        System.out.println(eval.meanAbsoluteError());//计算ＭＡＥ
    }
}