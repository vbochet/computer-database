package com.excilys.formation.cdb.paginator;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.exceptions.PageException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

public class ComputerPage extends Page<Computer> {

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerPage.class);

    public ComputerPage() throws PageException {
        super();
    }

    @Override
    protected void refreshContent() throws PageException {
        try {
            setContent(ComputerService.INSTANCE.getList(getOffset(), getNbPerPage(), getOrderBy()));
        } catch (ServiceException e) {
            LOGGER.error("Error while refreshing page content", e);
            throw(new PageException("Error while refreshing page content", e));
        }
    }

    @Override
    public void setOrderBy(String orderBy) throws PageException {
        List<String> allowedOrders = Arrays.asList(new String[] {"id", "name", "introduced", "discontinued", "company"});
        List<String> ordersMap = Arrays.asList(new String[] {"computer.id", "computer.name", "computer.introduced", "computer.discontinued", "computer.company_name"});

        if (allowedOrders.contains(orderBy)) {
            this.orderBy = ordersMap.get(allowedOrders.indexOf(orderBy));
            this.refreshContent();
        }
    }

}
