import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;


interface StatusListener
{
    void onStatusChange(String status);
}

interface SourceListener
{
    void onRecieve(long[] times, double[] values);
}

public class Source implements AutoCloseable, StatusListener {

    
   
    
    BlockingQueue<Request> requestList = new LinkedBlockingQueue<>();
    
    String name;
    String url;
    int port;
    
    
    Thread myThread;
    SourceThread mySourceThread;
    
    public Source(String name, String url, int port) {
        mySourceThread = new SourceThread(url, port, requestList,this);
        myThread = new Thread(mySourceThread);
        myThread.start();
        
        this.name = name;
        this.url = url;
        this.port = port;
    }
    
    public void requestData(long startTime, long endTime, SourceListener list)
    {
        if (!closed)
        {
        Request res = new Request();
        res.startTime = startTime;
        res.endTime = endTime;
        res.list = list;
        
        requestList.add(res);
        }
    }

    boolean closed = false;
    
    @Override
    public void close() throws Exception {
        if (!closed)
        {
        Request res = new Request();
        res.list = null;
        requestList.add(res);
        myThread.join();
        closed = true;
        }
        
        
    }

    
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

   
    List<StatusListener> listeners = new ArrayList<>();
    
    public void addStatusListener(StatusListener a)
    {
        listeners.add(a);
    }

    @Override
    public void onStatusChange(String newStatus) {
        for (StatusListener listener : listeners)
            listener.onStatusChange(newStatus);
        
        
        status = newStatus;
        
    }
    
    String status = "";
    public String getStatus()
    {
        return status;
    }
    
 
}



class Request
{
    long startTime;
    long endTime;
    SourceListener list;
}



class SourceThread implements Runnable
{
    BlockingQueue<Request> requestList;
    
   
    
    String url;
    int port;
    
    
    
    StatusListener listener;
    
    public SourceThread(String url, int port, BlockingQueue<Request> requestList, StatusListener listener) {
        this.requestList = requestList;
        this.url = url;
        this.port = port;
        this.listener = listener;
       
    }
    
    private void updateStatus(final String status)
    {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
               listener.onStatusChange(status);
                
            }
        });
    }
    
    @Override
    public void run() {
       
        updateStatus("Connecting");
        try (Network network = new Network(new Socket(url, port))){
           
        while (true)
        {
            updateStatus("Waiting for request");
            
                final Request next = requestList.take();
                if (next.list == null)
                {
                    updateStatus("Quit");
                    break; 
                }
                
                if (next.endTime == -1)
                    next.endTime = System.currentTimeMillis();
                    
                updateStatus("Asking for data");
                network.requestArrayDataPoints(next.startTime, next.endTime, new ArrayDataPointListener() {
                    
                    @Override
                    public void onRecieve(final long[] times, final double[] values) {
                        updateStatus("Serving data to self");
                       SwingUtilities.invokeLater(new Runnable() {
                        
                        @Override
                        public void run() {
                            next.list.onRecieve(times, values);
                            
                        }
                    });
                        
                    }
                });
                
                
           
        }
        
        } catch (InterruptedException e) {
            updateStatus("Interrupted");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            updateStatus("Unknown host");
            e.printStackTrace();
        } catch (IOException e) {
            updateStatus("IOException(?)");
            e.printStackTrace();
        } catch (Exception e) {
            updateStatus("?");
            e.printStackTrace();
        }
       
        
        
    }
}
