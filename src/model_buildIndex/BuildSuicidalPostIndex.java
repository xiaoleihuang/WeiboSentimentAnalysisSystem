package model_buildIndex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.lucene4.AnsjAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
public class BuildSuicidalPostIndex {
	private long start=0,end=0;
	private long duration=0;
	
	private Directory indexDir;
	private IndexWriter writer;
	GetStopWords gsw=new GetStopWords();
	private final String index2bePlaced="./index/";
	GetAllWeiboPosts alldata=new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/tempALL.txt");
	private List<OneWeibo> list=new ArrayList<OneWeibo>();
	
	public BuildSuicidalPostIndex() throws CorruptIndexException, IOException{
		start=System.currentTimeMillis();
		Set<String> filter=new HashSet<String>(gsw.getWords());
		list=alldata.getList();
		
		//The place to put index data. Configuring Lucene4
		File index=new File(this.index2bePlaced);
		indexDir=FSDirectory.open(index);
//		Analyzer luceneAnalyzer=new AnsjAnalysis(filter,false);
		Analyzer luceneAnalyzer=new SmartChineseAnalyzer(Version.LUCENE_4_9,new CharArraySet(Version.LUCENE_4_9,filter,false));
		writer=new IndexWriter(indexDir,new IndexWriterConfig(Version.LUCENE_4_9,luceneAnalyzer));

		for(OneWeibo comment:list){
			Document doc=new Document();
			doc.add(new Field("content",new String(comment.getContent().getBytes(),"UTF-8"),TextField.TYPE_STORED));
			
			doc.add(new StringField("pid",comment.getPid(), Field.Store.YES));
			writer.addDocument(doc);
			System.out.println(comment.getPid());
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
		BuildSuicidalPostIndex indexer=new BuildSuicidalPostIndex();
		indexer.close();
	}
}