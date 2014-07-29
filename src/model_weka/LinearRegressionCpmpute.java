package model_weka;

import weka.classifiers.Evaluation;  
import weka.classifiers.functions.LinearRegression;  
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;  
  
public class LinearRegressionCpmpute {
	public static String trainPath="";
	public static String testPath="";
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
