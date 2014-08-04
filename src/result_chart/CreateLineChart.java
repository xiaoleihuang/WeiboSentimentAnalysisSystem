package result_chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
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
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.test.TestDataGenerator;
import org.jCharts.types.ChartType;

public class CreateLineChart extends JFrame{
	private static final long serialVersionUID = 7900314316647197052L;
	JPanel panel;
	AxisChart axisChart;
	
	/**
	 * Constructor for create a line chart
	 * @param data XY Line Chart's data
	 * @param legendLabels the array of legends' labels
	 * @param LineStrokes the array of Strokes types. LineChartProperties.DEFAULT_LINE_STROKE
	 * @param LineShapes the array of Line points' shape types. PointChartProperties.SHAPE_TRIANGLE
	 * @param xAxisLabels the labels list on x axis
	 * @param xAxisTitle the title of x axis
	 * @param yAxisTitle the title of y axis
	 * @throws ChartDataException
	 * @throws PropertyException
	 * @throws IOException
	 */
	public CreateLineChart(double[][] data,String[] legendLabels,
			Stroke[] LineStrokes,Shape[] LineShapes,String[] xAxisLabels,
			String xAxisTitle,String yAxisTitle
			) throws ChartDataException, PropertyException,IOException{
		this.setSize(1000, 1000);
		this.panel=new JPanel(true);
		this.panel.setSize(500,500);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		ChartFont axisScaleFont = new ChartFont(new Font( "TimesRoman",Font.BOLD, 18),Color.black );
		axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
		axisProperties.getYAxisProperties().setScaleChartFont( axisScaleFont );
		ChartFont axisTitleFont = new ChartFont( new Font( "TimesRoman", Font.BOLD, 18), Color.black );
		axisProperties.getXAxisProperties().setTitleChartFont( axisTitleFont );
		axisProperties.getYAxisProperties().setTitleChartFont( axisTitleFont );
		
		LineChartProperties lineChartProperties = new LineChartProperties(LineStrokes,LineShapes);
		
		DataSeries dataSeries = new DataSeries( xAxisLabels, xAxisTitle, yAxisTitle,title );
		
		Paint[] paints= TestDataGenerator.getRandomPaints(legendLabels.length);
		AxisChartDataSet acds = new AxisChartDataSet(data, legendLabels, paints,ChartType.SCATTER_PLOT, lineChartProperties );
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
	
	public static void main(String[] args) throws ChartDataException, PropertyException, IOException {
		// TODO Auto-generated method stub
		double[][] data=TestDataGenerator.getRandomNumbers( 1, 6, 20, 100 );
		String[] legendLabels= { "Predict" };
		String[] xAxisLabels= { "100", "200", "300", "400", "500", "600"};
		String xAxisTitle= "Topics";
		String yAxisTitle= "Coefficience";
		Stroke[] strokes= { LineChartProperties.DEFAULT_LINE_STROKE };
		Shape[] shapes= { PointChartProperties.SHAPE_TRIANGLE };
		
		new CreateLineChart(data, legendLabels, strokes, shapes, xAxisLabels, xAxisTitle, yAxisTitle);
	}
}