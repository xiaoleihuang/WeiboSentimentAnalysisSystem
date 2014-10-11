package model_weka;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Read instances of data from data file as "CSV" and "ARFF"
 * @author xiaolei
 */
public class WekaFileReaderUtils {
	/**
	 * Read CSV or ARFF file, the default class index is the last one as weka's default
	 * @param path file path
	 * @return Instances
	 * @throws Exception
	 */
	public static Instances getInstances(String path) throws Exception{
		if(path.contains(".arff")||path.contains(".csv")){
			DataSource data=new DataSource(path);
			Instances instances=data.getDataSet();
			
			instances.setClassIndex(instances.get(0).numAttributes()-1);
			return instances;
		}else
			return null;
	}
	/**
	 * Test
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Instances data=getInstances("C:\\Users\\Administrator\\Desktop\\UnigramFeaturesWeka.arff");
		System.out.println(data.size());
		System.out.println(data.get(0));
	}
}
