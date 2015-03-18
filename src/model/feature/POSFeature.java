package model.feature;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Using Stanford parser to derive sentence's POS features
 * @author xiaolei
 *
 */
public class POSFeature {
	static MaxentTagger tagger;
	static{
		tagger= new MaxentTagger("./lib/stanford/stanford-postagger-full-2015-01-29/stanford-postagger-full-2015-01-30/models/chinese-distsim.tagger");
	}
	
	/**
	 * return a list of tagged words
	 * @param str
	 * @return a list of tagged words
	 */
	public static List<TaggedWord> process(String str){
		List<List<HasWord>> list = MaxentTagger.tokenizeText(new StringReader(str));
		return tagger.tagSentence(list.get(0));
	}
	
	/**
	 * return a tagged String
	 * @param str
	 * @return a tagged String
	 */
	public static String process1(String str){
		return tagger.tagTokenizedString(str);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String str = tagger.tagTokenizedString("最后 一 步，就是 随意 打开 一张 图，打开 “奇幻 咔咔” 软件 对准 图片，这 只 小熊 就 出来 了");
		System.out.println(POSFeature.process1(str));
	}
}