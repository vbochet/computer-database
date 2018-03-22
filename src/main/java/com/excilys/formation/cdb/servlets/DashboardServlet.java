package com.excilys.formation.cdb.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.paginator.ComputerDtoPage;
import com.excilys.formation.cdb.service.ComputerService;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

    private static final long serialVersionUID = -8941279631510488886L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ComputerDtoPage page = new ComputerDtoPage();

        page.setNbTotal(ComputerService.INSTANCE.getNbFound());

        try {
            page.setNbPerPage(Integer.parseInt(request.getParameter("displayBy")));
        } catch (NumberFormatException e) { }

        try {
            page.setCurrentPage(Integer.parseInt(request.getParameter("npage")));
        } catch (NumberFormatException e) { }


        if (request.getParameter("next") != null) {
            page.next();
        }
        else if (request.getParameter("prev") != null) {
            page.prev();
        }


        request.setAttribute("nbComputersFound", page.getNbTotal());
        LOGGER.info("Number of computers found in database : {}", page.getNbTotal());
        
        request.setAttribute("computersList", page.getContent());
        LOGGER.info("Number of computers stored in computersList: {}", page.getContent().size());
        
        long maxPage = page.getNbTotal() / page.getNbPerPage();
        if (page.getNbTotal() % page.getNbPerPage() != 0) {
            maxPage++;
        }
        request.setAttribute("maxPage", maxPage);
        LOGGER.info("Maximum page number: {}", maxPage);

        request.setAttribute("currentPage", page.getCurrentPage());
        LOGGER.info("Current page number: {}", page.getCurrentPage());

        request.setAttribute("displayBy", page.getNbPerPage());
        LOGGER.info("Number of computers per page: {}", page.getNbPerPage());

        try {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/dashboard.jsp");
            rd.forward(request,response);
       } catch (Exception e) { 
           throw new ServletException(e);
       }
    }

}
