package info.doffice.implementations;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="subject")
public class Subject {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="ects")
	private float ects;
	@Column(name="subject_type")
	private String subjectType;


	public Subject(String name, float ects, String subjectType) {
		super();
		this.name = name;
		this.ects = ects;
		this.subjectType = subjectType;
	}

	public Subject() {}
	
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
	public float getEcts() {
		return ects;
	}
	public void setEcts(float ects) {
		this.ects = ects;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}



	@Override
	public String toString() {
		return "Subject [name=" + name + ", ects=" + ects + ", subjectType=" + subjectType + "]";
	} 
	

}
