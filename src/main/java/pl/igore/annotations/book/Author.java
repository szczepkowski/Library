package pl.igore.annotations.book;

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
import javax.persistence.Table;


@Entity
@Table(name="author")
public class Author implements java.io.Serializable{
	private int id;
	private String name;
	private Set<Book> books;
	
	public Author(){}

	public Author( String name){
		this.name = name;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Id
	@GeneratedValue
	@Column(name= "author_id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="author_name", unique = true, nullable = false)
	public String getName() {
		return name;
	}
	
	public boolean equals(Object other){
		return this.name == ((Author)other).name;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	@ManyToMany(mappedBy="authors" ,cascade=CascadeType.ALL)
	public Set<Book> getBooks() {
		return books;
	}
}
