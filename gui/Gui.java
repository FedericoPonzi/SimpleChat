package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import simplechat.SimpleChat;


public class Gui extends JFrame implements WindowListener, Runnable
{
	private static final long serialVersionUID = 2408778836724142968L;
	private JPanel chatLog;
	private JLabel chatComponents;
	private JTextArea chatLogArea;
	private JTextField inputField;
	private String connectToIPInputBox;
	private SimpleChat sc;
	private String[] chatMembers;

	public Gui(String nickname, SimpleChat simpleChat)
	{
		this.sc = simpleChat;
		// Some configurations:
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width / 2, screen.height / 2);
		setTitle("SimpleChat 0.0.2" + " | Ciao, "+ nickname);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		/*
		 * Menu:
		 */
		setJMenuBar(getMenu());
		/*
		 * Panels:
		 */
		add(getTextInputAndSendPanel(), BorderLayout.SOUTH);
		add(getChatLogPanel(), BorderLayout.CENTER);
	}
	/**
	 * Write to the ChatLog
	 * @param String message
	 */
	public void chatLogWrite(String message)
	{
		chatLogArea.append(message + "\n");
	}
	public JPanel getChatLogPanel()
	{
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setOpaque(true);
		//ChatLog:
		chatLogArea = new JTextArea(5, 30); // The textArea
		chatLogArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatLogArea); // The scroller
		contentPanel.add(scrollPane, BorderLayout.CENTER); // Adding the croller
		//Members List:
		JPanel secondPanel = new JPanel();
		JLabel textPane = new JLabel();
		textPane.setText("<html><p>Chat Members:<br> Tu madre<br> Tu zia</p></html>");
		secondPanel.add(textPane, BorderLayout.NORTH);
		contentPanel.add(secondPanel, BorderLayout.EAST);
		return contentPanel; // Return the panel.
	}
	/**
	 * Get the input text area, and the Send Button.
	 * @return JPanel SouthPanel
	 */
	public JPanel getTextInputAndSendPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		inputField = new JTextField();
		/*
		 * Key Listener to grab when user Hits Enter:
		 */
		inputField.addKeyListener(new KeyListener(){

			@Override
            public void keyPressed(KeyEvent arg0)
            {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					chatLogWrite(inputField.getText());
					sc.sendMessage(inputField.getText());
				}
            }

			@Override
            public void keyReleased(KeyEvent arg0)
            {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					inputField.setText("");	     
				}
            }

			@Override
            public void keyTyped(KeyEvent arg0)
            {
	            // TODO Auto-generated method stub
	            
            }
			
		});
		panel.add(inputField, BorderLayout.CENTER);
		JButton sendButton = new JButton("Send");
		/*
		 * Action listener to grab when the user click on Send Button
		 */
		sendButton.addActionListener(new ActionListener()
		{

			@Override
            public void actionPerformed(ActionEvent arg0)
            {
				if(sc.isConnected())
				{
					chatLogWrite(inputField.getText());
					sc.sendMessage(inputField.getText());
					inputField.setText("");
				}
            }
		});
		sendButton.setSize(new Dimension(10, 10));
		panel.add(sendButton, BorderLayout.EAST);
		return panel;
	}

	/**
	 * getMenu() Returns the JMenuBar, with all the options and ready to be
	 * attached to the main panel.
	 * 
	 * @return JMenuBar The Menu Bar
	 */
	private JMenuBar getMenu()
	{
		JMenuBar menu = new JMenuBar();
		// Elements of the bar:
		JMenu menuFile = new JMenu("File");
		menu.add(menuFile);
		JMenu menuHelp = new JMenu("Help");
		menu.add(menuHelp);

		// Elements of File:
		JMenuItem fileConnect = new JMenuItem("Connect");
		fileConnect.addActionListener(new ActionListener(){
			@Override
            public void actionPerformed(ActionEvent e)
            {
				connectToIPInputBox = (String) JOptionPane.showInputDialog("Insert Your Mate's IP:");
				sc.connect(connectToIPInputBox);
            }
		});
		JMenuItem fileExit = new JMenuItem("Disconnect");
		menuFile.add(fileConnect);
		menuFile.add(fileExit);

		// Elements of Help:
		JMenuItem helpHelp = new JMenuItem("Help");
		JMenuItem helpAbout = new JMenuItem("About");
		menuHelp.add(helpHelp);
		menuHelp.add(helpAbout);
		return menu;
	}
	
	@Override
    public void windowActivated(WindowEvent arg0)
    {
		
    }

	@Override
    public void windowClosed(WindowEvent arg0)
    {
		//TODO: Aggiungere chiusura connessione.
    }

	@Override
    public void windowClosing(WindowEvent arg0)
    {
		dispose();
		System.exit(0);
    }

	@Override
    public void windowDeactivated(WindowEvent arg0)
    {
	    
    }

	@Override
    public void windowDeiconified(WindowEvent arg0)
    {
	    
    }

	@Override
    public void windowIconified(WindowEvent arg0)
    {
	    
    }

	@Override
    public void windowOpened(WindowEvent arg0)
    {
	    
    }

	@Override
    public void run()
    {
	    // TODO Auto-generated method stub
	    
    }
	public static String getNickName()
	{
		return (String) JOptionPane.showInputDialog("Choose a NickName:");
	}

}
