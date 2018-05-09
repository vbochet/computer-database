package com.excilys.formation.cdb.dto;

public class CompanyDto {

    private long companyId;
    private String companyName;

    public long getCompanyId() {
        return companyId;
    }
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("COMPANY\n");
        sb.append("  id: ");
        sb.append(this.getCompanyId());
        sb.append("\n  name: ");
        sb.append(this.getCompanyName());
        sb.append("\n");
        return sb.toString();
    }

}
