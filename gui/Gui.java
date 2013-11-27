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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
import javax.swing.text.DefaultCaret;

import simplechat.SimpleChat;


public class Gui extends JFrame implements WindowListener, Runnable
{
	private SimpleChat sc;
	private static final long serialVersionUID = 2408778836724142968L;
	private JTextArea chatLogArea;
	private JTextField inputField;
	private JLabel membersPane;
	private JMenuItem fileDisconnect;
	private String connectToIPInputBox;
	private ArrayList<String> membersNickName;

	public Gui(String nickname, SimpleChat simpleChat)
	{
		membersNickName = new ArrayList<String>();
		membersNickName.add(nickname);
		this.sc = simpleChat;
		// Some configurations:
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width / 2, screen.height / 2);
		setTitle("SimpleChat 0.0.2" + " | Ciao, " + nickname);

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
		setVisible(true);
	}

	/**
	 * Write to the ChatLog
	 * 
	 * @param String
	 *            message
	 */
	public void chatLogWrite(String message)
	{
		chatLogArea.append(message + "\n");
	}

	/**
	 * Get Chat Log Panel.
	 * 
	 * @return
	 */
	public JPanel getChatLogPanel()
	{
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setOpaque(true);
		// ChatLog:
		chatLogArea = new JTextArea(5, 30); // The textArea
		DefaultCaret caret = (DefaultCaret) chatLogArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatLogArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatLogArea); // The scroller
		contentPanel.add(scrollPane, BorderLayout.CENTER); // Adding the croller
		// Members List:
		JPanel secondPanel = new JPanel();
		membersPane = new JLabel();
		updateMembersList();
		secondPanel.add(membersPane, BorderLayout.NORTH);
		contentPanel.add(secondPanel, BorderLayout.EAST);
		return contentPanel; // Return the panel.
	}

	/**
	 * Get the input text area, and the Send Button.
	 * 
	 * @return JPanel SouthPanel
	 */
	public JPanel getTextInputAndSendPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		inputField = new JTextField();
		inputField.setEnabled(false);
		/*
		 * Key Listener to grab when user Hits Enter:
		 */
		inputField.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent arg0)
			{
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					sc.sendMessage(inputField.getText());
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					inputField.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0)
			{
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
				if (sc.isConnected())
				{
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
		fileConnect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				connectToIPInputBox = (String) JOptionPane
				        .showInputDialog("Insert Your Mate's IP:");
				sc.connect(connectToIPInputBox);
			}
		});

		fileDisconnect = new JMenuItem("Disconnect");
		fileDisconnect.setEnabled(false);
		fileDisconnect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				sc.closeConnection();
				sc.createListener();
				setDisconnectButtonEnabled(false);
			}

		});
		menuFile.add(fileConnect);
		menuFile.add(fileDisconnect);

		// Elements of Help:
		JMenuItem helpHelp = new JMenuItem("Help");
		JMenuItem helpAbout = new JMenuItem("About");
		menuHelp.add(helpHelp);
		menuHelp.add(helpAbout);
		return menu;
	}

	public void setInputTextEditable(boolean enabled)
	{
		inputField.setEnabled(enabled);
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		// TODO: Aggiungere chiusura connessione.
	}

	@Override
	public void run()
	{
	}

	/**
	 * Add a nickName to the Members List.
	 * 
	 * @param nickName
	 */
	public void addNickname(String nickName)
	{
		membersNickName.add(nickName);
		updateMembersList();
	}

	/**
	 * Method to update the Members List.
	 */
	private void updateMembersList()
	{
		membersPane.setText("<html><p>Chat Members:<br><br><ul>");
		for (String member : membersNickName)
		{
			membersPane.setText(membersPane.getText() + "<li>" + member
			        + " </li>");
		}
		membersPane.setText(membersPane.getText() + "</ul></html>");
	}

	/**
	 * Static Method to prompt the NickName
	 * 
	 * @return NickName
	 */
	public static String getNickName()
	{
		String nickName;
		nickName = (String) JOptionPane.showInputDialog("Choose a NickName:");
		if (nickName.length() == 0) nickName = "Guest";
		return nickName;
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
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

	public void setDisconnectButtonEnabled(boolean b)
	{
		fileDisconnect.setEnabled(b);
	}

}
