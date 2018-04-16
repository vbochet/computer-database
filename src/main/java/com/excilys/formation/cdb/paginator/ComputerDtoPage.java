package com.excilys.formation.cdb.paginator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

public class ComputerDtoPage extends Page<ComputerDto> {

    private ComputerService computerService;

    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerDtoPage.class);

    public ComputerDtoPage() {
        super();
    }

    @Override
    protected void refreshContent() {
        List<Computer> computerList;
        long nb = getNbTotal();
        
        if (!getSearch().isEmpty()) {
            computerList = computerService.getSearchList(getOffset(), getNbPerPage(), getOrderBy(), getOrderDesc(), getSearch());
            nb = computerService.getNbSearch(getSearch());
        } else {
            computerList = computerService.getList(getOffset(), getNbPerPage(), getOrderBy(), getOrderDesc());
        }

        LOGGER.debug("refreshContentOrderBy: {}", orderBy);
        List<ComputerDto> computerDtoList = new ArrayList<>();
        Consumer<Computer> computerConsumer = x -> computerDtoList.add(ComputerMapper.INSTANCE.computerToComputerDto(x));
        computerList.forEach(computerConsumer);
        setContent(computerDtoList);
        setNbTotal(nb);
    }

}
