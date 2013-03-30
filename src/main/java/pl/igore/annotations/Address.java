package pl.igore.annotations;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="address") // if Address Class is in package with User Hibernate makes Entity Address .. other case not
public class Address implements Serializable{
	private int id;
	private String street;// Litewska
	private String number; // 66, 68
	private String homeNumber; // 1a, 1b
	private User user;
	private Librarian librarian;
	
	public String toString(){
		String s = ""+ this.getClass().getSimpleName()+"[ street = "+street+" , number = "+number+
		" , homeNumber = "+homeNumber;
		s+=" ]";
		return s;
	}
	
	public Address(){}
	
	public Address(String street, String number, String homeNumber){
		this.street = street;
		this.number = number;
		this.homeNumber= homeNumber;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Id
	@GeneratedValue()
	@Column(name="address_id")
	public int getId() {
		return id;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet() {
		return street;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}
	@Column(name="home_number")
	public String getHomeNumber() {
		return homeNumber;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@OneToOne(targetEntity=User.class)
	public User getUser() {
		return user;
	}

	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}
	@OneToOne(targetEntity=Librarian.class)
	public Librarian getLibrarian() {
		return librarian;
	}
}
