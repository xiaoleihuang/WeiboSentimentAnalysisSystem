package retrieval_writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import retrieval_extractor.OneWeibo;
import retrieval_extractor.ParseProvinceXMLFile;
import retrieval_extractor.XMLParser;
import weibo4j.model.Tag;
import weibo4j.model.User;

/**
 * Write posts to File with given path, and write user's profile to DataBase
 * @author xiaolei
 */
public class WeiboWriter {	
	/**
	 * Write Data to File, the file name is better to be named as "uid.txt"
	 * @param file path and name to be written
	 * @return true is successful, false is fail
	 */
	public static boolean WritePosts2File(String uid,List<OneWeibo> weiboList,String path){
		StringBuffer buffer=new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n");
		buffer.append("<posts "+"uid=\""+uid+"\" "+"count=\""+weiboList.size()+"\">"+"\n");
		for(OneWeibo post:weiboList){
			if(post.getType()==0){
				buffer.append("<post type=\""+post.getType()+"\">"+"\n");
				buffer.append("<content>"+post.getContent()+"</content>"+"\n");
				buffer.append("<date>"+post.getDate()+"</date>"+"\n");
				buffer.append("</post>"+"\n");
			}else{
				buffer.append("<post type=\""+post.getType()+"\">"+"\n");
				buffer.append("<content>"+post.getContent()+"</content>"+"\n");
				buffer.append("<forwardReason>"+post.getForwardReason()+"</forwardReason>"+"\n");
				buffer.append("<fromWho>"+post.getFromWho()+"</fromWho>"+"\n");
				buffer.append("<forwardChain>"+post.getForwardChain()+"</forwardChain>"+"\n");
				buffer.append("<date>"+post.getDate()+"</date>"+"\n");
				buffer.append("</post>"+"\n");
			}
		}
		buffer.append("</posts>");
		
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(path+uid+".txt"));
			writer.write(buffer.toString());
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static void WriteUserProfile2MySQL(User u,List<Tag> tags){
		ConnDB conn=new ConnDB("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306?DatabaseName=posts", "root", "root");
		StringBuffer buffer=new StringBuffer();
		for(Tag t:tags){
			buffer.append(t.getValue()+" ");
		}
		conn.executeQuery("insert into weibo.profile values("+u.getId()+","+u.getGender()+","+u.getUserDomain()+","+u.getScreenName()+","
				+getLocation(u.getProvince(),u.getCity())+","+u.getDescription()+","+u.getFollowersCount()+","+u.getFriendsCount()+","
				+buffer.toString().trim()+")");
		conn.close();
	}

	private static String getLocation(int province, int city) {
		// TODO Auto-generated method stub
		try{
			XMLParser p=new XMLParser("province","provinces.xml");
			ParseProvinceXMLFile parser=new ParseProvinceXMLFile(p.getList());
			return parser.SearchAllName(province, city);
		}catch(Exception e){
			return "null";
		}
	}
}
