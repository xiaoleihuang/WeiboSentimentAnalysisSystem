package model_buildIndex;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FilterAtomicReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class TermReader {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Directory dir=FSDirectory.open(new File("./index"));
		IndexReader r=DirectoryReader.open(dir);
		FilterAtomicReader reader=new FilterAtomicReader(SlowCompositeReaderWrapper.wrap(r));
		System.out.println(reader.numDocs());
		Terms terms=reader.terms("content");
		System.out.println(terms.getDocCount());
		TermsEnum enums=terms.iterator(null);
		System.out.println(enums);
		while(enums.next() != null){
			System.out.println(enums.term().utf8ToString()+" "+enums.docFreq());
		}
		
		dir.close();
		r.close();
		reader.close();
	}

}
