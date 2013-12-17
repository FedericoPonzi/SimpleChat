package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class HelpFrame extends JFrame
{
	public HelpFrame()
	{
		super("Help | SimpleChat" ); 
		setSize( 500,120 ); 
		setLocationRelativeTo( null ); 
		JLabel label = new JLabel();
		label.setText(
				"<html>"+
					"<body>"+
						"<h2>Get Help for SimpleChat</h2>"+
						"<p>"+
							"SimpleChat is a chat born to learn how TCP/IP works. That\' s all. Bytheway, you can download the code and use it in your projects or just help me developing some more.<br/>"+
							"This program is free software, and made for fun. Enjoy."+
						"</p>"+
					"</body>"+
				"</html>");
		getContentPane().add(label);
		setVisible( true );
		
	}
}
