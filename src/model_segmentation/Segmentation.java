package model_segmentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import model_buildIndex.GetStopWords;
import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;
import retrieval_extractor.Regex;
import retrieval_writer.WeiboWriter;

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
	 * static method to segment sentence into terms, this method with stop word filter
	 * @param content the sentence needs to be segmented
	 * @return list of terms, contains terms' nature and segmented terms with filtered by stop words.
	 */
	public List<Term> getSegmentationResults(String content){
		terms=BaseAnalysis.parse(content);
		new NatureRecognition(terms).recognition();
		terms=FilterModifWord.modifResult(terms);
		return this.terms;
	}
	
	/**
	 * static method to segment sentence into terms, this method with <b>no</b> stop word filter
	 * @param str the sentence needs to be segmented
	 * @return list of terms, contains terms' nature and segmented terms.
	 */
	public static List<Term> segmentSentence(String str){
		return ToAnalysis.parse(str);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		GetStopWords getstop = new GetStopWords();
		GetAllWeiboPosts alldata=new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/tempTrainData");
		List<OneWeibo> list=alldata.getList();
		Segmentation s=new Segmentation(getstop.getWords());
		List<String> segmentedList=new ArrayList<String>();
		int count=0;
		for(OneWeibo post:list){
			String str=Regex.removeHttp(post.getContent());
			str=Regex.removeRetweetName(str);
			str=Regex.removeAtUsers(str);
			try{
				List<Term> terms=s.getSegmentationResults(str);
			
				StringBuilder sb=new StringBuilder();
				for(Term t:terms){
					if(t.getName().trim()!=null&&t.getName().trim()!="")
						sb.append(t.getName().trim()+" ");
				}
				String temp=sb.toString().trim();
				temp=post.getPid()+"\t"+temp+"\t"+post.getDate()+"\t"+post.getType()+"\t"+post.getSuicide();
				
				segmentedList.add(temp);
			}catch(Exception e){
//				System.err.println(post.getPid()+"\t"+post.getContent()+"\t"+post.getDate()+"\t"+post.getType()+"\t"+post.getSuicide());
				e.printStackTrace();
				count++;
				continue;
			}
		}
		System.out.println(count);
		WeiboWriter.write2file(segmentedList, "Segmentedall.txt");
	}
}