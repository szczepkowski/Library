package pl.igore.dao.book;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import pl.igore.annotations.book.Category;
import pl.igore.dao.AdException;
import pl.igore.dao.DAO;

public class CategoryDAO extends DAO{
	public CategoryDAO(){}
	
	public Category get(String name) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Category where name = :name");
			q.setString("name", name);
			Category category = (Category)q.uniqueResult();
			commit();
			return category;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain category named "+ name, e);
		}
	}
	public boolean contains(String name)throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from Category where name = :name");
			q.setString("name", name);
			if( q.list().isEmpty() ) return false;
			return true;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not check does category named "+ name+"is in db", e);
		}
	}
	
	public List<Category> list() throws AdException {
		try{
			begin();
			Query q = getSession().createQuery("from Category");
			List<Category> list = q.list();
			commit();
			return list;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not list the catogories",e);
		}
	}
	public Category create(String title) throws AdException {
		try{
			begin();
			Category cat = new Category(title);
			getSession().save(cat);
			commit();
			return cat;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create the category",e);
		}
	}
	
	public void save(Category category) throws AdException{
		try{
			begin();
			getSession().update(category);
			commit();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not save catogory",e);
		}
	}
	
	public void delete(Category category) throws AdException {
		try{
			begin();
			getSession().delete(category);
			commit();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not delete the catogory",e);
		}
	}
}