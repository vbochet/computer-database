package com.excilys.formation.cdb.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;
import com.excilys.formation.cdb.validator.ComputerDtoValidator;

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
    @Autowired
    CompanyMapper companyMapper;
    @Autowired
    private ComputerDtoValidator computerDtoValidator;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);


    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(computerDtoValidator);
    }


    @PostMapping("/add")
    public ModelAndView addPost(@Validated @ModelAttribute("computerDto") ComputerDto computerDto, BindingResult bindingResult, Model model) {
        Computer computer;
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            LOGGER.error("Error in ComputerDto fields, going back to add page");
            mav.setViewName("addComputer");
            mav.addObject("error", true);
            mav.addObject("computer", computerDto);
            return mav;
        }

        computer = computerMapper.computerDtoToComputer(computerDto);

        Computer res = computerService.createComputer(computer);

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
    public ModelAndView addGet(@RequestParam Map<String, String> parameters) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addComputer");
        
        setCompanyDtoListInMAV(mav);
        mav.addObject("computerDto", new ComputerDto());

        return mav;
    }

    @PostMapping("/edit")
    public ModelAndView editPost(@Validated @ModelAttribute("computerDto") ComputerDto computerDto, BindingResult bindingResult, Model model) {
        Computer computer;
        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            LOGGER.error("Error in ComputerDto fields, going back to edit page");
            mav.setViewName("editComputer");
            mav.addObject("error", true);
            mav.addObject("computerDto", computerDto);
            return mav;
        }

        computer = computerMapper.computerDtoToComputer(computerDto);
        
        Computer res = computerService.updateComputer(computer);



        if (res != null) {
            mav.setViewName("computerAdded");
            mav.addObject("computer", computerMapper.computerToComputerDto(res));
        } else {
            mav.setViewName("editComputer");
            mav.addObject("error", true);
            setCompanyDtoListInMAV(mav);
            mav.addObject("computer", computerMapper.computerToComputerDto(computer));
        }
        
        return mav;
    }

    @GetMapping("/edit")
    public ModelAndView editGet(@RequestParam Map<String, String> parameters) throws ServletException {
        ModelAndView mav = new ModelAndView();        
        setCompanyDtoListInMAV(mav);
        
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
        
        mav.addObject("computerDto", computerDto);
        mav.setViewName("editComputer");

        return mav;
    }


    private void setCompanyDtoListInMAV(ModelAndView mav) {
        List<Company> companyList;
        companyList = companyService.getList(0, (int)companyService.getNbFound());
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Consumer<Company> companyConsumer = x -> companyDtoList.add(companyMapper.companyToCompanyDto(x));
        companyList.forEach(companyConsumer);
        
        mav.addObject("companyList", companyDtoList);
    }
}
