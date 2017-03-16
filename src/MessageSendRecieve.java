import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class MessageSendRecieve extends Thread{
	// Contains reference to socket object
	private Socket echoSocket;
	
	// Contains reference to output stream of socket
	private PrintWriter messageOut;
	
	// Contains reference to input stream of socket
    private BufferedReader messageIn;
    private String username;
    
    // Contains the reference to the object of WPIChatClient class.
    private WPIChatClient parent;
    private Boolean disconnect = false;
    
    /**
	 * Constructor.
	 * @param parent contains reference to WPIChatClient object.
	 * 		  username contains the username selected by the user.
	 * @return none
	 */
	public MessageSendRecieve(String username, WPIChatClient parent){
		this.username = username;
		this.parent = parent;
		this.createConnection();
	}

	/**
	 * Creates the socket, output and input stream for the socket.
	 * Stores the reference of the socket object.
	 * Stores the reference to output/input streams.
	 * @param none.
	 * @return none.
	 * @exception UnknownHostException, IOException.
	 */
	private void createConnection(){
		try{
			this.echoSocket = new Socket("localhost", 9001);
			this.messageOut = new PrintWriter(this.echoSocket.getOutputStream(), true);
			this.messageIn = new BufferedReader(new InputStreamReader(this.echoSocket.getInputStream()));
			
			sendName(this.username);
			
			this.start();
		}catch (UnknownHostException e) {
            System.err.println("Don't know about host ");
            JOptionPane.showMessageDialog(null, StringLiterals.UNABLE_TO_CONNECT);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to ");
            JOptionPane.showMessageDialog(null, StringLiterals.UNABLE_TO_CONNECT);
            System.exit(1);
        } 
	}
	
	/**
	 * Overrides the run method of thread class.
	 * Handles messages received in socket input stream.
	 * Calls functions like display message, show error message and display online users list.
	 * Handles disconnect functionality.
	 * @param none
	 * @return none
	 */
	public void run(){
		try {
			while(true){
					
					String input = this.messageIn.readLine();
					if(input == null){
						this.disconnect = true;
						break;
					}
					String[] messageArray = input.split(":");
					
					switch(messageArray[0]){
						case StringLiterals.USER_NOT_ACCEPTED:
							this.parent.showErrorMessage();
						break;
						
						case StringLiterals.FROM:
							this.parent.displayMessageToUser(input);
						break;
						
						default:
							this.parent.displayOnlineUserUI(input, this.username);	
					}
				}
			
			if(this.disconnect)
				this.disconnect();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, StringLiterals.UNABLE_TO_CONNECT);
            System.exit(1);
		}
	}
	
	/**
	 * Close the socket connection for the user.
	 * Display the login page.
	 * @param none.
	 * @return none.
	 */
	private void disconnect() throws IOException{
		this.echoSocket.close();
		this.parent.showLoginPage();
	}
	
	/**
	 * Send username to server entered by the user.
	 * @param username contains the username(String) entered by the user.
	 * @return none.
	 */
	public void sendName(String username){
		String messageToBeSent = StringLiterals.USERNAME + ":" + username;
		this.username = username;
		sendMessage(messageToBeSent);
	}
	
	/**
	 * Writes the message to output stream.
	 * @param message String.
	 * @return none.
	 */
	public void sendMessage(String message){
		this.messageOut.println(message);
		this.messageOut.flush();
	}
}
