package com.excilys.formation.cdb.paginator;

import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.ComputerService;

public class ComputerPage extends Page<Computer> {

	protected void refreshContent() {
		setContent(ComputerService.INSTANCE.getList(offset, nbPerPage));
	}

}
