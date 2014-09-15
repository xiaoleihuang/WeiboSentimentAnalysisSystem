package model_keyword;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * Using Sensitive Key Words to detect whether they contains suicide posts.
 * @author xiaolei
 * @version 1.0
 */
public class KeyWordMatcher {
	/**
	 * Return suicide keywords
	 * @return suicide keywords
	 * @throws IOException
	 */
	public static HashSet<String> getKeyWords(String path) throws IOException{
		HashSet<String> keywords=new HashSet<String>();
		BufferedReader reader=new BufferedReader(new FileReader(path));
		String line;
		while((line=reader.readLine())!=null){
			keywords.add(line);
		}
		reader.close();
		return keywords;
	}
	
	/**
	 * Find the sentence whether contains keywords or not
	 * @param str input sentence
	 * @param keywords suicide keywords
	 * @return true for contains, false for not
	 */
	public static boolean match(String str,HashSet<String> keywords){
		for(String k:keywords){
			if(str.contains(k)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Test
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File dir=new File("/home/xiaolei/Documents/Web Mining/project补充数据/Weibo");
		File[] files=dir.listFiles();
		HashSet<String> keys=KeyWordMatcher.getKeyWords("./keywords.txt");
		List<String> list=new ArrayList<String>();
		for(File file:files){
			BufferedReader reader=new BufferedReader(new FileReader(file));
			String line=new String();
			System.err.println(file.getName());
			while((line=reader.readLine())!=null){
				for(String k:keys){
					if(line.contains(k)){
						list.add(line);
						break;
					}
				}
			}
//				System.out.println(line);
			
			reader.close();
		}
		System.out.println(list.size());
		
		BufferedWriter writer=new BufferedWriter(new FileWriter("./1.txt"));
		for(String s:list){
			writer.append(s+"\n");
		}
		writer.flush();
		writer.close();
	}
}