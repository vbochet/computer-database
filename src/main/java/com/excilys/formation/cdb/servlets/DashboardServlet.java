package com.excilys.formation.cdb.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.paginator.ComputerDtoPage;
import com.excilys.formation.cdb.service.ComputerService;

public class DashboardServlet extends HttpServlet {

    static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

    private static final long serialVersionUID = -8941279631510488886L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ComputerDtoPage page = null;
        HttpSession session = request.getSession();

        if (session.getAttribute("page") != null) {
            page = (ComputerDtoPage)session.getAttribute("page");
            LOGGER.info("Found a page instance in session");
        } else {
            page = new ComputerDtoPage();
            LOGGER.info("No page instance in session, created a new one");
        }
        page.setNbTotal(ComputerService.INSTANCE.getNbFound());

        try {
            int nbPerPage = Integer.parseInt(request.getParameter("displayBy"));
            page.setNbPerPage(nbPerPage);
        } catch (NumberFormatException e) { }

        if (request.getParameter("next") != null) {
            page.next();
        }
        else if (request.getParameter("prev") != null) {
            page.prev();
        }
        else {
            try {
                int pageNumber = Integer.parseInt(request.getParameter("npage"));
                page.setCurrentPage(pageNumber);
            } catch (NumberFormatException e) { }
        }
        

        session.setAttribute("page", page);

        request.setAttribute("nbComputersFound", page.getNbTotal());
        LOGGER.info("Number of computers found in database : {}", ComputerService.INSTANCE.getNbFound());
        
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

        try {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/dashboard.jsp");
            rd.forward(request,response);
       } catch (Exception e) { 
           throw new ServletException(e);
       }
    }

}
