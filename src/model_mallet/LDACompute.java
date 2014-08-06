package model_mallet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;
import retrieval_extractor.OneWeibo;

/**
 * Compute LDA distributions for each topic
 * @author xiaolei
 */
@SuppressWarnings("unused")
public class LDACompute {
	public final static String trainingpath="/home/xiaolei/Desktop/dataset/trainData/train.txt";
	public String testingpath="";
	public ParallelTopicModel model;
	public int numTopics=500;
	public static InstanceList traininglist;
	static{
		try {
			traininglist=InstancesReader.getInstances(trainingpath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param info one entry information
	 */
	
	public LDACompute(String[] info){
		String pid=info[0];
		//note here the label could be random value, because this value will be predicted
		String label=info[1];
		String content=info[2];
		
	}
	
	/**
	 * @param post one post entry
	 */
	public LDACompute(OneWeibo post){
		String pid=post.getPid();
		String label="0";
		String content=post.getContent();
	}
	
	/**
	 * Constructor,Create a model with given number of topics, alpha_t = 0.01, beta_w = 0.01
	 * @param testFilePath input testing data file path
	 * @param topics number of topics
	 * @throws IOException
	 */
	public LDACompute(String testFilePath,int topics) throws IOException{
		InstanceList instances=InstancesReader.getInstances(testFilePath);
		instances.addAll(traininglist);
		if(topics!=0)this.numTopics=topics;
		
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
		model=new ParallelTopicModel(numTopics, 1.0, 0.01);
		model.addInstances(instances);
        // Use four parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
		model.setNumThreads(4);
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(1000);
        model.estimate();
	}
	
	public ParallelTopicModel getModel(){
		return this.model;
	}
	
	public InstanceList getTrainingList(){
		return LDACompute.traininglist;
	}
	
	/**
	 * Write features to file, the default file type is csv
	 * @return a writer contains all features
	 * @throws IOException
	 */
	public BufferedWriter WriteFeatures2file() throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter("./resource/lda/probIterations/prob.csv"));
		writer.append("class,"+"topic,");
        for(int a=0;a<numTopics;a++){
        	if(a!=(numTopics-1))
        		writer.append("topic"+a+",");
        	else
        		writer.append("topic"+a+"\n");
        }
        for(int topic =0;topic<model.getData().size();topic++){
        	double[] probs = model.getTopicProbabilities(topic);
        	String line = new String();
        	for(int i=0;i<probs.length;i++)
        		line += String.valueOf(probs[i])+",";
//        		System.out.println(probs.length);
        	line=line.substring(0, line.length()-1);
//        	System.out.println(topic+"\t"+line);
        	if(topic<500)
        		writer.append("1,"+topic+","+line+"\n");
        	else
        		writer.append("0,"+topic+","+line+"\n");
        }
        writer.flush();
        writer.close();
		return writer;
	}
	
	public void WriteModel2file(){
		this.model.write(new File("./resource/lda/model.model"));
	}
	
	public static void getFeaturesForWeka(){
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}