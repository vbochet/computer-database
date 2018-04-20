package com.excilys.formation.cdb.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component("computerdto")
public class ComputerDto {

    private long computerId;
    @NotBlank
    @NotNull
    @Size(min=2, max=5)
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

}
