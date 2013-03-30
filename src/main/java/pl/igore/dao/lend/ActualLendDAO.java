package pl.igore.dao.lend;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import pl.igore.annotations.User;
import pl.igore.annotations.book.Book;
import pl.igore.annotations.lend.ActualLend;
import pl.igore.annotations.lend.Lend;
import pl.igore.dao.AdException;
import pl.igore.dao.DAO;
import pl.igore.dao.book.BookDAO;

public class ActualLendDAO extends DAO {
	public ActualLendDAO(){}
	
	public ActualLend create(Lend lend) throws AdException{
		try{
			begin();
			ActualLend lendH = new ActualLend(lend);
			System.out.println("im here1");
			getSession().save(lendH);
			System.out.println("im here2");
			commit();
			System.out.println("im here3");
			return lendH;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create ActualLend  = "+ lend.toString(), e);
		}
	}
	
	public List<ActualLend> listAll() throws AdException { 
		try{
			begin();
			Query q = getSession().createQuery("from ActualLend");
			commit();
			return q.list();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain lend List", e);
		}
	}
	
	public List <ActualLend> listAll(User user) { 
		Criteria crit = getSession().createCriteria(ActualLend.class);
		Criteria lendCrit = crit.createCriteria("lend");
		lendCrit.add(Restrictions.eq("user", user));	
		return  (List<ActualLend>) crit.list();
	}
	
	public ActualLend get(User user,String title) { 
		BookDAO bookD = new BookDAO();
		Book book = null;
		try {
			book = bookD.get(title);
		} catch (AdException e) {
			e.getMessage();
		}
		Criteria crit = getSession().createCriteria(ActualLend.class);
		Criteria lendCrit = crit.createCriteria("lend");
		lendCrit.add(Restrictions.eq("user", user));	
		lendCrit.add(Restrictions.eq("book",book));
		return  (ActualLend) crit.uniqueResult();
	}
	
	public void delete(ActualLend lend)throws AdException{
		LendDAO lendD = new LendDAO();
		
		try{
			begin();
			getSession().delete(lend);
			commit();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not delete ActualLend = "+ lend.toString(), e);
		}
	}
}
