package com.excilys.formation.cdb.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

@Controller
@Component("addComputerControllerBean")
public class AddComputerController {
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ManageComputerUtils manageComputerUtils;

    static final Logger LOGGER = LoggerFactory.getLogger(AddComputerController.class);

    @PostMapping("/addComputer")
    public ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Computer computer = manageComputerUtils.requestToComputer(LOGGER, request, formatter);

        Computer res = computerService.createComputer(computer);

        ModelAndView mav = new ModelAndView();

        if (res != null) {
            mav.setViewName("computerAdded");
            mav.addObject("computer", ComputerMapper.INSTANCE.computerToComputerDto(res));
        } else {
            mav.setViewName("addComputer");
            mav.addObject("error", true);
            mav.addObject("computer", ComputerMapper.INSTANCE.computerToComputerDto(computer));
        }
        
        return mav;
    }

    @GetMapping("/addComputer")
    public ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addComputer");
        
        manageComputerUtils.setCompanyDtoListInMAV(LOGGER, mav);

        return mav;
    }

}
