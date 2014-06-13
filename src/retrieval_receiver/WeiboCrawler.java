package retrieval_receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import retrieval_extractor.OneWeibo;
import retrieval_extractor.WeiboExtractor;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Using Simulating Chrome_Browser Log-in to grab users' WeiBo
 * @author xiaolei
 * @version 1.0
 */
public class WeiboCrawler {
	static Logger log=Logger.getLogger(WeiboCrawler.class.getName());
	final String url="http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=";
	String loginName="",passwd="";
	String uid="";
	HtmlUnitDriver driver;
	int page=0,countPage=0;
	List<OneWeibo> weiboList=new ArrayList<OneWeibo>();
	
	/**
	 * Constructor
	 * @param uid User's id or Name
	 * @param name your account's name, because need to login
	 * @param pwd your account's password
	 * @param limitNum limit receive the number of posts, -1 means unlimited
	 */
	public WeiboCrawler(String uid,String name,String pwd,int limitNum){
		this.loginName=name;
		this.passwd=pwd;
		driver=new HtmlUnitDriver(BrowserVersion.CHROME);
		driver.get(url);
		System.err.println("Page's Title "+driver.getTitle());
		
		//Use name and password to login WeiBo Account
		try{
			driver.findElement(By.name("mobile")).sendKeys(loginName);
			driver.findElement(By.xpath("//input[@type='password']")).sendKeys(passwd);
			driver.findElement(By.xpath("//input[@type='submit']")).click();
			System.err.println("Page's Title "+driver.getTitle());
		}catch(Exception e){
			log.error("Uid Error:"+uid,e);
		}
		
		if(driver.getTitle().contains("我的首页")){
			System.err.println("login success");
		}
		else{
			JOptionPane.showMessageDialog(null, "login failed, we will try again One Time");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			try {
				driver.get(url);
				driver.findElement(By.name("mobile")).sendKeys(loginName);
				driver.findElement(By.xpath("//input[@type='password']")).sendKeys(passwd);
				driver.findElement(By.xpath("//input[@type='submit']")).click();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("Uid Error:"+uid,e);
			}
		}
		
		//navigate to user's HomePage
		int WeiboInOnePageNum=0;
		driver.navigate().to("http://weibo.cn/"+uid);
		System.err.println("Current Crawling User's Id: "+uid);
		try{
			page=Integer.valueOf(driver.findElement(By.xpath("//*[@id='pagelist']/form/div/input[1]")).getAttribute("value"));
		}catch(Exception e){
			page=1;
			log.error("Uid Error:"+uid,e);
		}
		System.err.println("User's Weibo has "+page+" pages");
		System.setProperty(uid, String.valueOf(page));
		
		while(page>0){
			List<WebElement> elements=driver.findElements(By.className("c"));
			WeiboInOnePageNum=elements.size();
			try{
				for(int i=0;i<WeiboInOnePageNum-2;i++){//because last 2 options are not Weibo_Tweet
					this.weiboList.add(WeiboExtractor.extract(elements.get(i)));
				}
				driver.findElement(By.xpath("//*[@id='pagelist']/form/div/a[1]")).click();
				page--;countPage++;
				if(countPage>100)
					break;
				if(limitNum>=0)
					if(weiboList.size()>limitNum)
						break;
			}catch(Exception e){
				//driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
				e.printStackTrace();
				if(page<2) 
					break;
				else
					continue;
			}
		}
	}
	
	/**
	 * close the crawler system
	 * @return false if fails
	 */
	public boolean close(){
		try{
			driver.close();
			this.driver.quit();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * get all data
	 * @return data
	 */
	public List<OneWeibo> getPostsList(){
		return this.weiboList;
	}
}