package info.doffice.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="person")
public class Person {

	@Id
	@Column(name="id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="surname")
	private String surname;
	@Column(name="is_student")
	private boolean isStudent;
	@Column(name="password")
	private String password;


	
	
	public Person() {
		
	}


	public Person(int id, String name, String surname, boolean isStudent) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.isStudent = isStudent;
	}


	public void viewDetail() {
		System.out.println("Personal data: \n"
				+ toString());
	}


	public void setSafePassword(String password) {
		setPassword(password);
	}

	public String getSafePassword() {
		String passw = getPassword();
		return passw;
	}

	private String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public boolean isStudent() {
		return isStudent;
	}
	public void setStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}


	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
	}


}
