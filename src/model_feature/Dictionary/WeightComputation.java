package model_feature.Dictionary;

import java.util.ArrayList;
import java.util.List;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;

/**
 * give a weight to the feature to solve imbalanced problems
 * @author Xiaolei Huang
 */
public class WeightComputation {
	public static List<String> contents=new ArrayList<String>();
	public static GetAllWeiboPosts all;
	static{
		all=new GetAllWeiboPosts("./resource/Segmentedall.txt");
	}
	
	/**
	 * Assign initial weight to feature
	 * @param ratio
	 * @return weight
	 */
	public double GetInitialWeight(double ratio,String word){		
		double countSuicide=0.0,countNoneSuicide=0.0;
		for(OneWeibo post:all.getList()){
			if(post.getContent().contains(word)&&Integer.parseInt(post.getSuicide())==1)
				countSuicide++;
			else
				countNoneSuicide++;
		}
		double weight= 10*Math.log(countSuicide+1/countNoneSuicide)*ratio;
		return weight;
	}
	
	/**
	 * Compute the ratio of suicidal posts and none suicidal posts
	 * @return ratio
	 */
	public double getRatio(){
		double ratio=0.0,SuicidePostCount=0.0,NoneSuicidalPostCount=0.0;
		for(OneWeibo post:all.getList()){
			if(Integer.parseInt(post.getSuicide())==1){
				SuicidePostCount++;
			}
			else
				NoneSuicidalPostCount++;
		}
		ratio=NoneSuicidalPostCount/SuicidePostCount;
		return ratio;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}