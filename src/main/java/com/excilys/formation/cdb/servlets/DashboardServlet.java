package com.excilys.formation.cdb.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.formation.cdb.service.ComputerService;

public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = -8941279631510488886L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("nbComputersFound", ComputerService.INSTANCE.getNbFound());
            
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/dashboard.jsp");
            rd.forward(request,response);
       } catch (Exception e) { 
           throw new ServletException(e);
       }
    }

}
