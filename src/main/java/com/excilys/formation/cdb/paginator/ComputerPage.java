package com.excilys.formation.cdb.paginator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.formation.cdb.exceptions.PageException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

@Component("computerPageBean")
public class ComputerPage extends Page<Computer> {
    @Autowired
    private ComputerService computerService;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerPage.class);

    public ComputerPage() throws PageException {
        super();
    }

    @Override
    protected void refreshContent() throws PageException {
        try {
            setContent(computerService.getList(getOffset(), getNbPerPage(), getOrderBy(), getOrderDesc()));
        } catch (ServiceException e) {
            LOGGER.error("Error while refreshing page content", e);
            throw(new PageException("Error while refreshing page content", e));
        }
    }

}
