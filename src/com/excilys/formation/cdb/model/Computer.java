package com.excilys.formation.cdb.model;

import java.sql.Timestamp;

import com.excilys.formation.cdb.service.CompanyService;

public class Computer {

	private long id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
	private Company company;
	

	public Computer() {
		this.id = 0;
		this.name = null;
		this.introduced = null;
		this.discontinued = null;
		this.company = null;
	}
	
	public Computer(long id, String name, Timestamp introduced, Timestamp discontinued, Company company) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}
	
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Timestamp getIntroduced() {
		return introduced;
	}
	
	public Timestamp getDiscontinued() {
		return discontinued;
	}
	
	public Company getCompany() {
		return company;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
	}
	
	public void setDiscontinued(Timestamp discontinued) {
		this.discontinued = discontinued;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("COMPUTER\n");
		sb.append("  id: ");
		sb.append(this.getId());
		sb.append("\n  name: ");
		sb.append(this.getName());
		sb.append("\n  introduced: ");
		sb.append(this.getIntroduced());
		sb.append("\n  discontinued: ");
		sb.append(this.getDiscontinued());
		sb.append("\n  company: ");
		sb.append(this.getCompany());
		sb.append("\n");
		return sb.toString();
	}

}
