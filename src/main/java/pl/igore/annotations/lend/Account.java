package pl.igore.annotations.lend;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import pl.igore.annotations.User;


@Entity
@Table(name="account")
public class Account implements Serializable{
	public static final double rate=0.1; // rate for 1 day of lend
	private int id;
	private User user;
	private double debt;
	
	public Account(){
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue()
	@Column(name="account_id")
	public int getId() {
		return id;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@OneToOne(targetEntity=User.class)
	public User getUser() {
		return user;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}

	public double getDebt() {
		return debt;
	}
	@Transient
	public String getStringDebt(){
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String s = formatter.format(debt);
		return s;
	}
}
