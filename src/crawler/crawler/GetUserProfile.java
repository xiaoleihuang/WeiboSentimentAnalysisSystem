package crawler.crawler;

import crawler.config.AccountConfig;
import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

/**
 * By official API, retrieve User's public profile
 * @author xiaolei
 * @since Java 1.7
 * @version 1.0
 */
public class GetUserProfile {
	Users um;
	public GetUserProfile(){
		String access_token = AccountConfig.accesstoken1;
		um = new Users(access_token);
	}
	
	/**
	 * @param uid user's id, it should only be numbers
	 * @return Basic user's profile, returns null if exception,like this method needs id, but your input is his name
	 */
	public User getUserById(String uid){
		try {
			return um.showUserById(uid);
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param screenName the name display on its account, like "广告设计精选", it could be any words
	 * @return Basic user's profile, returns null if exception,like this method needs name, but your input is his id
	 */
	public User getUserByName(String screenName){
		try{
			return um.showUserByScreenName(screenName);
		}catch(WeiboException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param domainName user's domain name
	 * @return Basic user's profile, returns null if exception,like this method needs domain name, but your input is his id
	 */
	public User getUserByDomainName(String domainName){
		try{
			return um.showUserByDomain(domainName);
		}catch(WeiboException e){
			e.printStackTrace();
			return null;
		}
	}
}