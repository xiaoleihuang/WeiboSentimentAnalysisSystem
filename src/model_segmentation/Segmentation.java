package model_segmentation;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import buildIndex.GetStopWords;
import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;

/**
 * Segment sentence into different terms, the terms could be either single word or phrase
 * @author xiaolei
 * @version 1.0
 */
public class Segmentation {
	List<OneWeibo> list;
	GetAllWeiboPosts posts=new GetAllWeiboPosts();
	GetStopWords getstop;
	public Segmentation(){
		try {
			getstop=new GetStopWords();
			FilterModifWord.insertStopWords(getstop.getWords());
			list=posts.getList();
			List<Term> terms=ToAnalysis.parse("");
			new NatureRecognition(terms).recognition();
			terms=FilterModifWord.modifResult(terms);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}