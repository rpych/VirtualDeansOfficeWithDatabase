package info.doffice.controller;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import info.doffice.api.Person;
import info.doffice.implementations.Student;
import info.doffice.implementations.Subject;
import info.doffice.implementations.Teacher;

public class Main {

	public static void main(String[] args) {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Person.class);
		SessionFactory factory = configuration.buildSessionFactory();
		Session session = factory.getCurrentSession();
		
		ApplicationContext xmlContext = new ClassPathXmlApplicationContext("context.xml");
		ControllerDAO con = xmlContext.getBean("controllerDAO", ControllerDAO.class);
		UserInterface mainController = xmlContext.getBean("mainController", UserInterface.class);
		
		mainController.enterService();
	}

}
