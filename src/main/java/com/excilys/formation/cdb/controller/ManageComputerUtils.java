package com.excilys.formation.cdb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;

@Component("manageComputerUtilsBean")
public class ManageComputerUtils {

    @Autowired
    private CompanyService companyService;


    public Computer requestToComputer(final Logger LOGGER, HttpServletRequest request, DateTimeFormatter formatter)  {
        Computer computer = new Computer();
        
        String name = request.getParameter("computerName");
        computer.setName(name);
        LOGGER.debug("Name set to \"{}\"", name);

        try {
            String introString = request.getParameter("introduced");
            LocalDate introLD = LocalDate.parse(introString, formatter);
            computer.setIntroduced(introLD);
            LOGGER.debug("Introduction date set to {}", introLD);
        } catch (DateTimeParseException e) {
            computer.setIntroduced(null);
            LOGGER.debug("Introduction date set to null (received value \"{}\")", request.getParameter("introduced"));
        }

        try {
            String discontString = request.getParameter("discontinued");
            LocalDate discontLD = LocalDate.parse(discontString, formatter);
            computer.setDiscontinued(discontLD);
            LOGGER.debug("Discontinuation date set to {}", discontLD);
        } catch (DateTimeParseException e) {
            computer.setDiscontinued(null);
            LOGGER.debug("Discontinuation date set to null (received value \"{}\")", request.getParameter("discontinued"));
        }

        try {
            long companyId = Long.parseLong(request.getParameter("companyId"));

            Optional<Company> optCompany;
                optCompany = companyService.getById(companyId);

            if (optCompany.isPresent()) {
                computer.setCompany(optCompany.get());
                LOGGER.debug("Company set to {}", optCompany.get().toString());
            } else {
                computer.setCompany(null);
                LOGGER.debug("Company set to null");
            }

        } catch (NumberFormatException e) {
            computer.setCompany(null);
            LOGGER.debug("Company set to null (received value \"{}\")", request.getParameter("companyId"));
        }
        
        return computer;
    }

    public void setCompanyDtoListInMAV(Logger logger, ModelAndView mav) {
        List<Company> companyList;
        companyList = companyService.getList(0, (int)companyService.getNbFound());
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Consumer<Company> companyConsumer = x -> companyDtoList.add(CompanyMapper.INSTANCE.companyToCompanyDto(x));
        companyList.forEach(companyConsumer);
        
        mav.addObject("companyList", companyDtoList);
    }
}
