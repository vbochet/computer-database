package com.excilys.formation.cdb.paginator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.exceptions.PageException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

public class ComputerDtoPage extends Page<ComputerDto> {

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerDtoPage.class);

    public ComputerDtoPage() throws PageException {
        super();
    }

    @Override
    protected void refreshContent() throws PageException {
        List<Computer> computerList;
        try {
            computerList = ComputerService.INSTANCE.getList(getOffset(), getNbPerPage(), getOrderBy(), getOrderDesc());
            LOGGER.debug("refreshContentOrderBy: " + orderBy);
        } catch (ServiceException e) {
            LOGGER.error("Error while refreshing page content", e);
            throw(new PageException("Error while refreshing page content", e));
        }
        List<ComputerDto> computerDtoList = new ArrayList<>();
        Consumer<Computer> computerConsumer = (x) -> computerDtoList.add(ComputerMapper.INSTANCE.computerToComputerDto(x));
        computerList.forEach(computerConsumer);
        setContent(computerDtoList);
    }

}
