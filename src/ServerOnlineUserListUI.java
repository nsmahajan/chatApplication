import java.awt.Frame;
import java.awt.TextArea;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.awt.Color;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class ServerOnlineUserListUI {

	private JFrame frame;
	private TextArea usersListTextArea;
	private int pX, pY;
	/**
	 * Create the application.
	 */
	public ServerOnlineUserListUI() {
		initialize();
	}

	/**
	 * Creates the JFrame and adds all UI components to it.
	 * Contains function call for minimize button.
	 * Drag functionality for JFrame is implemented in the function.
	 * @param none
	 * @return none
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(550, 220, 258, 374);
		frame.setUndecorated(true);
		frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 10, 10));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton closeBtn = new JButton();
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hidePage();
			}
		});
		
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
		
		JLabel lblNewLabel = new JLabel(StringLiterals.ONLINE_USERNAMES);
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(15, 60, 120, 14);
		frame.getContentPane().add(lblNewLabel);
		
		usersListTextArea = new TextArea("",2,50, TextArea.SCROLLBARS_VERTICAL_ONLY);
		usersListTextArea.setEditable(false);
		usersListTextArea.setBounds(10, 85, 234, 275);
		frame.getContentPane().add(usersListTextArea);
		
		JLabel listUI = new JLabel();
		ImageIcon listUIbgImage = new ImageIcon("images\\online_user_bg.png");
		listUI.setIcon(listUIbgImage);
		listUI.setBounds(0, 0, 258, 374);
		frame.getContentPane().add(listUI);
		
		frame.setVisible(true);
	}

	/**
	 * Displays the updated list of online users.
	 * @param userSockets contains the reference to ArrayList of user sockets created in ChatServer class.
	 * @return none
	 */
	public void updateList(ArrayList<SocketUtil> userSockets){
		if(userSockets.size() == 0){
			usersListTextArea.setText(StringLiterals.SERVER_NO_USER);
		}
		else{
			String temp = "";
			for(int i = 0; i < userSockets.size(); i++){
				SocketUtil tempToUser = userSockets.get(i);
				temp += tempToUser.getUsername() + "\n";
			}
			
			usersListTextArea.setText(temp);
		}
	}
	
	/**
	 * Hides the JFrame.
	 * @param none.
	 * @return none
	 */
	public void hidePage() {
		this.frame.setVisible(false);
	}

	/**
	 * Displays the JFrame.
	 * @param none.
	 * @return none
	 */
	public void showPage() {
		this.frame.setVisible(true);
	}
}
