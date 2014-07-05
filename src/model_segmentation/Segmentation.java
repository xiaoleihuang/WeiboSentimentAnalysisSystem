package model_segmentation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model_buildIndex.GetStopWords;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;
import retrieval_extractor.Regex;

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
		terms=NlpAnalysis.parse(content);
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
		GetAllWeiboPosts alldata=new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/all.txt");
		List<OneWeibo> list=alldata.getList();
		Segmentation s=new Segmentation(getstop.getWords());
		List<String> segmentedList=new ArrayList<String>();
		
		for(OneWeibo post:list){
			String str=Regex.removeHttp(post.getContent());
			List<Term> terms=s.getSegmentationResults(str);
			StringBuffer buffer=new StringBuffer();
			for(Term t:terms){
				buffer.append(t.getName()+" ");
			}
			segmentedList.add(buffer.toString().trim());
		}
		
		BufferedWriter writer=new BufferedWriter(new FileWriter("/home/xiaolei/Desktop/dataset/suicide/Segmentedall.txt"));
		for(String str:segmentedList){
			writer.append(str+"\n");
		}
		writer.flush();
		writer.close();
	}
}