package crawler.crawler;

import java.util.List;

import crawler.config.AccountConfig;
import weibo4j.Tags;
import weibo4j.model.Tag;
import weibo4j.model.WeiboException;

/**
 * get User's tags from given user's id
 * @author xiaolei
 * @version 1.0
 */
public class GetUserTags {
	private Tags tags;
	public GetUserTags(){
		String access_token=AccountConfig.accesstoken1;
		tags=new Tags(access_token);	
	}
	
	/**
	 * list of tags
	 * @param uid the user's id
	 * @return list of tags
	 */
	public List<Tag> getTags(String uid){
		try {
			return tags.getTags(uid);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetUserTags g=new GetUserTags();
		for(Tag t:g.getTags("2108922361")){
			System.err.println(t.getValue());
		}
	}
}