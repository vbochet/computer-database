package com.excilys.formation.cdb.model;

import java.sql.Timestamp;

public class Computer {

	private int id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
	private int company_id;
	

	public Computer() {
		this.id = 0;
		this.name = null;
		this.introduced = null;
		this.discontinued = null;
		this.company_id = 0;
	}
	
	public Computer(int id, String name, Timestamp introduced, Timestamp discontinued, int company_id) {
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
	
	public Timestamp getIntroduced() {
		return introduced;
	}
	
	public Timestamp getDiscontinued() {
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
	
	public void setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
	}
	
	public void setDiscontinued(Timestamp discontinued) {
		this.discontinued = discontinued;
	}
	
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
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
		sb.append("\n  company id: ");
		sb.append(this.getCompany_id());
		return sb.toString();
	}

}
