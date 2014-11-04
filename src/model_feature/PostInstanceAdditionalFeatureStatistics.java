package model_feature;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;

import model_feature.Dictionary.LoadSentimentDictionary;

/**
 * Statistics about post instance, include time, forward,suicide and none suicide
 * @author xiaolei
 */
public class PostInstanceAdditionalFeatureStatistics {
	
	public static void main(String[] args) throws IOException, ParseException{
		// TODO Auto-generated method stub
		BufferedReader reader=new BufferedReader(new FileReader("./resource/Segmentedall.txt"));
		String line;
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Calendar ca;
		int suicide=0,nonesuicide=0,original=0,forward=0,noriginal=0,nforward=0;
		
		//suicide Time Count:
		//time1Count:23:00-7:00,time2Count:7:00-13:00,time3Count:13:00-18:00,time4Count:18:00-23:00;
		int time1Count=0,time2Count=0,time3Count=0,time4Count=0;
		
		//None suicide Time Count:
		//ntime1Count:23:00-7:00,ntime2Count:7:00-13:00,ntime3Count:13:00-18:00,ntime4Count:18:00-23:00;
		int ntime1Count=0,ntime2Count=0,ntime3Count=0,ntime4Count=0;
		
		//Reference Count:
		HashSet<String> SelfReferenceDictionary=LoadSentimentDictionary.loadDic("./resource/dic/selfReference.txt");
		HashSet<String> ReferenceDictionary=LoadSentimentDictionary.loadDic("./resource/dic/Reference.txt");
		int reference=0,selfReference=0,NoneSuicideselfReference=0,NoneSuicideReference=0;
		while((line=reader.readLine())!=null){
			String info[]=line.split("\t");
			ca=Calendar.getInstance();
			ca.setTime(formatter.parse(info[2]));
			int hour=ca.get(Calendar.HOUR_OF_DAY);
			
			int postForwardType=Integer.parseInt(info[3]);
			int postSuicideType=Integer.parseInt(info[4]);
			
			//Statistics of Self-reference and references
			for(String word:SelfReferenceDictionary){
				if(info[1].contains(word)&&postSuicideType==1){
					selfReference++;
					break;
				}else if(info[1].contains(word)&&postSuicideType==0){
					NoneSuicideselfReference++;
					break;
				}
			}
			for(String word:ReferenceDictionary){
				if(info[1].contains(word)&&postSuicideType==1){
					reference++;
					break;
				}else if(info[1].contains(word)&&postSuicideType==0){
					NoneSuicideReference++;
					break;
				}
			}
			
			//Time feature statistics
			if(hour==23||hour<6){
				if(postSuicideType==1){
					time1Count++;
				}else{
					ntime1Count++;
				}
			}else if(hour>=6&&hour<14){
				if(postSuicideType==1){
					time2Count++;
				}else{
					ntime2Count++;
				}
			}else if(hour>13&&hour<19){
				if(postSuicideType==1){
					time3Count++;
				}else{
					ntime3Count++;
				}
			}else{
				if(postSuicideType==1){
					time4Count++;
				}else{
					ntime4Count++;
				}
			}
			
			if(postSuicideType==1){
				suicide++;
			}else{
				nonesuicide++;
			}
			
			if(postSuicideType==1){
				if(postForwardType==0){
					forward++;
				}else{
					original++;
				}
			}else{
				if(postForwardType==0){
					nforward++;
				}else{
					noriginal++;
				}
			}
		}
		
		System.out.println("自杀条目\t非自杀条目");
		System.out.println(suicide+"\t"+nonesuicide);
		System.out.println("\n");
		
		System.out.println("自杀统计：");
		System.out.println("自杀微博时间统计：");
		System.out.println(time1Count+"\t"+time2Count+"\t"+time3Count+"\t"+time4Count);
		System.out.println(time1Count*100/suicide+"%\t"+time2Count*100/suicide+"%\t"+time3Count*100/suicide+"%\t"+time4Count*100/suicide+"%");
		System.out.println("微博原创、转发统计：");
		System.out.println(original+"\t"+forward);
		System.out.println(original*100/suicide+"%\t"+forward*100/suicide+"%");
		System.out.println("\n");
		
		System.out.println("非自杀统计：");
		System.out.println("非自杀微博时间统计：");
		System.out.println(ntime1Count+"\t"+ntime2Count+"\t"+ntime3Count+"\t"+ntime4Count);
		System.out.println(ntime1Count*100/nonesuicide+"%\t"+ntime2Count*100/nonesuicide+"%\t"+ntime3Count*100/nonesuicide+"%\t"+ntime4Count*100/nonesuicide+"%");
		System.out.println("微博原创、转发统计：");
		System.out.println(noriginal+"\t"+nforward);
		System.out.println(noriginal*100/nonesuicide+"%\t"+nforward*100/nonesuicide+"%");
		
		System.out.println("Self-Reference:");
		System.out.println(selfReference*100/suicide+"%"+"\t"+NoneSuicideselfReference*100/nonesuicide+"%");
		System.out.println("All-Reference:");
		System.out.println(reference*100/suicide+"%"+"\t"+NoneSuicideReference*100/nonesuicide+"%");
		reader.close();
	}
}