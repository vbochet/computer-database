package com.excilys.formation.cdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id")
    private long id;

    @Column(name = "name", length = 255)
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
                return this.sameName(otherc);
            }
            return false;
        }
        return false;
    }

    private boolean sameName(Company other) {
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(id).append(name).toHashCode();
    }
}
