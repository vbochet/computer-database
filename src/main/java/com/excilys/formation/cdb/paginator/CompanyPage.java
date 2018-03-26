package com.excilys.formation.cdb.paginator;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.service.CompanyService;

public class CompanyPage extends Page<Company> {

    @Override
    protected void refreshContent() {
        setContent(CompanyService.INSTANCE.getList(getOffset(), getNbPerPage()));
    }

}
