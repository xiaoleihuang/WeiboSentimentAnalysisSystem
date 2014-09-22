package model_feature;

import java.util.ArrayList;
import java.util.List;

import retrieval_extractor.OneWeibo;

/**
 * compute Posting Type's feature value
 * @author xiaolei
 */
public class PostingTypeFeature {
	
	/**
	 * Assign each type a value
	 * @param type the type of post, forward or original creation
	 * @return feature value
	 */
	public static double AssignValue(int type){
		if(type==1)
			return 10;
		else
			return -2;
	}
	
	/**
	 * @param str type of a post
	 * @return a post type feature
	 */
	public static double getTypeFeature(int type){
		return AssignValue(type);
	}
	
	/**
	 * @param list a list of types
	 * @return a list of features
	 */
	public static List<Double> getTypeFeatureList(List<OneWeibo> list){
		List<Double> typeFeatures=new ArrayList<Double>();
		for(OneWeibo post:list){
			typeFeatures.add(getTypeFeature(post.getType()));
		}
		return typeFeatures;
	}
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
