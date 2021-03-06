import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class ChatGUI extends JFrame implements ActionListener, WindowListener {
	private JTextArea message, event;
	private JTextField letter;
	private JButton send;
	private JLabel label;
	private ClientCommunicationThread comm = null;
	
	public ChatGUI(ClientCommunicationThread newComm, String name){
		this.setTitle(name);
		comm = newComm;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,400);
		JPanel panel = new JPanel(); // the panel is not visible in output
		label = new JLabel("Enter Text");
		letter = new JTextField(20);// accepts upto 10 characters
		send = new JButton("Send");
		send.addActionListener(this);
		panel.add(label);// Components Added using Flow Layout
		panel.add(letter);
		panel.add(send);
		// Text Area at the Center
		message = new JTextArea();
		//Adding Components to the frame.
		this.getContentPane().add(BorderLayout.SOUTH,panel);
		this.getContentPane().add(BorderLayout.CENTER,message);
		this.setVisible(true);
	}
	public void appendMessage(String newMessage, String name){
		message.append("\n" + name + ": " + newMessage);
	}
	public void actionPerformed(ActionEvent e) {
		String toSend = letter.getText();
		letter.setText("");
		appendMessage(toSend, "Me");
		comm.send(toSend);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}
	public void windowClosed(WindowEvent arg0) {
	}
	public void windowClosing(WindowEvent arg0) {
		try {
			comm.close();
			comm.remove();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void windowDeactivated(WindowEvent arg0) {
	}
	public void windowDeiconified(WindowEvent arg0) {
	}
	public void windowIconified(WindowEvent arg0) {
	}
	public void windowOpened(WindowEvent arg0) {
	}
}
