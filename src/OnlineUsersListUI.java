import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class OnlineUsersListUI {

	// Contains the references for all chat windows opened.
	private static ArrayList<ChatWindow> ChatWindows = new ArrayList<ChatWindow>();
	
	private JFrame frame;
	private JScrollPane scrollPane;
	private JPanel scrollPanel;
	private WPIChatClient parent;
	private JLabel usernameText, noUserOnlineLabel;
	private String username;
	
	// Contains the reference to broadcast chat window.
	private ChatWindow sendToAll;
	private int onlineUserCount = 0;
	private JLabel listUI;
	private int pX, pY;

	/**
	 * Constructor.
	 * @param  parent contains reference to WPIChatClient class.
	 * @return none
	 */
	public OnlineUsersListUI(WPIChatClient parent) {
		this.parent = parent;
		initialize();
	}

	/**
	 * Creates the JFrame and adds all UI components to it.
	 * Contains the functionality for close, minimize and logout button.
	 * @param  none.
	 * @return none
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					disconnectFromServer();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		frame.setBounds(550, 220, 258, 374);
		frame.setUndecorated(true);
		frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 10, 10));
		frame.getContentPane().setLayout(null);
		
		usernameText = new JLabel();
		usernameText.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
		usernameText.setForeground(new Color(162, 10, 35));
		usernameText.setBounds(10, 55, 180, 23);
		frame.getContentPane().add(usernameText);
		
		noUserOnlineLabel = new JLabel(StringLiterals.NO_USER_ONLINE_LABEL);
		noUserOnlineLabel.setBounds(85, 100, 245, 260);
		noUserOnlineLabel.setVisible(false);
		frame.getContentPane().add(noUserOnlineLabel);
		
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
		
		JButton logoutBtn = new JButton();
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					disconnectFromServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		logoutBtn.setBounds(225, 5, 20, 20);
		logoutBtn.setBorder(null);
		logoutBtn.setBorderPainted(false);
		logoutBtn.setOpaque(false);
		logoutBtn.setContentAreaFilled(false);
		ImageIcon logoutImage = new ImageIcon("images\\logout.png");
		logoutBtn.setIcon(logoutImage);
		frame.getContentPane().add(logoutBtn);
		
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
		panel.setBounds(0, 0, 200, 45);
		panel.setOpaque(false);
		
		frame.getContentPane().add(panel);
		
		scrollPanel = new JPanel();
		scrollPane = new JScrollPane(scrollPanel);
		scrollPane.setBounds(5, 100, 245, 260);
		scrollPane.setBorder(null);
		frame.getContentPane().add(scrollPane);
		
		JButton btnSendToAll = new JButton();
		btnSendToAll.setBounds(200, 55, 34, 25);
		btnSendToAll.setBorder(null);
		btnSendToAll.setBorderPainted(false);
		btnSendToAll.setOpaque(false);
		btnSendToAll.setContentAreaFilled(false);
		ImageIcon broadcastImage = new ImageIcon("images\\broadcast_icon.png");
		btnSendToAll.setIcon(broadcastImage);
		btnSendToAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(onlineUserCount == 0){
					JOptionPane.showMessageDialog(null, StringLiterals.NO_USER_ONLINE);
				} else {
					createSendAllWindow();
				}
			}
		});
		frame.getContentPane().add(btnSendToAll);
		
		listUI = new JLabel();
		ImageIcon listUIbgImage = new ImageIcon("images\\online_user_bg.png");
		listUI.setIcon(listUIbgImage);
		listUI.setBounds(0, 0, 258, 374);
		frame.getContentPane().add(listUI);
		
		frame.setVisible(true);
	}
	
	/**
	 * Creates an instance of broadcast chat window and stores it reference.
	 * If chat window already exists then just display the chat window.
	 * @param  none.
	 * @return none
	 */
	private void createSendAllWindow(){
		if(sendToAll == null){
			sendToAll = new ChatWindow(StringLiterals.SEND_TO_ALL, this);
		} else{
			this.sendToAll.showPage();
		}
	}
	
	/**
	 * Updates the UI for displaying online users list.
	 * @param  onlineUsers contains list of all online users received from server.
	 * 		   username contains username of the logged in user.
	 * @return none
	 */
	public void updateOnlineUsersList(String onlineUsers, String username) {
		String nameText = StringLiterals.LOGGED_IN_AS + username;
		usernameText.setText(nameText);
		this.username = username;
		String[] users = onlineUsers.split(":");

		if(this.scrollPanel.getComponentCount() != 0){
			this.scrollPanel.removeAll();
		}
		
		onlineUserCount = 0;
		for(int i = 1; i < users.length; i++){
			if(!(users[i].equals(username))){
				onlineUserCount++;
				OnlineUserCustomButton btnUsers = new OnlineUserCustomButton(users[i], 0, (onlineUserCount-1) * 36);
				btnUsers.addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent mouseEvent){
						openChatWindow(((OnlineUserCustomButton) mouseEvent.getSource()).getUsername());
					}
				});
				this.scrollPanel.add(btnUsers);
			}
		}
		this.scrollPanel.setPreferredSize(new Dimension(225,onlineUserCount * 36 + (onlineUserCount - 1)* 11));
		this.scrollPanel.revalidate();
		this.scrollPane.revalidate();
		this.scrollPane.repaint();
		
		if(onlineUserCount == 0){
			noUserOnlineLabel.setVisible(true);
		}
		else {
			noUserOnlineLabel.setVisible(false);
		}
		
		showUserLoggedOff(users);
		showUserLoggedIn(users);
	}
	
	/**
	 * Show user as logged in if its chat window is open.
	 * If online users are greater than 1 then enable the broadcast chat window else disable it.
	 * @param  users contains list of all online users.
	 * @return none
	 */
	private void showUserLoggedIn(String[] users) {
		for(int j = 0; j < ChatWindows.size(); j++){
			if(Arrays.asList(users).contains(ChatWindows.get(j).getUsername())){
				ChatWindows.get(j).showLoggedInAgain();
			}
		}
		
		if(sendToAll != null){
			if(onlineUserCount == 0){
				sendToAll.disableSendForBroadcast();
			} else {
				sendToAll.enableSendForBroadcast();
			}
		}
	}

	/**
	 * Show user as logged off if its chat window is open.
	 * If online users are greater than 1 then enable the broadcast chat window else disable it.
	 * @param  users contains list of all online users.
	 * @return none
	 */
	private void showUserLoggedOff(String[] users) {
		for(int j = 0; j < ChatWindows.size(); j++){
			if(!Arrays.asList(users).contains(ChatWindows.get(j).getUsername())){
				ChatWindows.get(j).showLoggedOutMessage();
			}
		}
		
		if(sendToAll != null){
			if(onlineUserCount == 0){
				sendToAll.disableSendForBroadcast();
			} else {
				sendToAll.enableSendForBroadcast();
			}
		}
	}

	/**
	 * Create instances of chat window for each user selected in the online users list and store there references in ArrayList.
	 * If chat window already exists then just display the chat window.
	 * @param  user contains name of the recipient user.
	 * @return none
	 */
	private void openChatWindow(String user){
		
		boolean userNotFound = true;
		for(int i = 0; i < ChatWindows.size(); i++){
			ChatWindow tempUserChatWindow = ChatWindows.get(i);
			if(tempUserChatWindow.getUsername().equals(user)){
				tempUserChatWindow.showPage();
				userNotFound  = false;
				break;
			}
		}
		if(userNotFound == true){
			ChatWindow chatWindow = new ChatWindow(user, this);
			ChatWindows.add(chatWindow);
		}
	}

	/**
	 * Display the message in the appropriate chat window depending 
	 * upon the sender name received in the message.
	 * If the sender is the user itself then display message in chat window for the recipient name.
	 * If message is broadcast, then display message in broadcast chat window.
	 * @param  input contains message from the server.
	 * @return none
	 */
	public void displayMessageToUser(String input) {
		String[] messageArray = input.split(":");
		ChatWindow tempUserChatWindow;
		Boolean userNotFound = true;
		
		if(messageArray[3].equals(StringLiterals.SEND_TO_ALL)){
			createSendAllWindow();
			sendToAll.displayMessage(input);
		} else {
			if(messageArray[1].equals(this.username)){
				for(int i = 0; i < ChatWindows.size(); i++){
					tempUserChatWindow = ChatWindows.get(i);
					if(tempUserChatWindow.getUsername().equals(messageArray[3])){
						tempUserChatWindow.displayMessage(input);
						break;
					}
				}
			} else {
				for(int i = 0; i < ChatWindows.size(); i++){
					tempUserChatWindow = ChatWindows.get(i);
					if(tempUserChatWindow.getUsername().equals(messageArray[1])){
						tempUserChatWindow.displayMessage(input);
						userNotFound = false;
						break;
					}
				}
				if(userNotFound == true){
					ChatWindow chatWindow = new ChatWindow(messageArray[1], this);
					ChatWindows.add(chatWindow);
					chatWindow.displayMessage(input);
				}
			}
		}
	}
	
	public void sendMessage(String message){
		String tempMessage = StringLiterals.FROM + ":" + this.username + ":" + message;
		this.parent.sendMessage(tempMessage);
	}
	
	/**
	 * Returns the username of the user.
	 * @param  none.
	 * @return username
	 */
	public String getUsername(){
		return this.username;
	}
	
	/**
	 * Remove all the references of chat windows including broadcast chat window.
	 * Clear the ArrayList of chat windows.
	 * This function is called when client clicks on log out/disconnect button.
	 * @param  none.
	 * @return none.
	 * @exception IOException
	 */
	private void disconnectFromServer() throws IOException {
		for(int i = 0; i < ChatWindows.size(); i++){
			ChatWindow tempUserChatWindow = ChatWindows.get(i);
			tempUserChatWindow.hidePage();
			tempUserChatWindow = null;
		}
		ChatWindows.clear();
		
		if(sendToAll != null){
			sendToAll.hidePage();
			sendToAll = null;
		}
		
		this.parent.disconnectFromServer();	
	}
	
	/**
	 * Hides the JFrame.
	 * @param  none.
	 * @return none.
	 */
	public void hidePage(){
		this.frame.setVisible(false);
	}
}
