import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Menu;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import javax.swing.JList;
import javax.swing.JDesktopPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.SystemColor;
import javax.swing.JInternalFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.toedter.calendar.JDateChooser;


interface HistoryViewerProvider
{
    HistoryViewer getHistoryViewer();
}

abstract class OnlyCloseListener implements InternalFrameListener
{
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }
}

public class MainWindow{

	private JFrame frmCommunitySeismicNetwork;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 UIManager.setLookAndFeel(
					            UIManager.getSystemLookAndFeelClassName());
					MainWindow window = new MainWindow();
					window.frmCommunitySeismicNetwork.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	
	StreamViewer streamViewer;
    HistoryViewer historyViewer;
	RssViewer rssViewer;
    
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	    
		frmCommunitySeismicNetwork = new JFrame();
		frmCommunitySeismicNetwork.setTitle("Community Seismic Network- Viewer");
		frmCommunitySeismicNetwork.setBounds(100, 100, 734, 395);
		frmCommunitySeismicNetwork.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCommunitySeismicNetwork.getContentPane().setLayout(new BoxLayout(frmCommunitySeismicNetwork.getContentPane(), BoxLayout.X_AXIS));
		
		final JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.menu);
		frmCommunitySeismicNetwork.getContentPane().add(desktopPane);
	
		
	
		historyViewer = new HistoryViewer();
		desktopPane.add(historyViewer);
		
		rssViewer = new RssViewer(new HistoryViewerProvider() {
            
            @Override
            public HistoryViewer getHistoryViewer() {
                // TODO Auto-generated method stub
                return historyViewer;
            }
        });
        desktopPane.add(rssViewer);
		
		streamViewer =  new StreamViewer();
		desktopPane.add(streamViewer);
		
//		SourceViewerFrame f = new SourceViewerFrame();
//		desktopPane.add(f);
//		f.setVisible(true);
		
		JMenuBar menuBar = new JMenuBar();
		frmCommunitySeismicNetwork.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		final JCheckBoxMenuItem chckbxmntmStreamViewer = new JCheckBoxMenuItem("Stream Viewer");
		chckbxmntmStreamViewer.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		       
		        streamViewer.setVisible(chckbxmntmStreamViewer.getState());
		            
		    }
		});
		mnView.add(chckbxmntmStreamViewer);
		
		streamViewer.addInternalFrameListener(new OnlyCloseListener() {
            
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                chckbxmntmStreamViewer.setState(false);
                
            }
        });
		
		final JCheckBoxMenuItem chckbxmntmHistoryViewer = new JCheckBoxMenuItem("History Viewer");
		chckbxmntmHistoryViewer.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        historyViewer.setVisible(chckbxmntmHistoryViewer.getState());
		    }
		});
		mnView.add(chckbxmntmHistoryViewer);
		
		historyViewer.addInternalFrameListener(new OnlyCloseListener() {
            
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                chckbxmntmHistoryViewer.setState(false);
                
            }
        });
		
		
		
		final JCheckBoxMenuItem chckbxmntmRSSViewer = new JCheckBoxMenuItem("RSS Viewer");
		chckbxmntmRSSViewer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rssViewer.setVisible(chckbxmntmRSSViewer.getState());
            }
        });
        mnView.add(chckbxmntmRSSViewer);
        
        rssViewer.addInternalFrameListener(new OnlyCloseListener() {
            
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                chckbxmntmRSSViewer.setState(false);
                
            }
        });
	}
}
