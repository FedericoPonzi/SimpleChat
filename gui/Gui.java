package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import simplechat.SimpleChat;

public class Gui extends JFrame implements Runnable
{
	private SimpleChat sc;
	private static final long serialVersionUID = 2408778836724142968L;
	private Menu menu;
	private JTextArea chatLogArea;
	private JTextField inputField;
	private JLabel membersPane;
	private ArrayList<String> membersNickName;

	public Gui(String nickname)
	{
		membersNickName = new ArrayList<String>();
		membersNickName.add(nickname);
		this.sc = SimpleChat.getInstance();
		membersPane = new JLabel();
		// Some configurations:
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width / 2, screen.height / 2);
		setTitle("SimpleChat 0.0.2 | Ciao, " + nickname);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		/*
		 * Menu:
		 */
		menu = new Menu();
		setJMenuBar(menu);
		/*
		 * Panels:
		 */
		add(getTextInputAndSendPanel(), BorderLayout.SOUTH);
		add(getChatLogPanel(), BorderLayout.CENTER);
		//JOptionPane.showMessageDialog(this, "Use this IP:" + ip +" and Port:" + port +);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				setVisible(false);
				sc.closeConnection();
				sc.removeUPnPMapping();
				dispose();
				System.exit(0);
			}
		}
		
				);
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
					sc.sendChatMessage(inputField.getText());
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
					sc.sendChatMessage(inputField.getText());
					inputField.setText("");
				}
			}
		});
		sendButton.setSize(new Dimension(10, 10));
		panel.add(sendButton, BorderLayout.EAST);
		return panel;
	}

	

	@Override
	public void run()
	{
	}

	/**
	 * Method to update the Members List.
	 */
	private void updateMembersList()
	{
		String textToSet = "<html><p>Chat Members:<br><br><ol>";

		for (String member : membersNickName)
		{
			textToSet += "<li>" + member + " </li>";
		}
		membersPane.setText(textToSet + "</ol></html>");
	}

	
	/**
	 * Remove a Nickname from the membersNickName arraylist.
	 * 
	 * @param addresseeNickname
	 */
	public void removeNickName(String addresseeNickname)
	{
		membersNickName.remove(addresseeNickname);
		updateMembersList();
	}

	/**
	 * Set the InputText Editable
	 * 
	 * @param enabled
	 */
	public void setInputTextEditable(boolean enabled)
	{
		inputField.setEnabled(enabled);
	}

	/**
	 * Set the Disconnect Button Enabled
	 * 
	 * @param b
	 */
	public void setDisconnectButtonEnabled(boolean b)
	{
		menu.setDisconnectButtonEnabled(b);
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
	 * Static Method to prompt the NickName
	 * 
	 * @return NickName
	 */
	public static String getNickName()
	{
		String nickName = "";
		nickName = (String) JOptionPane.showInputDialog("Choose a NickName:");
		nickName = nickName!= null? nickName : "Guest";
		return nickName;
	}
}
