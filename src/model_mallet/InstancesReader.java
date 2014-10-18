package model_mallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.InstanceList;

/**
 * Read file data from given file, return data as Instances list to trainer
 * @author xiaolei
 */
public class InstancesReader {
	/**
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static InstanceList getInstances(String filepath) throws IOException{
		ArrayList<Pipe> pipeList=new ArrayList<Pipe>();
		pipeList.add( new CharSequenceLowercase() );
        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new TokenSequenceRemoveStopwords(new File("./resource/stopwords.txt"), "UTF-8", false, false, false) );
        pipeList.add( new TokenSequence2FeatureSequence() );
        
        InstanceList instances=new InstanceList(new SerialPipes(pipeList));
        Reader fileReader=new InputStreamReader(new FileInputStream(new File(filepath)),"UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, label, name fields
        
        return instances;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}