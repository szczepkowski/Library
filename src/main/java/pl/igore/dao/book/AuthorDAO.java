package pl.igore.dao.book;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import pl.igore.annotations.book.Author;
import pl.igore.dao.AdException;
import pl.igore.dao.DAO;

public class AuthorDAO extends DAO{
	
	public AuthorDAO(){}
	
	public Author get(String name) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Author where name = :name");
			q.setString("name", name);
			Author author = (Author)q.uniqueResult();
			commit();
			return  author;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain named Author"+ name, e);
		}
	}
	
	public boolean contains(String name)throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Author where author_name = :name");
			q.setString("name", name);
			if( q.list().isEmpty() ) return false;
			commit();
			return true;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not check does Author named "+ name+"is in db", e);
		}
	}
	
	public List<Author> list() throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Author");
			List<Author> list = q.list();
			commit();
			return list;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain any Author",e);
		}	
	}
	
	public Author create(String name)throws AdException{
		try{
			begin();
			Author author = new Author(name);
			getSession().save(author);
			commit();
			return author;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create Author = "+name,e);
		}	
	}
	public void save(Author author)throws AdException{
		try{
			begin();
			getSession().update(author);
			commit();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not save Author = "+author.getName(),e);
		}
	}
	public void delete(Author author) throws AdException{
		try{
			begin();
			getSession().delete(author);
			commit();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not delete Author = "+author.getName(),e);
		}
	}
}
