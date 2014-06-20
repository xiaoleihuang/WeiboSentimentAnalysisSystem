package buildIndex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetStopWords {
	final String path="./resource/stopwords.txt";
	private List<String> words=new ArrayList<String>();
	public GetStopWords() throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(new File(path)));
		String line="";
		while((line=reader.readLine())!=null){
			words.add(line.trim());
		}
		reader.close();
	}
	
	public List<String> getWords(){
		return this.words;
	}
	
	public static void main(String[] args) throws IOException{
		GetStopWords word=new GetStopWords();
		System.out.println(word.getWords().get(56));
	}
}