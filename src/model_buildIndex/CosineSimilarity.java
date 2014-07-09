package model_buildIndex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import retrieval_extractor.Regex;

public class CosineSimilarity {
	IndexReader reader=null;
	Directory indexDir=null;
	IndexSearcher searcher=null;
	Query q=null;
	Analyzer analyzer=null;
	TopDocs docs;
	
	double allScore[];
	double countScore=0.0;
	String[] contents=new String[10];
	double queryScore=0.0;

	GetStopWords gsw;
	Document[] docsReader;
	
	public CosineSimilarity(){
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
		
//		analyzer=new AnsjAnalysis(filter,false);
	}
	
	public double search(String query) throws ParseException{
		allScore=new double[10];
		countScore=0;
		queryScore=0;
		
		q=new QueryParser(Version.LUCENE_36, "content", analyzer).parse(query);
		
		//get top 10 docs
		try {
			docs=searcher.search(q,10);
			//read comments
			docsReader=new Document[10];
			
			for(int i=0;i<docs.scoreDocs.length-1;i++){
				docsReader[i]=searcher.doc(docs.scoreDocs[i].doc);
//				System.out.println(docs.scoreDocs[i]);
			}
			ScoreDoc[] scoreDoc=docs.scoreDocs;
			
			for(int i=0;i<docs.scoreDocs.length-1;i++){
				String temp=scoreDoc[i].toString();				
				temp=Regex.extractMotion(temp);
				double score=0;
				if(temp.contains("."))
					score= Double.valueOf(temp);
				else
					score=scoreDoc[i].score;
				allScore[i]=score;
//				System.out.println(score);
				
				countScore=countScore+score;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Calculate Scores for input query
//		System.out.println("countScore"+countScore);
		for(int m=0;m<docs.scoreDocs.length-1;m++){
//			System.out.println(allScore[m]);
			queryScore=queryScore+allScore[m]*(allScore[m]/countScore);
		}
//		System.out.println(queryScore);
		return queryScore;
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
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		HashMap<String,Double> map=new HashMap<String,Double>();
		File dir=new File("/home/xiaolei/Documents/Web Mining/project补充数据/Weibo");
		File[] files=dir.listFiles();
		CosineSimilarity c=new CosineSimilarity();
		List<String> errorLine=new ArrayList<String>();
		
		int count=0;
		List<String> list=new ArrayList<String>();
		List<String> list1=new ArrayList<String>();
		for(File file:files){
			BufferedReader reader=new BufferedReader(new FileReader(file));
			String line=new String();
			System.err.println(file.getName());
			
			while((line=reader.readLine())!=null){
				String pid;
				try{
					pid=line.split("\t")[0];
					line=line.split("\t")[1];
					count++;
				}catch(Exception e){
					continue;
				}
//				System.out.println(line);
				line=line.trim();
				line=line.concat(" ");
				line=line.replaceAll("\\[([^\\]]*)\\]", "");
				line=line.replace("?", "");
				
				line=line.replace("~", "");
				line=line.replace(":", "");
				line=line.replace("-", "");
				line=line.replace("^", "");
				line=line.replace("!", "");				
//				System.out.println(line);
				
				try{
					double score=c.search(line);
//					System.out.println(line+"\t"+score);
					if(score>1){
						list.add(file.getName()+"\t"+pid+"\t"+line+"\t"+score);
						System.out.println(line+"\t"+score);
					}else if(score<0.2){
						list1.add(score+"\t"+file.getName()+"\t"+pid+"\t"+line);
					}
				}catch(Exception e){
					errorLine.add(file.getName()+"\t"+pid+"\t"+line);
				}				
			}
			reader.close();
		}
		try {
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list.size());
		
		BufferedWriter writer=new BufferedWriter(new FileWriter("./CosineSimilarity.txt"));
		for(String s:list){
			writer.append(s+"\n");
		}
		writer.flush();
		writer.close();
		
		writer=new BufferedWriter(new FileWriter("./error.txt"));
		for(String s:errorLine){
			writer.append(s+"\n");
		}
		writer.flush();
		writer.close();
		
		Collections.sort(list1,Collator.getInstance(java.util.Locale.CHINA));
		writer=new BufferedWriter(new FileWriter("./a.txt"));
		for(String s:list1){
			writer.append(s+"\n");
		}
		writer.flush();
		writer.close();
		System.out.println(count);
	}
}