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


    @Override
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


    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (other instanceof Computer) {
            Computer otherc = (Computer) other;

            return (this.sameId(otherc) && this.sameName(otherc) && this.sameIntroduced(otherc) && this.sameDiscontinued(otherc) && this.sameCompany(otherc));
        }
        return false;
    }

    private boolean sameId(Computer other) {
        return this.getId() == other.getId();
    }

    private boolean sameName(Computer other) {
        if (this.getName() == other.getName()) {
            return true;
        }

        if (this.getName() != null) {
            return this.getName().equals(other.getName());
        }

        if (other.getName() != null) {
            return other.getName().equals(this.getName());
        }
        
        return false;
    }

    private boolean sameIntroduced(Computer other) {
        return (this.getIntroduced() == null && other.getIntroduced() == null)
                || (this.getIntroduced() != null && this.getIntroduced().equals(other.getIntroduced()));
    }

    private boolean sameDiscontinued(Computer other) {
        return (this.getDiscontinued() == null && other.getDiscontinued() == null)
                || (this.getDiscontinued() != null && this.getDiscontinued().equals(other.getDiscontinued()));
    }

    private boolean sameCompany(Computer other) {
        return (this.getCompany() == null && other.getCompany() == null)
                || (this.getCompany() != null && this.getCompany().equals(other.getCompany()));
    }
}
