import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;
import javax.swing.Timer;

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

	
	
	
	
	class Point
	{
		long time;

		long[] arrX;
		double[] arrY;
	}
	
	Queue<Point> buffer = new LinkedList<>();
	
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

		new Timer(1,new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("update");
				long timeNeeded = System.currentTimeMillis() - 5000;
				while (true)
				{
					
					Point p = buffer.peek();
					
					if (p != null && p.time < timeNeeded)
					{
						buffer.poll();
						for (int i = 0 ;i < p.arrX.length;i++)
						{
							series.add(p.arrX[i],p.arrY[i]);
						}
					}
					else
					{
						break;
					}
				}
				
			}
		}).start();
		
	}
	
	public void addPoints(long[] arrX, double[] arrY)
	{
		Point p = new Point();
		p.time = arrX[0];
		p.arrX = arrX;
		p.arrY = arrY;
		buffer.add(p);
	
	}

	
	

}
