package com.excilys.formation.cdb.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.exceptions.MapperException;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;

@Controller
@Component("addComputerControllerBean")
@RequestMapping("/computer")
public class ComputerController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ComputerMapper computerMapper;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

    @PostMapping("/add")
    public ModelAndView addPost(@ModelAttribute("computerDto") ComputerDto computerDto, Model model) throws ServletException, IOException {
        Computer computer;
        try {
            computer = computerMapper.computerDtoToComputer(computerDto);
        } catch (MapperException e) {
            throw new ServletException(e);
        }

        Computer res = computerService.createComputer(computer);

        ModelAndView mav = new ModelAndView();

        if (res != null) {
            mav.setViewName("computerAdded");
            mav.addObject("computer", computerMapper.computerToComputerDto(res));
        } else {
            mav.setViewName("addComputer");
            mav.addObject("error", true);
            mav.addObject("computer", computerMapper.computerToComputerDto(computer));
        }
        
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addGet(@RequestParam Map<String, String> parameters) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addComputer");
        
        setCompanyDtoListInMAV(LOGGER, mav);
        mav.addObject("computerDto", new ComputerDto());

        return mav;
    }

    @PostMapping("/edit")
    public ModelAndView editPost(@RequestParam Map<String, String> parameters) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Computer computer = requestToComputer(LOGGER, parameters, formatter);

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
            mav.addObject("computer", computerMapper.computerToComputerDto(res));
        } else {
            mav.setViewName("editComputer");
            mav.addObject("error", true);
            setCompanyDtoListInMAV(LOGGER, mav);
            mav.addObject("computer", computerMapper.computerToComputerDto(computer));
        }
        
        return mav;
    }

    @GetMapping("/edit")
    public ModelAndView editGet(@RequestParam Map<String, String> parameters) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView();        
        setCompanyDtoListInMAV(LOGGER, mav);
        
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

        ComputerDto computerDto = computerMapper.computerToComputerDto(computer);
        CompanyDto companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(computer.getCompany());
        
        mav.addObject("computer", computerDto);
        mav.addObject("company", companyDto);
        mav.setViewName("editComputer");

        return mav;
    }





    private Computer requestToComputer(final Logger LOGGER, Map<String, String> parameters, DateTimeFormatter formatter)  {
        Computer computer = new Computer();
        
        String name = parameters.get("computerName");
        computer.setName(name);
        LOGGER.debug("Name set to \"{}\"", name);

        try {
            String introString = parameters.get("introduced");
            LocalDate introLD = LocalDate.parse(introString, formatter);
            computer.setIntroduced(introLD);
            LOGGER.debug("Introduction date set to {}", introLD);
        } catch (DateTimeParseException e) {
            computer.setIntroduced(null);
            LOGGER.debug("Introduction date set to null (received value \"{}\")", parameters.get("introduced"));
        }

        try {
            String discontString = parameters.get("discontinued");
            LocalDate discontLD = LocalDate.parse(discontString, formatter);
            computer.setDiscontinued(discontLD);
            LOGGER.debug("Discontinuation date set to {}", discontLD);
        } catch (DateTimeParseException e) {
            computer.setDiscontinued(null);
            LOGGER.debug("Discontinuation date set to null (received value \"{}\")", parameters.get("discontinued"));
        }

        try {
            long companyId = Long.parseLong(parameters.get("companyId"));

            Optional<Company> optCompany;
                optCompany = companyService.getById(companyId);

            if (optCompany.isPresent()) {
                computer.setCompany(optCompany.get());
                LOGGER.debug("Company set to {}", optCompany.get().toString());
            } else {
                computer.setCompany(null);
                LOGGER.debug("Company set to null");
            }

        } catch (NumberFormatException e) {
            computer.setCompany(null);
            LOGGER.debug("Company set to null (received value \"{}\")", parameters.get("companyId"));
        }
        
        return computer;
    }

    private void setCompanyDtoListInMAV(Logger logger, ModelAndView mav) {
        List<Company> companyList;
        companyList = companyService.getList(0, (int)companyService.getNbFound());
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Consumer<Company> companyConsumer = x -> companyDtoList.add(CompanyMapper.INSTANCE.companyToCompanyDto(x));
        companyList.forEach(companyConsumer);
        
        mav.addObject("companyList", companyDtoList);
    }
}
