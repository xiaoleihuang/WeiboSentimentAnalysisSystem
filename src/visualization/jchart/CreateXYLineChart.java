package visualization.jchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.encoders.JPEGEncoder;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.AxisTypeProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.test.TestDataGenerator;
import org.jCharts.types.ChartType;


/**
 * Create a Line Chart
 * @author xiaolei
 */
public class CreateXYLineChart extends JFrame {
	private static final long serialVersionUID = 8470483224989800067L;
	public JPanel panel;
	public AxisChart axisChart;
	
	/**
	 * Create a close action
	 * @author xiaolei
	 */
	private class CloseActionListener extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			CreateXYLineChart.this.dispose();
		}
	}
	/**
	 * Constructor
	 * @throws ChartDataException
	 * @throws PropertyException
	 * @throws IOException
	 * @example <p>double[][] data={{0.09,0.1,0.2,0.3,0.4,0.5}};<br/>
	 *  String[] xAxisLabels= { "100", "200", "300", "400", "500", "600"};<br/>
	 *	String xAxisTitle= "Topics";<br/>
	 *	String yAxisTitle= "coefficient";<br/>
	 *	new CreateXYLineChart(xAxisLabels, xAxisTitle, yAxisTitle, data);</p>
	 */
	public CreateXYLineChart(String[] xAxisLabels,String xAxisTitle,String yAxisTitle,double[][] data) throws ChartDataException, PropertyException, IOException{
		this.setSize(1000, 1000);
		this.panel=new JPanel(true);
		this.panel.setAutoscrolls(true);
		this.panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		this.panel.setSize(500,500);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new CloseActionListener());
		
		//set the title of the line graph
		String title="Line Graph";
		
		//Start set the property
		//Legend Properties
		LegendProperties legendProperties = new LegendProperties();
		//Chart Properties
		ChartProperties chartProperties = new ChartProperties();
		chartProperties.setEdgePadding(10);
		chartProperties.setTitlePadding(20);
		chartProperties.setTitleFont(
			new ChartFont(
				new Font(null,Font.BOLD,24), Color.red
			)
		);
		//Axis Properties
		AxisProperties axisProperties = new AxisProperties( false );
		//AxisProperties
		ChartFont axisScaleFont = new ChartFont(new Font( "Georgia Negreta cursiva",Font.PLAIN, 23),Color.black );
		axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
		axisProperties.getYAxisProperties().setScaleChartFont( axisScaleFont );
		ChartFont axisTitleFont = new ChartFont( new Font( "Arial Narrow", Font.BOLD, 14 ), Color.black );
		axisProperties.getXAxisProperties().setTitleChartFont( axisTitleFont );
		axisProperties.getYAxisProperties().setTitleChartFont( axisTitleFont );
		axisProperties.getYAxisProperties().setShowTicks(AxisTypeProperties.TICKS_ONLY_WITH_LABELS);
		//adjust y axis scale
		DataAxisProperties dataAxisProperties= (DataAxisProperties) axisProperties.getYAxisProperties();
		dataAxisProperties.setRoundToNearest( -2 ); 
		
		Stroke[] strokes= { LineChartProperties.DEFAULT_LINE_STROKE};
		Shape[] shapes= { PointChartProperties.SHAPE_TRIANGLE};
		LineChartProperties lineChartProperties = new LineChartProperties(strokes,shapes);
		
		DataSeries dataSeries = new DataSeries( xAxisLabels, xAxisTitle, yAxisTitle,title );
		
		
		//Test
//		double[][] data= TestDataGenerator.getRandomNumbers( 1, 7, 0, 1 );

		String[] legendLabels= { "Line" };
		Paint[] paints= TestDataGenerator.getRandomPaints( 1 );
		AxisChartDataSet acds = new AxisChartDataSet(data, legendLabels, paints,ChartType.LINE, lineChartProperties );
		dataSeries.addIAxisPlotDataSet(acds);
		axisChart=new AxisChart(dataSeries, chartProperties, axisProperties,legendProperties, 980, 720);
		axisChart.setGraphics2D((Graphics2D) this.panel.getGraphics());
		axisChart.render();
	}
	
	/**
	 * Save Image 2 user appointed file path and name
	 * @param name File name and path, save the line chart file to local space
	 * @throws IOException 
	 * @throws PropertyException 
	 * @throws ChartDataException
	 */
	public void saveImage2File(String name) throws ChartDataException, PropertyException, IOException{
		JPEGEncoder.encode(axisChart, 1, new FileOutputStream(new File(name)));
	}
	
	/**
	 * Test
	 * @param args
	 * @throws ChartDataException
	 * @throws PropertyException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ChartDataException, PropertyException, IOException{
//		double[][] data={{0.0374,0.0765,0.0553,0.075,0.0961
//			,0.1125,0.1217,0.1444,0.1425,0.1489,0.1563,
//			0.1735,0.1924,0.2053,0.1675,0.1374}};
//		String[] xAxisLabels= { "50","100","150", "200", "250","300","350", "400", "450","500", "550"
//				,"600","650","700","750","800"};
//		String xAxisTitle= "Topics";
//		String yAxisTitle= "coefficient";
//		new CreateXYLineChart(xAxisLabels, xAxisTitle, yAxisTitle, data);
	}
}