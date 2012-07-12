import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.IOUtils;


class RssTableRow
{
    String date;
    String time;
    String magnitude;
    String location;
    DateTime actualTime;
}




public class RssTable {


    Map<String,Integer> monthsOfTheYear = new HashMap<String, Integer>(){{
       put("January",1); 
       put("February",2); 
       put("March",3); 
       put("April",4); 
       put("May",5); 
       put("June",6); 
       put("July",7); 
       put("August",8); 
       put("September",9); 
       put("October",10); 
       put("November",11); 
       put("December",12); 
 
    }};
    
    List<RssTableRow> rows = new ArrayList<>();
    
    
    public DateTime getRowData(int row)
    {
        return rows.get(row).actualTime;
    }
    
    public void getDataFrom(URL source)
    {
        rows.clear();
        
       
        
        try {
            InputStream input = source.openStream();
            List<String> lines = IOUtils.readLines(input);
            
            for (int i = 1 ; i < lines.size(); i++)
            {
                String line= lines.get(i);
                
                RssTableRow row = new RssTableRow();
                
                String[] columns = line.split(",");
                //System.out.println(Arrays.toString(columns));
            
        
                row.magnitude = columns[8];
                row.location = columns[11];
                
                
                String fullDate = (columns[4] + columns[5]).trim();
              //  System.out.print(fullDate);
                
                Pattern p = Pattern.compile("(\\w+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d{2}):(\\d{2}):(\\d{2})\\s*UTC\"");
                Matcher result = p.matcher(fullDate);
                result.matches();
              
                DateTime date = new DateTime(Integer.parseInt(result.group(3)),monthsOfTheYear.get(result.group(1)),Integer.parseInt(result.group(2)),Integer.parseInt(result.group(4)),Integer.parseInt(result.group(5)),Integer.parseInt(result.group(6)),0);
               System.out.println(date);
                
                row.date = date.format("MMM D",Locale.getDefault());
                row.time = date.format("h:mm:ss",Locale.getDefault());
                row.actualTime = date;
                rows.add(row);
                
                
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        myModel.fireTableDataChanged();
    }
    
    AbstractTableModel myModel = new AbstractTableModel() {

        
        String[] columns = {"Date", "Time", "Magnitude", "Location"};
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            // TODO Auto-generated method stub
            RssTableRow row = rows.get(rowIndex);
            
            switch(columnIndex)
            {
            case 0:
                return row.date;
                
            case 1:
                return row.time;
                
            case 2:
                return row.magnitude;
                
            case 3:
                return row.location;
            }
            
            return null;
        }
        
        @Override
        public int getRowCount() {
            // TODO Auto-generated method stub
            return rows.size();
        }
        
        @Override
        public int getColumnCount() {
            // TODO Auto-generated method stub
            return columns.length;
        }
        
        @Override
        public String getColumnName(int column) {
            // TODO Auto-generated method stub
            return columns[column];
        }
    };
    
    public TableModel getTableModel()
    {
        return myModel;
    }
}
