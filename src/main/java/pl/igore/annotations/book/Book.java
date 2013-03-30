package pl.igore.annotations.book;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import pl.igore.annotations.User;

@Entity
@Table(name="book")
public class Book implements java.io.Serializable {
	private int bookId;
	private Set<Author> authors;
	private String name;
	private Category category;
	private Date createDate;

	public Book(){}
	
	public Book(Set<Author> authors, String name, Category category,Date createDate){
		this.authors = authors;
		this.name = name;
		this.category = category;
		this.createDate = createDate;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	@Id
	@GeneratedValue
	@Column(name= "book_id")
	public int getBookId() {
		return bookId;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	@ManyToMany
	@JoinTable(
			name="book_author",
			joinColumns={@JoinColumn(name="book_id")},
			inverseJoinColumns={@JoinColumn(name="author_id")}
			)
	public Set<Author> getAuthors() {
		return authors;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable=false,unique=true)
	public String getName() {
		return name;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@ManyToOne(targetEntity=Category.class,optional=false)
	public Category getCategory() {
		return category;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}
	@Transient
	public String getStringDate(){
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String dateS =	format.format(createDate);
		return dateS;
	}
	
	public String toStringAuthors(){
		Iterator<Author> it = authors.iterator();
		String s="";
		boolean first=true;
		while(it.hasNext()){
			s+=((Author)it.next()).getName();
			if(!first)s+=" ,";
			first=false;
		}
		
		return s;
	}
	
	public List<String>list(){
		List<String> list = new ArrayList<String>();
		list.add(this.name);
		String s ="";
		Iterator<Author> it = authors.iterator();
		boolean first = true;
		while(it.hasNext()){
			s = ((Author)it.next()).getName();
			if(!first)s+=" ,";
			first=false;
		}
		list.add(s);
		list.add(this.category.getName());

		String dateS =	this.getStringDate();
		list.add(dateS);

		return list;
	}
	
	public boolean equals(Object other){
		return this.name == ((Book)other).name;
	}
	
	public String toString(){
		String s =  this.getClass().getSimpleName()+" [ bookId = "+this.bookId+" name = "+this.name+" category = "+ this.category.getName()+" ";
		Iterator<Author> it = authors.iterator();
		while(it.hasNext()){
			s+=((Author)it.next()).getName()+", ";
		}
		s+=" ]";
		return s;
	}
}
