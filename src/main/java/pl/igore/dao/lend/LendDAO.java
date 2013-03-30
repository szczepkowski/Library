package pl.igore.dao.lend;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import pl.igore.annotations.Librarian;
import pl.igore.annotations.User;
import pl.igore.annotations.book.Book;
import pl.igore.annotations.lend.Lend;
import pl.igore.dao.AdException;
import pl.igore.dao.DAO;
import pl.igore.dao.book.BookDAO;

public class LendDAO extends DAO {
	public LendDAO(){}
	
	public List<Lend> listAll() throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Lend");
			commit();
			return q.list();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain Lends list", e);
		}
	}
	
	public Lend get(Lend lend)throws AdException{
		
		begin();
		Criteria crit = getSession().createCriteria(Lend.class)
		.add(Restrictions.eq("user", lend.getUser()));
		crit.add(Restrictions.eq("book", lend.getBook()));
		crit.setMaxResults(1);

		Lend newlend = (Lend) crit.uniqueResult();
		commit();
		return newlend;
	}
	
	public List<Lend> get(User user) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Lend where user = :user");
			q.setEntity("user",user);
			List<Lend> list= q.list();
			commit();
			return list;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain Lends of User named = "+ user.getNickname(), e);
		}
	}
	
	public Lend create(User user, Book book,Librarian lib)throws AdException{
		ActualLendDAO actualLendD = new ActualLendDAO();
		try{
			begin();
			Lend lend = new Lend(user,book,lib);
			getSession().save(lend);
			commit();
			actualLendD.create(lend);
			return lend;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create User named ="+ user.getNickname(), e);
		}
	}
	public Lend delete(String bookTitle,User user)throws AdException{
		BookDAO bookD = new BookDAO();
		Book book = bookD.get(bookTitle);
		
		begin();
		Criteria crit = getSession().createCriteria(Lend.class)
		.add(Restrictions.eq("user", user));
		crit.add(Restrictions.eq("book", book));
		crit.setMaxResults(1);

		Lend lend = (Lend) crit.uniqueResult();
		getSession().delete(lend);
		commit();
		return lend;
	}
}
