package retrieval_System;

import retrieval_receiver.GetUserProfile;
import weibo4j.model.User;
/**
 * Get User's Profile information as JSon data format
 * @author xiaolei
 *
 */
public class TestUserProfile {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetUserProfile userprofile=new GetUserProfile();
		User u=userprofile.getUserByDomainName("02wmj");
		System.err.println(u.toString());
		
		System.err.println(u.getFollowersCount());
		System.err.println(u.getGender());
		System.err.println(u.getId());
//		System.err.println(u.getFavouritesCount());
//		System.err.println(u.getFriendsCount());
//		System.err.println(u.getStatusesCount());
//		System.err.println(u.getScreenName());
//		System.err.println(u.getStatusesCount());
//		System.err.println(u.getStatus());
//		System.err.println(u.getDescription());
//		System.err.println(u.getProvince());
//		System.err.println(u.getCity());
//		System.err.println(u.getName());
//		System.err.println(u.getUserDomain());
	}
}