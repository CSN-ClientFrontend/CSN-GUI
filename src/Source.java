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


interface SerialListener
{
    void onSerial(int[] serials);
}

public class Source implements AutoCloseable, StatusListener {

    
   
    
    BlockingQueue<Request> requestList = new LinkedBlockingQueue<>();
    
    String name;
    String url;
    int port;
    int serial;
    
    Thread myThread;
    SourceThread mySourceThread;
    
    public Source(String name, String url, int port, int serial) {
        mySourceThread = new SourceThread(url, port, requestList,this);
        myThread = new Thread(mySourceThread);
        myThread.start();
        
        this.name = name;
        this.url = url;
        this.port = port;
        this.serial = serial;
    }
    
    public void requestData(long startTime, long endTime, int resolution, SourceListener list)
    {
        if (!closed)
        {
        Request request = new Request();
        request.type = Request.RequestType.DataRequest;
        
        DataRequest res = new DataRequest();
        res.startTime = startTime;
        res.endTime = endTime;
        res.resolution = resolution;
        res.serial = serial;
        res.list = list;
        
        request.actualRequest = res;
        
        requestList.add(request);
        }
    }
    
    public void requestSerialNumbers(SerialListener list)
    {
        if (!closed)
        {
            throw new RuntimeException("I fail. Track me down");
            /*
        Request request = new Request();
        request.type = Request.RequestType.SerialRequest;
        
        SerialRequest res = new SerialRequest();
        res.list = list;
        
        request.actualRequest = res;
        
        requestList.add(request);
        */
        }
        
        
    }

    boolean closed = false;
    
    @Override
    public void close() throws Exception {
        if (!closed)
        {
        Request res = new Request();
        res.type = Request.RequestType.CloseSocket;
        requestList.add(res);
        System.out.println("Joining thread");
        myThread.join();
        System.out.println("Now closed");
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

    public int getSerial() {
        // TODO Auto-generated method stub
        return serial;
    }
    
 
}




class Request
{
    enum RequestType
    {
        DataRequest, SerialRequest, CloseSocket
    }
    
    RequestType type;
    
    Object actualRequest;
}


class DataRequest
{
    long startTime;
    long endTime;
    int resolution;
    int serial;
    SourceListener list;
    
}


class SerialRequest
{
    SerialListener list;
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
                
                switch(next.type)
                {
                case CloseSocket:
                    updateStatus("Quit");
                    return;
                    
                    
                case DataRequest:
                    final DataRequest dataReq = (DataRequest) next.actualRequest;
                    if (dataReq.endTime == -1)
                        dataReq.endTime = System.currentTimeMillis();
                    updateStatus("Asking for data");
                    System.out.println("Asking for data");
                    network.requestArrayDataPoints(dataReq.startTime, dataReq.endTime, dataReq.resolution, dataReq.serial, new ArrayDataPointListener() {
                        
                        @Override
                        public void onRecieve(final long[] times, final double[] values) {
                            updateStatus("Serving data to self");
                           SwingUtilities.invokeLater(new Runnable() {
                            
                            @Override
                            public void run() {
                                dataReq.list.onRecieve(times, values);
                                
                            }
                        });
                            
                        }
                    });
                    break;
                  
                case SerialRequest:
                    final SerialRequest serReq = (SerialRequest) next.actualRequest;
                    updateStatus("I fail in serial request. I really fail. Fail");
                    throw new RuntimeException("I really fail. I just wasted 10 minutes");
                    //break;
                    
                    
                  
                }
        
              
                
                
           
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
