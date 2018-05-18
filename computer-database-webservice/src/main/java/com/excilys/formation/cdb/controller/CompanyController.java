package com.excilys.formation.cdb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.service.CompanyService;

@RestController
@Component("restCompanyControllerBean")
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyMapper companyMapper;

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

    @GetMapping(value = "")
    public ResponseEntity<List<CompanyDto>> getCompanyList(@RequestParam Optional<Integer> limit, @RequestParam Optional<Integer> offset) {
        int limit_ = limit.isPresent() ? limit.get() : (int)companyService.getNbFound();
        int offset_ = offset.isPresent() ? offset.get() : 0;
        List<Company> companyList = companyService.getList(offset_, limit_);
        List<CompanyDto> companyDtoList = new ArrayList<>();

        for(Company company : companyList) {
            companyDtoList.add(companyMapper.companyToCompanyDto(company));
        }

        return new ResponseEntity<>(companyDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable long id) {
        CompanyDto companyDto;
        Optional<Company> optCpt = companyService.getById(id);

        if (optCpt.isPresent()) {
            companyDto = companyMapper.companyToCompanyDto(optCpt.get());
            return new ResponseEntity<>(companyDto, HttpStatus.OK);
        } else {
            LOGGER.error("No company matching id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<CompanyDto> addCompany(@RequestParam String name) {
        Company company = new Company(0, name);
        CompanyDto companyDto = companyMapper.companyToCompanyDto(companyService.createCompany(company));

        return new ResponseEntity<>(companyDto,HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CompanyDto> editCompany(@PathVariable long id, @RequestParam String name) {
        Optional<Company> optCpt = companyService.getById(id);

        if (optCpt.isPresent()) {
            Company company = new Company(id, name);
            CompanyDto companyDto = companyMapper.companyToCompanyDto(companyService.updateCompany(company));

            return new ResponseEntity<>(companyDto,HttpStatus.OK);
        } else {
            LOGGER.error("No company matching id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CompanyDto> deleteCompanyById(@PathVariable long id) {
        if (companyService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            LOGGER.error("No company matching id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
