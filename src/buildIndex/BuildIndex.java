package buildIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;

/**
 * Using Lucene to build index of data set, by reading index we can get statistical data of term, term occurrence, term position
 * @author xiaolei
 * @version 1.0
 */
public class BuildIndex {
	private long start=0,end=0;
	private long duration=0;
	
	private Directory indexDir;
	private IndexWriter writer;
	GetStopWords gsw=new GetStopWords();
	private final String index2bePlaced="./index/";
	GetAllWeiboPosts alldata=new GetAllWeiboPosts();
	private List<OneWeibo> list=new ArrayList<OneWeibo>();
	
	public BuildIndex(){
		start=System.currentTimeMillis();
		//The place to put index data
		File index=new File(this.index2bePlaced);
		indexDir=FSDirectory.open(index);
		
		Set<String>stopwords=null;
//		=gsw.getWords();
		list=alldata.getList();
		
		Analyzer luceneAnalyzer=new SmartChineseAnalyzer(Version.LUCENE_36,stopwords);
		writer=new IndexWriter(indexDir,new IndexWriterConfig(Version.LUCENE_36,luceneAnalyzer));

		for(OneComment comment:list){
			Document doc=new Document();
			
			Field rate=new Field("rate", comment.getLevel(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS, Field.TermVector.NO);
			Field content=new Field("content", comment.getContent(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
			
			doc.add(rate);
			doc.add(content);
			writer.addDocument(doc);
		}
		
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
		BuildIndex indexer=new BuildIndex();
		indexer.close();
	}
}