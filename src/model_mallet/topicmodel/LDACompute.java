package model_mallet.topicmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import model_mallet.InstancesReader;
import model_svm.LibSvmUtils;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import retrieval_extractor.OneWeibo;
import retrieval_writer.WeiboWriter;

/**
 * Compute LDA distributions for each topic
 * 
 * @author xiaolei
 */
@SuppressWarnings("unused")
public class LDACompute {
	public final static String trainingpath = "./resource/LDATrainData.csv";
	public String testingpath = "";
	public ParallelTopicModel model;
	public int numTopics = 500;
	public static InstanceList traininglist;
	static {
		try {
			traininglist = InstancesReader.getInstances(trainingpath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Constructor, Create a model with given number of topics and the Default
	 * DataList, alpha_t = 0.01, beta_w = 0.01
	 * 
	 * @param topics
	 *            number of topics
	 * @throws IOException
	 */
	public LDACompute(int topics) throws IOException {
		if (topics != 0)
			this.numTopics = topics;
		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		model = new ParallelTopicModel(numTopics, 1.0, 0.05);
		model.addInstances(traininglist);
		// Use four parallel samplers, which each look at one half the corpus
		// and combine
		// statistics after every iteration.
		model.setNumThreads(1);
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(1500);
		model.estimate();
	}

	/**
	 * 
	 * @param info
	 *            one entry information
	 */
	public LDACompute(String[] info) {
		String pid = info[0];
		// note here the label could be random value, because this value will be
		// predicted
		String label = info[1];
		String content = info[2];

	}

	/**
	 * @param post
	 *            one post entry
	 */
	public LDACompute(OneWeibo post) {
		String pid = post.getPid();
		String label = "0";
		String content = post.getContent();
	}

	/**
	 * Constructor,Create a model with given number of topics, alpha_t = 0.01,
	 * beta_w = 0.05
	 * 
	 * @param testFilePath
	 *            input testing data file path
	 * @param topics
	 *            number of topics, default is 500
	 * @throws IOException
	 */
	public LDACompute(String testFilePath, int topics) throws IOException {
		new LDACompute(testFilePath, topics, 1, 0.05);
	}
	
	/**
	 * Constructor,Create a model with given number of topics, alpha_t = 0.01,
	 * beta_w = 0.01
	 * 
	 * @param testFilePath
	 *            input testing data file path
	 * @param topics
	 *            number of topics, default is 500
	 * @throws IOException
	 */
	public LDACompute(String testFilePath, int topics, double alphaSum, double beta) throws IOException {
		InstanceList instances = InstancesReader.getInstances(testFilePath);
		instances.addAll(traininglist);
		if (topics != 0)
			this.numTopics = topics;

		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		model = new ParallelTopicModel(numTopics, alphaSum, beta);
		model.addInstances(instances);
		// Use four parallel samplers, which each look at one half the corpus
		// and combine
		// statistics after every iteration.
		model.setNumThreads(4);
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(1500);
		model.estimate();
	}
	
	
	/**
	 * @return LDA model
	 */
	public ParallelTopicModel getModel() {
		return this.model;
	}

	/**
	 * @return training data
	 */
	public InstanceList getTrainingList() {
		return LDACompute.traininglist;
	}
	
	/**
	 * @return TopicInferencer
	 */
	public TopicInferencer getInterferencer(){
		return this.model.getInferencer();
	}
	
	/**
	 * @return features
	 */
	public HashMap<Integer, List<Double>> getFeatures() {
		HashMap<Integer, List<Double>> features = new HashMap<Integer, List<Double>>();
		for (int topic = 0; topic < model.getData().size(); topic++) {
			double[] probs = model.getTopicProbabilities(topic);
			List<Double> list = new ArrayList<Double>();
			for (int i = 0; i < probs.length; i++) {
				list.add(Math.pow(probs[i] * 100, 1));
			}
			features.put(topic, list);
		}
		// JOptionPane.showMessageDialog(null, max+"\n"+min);
		return features;
	}
	
	/**
	 * @return features as matrix format
	 */
	public double[][] getFeaturesAsMatrix(){
		int size=model.getData().size();
		double[][] matrix=new double[size][model.getTopicProbabilities(0).length];
		
		for(int i=0;i<size;i++){
			matrix[i]=model.getTopicProbabilities(i);
		}
		return matrix;
	}
	/**
	 * Write features to file, the default file type is csv, this file could be
	 * used as Training data and Testing data for Weka.
	 * 
	 * @param fileName
	 *            the name of file
	 * @return a writer contains all features
	 * @throws IOException
	 */
	public BufferedWriter WriteFeatures2file(String fileName)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"./resource/lda/" + fileName + ".csv"));
		writer.append("topic,");
		for (int a = 0; a < numTopics; a++) {
			if (a != (numTopics - 1))
				writer.append("topic" + a + ",");
			else
				writer.append("topic" + a + ",class" + "\n");
		}
		for (int topic = 0; topic < model.getData().size(); topic++) {
			double[] probs = model.getTopicProbabilities(topic);

			StringBuilder line = new StringBuilder();
			for (int i = 0; i < probs.length; i++)
				line.append(String.valueOf(probs[i]) + ",");
			// System.out.println(probs.length);
			String content = line.substring(0, line.length() - 1);
			// System.out.println(topic+"\t"+line);
			if (topic < 663)
				writer.append(topic + "," + content + ",1" + "\n");
			else
				writer.append(topic + "," + content + ",0" + "\n");
		}
		writer.flush();
		writer.close();
		return writer;
	}
	
	/**
	 * Write model to local file
	 */
	public void WriteModel2file() {
		this.model.write(new File("./resource/lda/model.model"));
	}

	/**
	 * Save features to local file as LibSVM format, the default file format is
	 * .txt
	 * 
	 * @param fileName
	 *            the name of file
	 * @throws IOException
	 */
	public void SaveDataforSvm2File(String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"./resource/ldaSvm/" + fileName + ".txt"));
		double max = 0.0, min = 1.0;
		for (int topic = 0; topic < model.getData().size(); topic++) {
			double[] probs = model.getTopicProbabilities(topic);

			StringBuilder line = new StringBuilder();
			for (int i = 0; i < probs.length; i++) {
				line.append(i + ":" + String.valueOf(probs[i] * 20) + " ");
				if (probs[i] > max)
					max = probs[i];
				if (probs[i] < min)
					min = probs[i];
			}
			// System.out.println(probs.length);
			// System.out.println(topic+"\t"+line);
			if (topic < 663)
				writer.append("1 " + line.toString() + "\n");
			else
				writer.append("0 " + line.toString() + "\n");
			if(topic%500==0)
				writer.flush();
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * Return topic words for each topic
	 * @return topic words
	 */
	public List<HashSet<String>> getTopicWords(){
		List<HashSet<String>> topicWords = new ArrayList<HashSet<String>>();
		
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
		Alphabet dataAlphabet = this.model.getAlphabet();
		
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
			HashSet<String> words=new HashSet<String>();
			while (iterator.hasNext()) {
				IDSorter idCountPair = iterator.next();
				words.add(dataAlphabet.lookupObject(idCountPair.getID()).toString().trim());
			}
			topicWords.add(words);
		}
		
		return topicWords;
	}
	
	/**
	 * Write topic words to file
	 * @throws IOException
	 */
	public void WriteTopicWords2File() throws IOException {
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
		Formatter out;
		double[] topicDistribution = model.getTopicProbabilities(0);
		Alphabet dataAlphabet = this.model.getAlphabet();
		// Show top 5 words in topics with proportions for the first document
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"./resource/lda/topicIterations/topics" + numTopics + ".txt"));
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic)
					.iterator();

			out = new Formatter(new StringBuilder(), Locale.US);
			out.format("%d\t%.3f\t", topic, topicDistribution[topic]);

			int rank = 0;

			while (iterator.hasNext()) {
				IDSorter idCountPair = iterator.next();
				// out.format("%s (%.0f) ",
				// dataAlphabet.lookupObject(idCountPair.getID()),
				// idCountPair.getWeight());
				out.format("%s ",
						dataAlphabet.lookupObject(idCountPair.getID()),
						idCountPair.getWeight());

				rank++;
			}
			writer.write(out.toString() + "\n");
		}
		writer.flush();
		writer.close();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			List<String> result=new ArrayList<String>();
			for (int i = 10; i <= 100; i = i + 10) {
				LDACompute lda = new LDACompute(i);
				lda.SaveDataforSvm2File("prob" + i);
				// lda.WriteFeatures2file("prob"+i);
				 lda.WriteTopicWords2File();
			}
			File dir=new File("./resource/ldaSvm/");
			for(File f:dir.listFiles())
				result.add("Topic"+f.getName()+"\n"+LibSvmUtils.CrossValidattion(10, f.getAbsolutePath())+"\n");
			
			WeiboWriter.write2file(result, "Result.txt");
		}
}