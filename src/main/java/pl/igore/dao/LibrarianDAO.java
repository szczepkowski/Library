package pl.igore.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import pl.igore.annotations.Address;
import pl.igore.annotations.Librarian;
import pl.igore.annotations.User;

public class LibrarianDAO extends DAO{
	public LibrarianDAO(){}
	
	public Librarian get(String nickname) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Librarian where nickname = :nickname");
			q.setString("nickname", nickname);
			Librarian lib = (Librarian)q.uniqueResult();
			commit();
			return lib;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain Librarian named = "+ nickname, e);
		}
	}
	
	public Librarian create(String name,String nickname, Address address,double salary)throws AdException{
		try{
			begin();
			Librarian lib  = new Librarian(name,nickname,address,salary);
			getSession().save(lib);
			commit();
			return get(nickname);
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create Libraian name ="+ nickname, e);
		}
	}
}
