package retrieval_extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetAllWeiboPosts {
	List<OneWeibo> list=new ArrayList<OneWeibo>();
	public GetAllWeiboPosts(String p){
		try {
			BufferedReader reader=new BufferedReader(new FileReader(p));
			String line;
			while((line=reader.readLine())!=null){
				System.out.println(line);
				String[] infos=line.split("\t");
				String pid=infos[0];
				String content=infos[2];
				String date=infos[1];
				
				list.add(new OneWeibo(content, date, pid));
			}
			System.out.println(list.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<OneWeibo> getList() {
		// TODO Auto-generated method stub
		return this.list;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/all.txt");
	}
}