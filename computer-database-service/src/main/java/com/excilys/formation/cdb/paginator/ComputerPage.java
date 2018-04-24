package com.excilys.formation.cdb.paginator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

public class ComputerPage extends Page<Computer> {

    private ComputerService computerService;

    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerPage.class);

    public ComputerPage() {
        super();
    }

    @Override
    protected void refreshContent() {
        setContent(computerService.getList(getOffset(), getNbPerPage(), getOrderBy(), getOrderDesc()));
    }

}
