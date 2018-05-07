package com.excilys.formation.cdb.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;

@RestController
@Component("restComputerControllerBean")
@RequestMapping("/api/computer")
public class ComputerController {
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ComputerMapper computerMapper;
    @Autowired
    CompanyMapper companyMapper;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

    @GetMapping(value = "/{id}")
    public ResponseEntity<ComputerDto> getComputerById(@PathVariable long id) {
        ComputerDto computerDto;
        Optional<Computer> optCpt = computerService.getById(id);

        if (optCpt.isPresent()) {
            computerDto = computerMapper.computerToComputerDto(optCpt.get());
            return new ResponseEntity<>(computerDto, HttpStatus.OK);
        } else {
            LOGGER.error("No computer matching id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
