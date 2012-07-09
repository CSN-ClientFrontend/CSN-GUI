import java.awt.EventQueue;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;


public class SourcesDialog extends JFrame {
    private JTable table;

    /**
     * Launch the application.
     */
    
    SourceManager m;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SourcesDialog dialog = new SourcesDialog();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the dialog.
     */
    JTextField nameField = new JTextField();
    JTextField urlField = new JTextField();
    JTextField portField = new JTextField();
    
    
    public SourcesDialog() {
       this(new SourceManager());
    }
    
    
    public SourcesDialog(SourceManager sources) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Sources");
        m = sources;
        
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new MigLayout(""));    
   
        
        table = new JTable(m.getModelOf());
        
        getContentPane().add(new JScrollPane(table),"span 2, wrap");
        
        JButton stopCurrentSelection = new JButton("Stop current selection");
        getContentPane().add(stopCurrentSelection);
        stopCurrentSelection.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
              
               m.stopSource(table.getSelectedRow());
                
            }
        });
        
        
        JButton removeCurrentSelection = new JButton("Remove current selection");
        getContentPane().add(removeCurrentSelection,"wrap");
        removeCurrentSelection.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
               
               m.removeSource(table.getSelectedRow());
                
            }
        });
        
        
        JLabel doneYet = new JLabel();
        doneYet.setIcon(new ImageIcon(SourcesDialog.class.getResource("/com/sun/java/swing/plaf/windows/icons/Warn.gif")));
    
       
        getContentPane().add(new JSeparator(),"gapy unrel, growx, span 2, wrap");
        
        getContentPane().add(new JLabel("Name"), "gapy unrel, align label");
        getContentPane().add(nameField,"growx, wrap");
        
        getContentPane().add(new JLabel("Url"), "align label");
        getContentPane().add(urlField,"growx, wrap");
        
        getContentPane().add(new JLabel("Port"), "align label");
        getContentPane().add(portField,"growx , wrap");
      
        
        
        JButton button = new JButton("Request Serial Numbers");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("Please work");
                Runnable b = new Runnable() {
                    
                   
                    @Override
                    public void run() {
                        try {
                            Network b = new Network(new Socket(urlField.getText(),Integer.parseInt(portField.getText())));
                            b.requestSerials(new SerialListener() {
                                
                                @Override
                                public void onSerial(final int[] serials) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        
                                        @Override
                                        public void run() {
                                            for (int number : serials){
                                                serialBox.addItem(number);
                                                serialBox.setEnabled(true);
                                                
                                                serialLabel.setEnabled(true);
                                                
                                                
                                                addSourceButton.setEnabled(true);
                                            }
                                                
                                            
                                        }
                                    });
                                    
                                }
                            });
                        } catch (NumberFormatException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                    }
                };
                
                Thread d = new Thread(b);
                d.start();
              
                
            }
        });
        getContentPane().add(button,"span, split 2, align right,wrap");
        //getContentPane().add(doneYet, "wrap");
       
        
        serialLabel =new JLabel("Serial Number");
        serialLabel.setEnabled(false);
        getContentPane().add(serialLabel, "align label");
        
        
        serialBox = new JComboBox<Integer>();
        serialBox.setEnabled(false);
        serialBox.setEditable(false);
      
        
       
        
        getContentPane().add(serialBox,"growx, wrap");
        
        
        
        
        addSourceButton = new JButton("Add source");
        addSourceButton.setEnabled(false);
        addSourceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                
                m.addSourceToPlot(new Source(nameField.getText(),urlField.getText(),Integer.parseInt(portField.getText()),(int) serialBox.getSelectedItem()));
            }
        });
        
        addSourceButton.setEnabled(false);
        getContentPane().add(addSourceButton);
        
        
       
        
        
        
        

    }
    
    JButton addSourceButton ;
    JLabel serialLabel;
    JComboBox<Integer> serialBox;

}
