import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * Main launch point of the client application.
 * @author Neha Mahajan
 *
 */
public class WPIChatClient {

	// Contains reference to object of Login Page.
	private Login login;
	
	// Contains reference to object of MessageSendRecieve class.
	private MessageSendRecieve messageSendRecieve;
	
	// Contains reference to object of OnlineUsersListUI class.
	private OnlineUsersListUI onlineUsersListUI;
	
	public static void main(String[] args) {
		WPIChatClient wpiChatClient = new WPIChatClient();
		wpiChatClient.createLoginPage();
	}

	/**
	 * Create a new login page and store its reference.
	 * @param none
	 * @return none
	 */
	private void createLoginPage(){
		this.login = new Login(this);
	}
	
	/**
	 * Create a new object of MessageSendRecieve class and store its reference.
	 * Calls method of MessageSendRecieve to send username to server. 
	 * @param username contains the username(String) entered by the client.
	 * @return none
	 */
	public void createClientSocketConnection(String username){
		if(this.messageSendRecieve == null)
			this.messageSendRecieve = new MessageSendRecieve(username, this);
		else {
			this.messageSendRecieve.sendName(username);
		}
	}
	
	/**
	 * Hides the login page after successful login.
	 * Create a new object of OnlineUsersListUI class and store its reference.
	 * Displays the OnlineUsersListUI page.
	 * Calls method of OnlineUsersListUI to display list of online users. 
	 * @param username contains the username(String) validated by the server.
	 * 		  OnlineUsers contains the list of online users(String) returned by the server.
	 * @return none
	 */
	public void displayOnlineUserUI(String OnlineUsers, String username){
		login.hideLoginPage();
		
		if(this.onlineUsersListUI == null){
			this.onlineUsersListUI = new OnlineUsersListUI(this);
		}
		this.onlineUsersListUI.updateOnlineUsersList(OnlineUsers,username);
	}

	/**
	 * Calls method of OnlineUsersListUI to display message in the chat window. 
	 * @param input contains the input message(String) received from the server(sender).
	 * @return none
	 */
	public void displayMessageToUser(String input) {
		this.onlineUsersListUI.displayMessageToUser(input);
	}
	
	/**
	 * Sends message to server. 
	 * @param message contains the message(String) typed by the client in the chat window.
	 * @return none
	 */
	public void sendMessage(String message){
		this.messageSendRecieve.sendMessage(message);
	}

	/**
	 * Displays error message in a popup if username is not unique. 
	 * @param none.
	 * @return none
	 */
	public void showErrorMessage() {
		JOptionPane.showMessageDialog(null, StringLiterals.ERROR_NEW_USER);
		login.resetUsernameTextField();
	}

	/**
	 * Sends disconnect message to server. 
	 * @param none.
	 * @return none
	 */
	public void disconnectFromServer() throws IOException {
		this.messageSendRecieve.sendMessage(StringLiterals.DISCONNECT + ":");
	}

	/**
	 * Displays the login page after successful disconnect from server.
	 * Removes the references for both MessageSendRecieve and OnlineUsersListUI class objects.
	 * @param none.
	 * @return none
	 */
	public void showLoginPage() {
		this.messageSendRecieve = null;
		this.onlineUsersListUI.hidePage();
		this.onlineUsersListUI = null;
		this.login.showPage();
	}
}

