package com.excilys.formation.cdb.model;

import java.time.LocalDateTime;

public class Computer {

	private int id;
	private String name;
	private LocalDateTime introduced;
	private LocalDateTime discontinued;
	private int company_id;
	

	public Computer() {
		this.id = 0;
		this.name = null;
		this.introduced = null;
		this.discontinued = null;
		this.company_id = 0;
	}
	
	public Computer(int id, String name, LocalDateTime introduced, LocalDateTime discontinued, int company_id) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company_id = company_id;
	}
	
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDateTime getIntroduced() {
		return introduced;
	}
	
	public LocalDateTime getDiscontinued() {
		return discontinued;
	}
	
	public int getCompany_id() {
		return company_id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setIntroduced(LocalDateTime introduced) {
		this.introduced = introduced;
	}
	
	public void setDiscontinued(LocalDateTime discontinued) {
		this.discontinued = discontinued;
	}
	
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

}
