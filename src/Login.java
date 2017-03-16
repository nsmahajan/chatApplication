import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.Border;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.Frame;
import java.awt.Color;
import java.awt.Font;

/**
 * 
 * @author Neha Mahajan
 *
 */
public class Login{

	private JFrame frame;
	private JTextField usernameTextField;
	
	// Contains the reference to the object of WPIChatClient class.
	private WPIChatClient parent;
	private JLabel bgLabel;
	private int pX, pY;
	
	/**
	 * Constructor.
	 * @param parent contains reference to WPIChatClient object.
	 * @return none
	 */
	public Login(WPIChatClient parent) {
		this.parent = parent;
		initialize();
	}

	/**
	 * Creates the main JFrame and adds all UI components to it.
	 * Contains function calls for close,minimize and connect button.
	 * Drag functionality for JFrame is implemented in the function.
	 * @param none
	 * @return none
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(550, 220, 254, 254);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 20, 20));
     	
		JLabel lblUsername = new JLabel(StringLiterals.USERNAME);
		lblUsername.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(90, 142, 74, 14);
		frame.getContentPane().add(lblUsername);
		
		usernameTextField = new JTextField();
		usernameTextField.setBounds(26, 167, 204, 26);
		Border border = BorderFactory.createLineBorder(Color.RED, 1);
		usernameTextField.setBorder(border);

		frame.getContentPane().add(usernameTextField);
		usernameTextField.setColumns(10);
		
		JLabel wpi_logo = new JLabel();
		wpi_logo.setBounds(26, 40, 204, 47);
		frame.getContentPane().add(wpi_logo);
		ImageIcon wpiLogoImage = new ImageIcon("images\\wpi_logo.png");
		wpi_logo.setIcon(wpiLogoImage);
		
		JButton btnConnect = new JButton(StringLiterals.CONNECT);
		btnConnect.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 11));
		btnConnect.setBorder(null);
		btnConnect.setBackground(Color.WHITE);
		btnConnect.setForeground(new Color(162, 10, 35));
		btnConnect.setFocusPainted(false);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(usernameTextField.getText().equals(""))
					JOptionPane.showMessageDialog(null, StringLiterals.ENTER_VALID_USERNAME);
				else if(usernameTextField.getText().length() > 8){
					JOptionPane.showMessageDialog(null, StringLiterals.USERNAME_LENGTH);
					usernameTextField.setText("");
				}
				else 
					parent.createClientSocketConnection(usernameTextField.getText());
			}
		});
		btnConnect.setBounds(78, 217, 89, 23);
		frame.getContentPane().add(btnConnect);
		
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
		
		bgLabel = new JLabel();
		bgLabel.setBounds(0, 0, 254, 254);
		frame.getContentPane().add(bgLabel);
		ImageIcon bgImage = new ImageIcon("images\\bg.png");
		bgLabel.setIcon(bgImage);
		frame.getContentPane().add(bgLabel);
		
		frame.setVisible(true);
	}
	
	/**
	 * Hides the JFrame of the Login Page.
	 * @param none
	 * @return none
	 */
	public void hideLoginPage(){
		frame.setVisible(false);
	}

	/**
	 * Reset the username text field.
	 * @param none
	 * @return none
	 */
	public void resetUsernameTextField() {
		usernameTextField.setText("");
	}

	/**
	 * Displays the JFrame of the Login Page.
	 * @param none
	 * @return none
	 */
	public void showPage() {
		frame.setVisible(true);
	}
}
