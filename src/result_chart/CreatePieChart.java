package result_chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.PieChartDataSet;
import org.jCharts.nonAxisChart.PieChart2D;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.PieChart2DProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.types.PieLabelType;

/**
 * Create a Pie Chart
 * @author xiaolei
 */
public class CreatePieChart extends JFrame{
	private static final long serialVersionUID = -7014784042194520573L;
	public JPanel panel;
	public static BufferedImage image;
	/**
	 * Constructor
	 * @param labels the labels
	 * @param data the double data
	 * @param paints Colors
	 * @throws ChartDataException
	 * @throws PropertyException
	 */
	public CreatePieChart(String[] labels,double[] data,Paint[] paints) throws ChartDataException, PropertyException{
		this.setSize(500, 500);
		this.panel=new JPanel(true);
		this.panel.setSize(500,500);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
		
		String title="Pie Graph";
		PieChart2DProperties pieChart2DProperties=new PieChart2DProperties();
		pieChart2DProperties.setPieLabelType(PieLabelType.VALUE_LABELS);
		pieChart2DProperties.setValueLabelFont(
			new ChartFont(
				new Font(null,Font.BOLD,14), Color.red
			)
		);
		
		
		PieChartDataSet pieChartDataSet=new PieChartDataSet( title, data, labels, paints, pieChart2DProperties );
		ChartProperties chartProper=new ChartProperties();
		chartProper.setTitlePadding(20);
		chartProper.setTitleFont(new ChartFont(new Font(null,Font.BOLD,20), Color.black));
		LegendProperties legendProper=new LegendProperties();
		legendProper.setBackgroundPaint(Color.white);
		legendProper.setChartPadding(30);
		legendProper.setRowPadding(10);
		legendProper.setColumnPadding(10);
		
		PieChart2D pieChart2D=new PieChart2D( pieChartDataSet,legendProper,chartProper, 450, 450 );
		
		
		pieChart2D.setGraphics2D( (Graphics2D) this.panel.getGraphics());
		image=pieChart2D.getBufferedImage();
		pieChart2D.render();	
	}
	
	/**
	 * Save image 2 file, the default type is "jpg", and the default place is the current directory
	 * @throws IOException 
	 */
	public void SaveImage2File(String format,String savePath) throws IOException{
		if(savePath==null)
			savePath="";
		if(format==null)
			format="jpg";
		ImageIO.write(image, format, new File(savePath));
	}
	/**
	 * Test 
	 * @param args
	 * @throws ChartDataException
	 * @throws PropertyException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ChartDataException, PropertyException, IOException {
		// TODO Auto-generated method stub
		String[] labels={"Suicide Posts", "Not Suicide Posts"};
		Paint[] paints={Color.blue, Color.gray};
		double[] data={500d, 5000d};
		new CreatePieChart(labels, data, paints);
	}
}