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
    
    StreamViewerOptions options = new StreamViewerOptions();
    
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
    Map<String,Long> sourceToLastData = new HashMap<>();
    
    
    boolean started = false;
    
    long lastTime = 0;
    
    public StreamViewer() {
       
       sources.addChangeListener(new SourceManagerChangesListener() {
        
        @Override
        public void removeSource(Source b) {
            XYSeries series = sourcesToSeries.get(b.getName());
            
            coll.removeSeries(series);
            sourcesToSeries.remove(b.getName());
            
        }
        
        @Override
        public void addSource(Source a) {
       
            started = true;
            XYSeries series = new XYSeries(a.getName());
            series.setMaximumItemCount(20000*4);
            coll.addSeries(series);
            sourcesToSeries.put(a.getName(), series);
            
            
            sourceToLastData.put(a.getName(), (long) 0);
            
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
        
        getContentPane().add(panel, "wrap, span 2");
        getContentPane().add(new JSeparator(), "growx, span 2,wrap");
        
        
        
        JButton button = new JButton("Sources");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new SourcesDialog(sources).setVisible(true);
            }
        });
        getContentPane().add(button);
       
        
        JButton optionsButton = new JButton("Options");
        optionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new StreamViewerOptionsDialog(options).setVisible(true);
            }
        });
        getContentPane().add(optionsButton, "align right");
        
        new Timer(2000,new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                long[] startTimes = new long[sources.sources.size()];
                long[] endTimes = new long[sources.sources.size()];
                for (int i = 0; i< sources.sources.size(); i++)
                {
                   Source c = sources.sources.get(i);
                    long lastTime = sourceToLastData.get(c.getName());
                    if (lastTime < System.currentTimeMillis() - options.getDelay()*1000)
                        lastTime = System.currentTimeMillis() - options.getDelay()*1000;
                    
                    startTimes[i] = lastTime;
                    endTimes[i] = -1;
                    
                }
                
                
               sources.requestData(startTimes, endTimes, new SourceManagerListener() {
                
                @Override
                public void onRecieve(long[] times, double[] values, Source source) {
                   String name = source.getName();
                   
                   XYSeries series = sourcesToSeries.get(name);                 
                   
                   for (int i = 0; i  < times.length; i+=options.getResolution())
                   {
                       Point p = new Point();
                       p.time = times[i];
                       p.value = values[i];
                       p.seriesToAddTo = series;
                       
                       buffer.add(p);
                   }
                   
                   
                   sourceToLastData.put(source.getName(), times[times.length -1]);
                   
                }
                
                
            });
                
            }
        }).start();
        
        new Timer(1,new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                long timeNeeded = System.currentTimeMillis() - options.getDelay()*1000;
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
