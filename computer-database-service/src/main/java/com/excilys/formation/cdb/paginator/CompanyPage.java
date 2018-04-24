package com.excilys.formation.cdb.paginator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.service.CompanyService;

public class CompanyPage extends Page<Company> {
    private CompanyService companyService;

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyPage.class);

    public CompanyPage() {
        super();
    }

    @Override
    protected void refreshContent() {
        setContent(companyService.getList(getOffset(), getNbPerPage()));
    }

}
