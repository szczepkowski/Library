package pl.igore.dao;


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import pl.igore.annotations.Address;
import pl.igore.annotations.User;

public class UserDAO extends DAO {
	public UserDAO(){}
	
	public User getByName(String name) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from User where name = :name");
			q.setString("name", name);
			User user = (User)q.uniqueResult();
			commit();
			return user;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain User named = "+ name, e);
		}
	}
	
	
	public User get(String nickname) throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from User where nickname = :nickname");
			q.setString("nickname", nickname);
			User user = (User)q.uniqueResult();
			commit();
			return user;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not obtain User named = "+ nickname, e);
		}
	}
	public User create(String name,String nickname,String password, Address address)throws AdException{
		try{
			begin();
			User user  = new User(name,nickname,password,address);
			getSession().save(user);
			commit();
			return get(nickname);
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create User name ="+ nickname, e);
		}
	}
	
	public User create(String name,String nickname, Address address)throws AdException{
		try{
			begin();
			User user  = new User(name,nickname,address);
			getSession().save(user);
			commit();
			return get(nickname);
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create User name ="+ nickname, e);
		}
	}
	public List<User>list()throws AdException{
		try{
			begin();
			Query q = getSession().createQuery("from User");
			List<User>list = q.list();
			commit();
			return list;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not get User list =", e);
		}
	}
}
