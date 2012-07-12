import hirondelle.date4j.DateTime;

import java.awt.EventQueue;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import com.toedter.calendar.JDateChooser;
import com.toedter.components.JSpinField;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

import org.jfree.data.xy.XYSeries;


public class HistoryViewer extends JInternalFrame {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HistoryViewer frame = new HistoryViewer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    HistoryViewerOptions options = new HistoryViewerOptions();
    
    SourceManager sources = new SourceManager();
    
    CSNPlot plot = new CSNPlot(sources);
    
    
    JDateChooser fromDate = new JDateChooser(new Date(DateTime.now(TimeZone.getTimeZone("GMT")).getMilliseconds(TimeZone.getDefault())));
    JDateChooser toDate = new JDateChooser(new Date(DateTime.now(TimeZone.getTimeZone("GMT")).getMilliseconds(TimeZone.getDefault())));
    
    TimePanel fromTime = new TimePanel();
    TimePanel toTime = new TimePanel();
    
  
    public void setTime(DateTime fromDateTime,DateTime toDateTime)
    {
      
        
        fromDate.setDate(new Date(fromDateTime.getMilliseconds(TimeZone.getDefault())));
        toDate.setDate(new Date(toDateTime.getMilliseconds(TimeZone.getDefault())));
       
        
        fromTime.set(fromDateTime);
        toTime.set(toDateTime);
        
        goButton.doClick();
        
    }
    
    JButton goButton;
    
    public HistoryViewer() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 513, 500);
        setTitle("History Viewer");
        setClosable(true);
        getContentPane().setLayout(new MigLayout(""));
        
        
        getContentPane().add(plot.getPanel(),"span, wrap");
        getContentPane().add(new JSeparator(),"span, grow, wrap");
        
        getContentPane().add(new JLabel("From:"));
        getContentPane().add(fromDate,"growx, pushx");
        
        getContentPane().add(new JSeparator(JSeparator.VERTICAL),"spany 3, growy, gap unrelated");
        
        getContentPane().add(new JLabel("To:"),"gap unrelated");
        getContentPane().add(toDate,"growx, pushx");
        
       
        goButton = new JButton("Go");
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Calendar fromCalendar = fromDate.getCalendar();
                DateTime fromDateTime = new DateTime(fromCalendar.get(Calendar.YEAR),fromCalendar.get(Calendar.MONTH)+1,fromCalendar.get(Calendar.DAY_OF_MONTH),fromTime.getHour(),fromTime.getMinute(),fromTime.getSecond(),fromTime.getMilliSecond()*1000);
                
                
                Calendar toCalendar = toDate.getCalendar();
                DateTime toDateTime = new DateTime(toCalendar.get(Calendar.YEAR),toCalendar.get(Calendar.MONTH)+1,toCalendar.get(Calendar.DAY_OF_MONTH),toTime.getHour(),toTime.getMinute(),toTime.getSecond(),toTime.getMilliSecond()*1000);
             
                long fromMilliseconds = fromDateTime.getMilliseconds(TimeZone.getTimeZone("GMT"));
                long toMilliseconds = toDateTime.getMilliseconds(TimeZone.getTimeZone("GMT"));
               
                
                sources.requestData(fromMilliseconds, toMilliseconds, options.getResolution(), new SourceManagerListener() {
                    
                    @Override
                    public void onRecieve(long[] times, double[] values, Source source) {
                       
                       
                       XYSeries series = plot.getSeries(source);                
                       series.clear();
                      
                       for (int i = 0; i < times.length; i+= 10)
                       {
                           series.add(times[i], values[i]);
                       }
                       
                    }
                    
                    
                });
                
                
            }
        });
        getContentPane().add(goButton,"gap unrelated, wrap");
        
        
        getContentPane().add(new JLabel("(GMT)Hours:Minutes:Seconds:Milliseconds"),"span 2");
        getContentPane().add(new JLabel("(GMT)Hours:Minutes:Seconds:Milliseconds"),"span 2, wrap, gap unrelated");
        
        getContentPane().add(fromTime,"span 2, growx");
        getContentPane().add(toTime,"span 2, growx, gap unrelated,wrap");
        
        getContentPane().add(new JSeparator(),"span, grow, wrap");
        
        
        JButton sourcesButton = new JButton("Sources");
        sourcesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new SourcesDialog(sources).setVisible(true);
            }
        });
        getContentPane().add(sourcesButton);
       
        
        JButton optionsButton = new JButton("Options");
        optionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HistoryViewerOptionsDialog(options).setVisible(true);
            }
        });
        getContentPane().add(optionsButton, "span, align right");

        
        

    }

}
