package crawler.extractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * extract time from user's posts
 * @author xiaolei
 * @version 1.0
 */
public class TimeExtractor {
	/**
	 * We have several format of post time, getting from Weibo System, but here we will make a Standard here yyyy-MM-dd HH:mm:ss
	 * (1)Standard time:2011-08-25 18:55:09 (2) This year's time 05月16日 16:59 
	 * (3)Today's time:今天 09:09 (4)In one hour's post:54分钟前
	 * @param time
	 * @return formated time
	 */
	public static String extractTime(String time){
		SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Calendar ca=Calendar.getInstance(Locale.CHINA);
		if(time.contains("今天")){
			time=time.split("天")[1].trim();
			String hour=time.split(":")[0];
			String minute=time.split(":")[1];
			ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH),Integer.valueOf(hour),Integer.valueOf(minute));
			return format.format(ca.getTime());
		}else if(time.contains("分钟前")){
			int minute=Integer.valueOf(time.split("分钟")[0]);
			int m=ca.get(Calendar.MINUTE);
			if(m>=minute)
				ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH),ca.get(Calendar.HOUR),m-minute);
			else if(ca.get(Calendar.HOUR)!=0)
				ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH),ca.get(Calendar.HOUR)-1,m+60-minute);
			else
				ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH),23,m+60-minute);
			return format.format(ca.getTime());
		}else if(time.contains("月")){
			int month=Integer.valueOf(time.trim().split("月")[0].trim());
			time=time.split("月")[1].trim();
			int day=Integer.valueOf(time.split("日")[0].trim());
			time=time.split("日")[1].trim();
			int hour=Integer.valueOf(time.split(":")[0]);
			int minute=Integer.valueOf(time.split(":")[1]);
			ca.set(ca.get(Calendar.YEAR), month, day, hour, minute);
			return format.format(ca.getTime());
		}else{
			SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return format.format(format1.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error";
			}
		}
	}
}
