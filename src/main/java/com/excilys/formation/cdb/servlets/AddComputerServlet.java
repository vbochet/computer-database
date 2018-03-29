package com.excilys.formation.cdb.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;


@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {

    static final Logger LOGGER = LoggerFactory.getLogger(AddComputerServlet.class);

    private static final long serialVersionUID = 4171669005687350388L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
            try {
                optCompany = CompanyService.INSTANCE.getById(companyId);
            } catch (ServiceException e) {
                LOGGER.error("Error while getting company details", e);
                throw(new ServletException("Error while getting company details", e));
            }

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
        
        Computer res;
        try {
            res = ComputerService.INSTANCE.createComputer(computer);
        } catch (ServiceException e) {
            LOGGER.error("Error while creating computer", e);
            throw(new ServletException("Error while creating computer", e));
        }

        if (res != null) {
            request.setAttribute("computer", ComputerMapper.INSTANCE.computerToComputerDto(res));

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/computerAdded.jsp");
            rd.forward(request,response);
        } else {
            request.setAttribute("error", true);
            request.setAttribute("computer", ComputerMapper.INSTANCE.computerToComputerDto(computer));

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/addComputer.jsp");
            rd.forward(request,response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Company> companyList;
        try {
            companyList = CompanyService.INSTANCE.getList(0, (int)CompanyService.INSTANCE.getNbFound());
        } catch (ServiceException e) {
            LOGGER.error("Error while getting company list", e);
            throw(new ServletException("Error while getting company list", e));
        }
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Consumer<Company> companyConsumer = (x) -> companyDtoList.add(CompanyMapper.INSTANCE.companyToCompanyDto(x));
        companyList.forEach(companyConsumer);
        
        request.setAttribute("companyList", companyDtoList);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/addComputer.jsp");
        rd.forward(request,response);
    }
}
