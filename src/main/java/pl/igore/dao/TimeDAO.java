package pl.igore.dao;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import pl.igore.annotations.Time;

public class TimeDAO extends DAO{
	
	public TimeDAO(){}
	
	public void edit(Date date)throws AdException{
		try{
			begin();
			Time time = get();
			time.setLibDate(date);
			getSession().update(time);
			commit();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not edit Time ", e);
		}
	}

	public Time create(Date date)throws AdException{
		try{
			begin();
			Time time = new Time();
			time.setLibDate(date);
			getSession().save(time);
			commit();
			return get();
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not create Time ", e);
		}
	}
	
	public Time get()throws AdException{
		try{
			begin();
			Query q  = getSession().createQuery("from Time");
			Time time = (Time)q.list().get(q.list().size()-1);
			commit();
			return time;
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not get Time ", e);
		}
		catch(ArrayIndexOutOfBoundsException a){}
		return null;
	}

	public void saveOrUpdate(Time time,Date date) throws AdException {
		try{
			begin();
			if(time==null){;
				time = new Time();
				time.setLibDate(date);
				getSession().save(time);
				commit();
			}
			else{
				time.setLibDate(date);
				getSession().save(time);
				commit();
			}
		}
		catch(HibernateException e){
			rollback();
			throw new AdException("Could not update Time ", e);
		}
	}
}
