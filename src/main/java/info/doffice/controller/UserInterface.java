package info.doffice.controller;

import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import info.doffice.api.Person;
import info.doffice.implementations.Student;
import info.doffice.implementations.Subject;
import info.doffice.implementations.Teacher;

public class UserInterface {

	private ControllerDAO controller;



	public void enterService() {
		
		controller.writeSubjectsToDatabase();
		controller.readstudentsFromDatabase();
		controller.readTeachersFromDatabase();
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		String choice = "";
		while(!choice.equalsIgnoreCase("e")) {
			System.out.println("Welcome in virtual deans office service! Please choose what you want to do:\n"
					+ "r - register on service \n"
					+ "l - log into service \n"
					+ "e - exit from program \n");
			try {
				choice = bfr.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch(choice.toLowerCase()){
				case "r":
					register();
					break;
				case "l":
					log();
					break;
				case "e":
					System.out.println("Exiting from program...");
					controller.writeStudentsToDatabase();
					controller.writeTeachersToDatabase();
					return;
				default: System.out.println("Command has not been recognized. Try once again! ");
			}
		}
		
		
	}


	public void register() {
		
		String login="", password="";
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("-------------------------------------------------------\n"
				+ "Enter your login ( your id ) and password! \n"
						+ "login ");
		try {
			login = bfr.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("\npassword ");
		try {
			password = bfr.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		boolean answer = validatePerson(login);
		if(answer) {
		    System.out.println("\nSelect your status: \n"
		    		+ "student - type \"s\" \n"
		    		+ "teacher - type \"t\" \n");
		
		    String choice="";
		    try {
		    	choice = bfr.readLine();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    
			String name = "",surname = "", ch = "";
			while(!ch.equalsIgnoreCase("c")) {
			System.out.print("You are successfully registered. Now please fill your personal data: \n"
					+ "name: ");
			
		    try {
				name = bfr.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    System.out.print("\nsurname: ");
		    try {
				surname = bfr.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    System.out.println("\nConfirm, deny your choice or try again:\n"
					+ "c - confirm \n"
					+ "d - deny \n");
			
			try {
				ch = bfr.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}   
			}
		    
		if(choice.equalsIgnoreCase("s")) {
				
			    Student s = new Student(parseInt(login),name,surname,true, 0.0f, false);
			    s.setSafePassword(password);
			    s.generateGrades(controller.getSubjects());
			    s.calcAverage(controller.getSubjects());
			    s.grantScholarship();
			    controller.getStudents().add(s);
			    System.out.println("Student added!\n"
			    		+ "Now, to use your account, please log into service!\n"
			    		+ "---------------------------------------------------------");
				this.log();
			}
		else if(choice.equalsIgnoreCase("t")) {
			System.out.print("Select your subject: ");
		    String subject = "";
			try {
				subject = bfr.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    Subject subj = controller.getMySubject(subject);
		   
			if(subj!= null) {
				Teacher t = new Teacher(parseInt(login),name,surname,false, subj, new LinkedList<Student>());
				t.setSafePassword(password);
				t.setStudentsList(controller.getStudents());
				controller.getTeachers().add(t);
				System.out.println("Teacher added!\n"
						+ "Now, to use your account, please log into service!\n"
						+ "---------------------------------------------------------");
				this.log();
			}
			else System.out.println("Such a subject does not exist!");
		}
		else System.out.println("You have selected wrong letter! ");
		System.out.println("------------------------------------------------------------------");
		}
		else System.out.println("Person with such a login already exists");
			
		
	}


	public boolean validatePerson(String login) {
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
		List<Student> students = controller.getStudents();
		List<Teacher> teachers = controller.getTeachers();
		if(!controller.personExists(login, students) && !controller.personExists(login, teachers)) {
			return true;
		}
		else return false;
		}
	}



	public void log() {
		System.out.println("Enter your login (id) and password: \n"
				+ "login:");
		String login="", password="";
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		try {
			login = bfr.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("password:");
		try {
			password = bfr.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Person p = controller.getPersonByLogin(parseInt(login));
		if(p != null) {
			if(p.getSafePassword().equals(password)) {
				displayAccountDetails(p);
				String choice = "";
				while(!choice.equalsIgnoreCase("q")) {
					System.out.println("\"q\" to log out:");
				try {
					choice = bfr.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
				clearScreen();
				System.out.println("Logged out...\n");
			}
			else System.out.println("Wrong password");
		}
		else System.out.println("User with such a login does not exist!");
		
	}


	public void displayAccountDetails(Person p) {
		System.out.println("----------------------------------------------------"
				+ "\nYou are logged into service! To log out enter \"q\"!");
		p.viewDetail();
		System.out.println("----------------------------------------------------");
		/* if p is object of Teacher then update list of all students of virtual dean's office
		 because students grades might have changed  */
		if(p instanceof Teacher) {
			controller.setStudents(((Teacher) p).getStudentsList());
			controller.updateStudentDetails();
		}
	}


	public void clearScreen() {
		for(int i=0;i<200;i++) {
			System.out.println("");
		}
	}

	public ControllerDAO getController() {
		return controller;
	}

	public void setController(ControllerDAO controller) {
		this.controller = controller;
	}
	
	
}
