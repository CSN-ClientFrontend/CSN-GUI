import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import org.apache.commons.io.IOUtils;


import com.google.gson.Gson;



interface SegmentListener
{
    public void onReceive(long startTime, long endTime, byte[] arr);
}

interface DataPointListener
{
    public void onRecieve(long time, double value);
}

interface BufferedDataPointListener
{
    public void onRecieve(long[] times, double[] values);
}


interface ArrayDataPointListener
{
    public void onRecieve(long[] times, double[] values);
}


public class Network implements AutoCloseable{

    Socket mySock;
    DataOutputStream out;
    DataInputStream in;
    Gson g = new Gson();
    
    public Network(Socket mySock) throws IOException {
        this.mySock = mySock;
       out = new DataOutputStream(mySock.getOutputStream());
       in = new DataInputStream(mySock.getInputStream());
    }
    
    
    public void requestSerials(SerialListener list) throws IOException
    {
        Protocol.Request req = new Protocol.Request();
        req.type = Protocol.Request.TypeOfRequest.RequestSerials;
        
        out.writeUTF(g.toJson(req));
        
        
        String response = in.readUTF();
       
        Protocol.RequestSerials.SerialsResponse ser = g.fromJson(response, Protocol.RequestSerials.SerialsResponse.class);
        list.onSerial(ser.serialNumbers);
        
        
        
        
    }
    
    public void requestSegment(long startTime, long endTime, int resolution, int serial, SegmentListener list) throws IOException
    {
        Protocol.Request req = new Protocol.Request();
        req.type = Protocol.Request.TypeOfRequest.RequestData;
        
        out.writeUTF(g.toJson(req));
        
        
        Protocol.RequestData.RequestMessageParameters mes = new Protocol.RequestData.RequestMessageParameters();
        
        mes.startTime = startTime;
        mes.endTime =endTime;   
        mes.resolution = resolution;
        mes.serialNumber = serial;
        
        out.writeUTF(g.toJson(mes));

        System.out.println("Reading at " + System.currentTimeMillis());
        String s = in.readUTF();
        System.out.println(s + "; was " + s.length());

        Protocol.RequestData.ResponseMetadata res = g.fromJson(s,  Protocol.RequestData.ResponseMetadata.class);
           System.out.println("I was ok with this?");
        
        for (Protocol.RequestData.SectionMetada sect : res.sections) {
            System.out.println(sect.length);

          

            byte[] buffer = new byte[(int) sect.length];
            IOUtils.readFully(in, buffer);
            list.onReceive(sect.startTime,sect.endTime,buffer);
        }
        
    }
    
    public void requestDataPoints(long startTime,long endTime, int resolution,int serial, final DataPointListener list) throws IOException
    {
        requestSegment(startTime, endTime,resolution,serial, new SegmentListener() {
            
            @Override
            public void onReceive(long startTime, long endTime, byte[] buffer) {
                double tick =  (endTime - startTime) / ((double) buffer.length/2);
                
                
                for (int i = 0; i < buffer.length / 2; i ++) {

                    long time = (long) (tick * i + startTime);

                    double value = Network.getValue(buffer[i * 2], buffer[i * 2 + 1]);
                
                    list.onRecieve(time, value);
                   
                }

                
            }
        });
    }
    
    
    public void requestArrayDataPoints(long startTime, long endTime, int resolution, int serial, final ArrayDataPointListener list) throws IOException
    {
        requestSegment(startTime, endTime,resolution, serial,new SegmentListener() {
            
            @Override
            public void onReceive(long startTime, long endTime, byte[] buffer) {
                int numOfValues = buffer.length/2;
                
                long[] times = new long[numOfValues];
                double[] values = new double[numOfValues];
                
                double tick =  (endTime - startTime) / ((double) buffer.length/2);
                
                
                for (int i = 0; i < numOfValues; i ++) {

                    long time = (long) (tick * i + startTime);

                    double value = Network.getValue(buffer[i * 2], buffer[i * 2 + 1]);
                
                    times[i] = time;
                    values[i] = value;
                   
                }
                
                list.onRecieve(times, values);
                
            }
        });
        
    }
    
    
    private int counter;
    public void requestBufferedDataPoints(long startTime, long endTime, int resolution, int serial, final int bufferAmount, final BufferedDataPointListener list) throws IOException
    {
        counter = 0;
        final long[] times = new long[bufferAmount];
        final double[] values = new double[bufferAmount];
        
        requestDataPoints(startTime, endTime,resolution, serial, new DataPointListener() {
            
            @Override
            public void onRecieve(long time, double value) {
                times[counter] = time;
                values[counter] = value;
                
                counter++;
                
                if (counter == bufferAmount)
                {
                    list.onRecieve(times.clone(), values.clone());
                    counter = 0;
                }
                
            }
        });
        
        if (counter > 0)
        {
            long[] remainingTimes = new long[counter];
            double[] remainingValues = new double[counter];
            for (int i = 0; i < counter; i++)
            {
                remainingTimes[i] = times[i];
                remainingValues[i] = values[i];
                list.onRecieve(remainingTimes, remainingValues);
            }
        }
     
        
    }


    
    public static double getValue(byte first, byte second)
    {
        int a = (int) (first & 0xFF);
        int c = (int) (second & 0xff);

        int temp = (a << 8) + c;
        double denom = 1 << 16;
        double value = temp / denom;
        value -= .5;
        value *= 5;
        
        return value;
    }
    
    @Override
    public void close() throws Exception {
        
        out.close();
        in.close();
        mySock.close();
        
    }
}
