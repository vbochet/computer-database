package com.excilys.formation.cdb.servlets;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

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

import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;


@WebServlet("/addComputer")
@Component("addComputerServletBean")
public class AddComputerServlet extends ManageComputerServlet {
    @Autowired
    private ComputerService computerService;

    static final Logger LOGGER = LoggerFactory.getLogger(AddComputerServlet.class);

    private static final long serialVersionUID = 4171669005687350388L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Computer computer = new Computer();

        requestToComputer(LOGGER, request, computer, formatter);

        Computer res = computerService.createComputer(computer);

        checkAndRedirect(request, response, computer, res, "/WEB-INF/JSP/addComputer.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCompanyDtoListInRequest(LOGGER, request);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/addComputer.jsp");
        rd.forward(request,response);
    }
}
