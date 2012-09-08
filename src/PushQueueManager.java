import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;



interface AsyncNetworkFunction<T>
{
    T getData(Network net) throws IOException;
    void publishData(T info);
}

public class PushQueueManager {

    
    List<Protocol.PushQueue.PushQueueItem> listOfItems = new ArrayList<>();
  
    
    <T> void runAsyncFunction(final AsyncNetworkFunction<T> data)
    {
             Runnable b = new Runnable() {
            
            @Override
            public void run() {
                try (Network net = new Network(new Socket(url,port)))
                {
                    final T info = data.getData(net);
                    SwingUtilities.invokeLater(new Runnable() {
                        
                        @Override
                        public void run() {
                           data.publishData(info);
                            
                        }
                    });
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        };
        
        Thread t = new Thread(b);
        t.start();
        
    }
    
    void refresh()
    {
        System.out.println("Refresh requested");
        runAsyncFunction(new AsyncNetworkFunction<Protocol.PushQueue.PushQueueItem[]>() {

            @Override
            public Protocol.PushQueue.PushQueueItem[] getData(Network net) throws IOException {
                // TODO Auto-generated method stub
                return net.requestPushQueueDisplay();
            }

            @Override
            public void publishData(Protocol.PushQueue.PushQueueItem[] info) {
                listOfItems.clear();
                listOfItems.addAll(Arrays.asList(info));
                System.out.println("I have new data of size:" + info.length);
                model.fireTableDataChanged();
                
            }  
        });
        
    }
    
    void removeElement(final long idToRemove)
    {
        runAsyncFunction(new AsyncNetworkFunction<Object>() {

            @Override
            public Object getData(Network net) throws IOException{
                // TODO Auto-generated method stub
                Protocol.PushQueue.PushQueueRemoveRequest req = new Protocol.PushQueue.PushQueueRemoveRequest();
                req.id = idToRemove;
                net.requestPushQueueRemove(req);
                
                return null;
            }

            @Override
            public void publishData(Object info) {
                System.out.println("Remove completed");
                refresh();
                
            }  
        });           
    }
    
    void addElement(final String url, final int port, final long timeBetween)
    {
        runAsyncFunction(new AsyncNetworkFunction<Long>() {

            @Override
            public Long getData(Network net) throws IOException {
                // TODO Auto-generated method stub
                Protocol.PushQueue.PushQueueAddRequest req = new Protocol.PushQueue.PushQueueAddRequest();
                req.url = url;
                req.port = port;
                req.timeBetween = timeBetween;
                
                
                return net.requestPushQueueAdd(req);
            }

            @Override
            public void publishData(Long info) {
                refresh();
                
            }  
        });       
    }
    
    long getId(int row)
    {
        return listOfItems.get(row).id;
    }
    
    AbstractTableModel model = new AbstractTableModel() {
        
        String columnNames[] = {"Url","Port","Last update(UTC)","Update interval(milliseconds)", "Id"};
        
        @Override
        public Object getValueAt(int row , int column) {
            // TODO Auto-generated method stub
            Protocol.PushQueue.PushQueueItem obj = listOfItems.get(row);
            
            switch( column)
            {
            case 0:
                return obj.url;
                
            case 1:
                return obj.port;
                
            case 2:
                DateTime n = DateTime.forInstant(obj.lastTime, TimeZone.getTimeZone("UTC"));
                return n;
                
            case 3:
                return obj.timeBetween;
            
            case 4:
                return obj.id;
            }
            
            throw new RuntimeException("Invalid column number in PushQueueManager");
        }
        
        @Override
        public int getRowCount() {
            // TODO Auto-generated method stub
            return listOfItems.size();
        }
        
        @Override
        public int getColumnCount() {
            // TODO Auto-generated method stub
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int column) {
            // TODO Auto-generated method stub
            return columnNames[column];
        }
    };
    
   String url;
   int port;
    
    public PushQueueManager(String url, int port) {
         this.url = url;
         this.port = port;
    }

    public TableModel getModelOf()
    {
        return model;
    }
}
