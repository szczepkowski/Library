package pl.igore.annotations.book;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import pl.igore.annotations.Librarian;

@Entity
@Table(name="add_book")
public class AddBook implements Serializable {
	private int id;
	private Librarian librarian;
	private Book book;
	
	public AddBook(){}
	
	public AddBook(Librarian lib,Book book){
		this.librarian=lib;
		this.book=book;
	}
	
	public String toString(){
		return this.getClass().getSimpleName()+" [ librarian = "+librarian.getNickname()+" , book = "+book.toString()+" ]";
	}
	
	public void setId(int id) {
		this.id = id;
	}
	@Id
	@GeneratedValue
	@Column(name= "add_book_id")
	public int getId() {
		return id;
	}
	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}
	@OneToOne(targetEntity=Librarian.class)
	public Librarian getLibrarian() {
		return librarian;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	@OneToOne(targetEntity=Book.class,cascade=CascadeType.ALL)
	public Book getBook() {
		return book;
	}
}
