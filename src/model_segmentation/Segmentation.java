package model_segmentation;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import buildIndex.GetStopWords;

/**
 * Segment sentence into different terms, the terms could be either single word or phrase
 * @author xiaolei
 * @version 1.0
 */
public class Segmentation {
	List<Term> terms;
	GetStopWords getstop;
	/**
	 * constructor with preset stop words
	 */
	public Segmentation(){
		try {
			getstop = new GetStopWords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FilterModifWord.insertStopWords(getstop.getWords());
	}
	
	/**
	 * constructor with user defined stop words
	 * @param stopwords stop words
	 */
	public Segmentation(List<String> stopwords){
		FilterModifWord.insertStopWords(stopwords);
	}
	
	/**
	 * @param content the sentence needs to be segmented
	 * @return list of terms, contains terms' nature and segmented terms with filtered by stop words.
	 */
	public List<Term> getSegmentationResults(String content){
		terms=ToAnalysis.parse(content);
		new NatureRecognition(terms).recognition();
		terms=FilterModifWord.modifResult(terms);
		return this.terms;
	}
	
	/**
	 * static method to segment sentence into terms, this method with no stop word filter
	 * @param str the sentence needs to be segmented
	 * @return list of terms, contains terms' nature and segmented terms.
	 */
	public static List<Term> segmentSentence(String str){
		return ToAnalysis.parse(str);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		GetStopWords getstop = new GetStopWords();
		String str="我回北京了[熊猫]到家了！还有一个星期就开学啦！好想大家！！";
		Segmentation s=new Segmentation(getstop.getWords());
		List<Term> terms=s.getSegmentationResults(str);
		for(Term t:terms){
			System.out.print(t);
		}
	}
}