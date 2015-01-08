package crawler.system;

import java.io.IOException;

import crawler.config.AccountConfig;
import crawler.crawler.GetUserProfile;
import weibo4j.Friendships;
import weibo4j.Tags;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
/**
 * Get User's Profile information as JSon data format
 * @author xiaolei
 */
public class TestUserProfile {
	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		GetUserProfile userprofile=new GetUserProfile();
		String access_token = AccountConfig.accesstoken1;
		Friendships relations=new Friendships(access_token);
		System.out.println(relations.getFollowersIdsById("1836203000")[0]);
		
		User u = userprofile.getUserByDomainName("sixvivian");
//		User u = userprofile.getUserById("1836203000");
		System.err.println(u.toString());
	}
}