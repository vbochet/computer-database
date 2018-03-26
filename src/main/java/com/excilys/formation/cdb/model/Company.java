package com.excilys.formation.cdb.model;

public class Company {

    private long id;
    private String name;


    public Company() {
        this.id = 0;
        this.name = null;
    }

    public Company(long id, String name) {
        this.id = id;
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("COMPANY\n");
        sb.append("  id: ");
        sb.append(this.getId());
        sb.append("\n  name: ");
        sb.append(this.getName());
        sb.append("\n");
        return sb.toString();
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (other instanceof Company) {
            Company otherc = (Company) other;

            if (this.getId() == otherc.getId()) {
                if (this.getName() == otherc.getName()) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
