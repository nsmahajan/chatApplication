import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class ConnectionListener extends Thread{
	
	// Contains reference to object of ServerSocket.
	private ServerSocket serverSocket;
	
	// Contains reference to object of ChatServer class.
	private ChatServer chatServer;
	public Boolean serverSocketClosed = false;
	
	/**
	 * Constructor.
	 * Initializes the ChatServer object.
	 * @param parentChatServer contains reference to the parent class.
	 * @return none
	 */
	public ConnectionListener(ChatServer parentChatServer){
		this.chatServer = parentChatServer;
		this.start();
	}
	
	/**
	 * Overrides the run method of thread class. 
	 * Creates a new server socket and wait for client connections.
	 * On new client connection creates new client thread and adds thread to the ArrayList.
	 * Calls run method of newly created client thread.
	 * Handles the exception when server socket is closed or
	 * when there is an exception with input/output streams.
	 * @param none.
	 * @return none.
	 * @exception Exception, IOException
	 */
	public void run(){
		try
		{
			serverSocket = new ServerSocket(9001);
				while(true){
					if(serverSocketClosed == true)
						break;
					
					Socket tempSocket = serverSocket.accept();
					ClientThread tempClientThread = new ClientThread(tempSocket, this.chatServer);
					tempClientThread.start();
					chatServer.ClientThreads.add(tempClientThread);
				}
		}catch (Exception e){
		}finally{
			try {
				if(!serverSocket.isClosed())
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The function is called to close a server socket.
	 * Calls the close method of server socket.
	 * @param none.
	 * @return none.
	 */
	public void closeServerSocket() throws IOException{
		this.serverSocket.close();
	}
}
