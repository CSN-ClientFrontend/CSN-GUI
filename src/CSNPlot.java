import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CSNPlot {

    XYSeriesCollection coll = new XYSeriesCollection();

    NumberAxis range = new NumberAxis();
    DateAxis domain = new DateAxis();
    Map<String, XYSeries> sourcesToSeries = new HashMap<>();

    XYPlot p = new XYPlot(coll, domain, range, new SamplingXYLineRenderer());

    {
        p.addRangeMarker(new ValueMarker(0, Color.RED, new BasicStroke(5)));

        range.setAutoRangeIncludesZero(true);

    }

    JFreeChart chart = new JFreeChart(p);
    ChartPanel panel = new ChartPanel(chart);

    public CSNPlot(SourceManager m) {
        m.addChangeListener(new SourceManagerChangesListener() {
            
            @Override
            public void removeSource(Source b) {
               removeSourceToPlot(b);
                
            }
            
            @Override
            public void addSource(Source a) {
           
               addSourceToPlot(a);
                             
                
            }
        });
        
    }
    
 
    
    public void addSourceToPlot(Source s) {
        XYSeries series = new XYSeries(s.getName());
        if (isInFixed)
            series.setMaximumItemCount(20000 * 4);
        coll.addSeries(series);
        sourcesToSeries.put(s.getName(), series);

    }

    public void removeSourceToPlot(Source s) {
        XYSeries series = sourcesToSeries.get(s.getName());

        coll.removeSeries(series);
        sourcesToSeries.remove(s.getName());
    }

    public JPanel getPanel() {
        // TODO Auto-generated method stub
        return panel;
    }

    boolean isInFixed = false;

    public void setFixedSize() {
        isInFixed = true;
        domain.setFixedAutoRange(10000);
    }

    public XYSeries getSeries(Source a) {
        String name = a.getName();
        return sourcesToSeries.get(name);
    }

}
