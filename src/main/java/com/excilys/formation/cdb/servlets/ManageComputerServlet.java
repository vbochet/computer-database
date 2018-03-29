package com.excilys.formation.cdb.servlets;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.slf4j.Logger;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;

public class ManageComputerServlet extends HttpServlet {

    private static final long serialVersionUID = 3330375264799217747L;

    protected void requestToComputer(final Logger LOGGER, HttpServletRequest request, Computer computer, DateTimeFormatter formatter) throws ServletException {
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
                optCompany = CompanyService.INSTANCE.getById(companyId);

            if (optCompany.isPresent()) {
                computer.setCompany(optCompany.get());
                LOGGER.debug("Company set to {}", optCompany.get().toString());
            } else {
                computer.setCompany(null);
                LOGGER.debug("Company set to null");
            }

        } catch (ServiceException e) {
            LOGGER.error("Error while getting company details", e);
            throw(new ServletException("Error while getting company details", e));
        } catch (NumberFormatException e) {
            computer.setCompany(null);
            LOGGER.debug("Company set to null (received value \"{}\")", request.getParameter("companyId"));
        }
    }

    protected void setCompanyDtoListInRequest(Logger logger, HttpServletRequest request) throws ServletException {
        List<Company> companyList;
        try {
            companyList = CompanyService.INSTANCE.getList(0, (int)CompanyService.INSTANCE.getNbFound());
        } catch (ServiceException e) {
            logger.error("Error while getting company list", e);
            throw(new ServletException("Error while getting company list", e));
        }
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Consumer<Company> companyConsumer = x -> companyDtoList.add(CompanyMapper.INSTANCE.companyToCompanyDto(x));
        companyList.forEach(companyConsumer);
        
        request.setAttribute("companyList", companyDtoList);
    }
}
