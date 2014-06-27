package retrieval_extractor;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

//import javax.swing.JFileChooser;
//import javax.swing.JOptionPane;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.filechooser.FileSystemView;



import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Extract data from excel files, the files contains weibo's post information
 * @author xiaolei
 * @version 1.0
 */
public class ExcelExtractor {
	HSSFWorkbook excel;
	HSSFSheet sheet;
	String path;
	List<ExcelWeiboPost> list=new ArrayList<ExcelWeiboPost>();
	public ExcelExtractor(String p){
		this.path=p;
		try {
//			JFileChooser chooser=new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
//			JOptionPane.showMessageDialog(null, "Choose excel file contains weibo posts information");
//			chooser.setFileFilter(new FileNameExtensionFilter("Choose excel format file", "excel,xls"));
//			chooser.showOpenDialog(chooser);
			excel=new HSSFWorkbook(new FileInputStream(path));
			
//			System.out.println(excel.getSheetName(0));
//			System.out.println(excel.getSheetAt(1).getSheetName());
//			System.out.println(excel.getNumberOfSheets());
			
			int NumberOFSheet=excel.getNumberOfSheets();
			for(int i=0;i<NumberOFSheet;i++){
				//get every sheet from excel
				sheet=excel.getSheetAt(i);
				//The sheet name is the user's name 
				String uid=excel.getSheetName(i);
				System.out.println(uid);
				
				//read lines of rows
				int rows=sheet.getPhysicalNumberOfRows();
				
				//get each information's cell position
				int content=1,date=3,keyword=4,degree=5;
				
				//the first row of sheet is the names of those columns
				for(int m=1;m<rows;m++){
					HSSFRow row=sheet.getRow(m);
//					System.out.println(contents);
//					System.out.println(Double.parseDouble(row.getCell(degree).toString()));
//					if(row.getCell(degree).toString()=="1"){
//						System.out.println(String.format("%.0f",row.getCell(0).getNumericCellValue()));
//					}
					
					
					if(Double.parseDouble(row.getCell(degree).toString())>0){
						String contents=row.getCell(content).toString()+" ";
						
						if(!row.getCell(content+1).toString().contains("N/A")&&!row.getCell(content+1).toString().contains("此微博已被作者删除")){
							contents=contents+"。 "+row.getCell(content+1).toString()+" ";
						}
						if(contents.contains("转发微博"))
							contents=contents.split("转发微博")[1];
						if(contents.contains("转发微博"))
							System.out.println(contents);
						contents.replace("Repost", " ");
						
						System.out.println(contents);
						if(row.getCell(keyword)==null){
							list.add(new ExcelWeiboPost(String.format("%.0f",row.getCell(0).getNumericCellValue()), contents, row.getCell(date).toString(), "", uid,Double.parseDouble(row.getCell(degree).toString())));
						}else
							list.add(new ExcelWeiboPost(String.format("%.0f",row.getCell(0).getNumericCellValue()), contents, row.getCell(date).toString(), row.getCell(keyword).toString(), uid,Double.parseDouble(row.getCell(degree).toString())));
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<ExcelWeiboPost> getList(){
		return this.list;
	}
	
	public void Write2File(String name){
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter("./resource/"+name+".txt"));
			for(ExcelWeiboPost post:list){
				writer.append(post.getPostId()+"\t"+post.getContent()+"\t"+post.getDate()+"\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i=1;i<5;i++){
			String p="/home/xiaolei/Documents/"+i+".xls";
			System.err.println(i);
			new ExcelExtractor(p).Write2File(""+i);
		}
	}
}