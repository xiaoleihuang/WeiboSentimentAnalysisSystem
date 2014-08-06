package model_mallet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;

public class BuildLDA {
	String path2="/home/xiaolei/Desktop/dataset/trainData/train.txt";
	@SuppressWarnings("unused")
	/**
	 * @param num number of topics
	 * @param alphaPath a topic matrix
	 * @param probPath a probability of topic matrix
	 * @throws IOException
	 */
	public BuildLDA(int num,String alphaPath,String probPath) throws IOException{
//		PrintStream outstream=new PrintStream(new File("./test.txt"));
//		System.setOut(outstream);
		ArrayList<Pipe> pipeList=new ArrayList<Pipe>();
		// Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add( new CharSequenceLowercase() );
        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new TokenSequenceRemoveStopwords(new File("/home/xiaolei/jar/mallet-2.0.7/stoplists/en.txt"), "UTF-8", false, false, false) );
        pipeList.add( new TokenSequence2FeatureSequence() );
		
        InstanceList instances=new InstanceList(new SerialPipes(pipeList));
        Reader fileReader=new InputStreamReader(new FileInputStream(new File(path2)),"UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, label, name fields
        
        // Create a model with 30 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = num;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
        model.addInstances(instances);
        
        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(4);
        
        // Run the model for 50 iterations and stop (this is for testing only, 
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(1000);
        model.estimate();
        
        // Show the words and topics in the first instance
        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();
//        Object[] arrays=dataAlphabet.toArray();
//        System.err.println("array length"+arrays.length);
//        int index=dataAlphabet.lookupIndex("对不起");
//        System.err.println(index);
        System.err.println("Size:"+dataAlphabet.size());
//        for(Object a:arrays){
//        	System.err.println(a.toString());
//        }
        
        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;
        
        
        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        System.err.println(out);
        
        // Estimate the topic distribution of the first instance, 
        //  given the current Gibbs state.
        double[] topicDistribution = model.getTopicProbabilities(0);
        

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
        
        // Show top 5 words in topics with proportions for the first document
        BufferedWriter writer=new BufferedWriter(new FileWriter("./resource/lda/topicIterations/"+alphaPath+"topics.txt"));
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
            
            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
            int rank = 0;
            
            while (iterator.hasNext()) {
                IDSorter idCountPair = iterator.next();
//                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                out.format("%s ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                
                rank++;
            }
            writer.write(out.toString()+"\n");
            
            System.out.println(out);
        }
        writer.flush();writer.close();
        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        //int rank = 0;
        while (iterator.hasNext()){ //&& rank < 5) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
        //    rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        System.out.println(testing.get(0));
        System.out.println(testProbabilities.length);
        System.out.println("\t" + testProbabilities[0]);
        
        //topic probability
        File featurefile = new File("job.arff");
        //BufferedWriter
        writer=new BufferedWriter(new FileWriter("./resource/lda/probIterations/"+probPath+"prob.csv"));
        writer.append("class,"+"topic,");
        for(int a=0;a<numTopics;a++){
        	if(a!=(numTopics-1))
        		writer.append("topic"+a+",");
        	else
        		writer.append("topic"+a+"\n");
        }
        for(int topic =0;topic<instances.size();topic++){
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
        System.out.print(instances.size());
        writer.flush();writer.close();
        
//        model.write(new File("./resource/lda/model.txt"));
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		for(int i=50;i<=1000;i=i+50){
			new BuildLDA(i,String.valueOf(i),String.valueOf(i));
		}
	}
}