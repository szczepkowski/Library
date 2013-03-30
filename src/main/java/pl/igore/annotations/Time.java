package pl.igore.annotations;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="time")
public class Time {
	private int id;
	private Date libDate;
	
	public Time(){}
	
	public void setId(int id) {
		this.id = id;
	}
	@Id
	@GeneratedValue
	@Column(name="time_id")
	public int getId() {
		return id;
	}
	public void setLibDate(Date libDate) {
		this.libDate = libDate;
	}
	public Date getLibDate() {
		return libDate;
	}
}
