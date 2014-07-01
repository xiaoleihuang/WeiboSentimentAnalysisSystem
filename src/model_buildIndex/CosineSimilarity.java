package model_buildIndex;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class CosineSimilarity {
	IndexReader reader=null;
	Directory indexDir=null;
	IndexSearcher searcher=null;
	Query q=null;
	Analyzer analyzer=null;
	Sort sort;
	TopDocs docs;
	
	double allScore[]=new double[10];
	double countScore=0.0;
	String[] contents=new String[10];
	double queryScore=0.0;

	GetStopWords gsw;
	Document[] docsReader;
	
	public CosineSimilarity(String query){
		try {
			gsw=new GetStopWords();
			indexDir=FSDirectory.open(new File("./index"));
			reader=IndexReader.open(indexDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		searcher=new IndexSearcher(reader);
		Set<String> filter=new HashSet<String>(gsw.getWords());
		analyzer=new SmartChineseAnalyzer(Version.LUCENE_36, filter);
		
		String fields="content";
		try {
			q=new QueryParser(Version.LUCENE_36, fields, analyzer).parse(query);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sort=Sort.RELEVANCE;
		
		//get top 10 docs
		try {
			docs=searcher.search(q,10, sort);
			//read comments
			docsReader=new Document[10];
			for(int i=0;i<10;i++){
				docsReader[i]=searcher.doc(docs.scoreDocs[i].doc);
				System.out.println(docsReader[i].get(fields));
			}
			ScoreDoc[] scoreDoc=docs.scoreDocs;
			
			for(int i=0;i<10;i++){
				String temp=scoreDoc[i].toString();
				System.out.println(temp);
//				temp=temp.substring(temp.length()-11, temp.length()-1);
//
//				double score = Double.valueOf(temp);
////				double score=scoreDoc[i].score;
//				
//				allScore[i]=score;
//				countScore=countScore+score;
//				
//				System.out.println(score);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//Calculate Scores for input query
//		for(int m=0;m<10;m++){
//			queryScore=queryScore+allScore[m]/countScore;
//		}
//		System.out.println(queryScore);				
	}
	
	public void close() throws Exception{
		searcher.close();
		reader.close();
		indexDir.close();
	}
	
	/**
	 * get top 10 query-related comments
	 * @return top 10 query-related comments
	 */
	public String[] getComments(){
		return this.contents;
	}
	
	/**
	 * get top 10 query-related comments' score
	 * @return top 10 query-related comments' score
	 */
	public double[] getScores(){
		return this.allScore;
	}
	/**
	 * get query's score
	 * @return query's score
	 */
	public double getQueryScore(){
		return this.queryScore;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CosineSimilarity("从今天起，我的微薄将会永远的关闭，再见各位！");
	}
}