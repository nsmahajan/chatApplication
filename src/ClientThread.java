import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class ClientThread extends Thread{

	// Contains the reference to client socket object.
    private SocketUtil SocketUtil;
	private boolean disconnect = false;
	
	// Contains reference to object of ChatServer class.
	private ChatServer chatServer;
	
	/**
	 * Constructor.
	 * Initializes the ChatServer object.
	 * Creates a new SocketUtil object with reference to socket object created for the client
	 * by the ConnectionListener class.
	 * @param   socket contains reference to socket object.
	 * 			parentChatServer contains reference to the parent class.
	 * @return none
	 */
	public ClientThread(Socket socket, ChatServer parentChatServer) throws Exception{
		this.chatServer = parentChatServer;
		SocketUtil = new SocketUtil(socket);
	}
	
	/**
	 * Overrides the run method of thread class.
	 * Handles messages received in socket input stream.
	 * Adds socket to ArrayList of Sockets.
	 * Calls functions like broadcast and send message.
	 * Handles client disconnect.
	 * @param none
	 * @return none
	 */
	public void run(){
		try{
			while(true){
				String input = SocketUtil.getMessage();
				String[] outputMessage = input.split(":");
				
				switch(outputMessage[0]){
					case StringLiterals.USERNAME:
						if(checkIfUsernameExists(outputMessage[1]) == true){
							SocketUtil.sendMessage(StringLiterals.USER_NOT_ACCEPTED + ":");
						} else {
							SocketUtil.setUsername(outputMessage[1]);
							chatServer.UserSockets.add(SocketUtil);
							broadcastOnlineUsers();
						}
					break;
					
					case StringLiterals.FROM:
						sendMessageToUser(input);
					break;
					
					case StringLiterals.DISCONNECT:
						this.disconnect  = true;
					break;
				}
				if(this.disconnect)
					break;
			}
			if(this.disconnect)
				disconnectClient();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Closes the client socket.
	 * Removes socket entry from ArrayList.
	 * Updates all other online users.
	 * Initiates client thread removal.
	 * @param none
	 * @return none
	 */
	private void disconnectClient() {
		SocketUtil.closeSocket();
		chatServer.UserSockets.remove(SocketUtil);
		try {
			broadcastOnlineUsers();
			this.chatServer.removeClientThread(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the message back to the sender to be displayed in the chat window.
	 * @param input contains the messages written by the client.
	 * @return none
	 */
	private void sendMessageToFromUser(String input) {
		SocketUtil.sendMessage(input);
	}

	/**
	 * The function validates the username to check if it is unique.
	 * It checks in the ArrayList of sockets and if username does not exists returns false.
	 * If ArrayList contains username then return true.
	 * @param username contains the username(String) provided by the client.
	 * @return boolean
	 */
	private Boolean checkIfUsernameExists(String username){
		SocketUtil tempUser;
		
		for(int i = 0; i < chatServer.UserSockets.size(); i++){
			tempUser = chatServer.UserSockets.get(i);
			if(tempUser.getUsername().equals(username))
				return true;
		}
		
		return false;
	}
	
	/**
	 * When a new client connects and username is accepted this function is called to
	 * inform all the other online users about the newly connected clients.
	 * The function access all the user sockets from the ArrayList and writes 
	 * the list of online users as string to the output streams.
	 * It also calls method of online user list UI to update the list.
	 * @param none
	 * @return none
	 */
	private void broadcastOnlineUsers() throws IOException{
		if(chatServer.serverOnlineUserListUI != null){
			chatServer.serverOnlineUserListUI.updateList(chatServer.UserSockets);
		}
		
		String tempString = StringLiterals.ONLINE_USERNAMES + ":";
		SocketUtil tempUser;
		
		for(int i = 0; i < chatServer.UserSockets.size(); i++){
			tempUser = chatServer.UserSockets.get(i);
			tempString += tempUser.getUsername();
			if(i < chatServer.UserSockets.size() - 1)
				tempString += ":";
		}
		
		for(int i = 0; i < chatServer.UserSockets.size(); i++){
			tempUser = chatServer.UserSockets.get(i);
			tempUser.sendMessage(tempString);
		}
	}
	
	/**
	 * Handles the forwarding of the messages.
	 * Sends message to all users including the sender if it is broadcast message.
	 * Sends message to sender and receiver only if a whisper message.
	 * @param inputMessage contains the message(String) from the sender.
	 * @return none
	 */
	private void sendMessageToUser(String inputMesage) throws IOException{
		String[] messageArray = inputMesage.split(":");
		SocketUtil tempToUser;
		
		if(messageArray[3].equals(StringLiterals.SEND_TO_ALL)){
			for(int i = 0; i < chatServer.UserSockets.size(); i++){
				tempToUser = chatServer.UserSockets.get(i);
				tempToUser.sendMessage(inputMesage);
			}
		} else {
			sendMessageToFromUser(inputMesage);
			for(int i = 0; i < chatServer.UserSockets.size(); i++){
				tempToUser = chatServer.UserSockets.get(i);
				if(tempToUser.getUsername().equals(messageArray[3])){
					tempToUser.sendMessage(inputMesage);
					break;
				}
			}
		}
	}
}

