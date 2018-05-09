package com.excilys.formation.cdb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;

@RestController
@Component("restComputerControllerBean")
@RequestMapping("/api/computer")
public class ComputerController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ComputerMapper computerMapper;
    @Autowired
    CompanyMapper companyMapper;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

    @GetMapping(value = "")
    public ResponseEntity<List<ComputerDto>> getComputerList(@RequestParam Optional<Integer> limit, @RequestParam Optional<Integer> offset) {
        int limit_ = limit.isPresent() ? limit.get() : (int)computerService.getNbFound();
        int offset_ = offset.isPresent() ? offset.get() : 0;
        List<Computer> computerList = computerService.getList(offset_, limit_, "id", false);
        List<ComputerDto> computerDtoList = new ArrayList<>();

        for(Computer computer : computerList) {
            computerDtoList.add(computerMapper.computerToComputerDto(computer));
        }

        return new ResponseEntity<>(computerDtoList, HttpStatus.OK);
    }

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

    @PostMapping(value = "")
    public ResponseEntity<ComputerDto> addComputer(@RequestParam String name, @RequestParam String introduced, @RequestParam String discontinued, @RequestParam String companyId) {
        LocalDate introducedLD = null;
        LocalDate discontinuedLD = null;
        Optional<Company> companyOpt;
        Company company;

        try {
            introducedLD = introduced.isEmpty() ?  null : LocalDate.parse(introduced);
            discontinuedLD = discontinued.isEmpty() ?  null : LocalDate.parse(discontinued);
            if (companyId.isEmpty()) {
                company = null;
            }
            else {
                companyOpt = companyService.getById(Long.parseLong(companyId));
                if(!companyOpt.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                else {
                    company = companyOpt.get();
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

       Computer computer = new Computer(0, name, introducedLD, discontinuedLD, company);
       ComputerDto computerDto = computerMapper.computerToComputerDto(computerService.createComputer(computer));

       return new ResponseEntity<>(computerDto,HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ComputerDto> editComputer(@PathVariable long id, @RequestParam String name, @RequestParam String introduced, @RequestParam String discontinued, @RequestParam String companyId) {
        LocalDate introducedLD = null;
        LocalDate discontinuedLD = null;
        Optional<Company> companyOpt;
        Company company;

        try {
            introducedLD = introduced.isEmpty() ?  null : LocalDate.parse(introduced);
            discontinuedLD = discontinued.isEmpty() ?  null : LocalDate.parse(discontinued);
            if (companyId.isEmpty()) {
                company = null;
            }
            else {
                companyOpt = companyService.getById(Long.parseLong(companyId));
                if(!companyOpt.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                else {
                    company = companyOpt.get();
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

       Computer computer = new Computer(id, name, introducedLD, discontinuedLD, company);
       ComputerDto computerDto = computerMapper.computerToComputerDto(computerService.updateComputer(computer));

       return new ResponseEntity<>(computerDto,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ComputerDto> deleteComputerById(@PathVariable long id) {
        if (computerService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            LOGGER.error("No computer matching id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
