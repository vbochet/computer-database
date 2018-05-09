package com.excilys.formation.cdb.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.formation.cdb.configuration.CliConfig;
import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;
import com.excilys.formation.cdb.service.ComputerService;

@Component("cliBean")
public class Cli {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ComputerMapper computerMapper;

    static Logger LOGGER = LoggerFactory.getLogger(Cli.class);

    private Client client = ClientBuilder.newClient();
    private WebTarget computerWebTarget = client.target("http://localhost:8080/computer-database-webservice/api/computer");
    private WebTarget companyWebTarget = client.target("http://localhost:8080/computer-database-webservice/api/company");

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CliConfig.class);
        Cli cli = context.getBean(Cli.class);
        LOGGER.debug("Starting Computer-Database command line interface");

        System.out.println("COMPUTER DATABASE\n-----------------\n");

        Boolean stop = false;
        Scanner scanner = new Scanner(System.in);
        int i;

        while (!stop) {
            try {
                cli.printMenu();
                System.out.print("Your choice: ");
                i = scanner.nextInt();
                if (i < CliMenuChoices.values().length) {
                    switch(CliMenuChoices.values()[i]) {
                    case QUIT:
                        System.out.println("Received exit signal");
                        stop = true;
                        break;

                    case LIST_COMPUTERS:
                        cli.caseListComputer(scanner);
                        break;

                    case LIST_COMPANIES:
                        cli.caseListCompany(scanner);
                        break;

                    case READ_COMPUTER:
                        cli.caseShowComputer(scanner);
                        break;

                    case CREATE_COMPUTER:
                        cli.caseCreateComputer(scanner);
                        break;

                    case UPDATE_COMPUTER:
                        cli.caseUpdateComputer(scanner);
                        break;

                    case DELETE_COMPUTER:
                        cli.caseDeleteComputer(scanner);
                        break;

                    case DELETE_COMPANY:
                        cli.caseDeleteCompany(scanner);
                        break;

                    default:
                        System.out.println("Option " + i + " is unknown");
                    }
                } else {
                    System.out.println("Option " + i + " is unknown");
                    LOGGER.debug("Invalid option in CLI menu: {}", i);
                }
            } catch(InputMismatchException e) {
                String input = scanner.nextLine();
                LOGGER.error("Input error: Unexpected value \"{}\" received", input);
                stop = true;
            }
        }

        System.out.println("Terminating...");
        LOGGER.debug("Stopping Computer-Database command line interface");
        scanner.close();
        context.close();
    }

    private void printMenu() {
        System.out.println("Select the action you want to perform in the list below:");

        StringBuilder sb = new StringBuilder();
        sb.append(CliMenuChoices.LIST_COMPUTERS.ordinal())
            .append(". List computers \t\t ")
            .append(CliMenuChoices.LIST_COMPANIES.ordinal())
            .append(". List companies\n")
            .append(CliMenuChoices.READ_COMPUTER.ordinal())
            .append(". Show computer details \t ")
            .append(CliMenuChoices.CREATE_COMPUTER.ordinal())
            .append(". Create a computer\n")
            .append(CliMenuChoices.UPDATE_COMPUTER.ordinal())
            .append(". Update a computer \t\t ")
            .append(CliMenuChoices.DELETE_COMPUTER.ordinal())
            .append(". Delete a computer\n")
            .append(CliMenuChoices.DELETE_COMPANY.ordinal())
            .append(". Delete a company (and related computers)\n")
            .append(CliMenuChoices.QUIT.ordinal())
            .append(". Exit\n");
        System.out.println(sb.toString());
    }

    private void caseListComputer(Scanner scanner) {
        LOGGER.debug("User choice: List computers");

        int nbToPrint = getNbToPrint(scanner);
        int offset = getOffset(scanner);
        LOGGER.debug("(print {} computers from id {})", nbToPrint, offset);

        WebTarget computerWebTarget = this.computerWebTarget.queryParam("limit",nbToPrint).queryParam("offset",offset);
        Invocation.Builder invocationBuilder = computerWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get(Response.class);
        List<ComputerDto> result = response.readEntity(new GenericType<List<ComputerDto>>() {});

        for(ComputerDto dto : result) {
          System.out.println(dto.toString());
        }

        LOGGER.debug("End of computer listing");
    }

    private void caseListCompany(Scanner scanner) {
        LOGGER.debug("User choice: List companies");

        int nbToPrint = getNbToPrint(scanner);
        int offset = getOffset(scanner);
        LOGGER.debug("(print {} companies from id {})", nbToPrint, offset);

        WebTarget companyWebTarget = this.companyWebTarget.queryParam("limit",nbToPrint).queryParam("offset",offset);
        Invocation.Builder invocationBuilder = companyWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get(Response.class);
        List<CompanyDto> result = response.readEntity(new GenericType<List<CompanyDto>>() {});

        for(CompanyDto dto : result) {
          System.out.println(dto.toString());
        }

        LOGGER.debug("End of company listing");
    }

    private void caseShowComputer(Scanner scanner) {
        LOGGER.debug("User choice: Show computer info");
        long id = getId(scanner);
        LOGGER.debug("Computer's id: {}", id);

        WebTarget computerWebTarget = this.computerWebTarget.path(String.valueOf(id));
        Invocation.Builder invocationBuilder = computerWebTarget.request(MediaType.APPLICATION_JSON);
        ComputerDto response = invocationBuilder.get(ComputerDto.class);

        System.out.println(response.toString());

        LOGGER.debug("End of computer info");
    }

    private void caseCreateComputer(Scanner scanner) {
        LOGGER.debug("User choice: Create new computer");
        String name;
        String intro;
        String discont;
        String companyIdStr = null;
        long companyId;
        boolean loop = true;
        Computer computer = new Computer();


        name = getName(scanner);
        LOGGER.debug("Name: {}", name);
        computerService.setName(name, computer);

        intro = getIntroDate(scanner);
        while (!computerService.setIntroDate(intro, computer)) {
            intro = getIntroDate(scanner);
        }
        LOGGER.debug("Introduced: {}", intro);

        discont = getDiscontDate(scanner);
        while (!computerService.setDiscontDate(discont, computer)) {
            discont = getDiscontDate(scanner);
        }
        LOGGER.debug("Discontinued: {}", discont);

        while (loop) {
            System.out.print("Company's id (if none, leave empty): ");
            try {
                companyIdStr = scanner.nextLine();
                if (!companyIdStr.isEmpty()) {
                    companyId = Integer.parseInt(companyIdStr);
                    Optional<Company> optCompany = companyService.getById(companyId);
                    LOGGER.debug("Company id: {}", companyId);
                    if (optCompany.isPresent()) {
                        computer.setCompany(optCompany.get());
                        System.out.println("Company found: " + optCompany.get().getName());
                        LOGGER.debug("Company found: {}",optCompany.get().getName());
                    } else {
                        System.out.println("No company matches this id");
                        LOGGER.debug("No company matches this id");
                    }
                } else {
                    LOGGER.debug("Company id: null");
                }
                loop = false;
            } catch (NumberFormatException e) {
                LOGGER.error("Input error: Unexpected value \"{}\" received", companyIdStr);
            }
        }

        ComputerDto computerDto = computerMapper.computerToComputerDto(computer);
        WebTarget computerWebTarget = this.computerWebTarget.queryParam("name",computerDto.getComputerName())
                .queryParam("introduced",computerDto.getComputerIntroduced() == null ? "" : computerDto.getComputerIntroduced())
                .queryParam("discontinued",computerDto.getComputerDiscontinued() == null ? "" : computerDto.getComputerDiscontinued())
                .queryParam("companyId",computerDto.getComputerCompanyId());
        Invocation.Builder invocationBuilder = computerWebTarget.request(MediaType.APPLICATION_JSON);
        ComputerDto response = invocationBuilder.post(Entity.entity(new Form(),MediaType.APPLICATION_FORM_URLENCODED_TYPE), ComputerDto.class);

        System.out.println(response.toString());
        LOGGER.debug("End of computer creation");
    }

    private void caseUpdateComputer(Scanner scanner) {
        LOGGER.debug("User choice: Update computer");
        long id;
        Computer computer;
        Optional<Computer> optComputer;

        id = getId(scanner);
        LOGGER.debug("Computer's id: {}", id);
        optComputer = computerService.getById(id);
        if (!optComputer.isPresent()) {
            System.out.println("No computer matching this id");
            LOGGER.warn("No computer matching id {}", id);
            return;
        } else {
            computer = optComputer.get();
        }

        updateComputerName(scanner, computer);
        updateComputerIntroduced(scanner, computer);
        updateComputerDiscontinued(scanner, computer);
        updateComputerCompany(scanner, computer);

        computer = computerService.updateComputer(computer);
        System.out.println(computer);
        LOGGER.debug("End of computer update");
    }

    private void updateComputerName(Scanner scanner, Computer computer) {
        String answer = "";
        String name;
        System.out.println("Current name: [" + computer.getName() + "].");

        answer = chooseUpdate(scanner, "name");

        if (answer.equals("y")) {
            name = getName(scanner);
            computerService.setName(name, computer);
        }
    }

    private void updateComputerIntroduced(Scanner scanner, Computer computer) {
        String answer = "";
        String intro;
        System.out.println("Current introduction date: [" + computer.getIntroduced() + "].");

        answer = chooseUpdate(scanner, "introduction date");

        if (answer.equals("y")) {
            intro = getIntroDate(scanner);
            while (!computerService.setIntroDate(intro, computer)) {
                intro = getIntroDate(scanner);
            }
        }
    }

    private void updateComputerDiscontinued(Scanner scanner, Computer computer) {
        String answer = "";
        String discont;
        System.out.println("Current discontinuation date: [" + computer.getDiscontinued() + "].");

        answer = chooseUpdate(scanner, "discontinuation date");

        if (answer.equals("y")) {
            discont = getDiscontDate(scanner);
            while (!computerService.setDiscontDate(discont, computer)) {
                discont = getDiscontDate(scanner);
            }
        }
    }

    private void updateComputerCompany(Scanner scanner, Computer computer) {
        String answer = "";
        String companyIdStr = null;
        boolean loop = true;
        long companyId;
        Company company = null;
        System.out.println("Current company: [" + computer.getCompany() + "].");

        answer = chooseUpdate(scanner, "company");

        if (answer.equals("y")) {
            while (loop) {
                System.out.print("Company's id (if none, leave empty): ");
                try {
                    companyIdStr = scanner.nextLine();
                    if (!companyIdStr.isEmpty()) {
                        companyId = Integer.parseInt(companyIdStr);
                        Optional<Company> optCompany = companyService.getById(companyId);
                        if (optCompany.isPresent()) {
                            company = optCompany.get();
                        }
                    }
                    loop = false;
                } catch (InputMismatchException e) {
                    LOGGER.error("Input error: Unexpected value \"{}\" received", companyIdStr);
                } finally {
                    computer.setCompany(company);
                }
            }
        }
    }

    private String chooseUpdate(Scanner scanner, String nameOfThingToUpdate) {
        String answer = "";
        while (!(answer.equals("y") || answer.equals("n"))) {
            System.out.print("Update " + nameOfThingToUpdate + " (y/n)? ");
            answer = scanner.nextLine();
        }
        return answer;
    }

    private void caseDeleteComputer(Scanner scanner) {
        LOGGER.debug("User choice: Delete computer");
        long id;

        id = getId(scanner);
        LOGGER.debug("Computer's id: {}", id);

        if (computerService.deleteById(id)) {
            System.out.println("Computer n°" + id + " has been successfully deleted");
            LOGGER.debug("Computer {} has been deleted", id);
        } else {
            System.out.println("A problem occured. Computer n°" + id + " couldn't be deleted");
            LOGGER.warn("Computer {} couldn't be deleted", id);
        }
        LOGGER.debug("End of computer deletion");
    }

    private void caseDeleteCompany(Scanner scanner) {
        LOGGER.debug("User choice: Delete company");
        long id;

        id = getId(scanner);
        LOGGER.debug("Company's id: {}", id);

        if (companyService.deleteById(id)) {
            System.out.println("Company n°" + id + " and its related computers have been successfully deleted");
            LOGGER.debug("Company {} and its related computers have been deleted", id);
        } else {
            System.out.println("A problem occured. Company n°" + id + " or one of its computers couldn't be deleted");
            LOGGER.warn("Company {} or one of its computers couldn't be deleted", id);
        }
        LOGGER.debug("End of computer deletion");
    }

    private int getNbToPrint(Scanner scanner) {
        int nbToPrint = 0;
        boolean stop = false;

        while (!stop) {
            try {
                System.out.print("How many elements a page should contain: ");
                nbToPrint = scanner.nextInt();
                stop = true;
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }

        return nbToPrint;
    }

    private int getOffset(Scanner scanner) {
        int offset = 0;
        boolean stop = false;

        while (!stop) {
            try {
                System.out.print("Indicate the index from which display elements: ");
                offset = scanner.nextInt();
                stop = true;
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }

        return offset;
    }

    private long getId(Scanner scanner) {
        long inputId = 0;
        boolean stop = false;

        while (!stop) {
            try {
                System.out.print("Id: ");
                inputId = scanner.nextLong();
                stop = true;
                scanner.nextLine();
            } catch(InputMismatchException e) {
                scanner.nextLine();
            }
        }

        return inputId;
    }

    private String getName(Scanner scanner) {
        String name = "";

        while (name.isEmpty()) {
            System.out.print("Computer's name (mandatory): ");
            name = scanner.nextLine();
        }

        return name;
    }

    private String getIntroDate(Scanner scanner) {
        System.out.print("Company's introduction date, with yyyy-MM-dd formatting (if none, leave empty): ");
        return scanner.nextLine();
    }

    private String getDiscontDate(Scanner scanner) {
        System.out.print("Company's discontinuation date, with yyyy-MM-dd formatting (if none, leave empty): ");
        return scanner.nextLine();
    }
}
