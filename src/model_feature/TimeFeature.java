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
	 * @param hour the hour number of posting time, in 24-hour value<br/>
	 * Value=24*percentageOfTrain*weight
	 * @return an assigned value
	 */
	public static int AssignValue(int hour){
		if(hour==1)
			return 10;
		else if(hour>1&&hour<4)
			return 14;
		else if(hour>3&&hour<6)
			return 16;
		else if(hour==23||hour==6)
			return 6;
		else if(hour>6&&hour<12)
			return 1;
		else if(hour>12&&hour<18)
			return 4;
		else if(hour>18&&hour<23)
			return 6;
		else
			return 0;
	}
	
	/**
	 * @return one Time feature
	 * @throws ParseException 
	 */
	public static int GetTimeFeature(String timeData) throws ParseException{
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Calendar ca=Calendar.getInstance();
		ca.setTime(formatter.parse(timeData));
		return AssignValue(ca.get(Calendar.HOUR_OF_DAY));
	}
	
	/**
	 * @return a list of Time features
	 * @throws ParseException 
	 */
	public static List<Integer> GetTimeFeatureList(List<String> timeData) throws ParseException{
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