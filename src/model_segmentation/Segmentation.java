package model_segmentation;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import model_buildIndex.GetStopWords;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

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
	public List<String> getSegmentationResults(String content){
		List<String> list=new ArrayList<String>();
		try{
			terms=NlpAnalysis.parse(content);
			new NatureRecognition(terms).recognition();
			terms=FilterModifWord.modifResult(terms);
			for(Term t:terms){
				String term=t.getName().trim();
				if(term!=null&&term!=""&&term!=" "){
					list.add(term);
				}else{
					continue;
				}
			}
			
		}catch(Exception e){
			StringReader reader=new StringReader(content);
			IKSegmenter ik=new IKSegmenter(reader,true);
			Lexeme lex=null;
			try {
				while((lex=ik.next())!=null){
					list.add(lex.getLexemeText().trim());
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			reader.close();
		}
		return list;
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
		for(OneWeibo post:list){
			String str=post.getContent();
//			if(str.contains("http")||str.contains("//@")||str.contains("@")){
			System.out.println(str);
				str=Regex.removeHttp(post.getContent());
				str=Regex.removeRetweetName(str);
				str=Regex.removeAtUsers(str);
				System.out.println(str);
//			}else{
//				str=post.getContent();
//			}
			List<String> terms;
			StringBuilder sb=new StringBuilder();
			terms=s.getSegmentationResults(str);
//			System.out.println(terms.size());
			try{
				for(String t:terms){
					t=t.trim();
					if(t!=null&&t!=""&&t!=" ")
						sb.append(t+" ");
				}
				String temp=sb.toString().trim();
				temp=post.getPid()+"\t"+temp+"\t"+post.getDate()+"\t"+post.getType()+"\t"+post.getSuicide();
				segmentedList.add(temp);
			}catch(Exception e){
				System.err.println(str);
//				e.printStackTrace();
				continue;
			}
		}
		WeiboWriter.write2file(segmentedList, "Segmentedall.txt");
	}
}