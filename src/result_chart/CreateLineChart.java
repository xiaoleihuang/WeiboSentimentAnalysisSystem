package result_chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.types.ChartType;

public class CreateLineChart extends JFrame{
	private static final long serialVersionUID = 7900314316647197052L;
	JPanel panel;
	AxisChart axisChart;
	
	public CreateLineChart() throws ChartDataException, PropertyException{
		int numberOfDataSets=1;
		Stroke[] strokes = new Stroke[ 1 ];
		for( int j = 0; j < numberOfDataSets; j++ )
		{
			strokes[ j ] = LineChartProperties.DEFAULT_LINE_STROKE;
		}
		strokes[ 0 ] = new BasicStroke( 3.0f );

		Shape[] shapes = new Shape[ numberOfDataSets ];
		for( int j = 0; j < numberOfDataSets; j++ )
		{
			shapes[ j ] = PointChartProperties.SHAPE_DIAMOND;
		}
		shapes[ 0 ] = PointChartProperties.SHAPE_CIRCLE;
		LineChartProperties lineChartProperties=new LineChartProperties(strokes, shapes);
		double[][] data= { { 280, 16, 150, 90 } };
		Paint[] paints= { Color.blue };
		String[] legendLabels= { "Topic Model Comparison" };
		AxisChartDataSet axisChartDataSet = new AxisChartDataSet( data, legendLabels, paints, ChartType.LINE, lineChartProperties );
		
		String[] axisLabels= null; //{ "1900", "1950", "2000", "2050" };
		DataSeries dataSeries = new DataSeries( axisLabels, "X-Axis Title", "Y-Axis Title", "Chart Title"  );
		dataSeries.addIAxisPlotDataSet( axisChartDataSet );


		ChartProperties chartProperties= new ChartProperties();
		AxisProperties axisProperties= new AxisProperties( false );
		axisProperties.getYAxisProperties().setShowAxisLabels( false );
		axisProperties.getXAxisProperties().setShowAxisLabels( false );

		DataAxisProperties yAxis= (DataAxisProperties) axisProperties.getYAxisProperties();
		yAxis.setUserDefinedScale( -10, 50 );
		yAxis.setNumItems( 4 );
		yAxis.setRoundToNearest( 1 );

		LegendProperties legendProperties= new LegendProperties();

		axisChart= new AxisChart( dataSeries, chartProperties, axisProperties, legendProperties, 500, 400 );
		
		this.setSize(1000, 1000);
		this.panel=new JPanel(true);
		this.panel.setSize(1000,1000);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
		
		axisChart.setGraphics2D((Graphics2D)this.panel.getGraphics());
		
		axisChart.render();
		
	}
	
	public static void main(String[] args) throws ChartDataException, PropertyException {
		// TODO Auto-generated method stub
		new CreateLineChart();
	}
}