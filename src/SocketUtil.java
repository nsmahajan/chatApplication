import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class SocketUtil {
	private String username ="";
	
	// Contains reference to socket object
	private Socket socket;
	
	// Contains reference to output stream of socket
	private PrintWriter messageOut;
	
	// Contains reference to input stream of socket
    private BufferedReader messageIn;
	
    /**
	 * Constructor.
	 * @param   socket contains reference to socket object.
	 * @return none
	 */
	public SocketUtil(Socket socket) throws IOException {
		setSocket(socket);
	}

	/**
	 * Creates the output and input stream for the socket.
	 * Stores the reference of the socket object.
	 * @param   socket contains reference to socket object.
	 * @return none
	 */
	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.messageOut = new PrintWriter(this.socket.getOutputStream(), true);
		this.messageIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
	}
	
	/**
	 * Returns socket object.
	 * @param none.
	 * @return socket.
	 */
	public Socket getSocket(){
		return this.socket;
	}
	
	/**
	 * Initializes the username.
	 * @param username contains the username(String) provided by the client.
	 * @return none.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Returns username.
	 * @param none.
	 * @return username.
	 */
	public String getUsername(){
		return this.username;
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
	
	/**
	 * Writes the message to output stream.
	 * @param message String.
	 * @return message returns message(String) received in the input stream.
	 * @exception IOException
	 */
	public String getMessage() throws IOException{
		return this.messageIn.readLine();
	}
	
	/**
	 * Closes the client socket.
	 * @param none.
	 * @return none.
	 * @exception IOException
	 */
	public void closeSocket(){
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
