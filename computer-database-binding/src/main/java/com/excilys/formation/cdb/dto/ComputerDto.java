package com.excilys.formation.cdb.dto;

import org.springframework.stereotype.Component;

@Component("computerdto")
public class ComputerDto {

    private long computerId;
    private String computerName;
    private String computerIntroduced;
    private String computerDiscontinued;
    private long computerCompanyId;
    private String computerCompanyName;

    public long getComputerId() {
        return computerId;
    }
    public void setComputerId(long computerId) {
        this.computerId = computerId;
    }

    public String getComputerName() {
        return computerName;
    }
    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getComputerIntroduced() {
        return computerIntroduced;
    }
    public void setComputerIntroduced(String computerIntroduced) {
        this.computerIntroduced = computerIntroduced;
    }

    public String getComputerDiscontinued() {
        return computerDiscontinued;
    }
    public void setComputerDiscontinued(String computerDiscontinued) {
        this.computerDiscontinued = computerDiscontinued;
    }
    
    public long getComputerCompanyId() {
        return computerCompanyId;
    }
    public void setComputerCompanyId(long computerCompanyId) {
        this.computerCompanyId = computerCompanyId;
    }
    public String getComputerCompanyName() {
        return computerCompanyName;
    }
    public void setComputerCompanyName(String computerCompanyName) {
        this.computerCompanyName = computerCompanyName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("COMPUTER\n");
        sb.append("  id: ");
        sb.append(this.getComputerId());
        sb.append("\n  name: ");
        sb.append(this.getComputerName());
        sb.append("\n  introduced: ");
        sb.append(this.getComputerIntroduced());
        sb.append("\n  discontinued: ");
        sb.append(this.getComputerDiscontinued());
        sb.append("\n  company: ");
        sb.append("\n    id: ");
        sb.append(this.getComputerCompanyId());
        sb.append("\n    name: ");
        sb.append(this.getComputerCompanyName());
        sb.append("\n");
        return sb.toString();
    }

}
