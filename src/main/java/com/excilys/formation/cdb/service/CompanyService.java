package com.excilys.formation.cdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.dao.CompanyDaoImpl;
import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.exceptions.ServiceException;
import com.excilys.formation.cdb.model.Company;

public enum CompanyService {
    INSTANCE;

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    public List<Company> getList(int offset, int nbToPrint) throws ServiceException {
        if (nbToPrint >= 1) {
            try {
                return CompanyDaoImpl.INSTANCE.list(offset, nbToPrint);
            } catch (DaoException e) {
                LOGGER.error("Error while listing companies from {} to {}", offset, offset + nbToPrint, e);
                throw(new ServiceException("Error while listing companies from " + offset + " to " + (offset + nbToPrint), e));
            }
        }

        return new ArrayList<>();
    }

    public Optional<Company> getById(long companyId) throws ServiceException {
        Optional<Company> ret = Optional.empty();
        if (companyId > 0) {
            try {
                ret = CompanyDaoImpl.INSTANCE.read(companyId);
            } catch (DaoException e) {
                LOGGER.error("Error while reading details of company n°{} ", companyId, e);
                throw(new ServiceException("Error while reading details of company n°" + companyId, e));
            }
        }

        return ret;
    }

    public Optional<Company> getByName(String companyName) throws ServiceException {
        Optional<Company> ret = Optional.empty();
        if (companyName != null && !companyName.isEmpty()) {
            try {
                ret = CompanyDaoImpl.INSTANCE.findByName(companyName);
            } catch (DaoException e) {
                LOGGER.error("Error while counting computers in database", e);
                throw(new ServiceException("Error while counting computers in database", e));
            }
        }

        return ret;
    }

    public boolean deleteById(long id) throws ServiceException {
        if (id > 0) {
            try {
                CompanyDaoImpl.INSTANCE.deleteById(id);
            } catch (DaoException e) {
                LOGGER.error("Error while deleting company n°{}", id, e);
                throw(new ServiceException("Error while deleting company n°" + id, e));
            }
            return true;
        }

        return false;
    }

    public long getNbFound() throws ServiceException {
        try {
            return CompanyDaoImpl.INSTANCE.count();
        } catch (DaoException e) {
            LOGGER.error("Error while counting companies in database", e);
            throw(new ServiceException("Error while counting companies in database", e));
        }
    }
}
