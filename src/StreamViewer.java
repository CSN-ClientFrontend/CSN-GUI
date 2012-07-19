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
import java.util.Random;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import javax.swing.Timer;
import javax.swing.JFrame;

public class StreamViewer extends JInternalFrame {

    /**
     * Launch the application.
     */
    
    Map<String,Long> sourceToLastData = new HashMap<>();
  
     
    
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
    
   
   
    
    CSNPlot plot = new CSNPlot(sources);
    
  
    
    
    public StreamViewer() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
       
       sources.addChangeListener(new SourceManagerChangesListener() {
        
        @Override
        public void removeSource(Source b) {
        
            
        }
        
        @Override
        public void addSource(Source a) {
       
        
           sourceToLastData.put(a.getName(), (long) 0);
          
            
        }
    });
        
        setTitle("Stream Viewer");
        setClosable(true);
        setBounds(100, 100, 437, 347);
        getContentPane().setLayout(new MigLayout(""));
        
       
        plot.setFixedSize();
    
       
       
        
        getContentPane().add(plot.getPanel(), "wrap, span 2");
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
                
                
               sources.requestData(startTimes, endTimes,options.getResolution(), new SourceManagerListener() {
                
                @Override
                public void onRecieve(long[] times, double[] values, Source source) {
                   
                   XYSeries series = plot.getSeries(source);                
                   
                   for (int i = 0; i  < times.length; i++)
                   {
                       
                       series.add(times[i], values[i]);
                   }
                   
                   
                   sourceToLastData.put(source.getName(), times[times.length -1]);
                   
                }
                
                
            });
                
            }
        }).start();
        
        new Timer(1000/30,new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                plot.setRange(System.currentTimeMillis() - options.getDelay()*1000);
                
            }
        }).start();
        
        
        
        
    }

}
