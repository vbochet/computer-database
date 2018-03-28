package com.excilys.formation.cdb.paginator;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.exceptions.PageException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.service.CompanyService;

public class CompanyPage extends Page<Company> {

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyPage.class);

    public CompanyPage() throws PageException {
        super();
    }

    @Override
    protected void refreshContent() throws PageException {
        try {
            setContent(CompanyService.INSTANCE.getList(getOffset(), getNbPerPage()));
        } catch (ServiceException e) {
            LOGGER.error("Error while refreshing page content", e);
            throw(new PageException("Error while refreshing page content", e));
        }
    }

    @Override
    public void setOrderBy(String orderBy) throws PageException {
        List<String> allowedOrders = Arrays.asList(new String[] {});
        List<String> ordersMap = Arrays.asList(new String[] {});

        if (allowedOrders.contains(orderBy)) {
            this.orderBy = ordersMap.get(allowedOrders.indexOf(orderBy));
            this.refreshContent();
        }
    }

}
