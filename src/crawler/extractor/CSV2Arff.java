package crawler.extractor;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class CSV2Arff {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		// load CSV
	    CSVLoader loader = new CSVLoader();
	    loader.setSource(new File("/home/xiaolei/Desktop/dataset/trainData/train.csv"));
	    Instances data = loader.getDataSet();
	 
	    // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File("./resource/train.arff"));
	    saver.writeBatch();
	  }
}