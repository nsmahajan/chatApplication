import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.Font;
import java.awt.Color;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class ChatWindow {

	private JFrame frame;
	private TextArea inputChat,outputChat;
	private String recieverUserName = "";
	
	// Contains the reference to object of OnlineUsersListUI class.
	private OnlineUsersListUI parent;
	private int pX, pY;
	private JButton btnSend;
	private Boolean isOnline = true;

	/**
	 * Constructor.
	 * @param parent contains reference to OnlineUsersListUI object.
	 * 		  user contains the username(String) of the recipient.
	 * @return none
	 */
	public ChatWindow(String user, OnlineUsersListUI parent) {
		this.parent = parent;
		this.recieverUserName = user;
		initialize(user);
	}

	/**
	 * Creates the JFrame and adds all UI components to it.
	 * Contains function calls for close and minimize button.
	 * Drag functionality for JFrame is implemented in the function.
	 * @param user contains the username(String) of the recipient.
	 * @return none
	 */
	private void initialize(String user) {
		frame = new JFrame();
		frame.setBounds(550, 220, 258, 374);
		frame.setUndecorated(true);
		frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 10, 10));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton minimizeBtn = new JButton();
		minimizeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setState(Frame.ICONIFIED);
			}
		});
		minimizeBtn.setBounds(205, 5, 20, 20);
		minimizeBtn.setBorder(null);
		minimizeBtn.setBorderPainted(false);
		minimizeBtn.setOpaque(false);
		minimizeBtn.setContentAreaFilled(false);
		ImageIcon minimizeImage = new ImageIcon("images\\minimize.png");
		minimizeBtn.setIcon(minimizeImage);
		frame.getContentPane().add(minimizeBtn);
		
		JButton closeBtn = new JButton();
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hidePage();
			}
		});
		closeBtn.setBounds(233, 8, 11, 12);
		closeBtn.setBorder(null);
		closeBtn.setBorderPainted(false);
		closeBtn.setOpaque(false);
		closeBtn.setContentAreaFilled(false);
		ImageIcon closeImage = new ImageIcon("images\\close.png");
		closeBtn.setIcon(closeImage);
		frame.getContentPane().add(closeBtn);
		
		JPanel panel = new JPanel();
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent mouseEvent) {
				frame.setLocation(frame.getLocation().x+mouseEvent.getX()-pX,frame.getLocation().y+mouseEvent.getY()-pY);
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				pX=mouseEvent.getX();
                pY=mouseEvent.getY();
			}
		});
		panel.setBounds(0, 0, 200, 40);
		panel.setOpaque(false);
		
		frame.getContentPane().add(panel);
		
		outputChat = new TextArea("",2,50, TextArea.SCROLLBARS_VERTICAL_ONLY);
		outputChat.setEditable(false);
		outputChat.setBounds(10, 85, 234, 223);
		frame.getContentPane().add(outputChat);
		
		JLabel lblTo = new JLabel((StringLiterals.TO).toUpperCase() + " : " + user);
		lblTo.setForeground(new Color(162, 10, 35));
		lblTo.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
		lblTo.setBounds(10, 55, 234, 14);
		frame.getContentPane().add(lblTo);
		
		btnSend = new JButton(StringLiterals.SEND);
		btnSend.setBackground(new Color(162, 10, 35));
		btnSend.setForeground(new Color(255, 255, 255));
		btnSend.setBorder(null);
		btnSend.setFocusPainted(false);
		btnSend.setBorderPainted(false);
		btnSend.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
		btnSend.setBounds(184, 326, 60, 29);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = inputChat.getText();
				if(input.equals("")){
					JOptionPane.showMessageDialog(null, StringLiterals.MESSAGE_EMPTY);
				} else {
					String message = StringLiterals.TO + ":" + recieverUserName + ":";
					inputChat.setText("");
					input = input.replaceAll(System.getProperty("line.separator"), " ");
					message += input;
					parent.sendMessage(message);
				}
			}
		});
		frame.getContentPane().add(btnSend);
		
		inputChat = new TextArea("",2,50, TextArea.SCROLLBARS_VERTICAL_ONLY);
		inputChat.setBounds(10, 319, 160, 44);
		frame.getContentPane().add(inputChat);
		
		JLabel listUI = new JLabel();
		ImageIcon listUIbgImage = new ImageIcon("images\\online_user_bg.png");
		listUI.setIcon(listUIbgImage);
		listUI.setBounds(0, 0, 258, 374);
		frame.getContentPane().add(listUI);
		
		frame.setVisible(true);
	}
	
	/**
	 * Returns the recipient username.
	 * @param none.
	 * @return recieverUserName
	 */
	public String getUsername(){
		return this.recieverUserName;
	}

	/**
	 * Displays the message in the output text area of chat window.
	 * @param input contains the input message(String) from the sender.
	 * @return none.
	 */
	public void displayMessage(String input) {
		String[] messageArray = input.split(":");
		if(messageArray[1].equals(parent.getUsername())){
			String messageString = StringLiterals.YOU + " : " + messageArray[4];
			outputChat.append("\n"+messageString);
		}else {
			String messageString = messageArray[1].toUpperCase() + " : " + messageArray[4];
			outputChat.append("\n"+messageString);
		}
		this.inputChat.requestFocus();
	}

	/**
	 * Displays message that recipient is online in message window if recipient logs in again.
	 * Enables the send button and output text.
	 * @param none.
	 * @return none.
	 */
	public void showLoggedInAgain(){
		if(this.isOnline == false){
			this.isOnline = true;
			String messageString = this.recieverUserName + " : is online";
			outputChat.append("\n"+messageString);
			this.btnSend.setEnabled(true);
			this.inputChat.setEditable(true);
			this.inputChat.setEnabled(true);
		}
	}
	
	/**
	 * Displays message that recipient is offline in message window if recipient logs off again.
	 * Disables the send button and output text.
	 * @param none.
	 * @return none.
	 */
	public void showLoggedOutMessage() {
		if(this.isOnline == true){
			this.isOnline = false;
			String messageString = this.recieverUserName + " : is offline";
			outputChat.append("\n"+messageString);
			this.btnSend.setEnabled(false);
			this.inputChat.setEditable(false);
			this.inputChat.setEnabled(false);
		}
	}

	/**
	 * Hides the JFrame of the chat window.
	 * @param none.
	 * @return none.
	 */
	public void hidePage() {
		this.frame.setVisible(false);
	}

	/**
	 * Displays the JFrame of the chat window.
	 * @param none.
	 * @return none.
	 */
	public void showPage() {
		this.frame.setVisible(true);
	}

	/**
	 * Disable send button when no user is online for broadcast window.
	 * Input chat area is not editable.
	 * @param none.
	 * @return none.
	 */
	public void disableSendForBroadcast() {
		this.btnSend.setEnabled(false);
		this.inputChat.setEditable(false);
		this.inputChat.setEnabled(false);
	}

	/**
	 * Enables send button when user is online for broadcast window.
	 * Input chat area is editable.
	 * @param none.
	 * @return none.
	 */
	public void enableSendForBroadcast() {
		this.btnSend.setEnabled(true);
		this.inputChat.setEditable(true);
		this.inputChat.setEnabled(true);
	}
}
