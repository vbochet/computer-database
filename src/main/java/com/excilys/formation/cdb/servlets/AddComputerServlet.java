package com.excilys.formation.cdb.servlets;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;


@WebServlet("/addComputer")
public class AddComputerServlet extends ManageComputerServlet {

    static final Logger LOGGER = LoggerFactory.getLogger(AddComputerServlet.class);

    private static final long serialVersionUID = 4171669005687350388L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Computer computer = new Computer();

        requestToComputer(LOGGER, request, computer, formatter);

        Computer res;
        try {
            res = ComputerService.INSTANCE.createComputer(computer);
        } catch (ServiceException e) {
            LOGGER.error("Error while creating computer", e);
            throw(new ServletException("Error while creating computer", e));
        }

        checkAndRedirect(request, response, computer, res, "/WEB-INF/JSP/addComputer.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCompanyDtoListInRequest(LOGGER, request);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/addComputer.jsp");
        rd.forward(request,response);
    }
}
