package model_feature;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

/**
 * Calculate Time feature
 * @author xiaolei
 */
public class TimeFeature {
	/**
	 * According to hour value, we assign a value to the instances
	 * @param hour the hour number of posting time, in 24-hour value
	 * @return an assigned value
	 */
	public static int AssignValue(int hour){
		if(hour==23||hour<7)
			return 20;
		else if(hour>6&&hour<12)
			return 1;
		else if(hour>12&&hour<18)
			return 5;
		else if(hour>18&&hour<23)
			return 15;
		else
			return 0;
	}
	
	/**
	 * @return Time features
	 * @throws ParseException 
	 */
	public static List<Integer> GetTimeFeature(List<String> timeData) throws ParseException{
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Calendar ca=Calendar.getInstance();
		List<Integer> timeFeatures=new ArrayList<Integer>();
		for(String str:timeData){
			ca.setTime(formatter.parse(str));
			timeFeatures.add(AssignValue(ca.get(Calendar.HOUR_OF_DAY)));
		}
		return timeFeatures;
	}
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}