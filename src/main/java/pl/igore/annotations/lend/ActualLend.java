package pl.igore.annotations.lend;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import pl.igore.annotations.User;

@Entity
@Table(name="actual_lend")
public class ActualLend implements Serializable{
	private int id;
	private Lend lend;
	
	public ActualLend(){}
	
	public ActualLend(Lend lend){
		this.lend=lend;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	@Id
	@GeneratedValue()
	@Column(name="actual_lend_id")
	public int getId() {
		return id;
	}
	public void setLend(Lend lend) {
		this.lend = lend;
	}
	@OneToOne(targetEntity=Lend.class)
	public Lend getLend() {
		return lend;
	}
}
