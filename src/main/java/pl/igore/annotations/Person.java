package pl.igore.annotations;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public abstract class Person implements Serializable{
	private int id;
	private String name;
	private String nickname;
	private Address address;
	
	public Person(){}
	
	public Person(String name, String nickname,Address addr){
		this.name=name;
		this.nickname=nickname;
		this.address=addr;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Id
	@GeneratedValue()
	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(unique=true,nullable=false)
	public String getName() {
		return name;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Column(unique=true,nullable=false)
	public String getNickname() {
		return nickname;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	@OneToOne(targetEntity=Address.class,cascade=CascadeType.ALL)
	public Address getAddress() {
		return address;
	}
}
