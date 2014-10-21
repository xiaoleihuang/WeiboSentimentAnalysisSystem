package model_buildIndex;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;
/**
 * Using Lucene4 to build index of data set, by reading index we can get statistical data of term, term occurrence, term position
 * @author xiaolei
 * @version 1.0
 */
@SuppressWarnings("unused")
public class BuildTrainDataSetIndex {
	private long start=0,end=0;
	private long duration=0;
	
	private Directory indexDir;
	private IndexWriter writer;
	GetStopWords gsw=new GetStopWords();
	private final String index2bePlaced="./index/";
	GetAllWeiboPosts alldata=new GetAllWeiboPosts("C:\\Users\\Administrator\\Desktop\\tempSuicide.txt");
	private List<OneWeibo> list=new ArrayList<OneWeibo>();
	
	public BuildTrainDataSetIndex() throws CorruptIndexException, IOException{
		start=System.currentTimeMillis();
		list=alldata.getList();
		
		//The place to put index data. Configuring Lucene4
		File index=new File(this.index2bePlaced);
		indexDir=FSDirectory.open(index);		
//		WhitespaceAnalyzer luceneAnalyzer=new WhitespaceAnalyzer(Version.LUCENE_4_9);
		Analyzer luceneAnalyzer=new SmartChineseAnalyzer(Version.LUCENE_4_9);
		writer=new IndexWriter(indexDir,new IndexWriterConfig(Version.LUCENE_4_9,luceneAnalyzer));

		for(OneWeibo comment:list){
			Document doc=new Document();
			doc.add(new Field("content",comment.getContent(),TextField.TYPE_STORED));
			doc.add(new StringField("pid",comment.getPid(), Field.Store.YES));
			doc.add(new StringField("label",comment.getDate(),Field.Store.YES));
			writer.addDocument(doc);
		}
		
		writer.commit();
		end=System.currentTimeMillis();
		duration=end-start;
		System.out.println("Building "+list.size()+" Comments'"+" Index costs: "+duration/1000+" seconds");
	}
	
	/**
	 * @return the duration of building index
	 */
	public long getDuration(){
		return this.duration;
	}
	
	/**
	 * close the index builder
	 * @throws Exception
	 */
	public void close() throws Exception{
		writer.close();
		indexDir.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		BuildTrainDataSetIndex indexer=new BuildTrainDataSetIndex();
//		indexer.close();
		
		DirectoryReader reader=DirectoryReader.open(FSDirectory.open(new File("./index")));
//		reader.getTermVector(0, "content").iterator(null).term();

		Document doc=reader.document(1);
		System.out.println();
		System.out.println(doc.get("content"));
	}
}