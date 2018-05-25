// imports
import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagConstraints;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.io.File;

// Create a GUI to encrypt and decrypt files
public class CryptoGUI extends JPanel {
	private JPanel keyPanel, keyPanel2, inPanel, buttonPanel, encryptPanel, decryptPanel;
	private JLabel keyLabel, inLabel, statusLabel;
	private JTextArea keyArea, inArea;
	private JScrollPane keyPane, inPane;
	private JButton inButton, encryptButton, decryptButton;
	private File inFile, outFile, selectFile;
	
	// create GridBagConstraints
	private GridBagConstraints createGBC(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
		return c;
	}

	// create CryptoGUI
	public CryptoGUI() {
		// init files
		this.inFile = null;
		this.outFile = null;

		// init panels
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		this.keyPanel = new JPanel(new GridBagLayout());
		this.keyPanel2 = new JPanel(new BorderLayout());
		this.inPanel = new JPanel(new GridBagLayout());
		this.buttonPanel = new JPanel(new GridLayout(1, 2));
		this.encryptPanel = new JPanel(new FlowLayout());
		this.decryptPanel = new JPanel(new FlowLayout());

		// init labels
		this.keyLabel = new JLabel("Key:");
		this.inLabel = new JLabel("Input File:");
		this.statusLabel = new JLabel("Status: Waiting", JLabel.CENTER);
		
		// init textareas
		this.keyArea = new JTextArea(1, 22);
		this.inArea = new JTextArea(1, 15);

		// init scrollpanes
		this.keyPane = new JScrollPane(this.keyArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.inPane = new JScrollPane(this.inArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// init buttons
		this.inButton = new JButton("Select");
		this.encryptButton = new JButton("Encrypt");
		this.decryptButton = new JButton("Decrypt");

		this.inButton.addActionListener(new InListener());
		this.encryptButton.addActionListener(new EncryptListener());
		this.decryptButton.addActionListener(new DecryptListener());

		// assemble
		GridBagConstraints c;

		c = createGBC(0, 0);
		this.keyPanel.add(this.keyLabel, c);
		
		c = createGBC(0, 1);
		this.keyPanel.add(this.keyPane, c);

		this.keyPanel2.add(this.keyPanel, BorderLayout.WEST);
		
		c = createGBC(0, 0);
		add(keyPanel2, c);

		c = createGBC(0, 0);
		this.inPanel.add(this.inLabel, c);
		
		c = createGBC(0, 1);
		this.inPanel.add(this.inPane, c);

		c = createGBC(1, 1);
		this.inPanel.add(this.inButton, c);
		
		c = createGBC(0, 1);
		add(this.inPanel, c);

		this.encryptPanel.add(this.encryptButton);
		this.decryptPanel.add(this.decryptButton);
		this.buttonPanel.add(this.encryptPanel);
		this.buttonPanel.add(this.decryptPanel);
		
		c = createGBC(0, 3);
		add(this.buttonPanel, c);

		c = createGBC(0, 4);
		add(this.statusLabel, c);
	}

	// get file name without extension
	private static String getNameNoExtension(File f) {
		String s = f.getAbsolutePath();
		int sep = s.lastIndexOf(File.separator) + 1;
		String ss = s.substring(sep);
		int dot = ss.indexOf(".");
		return ss.substring(0, dot);
	}

	// get file extension
	private static String getExtension(File f) {
		String s = f.getAbsolutePath();
		int dot = s.lastIndexOf(".") + 1;
		return s.substring(dot);
	}

	// get file path
	private static String getPath(File f) {
		String s = f.getAbsolutePath();
		int sep = s.lastIndexOf(File.separator);
		return s.substring(0, sep) + File.separator;
	}	

	// sets error messages
	private static String getError(Exception e) {
		String s = e.getMessage();
		switch(s) {
			case "BadPaddingException":
				return "Incorrect Key";
			case "NullPointerException":
				return "Select File(s)";
			case "IllegalBlockSizeException":
				return "File Not Encrypted";
			default:
				return s;
		}
	}
	
	// listener for the input file button
	private class InListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// init JFileChooser
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int val = fc.showOpenDialog(CryptoGUI.this);
			if(val == JFileChooser.APPROVE_OPTION) {
				CryptoGUI.this.selectFile = fc.getSelectedFile();
				CryptoGUI.this.inArea.setText(CryptoGUI.this.selectFile.getAbsolutePath());
			}
		}
	}

	
	// listener for the encrypt button
	private class EncryptListener implements ActionListener {
		private int count;

		public void actionPerformed(ActionEvent e) {
			try {
				// file
				if(CryptoGUI.this.selectFile.isFile()) {
					// check file
					if(!CryptoGUI.this.selectFile().getName().contains("enc")) {
						// get output file
						CryptoGUI.this.outFile = new File(CryptoGUI.this.getPath(CryptoGUI.this.selectFile) + CryptoGUI.this.getNameNoExtension(CryptoGUI.this.selectFile) + ".enc." + CryptoGUI.this.getExtension(CryptoGUI.this.selectFile));

						// encrypt file
						MyCryptoUtils.encrypt(CryptoGUI.this.keyArea.getText(), CryptoGUI.this.selectFile, CryptoGUI.this.outFile);

						// status report
						CryptoGUI.this.statusLabel.setText("Status: Encrypting...");
						Timer timer = new Timer(1000, new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								CryptoGUI.this.statusLabel.setText("Status: 1 File Encrypted");
							}});
						timer.setRepeats(false);
						timer.start();

						// delete
						if(!CryptoGUI.this.selectFile.getAbsolutePath().equals(CryptoGUI.this.outFile.getAbsolutePath())) {
							CryptoGUI.this.selectFile.delete();
						}
					} else {
						CryptoGUI.this.statusLabel.setText("Status: Error!");
						JOptionPane.showMessageDialog(CryptoGUI.this, "File Already Encrypted", "Error", JOptionPane.ERROR_MESSAGE);
					}
					   
				// directory
				} else if(CryptoGUI.this.selectFile.isDirectory()) {
					File[] fileList = CryptoGUI.this.selectFile.listFiles();
					this.count = 0;
					for(int i = 0;  i < fileList.length; i++) {
						if(!fileList[i].getName().contains("enc")) {
							// bump count
							this.count++;
							
							// get output file
							CryptoGUI.this.outFile = new File(CryptoGUI.this.getPath(fileList[i]) + CryptoGUI.this.getNameNoExtension(fileList[i]) + ".enc." + CryptoGUI.this.getExtension(fileList[i]));

							// decrypt file
							MyCryptoUtils.decrypt(CryptoGUI.this.keyArea.getText(), fileList[i], CryptoGUI.this.outFile);

							// status report
							CryptoGUI.this.statusLabel.setText("Status: Encrypting...");

							// delete
							if(!fileList[i].getAbsolutePath().equals(CryptoGUI.this.outFile.getAbsolutePath())) {
								fileList[i].delete();
							}
						}
					}
					// status report
					Timer timer = new Timer(1000, new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							CryptoGUI.this.statusLabel.setText("Status: " + EncryptListener.this.count + " File(s) Encrypted");
					}});
					timer.setRepeats(false);
					timer.start();
				}
			} catch (Exception ex) {
				// error popup
				CryptoGUI.this.statusLabel.setText("Status: Error!");
				String errorMessage = CryptoGUI.getError(ex);
				JOptionPane.showMessageDialog(CryptoGUI.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// listener for the decrypt button
	private class DecryptListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                        try {
				// get output file
				CryptoGUI.this.outFile = new File(CryptoGUI.this.getPath(CryptoGUI.this.inFile) + CryptoGUI.this.getNameNoExtension(CryptoGUI.this.inFile) + "." + CryptoGUI.this.getExtension(CryptoGUI.this.inFile));
				
				// decrypt file
                                MyCryptoUtils.decrypt(CryptoGUI.this.keyArea.getText(), CryptoGUI.this.inFile, CryptoGUI.this.outFile);
				
				// status report
				Timer timer = new Timer(1000, new ActionListener() {
                                        public void actionPerformed(ActionEvent evt) {
                                                CryptoGUI.this.statusLabel.setText("Status: Done!");
                                        }});
                                timer.setRepeats(false);
                                timer.start();
                                CryptoGUI.this.statusLabel.setText("Status: Decrypting...");

				// delete input file
				CryptoGUI.this.inFile.delete();
                        } catch (Exception ex) {
				// error popup
				CryptoGUI.this.statusLabel.setText("Status: Error!");
				String errorMessage = CryptoGUI.getError(ex);
				JOptionPane.showMessageDialog(CryptoGUI.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
                }
        }

	// run		
	public static void main(String[] args) {
		// create gui
		JFrame frame = new JFrame("CryptoGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(new CryptoGUI());
		frame.pack();
		frame.setVisible(true);
	}
}
		
