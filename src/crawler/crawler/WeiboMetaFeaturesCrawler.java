package crawler.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

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
	
	private HtmlUnitDriver driver;
	private final String url="http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=";
	
	/**
	 *  it will direct driver to http://weibo.cn/META/ID.
	 * The META could be attitude, comment, or favorite
	 * Constructor
	 * @throws IOException
	 */
	public WeiboMetaFeaturesCrawler() throws IOException{
		driver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER_10);
		driver.get(url);
		System.err.println("Page's Title "+driver.getTitle());
		
		//Use name and password to login WeiBo Account
		try{
			driver.findElement(By.name("mobile")).sendKeys(username);
			driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
			driver.findElement(By.xpath("//input[@type='submit']")).click();
			System.err.println("Page's Title "+driver.getTitle());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(driver.getTitle().contains("我的首页")){
			System.err.println("login success");
		}
		else{
			JOptionPane.showMessageDialog(null, "login failed, we will try again One Time");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			try {
				driver.get(url);
				driver.findElement(By.name("mobile")).sendKeys(username);
				driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
				driver.findElement(By.xpath("//input[@type='submit']")).click();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Extract comments
	 * @param weiboID the id of weibo on the web, like M_BD3mkavDG.
	 * @param uid user's id
	 * @return 
	 */
	public List<WebElement> extractComments(String weiboID,String uid){
		this.driver.get("http://weibo.cn/comment/"+weiboID+"?&uid="+uid);
		
		//indicate the number of pages
		int page=0;
		WebElement pageElement=driver.findElement(By.id("pagelist"));
		if(pageElement!=null){
			page=Integer.parseInt(pageElement.findElement(By.name("mp")).getAttribute("value"));
		}
		
		List<WebElement> elements=new ArrayList<WebElement>();
		System.err.println(page);
		
		if(page!=0)
			for(int i=1;i<=page;i++){
				driver.get("http://weibo.cn/comment/"+weiboID+"?uid="+uid+"&page="+i);
				elements.addAll(driver.findElements(By.className("c")));
			}
		else{
			elements=driver.findElements(By.className("c"));
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
		this.driver.navigate().to("http://weibo.cn/attitude/"+weiboID);
		
		//indicate the number of pages
		int page=0;
		System.out.println(driver.getCurrentUrl());
		WebElement pageElement=driver.findElement(By.className("pa"));
		if(pageElement!=null){
			page=Integer.parseInt(pageElement.findElement(By.name("mp")).getAttribute("value"));
		}
		
		System.err.println(driver.getPageSource());
		
		List<WebElement> elements=driver.findElements(By.className("c"));;
				
		//iterate to read every elements
		if(page!=0){
			for(int i=2;i<=page;i++){
				driver.get("http://weibo.cn/attitude/"+weiboID+"?rl=1&page="+i);
				elements.addAll(driver.findElements(By.className("c")));
			}
		}
		
		//filter process
		for(WebElement e:elements){
//			if(!e.getText().contains("分钟前")){
//				elements.remove(e);
//				continue;
//			}
			System.err.println(e);
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
		this.driver.get("http://weibo.cn/repost/"+weiboID);
		
		//indicate the number of pages
		int page=0;
		WebElement pageElement=driver.findElement(By.id("pagelist"));
		if(pageElement!=null){
			page=Integer.parseInt(pageElement.findElement(By.name("mp")).getAttribute("value"));
		}
		
		List<WebElement> elements=new ArrayList<WebElement>();
		
		//iterate to read every elements
		if(page!=0)
			for(int i=1;i<=page;i++){
				driver.get("http://weibo.cn/repost/"+weiboID+"&page="+i);
				elements.addAll(driver.findElements(By.className("c")));
			}
		else{
			elements=driver.findElements(By.className("c"));
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
		return this.driver;
	}
	
	/**
	 * Close the driver
	 */
	public void close(){
		driver.quit();
		driver.close();
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