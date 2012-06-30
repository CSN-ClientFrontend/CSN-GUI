import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class SourcesDialog extends JDialog {
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
        
       
        getContentPane().add(new JSeparator(),"gapy unrel, growx, span 2, wrap");
        
        getContentPane().add(new JLabel("Name"), "gapy unrel, align label");
        getContentPane().add(nameField,"growx, wrap");
        
        getContentPane().add(new JLabel("Url"), "align label");
        getContentPane().add(urlField,"growx, wrap");
        
        getContentPane().add(new JLabel("Port"), "align label");
        getContentPane().add(portField,"growx, wrap");
        
        
        JButton addSourceButton = new JButton("Add source");
        addSourceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                
                m.addSource(new Source(nameField.getText(),urlField.getText(),Integer.parseInt(portField.getText())));
            }
        });
        
        getContentPane().add(addSourceButton);
        
        
       
        
        
        
        

    }

}
