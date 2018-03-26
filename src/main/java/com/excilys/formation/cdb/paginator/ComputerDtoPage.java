package com.excilys.formation.cdb.paginator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

public class ComputerDtoPage extends Page<ComputerDto> {

    @Override
    protected void refreshContent() {
        List<Computer> computerList = ComputerService.INSTANCE.getList(getOffset(), getNbPerPage());
        List<ComputerDto> computerDtoList = new ArrayList<>();
        Consumer<Computer> computerConsumer = (x) -> computerDtoList.add(ComputerMapper.INSTANCE.computerToComputerDto(x));
        computerList.forEach(computerConsumer);
        setContent(computerDtoList);
    }

}
