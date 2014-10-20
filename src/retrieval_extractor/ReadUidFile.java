package retrieval_extractor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
/**
 * Read weibo user's id from chosen file, return a list of user's id
 * @author xiaolei
 * @version 1.0
 */
public class ReadUidFile {
	String path="";
	JFileChooser chooser;
	File file=null;
	BufferedReader reader;
	List<String> list=new ArrayList<String>();
	public ReadUidFile() {
		chooser=new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		JOptionPane.showMessageDialog(null, "Choose txt file contains user id");
		chooser.setFileFilter(new FileNameExtensionFilter("Choose txt format file", "txt"));
		chooser.showOpenDialog(chooser);
		
		path=chooser.getSelectedFile().getAbsolutePath();
		try {
			reader=new BufferedReader(new FileReader(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return a list of user's id, return empty list if file is not readable or contains no data
	 * @throws Exception
	 */
	public List<String> readUid(){
		String uid=null;
		try {
			while((uid=reader.readLine())!=null){
				if(uid!="")
					list.add(uid);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.list;
	}
	
	/**
	 * @return the file, user chose, return its path
	 */
	public String getPath(){
		return this.path;
	}
	
	public void close(){
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}