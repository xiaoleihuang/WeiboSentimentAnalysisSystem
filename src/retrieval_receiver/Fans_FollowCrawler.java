package retrieval_receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * use simulating login to retrieve personal fans and followers
 * @author xiaolei
 *
 */
public class Fans_FollowCrawler {
	String uid="";
	Login login;
	final String url="http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=";
	WebDriver driver;
	List<String> funs=new ArrayList<String>();
	List<String> followers=new ArrayList<String>();
	
	/**
	 * Constructor
	 * @param u uid
	 * @param username login user name
	 * @param password login password
	 */
	public Fans_FollowCrawler(String u,String username,String password){
			this.uid=u;
			driver=new HtmlUnitDriver(BrowserVersion.CHROME);
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

			driver.get("http://weibo.cn/"+uid);
			System.err.println(driver.getCurrentUrl());
			System.err.println("Current Crawling: "+uid+"'s Fas & Followers");
			
			//Crawl user's followers
			driver.findElement(By.className("tip2")).findElements(By.tagName("a")).get(0).click();
			System.err.println(driver.getCurrentUrl());
			int page=Integer.parseInt(driver.findElement(By.xpath("//*[@id='pagelist']/form/div/input[1]")).getAttribute("value"));
			
			for(int i=0;i<page;i++){
				List<WebElement> elements=driver.findElements(By.tagName("table"));
				for(WebElement e:elements){
					String name=e.findElement(By.tagName("a")).getAttribute("href").split("weibo.cn/")[1];
					if(name.contains("u/")){
						name=name.substring(2);
					}
//					System.err.println(name);
					followers.add(name);
				}
			}
			
			//Crawl user's fans
			driver.findElement(By.xpath("/html/body/div[2]/div[2]/a[2]")).click();
			page=Integer.parseInt(driver.findElement(By.xpath("//*[@id='pagelist']/form/div/input[1]")).getAttribute("value"));
			for(int i=0;i<page;i++){
				List<WebElement> elements=driver.findElements(By.tagName("table"));
				for(WebElement e:elements){
					String name=e.findElement(By.tagName("a")).getAttribute("href").split("weibo.cn/")[1];
					
					if(name.contains("u/")){
						name=name.substring(2);
					}
					if(name.contains("?st")){
						name=name.split("?st")[0];
					}
					
//					System.err.println(name);
					funs.add(name);
				}
			}
	}
	
	/**
	 * get followers
	 * @return followers
	 */
	public List<String> getFollowers(){
		return this.followers;
	}
	
	/**
	 * get fans
	 * @return fans
	 */
	public List<String> getFans(){
		return this.funs;
	}
	
	/**
	 * get current uid
	 * @return uid
	 */
	public String getUid(){
		return this.uid;
	}
	
	/**
	 * close driver
	 */
	public void close(){
		driver.quit();
		driver.close();
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Fans_FollowCrawler("1775931057","610337308@qq.com","h67868377");
//	}
}