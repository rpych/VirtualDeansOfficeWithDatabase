package info.doffice.controller;

import static java.lang.Integer.parseInt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import info.doffice.api.Person;
import info.doffice.implementations.Student;
import info.doffice.implementations.Subject;
import info.doffice.implementations.Teacher;

public class ControllerDAO {

	SessionFactory factory;
	private List<Subject> subjects;
	private List<Student> students;
	private List<Teacher> teachers;

	
	public void prepareSession() {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Teacher.class);
		configuration.addAnnotatedClass(Subject.class);
		factory = configuration.buildSessionFactory();
	}

	
	public void writeSubjectsToDatabase() {
		prepareSession();
		Session session;
		for(Subject subject: subjects) {
			session = factory.getCurrentSession();
			session.beginTransaction();
			
			try {
			Subject sub = (Subject) session.createQuery("from Subject where name like '" + 
			subject.getName() + "'").getSingleResult();
			}catch( NoResultException e) {
				session.save(subject);
			}finally {
				session.getTransaction().commit();
			}
		}
		factory.close();
	}
	
	
	
	public void writeStudentsToDatabase() {
		prepareSession();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		for(Student student: students) {
			session.saveOrUpdate(student);
		}
		session.getTransaction().commit();
		session = factory.getCurrentSession();
		session.beginTransaction();
		for(Student student: students) {
			Person person = new Person(student.getId(), student.getName(), student.getSurname(), student.isStudent());
			person.setSafePassword(student.getSafePassword());
			session.saveOrUpdate(person);
			
		}
		session.getTransaction().commit();
		factory.close();
	}


	public void readstudentsFromDatabase() {
		prepareSession();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		students = (List<Student>) session.createQuery("from Student").getResultList();
		session.getTransaction().commit();
		for(Student student: students) {
			session = factory.getCurrentSession();
			session.beginTransaction();
			List<String> names = (List<String>) session.createNativeQuery("select name from grades where student_id = "
			+ student.getId() + "").getResultList();
			List<Float> studentGrades = (List<Float>) session.createNativeQuery("select grade from grades where student_id = "
			+ student.getId() + "").getResultList();
			session.getTransaction().commit();
			Map<String, Float> grades = new HashMap<String, Float>();
			for(int i=0;i<names.size();++i) 
				grades.put(names.get(i), studentGrades.get(i));
			student.setGrades(grades);
		}
		factory.close();
	}

	
	public void writeTeachersToDatabase() {
		prepareSession();
		Session session;
		
		for(Teacher teacher: teachers) {
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.saveOrUpdate(teacher);
			session.getTransaction().commit();
		}
		
		
		factory.close();
	}

	
	public void readTeachersFromDatabase() {
		prepareSession();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		teachers = (List<Teacher>) session.createQuery("from Teacher").getResultList();
		session.getTransaction().commit();
		
		for(Teacher teacher: teachers) {
			session = factory.getCurrentSession();
			session.beginTransaction();
			List<Integer> studentsIds = (List<Integer>) session.createNativeQuery("select student_id from students_lists where teacher_id= " 
					+ teacher.getId() + "").getResultList();
			session.getTransaction().commit();
			List<Student> students = new LinkedList<Student>();
			for(Integer id: studentsIds) {
				session = factory.getCurrentSession();
				session.beginTransaction();
				Student student = (Student) session.createQuery("from Student where id= " + id + "").getSingleResult();
				students.add(student);
				session.getTransaction().commit();
			}
			teacher.setStudentsList(students);
			
		}
		factory.close();
	}


	public boolean personExists(String login, List< ? extends Person> people) {
		for(Person person: people) {
			if(parseInt(login) == (person.getId())) return true;
		}
		return false;
	}


	public Subject getMySubject(String subject) {
		for(Subject s: subjects) {
			if(s.getName().equals(subject)) return s;
		}
		return null;
	}


	public Person getPersonByLogin(int login) {
		for(Student s: students) {
			if(s.getId() == login) return s; 
		}
		for(Teacher t: teachers) {
			if(t.getId() == login) return t;
		}
		return null;
	}


	public void updateStudentDetails() {
		for(Student s: students) {
			s.calcAverage(subjects);
			s.grantScholarship();
		}
	}

	//additional, helpful function
	public void deletePerson(int login) {
		Person p = getPersonByLogin(login);
		if(p instanceof Student) students.remove(p);
		else if (p instanceof Teacher) teachers.remove(p);
	}


	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
			
	
	
}
