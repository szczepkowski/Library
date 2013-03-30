package pl.igore.annotations;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import pl.igore.annotations.book.Book;
import pl.igore.annotations.lend.Account;

@Entity
@Table(name="lib_user")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@AttributeOverrides({
		@AttributeOverride(name="id", column=@Column(name="lib_user_id") ),
		@AttributeOverride(name="name", column=@Column(name="user_name") ),
		@AttributeOverride(name="nickname", column=@Column(name="user_nickname"))
        })
@DiscriminatorColumn(
		name="Discriminator",
		discriminatorType=DiscriminatorType.INTEGER
		)
@DiscriminatorValue(value = "1")
public class User extends Person implements Serializable{
	private String password;
	private Account account;
	
	public User(){}
	
	public String toString(){
		return this.getClass().getSimpleName() + "[ name = "+super.getName()+" , nickname = "+
		super.getNickname()+" , address = "+super.getAddress().toString()+" ,debt = "+account.getDebt()+" ]";
	}
	
	public User(String name,String nickname,Address addr){
		super(name, nickname, addr);
		this.account = new Account();
		account.setUser(this);
		this.password = "";
	}
	
	public User(String name,String nickname,String password,Address addr){
		super(name, nickname, addr);
		this.account = new Account();
		account.setUser(this);
		this.password = password;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@OneToOne(targetEntity=Account.class,cascade=CascadeType.ALL)
	public Account getAccount() {
		return account;
	}
	@ManyToMany()
	@JoinTable(
			name="user_books",
			joinColumns={@JoinColumn(name="lib_user_id") },
			inverseJoinColumns=	{@JoinColumn(name="book_id")}
	)

	public void setPassword(String password) {
		this.password = password;
	}
	@Column(nullable=false)
	public String getPassword() {
		return password;
	}

}