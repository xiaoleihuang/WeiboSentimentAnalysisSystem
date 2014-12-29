package crawler.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
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
	 */
	public void extractComments(String weiboID,String uid){
		this.unit.get("http://weibo.cn/comment/"+weiboID+"?uid="+uid);
		
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
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new WeiboMetaFeaturesCrawler();
	}

}
