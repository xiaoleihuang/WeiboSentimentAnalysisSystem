package model_feature;

import java.util.ArrayList;
import java.util.List;

import retrieval_extractor.GetAllWeiboPosts;

/**
 * give a weight to the feature to solve imbalanced problems
 * @author Xiaolei Huang
 *
 */
public class WeightComputation {
	public static List<String> contents=new ArrayList<String>();
	static{
		GetAllWeiboPosts all=new GetAllWeiboPosts("./resource/Segmentedall.txt");
		for(int i=0;i<all.getList().size();i++){
			contents.add(all.getList().get(i).getContent());
		}
	}
	
	/**
	 * Assign initial weight to feature
	 * @param ratio
	 * @return
	 */
	public double GetInitialWeight(double ratio){
		double weight= 10*Math.log(ratio);
		return weight;
	}
	
	/**
	 * Count word frequency in Suicidal posts
	 * @return count
	 */
	public double findSuicidalCount(String word){
		return 0.0;
	}
	
	/**
	 * Count word frequency in None Suicidal posts
	 * @return count
	 */
	public double findNoneSuicidalWordCound(String word){
		return 0.0;
	}
	
	/**
	 * Assign weight to value
	 * @return weight
	 */
	public double AssignWeight(){
		
		return 0.0;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}