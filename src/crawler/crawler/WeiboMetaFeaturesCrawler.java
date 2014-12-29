package crawler.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * crawl user's comments, favorite, reposts
 * @author xiaolei
 */
public class WeiboMetaFeaturesCrawler {
	static Logger log=Logger.getLogger(WeiboMetaFeaturesCrawler.class.getName());
	private static String username,password;
	static{
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"./resource/config/account.conf"));
			username = reader.readLine();
			password = reader.readLine();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private HtmlUnitDriver unit;
	private Login login;
	
	/**
	 *  it will direct driver to http://weibo.cn/META/ID.
	 * The META could be attitude, comment, or favorite
	 * Constructor
	 * @throws IOException
	 */
	public WeiboMetaFeaturesCrawler() throws IOException{
		login=new Login(WeiboMetaFeaturesCrawler.username, WeiboMetaFeaturesCrawler.password);
		unit=login.getDriver();
	}
	
	/**
	 * Extract comments
	 * @param weiboID the id of weibo on the web, like M_BD3mkavDG.
	 * @param uid user's id
	 * @return 
	 */
	public List<WebElement> extractComments(String weiboID,String uid){
		this.unit.get("http://weibo.cn/comment/"+weiboID+"?&uid="+uid);
		
		//indicate the number of pages
		int page=0;
		WebElement pageElement=unit.findElement(By.id("pagelist"));
		if(pageElement!=null){
			page=Integer.parseInt(pageElement.findElement(By.name("mp")).getAttribute("value"));
		}
		
		List<WebElement> elements=new ArrayList<WebElement>();
		System.err.println(page);
		
		if(page!=0)
			for(int i=1;i<=page;i++){
				unit.get("http://weibo.cn/comment/"+weiboID+"?uid="+uid+"&page="+i);
				elements.addAll(unit.findElements(By.className("c")));
			}
		else{
			elements=unit.findElements(By.className("c"));
		}
		
		//filter process
		for(WebElement e:elements){
			if(e.findElement(By.className("ctt"))==null){
				elements.remove(e);
				continue;
			}
		}
		return elements;
	}
	
	/**
	 * Extract favorite
	 * @param weiboID the id of weibo on the web, like M_BD3mkavDG.
	 * @param uid user's id
	 * @return 
	 */
	public List<WebElement> extractFavorite(String weiboID){
		this.unit.navigate().to("http://weibo.cn/attitude/"+weiboID);
		
		//indicate the number of pages
		int page=0;
		System.out.println(unit.getCurrentUrl());
		WebElement pageElement=unit.findElement(By.className("pa"));
		if(pageElement!=null){
			page=Integer.parseInt(pageElement.findElement(By.name("mp")).getAttribute("value"));
		}
		
		System.err.println(unit.getPageSource());
		
		List<WebElement> elements=unit.findElements(By.className("c"));;
				
		//iterate to read every elements
		if(page!=0){
			for(int i=2;i<=page;i++){
				unit.get("http://weibo.cn/attitude/"+weiboID+"?rl=1&page="+i);
				elements.addAll(unit.findElements(By.className("c")));
			}
		}
		
		//filter process
		for(WebElement e:elements){
//			if(!e.getText().contains("分钟前")){
//				elements.remove(e);
//				continue;
//			}
			System.err.println(e.getText());
		}
		return elements;
	}
	
	/**
	 * Extract repost
	 * @param weiboID the id of weibo on the web, like M_BD3mkavDG.
	 * @param uid user's id
	 * @return 
	 */
	public List<WebElement> extractRepost(String weiboID){
		this.unit.get("http://weibo.cn/repost/"+weiboID);
		
		//indicate the number of pages
		int page=0;
		WebElement pageElement=unit.findElement(By.id("pagelist"));
		if(pageElement!=null){
			page=Integer.parseInt(pageElement.findElement(By.name("mp")).getAttribute("value"));
		}
		
		List<WebElement> elements=new ArrayList<WebElement>();
		
		//iterate to read every elements
		if(page!=0)
			for(int i=1;i<=page;i++){
				unit.get("http://weibo.cn/repost/"+weiboID+"&page="+i);
				elements.addAll(unit.findElements(By.className("c")));
			}
		else{
			elements=unit.findElements(By.className("c"));
		}
		
		//filter process
		for(WebElement e:elements){
			if(e.findElement(By.className("cc"))==null){
				elements.remove(e);
				continue;
			}
		}		
		return elements;
	}
	
	/**
	 * @return driver
	 */
	public HtmlUnitDriver getDriver(){
		return this.unit;
	}
	
	/**
	 * Close the driver
	 */
	public void close(){
		login=null;
		unit.quit();
		unit.close();
	}
	
	/**
	 * Test
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		WeiboMetaFeaturesCrawler crawler=new WeiboMetaFeaturesCrawler();
		List<WebElement> list=crawler.extractFavorite("BD59HgdVY");
		System.err.println(list.get(0));
		System.err.println(list.size());
	}
}