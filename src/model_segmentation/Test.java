package model_segmentation;
import java.io.*;
public class Test {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader reader=new BufferedReader(new FileReader("./resource/Segmentedall.txt"));
		String line;
		
		while((line=reader.readLine())!=null){
			String infos[]=line.split("\t");
			if(infos[1].trim().length()<1){
				System.out.println(line);
			}
		}
		
		
		reader.close();
	}

}
