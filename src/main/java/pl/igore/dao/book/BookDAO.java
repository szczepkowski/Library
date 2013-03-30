package pl.igore.dao.book;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import pl.igore.annotations.book.Author;
import pl.igore.annotations.book.Book;
import pl.igore.annotations.book.Category;
import pl.igore.dao.AdException;
import pl.igore.dao.DAO;

public class BookDAO extends DAO {
	
	public BookDAO(){}
	
	public Book get(String name) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Book where name = :name");
			q.setString("name", name);
			Book book = (Book)q.uniqueResult();
			commit();
			return  book;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain Book name ="+ name, e);
		}
	}
	
	public boolean contains(String name)throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Book where name = :name");
			q.setString("name", name);
			if( q.list().isEmpty() ) return false;
			return true;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not check does Book named "+ name+"is in db", e);
		}
	}
	
	public List<Book> list() throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Book");
			List<Book> list = q.list();
			commit();
			return list;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain any Book",e);
		}	
	}
	
	public Book create(Book book)throws AdException{
		try{
			begin();
			getSession().save(book);
			commit();
			return get(book.getName());
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create book = "+book.getName(),e);
		}	
	}
	
	public Book create(String nameS,String categoryS, String authorsS,String dateS)throws AdException{
		AuthorDAO authD = new AuthorDAO();
		CategoryDAO catD = new CategoryDAO();
		
		Category cat = null;
		Set<Author>authors = new HashSet<Author>(); 
		
		String s = authorsS;	
		StringTokenizer t = new StringTokenizer(s,",");		

		while(t.hasMoreTokens()){
			String authS=t.nextToken();
			try {
				if (!authD.contains(authS)) authD.create(authS);
				authors.add(authD.get(authS));
			} catch (AdException e1) {
				e1.getMessage();
			}
		}	
		GregorianCalendar calendar = new GregorianCalendar();
		Date date = new Date();
		s = dateS;
		t = new StringTokenizer(s,"-.");
		int d = Integer.parseInt(t.nextToken());
		int m =  Integer.parseInt(t.nextToken());
		int y =  Integer.parseInt(t.nextToken());
		
		calendar.set(y, m, d);
		date = calendar.getTime();
		
			Book book = null;
		try{
			if(!catD.contains(categoryS)){
				catD.create(categoryS);
			}
			begin();
			cat = catD.get(categoryS);
			book =  new Book(authors,nameS,cat,date);
			getSession().save(book);
			if (!getSession().beginTransaction().wasCommitted()){
				commit();
			}
			return get(book.getName());
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create book = "+nameS,e);
		}	
		catch(AdException e1){e1.getMessage();}
		return book;
	}
	
	public Book create(String name,Category category, Set<Author> authors,Date date)throws AdException{
		try{
			begin();
			Book book = new Book(authors,name,category,date);
			getSession().save(book);
			commit();
			return book;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create book = "+name,e);
		}	
	}
	
	
	public void save(Book book,String nameS,String categoryS, String authorsS,String dateS)throws AdException{
		AuthorDAO authD = new AuthorDAO();
		CategoryDAO catD = new CategoryDAO();
		
		Category cat = null;
		Set<Author>authors = new HashSet<Author>(); 
		
		String s = authorsS;	
		StringTokenizer t = new StringTokenizer(s,",");		

		while(t.hasMoreTokens()){
			String authS=t.nextToken();
			try {
				if (!authD.contains(authS)) authD.create(authS);
				authors.add(authD.get(authS));
			} catch (AdException e1) {
				e1.getMessage();
			}
		}	
		GregorianCalendar calendar = new GregorianCalendar();
		Date date = new Date();
		s = dateS;
		t = new StringTokenizer(s,"-.");
		int d = Integer.parseInt(t.nextToken());
		int m =  Integer.parseInt(t.nextToken());
		int y =  Integer.parseInt(t.nextToken());
		
		calendar.set(y, m, d);
		date = calendar.getTime();
		
		try{
			if(!catD.contains(categoryS)){
				catD.create(categoryS);
			}
			begin();
			cat = catD.get(categoryS);
			book.setName(nameS);
			book.setCategory(cat);
			book.setAuthors(authors);
			book.setCreateDate(date);

			getSession().update(book);
			if (!getSession().beginTransaction().wasCommitted()){
				commit();
			}
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not update book = "+nameS,e);
		}	
		catch(AdException e1){e1.getMessage();}
		
	}
	
	public void delete(String name) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Book where name=:name");
			q.setString("name",name);
			Book book = (Book) q.uniqueResult();
			getSession().delete(book);
			commit();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not delete Book = "+name,e);
		}
	}

}
