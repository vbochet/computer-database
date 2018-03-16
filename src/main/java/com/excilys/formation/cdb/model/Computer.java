package com.excilys.formation.cdb.model;

import java.time.LocalDate;

public class Computer {

	private long id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private Company company;
	

	public Computer() { }
	
	public Computer(long id, String name, LocalDate introduced, LocalDate discontinued, Company company) {
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
	
	public LocalDate getIntroduced() {
		return introduced;
	}
	
	public LocalDate getDiscontinued() {
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
	
	public void setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
	}
	
	public void setDiscontinued(LocalDate discontinued) {
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
	
	
	public boolean equals(Computer other) {
		if(other == null) {
			return false;
		}
		
		if(this == other) {
			return true;
		}
		
		if(this.getId() == other.getId()) {
			if(this.getName() == other.getName()) {
				if((this.getIntroduced() == null && other.getIntroduced() == null)
					|| (this.getIntroduced() != null && this.getIntroduced().equals(other.getIntroduced()))) {

					if((this.getDiscontinued() == null && other.getDiscontinued() == null)
						|| (this.getDiscontinued() != null && this.getDiscontinued().equals(other.getDiscontinued()))) {

						if((this.getCompany() == null && other.getCompany() == null)
							|| (this.getCompany() != null && this.getCompany().equals(other.getCompany()))) {
							
							return true;
						}
						return false;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}

}
