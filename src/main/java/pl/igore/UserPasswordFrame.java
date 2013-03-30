package pl.igore;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import pl.igore.annotations.Address;
import pl.igore.annotations.User;
import pl.igore.dao.AdException;
import pl.igore.dao.UserDAO;

public class UserPasswordFrame extends JFrame implements ActionListener{
	private JTextField userTextField;
	private JPasswordField passTextField;
	
	public UserPasswordFrame(){
		setSize(350,200);
		setTitle("User Authentication");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)screenSize.getWidth()/4, (int)screenSize.getHeight()/4);
		
		PassPanel panel = new PassPanel();
		getContentPane().add(panel);
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets=new Insets(0,0,0,0);
		
		passTextField = new JPasswordField(10);
		userTextField = new JTextField(10);
		gc.anchor=GridBagConstraints.NORTH;
		gc.gridy=0;
		gc.gridx=0;
		//gc.gridheight=1;
		gc.insets = (new Insets(0,70,0,0));
		panel.add(userTextField,gc);
		gc.gridx=0;
		gc.gridy=1;
		gc.insets = (new Insets(0,70,90,0));
		panel.add(passTextField,gc);
		gc.insets = (new Insets(0,0,0,0));
		JButton login = new JButton("Login");
		gc.gridx=0;
		gc.gridy=2;
		panel.add(login,gc);
		gc.gridx=1;
		gc.gridy=2;
		JButton createUser = new JButton("Create User");
		panel.add(createUser,gc);
		
		CreateUserWork createUserWork = new CreateUserWork();
		createUser.addActionListener(createUserWork);
		LoginWork loginWork = new LoginWork();
		login.addActionListener(loginWork);
		login.addActionListener(this);
		
		InputMap im = login.getInputMap();
		im.put( KeyStroke.getKeyStroke( "ENTER" ), "pressed" );
		im.put( KeyStroke.getKeyStroke( "released ENTER" ), "released" );
		
	}
	private class PassPanel extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Font font = new Font("Serif",Font.BOLD,12);
			g.setFont(font);
			g.drawString("User: ", 0, 28);
			g.drawString("Password: ", 0, 45);
		}
	}
	private class CreateUserWork implements ActionListener{
		private CreateUserFrame createUserF;
		private JTextField name;
		private JTextField nickName;
		private JPasswordField password;
		private JTextField street;
		private JTextField number;
		private JTextField homeNumber;
		private boolean userMade;
		
		private class CreateUserFrame extends JFrame {
			public CreateUserFrame(){
				userMade=false;
				setTitle("Create User");
				setSize(600,400);
				
				JPanel panel = new JPanel();
				getContentPane().add(panel);
				
				name = new JTextField("Jan Kowalski",15);
				nickName = new JTextField("Kowal",15);
				password = new JPasswordField("pass",15);
				street = new JTextField("Poleska",15);
				number = new JTextField("555",5);
				homeNumber = new JTextField("1a",15);
				
				panel.setLayout(new GridBagLayout());
				
				GridBagConstraints gc = new GridBagConstraints();
				
				gc.gridy=GridBagConstraints.RELATIVE;
				gc.weighty=1;
				gc.gridx=0;
				
				panel.add(name,gc);
				panel.add(nickName,gc);
				panel.add(password,gc);
				panel.add(street,gc);
				gc.insets = new Insets(0,0,0,110);
				panel.add(number,gc);
				gc.insets = new Insets(0,0,0,0);
				panel.add(homeNumber,gc);
				
				CreateUserBWork createUserBWork = new CreateUserBWork();
				JButton createUserB = new JButton("Create");
				createUserB.addActionListener(createUserBWork);
				
				panel.add(createUserB,gc);
			}
		}
		private class CreateUserBWork implements ActionListener{
			
			public void actionPerformed(ActionEvent arg0) {
				UserDAO userD = new UserDAO();
				char[] passCh = password.getPassword();
				String passS="";
				for(int i=0;i<passCh.length;i++){
					passS +=passCh[i];
				}
				
				Address addr = new Address(street.getText(),number.getText(),homeNumber.getText());
				try {
					if(userD.get(nickName.getText())!=null ) throw new UniqueUserException() ;
					User user = userD.create(name.getText(), nickName.getText(),passS, addr);
					addr.setUser(user);
					JOptionPane.showMessageDialog(null, "BRAVO ! User is created. You are "+user.getId()+ " User");
					createUserF.setVisible(false);
				} 
				catch (AdException e) {
					e.getMessage();
				}
				catch(UniqueUserException e1){
					JOptionPane.showMessageDialog(null,e1.getMessage() );
				}
			}
		}
		public void actionPerformed(ActionEvent e) {
			createUserF = new CreateUserFrame();
			createUserF.setVisible(true);
		}	
	}
	
	private class LoginWork implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			UserDAO userD = new UserDAO();
			String userS = userTextField.getText();
			char[] passCh = passTextField.getPassword();
			String passS ="";
			for(int i=0;i<passCh.length;i++){
				passS +=passCh[i];
			}
			System.out.println(passS);
			User user = null;
			try {
				user = userD.get(userS);
			} catch (AdException e1) {
				e1.getMessage();
			}
			if (user==null) JOptionPane.showMessageDialog(null, "Wrong User");
			else {
				System.out.println(user.getPassword());
				if(!user.getPassword().equals(passS) )JOptionPane.showMessageDialog(null, "Wrong Password");
				else{
					UserFrame userFrame = new UserFrame(user);
					System.out.println(userFrame.getUser().toString());
					userFrame.setVisible(true);
				}
			}
		}

	}
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
