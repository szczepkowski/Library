package pl.igore;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import pl.igore.annotations.Librarian;
import pl.igore.annotations.Time;
import pl.igore.annotations.User;
import pl.igore.annotations.book.Book;
import pl.igore.annotations.lend.Account;
import pl.igore.annotations.lend.ActualLend;
import pl.igore.annotations.lend.Lend;
import pl.igore.dao.AdException;
import pl.igore.dao.LibrarianDAO;
import pl.igore.dao.TimeDAO;
import pl.igore.dao.UserDAO;
import pl.igore.dao.book.BookDAO;
import pl.igore.dao.lend.ActualLendDAO;
import pl.igore.dao.lend.LendDAO;

public class UserFrame extends JFrame {
	private User user;
	
	public UserFrame(User user){
		this.user=user;
		this.setTitle("User Interface");
		this.setSize(900, 600);
		
		BPanel panel = new BPanel();
		panel.setUser(getUser());
		System.out.println(panel.getUser());
		this.getContentPane().add(panel);
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
}

class BPanel extends JPanel{
	public static final int delay=5000; // delay for recount the debt // normally is a one 1 day but we have 5 sek day
	private User user; // logged User
	private Date date; // actual date - can by manipulated by panel comboBox
	private JComboBox combo;
	
		
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		Font font = new Font("Serif",Font.ROMAN_BASELINE,16);
		this.setFont(font);
		g.drawString("Logged as "+user.getNickname(), 20,20);
	}
	public void setUser(User user){
		this.user=user;
	}
	
	public User getUser(){
		return user;
	}
	
	public BPanel(){
		Dimension dim = new Dimension(250,30);
		//Dimension bigDim = new Dimension(300,30);

		JButton lendBook = new JButton("1. Lend Book");
		lendBook.setPreferredSize(dim);
		JButton returnBook = new JButton("2. Return Book");
		returnBook.setPreferredSize(dim);
		JButton lendHistory = new JButton("3. Show lend history");
		lendHistory.setPreferredSize(dim);
		JButton payYourDebts = new JButton("4. Pay Your Debts");
		payYourDebts.setPreferredSize(dim);
		
		TimeDAO timeD = new TimeDAO();

		try {
			Time time = timeD.get();
			if (time==null)date=new Date();
			else{
				date = timeD.get().getLibDate();
			}
		} catch (AdException e1) {
			e1.getMessage();
		}
		
		ActionListener recountDebt = new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DAY_OF_YEAR, 1);
					date=cal.getTime();
					System.out.println(date);
					ActualLendDAO lendD = new ActualLendDAO();
					List<ActualLend> list = null;
					try {
						list = lendD.listAll();
					} catch (AdException e) {
						 e.getMessage();
					}
					Iterator<ActualLend> it = list.iterator();
					while(it.hasNext()){
						ActualLend lend = ((ActualLend)it.next());
						int leftDays = lend.getLend().getLeftLendDays();
						lend.getLend().setLeftLendDays(lend.getLend().getLeftLendDays()-1);
						if(leftDays<1){
							lend.getLend().setPenality(-leftDays*Account.rate);
							lend.getLend().getUser().getAccount().setDebt(lend.getLend().getUser().getAccount().getDebt()+Account.rate);
						}
					}
				}
		};
		final Timer t = new Timer(delay,recountDebt);
		t.start();
		
		AncestorListener aListener = new AncestorListener(){
			public void ancestorAdded(AncestorEvent arg0) {
			}
			public void ancestorMoved(AncestorEvent arg0) {
			}
			public void ancestorRemoved(AncestorEvent arg0) {
				t.stop();
				TimeDAO timeD = new TimeDAO();
				try {
					Time time = timeD.get();
					timeD.saveOrUpdate(time, date);
				} catch (AdException e1) {
					e1.getMessage();
				}
			}
		};

		this.addAncestorListener(aListener);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor=GridBagConstraints.NORTH;
		gc.gridheight=1;
		gc.gridwidth=0;
		
		gc.gridx=0;
		gc.gridy=0;
		add(lendBook,gc);
		
		gc.insets = (new Insets(25,0,0,0));
		gc.gridx=0;
		gc.gridy=1;
		add(returnBook,gc);
		
		gc.gridx=0;
		gc.gridy=2;
		add(lendHistory,gc);
				
		gc.gridx=0;
		gc.gridy=3;
		gc.insets = (new Insets(25,0,200,0));
		add(payYourDebts,gc);


		combo = new JComboBox();
		combo.addItem(new Integer(1));

		gc.gridx=0;
		gc.gridy=4;
		gc.insets = (new Insets(0,0,0,85));
		add(combo,gc);
		
		JButton timeAdder = new JButton("Add days");
		gc.gridx=1;
		gc.gridy=4;
		gc.insets = (new Insets(0,85,0,0));
		add(timeAdder,gc);
		
		LendBookWork lendWork = new LendBookWork();
		ReturnBookWork returnWork = new ReturnBookWork();
		LendHistoryWork lendHistoryWork = new LendHistoryWork();
		PayYourDebtsWork payYourDebtsWork = new PayYourDebtsWork();
		
		lendBook.addActionListener(lendWork);
		returnBook.addActionListener(returnWork);
		lendHistory.addActionListener(lendHistoryWork);
		payYourDebts.addActionListener(payYourDebtsWork);
		timeAdder.addActionListener(recountDebt);
		
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	private class LendBookWork implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			LendFrame lendF = new LendFrame(); 
			lendF.setVisible(true);
		}

		private class LendFrame extends JFrame implements ActionListener{
			String selectedItem;
			public LendFrame(){
				setTitle("Lend Book");
				setSize(900,500);
				LendPanel panel = new LendPanel();
				this.getContentPane().add(panel);
						
				JComboBox combo = new JComboBox();
				BookDAO bookD = new BookDAO();
				List<Book> list = null;
				try {
					list = bookD.list();
				} catch (AdException e) {
					e.getMessage();
				}
				Iterator<Book> it = list.iterator();
				combo.addItem("----");
				while(it.hasNext()){
					combo.addItem( ((Book)it.next()).getName() );
				}
				combo.addActionListener(this);

				
				JButton lendB = new JButton("Lend");
				
				panel.add(combo);
				panel.add(lendB);
				LendBWork lendBWork = new LendBWork();
				lendB.addActionListener(lendBWork);
			}
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
				selectedItem = (String)combo.getSelectedItem();
				System.out.println(selectedItem);				
			}
			private class LendBWork implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					LendDAO lendD = new LendDAO();
					BookDAO bookD = new BookDAO();
					LibrarianDAO libD = new LibrarianDAO();
					System.out.println(user);
					
					try {
						if(user==null){
							System.out.println("User is a null");
							 throw new AdException("Cipka");
							}
						
						Book book = bookD.get(selectedItem);
						System.out.println(book.toString());
						Librarian lib = libD.get("Barabara");
						System.out.println( user.toString()+"\n"+book.toString()+"/n"+lib.toString() );
						int answer = JOptionPane.showConfirmDialog(null, "Do You really want to lend "+selectedItem+" ? ","Lend a book",JOptionPane.YES_NO_OPTION);
						if (answer==0){
							 lendD.create(user, book, lib);
							
							JOptionPane.showMessageDialog(null, "You have lended "+selectedItem);
							LendFrame.this.setVisible(false);
						}
					} catch (AdException e1) {
						e1.getMessage();
					}
				}
			}
				
		}
			private class LendPanel extends JPanel{
				public void paintComponent(Graphics g){
					super.paintComponent(g);
					g.setColor(Color.BLACK);
					Font font = new Font("Serif", Font.BOLD, 16);
					g.setFont(font);
					String s1 = "First 30 borrow days are for free. After this period of time there will be ";
					String s2 =  "a small amount of cash added to your library debts. Thanks for visiting. ";

					g.drawString(s1,50, 200);
					g.drawString(s2, 50,225);
				}
			}
		}
	
	private class ReturnBookWork implements ActionListener{
		private JCheckBox[] boxes;
		private ReturnFrame rFrame;
		
		private class ReturnFrame extends JFrame{
			public ReturnFrame(){
				setTitle("Return Frame");
				setSize(900,600);
				
				ReturnPanel panel = new ReturnPanel();
				getContentPane().add(panel);

				ActualLendDAO lendD = new ActualLendDAO();
				List<ActualLend>list = null;
				list = lendD.listAll(user);
				panel.setLayout(new GridBagLayout());
				GridBagConstraints gc = new GridBagConstraints();

				boxes = new JCheckBox[list.size()];
				
				Iterator<ActualLend>it = list.iterator();
				int i=0;
				while(it.hasNext()){
					gc.gridy=i;
					String s = ((ActualLend)it.next()).getLend().getBook().getName();
					System.out.println( s);
					boxes[i] = new JCheckBox(s);
					panel.add(boxes[i],gc);
					i++;
				}
				gc.gridy=i++;
				JButton ret = new JButton("Return");
				ret.addActionListener(panel);
				panel.add(ret,gc);
				
			}
		}
		private class ReturnPanel extends JPanel implements ActionListener{
			public ReturnPanel(){
				
			}
			public void actionPerformed(ActionEvent arg0) {
				LendDAO lendD = new LendDAO();
				ActualLendDAO actualLendD = new ActualLendDAO();
				for(int i=0;i<boxes.length;i++){
					if(boxes[i].isSelected()){
						String s = boxes[i].getText();
						try {
							ActualLend actLend= actualLendD.get(user,s);
							actLend.getLend().setReturnDate(BPanel.this.date );
							actualLendD.delete(actLend);
							JOptionPane.showMessageDialog(null, "You have returned"+s);
							rFrame.setVisible(false);
						} catch (AdException e) {
							System.out.println( e.getMessage() );
						}
					}
				}
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			rFrame = new ReturnFrame();
			rFrame.setVisible(true);
			
		}
	}
	
	private class LendHistoryWork implements ActionListener{
		private class LendHistoryFrame extends JFrame{
			public LendHistoryFrame(){
				setTitle("Lend History");
				setSize(900,600);
				
				JPanel panel = new JPanel();
				getContentPane().add(panel);
				
				LendDAO lendD = new LendDAO();
				List<Lend> list = null;
				try {
					list = lendD.get(user);
				} catch (AdException e) {
					e.getMessage();
				}
			    Vector rowData = new Vector();
				Iterator<Lend> it = list.iterator();
				
				while(it.hasNext()){
					Vector row = new Vector ( ((Lend)it.next()).list() );
					rowData.add(row);
				}
			    
			    String[] columnNames = {"Id","Book","Lend Date","Return Date"};
			    
			    Vector columnNamesV = new Vector(Arrays.asList(columnNames));

			    JTable table = new JTable(rowData, columnNamesV);
			    this.setSize(850, 450);
			    this.add(new JScrollPane(table));
			}
		}
		public void actionPerformed(ActionEvent e) {
			LendHistoryFrame lendHF = new LendHistoryFrame();
			lendHF.setVisible(true);
		}
	}
	
	private class PayYourDebtsWork implements ActionListener{
		private DebtsFrame debtsFrame ;
		private class DebtsFrame extends JFrame{
			public DebtsFrame(){
				setTitle("Debts Frame");
				setSize(900,600);	
				JPanel panel = new JPanel();
				
				getContentPane().add(panel);
				
				LendDAO lendD = new LendDAO();
				List<Lend> list = null;
				try {
					list = lendD.get(user);
				} catch (AdException e1) {
					e1.getMessage();
				}
				
			    Vector rowData = new Vector();
				Iterator<Lend> it = list.iterator();
				
				while(it.hasNext()){
					Lend lend = (Lend)it.next();				
					Vector row = new Vector (lend.debtList());
					rowData.add(row);
				}
				
			    String[] columnNames = {"Id","Book","Lend Date","Return Date","Left Lend Days","Penality"}; 
			    Vector columnNamesV = new Vector(Arrays.asList(columnNames));

			    JTable table = new JTable(rowData, columnNamesV);
			    JScrollPane scrollPane = new JScrollPane(table);
			    scrollPane.setPreferredSize(new Dimension(890,400));
			    panel.add(scrollPane);
			    
			    JTextField payT = new JTextField();
			    JButton pay = new JButton("Pay");
			    
			    payT.setText("OVERALL DEBT = "+user.getAccount().getStringDebt());
			    payT.setEditable(false);
			    PayWork payWork = new PayWork();
			    pay.addActionListener(payWork);
			    panel.add(payT);
			    panel.add(pay);
			}
		}
		private class PayWork implements ActionListener{
			public void actionPerformed(ActionEvent arg0) {
				user.getAccount().setDebt(0.0);
				JOptionPane.showMessageDialog(null, "Congratulations !! You payed your debts. ");	
				debtsFrame.setVisible(false);
			}	
		}
		public void actionPerformed(ActionEvent e) {
			debtsFrame = new DebtsFrame();
			debtsFrame.setVisible(true);
		}
	}
}