import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JSeparator;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import javax.swing.Timer;

public class StreamViewer extends JInternalFrame {

    /**
     * Launch the application.
     */
    
    class Point
    {
        long time;
        double value;
        XYSeries seriesToAddTo;
    }
    
    Queue<Point> buffer = new LinkedList<>();
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StreamViewer frame = new StreamViewer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    SourceManager sources = new SourceManager();
    
    XYSeriesCollection coll;
    
    NumberAxis range = new NumberAxis();
    DateAxis domain = new DateAxis();
    Map<String, XYSeries> sourcesToSeries = new HashMap<>();
    
    long lastTime = System.currentTimeMillis();
    
    public StreamViewer() {
       
       sources.addChangeListener(new SourceManagerChangesListener() {
        
        @Override
        public void removeSource(Source b) {
            XYSeries series = sourcesToSeries.get(b.getName());
            series.setMaximumItemCount(20000*4);
            coll.removeSeries(series);
            sourcesToSeries.remove(b.getName());
            
        }
        
        @Override
        public void addSource(Source a) {
            XYSeries series = new XYSeries(a.getName());
            coll.addSeries(series);
            sourcesToSeries.put(a.getName(), series);
            
        }
    });
        
        setTitle("Stream Viewer");
        setClosable(true);
        setBounds(100, 100, 437, 347);
        getContentPane().setLayout(new MigLayout(""));
        
        domain.setFixedAutoRange(10000);
        range.setAutoRangeIncludesZero(true);
        
        coll = new XYSeriesCollection();
        XYPlot p = new XYPlot(coll,domain ,range ,new SamplingXYLineRenderer());
        
        p.addRangeMarker(new ValueMarker(0,Color.RED,new BasicStroke(5)));
       
        JFreeChart chart = new JFreeChart(p);
        ChartPanel panel = new ChartPanel(chart);
        
        getContentPane().add(panel, "wrap");
        getContentPane().add(new JSeparator(), "growx, wrap");
        
        
        JButton button = new JButton("Sources");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new SourcesDialog(sources).setVisible(true);
            }
        });
        getContentPane().add(button);
        
        new Timer(2000,new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
               sources.requestData(lastTime, -1, new SourceManagerListener() {
                
                @Override
                public void onRecieve(long[] times, double[] values, Source source) {
                   String name = source.getName();
                   
                   XYSeries series = sourcesToSeries.get(name);                 
                   
                   for (int i = 0; i  < times.length; i++)
                   {
                       Point p = new Point();
                       p.time = times[i];
                       p.value = values[i];
                       p.seriesToAddTo = series;
                       
                       buffer.add(p);
                   }
                   
                   lastTime = times[times.length -1];
                   
                }
                
                
            });
                
            }
        }).start();
        
        new Timer(1,new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                long timeNeeded = System.currentTimeMillis() - 5000;
                while (true)
                {
                    
                    
                    Point p = buffer.peek();
                    
                    if (p != null && p.time < timeNeeded)
                    {
                        buffer.poll();
                        
                        p.seriesToAddTo.add(p.time,p.value);
                        
                        
                    }
                    else
                    {
                        break;
                    }
                }
                
            }
        }).start();
        
        
        
        
    }

}
