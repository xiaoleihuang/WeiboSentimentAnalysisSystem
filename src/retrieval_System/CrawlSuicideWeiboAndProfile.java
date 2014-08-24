package retrieval_System;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import retrieval_extractor.ReadUidFile;
import retrieval_receiver.WeiboCrawler;
import retrieval_writer.WeiboWriter;

/**
 * Test crawler, The data will be stored in "WeiboPosts.txt", the sample has been stored there,
 * you could check them first
 * @author xiaolei
 */
public class CrawlSuicideWeiboAndProfile {
	private String name="";
	private String pwd="";
	public static List<String> uids;
	private WeiboCrawler crawler;
	static{
		ReadUidFile reader=new ReadUidFile();
		uids=reader.readUid();
		reader.close();
	}
	/**
	 * @param u name
	 * @param p pwd
	 * @throws IOException 
	 */
	public CrawlSuicideWeiboAndProfile(String u, String p) throws IOException{
		name=u;
		pwd=p;
		int count=0;
		for(int i=0;i<uids.size();i++){
			crawler=new WeiboCrawler(uids.get(i), name, pwd, 200);
			count=+crawler.getPostsList().size();
			crawler.close();
//			WeiboWriter.WritePosts2File(uids.get(i), crawler.getPostsList(), "");//write file under the project
			WeiboWriter.Write2ExcelFile(uids.get(i), crawler.getPostsList(), "./resource/posts/");
//			GetUserProfile profile=new GetUserProfile();
//			GetUserTags getTags=new GetUserTags();
//			WeiboWriter.WriteUserProfile2MySQL(profile.getUserById(uids.get(i)), getTags.getTags(uids.get(i)));
			if(count>950){
				try {
					Thread.sleep(3600000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub		
		//args[0] is login account's name, args[1] is the password
		BufferedReader reader=new BufferedReader(new FileReader("./account.conf"));
		String username=reader.readLine();
		String password=reader.readLine();
		reader.close();
		new CrawlSuicideWeiboAndProfile(username,password);
	}
}