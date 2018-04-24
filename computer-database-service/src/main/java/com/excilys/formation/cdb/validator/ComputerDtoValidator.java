package com.excilys.formation.cdb.validator;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.formation.cdb.dto.ComputerDto;

@Component
public class ComputerDtoValidator implements Validator {

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerDtoValidator.class);

    public boolean validateDates(ComputerDto computerDto) {
        if(computerDto.getComputerDiscontinued() == null || computerDto.getComputerDiscontinued().isEmpty()) {
            return true;
        }
        if(computerDto.getComputerIntroduced() != null && !computerDto.getComputerIntroduced().isEmpty()) {
            if (((LocalDate.parse(computerDto.getComputerIntroduced()).isAfter(LocalDate.parse(computerDto.getComputerDiscontinued()))))) {
                LOGGER.error("The discontinuation date must be greater than the introduction date!");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> paramClass) {
        return ComputerDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "computerName", "isRequiredError");
        ComputerDto computerDTO = (ComputerDto) object;
        if (!validateDates(computerDTO)) {
            errors.rejectValue("computerIntroduced", "isAfterDiscontError");
        }
    }

}
