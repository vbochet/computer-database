package com.excilys.formation.cdb.model;

public class Company {
	
	private int id;
	private String name;

	
	public Company() {
		this.id = 0;
		this.name = null;
	}

	public Company(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
