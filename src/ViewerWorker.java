import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class ViewerWorker implements Runnable {

    static final int bufferSize = 1;

    String url, port;
    JDesktopPane desktop;
    SourceViewerFrame viewer;

    public ViewerWorker(String url, String port, JDesktopPane desktop) {
        this.url = url;
        this.port = port;

        this.desktop = desktop;
        viewer = new SourceViewerFrame();
        desktop.add(viewer);
        viewer.setVisible(true);

    }

    long lastTime = System.currentTimeMillis() - 3000;

    @Override
    public void run() {

        try (Network b = new Network(new Socket(url, Integer.parseInt(port)))) {

            System.out.println("connected");

            while (true) {
                b.requestBufferedDataPoints(lastTime, System.currentTimeMillis(), 1, new BufferedDataPointListener() {
                    
                    @Override
                    public void onRecieve(final long[] times, final double[] values) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                viewer.addPoints(times , values);

                            }
                        });
                        
                    }
                });
              
             

                System.out.println("I have read all the fun");

                Thread.sleep(2000);

            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
