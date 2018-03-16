package com.excilys.formation.cdb.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.paginator.CompanyPage;
import com.excilys.formation.cdb.paginator.ComputerPage;
import com.excilys.formation.cdb.paginator.Page;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;

public class Cli {

    static Logger LOGGER = LoggerFactory.getLogger(Cli.class);
    
	public static void main(String[] args) {
		LOGGER.info("Starting Computer-Database command line interface");
		
		System.out.println("COMPUTER DATABASE\n-----------------\n");
		
		Boolean stop = false;
		Scanner sc = new Scanner(System.in);
		int i;
		
		while(!stop) {
			try {
				printMenu();
				System.out.print("Your choice: ");
				i = sc.nextInt();
				
				switch(i) {
				case(0):
					System.out.println("Received exit signal");
					stop = true;
					break;
					
				case(1):
					caseListComputer(sc);
					break;
					
				case(2):
					caseListCompany(sc);
					break;
					
				case(3):
					caseShowComputer(sc);
					break;
					
				case(4):
					caseCreateComputer(sc);
					break;
					
				case(5):
					caseUpdateComputer(sc);
					break;
					
				case(6):
					caseDeleteComputer(sc);
					break;
					
				default:
					System.out.println("Option "+i+" is unknown");
					LOGGER.info("Invalid option in CLI menu: {}", i);
				}
			}
			catch(InputMismatchException e) {
				String input = sc.nextLine();
				System.err.println("Input error: Unexpected value \""+input+"\" received");
				LOGGER.error("Invalid user input in CLI menu: {}", input);
				stop = true;
			} catch (ParseException e) {
				e.printStackTrace();
				System.err.println("Input error: Bad date format (expected yyyy-mm-dd)");
				LOGGER.error("Invalid user input in CLI: bad date format");
				stop = true;
			}
		}
		
		System.out.println("Terminating...");
		LOGGER.info("Stopping Computer-Database command line interface");
		sc.close();
	}

	private static void printMenu() {
		System.out.println("Select the action you want to perform in the list below:");
		System.out.println("1. List computers \t\t 2. List companies\n" + 
						   "3. Show computer details \t 4. Create a computer\n" + 
						   "5. Update a computer \t\t 6. Delete a computer\n" + 
						   "0. Exit\n");
		
	}

	private static void caseListComputer(Scanner sc) {
		LOGGER.info("User choice: List computers");
		ComputerPage page = new ComputerPage();
		int nbToPrint = getNbToPrint(sc);
		page.setNbPerPage(nbToPrint);
		LOGGER.info("(print {} computers per page)", nbToPrint);

		System.out.println(page.getContent());
		System.out.println("\n");
		
		while(getAction(sc, page)) {
			System.out.println(page.getContent());
			System.out.println("\n");
		}
		LOGGER.info("End of computer listing");
	}

	private static void caseListCompany(Scanner sc) {
		LOGGER.info("User choice: List companies");
		CompanyPage page = new CompanyPage();
		int nbToPrint = getNbToPrint(sc);
		page.setNbPerPage(nbToPrint);
		LOGGER.info("(print {} companies per page)", nbToPrint);

		System.out.println(page.getContent());
		System.out.println("\n");
		
		while(getAction(sc, page)) {
			System.out.println(page.getContent());
			System.out.println("\n");
		}
		LOGGER.info("End of company listing");
	}

	private static boolean getAction(Scanner sc, Page page) {
		System.out.println("Type 'n' to go to next page, 'p' to go to previous page, 'g 42' to go to page 42, and 'q' to quit.");
		boolean loop = true, ret = true;
		String action;
		while(loop) {
			action = sc.next();
			switch(action) {
				case "n":
					page.next();
					loop = false;
					break;
				case "p":
					page.prev();
					loop = false;
					break;
				case "g":
					String nbInput = sc.next();
					try {
						int nb = Integer.parseInt(nbInput);
						page.goToPage(nb);
						loop = false;
					}
					catch(NumberFormatException e) {
						sc.nextLine();
					}
					break;
				case "q":
					loop = false;
					ret = false;
					break;
				default:
					sc.nextLine();
			}
		}
		
		return ret;
	}

	private static void caseShowComputer(Scanner sc) {
		LOGGER.info("User choice: Show computer info");
		long id;
		Computer computer = null;
		
		id = getId(sc);
		LOGGER.info("Computer's id: {}", id);
		
		computer = ComputerService.INSTANCE.getById(id);
		System.out.println(computer);
		LOGGER.info("End of computer info");
	}

	private static void caseCreateComputer(Scanner sc) throws ParseException {
		LOGGER.info("User choice: Create new computer");
		String name, intro, discont;
		long companyId;
		Computer computer = new Computer();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
		
		name = getName(sc);
		LOGGER.info("Name: {}", name);
		ComputerService.INSTANCE.setName(name, computer);
		
		intro = getIntroDate(sc);
		while(! ComputerService.INSTANCE.setIntroDate(intro, dateFormat, computer)) {
			intro = getIntroDate(sc);
		}
		LOGGER.info("Introducted: {}", intro);
		
		discont = getDiscontDate(sc);
		while(! ComputerService.INSTANCE.setDiscontDate(discont, dateFormat, computer)) {
			discont = getDiscontDate(sc);
		}
		LOGGER.info("Discontinued: {}", discont);
		
		System.out.print("Company's id (if none, leave empty): ");
		try {
			companyId = sc.nextLong();
			Company company = CompanyService.INSTANCE.getById(companyId);
			LOGGER.info("Company id: {}", companyId);
			computer.setCompany(company);
		}
		catch(InputMismatchException e) {
			String s = sc.nextLine();
			if(!s.isEmpty()) {
				System.err.println("Input error: Unexpected value \""+s+"\" received");
				LOGGER.error("Invalid user input: {}", s);
			}
			else {
				LOGGER.info("Computer id: null");
			}
		}
		
		computer = ComputerService.INSTANCE.createComputer(computer);
		System.out.println(computer);
		LOGGER.info("End of computer creation");
	}

	private static void caseUpdateComputer(Scanner sc) throws ParseException {
		LOGGER.info("User choice: Update computer");
		long id;
		Computer computer;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		id = getId(sc);
		LOGGER.info("Computer's id: {}", id);
		computer = ComputerService.INSTANCE.getById(id);
		if(null == computer) {
			System.out.println("No computer matching this id");
			LOGGER.error("No computer matching id {}", id);
			return;
		}

		updateComputerName(sc, computer);
		updateComputerIntroduced(sc, computer, dateFormat);
		updateComputerDiscontinued(sc, computer, dateFormat);
		updateComputerCompany(sc, computer);
		
		computer = ComputerService.INSTANCE.updateComputer(computer);
		System.out.println(computer);
		LOGGER.info("End of computer update");
	}

	private static void updateComputerName(Scanner sc, Computer computer) {
		String answer = "", name;
		System.out.println("Current name: ["+computer.getName()+"].");
		
		answer = chooseUpdate(sc, "name");
		
		if(answer.equals("y")) {
			name = getName(sc);
			ComputerService.INSTANCE.setName(name, computer);
		}
	}

	private static void updateComputerIntroduced(Scanner sc, Computer computer, DateFormat dateFormat) {
		String answer = "", intro;
		System.out.println("Current introduction date: ["+computer.getIntroduced()+"].");
		
		answer = chooseUpdate(sc, "introduction date");
		
		if(answer.equals("y")) {
			intro = getIntroDate(sc);
			while(! ComputerService.INSTANCE.setIntroDate(intro, dateFormat, computer)) {
				intro = getIntroDate(sc);
			}
		}
	}

	private static void updateComputerDiscontinued(Scanner sc, Computer computer, DateFormat dateFormat) {
		String answer = "", discont;
		System.out.println("Current discontinuation date: ["+computer.getDiscontinued()+"].");
		
		answer = chooseUpdate(sc, "discontinuation date");
		
		if(answer.equals("y")) {
			discont = getDiscontDate(sc);
			while(! ComputerService.INSTANCE.setDiscontDate(discont, dateFormat, computer)) {
				discont = getDiscontDate(sc);
			}
		}
	}

	private static void updateComputerCompany(Scanner sc, Computer computer) {
		String answer = "";
		long companyId;
		Company company = null;
		System.out.println("Current company: ["+computer.getCompany()+"].");
		
		answer = chooseUpdate(sc, "company");
		
		if(answer.equals("y")) {
			System.out.print("Company's id (if none, leave empty): ");
			String inputCompany = sc.nextLine();
				try {
					companyId = Long.parseLong(inputCompany);
					company = CompanyService.INSTANCE.getById(companyId);
				} 
				finally {
					computer.setCompany(company);
				}
		}
	}
	
	private static String chooseUpdate(Scanner sc, String s) {
		String answer = "";
		while(!(answer.equals("y") || answer.equals("n"))) {
			System.out.print("Update "+s+" (y/n)? ");
			answer = sc.nextLine();
		}
		return answer;
	}

	private static void caseDeleteComputer(Scanner sc) {
		LOGGER.info("User choice: Update computer");
		long id;
		
		id = getId(sc);
		LOGGER.info("Computer's id: {}", id);
		
		if(ComputerService.INSTANCE.deleteById(id)) {
			System.out.println("Computer n°"+id+" has been successfully deleted");
			LOGGER.info("Computer {} has been deleted", id);
		}
		else {
			System.out.println("A problem occured. Computer n°"+id+" couldn't be deleted");
			LOGGER.error("Computer {} couldn't be deleted", id);
		}
		LOGGER.info("End of computer deletion");
	}

	private static int getNbToPrint(Scanner sc) {
		int nbToPrint = 0;
		boolean stop = false;
		
		while(!stop) {
			try {
				System.out.print("How many elements a page should contain: ");
				nbToPrint = sc.nextInt();
				stop = true;
			}
			catch(InputMismatchException e) {
				sc.nextLine();
			}
		}
		
		return nbToPrint;
	}

	private static long getId(Scanner sc) {
		long inputId = 0;
		boolean stop = false;
		
		while(!stop) {
			try {
				System.out.print("Id: ");
				inputId = sc.nextLong();
				stop = true;
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				sc.nextLine();
			}
		}
		
		return inputId;
	}
	
	private static String getName(Scanner sc) {
		String name = "";
		
		while(name.isEmpty()) {
			System.out.print("Computer's name (mandatory): ");
			name = sc.nextLine();
		}
		
		return name;
	}

	private static String getIntroDate(Scanner sc) {
		System.out.print("Company's introduction date, with yyyy-MM-dd formatting (if none, leave empty): ");
		return sc.nextLine();
	}
	
	private static String getDiscontDate(Scanner sc) {
		System.out.print("Company's discontinuation date, with yyyy-MM-dd formatting (if none, leave empty): ");
		return sc.nextLine();
	}
}
