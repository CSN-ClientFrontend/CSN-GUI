import hirondelle.date4j.DateTime;
import hirondelle.date4j.DateTime.DayOverflow;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class RssViewer extends JInternalFrame {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                   
                           
                    

                    
                    RssViewer frame = new RssViewer();
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
    
    RssTable table = new RssTable();
    
    
    public RssViewer()
    {
        this(null);
    }
    
    HistoryViewerProvider providor;
    
    public RssViewer(HistoryViewerProvider prov) {
        providor = prov;
        setTitle("Rss Viewer");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new MigLayout());
        
        
        
        
        
        getContentPane().add(new JLabel("Source:"),"split 2");
        sourceChooser = new JComboBox<String>();
        for (String value : sourceDescriptionToUrl.keySet())
            sourceChooser.addItem(value);
        getContentPane().add(sourceChooser,"");
        
        JButton button = new JButton("Request info");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                table.getDataFrom(sourceDescriptionToUrl.get(sourceChooser.getSelectedItem()));
            }
        });
        getContentPane().add(button,"align right, wrap");
     
        final JTable tableWidget =new JTable(table.getTableModel());
        tableWidget.setAutoCreateRowSorter(true);
        
        getContentPane().add(new JScrollPane(tableWidget),"span 2,wrap");
        
        JButton button_1 = new JButton("Set History Viewer to current selection's time");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tableWidget.getSelectedRow();
                DateTime time = table.getRowData(row);
                HistoryViewer viewer = providor.getHistoryViewer();
                viewer.setTime(time, time.plus(0, 0, 0, 0, 0, 10, DayOverflow.Spillover));
                
            }
        });
        getContentPane().add(button_1,"span 2");

    }
    
    Map<String,URL> sourceDescriptionToUrl = new TreeMap<String,URL>(){
        {
            try {
            put("1 hour 0+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs1hour-M0.txt"));
            put("1 hour 1+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs1hour-M1.txt"));
            put("1 day 0+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs1day-M0.txt"));
            put("1 day 1+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs1day-M1.txt"));
            put("1 day 2.5+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs1day-M2.5.txt"));
            put("1 week 2.5+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M2.5.txt"));
            put("1 week 5+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.txt"));
            put("1 week 7+ magnitude",new URL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M7.txt"));
            }
            catch(Exception e)
            {
                
                throw new RuntimeException(e);
            }
            
        }};
    JComboBox<String> sourceChooser;

}
