package image_wordcloud;
import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import retrieval_extractor.GetAllWeiboPosts;
import retrieval_extractor.OneWeibo;

/**
 * Add visualization tools for word cloud
 * @author xiaolei
 */
public class TestOpenCloud {
	
    public String[] WORDS ;
    JFrame frame = new JFrame("Word Cloud");
    protected void initUI(String[] temp) throws IOException {
    	this.WORDS=temp;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        Cloud cloud = new Cloud();
        Random random = new Random();
        for (String s : WORDS) {
            for (int i = random.nextInt(150); i > 0; i--) {
                cloud.addTag(s);
            }
        }
        for (Tag tag : cloud.tags()) {
            final JLabel label = new JLabel(tag.getName());
            label.setOpaque(false);
            label.setFont(label.getFont().deriveFont((float) tag.getWeight() * 20));
            if(tag.getName().contains("死")||tag.getName().contains("抑郁"))
            	label.setForeground(Color.red);
            panel.add(label);
        }
        
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
        
        Container c = panel.getRootPane();
        BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        c.paint(im.getGraphics());
        ImageIO.write(im, "PNG", new File("word cloud.png"));
    }
    
    public static void saveImage(JPanel panel) throws IOException{
    	Container c = panel.getRootPane();
        BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        c.paint(im.getGraphics());
        ImageIO.write(im, "PNG", new File("./resource/Pic/WorldCloud.png"));
    }
    
    public static void saveImage(JFrame frame) throws IOException{
    	Container c = frame.getContentPane();
        BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        c.paint(im.getGraphics());
        ImageIO.write(im, "PNG", new File("./resource/Pic/WorldCloud.png"));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                	GetAllWeiboPosts posts=new GetAllWeiboPosts("/home/xiaolei/Desktop/dataset/suicide/Segmentedall.txt");
                	List<OneWeibo> list=posts.getList();
                	List<String> ws=new ArrayList<String>();

                	for(OneWeibo entry:list){
                		String[] words=entry.getContent().split(" ");
                		for(String w:words){
                			w=w.trim();
                			
                			if(w!=""&&w!=null&&!w.contains(" ")&&w!="\n"&&!w.contains("我")&&!w.contains("你")&&!w.contains("了")
                					&&!w.contains("都")&&!w.contains("就")&&!w.contains("也")&&!w.contains("是")&&!w.contains("有")
                					&&!w.contains("啊")){
                			System.out.print(w);
                			ws.add(w);
                			}
                		}
                		
                		System.out.println();
                	}
                	System.out.println(ws.size());
                	
					new TestOpenCloud().initUI(ws.toArray(new String[ws.size()]));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}