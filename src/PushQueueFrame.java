import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class PushQueueFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PushQueueFrame frame = new PushQueueFrame();
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
    
    JTable table = new JTable();
    PushQueueManager manager;
    JTextField urlField = new JTextField();
    JTextField portField = new JTextField();
    
    public PushQueueFrame() {
        setTitle("Push Queue");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 781, 468);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new MigLayout(""));
        
        contentPane.add(new JLabel("Server info:"),"wrap, span");
        
        contentPane.add(new JLabel("URL: "),"split 2");
        contentPane.add(urlField,"growx, wrap");
        
        contentPane.add(new JLabel("Port: "), "split 2");
        contentPane.add(portField,"growx, wrap");
        
        
        JButton button_3 = new JButton("Accept");
        
        contentPane.add(button_3, "span, wrap");
        
        contentPane.add(new JSeparator(),"wrap, span, growx");
        
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setEnabled(false);
        contentPane.add(scrollPane,"span 2,wrap, growx, pushx");
        
        final JButton button = new JButton("Refresh table");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.refresh();
            }
        });
        button.setEnabled(false);
        contentPane.add(button, "span, split");
        final JButton button_1 = new JButton("Remove selected");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] indexes = table.getSelectedRows();
                long[] ids = new long[indexes.length];
                for (int i =0 ; i < indexes.length; i++)
                {
                    ids[i] = manager.getId(indexes[i]);
                }
                
                for (long id : ids)
                {
                    manager.removeElement(id);
                }
            }
        });
        button_1.setEnabled(false);
        contentPane.add(button_1,"");
        final JButton button_2 = new JButton("Add new request");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dia = new PushQueueAddDialog(manager, PushQueueFrame.this);
                dia.setVisible(true);
            }
        });
        button_2.setEnabled(false);
        contentPane.add(button_2, "wrap");
        
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                manager = new PushQueueManager(urlField.getText(), Integer.parseInt(portField.getText()));
                scrollPane.setEnabled(true);
                button.setEnabled(true);
                button_1.setEnabled(true);
                button_2.setEnabled(true);
                
                manager.refresh();
                
                table.setModel(manager.getModelOf());
                
            }
        });
    }

}
