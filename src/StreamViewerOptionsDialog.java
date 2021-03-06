import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class StreamViewerOptionsDialog extends JDialog {

    
    StreamViewerOptions options;
    
    private final JPanel contentPanel = new JPanel();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            StreamViewerOptionsDialog dialog = new StreamViewerOptionsDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public StreamViewerOptionsDialog()
    {
        
        this(null);
        
    }
    
    public StreamViewerOptionsDialog(StreamViewerOptions _options) {
        options = _options;
        setTitle("Stream Viewer Options");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout(""));
        
        
        JLabel delayLabel = new JLabel("Delay(seconds):");
        final JTextField delayInput = new JTextField(options.getDelay() + "");
        
        
        JLabel resolutionLabel = new JLabel("Resolution:");
        final JTextField resolutionField = new JTextField(options.getResolution() + "");
        
        
        contentPanel.add(delayLabel,"align label");
        contentPanel.add(delayInput,"growx, pushx, wrap");
        
        contentPanel.add(resolutionLabel,"align label");
        contentPanel.add(resolutionField,"growx, pushx, wrap");
        
        
        
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        
                        options.setDelay(Integer.parseInt(delayInput.getText()));
                        options.setResolution(Integer.parseInt(resolutionField.getText()));
                      
                        dispose();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
