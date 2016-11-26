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
	private Socket sock;
	private ClientCommunicationThread comm = null;
	private DataOutputStream outStream = null;
	
	public ChatGUI(ClientCommunicationThread newComm, Socket sock){
		this.sock = sock;
		try {
			outStream = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,400);
		JPanel panel = new JPanel(); // the panel is not visible in output
		label = new JLabel("Enter Text");
		letter = new JTextField(10);// accepts upto 10 characters
		send = new JButton("Send");
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
	public void appendMessage(String newMessage){
		message.setText(newMessage);
	}
	public void actionPerformed(ActionEvent e) {
		String toSend = message.getText();
		send(toSend);
	}
	public void send(String toSend) {
		try {
			outStream.writeUTF(toSend);
			outStream.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
