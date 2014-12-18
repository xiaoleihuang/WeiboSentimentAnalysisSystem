package model.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Regex {

	/**
	 * @param args
	 */
	//extract the motion in []
	public static String extractMotion(String text){
		String result="";
		String regex="\\[([^\\]]*)\\]";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(text);
		while(matcher.find()){
			result+=matcher.group(1)+"\t";        
		}
		return result;
	}
	//抽取@的某人，多人以\t分隔
	public static String extractAtSomeOne(String text){
		String result="";
		String regex="@(.*)\\s";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(text);
		while(matcher.find()){
			result+=matcher.group(1)+"\t";        
		}
		return result;
	}
	
	/**
	 * Remove @users in WeiBo post
	 * @param str post
	 * @return post
	 */
	public static String removeAtUsers(String str){
		return str.replaceAll("@(.*)\\s", "");
	}
	
	/**
	 * 抽取链接，多个链接以\t分隔 Extract HTTP links
	 * @param text post
	 * @return post
	 */
	public static String extractHttp(String text){
		String result="";
		String regex="(http://.+)\\s+";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(text);
		while(matcher.find()){
			result+=matcher.group(1)+"\t";        
		}
		return result;
		
	}
	
	//去掉所有http
	public static String removeHttp(String text){
		return text.replaceAll("http://.*\\s+", "");
		
	}
	/**
	 * extract repost chain
	 * @param text post
	 * @return post
	 */
	public static String extractRetweetName(String text){
		String result="";
		String regex="//@([^//@]*):";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(text);
		while(matcher.find()){
			result+=matcher.group(1)+"\t";        
		}
		return result;
		
	}

	/**
	 * 去掉转发名字后的字符串,Remove RetweetString like //@冷笑话精选:
	 * @param text post
	 * @return post
	 */
	public static String removeRetweetName(String text){
		return text.replaceAll("//@([^//@]*):", "");		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String motion=" 宵夜去了！！回来再说！！[哈哈]";
		System.out.println(extractMotion(motion));
		System.out.println(motion.replaceAll("\\[([^\\]]*)\\]", ""));
		String atSomeOne="@lixie911  小Z拿回来了。去医院复诊，接着就回公司报道去。接近崩溃‍";
		System.out.println(extractAtSomeOne(atSomeOne));
		System.out.println(atSomeOne.replaceAll("@(.*)\\s", ""));
		String  link="http://t.cn/heuvLe 《孤独》把镜头对准那些生活在精神病院里有各种心理残疾的儿童，抑郁，自闭，妄想等各种精神疾病折磨着他们，被隔离在铁丝网的围墙内，发病时不停地用头撞墙发出闷响，墙上铺着很厚的海棉，但那声音依然让你无法忍受。她们就在监狱般的医院里，孤独地“活”着。";
		System.out.println(extractHttp(link));
		System.out.println(removeHttp(link));
		String retweet="  //@走饭:4、没死。//@虽浓未醇: 1、亲上一个同性的嘴唇,六十岁左右。2、裤子甩丢了。3、砸坏了路过行驶的奔驰要求赔偿。。 从急速行驶公交车里甩出去之后，最吓人的后果是什么样的？‍";
		System.out.println(extractRetweetName(retweet));
		System.out.println(removeRetweetName(retweet));
		
		Pattern pattern = Pattern.compile("~");
		Matcher matcher = pattern.matcher("~~~~正则表达式 Hel~~~lo World,正则表达式 Hello World ");
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
		    matcher.appendReplacement(sbr, "");
		}
		matcher.appendTail(sbr);
		System.out.println(sbr.toString());
		
		String line="周围食肆都排晒长龙??,醒目的我地!!~~好快就稳到位但原来考验在后面,几时有得吃阿!?";
		System.out.println(line.replace("?", ""));
		System.out.println(line.replace("~", ""));
	}
}