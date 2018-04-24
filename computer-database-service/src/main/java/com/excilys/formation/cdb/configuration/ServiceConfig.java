package com.excilys.formation.cdb.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.excilys.formation.cdb.service",
                "com.excilys.formation.cdb.validator"})
public class ServiceConfig {
}
