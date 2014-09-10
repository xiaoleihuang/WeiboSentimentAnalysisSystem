package model_segmentation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
/**
 * Using Stanford Parser to parser segmented sentence.
 * @author xiaolei
 * @version 1.0
 */
public class StanfordParser {
	static LexicalizedParser lp;
	Tree parse;
	Collection<TypedDependency> tdl;
	static{
		lp= LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/xinhuaFactored.ser.gz");
	}
	
	/**
	 * Constructor of Parser
	 * @param sentence 
	 */
	public StanfordParser(String[] sentence){
		List<CoreLabel> rawWords = Sentence.toCoreLabelList(sentence);
		parse= lp.apply(rawWords);
		parse.pennPrint();

		TreebankLanguagePack tlp = lp.getOp().langpack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);

		tdl=gs.allTypedDependencies();
	}
	
	/**
	 * @return Parsed tree of the sentence
	 */
	public Tree getTree(){
		return parse;
	}
	
	public Collection<TypedDependency> getBasicDependancy(){
		return tdl;
	}
	
	public ArrayList<TaggedWord> getTaggedWords(){
		return this.parse.taggedYield();
	}
	
	public static void main(String[] args) {
		String[] sent = { "他", "和", "我", "在", "学校", "里", "常", "打", "桌球", "。" };
		new StanfordParser(sent);
		
		
	}
}