package com.excilys.formation.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.paginator.ComputerDtoPage;
import com.excilys.formation.cdb.service.ComputerService;

@Controller
@Component("dashboardServletBean")
public class DashboardController {
    @Autowired
    private ComputerService computerService;

    static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    @PostMapping("/dashboard")
    public ModelAndView doPost(ServletRequest request, ServletResponse response) throws ServletException, IOException {
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

        computerService.deleteManyById(ids);
        request.setAttribute("deletionSuccess", true);

        return doGet(request, response);
    }

    @GetMapping("/dashboard")
    public ModelAndView doGet(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        ComputerDtoPage page;
        page = new ComputerDtoPage();
        page.setComputerService(computerService);

        try {
            page.setNbPerPage(Integer.parseInt(request.getParameter("displayBy")));
        } catch (NumberFormatException e) {
            // do nothing
        }

        page.setNbTotal(computerService.getNbFound());
        
        if (request.getParameter("search") != null) {
            page.setSearch(request.getParameter("search"));
        }

        try {
            page.setCurrentPage(Integer.parseInt(request.getParameter("npage")));
        } catch (NumberFormatException e) {
            // do nothing 
        }

        page.setOrderBy(request.getParameter("orderBy"));
        page.setOrderDesc(Boolean.valueOf(request.getParameter("orderDesc")));


        if (request.getParameter("next") != null) {
            page.next();
        }
        else if (request.getParameter("prev") != null) {
            page.prev();
        }

        LOGGER.debug("Number of computers found in database : {}", page.getNbTotal());
        LOGGER.debug("Number of computers stored in computersList: {}", page.getContent().size());
        LOGGER.debug("Maximum page number: {}", page.getMaxPage());
        LOGGER.debug("Current page number: {}", page.getCurrentPage());
        LOGGER.debug("Number of computers per page: {}", page.getNbPerPage());
        LOGGER.debug("Page elements ordered by: {}", page.getOrderBy());
        LOGGER.debug("Page elements order desc: {}", page.getOrderDesc());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("dashboard");
        mav.addObject("page", page);
        mav.addObject("deletionSuccess", request.getAttribute("deletionSuccess"));

        return new ModelAndView("dashboard", "page", page);
    }

}
