package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import simplechat.SimpleChat;

public class Menu extends JMenuBar
{
	private JMenuItem fileDisconnect;
	private SimpleChat sc = SimpleChat.getInstance();
	public Menu()
	{
		// Elements of the bar:
				JMenu menuFile = new JMenu("File");
				add(menuFile);
				JMenu menuHelp = new JMenu("Help");
				add(menuHelp);

				// Elements of File:
				JMenuItem fileConnect = new JMenuItem("Connect");
				fileConnect.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						String connectToIPInputBox = (String) JOptionPane.showInputDialog("Insert Your Mate's IP:");
						SimpleChat.getInstance().connect(connectToIPInputBox);
					}
				});

				fileDisconnect = new JMenuItem("Disconnect");
				fileDisconnect.setEnabled(false);
				fileDisconnect.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						sc.sendSCMessage("01-CLOSE_REQ");
						sc.closeConnection();
					}

				});
				JMenuItem fileExit = new JMenuItem("Exit");
				fileExit.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						sc.closeConnection();
						System.exit(0);
					}

				});
				menuFile.add(fileConnect);
				menuFile.add(fileDisconnect);
				menuFile.add(fileExit);
				// Elements of Help:
				JMenuItem helpHelp = new JMenuItem("Help");
				JMenuItem helpAbout = new JMenuItem("About");
				menuHelp.add(helpHelp);
				menuHelp.add(helpAbout);
	}
	public void setDisconnectButtonEnabled(boolean b)
    {
		fileDisconnect.setEnabled(b);
    }
}
