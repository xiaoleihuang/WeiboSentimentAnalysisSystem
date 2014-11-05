package model_feature.Dictionary;

import java.util.HashMap;
import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;

/**
 * Compute each dictionary words's initial weight
 * In addition, we take Add-One (Laplace) Smoothing in this class.
 * @author xiaolei
 */
public class WordInitalWeightedComputation {
	public static GetAllWeiboPosts all;
	static{
		all=new GetAllWeiboPosts("./resource/Segmentedall.txt");
	}
	
	/**
	 * suicideWordMap and positiveWordMap do not really mean they are suicidal words or positive words.
	 * Their type depends on its post's type, whether the post is suicidal post or not.
	 * wordMap include all words contains in both maps.
	 */
	HashMap<String, Double> suicideWordMap=new HashMap<String, Double>();
	/**
	 * suicideWordMap and positiveWordMap do not really mean they are suicidal words or positive words.
	 * Their type depends on its post's type, whether the post is suicidal post or not.
	 * wordMap include all words contains in both maps.
	 */
	HashMap<String, Double> positiveWordMap=new HashMap<String, Double>();
	/**
	 * suicideWordMap and positiveWordMap do not really mean they are suicidal words or positive words.
	 * Their type depends on its post's type, whether the post is suicidal post or not.
	 * wordMap include all words contains in both maps.
	 */
	HashMap<String, Double> wordMap=new HashMap<String, Double>();
	
	/**
	 * Constructor
	 */
	public WordInitalWeightedComputation(){	
		for(OneWeibo post:all.getList()){
			String[] words=post.getContent().split(" ");
			if(Integer.valueOf(post.getSuicide())==1){
				for(String word:words){
					word=word.trim();
					if(word.length()==0||word==null||word==""){
						continue;
					}else{
						if(!suicideWordMap.keySet().contains(word)){
							suicideWordMap.put(word, 1.0);
						}else{
							suicideWordMap.put(word,suicideWordMap.get(word)+1);
						}
					}
				}
			}else{
				for(String word:words){
					word=word.trim();
					if(word.length()==0||word==null||word==""){
						continue;
					}else{
						if(!positiveWordMap.keySet().contains(word)){
							positiveWordMap.put(word, 1.0);
						}else{
							positiveWordMap.put(word,positiveWordMap.get(word)+1);
						}
					}
				}
			}
		}
		
		/**
		 * Compute words in suicideWordMap, and merge them into one Map, the initial value could refers to my first paper.
		 * In addition, we take Add-One (Laplace) Smoothing.
		 */
		for(String word:suicideWordMap.keySet()){
			if(positiveWordMap.containsKey(word)){
				this.wordMap.put(word, suicideWordMap.get(word)/positiveWordMap.get(word));
			}else{
				this.wordMap.put(word, 1+suicideWordMap.get(word));//which is equal to (1+suicideWordMap.get(word))/1
			}
		}
		/**
		 * Compute words in positiveWordMap, and merge them into one Map, the initial value could refers to my first paper.
		 * In addition, we take Add-One (Laplace) Smoothing.
		 */
		for(String word:positiveWordMap.keySet()){
			if(this.wordMap.containsKey(word)){
				continue;
			}else{
				this.wordMap.put(word, 1/(1+positiveWordMap.get(word)));
			}
		}
	}
	
	/**
	 * suicideWordMap and positiveWordMap do not really mean they are suicidal words or positive words.
	 * Their type depends on its post's type, whether the post is suicidal post or not.
	 * wordMap include all words contains in both maps.
	 */
	public HashMap<String, Double> getPositiveWordWeightMap(){
		return this.positiveWordMap;
	}
	
	/**
	 * suicideWordMap and positiveWordMap do not really mean they are suicidal words or positive words.
	 * Their type depends on its post's type, whether the post is suicidal post or not.
	 * wordMap include all words contains in both maps.
	 */
	public HashMap<String, Double> getSuicideWordWeightMap(){
		return this.suicideWordMap;
	}
	
	/**
	 * suicideWordMap and positiveWordMap do not really mean they are suicidal words or positive words.
	 * Their type depends on its post's type, whether the post is suicidal post or not.
	 * wordMap include all words contains in both maps.
	 */
	public HashMap<String, Double> getAllWordWeightMap(){
		return this.wordMap;
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}