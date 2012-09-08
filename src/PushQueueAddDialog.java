import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.Closeable;


public class PushQueueAddDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            PushQueueAddDialog dialog = new PushQueueAddDialog(null,null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
    PushQueueManager manager;
    

    
    JTextField urlField = new JTextField(), portField = new JTextField(), timeBetweenField = new JTextField();
    
    public PushQueueAddDialog(PushQueueManager m, JFrame owner) {
        super(owner);
        manager = m;
        
        setBounds(100, 100, 450, 179);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout(""));
        
        contentPanel.add(new JLabel("Url:"));
        contentPanel.add(urlField,"wrap, growx , pushx");
        
        contentPanel.add(new JLabel("Port:"));
        contentPanel.add(portField,"wrap ,growx");
        
        contentPanel.add(new JLabel("How often(milliseconds): "));
        contentPanel.add(timeBetweenField,"wrap, growx");
        
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("Add item");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        manager.addElement(urlField.getText(), Integer.parseInt(portField.getText()), Long.parseLong(timeBetweenField.getText()));
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
