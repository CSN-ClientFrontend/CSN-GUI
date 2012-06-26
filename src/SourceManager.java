import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;



interface SourceManagerChangesListener
{
    void addSource(Source a);
    void removeSource(Source b);
}


interface SourceManagerListener
{
    void onRecieve(long[] times, double[] values, Source source);
}

public class SourceManager implements StatusListener {

    
    public void addChangeListener(SourceManagerChangesListener chan)
    {
        changes.add(chan);
    }
    
    private List<SourceManagerChangesListener> changes = new ArrayList<>();
    private List<Source> sources = new ArrayList<>();
    private List<AbstractTableModel> listeners = new ArrayList<>();
    
    public void requestData(long startTime, long endTime, final SourceManagerListener list)
    {
        for (final Source source : sources)
        {
            final Source temp = source;
            source.requestData(startTime, endTime, new SourceListener() {
                
                @Override
                public void onRecieve(long[] times, double[] values) {
                    list.onRecieve(times, values, temp);
                    
                }
            });
        }
    }
    
    public void addSource(Source s)
    {
        sources.add(s);
        s.addStatusListener(this);
        for (SourceManagerChangesListener chan : changes)
            chan.addSource(s);
        
        alertTables();
    }
    
    
    public void stopSource(int index)
    {
        Source b = sources.get(index);
        try {
            b.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      
        alertTables();
    }
    
    public void removeSource(int index)
    {
        Source b = sources.remove(index);
        try {
            b.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        for (SourceManagerChangesListener chan : changes)
            chan.removeSource(b);
        alertTables();
       
    }
   
    private void alertTables()
    {
        for (AbstractTableModel listener : listeners)
            listener.fireTableDataChanged();
    }
    
    
    public TableModel getModelOf()
    {
        
        AbstractTableModel model =  new AbstractTableModel() {
            
            String columnNames[] = {"Name","Location","Status"};
            
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
               
                // TODO Auto-generated method stub
                Source s = sources.get(rowIndex);
              
                switch(columnIndex)
                {
                case 0:
                    return s.getName();
                    
                
                case 1:
                    return String.format("%s:%s", s.getUrl(),s.getPort());
                   
                case 2:
                    return s.getStatus();
                }
                
                return "This should never happen";
            }
            
            @Override
            public int getRowCount() {
                // TODO Auto-generated method stub
               
                return sources.size();
            }
            
            @Override
            public int getColumnCount() {
                // TODO Auto-generated method stub
                return 3;
            }
            
            @Override
            public String getColumnName(int column) {
                // TODO Auto-generated method stub
                return columnNames[column];
            }
        };
        
      
        listeners.add(model);
        return model;
    }

    @Override
    public void onStatusChange(String status) {
        alertTables();
        
    }
  
   
}
