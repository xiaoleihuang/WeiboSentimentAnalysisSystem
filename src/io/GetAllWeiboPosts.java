package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.OneWeibo;

/**
 * retrieve all WeiBo posts<br/>
 * The document's first line must be "id	content	date	type	suicide"
 * @author xiaolei
 */
public class GetAllWeiboPosts {
	List<OneWeibo> list=new ArrayList<OneWeibo>();
	public GetAllWeiboPosts(String p){
		try {
			BufferedReader reader=new BufferedReader(new FileReader(p));
			
			String line;
			
			while((line=reader.readLine())!=null){
//				System.out.println(line);
				String[] infos=line.split("\t");
				String pid=infos[0].trim();
				String content=infos[1].trim();
				String date=infos[2].trim();
				String PostType=infos[3].trim();
				String suicide=infos[4].trim();
				
				list.add(new OneWeibo(content, pid,PostType, date,suicide));
			}
			System.out.println("Post Size:"+list.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return posts list
	 */
	public List<OneWeibo> getList() {
		// TODO Auto-generated method stub
		return this.list;
	}
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/tempTrainData");
	}
}