import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        
        
        Date b = new Date();
        
       
        
        hourField.setValue(b.getHours());
        minuteField.setValue(b.getMinutes());
        secondField.setValue(b.getSeconds());
        hourField.setValue(0);
        
        
        add(hourField,"span 2, split 4,growx 20");
        add(minuteField,"growx 20");
        add(secondField,"growx 20");
        add(millisecondField,"growx 40");
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
