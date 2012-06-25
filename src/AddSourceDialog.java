import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;


public class AddSourceDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField urlField;
	private JTextField portField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddSourceDialog dialog = new AddSourceDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	JDesktopPane desktop;
	
	public AddSourceDialog() {
		this(null);
	}
	
	public AddSourceDialog(final JDesktopPane desktop) {
		this.desktop = desktop;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 262, 160);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new MigLayout(""));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Thread b = new Thread(new ViewerWorker(urlField.getText(), portField.getText(),desktop));
						b.start();
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
		
		JLabel info = new JLabel("Please enter the details of your source");
		JLabel urlRequest = new JLabel("IP Address:");
		JLabel portRequest = new JLabel("Port:");
		
		urlField = new JTextField();
		urlField.setText("127.0.0.1");
		
		
		portField = new JTextField();
		portField.setText("5632");
		
		

		
		contentPanel.add(info,"cell 0 0 2 1,alignx center");
		contentPanel.add(urlRequest, "cell 0 1,alignx label");
		contentPanel.add(urlField,"cell 1 1,grow");
		contentPanel.add(portRequest,"cell 0 2,alignx label");
		contentPanel.add(portField,"cell 1 2,grow");
	}

}
