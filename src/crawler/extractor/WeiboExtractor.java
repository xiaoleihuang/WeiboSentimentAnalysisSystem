package crawler.extractor;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import crawler.config.ErrorLogger;
import entity.OneWeibo;
/**
 * using static method to extract weibo information from one post
 * @author xiaolei
 * @version 1.0
 */
public class WeiboExtractor {
	/**
	 * Extract every Weibo_Tweet, like its content, forward or original creation, time and so on.
	 * But some features are useless, because after their die, a lot of people write down wishes under their posts.
	 * 2 type2 of post should be handled in 2 different ways.<br/>
	 * (1)ForwardPost:
	 * (2)Original creation
	 * Note that fromWho is the the people's post has been forwarded, this is his name account
	 * @param webElement
	 */
	public static OneWeibo extract(WebElement element) {
		// TODO Auto-generated method stub
		String id=element.getAttribute("id");
		String all=element.getText();
		try{
			if(all.contains("转发了")||all.contains("Repost")){
				List<WebElement> list=element.findElements(By.tagName("div"));
				WebElement e=list.get(0);
				String fromWho="";
				try{
					fromWho=e.findElement(By.tagName("a")).getAttribute("href").split("weibo.cn/")[1];
				}catch(Exception a){
					fromWho="";
				}
				if(fromWho.contains("?st")){
					fromWho=fromWho.split("?st")[0];
				}
				
				//get Original content
				String content=e.findElement(By.className("ctt")).getText().trim();
				content.replace("转发了", "");
				content.replace("Repost", "");
				
				//get Forward Reason
				e=list.get(list.size()-1);
				String temp=e.getText();
				String forwardReason=temp.split("  赞")[0].split("//@")[0].trim();
				if(forwardReason==""||forwardReason==null||forwardReason=="转发微博"||forwardReason=="Repost"){
					forwardReason="null";
				}
				
				//forwardChain: contains the accounts' nickname and their forward reasons
				String forwardChain=temp.split("  赞")[0].trim();
				if(forwardChain==""||forwardChain==null||!forwardChain.contains("//@")){
					forwardChain="null";
				}else{
					forwardChain=forwardChain.split("//@")[1];
					forwardChain="@"+forwardChain;
				}
				
				//get Time
				String time=e.findElement(By.className("ct")).getText().split("来自")[0];
				time=TimeExtractor.extractTime(time);
				return new OneWeibo(content, time, 0,fromWho, forwardReason, forwardChain);
			}else{
				String content=element.findElement(By.className("ctt")).getText();
				String time=element.findElement(By.className("ct")).getText().split("来自")[0];
				time=TimeExtractor.extractTime(time);
				return new OneWeibo(content, time, 1);
			}
		}catch(Exception e){
			ErrorLogger.ErrorLog(WeiboExtractor.class.getName(), e.getMessage(), all);
		}
		return null;
	}
}