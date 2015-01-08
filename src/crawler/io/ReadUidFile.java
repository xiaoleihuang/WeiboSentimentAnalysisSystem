package crawler.io;
import io.BasicReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Read weibo user's id from chosen file, return a list of user's id
 * @author xiaolei
 * @version 1.0
 */
public class ReadUidFile {
	String path="";
	JFileChooser chooser;
	File file=null;
	List<String> list=new ArrayList<String>();
	public ReadUidFile() {
		chooser=new JFileChooser("./resource");
		JOptionPane.showMessageDialog(null, "Choose txt file contains user id");
		chooser.setFileFilter(new FileNameExtensionFilter("Choose txt format file", "txt"));
		chooser.showOpenDialog(chooser);
		
		path=chooser.getSelectedFile().getAbsolutePath();
	}
	
	/**
	 * @return a list of user's id, return empty list if file is not readable or contains no data
	 * @throws Exception
	 */
	public List<String> readUid(){
		try {
			list=BasicReader.basicRead(path);
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
}