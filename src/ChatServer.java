import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.Font;
import java.io.IOException;
import java.awt.event.WindowAdapter;

/**
 * Main launch point of the server application.
 * @author Neha Mahajan.
 */
public class ChatServer {

	private JFrame frame;
	private JLabel lblStartServer;
	private JButton btnOk,listUsersBtn;
	
	// ArrayList to contain client sockets is created.
	public ArrayList<SocketUtil> UserSockets = new ArrayList<SocketUtil>();
	
	//ArrayList to contain client threads is created.
	public ArrayList<ClientThread> ClientThreads = new ArrayList<ClientThread>();
	private int pX, pY;
	
	// Contains the reference to online users list UI object.
	public ServerOnlineUserListUI serverOnlineUserListUI = null;
	
	// Contains the reference to ConnectionListener object.
	private ConnectionListener connectionListener;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatServer window = new ChatServer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor.
	 * Calls method to draw UI components.
	 * @param none
	 * @return none
	 */
	public ChatServer() {
		initialize();
	}

	/**
	 * Creates the main JFrame and adds all UI components to it.
	 * Contains function calls for close,minimize,start server and list online users button.
	 * Drag functionality for JFrame is implemented in the function.
	 * @param none
	 * @return none
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				try {
					if(connectionListener != null){
						connectionListener.serverSocketClosed = true;
						connectionListener.closeServerSocket();
					}
					System.exit(0);
				} catch (IOException e) {
				}
			}
		});
		frame.setBounds(550, 220, 254, 254);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);	
		frame.setUndecorated(true);
		
		frame.addComponentListener(new ComponentAdapter() {
            @Override
             public void componentResized(ComponentEvent e) {
                 frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 20, 20));
             }
         });
		
		lblStartServer = new JLabel(StringLiterals.START_SERVER,SwingConstants.CENTER);
		lblStartServer.setFont(new Font("Segoe UI Symbol", Font.BOLD, 13));
		lblStartServer.setForeground(Color.WHITE);
		lblStartServer.setBounds(42, 190, 163, 14);
		frame.getContentPane().add(lblStartServer);
		
		btnOk = new JButton(StringLiterals.OK);
		btnOk.setBorder(null);
		btnOk.setBackground(Color.WHITE);
		btnOk.setForeground(new Color(162, 10, 35));
		btnOk.setFocusPainted(false);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnOk.setVisible(false);
				listUsersBtn.setVisible(true);
				lblStartServer.setText(StringLiterals.SERVER_STARTED);
				startServer();
			}
		});
		btnOk.setBounds(78, 217, 89, 23);
		frame.getContentPane().add(btnOk);
		
		listUsersBtn = new JButton(StringLiterals.SERVER_ONLINE_USERS_BUTTON);
		listUsersBtn.setVisible(false);
		listUsersBtn.setBorder(null);
		listUsersBtn.setBackground(Color.WHITE);
		listUsersBtn.setForeground(new Color(162, 10, 35));
		listUsersBtn.setFocusPainted(false);
		listUsersBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(serverOnlineUserListUI == null){
					serverOnlineUserListUI = new ServerOnlineUserListUI();
				}
				serverOnlineUserListUI.updateList(UserSockets);
				serverOnlineUserListUI.showPage();
			}
		});
		listUsersBtn.setBounds(58, 217, 129, 23);
		frame.getContentPane().add(listUsersBtn);
		
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
				try {
					if(connectionListener != null){
						connectionListener.serverSocketClosed = true;
						connectionListener.closeServerSocket();
					}
				} catch (IOException e) {
				}
				WindowEvent closingEvent = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
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
		panel.setBounds(0, 0, 193, 30);
		panel.setOpaque(false);
		
		frame.getContentPane().add(panel);
		
		JLabel server_logo = new JLabel();
		server_logo.setBounds(85, 100, 81, 74);
		frame.getContentPane().add(server_logo);
		ImageIcon serverImage = new ImageIcon("images\\server_logo.png");
		server_logo.setIcon(serverImage);
		
		JLabel wpi_logo = new JLabel();
		wpi_logo.setBounds(26, 40, 204, 47);
		frame.getContentPane().add(wpi_logo);
		ImageIcon wpiLogoImage = new ImageIcon("images\\wpi_logo.png");
		wpi_logo.setIcon(wpiLogoImage);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(0, 0, 254, 254);
		frame.getContentPane().add(lblNewLabel);
		ImageIcon bgImage = new ImageIcon("images\\bg.png");
		lblNewLabel.setIcon(bgImage);
	}
	
	/**
	 * The function creates an object of ConnectionListener class.
	 * ConnectionListener class creates a new server socket.
	 * Stores the reference of object in variable connectionListener.
	 * @param none
	 * @return none
	 */
	private void startServer(){
		connectionListener = new ConnectionListener(this);
	}

	/**
	 * This function is called when a client disconnects from server.
	 * It removes the disconnected client thread from the ArrayList.
	 * @param clientThread contains the reference to disconnected client thread.
	 * @return none
	 */
	public void removeClientThread(ClientThread clientThread) {
		this.ClientThreads.remove(clientThread);
	}
}
