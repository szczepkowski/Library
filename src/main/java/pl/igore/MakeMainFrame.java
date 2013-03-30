package pl.igore;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MakeMainFrame {
	public static void main(String[] args){
		MainFrame main = new MainFrame();
		main.setVisible(true);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class MainFrame extends JFrame{
	public static final int width=900;
	public static final int height=500;
	
	public MainFrame(){
		setTitle("Library ");
		setSize(width,height);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JButton userB = new JButton("User Interface");
		userB.setPreferredSize(new Dimension(300,70));
		JButton adminB = new JButton("Admin Interface");
		adminB.setPreferredSize(new Dimension(300,70));
		panel.add(userB);
		panel.add(adminB);
		
		Container contentPane = getContentPane();
		contentPane.add(panel);
		
		UserBWork userBWork = new UserBWork();
		userB.addActionListener(userBWork);
		AdminBWork adminBWork = new AdminBWork();
		adminB.addActionListener(adminBWork);
		
	}
	private class UserBWork implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			UserPasswordFrame userPasswordFrame = new UserPasswordFrame();
			userPasswordFrame.setVisible(true);
		}
	}
	
	private class AdminBWork implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			String pass = JOptionPane.showInputDialog("What is the secret password ?");
			if( (pass==null) || (!pass.equalsIgnoreCase("java") )){
				JOptionPane.showMessageDialog(null, "Tip - What is the programming language I was written in ?");
			}
			else{
				AdminFrame adminFrame = new AdminFrame();
				adminFrame.setVisible(true);
			}
		}
	}
}
