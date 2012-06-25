import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JInternalFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.JFreeChartEntity;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class SourceViewerFrame extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SourceViewerFrame frame = new SourceViewerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	NumberAxis range = new NumberAxis();
	DateAxis domain = new DateAxis();
	XYSeries series;
	public SourceViewerFrame() {
		setClosable(true);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setBounds(100, 100, 450, 300);
		
		
		domain.setFixedAutoRange(10000);
		range.setAutoRangeIncludesZero(true);
		
		
		series = new XYSeries("x-axis");
		XYSeriesCollection coll = new XYSeriesCollection(series);
		XYPlot p = new XYPlot(coll,domain ,range ,new SamplingXYLineRenderer());
	
		p.addRangeMarker(new ValueMarker(0,Color.RED,new BasicStroke(5)));
	
		
		JFreeChart chart = new JFreeChart(p);
		ChartPanel panel = new ChartPanel(chart);
		
		add(panel);

	}
	
	public void addPoints(long[] arrX, double[] arrY)
	{
		for (int i = 0 ;i < arrX.length;i++)
		{
			series.add(arrX[i],arrY[i]);
		}
		
	
	}

	
	

}
