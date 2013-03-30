package pl.igore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import pl.igore.annotations.Address;
import pl.igore.annotations.Librarian;
import pl.igore.annotations.User;
import pl.igore.annotations.book.Author;
import pl.igore.annotations.book.Book;
import pl.igore.annotations.book.Category;
import pl.igore.annotations.lend.Lend;
import pl.igore.dao.AdException;
import pl.igore.dao.LibrarianDAO;
import pl.igore.dao.UserDAO;
import pl.igore.dao.book.AddBookDAO;
import pl.igore.dao.book.AuthorDAO;
import pl.igore.dao.book.BookDAO;
import pl.igore.dao.book.CategoryDAO;
import pl.igore.dao.lend.ActualLendDAO;
import pl.igore.dao.lend.LendDAO;

public class App {
	
	public static void main(String[] args) {	
	/*	
		loadUsers();
		loadLibrarian();
		loadAddBooks();
		loadLends();
	 */
		System.out.println( toStringBooks() );
	}

	public static void loadLibrarian(){
		LibrarianDAO libD = new LibrarianDAO();
		BufferedReader we = null;
		String linia;
		try{
			 we = new BufferedReader( new FileReader("/home/gore/Projects/library/librarian.txt") );
			 while( (linia = we.readLine())  != null) {		
				StringTokenizer t = new StringTokenizer(linia,"|");
				 String name = t.nextToken();
				 String nickname = t.nextToken();
				 Address addr = new Address(t.nextToken(),t.nextToken(),t.nextToken());
				 double salary = Double.parseDouble(t.nextToken());
				 addr.setLibrarian( libD.create(name, nickname, addr, salary)) ;
			 }
		}
		catch(FileNotFoundException e){e.printStackTrace();} 
		catch (IOException e) { e.printStackTrace(); }
		catch(AdException e){e.printStackTrace();}
		
	}
	
	public static void loadLends(){
		LendDAO lendD = new LendDAO();
		BufferedReader we = null;
		String linia;
		UserDAO userD = new UserDAO();
		BookDAO bookD = new BookDAO();
		LibrarianDAO libD = new LibrarianDAO();
		
		try{
			 we = new BufferedReader( new FileReader("/home/gore/Projects/library/lends.txt") );
			 while( (linia = we.readLine())  != null) {		
				StringTokenizer t = new StringTokenizer(linia,"|");
				User user = userD.get(t.nextToken());
				//System.out.println( user.toString() );
				Book book = bookD.get(t.nextToken());
				Lend lend = lendD.create(user, book,libD.get("Barabara"));
				System.out.println(lend.toString() );

			 }
		}
		catch(FileNotFoundException e){e.printStackTrace();} 
		catch (IOException e) { e.printStackTrace(); }
		catch(AdException e){e.printStackTrace();}
	}
	
	public static void loadUsers(){
		BufferedReader we = null;
		String linia;
		UserDAO userD = new UserDAO();
		
		try{
			 we = new BufferedReader( new FileReader("/home/gore/Projects/library/users.txt"));
			 while( (linia = we.readLine())  != null) {		
				StringTokenizer t = new StringTokenizer(linia,"|");
				String name = t.nextToken();
				String nickname = t.nextToken();
				Address addr = new Address(t.nextToken(),t.nextToken(),t.nextToken());
				addr.setUser( userD.create(name,nickname,addr) );	
			 }
		}
			catch(FileNotFoundException e){e.printStackTrace();} 
			catch (IOException e) { e.printStackTrace(); }
			catch(AdException e){e.printStackTrace();}
	}
	public static void loadAddBooks(){
		AddBookDAO addBookD= new AddBookDAO();
		LibrarianDAO libD = new LibrarianDAO();
		BookDAO bookD = new BookDAO();
		Librarian lib = null;
		Book book = null;
		BufferedReader we = null;
		String linia;
		try{
			 we = new BufferedReader( new FileReader("/home/gore/Projects/library/books.txt"));
			 linia = we.readLine();
			 lib =  libD.get("Barabara");
			 while( (linia = we.readLine())  != null) {		
				StringTokenizer t = new StringTokenizer(linia,"|");
				String title = t.nextToken();
				Category cat = createCategory(t.nextToken());
				Author author = createAuthor(t.nextToken());
				Set<Author> auth = new HashSet<Author>();
				auth.add(author);

				GregorianCalendar calendar = new GregorianCalendar();
				int d = Integer.parseInt(t.nextToken());
				int m =  Integer.parseInt(t.nextToken());
				int y =  Integer.parseInt(t.nextToken());
				calendar.set(y, m, d);
				Date date = calendar.getTime();
				book = new Book(auth,title,cat,date);
				System.out.println( addBookD.create(lib,bookD.create(book)).toString() );
			}
		}
		catch(FileNotFoundException e){e.printStackTrace();} 
		catch (IOException e) { e.printStackTrace(); }
		catch(AdException e){e.printStackTrace();}
			
	}
	public static List<Book> listBooks(){
		BookDAO bookDao = new BookDAO();
		String s = "";
		List<Book> list = null;
		try{
			list = bookDao.list();
		}
		catch(AdException e){e.printStackTrace();}
		return list;
	}
	
	public static String toStringBooks(){
		BookDAO bookDao = new BookDAO();
		String s = "";
		try{
			List<Book> list = bookDao.list();
			Iterator<Book> it = list.iterator();
			while(it.hasNext()){
				s+= ((Book)it.next()).toString() ;
				s+="\n";
			}
		}
		catch(AdException e){e.printStackTrace();}
		return s;	
	}

	private static Category createCategory(String name) throws AdException{
		CategoryDAO categoryDao = new CategoryDAO();
		
		if (!categoryDao.contains(name)){
			categoryDao.create(name);
		}
		
		return categoryDao.get(name);
	}
	
	private static Author createAuthor(String name)throws AdException{
		AuthorDAO authorDao = new AuthorDAO();
		
		if(!authorDao.contains(name)){
			authorDao.create(name);
		}
		return authorDao.get(name);
	}
}
