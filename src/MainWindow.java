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
import javax.swing.JCheckBoxMenuItem;


public class MainWindow {

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
		        System.out.println("Foo");
		        streamViewer.setVisible(chckbxmntmStreamViewer.getState());
		            
		    }
		});
		mnView.add(chckbxmntmStreamViewer);
	}
}
