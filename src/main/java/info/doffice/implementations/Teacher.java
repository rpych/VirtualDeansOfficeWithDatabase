package info.doffice.implementations;

import static java.lang.Float.parseFloat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import info.doffice.api.Person;

@Entity
@Table(name="teacher")
public class Teacher extends Person {
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="subject_id")
	private Subject subject; //aggregation
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "students_lists", 
		joinColumns = { @JoinColumn(name = "teacher_id")},
		inverseJoinColumns = { @JoinColumn(name = "student_id") })
	private List<Student> studentsList; //aggregation


	public Teacher(int id, String name, String surname, boolean isStudent, Subject subject,
			List<Student> studentsList) {
		super(id, name, surname, isStudent);
		this.subject = subject;
		this.studentsList = studentsList;
	}


	public Teacher() {}
	
	public void viewDetail() {
		super.viewDetail();
		System.out.println("Subject data:\n "
				+ subject.getName() + ", type-" + subject.getSubjectType());
		this.judgeStudents();
	}


	public void addGradesForStudents() {
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		
		for(Student s: studentsList) {
			String choice = "";
			System.out.println(s.toString() + ", next student -n \nGrade:");
			while(!choice.equalsIgnoreCase("c") && !choice.equalsIgnoreCase("q")) {
				try {
					choice = bfr.readLine(); //inserting grade
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(choice.equalsIgnoreCase("n")) break;
				
				for(Map.Entry<String, Float> entry: s.getGrades().entrySet()) {
					if(entry.getKey().equals(subject.getName())) {
						entry.setValue(parseFloat(choice));
					}
				}
				System.out.println("To confirm student's grade, type c, to quit type q!");
				try {
					choice = bfr.readLine(); //confirming
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Grades marked");
	}


	public void judgeStudents() {
		System.out.println("To enter students grades, type \"g\" \n");
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		String choice = "";
		try {
			choice = bfr.readLine(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(choice.equalsIgnoreCase("g")) {
			this.addGradesForStudents();
		}
		else {
			System.out.println("Wrong letter! Your access attempt has failed");
		}
		
	}


	public Subject getSubject() {
		return subject;
	}


	public void setSubject(Subject subject) {
		this.subject = subject;
	}


	public List<Student> getStudentsList() {
		return studentsList;
	}


	public void setStudentsList(List<Student> studentsList) {
		this.studentsList = studentsList;
	}
		
}
