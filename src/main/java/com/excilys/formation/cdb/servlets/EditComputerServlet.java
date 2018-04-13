package com.excilys.formation.cdb.servlets;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;


@WebServlet("/editComputer")
@Component("editComputerBean")
public class EditComputerServlet extends ManageComputerServlet {
    @Autowired
    private ComputerService computerService;

    static final Logger LOGGER = LoggerFactory.getLogger(EditComputerServlet.class);

    private static final long serialVersionUID = -9075918957449353325L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Computer computer = new Computer();

        try {
            String idString = request.getParameter("computerId");
            long id = Long.parseLong(idString);
            computer.setId(id);
            LOGGER.debug("Computer id set to {}", id);
        } catch (NumberFormatException e) {
            LOGGER.error("Error: invalid computer id. Update cancelled", e);
            throw(new ServletException("Error: invalid computer id. Update cancelled", e));
        }

        requestToComputer(LOGGER, request, computer, formatter);

        Computer res;
        try {
            res = computerService.updateComputer(computer);
        } catch (ServiceException e) {
            String errorMsg = "Error while getting computer details";
            LOGGER.error(errorMsg, e);
            throw(new ServletException(errorMsg, e));
        }

        checkAndRedirect(request, response, computer, res, "/WEB-INF/JSP/editComputer.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCompanyDtoListInRequest(LOGGER, request);

        long id;
        Computer computer;

        try {
            id = Long.parseLong(request.getParameter("computerId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Error while getting computer details", e);
            throw(new ServletException("Bad id format", e));
        }

        try {
            Optional<Computer> optCpt = computerService.getById(id);

            if (optCpt.isPresent()) {
                computer = optCpt.get();
            } else {
                LOGGER.error("No computer matching id {}", id);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/404.jsp");
                rd.forward(request,response);
                return;
            }
        } catch (ServiceException e) {
            LOGGER.error("Error while retrieving computer n°{}", id, e);
            throw(new ServletException("Error while retrieving computer n°" + id, e));
        }

        ComputerDto computerDto = ComputerMapper.INSTANCE.computerToComputerDto(computer);
        CompanyDto companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(computer.getCompany());
        request.setAttribute("computer", computerDto);
        request.setAttribute("company", companyDto);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/editComputer.jsp");
        rd.forward(request,response);
    }
}
