package pl.igore.annotations.book;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="category")
public class Category implements java.io.Serializable{
	private int id;
	private String name;
	
	public Category(){}

	public Category(String name){
		this.name = name;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Id 
	@GeneratedValue
	@Column(name= "category_id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column( unique = true, nullable = false)
	public String getName() {
		return name;
	}
	public boolean equals(Object other){
		return this.name == ((Category)other).name;
	}
	
}