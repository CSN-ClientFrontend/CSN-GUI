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

	@Override
	public void run() {

		long lastTime = System.currentTimeMillis() -3000;
		try (Socket b = new Socket(url, Integer.parseInt(port));
				DataOutputStream out = new DataOutputStream(b.getOutputStream());
				DataInputStream in = new DataInputStream(b.getInputStream())) {

			Gson g = new Gson();

			System.out.println("connected");

			while (true) {
				final Protocol.Message mes = new Protocol.Message();
				
				mes.startTime = lastTime;		
				mes.endTime = System.currentTimeMillis();
				
				
				
				out.writeUTF(g.toJson(mes));

				String s = in.readUTF();
				System.out.println(s);

				Protocol.Response res = g.fromJson(s, Protocol.Response.class);

				for (Protocol.Section sect : res.sections) {
					System.out.println(sect.length);

					double tick = 2 * (sect.endTime - sect.startTime)
							/ ((double) sect.length);

					byte[] buffer = new byte[(int) sect.length];
					IOUtils.readFully(in, buffer);

					int counter = 0;
					long arrX[] = new long[10];
					double arrY[] = new double[10];
					for (int i = 0; i < sect.length / 2; i += 10) {

						long time = (long) (tick * i + sect.startTime);
						int a = (int) (buffer[i * 2] & 0xFF);
						int c = (int) (buffer[i * 2 + 1] & 0xff);

						int temp = (a << 8) + c;
						double denom = 1 << 16;
						double value = temp / denom;
						value -= .5;
						value *= 5;

						// System.out.println(a + " : " + c + " : " + temp +
						// " : "+ denom + " : " + value);
						arrX[counter] = time;
						arrY[counter] = value;

						final long[] tempArrX = arrX.clone();
						final double[] tempArrY = arrY.clone();
						counter++;
						if (counter == 10) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									viewer.addPoints(tempArrX, tempArrY);

								}
							});
							counter = 0;
						}

					}
					
					lastTime = sect.endTime+1;

				}

				System.out.println("I have read all the fun");
				
				Thread.sleep(200);

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
		}

	}

}
