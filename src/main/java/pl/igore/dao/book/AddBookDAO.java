package pl.igore.dao.book;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import pl.igore.annotations.Librarian;
import pl.igore.annotations.User;
import pl.igore.annotations.book.AddBook;
import pl.igore.annotations.book.Book;
import pl.igore.dao.AdException;
import pl.igore.dao.DAO;

public class AddBookDAO extends DAO{
	public AddBookDAO(){}
	
	public AddBook get(Book book) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from add_book where book = :book");
			q.setEntity("book", book);
			AddBook addB = (AddBook)q.uniqueResult();
			commit();
			return  addB;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain Book name ="+ book.getName(), e);
		}
	}
	
	public AddBook create(Librarian lib,Book book)throws AdException{
		try{
			begin();
			AddBook addB = new AddBook(lib,book);
			getSession().save(addB);
			commit();
			return addB;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not add book named = "+book.getName(),e);
		}	
	}
}
