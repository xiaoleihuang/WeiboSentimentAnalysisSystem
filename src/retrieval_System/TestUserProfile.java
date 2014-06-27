package retrieval_System;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrieval_receiver.GetUserProfile;
import weibo4j.model.User;
/**
 * Get User's Profile information as JSon data format
 * @author xiaolei
 */
public class TestUserProfile {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader;
		String line;
		User u = null;
		List<String> list=new ArrayList<String>();
		List<String> uids=new ArrayList<String>();
		reader=new BufferedReader(new FileReader("/home/xiaolei/Desktop/uids.txt"));
		while((line=reader.readLine())!=null){
			uids.add(line);
		}
		reader.close();
		
		reader=new BufferedReader(new FileReader("/home/xiaolei/Desktop/test.txt"));
		while((line=reader.readLine())!=null){
			GetUserProfile userprofile=new GetUserProfile();
//			System.err.println(line);
			u=userprofile.getUserByName(line);
//			System.err.println(u.getId());
			list.add(u.getId());
//			if(!uids.contains(u.getId())){
//				System.err.println(u.getId());
//				System.err.println(line);
//			}
			
			
		}
		
//		for(String id:uids){
//			if(!list.contains(id)){
//				System.err.println(id);
//			}
//		}
		
		for(String i:list){
			System.err.println(i);
		}
		reader.close();
//		System.err.println(u.toString());
//		
//		System.err.println(u.getFollowersCount());
//		System.err.println(u.getGender());
		
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