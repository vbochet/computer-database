package com.excilys.formation.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.formation.cdb.exceptions.PageException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.paginator.ComputerDtoPage;
import com.excilys.formation.cdb.service.ComputerService;

@WebServlet("/dashboard")
@Component("dashboardServletBean")
public class DashboardServlet extends HttpServlet {
    @Autowired
    private ComputerService computerService;

    static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

    private static final long serialVersionUID = -8941279631510488886L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

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
                String errorMsg = "Error: invalid computer id. Deletion cancelled";
                LOGGER.error(errorMsg, e);
                throw(new ServletException(errorMsg, e));
            }
        }

        try {
            computerService.deleteManyById(ids);
        } catch (ServiceException e) {
            String errorMsg = "Error while deleting computers";
            LOGGER.error(errorMsg, e);
            throw(new ServletException(errorMsg, e));
        }
    
        request.setAttribute("deletionSuccess", true);
        
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ComputerDtoPage page;
        try {
            page = new ComputerDtoPage();
            page.setComputerService(computerService);
        } catch (PageException e) {
            String errorMsg = "Error while constructing page";
            LOGGER.error(errorMsg, e);
            throw(new ServletException(errorMsg, e));
        }

        try {
            page.setNbPerPage(Integer.parseInt(request.getParameter("displayBy")));
        } catch (NumberFormatException e) {
            // do nothing
        } catch (PageException e) {
            String errorMsg = "Error while setting number of computers to display per page";
            LOGGER.error(errorMsg, e);
            throw(new ServletException(errorMsg, e));
        }

        try {
            page.setNbTotal(computerService.getNbFound());
        } catch (ServiceException e) {
            String errorMsg = "Error while retrieving the number of computers in database";
            LOGGER.error(errorMsg, e);
            throw(new ServletException(errorMsg, e));
        }
        
        if (request.getParameter("search") != null) {
            page.setSearch(request.getParameter("search"));
        }

        try {
            page.setCurrentPage(Integer.parseInt(request.getParameter("npage")));
        } catch (NumberFormatException e) {
            // do nothing 
        } catch (PageException e) {
            String errorMsg = "Error while setting current page number";
            LOGGER.error(errorMsg, e);
            throw(new ServletException(errorMsg, e));
        }

        String errorMsgOrder = "Error while setting ordering parameter";
        try {
            page.setOrderBy(request.getParameter("orderBy"));
        } catch (PageException e) {
            LOGGER.error(errorMsgOrder, e);
            throw(new ServletException(errorMsgOrder, e));
        }

        try {
            page.setOrderDesc(Boolean.valueOf(request.getParameter("orderDesc")));
        } catch (PageException e) {
            LOGGER.error(errorMsgOrder, e);
            throw(new ServletException(errorMsgOrder, e));
        }


        if (request.getParameter("next") != null) {
            try {
                page.next();
            } catch (PageException e) {
                String errorMsg = "Error while going to next page";
                LOGGER.error(errorMsg, e);
                throw(new ServletException(errorMsg, e));
            }
        }
        else if (request.getParameter("prev") != null) {
            try {
                page.prev();
            } catch (PageException e) {
                String errorMsg = "Error while going to previous page";
                LOGGER.error(errorMsg, e);
                throw(new ServletException(errorMsg, e));
            }
        }


        request.setAttribute("page", page);
        LOGGER.debug("Number of computers found in database : {}", page.getNbTotal());
        LOGGER.debug("Number of computers stored in computersList: {}", page.getContent().size());
        LOGGER.debug("Maximum page number: {}", page.getMaxPage());
        LOGGER.debug("Current page number: {}", page.getCurrentPage());
        LOGGER.debug("Number of computers per page: {}", page.getNbPerPage());
        LOGGER.debug("Page elements ordered by: {}", page.getOrderBy());
        LOGGER.debug("Page elements order desc: {}", page.getOrderDesc());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/dashboard.jsp");
        rd.forward(request,response);
    }

}
