package com.excilys.formation.cdb.controller;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.paginator.ComputerDtoPage;
import com.excilys.formation.cdb.service.ComputerService;

@Controller
@Component("dashboardControllerBean")
public class DashboardController {
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ComputerMapper computerMapper;

    static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping("/dashboard")
    public ModelAndView doGet(Locale locale, @RequestParam Map<String, String> parameters) {
        ComputerDtoPage page;
        page = new ComputerDtoPage(computerService, computerMapper);

        try {
            page.setNbPerPage(Integer.parseInt(parameters.get("displayBy")));
        } catch (NumberFormatException e) {
            // do nothing
        }

        page.setNbTotal(computerService.getNbFound());
        
        if (parameters.get("search") != null) {
            page.setSearch(parameters.get("search"));
        }

        try {
            page.setCurrentPage(Integer.parseInt(parameters.get("npage")));
        } catch (NumberFormatException e) {
            // do nothing 
        }

        page.setOrderBy(parameters.get("orderBy"));
        page.setOrderDesc(Boolean.valueOf(parameters.get("orderDesc")));


        if (parameters.get("next") != null) {
            page.next();
        }
        else if (parameters.get("prev") != null) {
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
        mav.addObject("deletionSuccess", parameters.get("deletionSuccess"));

        Locale currentLocale = LocaleContextHolder.getLocale();
        mav.addObject("locale", currentLocale);


        return new ModelAndView("dashboard", "page", page);
    }

}
