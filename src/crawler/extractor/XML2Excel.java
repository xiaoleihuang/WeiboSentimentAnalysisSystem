package crawler.extractor;

import io.XMLParser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Element;

public class XML2Excel {
	public XML2Excel()throws IOException{
		File dir=new File("/home/xiaolei/Desktop/uid/uid tasks/untitled folder/");
		File files[]=dir.listFiles(new MyFilter());
		System.out.println(files.length);
		for(File f:files){
			String uid=f.getName().substring(0,(f.getName().length()-4));
			XMLParser parser=new XMLParser("post",f.getAbsolutePath());
			List<Element>list=parser.getList();
			
			BufferedOutputStream stream=new BufferedOutputStream(new FileOutputStream(uid+".xls"));
			HSSFWorkbook excel=new HSSFWorkbook();
			
			for(int i=0;i<list.size();i++){
				
			}
			stream.flush();
			excel.write(stream);
			stream.close();
		}
	}
	
	/**
	 * Filter temporal files ended with "~"
	 * @author xiaolei
	 *
	 */
	class MyFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String name) {
			// TODO Auto-generated method stub
			return !name.contains("~");
		}
		
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new XML2Excel();
	}
}