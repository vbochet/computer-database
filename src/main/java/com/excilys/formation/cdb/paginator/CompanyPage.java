package com.excilys.formation.cdb.paginator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.formation.cdb.exceptions.PageException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.service.CompanyService;

@Component("companyPageBean")
public class CompanyPage extends Page<Company> {
    @Autowired
    private CompanyService companyService;

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyPage.class);

    public CompanyPage() throws PageException {
        super();
    }

    @Override
    protected void refreshContent() throws PageException {
        try {
            setContent(companyService.getList(getOffset(), getNbPerPage()));
        } catch (ServiceException e) {
            LOGGER.error("Error while refreshing page content", e);
            throw(new PageException("Error while refreshing page content", e));
        }
    }

}
