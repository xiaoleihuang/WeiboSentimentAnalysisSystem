package crawler.system;

import java.io.IOException;

import crawler.crawler.GetUserProfile;
import weibo4j.model.User;
/**
 * Get User's Profile information as JSon data format
 * @author xiaolei
 */
public class TestUserProfile {
	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		GetUserProfile userprofile=new GetUserProfile();
		
		User u = userprofile.getUserByDomainName("sixvivian");
//		User u = userprofile.getUserById("1836203000");
		System.err.println(u.toString());
	}
}