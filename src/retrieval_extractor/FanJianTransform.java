package retrieval_extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Transform Traditional Chinese to Simplified Chinese, this could only be used as single word transformation<br/>
 * Extremely thanks both OpenCC and Ansj providing dictionary resources<br/>
 * {@link https://github.com/BYVoid/OpenCC/}<br/>
 * {@linkplain https://github.com/NLPchina/nlp-lang/}
 * @author xiaolei
 */
public class FanJianTransform {
	static HashMap<Character,Character> wordMapping;
	/**
	 * Using static method to read dictionary's word
	 */
	static{
		wordMapping=new HashMap<Character,Character>();
		try {
			BufferedReader reader=new BufferedReader(new FileReader("./library/FanJianMapping.dic"));
			String line;
			while((line=reader.readLine())!=null){
				String[] words=line.split("\t");
				wordMapping.put(words[0].charAt(0), words[1].charAt(0));
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Transform Traditional Chinese sentence into Simplified Chinese
	 * @param sentence
	 * @return Simplified Chinese sentence
	 */
	public static String transformSentence(String sentence){
		if(sentence==null)
			return null;
		if(sentence.trim().length()==0)
			return null;
		StringBuilder sb=new StringBuilder(sentence.length());
		char[] words=sentence.toCharArray();
		for(char word:words){
			sb.append(transformWord(word));
		}
		return sb.toString();
	}
	
	/**
	 * Transform Traditional Chinese word into Simplified word
	 * @param word
	 * @return Simplified Chinese word
	 */
	public static char transformWord(char word){
		try{
			String temp=wordMapping.get(word).toString();
			return temp.charAt(0);
		}catch(Exception e){
			return word;
		}			
	}
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sentence="輸入簡體字,點下面繁體字按鈕進行在線轉換";
		char[] words=sentence.toCharArray();
		for(char c:words)
			System.out.println(FanJianTransform.transformWord(c));
		System.out.println(FanJianTransform.transformSentence(sentence));
	}
}