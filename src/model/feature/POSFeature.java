package model.feature;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Using Stanford parser to derive sentence's POS features
 * @author xiaolei
 *
 */
public class POSFeature {
	public POSFeature(){
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String sentence="今天 天气 很好";
		Properties config = new Properties();
		config.load(new FileReader("./lib/stanford/stanford-postagger-full-2015-01-29/stanford-postagger-full-2015-01-30/models/chinese-distsim.tagger.props"));
		
		System.out.println(config.toString());
		
		MaxentTagger tagger = new MaxentTagger("./lib/stanford/stanford-postagger-full-2015-01-29/stanford-postagger-full-2015-01-30/models/chinese-distsim.tagger");
		System.out.println(tagger.tagTokenizedString(sentence));
		
		tagger= new MaxentTagger("./lib/stanford/stanford-postagger-full-2015-01-29/stanford-postagger-full-2015-01-30/models/chinese-nodistsim.tagger");
		System.out.println(tagger.tagTokenizedString(sentence));
	}

}