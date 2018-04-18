package com.excilys.formation.cdb.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

@Controller
@Component("editComputerControllerBean")
public class EditComputerController {
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ManageComputerUtils manageComputerUtils;

    static final Logger LOGGER = LoggerFactory.getLogger(EditComputerController.class);

    @PostMapping("/editComputer")
    public ModelAndView doPost(@RequestParam Map<String, String> parameters) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Computer computer = manageComputerUtils.requestToComputer(LOGGER, parameters, formatter);

        try {
            String idString = parameters.get("computerId");
            long id = Long.parseLong(idString);
            computer.setId(id);
            LOGGER.debug("Computer id set to {}", id);
        } catch (NumberFormatException e) {
            LOGGER.error("Error: invalid computer id. Update cancelled", e);
            throw(new ServletException("Error: invalid computer id. Update cancelled", e));
        }

        Computer res = computerService.updateComputer(computer);


        ModelAndView mav = new ModelAndView();

        if (res != null) {
            mav.setViewName("computerAdded");
            mav.addObject("computer", ComputerMapper.INSTANCE.computerToComputerDto(res));
        } else {
            mav.setViewName("editComputer");
            mav.addObject("error", true);
            manageComputerUtils.setCompanyDtoListInMAV(LOGGER, mav);
            mav.addObject("computer", ComputerMapper.INSTANCE.computerToComputerDto(computer));
        }
        
        return mav;
    }

    @GetMapping("/editComputer")
    public ModelAndView doGet(@RequestParam Map<String, String> parameters) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView();        
        manageComputerUtils.setCompanyDtoListInMAV(LOGGER, mav);
        
        long id;
        Computer computer;

        try {
            id = Long.parseLong(parameters.get("computerId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Error while getting computer details", e);
            throw(new ServletException("Bad id format", e));
        }

        Optional<Computer> optCpt = computerService.getById(id);

        if (optCpt.isPresent()) {
            computer = optCpt.get();
        } else {
            LOGGER.error("No computer matching id {}", id);
            mav.setViewName("404");
            return mav;
        }

        ComputerDto computerDto = ComputerMapper.INSTANCE.computerToComputerDto(computer);
        CompanyDto companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(computer.getCompany());
        
        mav.addObject("computer", computerDto);
        mav.addObject("company", companyDto);
        mav.setViewName("editComputer");

        return mav;
    }

}
