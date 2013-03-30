package pl.igore.annotations.lend;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import pl.igore.annotations.Librarian;
import pl.igore.annotations.User;
import pl.igore.annotations.book.Book;
@Entity
@Table(name="lend")
public class Lend implements Serializable,Cloneable{
	public static final int LENDDAYS=30;
	
	private int id;
	private User user;
	private Librarian lib;
	private Book book;
	private int leftLendDays;
	private Date lendDate;
	private Date returnDate;
	private double penality;
	
	public Lend(){
	}
	
	public Lend(User user, Book book,Librarian lib){
		this.user=user;
		this.book=book;
		this.lib = lib;
		this.leftLendDays=Lend.LENDDAYS;
		this.lendDate = new Date();
	}
	
	public String toString(){
		return this.getClass().getSimpleName()+"[ user = "+user.getNickname()+" , librarian = "+lib.getNickname()+
		" , book = "+book.getName()+" , Left Lend Days = "+leftLendDays+" , date = "+lendDate.toString();
	}
	
	public void setId(int id){
		this.id = id;
	}
	@Id
	@GeneratedValue()
	@Column(name="lend_user_id")
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
	public void setBook(Book book) {
		this.book = book;
	}
	@OneToOne(targetEntity=Book.class)
	public Book getBook() {
		return book;
	}

	public void setLeftLendDays(int lendDays) {
		this.leftLendDays = lendDays;
	}
	public int getLeftLendDays() {
		return leftLendDays;
	}

	public void setLib(Librarian lib) {
		this.lib = lib;
	}
	@OneToOne(targetEntity=Librarian.class,optional=true)
	public Librarian getLib() {
		return lib;
	}

	public void setDate(Date date) {
		this.lendDate = date;
	}
	@Column(name="lend_date",nullable=false)
	public Date getDate() {
		return lendDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}
	
	public String libDateFormat(Date date){
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String dateS =	format.format(date);
		return dateS;
	}
	
	public void setPenality(double penality) {
		this.penality = penality;
	}

	public double getPenality() {
		return penality;
	}

	@Transient
	public List<String> list(){
		List<String>list = new ArrayList<String>();
		list.add(new Integer(this.getId()).toString());
		list.add(this.getBook().getName());
		list.add(libDateFormat(lendDate));
		if(this.returnDate!=null) list.add(this.libDateFormat(returnDate));
		else{list.add("Still in your hands");}
		return list;
	}
	
	@Transient
	public List<String> debtList(){
		List<String>list = new ArrayList<String>();
		list.add(new Integer(this.getId()).toString());
		list.add(this.getBook().getName());
		list.add(libDateFormat(lendDate));
		if(this.returnDate!=null) list.add((this.libDateFormat(returnDate)));
		else{list.add("Still in your hands");}
		list.add(String.valueOf( this.getLeftLendDays() ));
		list.add(getStringPenality(penality));
		return list;
	}
	
	@Transient
	public String getStringPenality(Double penality){
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String s = formatter.format(penality);
		return s;
	}
}
