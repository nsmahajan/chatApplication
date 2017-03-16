import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * 
 * @author Neha Mahajan
 *
 */
public class OnlineUserCustomButton extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel usernameText;
	
	/**
	 * Constructor.
	 * Creates a custom UI button for a online user to be displayed in OnlineUsersListUI page. 
	 * @param username contains the username(String) for a online user.
	 * @return none
	 */
	public OnlineUserCustomButton(String username, int x, int y){
		this.setLayout(null);
		this.setPreferredSize(new Dimension(225,36));
		this.setLocation(x,y);
		
		usernameText = new JLabel(username);
		usernameText.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
		usernameText.setForeground(new Color(162, 10, 35));
		usernameText.setBounds(10, 0, 190, 36);
		this.add(usernameText);
		
		JLabel onlineIcon = new JLabel();
		onlineIcon.setBounds(190, 10, 17, 17);
		ImageIcon onlineIconImage = new ImageIcon("images\\online.png");
		onlineIcon.setIcon(onlineIconImage);
		this.add(onlineIcon);
		
		JLabel bgIcon = new JLabel();
		bgIcon.setBounds(0, 0, 225, 36);
		ImageIcon bgIconImage = new ImageIcon("images\\online_button_bg.png");
		bgIcon.setIcon(bgIconImage);
		this.add(bgIcon);
	}

	/**
	 * Returns the username associated with the button.
	 * @param none.
	 * @return username(String)
	 */
	public String getUsername(){
		return this.usernameText.getText();
	}
}
