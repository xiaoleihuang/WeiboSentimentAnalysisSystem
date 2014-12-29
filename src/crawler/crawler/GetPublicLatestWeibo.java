package crawler.crawler;

import java.util.List;

import crawler.config.AccountConfig;
import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

/**
 * Retrieve latest 20 public WeiBo 
 * @author xiaolei
 * @version 1.0
 */
public class GetPublicLatestWeibo {
	String access_token=AccountConfig.accesstoken1;
	Timeline tm = new Timeline(access_token);
	StatusWapper status;
	
	public GetPublicLatestWeibo(){
		try {
			status=tm.getPublicTimeline();
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 20 latest "WeiBo"
	 */
	public List<Status> getLastestStatus(){
		return status.getStatuses();
	}
}