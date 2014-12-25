package crawler.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import crawler.crawler.WeiboCrawler;
import crawler.io.ReadUidFile;
import crawler.io.WeiboWriter;

/**
 * Test crawler, The data will be stored in "WeiboPosts.txt", the sample has
 * been stored there, you could check them first
 * 
 * @author xiaolei
 */
public class CrawlSuicideWeiboAndProfile {
	private String name = "";
	private String pwd = "";
	public static List<String> uids;
	private WeiboCrawler crawler;
	static {
		ReadUidFile reader = new ReadUidFile();
		uids = reader.readUid();
	}

	/**
	 * @param u
	 *            name
	 * @param p
	 *            pwd
	 * @throws IOException
	 */
	public CrawlSuicideWeiboAndProfile(String u, String p) throws IOException {
		name = u;
		pwd = p;
		int count = 0;

		for (int i = 0; i < uids.size(); i++) {
			try {
				crawler = new WeiboCrawler(uids.get(i), name, pwd, -1);
				count = +crawler.getPostsList().size();
				crawler.close();
				// WeiboWriter.WritePosts2XML(uids.get(i),
				// crawler.getPostsList(), "./resource/posts/");//write file
				// under the project
				WeiboWriter.Write2ExcelFile(uids.get(i),
						crawler.getPostsList(), "./resource/posts/");
				// WeiboWriter.Write2CSVFile(uids.get(i),
				// crawler.getPostsList(), "./resource/posts/");
				// GetUserProfile profile=new GetUserProfile();
				// GetUserTags getTags=new GetUserTags();
				// WeiboWriter.WriteUserProfile2MySQL(profile.getUserById(uids.get(i)),
				// getTags.getTags(uids.get(i)));
				if (count > 950) {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "", JOptionPane.ERROR_MESSAGE, null);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// args[0] is login account's name, args[1] is the password
		BufferedReader reader = new BufferedReader(new FileReader(
				"./resource/config/account.conf"));
		String username = reader.readLine();
		String password = reader.readLine();
		reader.close();
		new CrawlSuicideWeiboAndProfile(username, password);
	}
}