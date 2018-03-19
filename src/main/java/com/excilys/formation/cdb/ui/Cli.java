package com.excilys.formation.cdb.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.paginator.CompanyPage;
import com.excilys.formation.cdb.paginator.ComputerPage;
import com.excilys.formation.cdb.paginator.Page;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;

public class Cli {

	public static void main(String[] args) {
		System.out.println("COMPUTER DATABASE\n-----------------\n");
		
		Boolean stop = false;
		Scanner sc = new Scanner(System.in);
		int i;
		
		while(!stop) {
			try {
				printMenu();
				System.out.print("Your choice: ");
				i = sc.nextInt();
				if(i < CliMenuChoices.values().length) {
					switch(CliMenuChoices.values()[i]) {
					case QUIT:
						System.out.println("Received exit signal");
						stop = true;
						break;
						
					case LIST_COMPUTERS:
						caseListComputer(sc);
						break;
						
					case LIST_COMPANIES:
						caseListCompany(sc);
						break;
						
					case READ_COMPUTER:
						caseShowComputer(sc);
						break;
						
					case CREATE_COMPUTER:
						caseCreateComputer(sc);
						break;
						
					case UPDATE_COMPUTER:
						caseUpdateComputer(sc);
						break;
						
					case DELETE_COMPUTER:
						caseDeleteComputer(sc);
						break;
						
					default:
						System.out.println("Option "+i+" is unknown");
					}
				}
				else {
					System.out.println("Option "+i+" is unknown");
				}
			}
			catch(InputMismatchException e) {
				System.err.println("Input error: Unexpected value \""+sc.nextLine()+"\" received");
				stop = true;
			} catch (ParseException e) {
				e.printStackTrace();
				System.err.println("Input error: Bad date format (expected yyyy-mm-dd)");
				stop = true;
			}
		}
		
		System.out.println("Terminating...");
		sc.close();
	}

	private static void printMenu() {
		System.out.println("Select the action you want to perform in the list below:");

		StringBuilder sb = new StringBuilder();
		sb.append(CliMenuChoices.LIST_COMPUTERS.ordinal())
			.append(". List computers \t\t ")
			.append(CliMenuChoices.LIST_COMPANIES.ordinal())
			.append(". List companies\n")
			.append(CliMenuChoices.READ_COMPUTER.ordinal())
			.append(". Show computer details \t ")
			.append(CliMenuChoices.CREATE_COMPUTER.ordinal())
			.append(". Create a computer\n")
			.append(CliMenuChoices.UPDATE_COMPUTER.ordinal())
			.append(". Update a computer \t\t ")
			.append(CliMenuChoices.DELETE_COMPUTER.ordinal())
			.append(". Delete a computer\n")
			.append(CliMenuChoices.QUIT.ordinal())
			.append(". Exit\n");
		System.out.println(sb.toString());
	}

	private static void caseListComputer(Scanner sc) {
		ComputerPage page = new ComputerPage();
		int nbToPrint = getNbToPrint(sc);
		page.setNbPerPage(nbToPrint);

		System.out.println(page.getContent());
		System.out.println("\n");
		
		while(getAction(sc, page)) {
			System.out.println(page.getContent());
			System.out.println("\n");
		}
	}

	private static void caseListCompany(Scanner sc) {
		CompanyPage page = new CompanyPage();
		int nbToPrint = getNbToPrint(sc);
		page.setNbPerPage(nbToPrint);

		System.out.println(page.getContent());
		System.out.println("\n");
		
		while(getAction(sc, page)) {
			System.out.println(page.getContent());
			System.out.println("\n");
		}
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
		long id;
		Computer computer = null;
		
		id = getId(sc);
		
		computer = ComputerService.INSTANCE.getById(id);
		System.out.println(computer);
	}

	private static void caseCreateComputer(Scanner sc) throws ParseException {
		String name, intro, discont, companyIdStr = null;
		long companyId;
		boolean loop = true;
		Computer computer = new Computer();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
		
		name = getName(sc);
		ComputerService.INSTANCE.setName(name, computer);
		
		intro = getIntroDate(sc);
		while(! ComputerService.INSTANCE.setIntroDate(intro, dateFormat, computer)) {
			intro = getIntroDate(sc);
		}
		
		discont = getDiscontDate(sc);
		while(! ComputerService.INSTANCE.setDiscontDate(discont, dateFormat, computer)) {
			discont = getDiscontDate(sc);
		}
		
		while(loop) {
			System.out.print("Company's id (if none, leave empty): ");
			try {
				companyIdStr = sc.nextLine();
				if(! companyIdStr.isEmpty()) {
					companyId = Integer.parseInt(companyIdStr);
					Company company = CompanyService.INSTANCE.getById(companyId);
					computer.setCompany(company);
				}
				loop = false;
			}
			catch(NumberFormatException e) {
				System.err.println("Input error: Unexpected value \""+companyIdStr+"\" received");
			}
		}
		computer = ComputerService.INSTANCE.createComputer(computer);
		System.out.println(computer);
	}

	private static void caseUpdateComputer(Scanner sc) throws ParseException {
		long id;
		Computer computer;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		id = getId(sc);
		computer = ComputerService.INSTANCE.getById(id);
		if(null == computer) {
			System.out.println("No computer matching this id");
			return;
		}

		updateComputerName(sc, computer);
		updateComputerIntroduced(sc, computer, dateFormat);
		updateComputerDiscontinued(sc, computer, dateFormat);
		updateComputerCompany(sc, computer);
		
		computer = ComputerService.INSTANCE.updateComputer(computer);
		System.out.println(computer);
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
		String answer = "", companyIdStr = null;
		boolean loop = true;
		long companyId;
		Company company = null;
		System.out.println("Current company: ["+computer.getCompany()+"].");
		
		answer = chooseUpdate(sc, "company");
		
		if(answer.equals("y")) {
			while(loop) {
				System.out.print("Company's id (if none, leave empty): ");
				try {
					companyIdStr = sc.nextLine();
					if(! companyIdStr.isEmpty()) {
						companyId = Integer.parseInt(companyIdStr);
						company = CompanyService.INSTANCE.getById(companyId);
						computer.setCompany(company);
					}
					loop = false;
				}
				catch(InputMismatchException e) {
					System.err.println("Input error: Unexpected value \""+companyIdStr+"\" received");
				}
				finally {
					computer.setCompany(company);
				}
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
		long id;
		
		id = getId(sc);
		
		if(ComputerService.INSTANCE.deleteById(id)) {
			System.out.println("Computer n°"+id+" has been successfully deleted");
		}
		else {
			System.out.println("A problem occured. Computer n°"+id+" couldn't be deleted");
		}
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
