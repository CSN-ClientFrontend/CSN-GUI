import hirondelle.date4j.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JPanel;

import com.toedter.components.JSpinField;

import net.miginfocom.swing.MigLayout;


public class TimePanel extends JPanel {

    /**
     * Create the panel.
     */
    
    
    JSpinField hourField, minuteField, secondField, millisecondField;
    
    public TimePanel() {
        setLayout(new MigLayout("insets 0", "[grow]", "[]"));
        
        hourField = new JSpinField(0, 23);
        minuteField =  new JSpinField(0, 59);
        secondField = new JSpinField(0, 59);
        millisecondField = new JSpinField(0, 999);
        
        
        DateTime d = DateTime.now(TimeZone.getTimeZone("GMT"));
     
        
        set(d);
        
        
        add(hourField,"span 2, split 4,growx 20");
        add(minuteField,"growx 20");
        add(secondField,"growx 20");
        add(millisecondField,"growx 40");
    }
    
    
    public void set(DateTime t)
    {
        hourField.setValue(t.getHour());
        minuteField.setValue(t.getMinute());
        secondField.setValue(t.getSecond());
        millisecondField.setValue(t.getNanoseconds()/1000);
    }
    
    public int getHour()
    {
        return hourField.getValue();
    }
    
    public int getMinute()
    {
        return minuteField.getValue();
       
    }
    
    public int getSecond()
    {
        return secondField.getValue();

    }
    
    
    public int getMilliSecond()
    {
        return millisecondField.getValue();
        
    }

}
