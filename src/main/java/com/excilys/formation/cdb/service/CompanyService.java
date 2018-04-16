package com.excilys.formation.cdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.formation.cdb.dao.CompanyDao;
import com.excilys.formation.cdb.model.Company;

@Service("companyServiceBean")
public class CompanyService {
    @Autowired
    private CompanyDao companyDao;

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    public List<Company> getList(int offset, int nbToPrint) {
        if (nbToPrint >= 1) {
            return companyDao.list(offset, nbToPrint);
        }

        return new ArrayList<>();
    }

    public Optional<Company> getById(long companyId) {
        Optional<Company> ret = Optional.empty();
        if (companyId > 0) {
            ret = companyDao.read(companyId);
        }

        return ret;
    }

    public Optional<Company> getByName(String companyName) {
        Optional<Company> ret = Optional.empty();
        if (companyName != null && !companyName.isEmpty()) {
            ret = companyDao.findByName(companyName);
        }

        return ret;
    }

    public boolean deleteById(long id) {
        if (id > 0) {
            companyDao.deleteById(id);
            return true;
        }

        return false;
    }

    public long getNbFound() {
        return companyDao.count();
    }
}
