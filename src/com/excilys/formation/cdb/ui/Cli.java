package com.excilys.formation.cdb.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.excilys.formation.cdb.dao.ComputerDaoImpl;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
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
		System.out.println("1. List computers \t\t 2. List companies\n" + 
						   "3. Show computer details \t 4. Create a computer\n" + 
						   "5. Update a computer \t\t 6. Delete a computer\n" + 
						   "0. Exit\n");
		
	}

	private static void caseListComputer(Scanner sc) {
		long id_first;
		int nb_to_print;
		List<Computer> list_computer = new ArrayList<>();
		
		id_first = getBeginId(sc);
		nb_to_print = getNbToPrint(sc);
		
		list_computer = ComputerService.INSTANCE.getList(id_first, nb_to_print);
		System.out.println(list_computer);
	}

	private static void caseListCompany(Scanner sc) {
		long id_first;
		int nb_to_print;
		List<Company> list_company = new ArrayList<>();
		
		id_first = getBeginId(sc);
		nb_to_print = getNbToPrint(sc);
		
		list_company = CompanyService.INSTANCE.getList(id_first, nb_to_print);
		System.out.println(list_company);
	}

	private static void caseShowComputer(Scanner sc) {
		long id;
		Computer computer = null;
		
		id = getId(sc);
		
		computer = ComputerService.INSTANCE.getById(id);
		System.out.println(computer);
	}

	private static void caseCreateComputer(Scanner sc) throws ParseException {
		String name, intro, discont;
		long company_id;
		Computer computer = new Computer();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
		
		name = getName(sc);
		computer = ComputerService.INSTANCE.setName(name, computer);
		
		intro = getIntroDate(sc);
		while(! ComputerService.INSTANCE.setIntroDate(intro, dateFormat, computer)) {
			intro = getIntroDate(sc);
		}
		
		discont = getDiscontDate(sc);
		while(! ComputerService.INSTANCE.setDiscontDate(discont, dateFormat, computer)) {
			discont = getDiscontDate(sc);
		}
		
		System.out.print("Company's id (enter \"_\" if none): ");
		try {
			company_id = sc.nextLong();
			computer.setCompany_id(company_id);
		}
		catch(InputMismatchException e) {
			String s = sc.next();
			if(!s.equals("_")) {
				System.err.println("Input error: Unexpected value \""+s+"\" received");
			}
		}
		
		computer = ComputerDaoImpl.INSTANCE.create(computer);
		System.out.println(computer);
	}

	private static void caseUpdateComputer(Scanner sc) throws ParseException {
		String name, intro, discont;
		long id, company_id;
		Computer computer = new Computer();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		id = getId(sc);
		computer.setId(id);

		name = getName(sc);
		computer = ComputerService.INSTANCE.setName(name, computer);
		
		intro = getIntroDate(sc);
		while(! ComputerService.INSTANCE.setIntroDate(intro, dateFormat, computer)) {
			intro = getIntroDate(sc);
		}
		
		discont = getDiscontDate(sc);
		while(! ComputerService.INSTANCE.setDiscontDate(discont, dateFormat, computer)) {
			discont = getDiscontDate(sc);
		}
		
		System.out.print("Company's id (enter \"_\" if none): ");
		try {
			company_id = sc.nextLong();
			computer.setCompany_id(company_id);
		}
		catch(InputMismatchException e) {
			String s = sc.next();
			if(!s.equals("_")) {
				System.err.println("Input error: Unexpected value \""+s+"\" received");
			}
		}
		
		computer = ComputerDaoImpl.INSTANCE.update(computer);
		System.out.println(computer);
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

	private static long getBeginId(Scanner sc) {
		long beginId = 0;
		boolean stop = false;
		
		while(!stop) {
			try {
				System.out.print("Begin at id: ");
				beginId = sc.nextLong();
				stop = true;
			}
			catch(InputMismatchException e) {
				sc.nextLine();
			}
		}
		
		return beginId;
	}

	private static int getNbToPrint(Scanner sc) {
		int nbToPrint = 0;
		boolean stop = false;
		
		while(!stop) {
			try {
				System.out.print("How many elements to print: ");
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
			}
			catch(InputMismatchException e) {
				sc.nextLine();
			}
		}
		
		return inputId;
	}
	
	private static String getName(Scanner sc) {
		String name;
		System.out.print("Computer's name (mandatory): ");
		name = sc.next();
		return name;
	}

	private static String getIntroDate(Scanner sc) {
		String introDate = null;
		boolean stop = false;
		
		while(!stop) {
			try {
				System.out.print("Company's introduction date, with yyyy-MM-dd formatting (enter \"_\" if none): ");
				introDate = sc.next();
				stop = true;
			}
			catch(InputMismatchException e) {
				sc.nextLine();
			}
		}
		return introDate;
	}
	
	private static String getDiscontDate(Scanner sc) {
		String discontDate = null;
		boolean stop = false;
		
		while(!stop) {
			try {
				System.out.print("Company's discontinuation date, with yyyy-MM-dd formatting (enter \"_\" if none): ");
				discontDate = sc.next();
				stop = true;
			}
			catch(InputMismatchException e) {
				sc.nextLine();
			}
		}
		return discontDate;
	}
}
