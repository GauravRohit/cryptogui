import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagConstraints;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.Timer;
import java.io.File;


public class CryptoGUI extends JPanel {
	
	private JPanel keyPanel, keyPanel2, inPanel, outPanel, buttonPanel, encryptPanel, decryptPanel;
	private JLabel keyLabel, inLabel, outLabel, statusLabel;
	private JTextArea keyArea, inArea, outArea;
	private JScrollPane keyPane, inPane, outPane;
	private JButton inButton, outButton, encryptButton, decryptButton;
	private File inFile, outFile, selectFile;
	

	private GridBagConstraints createGBC(int x, int y) {
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
		
		return c;
	}


	public CryptoGUI() {
		
		// init files
		this.inFile = null;
		this.outFile = null;
		this.selectFile = null;

		// init panels
		setLayout(new GridBagLayout());
		this.keyPanel = new JPanel(new GridBagLayout());
		this.keyPanel2 = new JPanel(new BorderLayout());
		this.inPanel = new JPanel(new GridBagLayout());
		this.outPanel = new JPanel(new GridBagLayout());
		this.buttonPanel = new JPanel(new GridLayout(1, 2));
		this.encryptPanel = new JPanel(new FlowLayout());
		this.decryptPanel = new JPanel(new FlowLayout());

		// init labels
		this.keyLabel = new JLabel("Key:");
		this.inLabel = new JLabel("Input File:");
		this.outLabel = new JLabel("Output:");
		this.statusLabel = new JLabel("Status:", JLabel.CENTER);
		
		// init textareas
		this.keyArea = new JTextArea(1, 22);
		this.inArea = new JTextArea(1, 15);
		this.outArea = new JTextArea(1, 15);

		// init scrollpanes
		this.keyPane = new JScrollPane(this.keyArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.inPane = new JScrollPane(this.inArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.outPane = new JScrollPane(this.outArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// init buttons
		this.inButton = new JButton("Select");
		this.outButton = new JButton("Select");
		this.encryptButton = new JButton("Encrypt");
		this.decryptButton = new JButton("Decrypt");

		this.inButton.addActionListener(new InListener());
		this.outButton.addActionListener(new OutListener());
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

		c = createGBC(0, 0);
                this.outPanel.add(this.outLabel, c);

                c = createGBC(0, 1);
                this.outPanel.add(this.outPane, c);

                c = createGBC(1, 1);
                this.outPanel.add(this.outButton, c);

                c = createGBC(0, 2);
                add(this.outPanel, c);

		this.encryptPanel.add(this.encryptButton);
		this.decryptPanel.add(this.decryptButton);
		this.buttonPanel.add(this.encryptPanel);
		this.buttonPanel.add(this.decryptPanel);
		
		c = createGBC(0, 3);
		add(this.buttonPanel, c);

		c = createGBC(0, 4);
		add(this.statusLabel, c);
	}

	
	private static String getNameNoExtension(String s) {
		
		int sep = s.lastIndexOf("/") + 1;
		String ss = s.substring(sep);
		int dot = ss.indexOf(".");
		return ss.substring(0, dot);
	}

	
	
	private static String getExtension(String s) {
		
		int dot = s.lastIndexOf(".") + 1;
		return s.substring(dot);
	}

	private class InListener implements ActionListener {
	
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int val = fc.showOpenDialog(CryptoGUI.this);
			if(val == JFileChooser.APPROVE_OPTION) {
				CryptoGUI.this.inFile = fc.getSelectedFile();
				CryptoGUI.this.inArea.setText(CryptoGUI.this.inFile.getAbsolutePath());
			}
		}
	}


	private class OutListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int val = fc.showOpenDialog(CryptoGUI.this);
			if (val == JFileChooser.APPROVE_OPTION) {
				CryptoGUI.this.selectFile = fc.getSelectedFile();
				CryptoGUI.this.outArea.setText(CryptoGUI.this.selectFile.getAbsolutePath());
			}
		}
	}
	
	private class EncryptListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			try {
				try {
					if (CryptoGUI.this.selectFile.isDirectory()) {
						CryptoGUI.this.outFile = new File(CryptoGUI.this.selectFile.getAbsolutePath() + "/" + CryptoGUI.getNameNoExtension(CryptoGUI.this.inFile.getAbsolutePath()) + ".enc." + CryptoGUI.getExtension(CryptoGUI.this.inFile.getAbsolutePath()));
					} else {
						CryptoGUI.this.outFile = CryptoGUI.this.selectFile;
					}
				} catch (Exception e1) {
					throw new MyCryptoUtils.CustomException(e1.getClass().getSimpleName());
				}

				MyCryptoUtils.encrypt(CryptoGUI.this.keyArea.getText(), CryptoGUI.this.inFile, CryptoGUI.this.outFile);
				Timer timer = new Timer(1000, new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						CryptoGUI.this.statusLabel.setText("Status: Done!");
					}});
				timer.setRepeats(false);
				timer.start();
				CryptoGUI.this.statusLabel.setText("Status: Encrypting...");
			} catch (Exception ex) {
				CryptoGUI.this.statusLabel.setText("Status: Error!");
				JOptionPane.showMessageDialog(CryptoGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	
	private class DecryptListener implements ActionListener {

                public void actionPerformed(ActionEvent e) {
			
                        try {
				try {
					if (CryptoGUI.this.selectFile.isDirectory()) {
						CryptoGUI.this.outFile = new File(CryptoGUI.this.selectFile.getAbsolutePath() + "/" + CryptoGUI.getNameNoExtension(CryptoGUI.this.inFile.getAbsolutePath()) + ".dec." + CryptoGUI.getExtension(CryptoGUI.this.inFile.getAbsolutePath()));
                        		} else {
						CryptoGUI.this.outFile = CryptoGUI.this.selectFile;
					}
				} catch (Exception e1) {
					throw new MyCryptoUtils.CustomException(e1.getClass().getSimpleName());
				}
			
                                MyCryptoUtils.decrypt(CryptoGUI.this.keyArea.getText(), CryptoGUI.this.inFile, CryptoGUI.this.outFile);
				Timer timer = new Timer(1000, new ActionListener() {
                                        public void actionPerformed(ActionEvent evt) {
                                                CryptoGUI.this.statusLabel.setText("Status: Done!");
                                        }});
                                timer.setRepeats(false);
                                timer.start();
                                CryptoGUI.this.statusLabel.setText("Status: Decrypting...");
                        } catch (Exception ex) {
				CryptoGUI.this.statusLabel.setText("Status: Error!");
                        	if(ex.getMessage().equals("BadPaddingException")) {
                                        JOptionPane.showMessageDialog(CryptoGUI.this, "Incorrect key", "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                        JOptionPane.showMessageDialog(CryptoGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
			}
                }
        }

			
	public static void main(String[] args) {
		JFrame frame = new JFrame("CryptoGUI");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(300, 200));
                frame.setResizable(false);
		frame.add(new CryptoGUI());
		frame.pack();
		frame.setVisible(true);
	}
}
		
