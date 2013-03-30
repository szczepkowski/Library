package pl.igore;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import pl.igore.annotations.book.Author;
import pl.igore.annotations.book.Book;
import pl.igore.annotations.book.Category;
import pl.igore.dao.AdException;
import pl.igore.dao.book.AuthorDAO;
import pl.igore.dao.book.BookDAO;
import pl.igore.dao.book.CategoryDAO;

class AdminFrame extends JFrame   {
	public static final int width=900;
	public static final int height=500;
	
	public AdminFrame(){
		setTitle("Library - Admin Interface");
		setSize(width,height);
		
		ButtonsPanel panel = new ButtonsPanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
	}

}
class ButtonsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public ButtonsPanel() {

		JButton createBook = new JButton("1. Create Book");
		JButton readBooks = new JButton("2. Show Books");
		JButton updateBook = new JButton("3. Update Book");
		JButton deleteBook = new JButton("4. Delete Book");
		
		GridBagLayout grid = new GridBagLayout();
		setLayout(grid);
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridy=GridBagConstraints.RELATIVE;
		gc.weighty=1;
		gc.gridx=0;

		add(createBook,gc);
		add(readBooks,gc);
		add(updateBook,gc);
		add(deleteBook,gc);
		
		CreateBookButtonWork create = new CreateBookButtonWork();
		ReadBookButtonWork read = new ReadBookButtonWork();
		UpdateBookButtonWork update = new UpdateBookButtonWork();
		DeleteBookButtonWork delete = new DeleteBookButtonWork();
		
		createBook.addActionListener(create);
		readBooks.addActionListener(read);
		updateBook.addActionListener(update);
		deleteBook.addActionListener(delete);
	}

	
	private class SubMenu extends JFrame implements ActionListener {
		public static final int width=800;
		public static final int height=350;
		
		public SubMenu(){
			setSize(width,height);
		}

		public void actionPerformed(ActionEvent arg0) {
			this.setVisible(false);
		}

	}
	
	private class CreateBookButtonWork implements ActionListener{
		SubMenu menu;
		
		public void actionPerformed(ActionEvent arg0) {
			menu = new SubMenu();
			Container contentPane = menu.getContentPane();
			NewPanel panel = new NewPanel();
			contentPane.add(panel);
			menu.setTitle("Create Book");
			menu.setVisible(true);
		}
		private class NewPanel extends JPanel {
			JTextField textAuthors;
			JTextField textName;
			JTextField textCategory;
			JTextField textCreateDate;
			
			public NewPanel(){

			 textAuthors = new JTextField("Jan Kowalski",30);
			 textName = new JTextField("Moje zmagania z JAVA",30);
			 textCategory = new JTextField("Nauka",30);
			 textCreateDate = new JTextField("10-10-2012",30);
		   	JButton addBook= new JButton("Add Book"); 
			
			GridBagLayout grid = new GridBagLayout();
			setLayout(grid);
			
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridy=GridBagConstraints.RELATIVE;
			gc.weighty=1;
			gc.gridx=0;
			
			add(textAuthors,gc);
			add(textName,gc);
			add(textCategory,gc);
			add(textCreateDate,gc);
			add(addBook,gc);
			
			AddBookButtonWork addWork = new AddBookButtonWork();
			addBook.addActionListener(addWork);
			addBook.addActionListener(menu);
			}
		
			private class AddBookButtonWork implements ActionListener{
				private String name;
				private Category cat;
				private Set<Author> authors = new HashSet<Author>();
				private Date date;
				
				public void actionPerformed(ActionEvent e) {
					BookDAO bookD = new BookDAO();
					CategoryDAO catD = new CategoryDAO();
					AuthorDAO authD = new AuthorDAO();
					
					name = textName.getText();
					
					String catS = textCategory.getText();
					try {
						if ( !catD.contains(catS) ) catD.create(catS);
						cat = catD.get(catS);			
					} catch (AdException e2) {
						e2.getMessage();
					}
					
					Set<Author> authors = new HashSet<Author>();
					String s = textAuthors.getText();
					StringTokenizer t = new StringTokenizer(s,",");
					while(t.hasMoreTokens()){
						Author author = new Author(t.nextToken());
						authors.add(author);
					}

						Iterator<Author> it = authors.iterator();
						while(it.hasNext()){
							String authS=((Author)it.next()).getName();	
							try {
								if (!authD.contains(authS)) authD.create(authS);
								this.authors.add(authD.get(authS));
							} catch (AdException e1) {
								e1.getMessage();
							}
						}		
					s = textCreateDate.getText();
					t = new StringTokenizer(s,"-");
					GregorianCalendar calendar = new GregorianCalendar();
					int d = Integer.parseInt(t.nextToken());
					int m =  Integer.parseInt(t.nextToken());
					int y =  Integer.parseInt(t.nextToken());
					calendar.set(y, m, d);
					date = calendar.getTime();
					
					try {
						bookD.create(name, cat, this.authors , date);
					} catch (AdException e1) {
						e1.printStackTrace();
					}	
				}	
			}
		}
	}
	
		private class ReadBookButtonWork implements ActionListener{
			public static final int y=15;
			
			public void actionPerformed(ActionEvent arg0) {
				SubMenu menu = new SubMenu();
				menu.setTitle("Show Books");
				menu.setVisible(true);
				
				BookDAO bookD = new BookDAO();
				List<Book> list = null;
				try {
					list = bookD.list();
				} catch (AdException e) {
					e.getMessage();
				}
			    Vector rowData = new Vector();
				Iterator<Book> it = list.iterator();
				
				while(it.hasNext()){
					Vector row = new Vector ( ((Book)it.next()).list() );
					rowData.add(row);
				}
			    
			    String[] columnNames = {"Title","Author","Category","Create Date"};
			    
			    Vector columnNamesV = new Vector(Arrays.asList(columnNames));

			    JTable table = new JTable(rowData, columnNamesV);
			    menu.setSize(850, 450);
			    menu.add(new JScrollPane(table));
			    menu.setVisible(true);
			}

		}
		
		private class UpdateBookButtonWork implements ActionListener{
			private JTextField textAuthors;
			private JTextField textName;
			private JTextField textCategory;
			private JTextField textCreateDate;
			private UpdateMenu menu;
			private BookDAO bookD;
			
			private class UpdateMenu extends JFrame implements ActionListener{
				public UpdateMenu(){
					this.setSize(850, 400);				}
				
				public void actionPerformed(ActionEvent e) {
					JComboBox combo = (JComboBox)e.getSource();
					String s = (String)combo.getSelectedItem();
					System.out.println(s);
					BookDAO bookD = new BookDAO();
					Book book = null;
					try {
						book = bookD.get(s);
					} catch (AdException e1) {
						e1.getMessage();
					}
					if(s!="-----"){
						textAuthors.setText(book.toStringAuthors());
						textName.setText(book.getName());
						textCategory.setText(book.getCategory().getName());
						textCreateDate.setText(book.getStringDate());
					}
				}
			}
			
			public void actionPerformed(ActionEvent arg0) {
				UpdateMenu menu = new UpdateMenu();
				this.menu = menu;
				menu.setTitle("Update Book");
				menu.setVisible(true);
				
				BookDAO bookD = new BookDAO();
				this.bookD= bookD; 
				List<Book> list = null;
				try {
					list = bookD.list();
				} catch (AdException e) {
					e.getMessage();
				}
				
				JComboBox titles = new JComboBox();
				titles.addActionListener(menu);
				Iterator<Book> it = list.iterator();
				titles.addItem("-----");
				while(it.hasNext()){
					titles.addItem(((Book)it.next()).getName() );
				}
				
				JPanel panel = new JPanel();
				Container pane = menu.getContentPane();
				pane.add(panel);
				
				GridBagLayout grid = new GridBagLayout();
				panel.setLayout(grid);
				
				GridBagConstraints gc = new GridBagConstraints();
				gc.gridx=GridBagConstraints.RELATIVE;
				gc.gridy=GridBagConstraints.RELATIVE;
				gc.weighty=1;
				gc.weightx=1;
				gc.gridx=1;
				gc.gridheight=2;
				gc.gridwidth=2;
				
				panel.add(titles,gc);
				panel.add( textAuthors = new JTextField("",30),gc );
				panel.add(  textName = new JTextField("",30),gc );
				panel.add(  textCategory = new JTextField("",30) ,gc);
				panel.add(  textCreateDate = new JTextField("",30) ,gc);
				JButton save= new JButton("save");
				panel.add(save,gc);
				SaveButtonWork saveWork = new SaveButtonWork();
				save.addActionListener(saveWork);
			}	
				private class SaveButtonWork implements ActionListener{

					public void actionPerformed(ActionEvent e) {
						menu.setVisible(false);
						Book book = null;
						try {
							book = bookD.get(textName.getText());
							bookD.save(book,textName.getText(), textCategory.getText(), textAuthors.getText(), textCreateDate.getText());
						}
						catch (AdException e1) {
							e1.printStackTrace();
						}
					}			
				}
		}
		private class DeleteBookButtonWork implements ActionListener{
			String selectedItem;
			DeleteFrame deleteFrame;
			
			private class DeleteFrame extends JFrame implements ActionListener{
				
				public DeleteFrame(){
					setSize(800,450);
					setTitle("Delete Book");
				}
				public void actionPerformed(ActionEvent e) {
					JComboBox combo = (JComboBox)e.getSource();
					selectedItem = (String)combo.getSelectedItem();
				}
			}
	
			public void actionPerformed(ActionEvent arg0) {
				deleteFrame = new DeleteFrame();
				deleteFrame.setVisible(true);
				
				Container pane = deleteFrame.getContentPane();
				JPanel panel = new JPanel();
				pane.add(panel);
				
				BookDAO bookD = new BookDAO();

				List<Book> list = null;
				try {
					list = bookD.list();
				} catch (AdException e) {
					e.getMessage();
				}
				
				JComboBox titles = new JComboBox();
				Iterator<Book> it = list.iterator();
				titles.addItem("-----");
				while(it.hasNext()){
					titles.addItem(((Book)it.next()).getName() );
				}
				
				panel.add(titles);
				JButton delete = new JButton("Delete");
				panel.add(delete);
				
				DeleteButtonWork deleteWork = new DeleteButtonWork();
				titles.addActionListener(deleteFrame);
				delete.addActionListener(deleteWork);
	
			}
			private class DeleteButtonWork implements ActionListener{

				public void actionPerformed(ActionEvent arg0) {
					int i = JOptionPane.showConfirmDialog(null,"Are You sure to want delete this Book ?", "Delete Book ?", JOptionPane.YES_NO_OPTION);
					if(i==0){
						BookDAO bookD = new BookDAO();
						try {
							bookD.delete(selectedItem);
						} catch (AdException e) {
							e.getMessage();
						}
						deleteFrame.setVisible(false);
					}
				}
			}
		}
	}

