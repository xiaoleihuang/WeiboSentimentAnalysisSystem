package model.Dictionary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Compute every word's sentiment polarity in dictionary
 * @author xiaolei
 */
public class DictionarySentimentComputation implements Serializable{
//	public static HashSet<String> suicideDictionaries;
//	public static HashSet<String> negativeDictionaries;
//	public static HashSet<String> howNetNegativeDictionaries;
//	public static HashSet<String> howNetPositiveDictionaries;
	
	private static final long serialVersionUID = 9180682333756736841L;
	private static HashSet<String> temp;
	/**
	 * Load dictionaries
	 */
	static{
		try {
			temp=LoadSentimentDictionary.getSuicideWords();
			temp.addAll(LoadSentimentDictionary.getUpsetWords());
			temp.addAll(LoadSentimentDictionary.getHowNetNegativeWords());
//			suicideDictionaries=LoadSentimentDictionary.getSuicideWords();
//			negativeDictionaries=LoadSentimentDictionary.getUpsetWords();
//			howNetNegativeDictionaries=LoadSentimentDictionary.getHowNetNegativeWords();
//			howNetPositiveDictionaries=LoadSentimentDictionary.getHowNetPositiveWords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public HashMap<String,Double> sentimentPolarityMap=new HashMap<String,Double>();
	
	/**
	 * Constructor, compute initial senitiment from training data set
	 * @param polarity with this true, return a map with all 1 initial weight
	 */
	public DictionarySentimentComputation(boolean polarity){
		//initialize sentiment
		if(polarity){
			for(String word:temp){
				this.sentimentPolarityMap.put(word, 1.0);
			}
		}else{
			WordInitalWeightedComputation wiwc=new WordInitalWeightedComputation();
			HashMap<String, Double> tempMap=wiwc.getAllWordWeightMap();
			for(String word:temp){
				if(tempMap.containsKey(word)){
					this.sentimentPolarityMap.put(word, tempMap.get(word));
				}else{
//					this.sentimentPolarityMap.put(word, 1.0);
				}
			}
		}
	}
	
	public HashMap<String, Double> getSentimentPolarityMap(){
		return this.sentimentPolarityMap;
	}
	
	/**
	 * load sentiment map from file
	 * @return sentiment Map
	 */
	public static HashMap<String,Double> loadSentimentPolarityMap(String path){
		try {
			ObjectInputStream stream=new ObjectInputStream(new FileInputStream(path));
			SentimentPolarityMapObject object=(SentimentPolarityMapObject)stream.readObject();
			stream.close();
			return object.getMap();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * write map to file
	 * @param path
	 * @throws IOException 
	 * @throws  
	 */
	public void writeSentimentPolarityMap(String path) throws IOException{
		ObjectOutputStream output=new ObjectOutputStream(new FileOutputStream(path));
		output.writeObject(new SentimentPolarityMapObject(this.sentimentPolarityMap));
		output.flush();
		output.close();
	}
	
	class SentimentPolarityMapObject implements Serializable{
		private static final long serialVersionUID = 8867759214847948893L;
		HashMap<String,Double> map;
		public SentimentPolarityMapObject(HashMap<String,Double> map){
			this.map=map;
		}
		
		public HashMap<String,Double> getMap(){
			return this.map;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}