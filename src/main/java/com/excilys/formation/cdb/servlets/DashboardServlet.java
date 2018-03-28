package com.excilys.formation.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.exceptions.PageException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.paginator.ComputerDtoPage;
import com.excilys.formation.cdb.service.ComputerService;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

    private static final long serialVersionUID = -8941279631510488886L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Long> ids = new ArrayList<>();

        String idsString = request.getParameter("selection");
        LOGGER.debug("Ids received for deletion: {}", idsString);
      
        String[] idsList = idsString.split(",");
        for (String idString : idsList) {
            try {
                ids.add(Long.valueOf(idString));
            } catch (NumberFormatException e) {
                LOGGER.error("Error: invalid computer id. Deletion cancelled", e);
                throw(new ServletException("Error: invalid computer id. Deletion cancelled", e));
            }
        }

        try {
            ComputerService.INSTANCE.deleteManyById(ids);
        } catch (ServiceException e) {
            LOGGER.error("Error while deleting computers", e);
            throw(new ServletException("Error while deleting computers", e));
        }
    
        request.setAttribute("deletionSuccess", true);
        
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ComputerDtoPage page;
        try {
            page = new ComputerDtoPage();
        } catch (PageException e) {
            LOGGER.error("Error while constructing page", e);
            throw(new ServletException("Error while constructing page", e));
        }

        try {
            page.setNbPerPage(Integer.parseInt(request.getParameter("displayBy")));
        } catch (NumberFormatException e) { 
        } catch (PageException e) {
            LOGGER.error("Error while setting number of computers to display per page", e);
            throw(new ServletException("Error while setting number of computers to display per page", e));
        }

        try {
            page.setNbTotal(ComputerService.INSTANCE.getNbFound());
        } catch (ServiceException e) {
            LOGGER.error("Error while retrieving the amount of computers in database", e);
            throw(new ServletException("Error while retrieving the amount of computers in database", e));
        }
        
        if (request.getParameter("search") != null) {
            page.setSearch(request.getParameter("search"));
        }

        try {
            page.setCurrentPage(Integer.parseInt(request.getParameter("npage")));
        } catch (NumberFormatException e) { 
        } catch (PageException e) {
            LOGGER.error("Error while setting current page number", e);
            throw(new ServletException("Error while setting current page number", e));
        }

        try {
            page.setOrderBy(request.getParameter("orderBy"));
        } catch (PageException e) {
            LOGGER.error("Error while setting ordering parameter", e);
            throw(new ServletException("Error while setting ordering parameter", e));
        }

        try {
            page.setOrderDesc(Boolean.valueOf(request.getParameter("orderDesc")));
        } catch (PageException e) {
            LOGGER.error("Error while setting ordering parameter", e);
            throw(new ServletException("Error while setting ordering parameter", e));
        }


        if (request.getParameter("next") != null) {
            try {
                page.next();
            } catch (PageException e) {
                LOGGER.error("Error while going to next page", e);
                throw(new ServletException("Error while going to next page", e));
            }
        }
        else if (request.getParameter("prev") != null) {
            try {
                page.prev();
            } catch (PageException e) {
                LOGGER.error("Error while going to previous page", e);
                throw(new ServletException("Error while going to previous page", e));
            }
        }


        request.setAttribute("page", page);
        LOGGER.info("Number of computers found in database : {}", page.getNbTotal());
        LOGGER.info("Number of computers stored in computersList: {}", page.getContent().size());
        LOGGER.info("Maximum page number: {}", page.getMaxPage());
        LOGGER.info("Current page number: {}", page.getCurrentPage());
        LOGGER.info("Number of computers per page: {}", page.getNbPerPage());
        LOGGER.info("Page elements ordered by: {}", page.getOrderBy());
        LOGGER.info("Page elements order desc: {}", page.getOrderDesc());

        try {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/dashboard.jsp");
            rd.forward(request,response);
       } catch (Exception e) { 
           throw new ServletException(e);
       }
    }

}
