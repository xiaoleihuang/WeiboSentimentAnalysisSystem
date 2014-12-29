package crawler.crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * return a driver that has already logged in the given account
 * @author xiaolei
 */
public class Login {
	HtmlUnitDriver driver;
	final String url="http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3A%2F%2Fweibo.cn%2F&backTitle=%D0%C2%C0%CB%CE%A2%B2%A9&vt=";
	BufferedWriter errorLogger=new BufferedWriter(new FileWriter("loginError.txt",true));
	
	/**
	 * Constructor
	 * @param name account's name
	 * @param pwd account's password
	 * @throws IOException
	 */
	public Login(String name,String pwd) throws IOException{
		driver=new HtmlUnitDriver(BrowserVersion.CHROME);
		driver.get(url);
		System.err.println("Page's Title "+driver.getTitle());
		
		//Use name and password to login WeiBo Account
		try{
			driver.findElement(By.name("mobile")).sendKeys(name);
			driver.findElement(By.xpath("//input[@type='password']")).sendKeys(pwd);
			driver.findElement(By.xpath("//input[@type='submit']")).click();
			System.err.println("Page's Title "+driver.getTitle());
		}catch(Exception e){
			errorLogger.append(System.currentTimeMillis()+"\n"+e);
		}
		
		if(driver.getTitle().contains("我的首页")){
			System.err.println("login success");
		}
		else{
			JOptionPane.showMessageDialog(null, "login failed, we will try again One Time");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			try {
				driver.get(url);
				driver.findElement(By.name("mobile")).sendKeys(name);
				driver.findElement(By.xpath("//input[@type='password']")).sendKeys(pwd);
				driver.findElement(By.xpath("//input[@type='submit']")).click();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.errorLogger.append(System.currentTimeMillis()+"\n"+e);
			}
		}
		System.err.println(driver.getCurrentUrl());
	}
	
	/**
	 * return WebDriver to crawler which the account has logged in
	 * @return HtmlUnitDriver
	 */
	public HtmlUnitDriver getDriver(){
		return this.driver;
	}
	
	/**
	 * close the web driver and log out account, throws IOExceotion when writing log error has exception
	 * @throws IOException
	 */
	public void close() throws IOException{
		this.driver.quit();
		this.driver.close();
		this.errorLogger.flush();
		this.errorLogger.close();
	}
}