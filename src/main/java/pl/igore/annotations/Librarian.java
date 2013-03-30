package pl.igore.annotations;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="librarian")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@AttributeOverrides({
		@AttributeOverride(name="id", column=@Column(name="librarian_id") ),
		@AttributeOverride(name="name", column=@Column(name="librarian_name") ),
		@AttributeOverride(name="nickname", column=@Column(name="librarian_nickname"))
        })
@DiscriminatorColumn(
		name="Discriminator",
		discriminatorType=DiscriminatorType.INTEGER
		)
@DiscriminatorValue(value = "1")

public class Librarian extends Person{
	private double salary;

	public Librarian(){}
	
	public String toString(){
		return this.getClass().getSimpleName()+"[ name ="+super.getNickname()+" , nickname = "+super.getNickname()+
		" , address = "+super.getAddress().toString()+" , salary"+salary+" ]";
	}
	
	public Librarian(String name,String nickname, Address addr,Double salary){
		super(name,nickname,addr);
		this.salary = salary;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public double getSalary() {
		return salary;
	}
}
