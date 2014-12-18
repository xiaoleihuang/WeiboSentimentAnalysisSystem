package model.index;
import io.BasicReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetStopWords {
	final String path="./resource/stopwords.txt";
	private List<String> words=new ArrayList<String>();
	public GetStopWords() throws IOException{
		words=BasicReader.basicRead(path);
	}
	
	public List<String> getWords(){
		return this.words;
	}
	
	public static void main(String[] args) throws IOException{
		GetStopWords word=new GetStopWords();
		System.out.println(word.getWords().get(56));
	}
}