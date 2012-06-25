import java.awt.EventQueue;

import javax.swing.JInternalFrame;


public class StreamViewer extends JInternalFrame {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StreamViewer frame = new StreamViewer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public StreamViewer() {
        setBounds(100, 100, 450, 300);

    }

}
